package blog.security;


import blog.entity.User;
import blog.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static blog.security.SecurityConstants.*;

@Component
public class JwtProvider {

    private final UserService userService;

    @Value("${jwt.secret: d214y124eh1upr}")
    private String secretKey;
    @Value("${jwt.expiration: 3600000}")
    private long validityInMilliseconds;

    public JwtProvider(UserService userService) {
        this.userService = userService;
    }

    public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("user", username)
                .claim(CLAIM_ROLES_KEY, getAuthoritiesAsStringList(authorities))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 60 * 60 * 24 * 1000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("user", username)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    private static List<String> getAuthoritiesAsStringList(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public Authentication getAuthentication(String token) {
        User user = this.userService.findByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getRoles());
    }

    public Authentication getRefreshAuthentication(String token) {
        User user = this.userService.findByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), Collections.emptyList());
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String resolveRefreshToken(HttpServletRequest req) {
        return doResolveToken(req, REFRESH_TOKEN_HEADER, REFRESH_TOKEN_PREFIX);
    }

    public String resolveToken(HttpServletRequest req) {
        return doResolveToken(req, TOKEN_HEADER, TOKEN_PREFIX);
    }

    public String doResolveToken(HttpServletRequest req, String header, String prefix) {
        String bearerToken = req.getHeader(header);

        if (bearerToken == null || !bearerToken.startsWith(prefix)) {
            return null;
        }

        return bearerToken.substring(prefix.length());
    }

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty())
            return false;
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (NullPointerException | JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

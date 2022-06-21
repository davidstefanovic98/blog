package blog.security;

import blog.bean.CustomPasswordEncoder;
import blog.entity.User;
import blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Properties;

import static blog.util.StringUtils.falsy;

@Component
@RequiredArgsConstructor
@Primary
public class AuthenticationManagerImpl implements AuthenticationManager {
    private final UserRepository userRepository;
    private final Environment env;
    private final CustomPasswordEncoder passwordEncoder;

    @Resource
    private Properties errors;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        Object password = authentication.getCredentials();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(errors.getProperty("auth.login.user-not-found")));

        if (user.isDisabled())
            throw new DisabledException(errors.getProperty("auth.login.user-disabled"));

        if (falsy((String)password))
            throw new BadCredentialsException(errors.getProperty("auth.login.invalid-credentials"));

        if (!passwordEncoder.getEncoder().matches((String) password, user.getPassword()))
            throw new BadCredentialsException(errors.getProperty("auth.login.invalid-credentials"));

        if (user.isCredentialsExpired())
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());

        return new UsernamePasswordAuthenticationToken(username, password, user.getRoles());
    }
}

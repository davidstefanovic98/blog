package blog.service.impl;

import blog.data.ChangePasswordDTO;
import blog.data.RegisterUserDTO;
import blog.entity.Role;
import blog.entity.User;
import blog.entity.domain.RecordStatus;
import blog.exception.EmailValidationException;
import blog.exception.InvalidPasswordException;
import blog.exception.PasswordMismatchException;
import blog.exception.PasswordValidationException;
import blog.repository.UserRepository;
import blog.service.UserService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static blog.entity.Role.USER_ROLE;
import static blog.entity.domain.RecordStatus.*;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // 8-40 chars long, at least one number, one lower, one upper
    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+).{8,40}$");
    //(?=.*[@#$%^&+=]) for special characters
    private final UserRepository userRepository;

    protected UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Role> findAllRolesById(Integer userId) {
        return findById(userId).getRoles();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user found with this username: " + username));
    }

    @Override
    public void changePassword(User user, ChangePasswordDTO passwordDto) {
        if (user.getRecordStatus() == EXPIRED) {
            if (!passwordDto.isValid())
                throw new PasswordMismatchException();
            user.setRecordStatus(ACTIVE);
            if (!user.getRoles().contains(USER_ROLE))
                user.getRoles().add(USER_ROLE);
        } else {
            if (!passwordDto.isValid())
                throw new PasswordMismatchException();
            if (!passwordEncoder.matches(passwordDto.getPrevious(), user.getPassword()))
                throw new InvalidPasswordException();
        }

        validatePassword(passwordDto.getPassword());

        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));

        userRepository.save(user);
    }

    @Override
    public void disableUser(Integer userId) {
        setUserStatus(userId, DISABLED);
    }

    @Override
    public User register(RegisterUserDTO dto) {
        User user = makeUser(dto);
        user.getRoles().add(USER_ROLE);
        return userRepository.save(user);
    }

    @Override
    public User resetPassword(Integer userId) {
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(user.getDefaultPassword()));
        user.setRecordStatus(EXPIRED);
        return userRepository.save(user);
    }

    @Override
    public void enableUser(Integer userId) {
        setUserStatus(userId, ACTIVE);
    }

    private static void validatePassword(String passwordStr) {
        if (passwordStr == null)
            throw new PasswordValidationException();
        Matcher matcher = PASSWORD_REGEX.matcher(passwordStr);
        if (!matcher.matches())
            throw new PasswordValidationException();
    }

    private static void validateEmail(String emailStr) {
        Matcher matcher = EMAIL_REGEX.matcher(emailStr);
        if (!matcher.find())
            throw new EmailValidationException();
    }

    private void setUserStatus(Integer userId, RecordStatus recordStatus) {
        User user = findById(userId);
        user.setRecordStatus(recordStatus);
        userRepository.save(user);
    }

    private User makeUser(RegisterUserDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirm()))
            throw new PasswordMismatchException();
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword());

        User user = new User();
        user.setUsername(dto.getUsername().toLowerCase(Locale.ROOT).trim());
        user.setDisplayName(user.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName().trim());
        user.setLastName(dto.getLastName().trim());
        user.setEmail(dto.getEmail().trim());
        user.setRoles(new ArrayList<>());
        return user;
    }
}

package blog.data;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private String confirm;
    private String email;
    private String firstName;
    private String lastName;
}

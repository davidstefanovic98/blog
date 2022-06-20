package blog.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static blog.util.StringUtils.falsy;

@Setter
@NoArgsConstructor
public class ChangePasswordDTO {
    
    private String password;
    private String confirm;
    private String previous;

    @JsonIgnore
    public boolean isValid() {
        if (falsy(confirm) || falsy(password))
            return false;
        return password.equals(confirm);
    }

    public String getConfirm() {
        return confirm;
    }

    public String getPassword() {
        return password;
    }

    public String getPrevious() {
        return previous;
    }
}

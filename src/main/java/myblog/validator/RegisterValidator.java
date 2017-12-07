package myblog.validator;

import myblog.user.User;
import myblog.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegisterValidator implements Validator {

    private final UserService userService;

    @Autowired
    public RegisterValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public void validate(Object o, Errors errors) {
        User user = (User) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Empty.User.Username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Empty.User.Password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Empty.User.Email");
        if (user.getUsername().length() < 5 || user.getUsername().length() > 20) { //username min-5 max-20
            errors.rejectValue("username", "Size.User.Username");
        } else if (userService.loadUserByUsername(user.getUsername()) != null) { //if username already exists
            errors.rejectValue("username", "Duplicate.User.Username");
        } else if (user.getPassword().length() < 8 || user.getPassword().length() > 64) { // password min-8 max-64
            errors.rejectValue("password", "Size.User.Password");
        } else if (!user.getPasswordConfirmation().equals(user.getPassword())) { //if pass = passConfirmation
            errors.rejectValue("passwordConfirmation", "Diff.User.Password");
        } else if (user.getEmail().length() > 2 && user.getEmail().length() <= 64) { // email min-3 max-64
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                errors.rejectValue("email", "Pattern.User.Email"); // don't match the pattern *@*.*
            }
            if (userService.loadUserByEmail(user.getEmail()) != null) {
                errors.rejectValue("email", "Duplicate.User.Email"); // email already exists
            }
        } else if (user.getEmail().length() <= 2 || user.getEmail().length() > 64) { // email max-64
            errors.rejectValue("email", "Size.User.Email");
        }
    }
}

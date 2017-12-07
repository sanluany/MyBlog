package myblog.validator;

import myblog.user.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Empty.User.Username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Empty.User.Password");
        if (user.getUsername().length() < 5 || user.getUsername().length() > 20) { //username min-5 max-20
            errors.rejectValue("username", "Size.User.Username");
        } else if (user.getPassword().length() < 8 || user.getPassword().length() > 64) { // password min-8 max-64
            errors.rejectValue("password", "Size.User.Password");
        }
    }
}

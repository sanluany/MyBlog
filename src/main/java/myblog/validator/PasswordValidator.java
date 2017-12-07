package myblog.validator;

import myblog.misc.PasswordHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordHolder.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordHolder passwordHolder = (PasswordHolder) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "previousPassword", "Empty.User.Password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "Empty.User.Password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirmation", "Empty.User.Password");
        if (passwordHolder.getPreviousPassword().length() < 8 || passwordHolder.getPreviousPassword().length() > 64) { // password min-8 max-64
            errors.rejectValue("previousPassword", "Size.User.Password");
        } else if (passwordHolder.getNewPassword().length() < 8 || passwordHolder.getNewPassword().length() > 64) { // password min-8 max-64
            errors.rejectValue("newPassword", "Size.User.Password");
        } else if (!passwordHolder.getPasswordConfirmation().equals(passwordHolder.getNewPassword())) { //if pass = passConfirmation
            errors.rejectValue("passwordConfirmation", "Diff.User.Password");
        }
    }
}

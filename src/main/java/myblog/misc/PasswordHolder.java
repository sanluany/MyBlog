package myblog.misc;

import org.springframework.stereotype.Component;

@Component
public class PasswordHolder {

    private String previousPassword;
    private String newPassword;
    private String passwordConfirmation;

    public PasswordHolder(String previousPassword, String newPassword, String passwordConfirmation) {
        this.previousPassword = previousPassword;
        this.newPassword = newPassword;
        this.passwordConfirmation = passwordConfirmation;
    }
    public PasswordHolder(){}
    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }


}

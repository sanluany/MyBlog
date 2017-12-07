package myblog.web;

import myblog.user.User;
import myblog.user.UserService;
import myblog.user.info.UserInfoService;
import myblog.validator.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
public class RegisterController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final RegisterValidator registerValidator;

    @Autowired
    public RegisterController(RegisterValidator registerValidator, UserService userService, UserInfoService userInfoService) {
        this.registerValidator = registerValidator;
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/register", method = POST)
    public String processRegistrationForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) throws Exception {
        registerValidator.validate(user,bindingResult);
        if(bindingResult.hasErrors()){
            return "register";
        }
        userService.add(user);
        userService.addUserRole(user.getUsername());
        userInfoService.add(userInfoService.createFromUser(user));
        model.addAttribute("username",user.getUsername());
        return "redirect:/{username}";
    }
}

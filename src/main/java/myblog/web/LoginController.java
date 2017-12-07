package myblog.web;

import myblog.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = GET)
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("successLogout", true);
        } else {
            if (error != null) {
                model.addAttribute("error", true);
            }
        }
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("USER")) {
            return "profile";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }

    @RequestMapping(value = "/login", method = POST)
    public String processLoginForm() {
        return "profile";
    }

}

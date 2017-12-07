package myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = POST)
    public String logout(RedirectAttributes model){
        model.addFlashAttribute("successLogout",true);
        return "redirect:/login?logout";
    }
}

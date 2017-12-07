package myblog.web;

import myblog.file.FileService;
import myblog.user.User;
import myblog.user.info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class SettingsController {

    private final FileService fileService;

    private final UserInfoService userInfoService;

    private MessageSource messageSource;

    @Autowired
    public SettingsController(FileService fileService, UserInfoService userInfoService, MessageSource messageSource) {
        this.fileService = fileService;
        this.userInfoService = userInfoService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/settings", method = GET)
    public String showSettings(@RequestParam(value = "security", required = false) String security,
                               @RequestParam(name = "act", required = false) String act,
                               Model model) {
        model.addAttribute("activeUserInfo", userInfoService.getActiveUserInfo());
        model.addAttribute("passwordHolder", new User());
        if (act != null && act.equalsIgnoreCase("profile")) {
            return "fragments/dynamicFragments :: profileSettings";
        }
        if (act != null && act.equalsIgnoreCase("security")) {
            model.addAttribute("passHolder", new User());
            return "fragments/dynamicFragments :: securitySettings";
        }
        return "settings";
    }

    @RequestMapping(value = "/settings", method = POST)
    public String processSettings(@RequestParam(name = "image", required = false) String image, @RequestParam(name = "act",required = false) String act,
                                  @RequestParam(name = "firstName", required = false) String firstName, @RequestParam(name = "lastName", required = false) String lastName,Model model) {
        if (image != null) {
            fileService.safe(image, userInfoService.getActiveUserInfo());
        }
        if (act!=null && act.equalsIgnoreCase("doChangeName")) {
            userInfoService.changeName(userInfoService.getActiveUserInfo(), firstName, lastName);
            model.addAttribute("isError", false);
            model.addAttribute("msg", messageSource.getMessage("Message.Settings.ChangedName", null, LocaleContextHolder.getLocale()));
            return "fragments/dynamicFragments :: message";
        }
        return "redirect:/settings";
    }
}

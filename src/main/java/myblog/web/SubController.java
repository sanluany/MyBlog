package myblog.web;

import myblog.user.info.UserInfo;
import myblog.user.info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class SubController {

    private final UserInfoService userInfoService;

    @Autowired
    public SubController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @RequestMapping(value = "/subscriptions",method = GET)
    public String showSubscriptions(@RequestParam(name = "act",required = false) String act, Model model){
        UserInfo activeUser = userInfoService.getActiveUserInfo();
        userInfoService.fillSubsWithAvatars(activeUser);
        model.addAttribute("activeUser",activeUser);
        if( act !=null && act.equalsIgnoreCase("subscriptions")){
            return "fragments/dynamicFragments :: subscriptionsMenu";
        }
        if(act !=null && act.equalsIgnoreCase("subscribers")){
            return "fragments/dynamicFragments :: subscribersMenu";
        }
        return "people";
    }

}

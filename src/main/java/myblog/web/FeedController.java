package myblog.web;

import myblog.post.PostService;
import myblog.user.info.UserInfo;
import myblog.user.info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class FeedController {

    private final UserInfoService userInfoService;

    private final PostService postService;

    @Autowired
    public FeedController(UserInfoService userInfoService, PostService postService) {
        this.userInfoService = userInfoService;
        this.postService = postService;
    }

    @RequestMapping(value = "/feed", method = GET)
    public String showFeed(Model model){
        UserInfo activeUser = userInfoService.getActiveUserInfo();
        model.addAttribute("activeUser",activeUser);
        model.addAttribute("feed",postService.getFeed(activeUser));
        model.addAttribute("userInfoService",userInfoService);
        return "feed";
    }
}

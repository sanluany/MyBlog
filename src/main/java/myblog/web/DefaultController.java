package myblog.web;

import myblog.post.Post;
import myblog.post.PostService;
import myblog.user.info.UserInfo;
import myblog.user.info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class DefaultController {

    private final UserInfoService userInfoService;

    private final PostService postService;

    @Autowired
    public DefaultController(UserInfoService userInfoService, PostService postService) {
        this.userInfoService = userInfoService;
        this.postService = postService;
    }

    @RequestMapping(value = "/", method = GET)
    public String showPageByDefault(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            model.addAttribute("username", userInfoService.getActiveUserInfo().getUsername());
            return "redirect:/{username}";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/{username}", method = GET)
    public String showProfile(@PathVariable String username, Model model) {
        UserInfo activeUser = userInfoService.getActiveUserInfo();
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("posts",postService.loadPostsByUsername(username));
        model.addAttribute("postService",postService);
        if (activeUser.getUsername().equalsIgnoreCase(username)) { // check if user is owner of the page or guest
            userInfoService.setUserAvatarPath(activeUser);
            userInfoService.setUserRepostsAvatar(activeUser);
            model.addAttribute("user_role", "owner");
            model.addAttribute("newPost", new Post());
        } else {
            UserInfo passiveUser = userInfoService.load(username);
            if (passiveUser != null) {
                model.addAttribute("user_role", "guest");
                userInfoService.setUserAvatarPath(passiveUser);
                userInfoService.setUserRepostsAvatar(passiveUser);
                model.addAttribute("passiveUser", passiveUser);
            }
        }
        return "profile";
    }

    @RequestMapping(value = "/{username}", method = POST)
    public String processProfile(@PathVariable String username, @ModelAttribute(name = "activeUser") UserInfo activeUser,
                                 @ModelAttribute(name = "post") Post post) {
        postService.add(post, activeUser);
        return "redirect:/{username}";
    }




}

package myblog.web;

import myblog.misc.PasswordHolder;
import myblog.post.PostService;
import myblog.user.UserService;
import myblog.user.info.UserInfo;
import myblog.user.info.UserInfoService;
import myblog.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ActionController {

    private final UserInfoService userInfoService;

    private final UserService userService;

    private final PostService postService;

    private final PasswordValidator passwordValidator;

    private final MessageSource messageSource;

    @Autowired
    public ActionController(PasswordValidator passwordValidator, PostService postService, MessageSource messageSource, UserInfoService userInfoService, UserService userService) {
        this.passwordValidator = passwordValidator;
        this.postService = postService;
        this.messageSource = messageSource;
        this.userInfoService = userInfoService;
        this.userService = userService;
    }


    @RequestMapping(value = "/action", method = GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String likeAction(@RequestParam(name = "act") String act, @RequestParam(name = "postId", required = false) Long postId
            , @RequestParam(name = "subReqId", required = false) Long subscribeRequestId) {

        UserInfo activeUser = userInfoService.getActiveUserInfo();
        if (act.equalsIgnoreCase("doLike")) {
            postService.toggleAction(postId, activeUser, true);
            return String.valueOf(postService.loadPostById(postId).getLikes().size());
        }
        if (act.equalsIgnoreCase("doRepost")) {
            postService.toggleAction(postId, activeUser, false);
            return String.valueOf(postService.loadPostById(postId).getReposts().size());
        }
        if (act.equalsIgnoreCase("doRemove")) {
            postService.removePost(activeUser, postId);
        }
        if (act.equalsIgnoreCase("doSubscribe")) {
            userInfoService.toggleSubscribe(activeUser, subscribeRequestId);
            if (activeUser.isSubscribed(userInfoService.load(subscribeRequestId))) {
                return "{\"isSubscribed\":\"" + true + "\"," +
                        "\"btnText\":\"" + messageSource.getMessage("Text.Profile.Unsubscribe", null, LocaleContextHolder.getLocale()) + "\"}";
            }
            return "{\"isSubscribed\":\"" + false + "\"," +
                    "\"btnText\":\"" + messageSource.getMessage("Text.Profile.Subscribe", null, LocaleContextHolder.getLocale()) + "\"}";
        }
        return null;
    }

    @RequestMapping(value = "/action", method = POST)
    public String postActions(@RequestParam(name = "act") String act
            , @Valid @RequestBody PasswordHolder passwordHolder, BindingResult bindingResult, Model model) {
        boolean isError;
        String msg;
        if (act.equalsIgnoreCase("doChangePass")) {
            passwordValidator.validate(passwordHolder, bindingResult);
            if (bindingResult.hasErrors()) {
                isError = true;
                msg = messageSource.getMessage(bindingResult.getFieldError(), null);
            } else {
                try {
                    userService.changePassword(passwordHolder, userService.getActiveUser());
                    isError = false;
                    msg = messageSource.getMessage("Diff.User.Pass", null, LocaleContextHolder.getLocale());
                } catch (Exception e) {
                    isError = true;
                    msg = messageSource.getMessage("Message.Settings.ChangedPass", null, LocaleContextHolder.getLocale());
                }
            }
            model.addAttribute("isError", isError);
            model.addAttribute("msg", msg);
            return "fragments/dynamicFragments :: message";
        }
        return null;
    }


}

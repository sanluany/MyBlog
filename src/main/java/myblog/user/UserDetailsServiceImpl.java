package myblog.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        myblog.user.User user = userService.loadUserByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User "+username+" not found");
        GrantedAuthority authority = new SimpleGrantedAuthority(userService.loadUserRole(username));
        return (UserDetails) new User(user.getUsername(), user.getPassword(), Collections.singletonList(authority));
    }
}

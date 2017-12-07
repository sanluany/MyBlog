package myblog.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CharacterEncodingFilter characterEncodingFilter;
    private final UserDetailsService userDetailsService;
    private final BasicDataSource dataSource;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, BasicDataSource dataSource, CharacterEncodingFilter characterEncodingFilter) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
        this.characterEncodingFilter = characterEncodingFilter;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(characterEncodingFilter, CsrfFilter.class);
        http.requiresChannel()
                .antMatchers("/**").requiresSecure();
        http.authorizeRequests()
                .antMatchers("/resources/**", "/home", "/login", "/register").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout().logoutUrl("/logout").deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .logoutSuccessUrl("/login?logout")
                .and()
                .csrf()
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(1209600);
                http.sessionManagement()
                .sessionFixation()
                .none();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

package edu.asu.cassess.config;


import edu.asu.cassess.security.RestUnauthorizedEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("edu.asu.cassess.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String REMEMBER_ME_KEY = "rememberme_key";
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RestUnauthorizedEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler restAccessDeniedHandler;
    @Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;
    @Autowired
    private RememberMeServices rememberMeServices;

    public SecurityConfig() {
        super();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    //new resource locations must be added here
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/index.html", "/login.html", "/register.html",
                "/partials/**", "/template/**", "/", "/error/**", "/user", "/register", "/check_courseaccess", "/check_teamaccess", "/check_studentaccess");
    }

    //the .formLogin() method defines the location Spring processes authentication when
    //a POST is received at that path
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/failure").permitAll()
                .antMatchers("/users").permitAll()
                .antMatchers("/rest/**").hasAnyAuthority("rest", "super_user","admin")
                .antMatchers("/v2/api-docs").hasAnyAuthority("super_user")
                .antMatchers("/users/**").hasAnyAuthority("super_user")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
                .and()
                .formLogin()
                .loginProcessingUrl("/authenticate")
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(restAuthenticationFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices)
                .key(REMEMBER_ME_KEY)
                .and();
    }

    //Using BCrypt password encryption
    @Bean
    public DaoAuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}

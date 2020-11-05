package com.group1_cms.cms_antiques.configurations;

import com.group1_cms.cms_antiques.repositories.UserRepository;
import com.group1_cms.cms_antiques.services.UserService;
import com.group1_cms.cms_antiques.spring.security.CustomAuthenticationSuccessHandler;
import com.group1_cms.cms_antiques.spring.security.JwtFilter;
import com.group1_cms.cms_antiques.spring.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationContext applicationContext;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public WebSecurityConfig(ApplicationContext applicationContext, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.applicationContext = applicationContext;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Bean
    public UserRepository getUserRepository(){
        UserRepository userRepository = new UserRepository(namedParameterJdbcTemplate);
        return userRepository;
    }

    @Bean
    public UserService getUserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        UserService userService = new UserService(userRepository, passwordEncoder);
        return userService;
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return applicationContext.getBean(UserService.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(UserService userService){
        return new JwtTokenProvider(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                   .addFilterBefore(new JwtFilter(jwtTokenProvider(applicationContext.getBean(UserService.class))),
                            UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/posts/**").permitAll()
                .antMatchers("**").permitAll()
                .anyRequest().authenticated()
                .and()
                    .exceptionHandling().accessDeniedPage("/login")
                .and()
                    .formLogin().loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home")
                    .successHandler(new CustomAuthenticationSuccessHandler(applicationContext.getBean(JwtTokenProvider.class)))
                .and()
                    .logout()
                    .logoutUrl("/");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/webfonts/**");
    }
}

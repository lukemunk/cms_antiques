package com.group1_cms.cms_antiques.configurations;

import com.group1_cms.cms_antiques.components.RegistrationFormValidator;
import com.group1_cms.cms_antiques.components.StartupDatabaseLoader;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
import com.group1_cms.cms_antiques.repositories.RoleRepository;
import com.group1_cms.cms_antiques.repositories.UserRepository;
import com.group1_cms.cms_antiques.services.PermissionService;
import com.group1_cms.cms_antiques.services.RoleService;
import com.group1_cms.cms_antiques.services.UserService;
import com.group1_cms.cms_antiques.spring.security.CustomAuthenticationSuccessHandler;
import com.group1_cms.cms_antiques.spring.security.JwtFilter;
import com.group1_cms.cms_antiques.spring.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public StartupDatabaseLoader getDatabaseLoader(RoleService roleService, PermissionService permissionService, UserService userService){
        StartupDatabaseLoader startupDatabaseLoader = new StartupDatabaseLoader(roleService, permissionService, userService);
        return startupDatabaseLoader;
    }

    @Bean
    public UserRepository getUserRepository(){
        UserRepository userRepository = new UserRepository(namedParameterJdbcTemplate);
        return userRepository;
    }

    @Bean
    public UserService getUserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        UserService userService = new UserService(userRepository, roleRepository, passwordEncoder);
        return userService;
    }

    @Bean
    public RoleRepository getRoleRepository(){
        RoleRepository roleRepository = new RoleRepository(namedParameterJdbcTemplate);
        return roleRepository;
    }

    @Bean
    public RoleService getRoleService(RoleRepository roleRepository, PermissionRepository permissionRepository){  //Does roleRepository need to be autowired
        RoleService roleService = new RoleService(roleRepository, permissionRepository);//to pass to bean constructor
        return roleService;
    }

    @Bean
    public PermissionRepository permissionRepository(){
        PermissionRepository permissionRepository = new PermissionRepository(namedParameterJdbcTemplate);
        return permissionRepository;
    }

    @Bean
    public PermissionService permissionService(PermissionRepository permissionRepository){
        PermissionService permissionService = new PermissionService(permissionRepository);
        return permissionService;
    }

    @Bean
    public RegistrationFormValidator getRegistrationFormValidator(UserService userService){
        return new RegistrationFormValidator(userService);
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
       //return new BCryptPasswordEncoder();
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
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                    .exceptionHandling().accessDeniedPage("/login")
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login")
                    .successHandler(new CustomAuthenticationSuccessHandler(applicationContext.getBean(JwtTokenProvider.class)))
                    .defaultSuccessUrl("/public/index")
                    .failureUrl("/login-error");

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

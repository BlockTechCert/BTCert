package org.bham.btcert.config;

import org.bham.btcert.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 
* @Title: WebSecurityConfig.java 
* @Package org.bham.btcert.config 
* @Description: TODO
* @author https://spring.io/guides
* @version V1.0
* this code is come form: https://spring.io/guides/gs/securing-web/
 */
@Configuration
//@EnableGlobalMethodSecurity(jsr250Enabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    UserDetailsService customUserService(){ //注册UserDetailsService 的bean
        return new CustomUserService();
    }
    
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserService());
        authProvider.setPasswordEncoder(new CustomPasswordEncoder());
        return authProvider;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(customUserService()); //user Details Service验证
        auth.authenticationProvider(authProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll() //登录页面用户任意访问
                .and()
                .logout().permitAll(); //注销行为任意访问
*/          
    	
    	http.csrf().disable()
        .authorizeRequests()
			.antMatchers("/", "/home", "/verify", "/about","/demo","/download","/404","/403").permitAll()
			//.antMatchers("/", "/home", "/about","/webjars/bootstrap/3.3.7/js/**","/webjars/bootstrap/3.3.7/css/**").permitAll()
			.antMatchers("/student/**").hasAnyRole("STUDENT","ADMIN")
			.antMatchers("/checker/**").hasAnyRole("CHECKER","ADMIN")
			.antMatchers("/issuer/**").hasAnyRole("ISSUER","ADMIN")
			.antMatchers("/admin/**").hasAnyRole("ADMIN")
            //.antMatchers("/user/**").hasAnyRole("USER")
			.anyRequest().authenticated()
        .and()
        .formLogin()
			.loginPage("/login")
			.permitAll()
			.and()
        .logout()
			.permitAll()
			.and()
        .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication().withUser("student").password("student").roles("student");
        auth.inMemoryAuthentication().withUser("checker").password("checker").roles("checker");
        auth.inMemoryAuthentication().withUser("issuer").password("issuer").roles("issuer");*/
       /* auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");*/
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/jslib/**","/js/**", "/images/**","/webjars/bootstrap/3.3.7/js/**","/webjars/bootstrap/3.3.7/css/**");
    }
}

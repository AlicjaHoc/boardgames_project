package com.project.boardgames.utilities;

import com.project.boardgames.repositories.JwtTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private JwtTokenRepository jwtTokenRepository;
    SecurityConfig(JwtTokenRepository jwtTokenRepository){
        this.jwtTokenRepository = jwtTokenRepository;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/resources/**", "/loginUser", "/register").permitAll()
                    .antMatchers("/protectedPath").access("hasRole('ADMIN') and #JwtUtil.validateToken(request)")
                .and()
                .formLogin()
                .loginProcessingUrl("/loginUser")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/login?expired");
    }
}

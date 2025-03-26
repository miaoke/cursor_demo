package org.example.config;

import org.example.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            // 允许静态资源访问
            .antMatchers("/", "/index.html", "/login.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
            // 允许登录接口访问
            .antMatchers("/api/auth/login", "/api/auth/validate").permitAll()
            // 测试页面暂时开放访问
            .antMatchers("/**/*.html").permitAll()
            // 开放API测试
            .antMatchers("/api/**").permitAll()
            // 其他请求需要认证
            .anyRequest().authenticated()
            .and()
            // 移除表单登录配置，防止与RESTful API冲突
            .formLogin().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                // 使用自定义的PasswordUtil进行加密
                return org.example.util.PasswordUtil.encryptPassword(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                // 使用自定义的PasswordUtil进行密码验证
                return org.example.util.PasswordUtil.validatePassword(rawPassword.toString(), encodedPassword);
            }
        };
    }
} 
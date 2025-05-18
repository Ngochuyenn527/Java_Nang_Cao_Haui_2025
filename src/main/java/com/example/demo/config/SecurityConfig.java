package com.example.demo.config;

import com.example.demo.service.impl.CustomUserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    //Cấu hình bảo mật với SecurityFilterChain.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .antMatchers("/api/users/**").hasRole("MANAGER")
                .antMatchers("/api/building/**", "/api/sector/**", "/api/apt/**").hasAnyRole("MANAGER", "STAFF")
                .anyRequest().authenticated()
                .and()
                .httpBasic(); // hoặc .formLogin() tùy nhu cầu
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailServiceImpl();
    }

    //cung cấp UserDetailsService và PasswordEncoder.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailServiceImpl userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //PasswordEncoder là interface trong Spring Security dùng để
    //Mã hóa mật khẩu trước khi lưu vào database.
    //So sánh mật khẩu gốc (user nhập) với mật khẩu đã mã hóa (trong DB) khi đăng nhập.
    //➕ Spring không bao giờ lưu mật khẩu ở dạng rõ ràng (plain text) vì lý do bảo mật!
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //dùng để xử lý đăng nhập.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}

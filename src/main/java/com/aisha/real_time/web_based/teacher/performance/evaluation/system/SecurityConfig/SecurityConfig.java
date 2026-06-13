package com.aisha.real_time.web_based.teacher.performance.evaluation.system.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   
@Autowired
    private CustomUserDetailsService userDetailsService;

   
//,"/teacher/"
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            
            .csrf(csrf -> csrf.disable())  
            .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll() // ⬅️ كل المسارات مفتوحة
            )
            .formLogin(form -> form
                .loginPage("/signin")                    // صفحة تسجيل دخول مخصصة:contentReference[oaicite:6]{index=6}
                .loginProcessingUrl("/perform-login")            // نفس المسار لمعالجة بيانات النموذج
                .successHandler((request, response, auth) -> {
                    // توجيه المستخدم حسب صلاحياته بعد تسجيل الدخول الناجح
                    boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                        boolean isTeacher = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
                    if (isAdmin) {
                        response.sendRedirect("/admin/startmenu");
                    } else if(isTeacher) {
                        response.sendRedirect("/teacher/menu");
                    }
                })
                .failureUrl("/signin?error=true")        // ✅ إضافة صفحة الفشل
                
                .permitAll()
            )
           .logout(logout -> logout
                .logoutUrl("/perform-logout")            // ✅ مسار مختلف للتسجيل الخروج
                .logoutSuccessUrl("/signin?logout=true")
                .permitAll()
            )
            .userDetailsService(userDetailsService);
        return http.build();
    }

 @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}





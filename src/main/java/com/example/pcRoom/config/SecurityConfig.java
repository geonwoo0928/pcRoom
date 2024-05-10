package com.example.pcRoom.config;
//
//import com.example.myBoard.service.PrincipalOauth2UserService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //설정에 대한 어노테이션
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(); //비밀번호를 암호화
    }

    @Bean //클래스 인스턴스
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/css/**" , "/js/**" , "/images/**").permitAll() //** 모든것
                        .requestMatchers("/user/login" , "/user/register").permitAll()
                        .anyRequest().authenticated()) // 위에 폴더 외의 다른 폴더들은 인증받아야함 (anyRequest.authenticated)
//                        .requestMatchers("/**").permitAll())

                .formLogin((form)->form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/login")
//                        .usernameParameter("email") //email로 로그인할때 username 파라미터를 email로 한다 (@Id 가 email이 아니면 쿼리메소드로 findByEmail 만들어야함)
                        .defaultSuccessUrl("/" , true)) //로그인 성공하며 나오는 url
                .logout(out->out
                        .logoutSuccessUrl("/")
                        .logoutUrl("/logout"))

                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}

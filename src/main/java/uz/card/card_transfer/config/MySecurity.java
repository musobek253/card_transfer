package uz.card.card_transfer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.card.card_transfer.security.JwtFilter;
import uz.card.card_transfer.service.MyAuthService;

@Configuration
@EnableWebSecurity
@ComponentScan
public class MySecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    MyAuthService  myAuthService;
    @Autowired
    JwtFilter jwtFilter;




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myAuthService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // bu yerda faqat ikki yo'lga dostup bor qolganlari jwt orqali beriladi
                .antMatchers("/","/api/login").permitAll()
                .anyRequest()
                .authenticated();
        // bu user servisga kigandan keyin unga token beriladi va bu tokeni requst bilan hedirga keladi shu vaqtd asecuritiy birinchi jwt filtrga boraddi default holda UsernamePasswordAuthenticationFilter shunga boradi
           http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // bu token began vaqtda ssisinoni o'chirib qoyadi jwt tokeni o'chirilgan holda ham session ishlab turadi bu xatoto yani token muddati tugagan bo'lishi mumkin
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    // passwordni encod qilinadi
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // user bazada bor yoki yo'qligini ko'radigan metod
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}

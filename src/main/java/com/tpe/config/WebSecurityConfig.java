package com.tpe.config;

import com.tpe.security.service.AuthTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//? 25***
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//? 4*** ici bos sekilde bu classi olusturduk.5 icin sonra securityde service icine userdetailsimpl
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
//?26 extends WebSecurityConfigurerAdapter

    //? 29***
    @Autowired
    private UserDetailsService userDetailsService;

    //? 27***
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().//csrf().disable() yapmazsak update islemlerinde hata aliriz
                sessionManagement().//sessionla alakali bir yonetim var mi
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).//sessionla alakali yapilmasini istedigim seyler. restfuapide session yok stateless
                and().//ve
                authorizeRequests().//requestleri authorize et yetkilendir
                antMatchers("/register","/login").permitAll().//bunla gelen endpointi security katmanindan muaf tut
                anyRequest()//bunun disinda
                .authenticated();//authontice et

        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);// kendi filtremizi de ekledik
    }


    //? 31****
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    //? 28*** configure methodunda kullanmak icin. objenin create edilme islemini springe birakiyoruz
    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    //? 30 ****
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //? 32 *** AuthenticationManager bunu da eklememiz lazim
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }//33 icin UserDetailsImpl
}

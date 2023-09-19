package com.tpe.security.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@RequiredArgsConstructor
//? 7*** filtre koyacagimiz class ici bos olusturduk. 8 icin jwtutils
public class AuthTokenFilter extends OncePerRequestFilter {//filtre mekanizmasini ekledigim class
//? 16 *** extends OncePerRequestFilter bununla securitye bu classs filtre clasii diyoruz
    //? 17**
    @Autowired
    private  JwtUtils jwtUtils;

    //?22***
    @Autowired
    private  UserDetailsService userDetailsService;

    @Override//bu method exten edince geldi
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //? 18**  request icinden tokeni aliyoruz. bunun icin bu methodun disinda yardimci method yazicam 19.adim

        //?20***
        String jwtToken = parseJwt(request);// bu jwttokenden usernameyi cekmem lazim
        try {
            if(jwtToken!=null && jwtUtils.validateToken(jwtToken)) {//gelen token null degil ve valide edildi mi

                String userName = jwtUtils.getUserNameFromJwtToken(jwtToken);//usernameye ulasma amacim security contexte atmak
                //useri userdetailse cevirecegim methot servicede. oraya gidip loadUserByUsername methodunu olusturalim 21

                //?23**
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                // buradan itibaren Authenticate edilen kullaniciyi contexte atmak
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);//SecurityContextHolder valide edilen kullanicilari tutan class
            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }
        //request ve response icin filter olarak ekliyorum
        filterChain.doFilter(request, response);
    }//24 asagida

    //? 19****
    private String parseJwt(HttpServletRequest request){//jwtyi tut cek. HttpServletRequest bu classla http icindeki requesten tokeni cekebilirim

        String header = request.getHeader("Authorization");//jwttoken requestin header kisminda bulundugu icin. basinda 'Bearer' bulunarak geliyor
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")){//StringUtils.hasText(header) headerla gelen icinde text var mi& header.startsWith("Bearer ")ile mi basliyor
            return header.substring(7);
        }
        return null;
    }//20 icin yukari

    //? 24*** should yazinca geliyor. bu filtre hangileri icin gecerli olmasin
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        AntPathMatcher antMatcher = new AntPathMatcher();//AntPathMatcher bu class filtrelerden muaf tutar

        return antMatcher.match("/register", request.getServletPath()) ||
                antMatcher.match("/login", request.getServletPath());
    }
}//25 icin web security config

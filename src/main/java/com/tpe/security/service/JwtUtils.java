package com.tpe.security.service;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
//? 8*** bu classi olusturduk. sonra 9 rolerepository
//? 11**
@Component
public class JwtUtils {

    //? 12** secret kelimesine ve ttoken suresine ihtiyacimiz var. bunlari field olarak ekliyoruz
    private String jwtSecret ="sboot";
    private long jwtExpirationMs = 86400000; // 60*60*24*1000 ( 1 GUN )
    //normalde bu bilgiler burada degilde .propertiesde yazilir
    //burada jjwt kutuphanesi ekledik bu kutuphanin bize sagladigi methodlari kullanicaz

    //? 13 **********  GENERATE TOKEN ****************************
    public String generateToken(Authentication authentication){//token create eden method
    //anlik login olan kullanici Authenticationseklinde         //security contexe userdetails olarak degil Authentication seklinde atiliyor
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();//getPrincipal anlik login olan kullaniciyi bize getirir
        //neden user tipinde almadik. cunku security user bilmez
        return Jwts.builder().//builder bir nesneyi kisa yoldan setlemek icin. bundan donen deger jwttokenin kendisi
                setSubject(userDetails.getUsername()).//konu
                setIssuedAt(new Date()).//create edilme tarihi
                setExpiration(new Date(new Date().getTime() + jwtExpirationMs)).//suanki saati alip token suresini ekliyorum
                signWith(SignatureAlgorithm.HS512, jwtSecret).//bu secret key ile mi valide edildi
                compact();//hepsini kompatr yap paketle
    }


    // ?14***  **********  VALIDATE TOKEN ****************************
    public boolean validateToken(String token){
        try {
            Jwts.parser()//parser() tokeni parcalar, tokenin kendisi ve sifreyle karsilastirir
                    .setSigningKey(jwtSecret)//secret keyin neyse o.
                    .parseClaimsJws(token);//tokeni boluyor tersliyor
            return true;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }


    //? 15 **********  Get UserName from TOKEN *******************//jwt tokenden username bilgisini istiyoruz
    public String getUserNameFromJwtToken(String token){//username bilgisi neden tokende. unique oldugu icin

        return Jwts.parser().//claimslerine bolcez parcalara bolcez
                setSigningKey(jwtSecret).//sifrelendigini soyluyorum
                parseClaimsJws(token).//claimslerine bol
                getBody().//ilk jwttokenin bodysine git
                getSubject();//usernameyi subjecte atmistik yukarda. oradan aldik
    }//16 icin AuthTokenFilter



}

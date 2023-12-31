package com.tpe.controller;

import com.tpe.dto.LoginRequest;
import com.tpe.dto.RegisterRequest;
import com.tpe.security.service.JwtUtils;
import com.tpe.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
//? 39***
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserJwtController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    //? 40****
    //REGISTER ****************************
    @PostMapping("/register") // http://localhost:8080/register  + POST
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterRequest request){//41 registerequest dto classini olusturduk
        //42  user servise olusturduk
        //? 43***
        userService.registerUser(request);

        String responseMessage = "User registered successfully";

        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }//44 UserService

    //?  45 ***************** LOGIN ********************************
    @PostMapping("/login") // hhtp://localhost:8080/login  + POST
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequest request){//46 LoginRequest olusturduk
        //? 47***
        //kullanici authenticate edilecek
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        // JWT token uretiliyor
        String token = jwtUtils.generateToken(authentication);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return new ResponseEntity<>(map, HttpStatus.CREATED);


    }
}

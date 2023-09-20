package com.tpe.security.service;

import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//? 6**** olusturduk ve ici bos. 7 icin AuthToken filter
@Service
public class UserDetailsServiceImpl implements UserDetailsService {//? 21** implement ettik. tekrar AuthToken filter 22

    //? 37***
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //? 38***
        User user = userRepository.findByUserName(username).orElseThrow(()->
                new ResourceNotFoundException("User not found with username : " + username));

        return UserDetailsImpl.build(user);
    }//39 icin UserJwtController
}

package com.tpe.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tpe.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
//? 33****
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//? 5*** implement ediyoruz ve 7 methodu override ettik. 6 icin userdetails serviceimpl
public class UserDetailsImpl implements UserDetails {

    //? 34*** userdetailsi kendi istedigim fieldlarla olusturuyorum
    private Long id;

    private String userName;

    @JsonIgnore //client tarafina gideerse , password gitmesin
    private String password;

    private Collection<? extends GrantedAuthority> authorities;//rollerimi eklicem .GrantedAuthority extend eden herhangi bir class

    //? 35** user --> userDetails e cevirecek
    public static UserDetailsImpl build(User user) {
        // rollerimi GrantedAuthorities e cevirecem
        // loadUserByUserName metodunda yardimci method olarak cagirilacak
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream().
                map(role-> new SimpleGrantedAuthority(role.getName().name())).
                collect(Collectors.toList());
        // user bilgilerim ile userDetails olusturup donduruyorum

        return new UserDetailsImpl(user.getId(),
                user.getUserName(),
                user.getPassword(),
                authorities);
    }




    //? 36** nullari ceviriyoruz
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }//37 icin UserDetailsServiceImpl
}

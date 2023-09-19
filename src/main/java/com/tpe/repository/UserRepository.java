package com.tpe.repository;

import com.tpe.domain.User;
import com.tpe.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//? 10*** extend ettik findByUserName ve existsByUserName yi olusturduk
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName) throws ResourceNotFoundException;

    Boolean existsByUserName(String userName);
}//? 11 icin jwt utils

package com.tpe.repository;

import com.tpe.domain.Role;
import com.tpe.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
//? 9*** extend ettik ve findByName yi olusturduk
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(UserRole name);
}//? 10 icin userrepository

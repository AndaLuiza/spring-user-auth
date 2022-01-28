package com.anda.user.auth.repository;

import com.anda.user.auth.model.Role;
import com.anda.user.auth.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}

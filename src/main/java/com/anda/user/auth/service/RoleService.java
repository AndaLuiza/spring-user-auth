package com.anda.user.auth.service;

import com.anda.user.auth.model.Role;
import com.anda.user.auth.model.RoleEnum;

import java.util.List;
import java.util.Optional;

public interface RoleService
{
    List<Role> saveAll (List<Role> roles);

    List<Role> findAll();

    Optional<Role> findByName (RoleEnum name);
}

package com.anda.user.auth.service;

import com.anda.user.auth.model.Role;

import java.util.List;

public interface RoleService
{
    List<Role> saveAll (List<Role> roles);

    List<Role> findAll();
}

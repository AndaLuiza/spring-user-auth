package com.anda.user.auth.service;

import com.anda.user.auth.model.Role;
import com.anda.user.auth.model.RoleEnum;
import com.anda.user.auth.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<Role> saveAll(List<Role> roles)
    {
        return roleRepository.saveAll(roles);
    }

    @Override
    @Transactional
    public List<Role> findAll()
    {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findByName(RoleEnum name)
    {
        return roleRepository.findByName(name);
    }
}

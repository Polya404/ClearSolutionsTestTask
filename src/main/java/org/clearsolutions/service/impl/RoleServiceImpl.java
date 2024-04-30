package org.clearsolutions.service.impl;

import lombok.RequiredArgsConstructor;
import org.clearsolutions.model.Role;
import org.clearsolutions.model.User;
import org.clearsolutions.repository.RoleRepository;
import org.clearsolutions.repository.UserRepository;
import org.clearsolutions.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void addRoleToUser(long userid, long roleId) {
        User user = userRepository.findById(userid).orElseThrow();
        List<Role> roles = user.getRoles();
        roles.add(roleRepository.findById(roleId).orElseThrow());
        userRepository.save(user);
    }

    @Override
    public List<Role> getRoleByUserId(long userid) {
        return roleRepository.findUserRolesByUserId(userid);
    }

    @Override
    public List<Role> getAllExistRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteRoleById(long roleId) {
        roleRepository.deleteById(roleId);
    }
}


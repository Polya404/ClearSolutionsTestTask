package org.clearsolutions.service;

import org.clearsolutions.model.Role;

import java.util.List;

public interface RoleService {

    Role findRoleByName(String name);

    void addRoleToUser(long userid, long roleId);

    List<Role> getRoleByUserId(long userid);

    List<Role> getAllExistRoles();

    void deleteRoleById(long userId);
}

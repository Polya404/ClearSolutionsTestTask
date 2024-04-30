package org.clearsolutions.repository;

import org.clearsolutions.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Query("SELECT r FROM Role r INNER JOIN r.users u WHERE u.id = :userId")
    List<Role> findUserRolesByUserId(@Param("userId") long userId);
}

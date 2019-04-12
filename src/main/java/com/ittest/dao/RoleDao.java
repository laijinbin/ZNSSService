package com.ittest.dao;

import com.ittest.entiry.Role;

public interface RoleDao {
    Role findRoleByRoleName(String roleName);
}

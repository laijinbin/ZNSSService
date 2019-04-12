package com.ittest.dao;

import com.ittest.entiry.SysUser;

public interface SysUserDao {
    SysUser findUserByOpenId(String openId);

    int saveUser(SysUser sysUser);

    void deleteUser(String openId);

    SysUser findUserByUserName(String username);

    void modifyPassword(SysUser sysUser);

    void updateUser(SysUser sysUser);

    SysUser findUser(String username);
}

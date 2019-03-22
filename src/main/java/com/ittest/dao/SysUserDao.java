package com.ittest.dao;

import com.ittest.entiry.SysUser;

public interface SysUserDao {
    SysUser findUserByOpenId(String openId);

    int saveUser(SysUser sysUser);
}

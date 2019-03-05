package com.ittest.dao;

import com.ittest.entiry.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IUserDao {
    List<User> findAll();
}

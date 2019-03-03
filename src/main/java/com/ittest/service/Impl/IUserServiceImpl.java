package com.ittest.service.Impl;

import com.ittest.dao.IUserDao;
import com.ittest.entiry.User;
import com.ittest.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IUserServiceImpl  implements IUserService{
    @Autowired
    private IUserDao userDao;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}

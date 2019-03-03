package com.ittest.controller;


import com.ittest.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class IUserController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/findAll")
    public String findAll(){
        System.out.println( userService.findAll());
        System.out.println("我来了");
        return "success";
    }

}

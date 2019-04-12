package com.ittest.controller;

import com.ittest.dao.DeviceDao;
import com.ittest.dao.SysUserDao;
import com.ittest.entiry.Device;
import com.ittest.entiry.SysUser;
import com.ittest.service.MiniprogramService;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.commons.lang.StringUtils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class WebUser {

    @Autowired
    private MiniprogramService miniprogramService;

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/login")
    public String login(String username, String password,
                        String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (StringUtils.isNotBlank(username)) {
                if (code.equalsIgnoreCase((String) request.getSession().getAttribute("CHECKCODE_SERVER"))) {
                    UsernamePasswordAuthenticationToken token
                            = new UsernamePasswordAuthenticationToken(username, password);
                    Authentication authenticate = authenticationManager.authenticate(token);
                    if (authenticate.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(authenticate);
                        SysUser sysUser = sysUserDao.findUserByUserName(username);
                        request.getSession().setAttribute("sysUser", sysUser);
                        if (!"admin".equals(username)){
                            Device device = deviceDao.findById(Integer.toString(sysUser.getDeviceId()));
                            request.getSession().setAttribute("device", device);
                        }
                        request.getSession().setAttribute("loginTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        return "redirect:/index.jsp";
                    }
                } else {
                    request.getSession().setAttribute("msg", "验证码错误");
                }
            }
        } catch (AuthenticationException e) {
            request.getSession().setAttribute("msg", "账号或密码错误");
            e.printStackTrace();
        }
        return "redirect:/login.jsp";
    }

    @RequestMapping("/changPassword")
    @ResponseBody
    public Map<String, Object> changPassword(String oldPassword, String newPassword,HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        SysUser sysUser=(SysUser)request.getSession().getAttribute("sysUser");
        String userName = sysUser.getUserName();
        resultMap = miniprogramService.changPassword(oldPassword, newPassword, userName);
        return resultMap;
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public Map<String, Object> updateUser(String userName,String phone,String realName,int userId,HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap = miniprogramService.updateUser(userName,phone,realName,userId,request);
        return resultMap;
    }

}

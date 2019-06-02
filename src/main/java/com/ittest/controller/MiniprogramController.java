package com.ittest.controller;

import com.alibaba.fastjson.JSON;
import com.ittest.dao.SysUserDao;
import com.ittest.entiry.CheckWenShiDu;
import com.ittest.entiry.SysUser;
import com.ittest.service.MiniprogramService;
import com.ittest.utils.HttpClientUtils;
import com.ittest.utils.WebUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/wxMiniprogram")
public class MiniprogramController {

    @Autowired
    private MiniprogramService miniprogramService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SysUserDao sysUserDao;


    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(String code){
        if (StringUtils.isBlank(code)){
            return WebUtil.generateModelMap("0","code不能为空");
        }
        Map<String,Object> resultMap=miniprogramService.login(code);
        return resultMap;
    }

    @RequestMapping("/bindDevice")
    @ResponseBody
    public Map<String, Object> bindDevice(@RequestBody Map<String,Object> requestMap){
        Map<String,Object> resultMap= null;
        try {
            resultMap = miniprogramService.bindDevice(requestMap);
        } catch (Exception e) {
            resultMap=WebUtil.generateFailModelMap("服务器忙，等一下再来好不好");
            e.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping("/cancalBind")
    @ResponseBody
    public Map<String, Object> cancalBind(String openId,String deviceId){
        Map<String,Object> resultMap= null;
        try {
            resultMap = miniprogramService.cancalBind(openId,deviceId);
        } catch (Exception e) {
            resultMap=WebUtil.generateFailModelMap("服务器忙，等一下再来好不好");
            e.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping("/getdemo")
    @ResponseBody
    public Map<String, Object> getdemo(String openId){
        return miniprogramService.demo(openId);
    }

    @RequestMapping("/dingShi")
    @ResponseBody
    public Map<String, Object> dingShi(@RequestBody Map<String,Object> reqMap){
        return miniprogramService.dingShi(reqMap);
    }

    @RequestMapping("/saveWenShiDuMax")
    @ResponseBody
    public Map saveWenShiDuMax(@RequestBody CheckWenShiDu checkWenShiDu){
        return miniprogramService.saveWenShiDuMax(checkWenShiDu);
    }

}

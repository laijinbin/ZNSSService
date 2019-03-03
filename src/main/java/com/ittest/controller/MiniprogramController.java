package com.ittest.controller;

import com.alibaba.fastjson.JSON;
import com.ittest.utils.HttpClientUtils;
import com.ittest.utils.WebUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/wxMiniprogram")
public class MiniprogramController {

    @Value("${appid}")
    private String appid;
    @Value("${secret}")
    private String secret;
    @Value("${grant_type}")
    private String grantType;
    @Value("${wx_login_url}")
    private String wxLoginUrl;


    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(String code){
        Map<String,Object> map=null;
        HttpClientUtils httpClientUtils=new HttpClientUtils(true);
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("appid",appid);
        paramMap.put("secret",secret);
        paramMap.put("js_code",code);
        paramMap.put("grant_type",grantType);
        String result = null;
        try {
            result = httpClientUtils.sendGet(wxLoginUrl, paramMap);
            Map<String,Object> resultMap= JSON.parseObject(result,Map.class);
            map= WebUtil.generateSuccessModelMap();
            map.put("result",resultMap);
        } catch (Exception e) {
            map=WebUtil.generateFailModelMap("服务器忙，等下再试好不好");
            e.printStackTrace();
        }
        return map;
    }
}

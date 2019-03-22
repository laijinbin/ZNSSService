package com.ittest.service;

import com.alibaba.fastjson.JSON;
import com.ittest.dao.DeviceDao;
import com.ittest.dao.SysUserDao;
import com.ittest.entiry.Device;
import com.ittest.entiry.SysUser;
import com.ittest.utils.HttpClientUtils;
import com.ittest.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class MiniprogramService {

    @Value("${appid}")
    private String appid;
    @Value("${secret}")
    private String secret;
    @Value("${grant_type}")
    private String grantType;
    @Value("${wx_login_url}")
    private String wxLoginUrl;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private DeviceDao deviceDao;

    @Value("${openId}")
    private String openId;

    public Map<String, Object> login(String code) {
        //HttpClientUtils httpClientUtils=new HttpClientUtils(true);
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("appid",appid);
        paramMap.put("secret",secret);
        paramMap.put("js_code",code);
        paramMap.put("grant_type",grantType);
        //String result = null;
        Map<String,Object> map=null;
        Map<String,Object> resultMap=new HashMap<>();
        try {
            /*result = httpClientUtils.sendGet(wxLoginUrl, paramMap);
            Map<String,Object> resultMap= JSON.parseObject(result,Map.class);
            map= WebUtil.generateSuccessModelMap();
            map.put("result",resultMap);
            String openId=(String) resultMap.get("openid");*/
            map= WebUtil.generateSuccessModelMap();
            resultMap.put("openId",openId);
            map.put("resultMap",resultMap);
            SysUser sysUser=sysUserDao.findUserByOpenId(openId);
            if (sysUser!=null){
                map.put("sysUser",sysUser);
            }else {
                map.put("sysUser","");
            }
        } catch (Exception e) {
            map=WebUtil.generateFailModelMap("服务器忙，等下再试好不好");
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> bindDevice(Map<String, Object> requestMap) {
        try {
            Map<String,Object> resultMap=new HashMap<>();
            String openId=(String) requestMap.get("openId");
            String deviceName=(String) requestMap.get("deviceName");
            String password=(String) requestMap.get("password");
            String phone=(String) requestMap.get("phone");
            String realName=(String) requestMap.get("realName");
            String username=(String) requestMap.get("userName");
            Device device=deviceDao.findByDeviceName(deviceName);
            if (device==null){
                return WebUtil.generateFailModelMap("设备不存在,请仔细检查无误后重新输入");
            }
            SysUser sysUser=new SysUser();
            sysUser.setOpenId(openId);
            sysUser.setDeviceId(device.getDeviceId());
            sysUser.setIsBind("1");
            sysUser.setPassword(password);
            sysUser.setUserName(username);
            sysUser.setRealName(realName);
            sysUser.setPhone(phone);
            sysUserDao.saveUser(sysUser);
            resultMap=WebUtil.generateModelMap("0","绑定成功");
            resultMap.put("sysUser",sysUser);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtil.generateFailModelMap("服务器忙，等下再试好不好");
        }
    }
}

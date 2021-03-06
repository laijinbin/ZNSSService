package com.ittest.service;

import com.alibaba.fastjson.JSON;
import com.ittest.dao.*;
import com.ittest.entiry.*;
import com.ittest.utils.HttpClientUtils;
import com.ittest.utils.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private TaskLogDao taskLogDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SMSService smsService;

    /*@Value("${openId}")
    private String openId;*/

    public Map<String, Object> login(String code) {
        HttpClientUtils httpClientUtils = new HttpClientUtils(true);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appid);
        paramMap.put("secret", secret);
        paramMap.put("js_code", code);
        paramMap.put("grant_type", grantType);
        String result = null;
        Map<String, Object> map = null;
        // Map<String,Object> resultMap=new HashMap<>();
        try {
            result = httpClientUtils.sendGet(wxLoginUrl, paramMap);
            Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
            map = WebUtil.generateSuccessModelMap();
            // map.put("result",resultMap);
            String openId = (String) resultMap.get("openid");
            resultMap.put("openId", openId);
            map = WebUtil.generateSuccessModelMap();
            // resultMap.put("openId",openId);
            map.put("resultMap", resultMap);
            SysUser sysUser = sysUserDao.findUserByOpenId(openId);
            if (sysUser != null) {
                map.put("sysUser", sysUser);
                Device device = deviceDao.findById(Integer.toString(sysUser.getDeviceId()));
                map.put("device", device);
            } else {
                map.put("sysUser", "");
            }
        } catch (Exception e) {
            map = WebUtil.generateFailModelMap("服务器忙，等下再试好不好");
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> bindDevice(Map<String, Object> requestMap) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            String openId = (String) requestMap.get("openId");
            String deviceName = (String) requestMap.get("deviceName");
            String password = (String) requestMap.get("password");
            String phone = (String) requestMap.get("phone");
            String realName = (String) requestMap.get("realName");
            String username = (String) requestMap.get("userName");
            Device device = deviceDao.findByDeviceName(deviceName);
            SysUser sysUser1 = sysUserDao.findUser(username);
            if (device == null) {
                return WebUtil.generateFailModelMap("设备不存在,请仔细检查无误后重新输入");
            }
            if (device != null && "1".equals(device.getIsBind())) {
                return WebUtil.generateFailModelMap("该设备已被绑定，请核实无误后重新输入");
            }
            if (sysUser1 != null) {
                return WebUtil.generateFailModelMap("用户名存在，请重新输入");
            }
            SysUser sysUser = new SysUser();
            sysUser.setOpenId(openId);
            sysUser.setDeviceId(device.getDeviceId());
            sysUser.setPassword(bCryptPasswordEncoder.encode(password));
            sysUser.setUserName(username);
            sysUser.setRealName(realName);
            sysUser.setPhone(phone);
            //保存用户
            sysUserDao.saveUser(sysUser);
            //更新设备表的信息
            device.setBindUserId(sysUser.getUserId());
            device.setIsBind("1");
            deviceDao.updateBindInfo(device);
            //为用户分配角色
            Role role = roleDao.findRoleByRoleName("ROLE_USER");
            UserRole userRole = new UserRole();
            userRole.setRoleId(role.getRoleId());
            userRole.setUserId(sysUser.getUserId());
            userRoleDao.save(userRole);

            resultMap = WebUtil.generateModelMap("0", "绑定成功");
            resultMap.put("sysUser", sysUser);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtil.generateFailModelMap("服务器忙，等下再试好不好");
        }
    }

    public Map<String, Object> cancalBind(String openId, String deviceId) {
        try {
            //解绑用户表的
            sysUserDao.deleteUser(openId);
            //解绑设备的
            Device device = new Device();
            device.setIsBind("0");
            device.setBindUserId(0);
            device.setDeviceId(Integer.parseInt(deviceId));
            deviceDao.updateBindInfo(device);

            return WebUtil.generateModelMap("0", "解绑成功");
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtil.generateFailModelMap("服务器忙，等下再试好不好");
        }
    }

    public Map<String, Object> demo(String username) {
        SysUser sysUser = sysUserDao.findUserByUserName(username);
        System.out.println(sysUser);
        Map<String, Object> map = new HashMap<>();
        map.put("id", sysUser);
        return map;
    }

    public Map<String, Object> changPassword(String oldPassword, String newPassword, String userName) {
        SysUser sysUser = sysUserDao.findUserByUserName(userName);
        if (!bCryptPasswordEncoder.matches(oldPassword, sysUser.getPassword())) {
            return WebUtil.generateModelMap("1", "原密码错误，请重新输入");
        }
        try {
            sysUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            sysUserDao.modifyPassword(sysUser);
            return WebUtil.generateModelMap("0", "修改密码成功，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtil.generateModelMap("1", "服务器忙，等下再试行不行");
        }

    }

    public Map<String, Object> updateUser(String userName, String phone, String realName, int userId, HttpServletRequest request) {
        String oldName = ((SysUser) request.getSession().getAttribute("sysUser")).getUserName();
        if (!oldName.equals(userName)) {
            SysUser sysUser = sysUserDao.findUser(userName);
            if (sysUser != null) {
                return WebUtil.generateFailModelMap("用户名已存在，请重新输入");
            }
        }
        SysUser sysUser2 = new SysUser();
        sysUser2.setRealName(realName);
        sysUser2.setUserName(userName);
        sysUser2.setUserId(userId);
        sysUser2.setPhone(phone);
        try {
            sysUserDao.updateUser(sysUser2);
            SysUser sysUser1 = sysUserDao.findUser(userName);
            request.getSession().setAttribute("sysUser", sysUser1);
            return WebUtil.generateModelMap("0", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtil.generateFailModelMap("服务器忙，等下再试好不好");
        }

    }

    public Map<String, Object> dingShi(Map<String, Object> reqMap) {
        try {
            String openId = (String) reqMap.get("openId");
            int userId = (int) reqMap.get("userId");
            TaskLog taskLog = new TaskLog();
            taskLog.setCreateTime(new Date());
            taskLog.setDeviceName((String) reqMap.get("deviceName"));
            taskLog.setStatus("0");
            taskLog.setUserId(userId);
            taskLog.setRealName((String) reqMap.get("realName"));
            taskLog.setOpenId(openId);
            taskLog.setInstructions((String) reqMap.get("instructions"));
            taskLog.setIntervalTime((Integer) reqMap.get("intervalTime"));
            taskLogDao.save(taskLog);
            //存进redis
            redisTemplate.boundHashOps("TaskLogList").put(taskLog.getTaskId(), taskLog);
            return WebUtil.generateModelMap("0", "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtil.generateFailModelMap("服务器忙，等一下再试好不好");
        }
    }

    public Map saveWenShiDuMax(CheckWenShiDu checkWenShiDu) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<>();
            redisTemplate.boundHashOps("wenShiDuMaxList").put(checkWenShiDu.getCommonDeviceName(), checkWenShiDu);
            map = WebUtil.generateModelMap("0", "设置成功");
        } catch (Exception e) {
            map = WebUtil.generateModelMap("1", "服务器忙，等下再试好不好");
            e.printStackTrace();
        }
        return map;
    }

    public Map sendSmsCode(String changPwdPhone) {
        Map<String, Object> map = new HashMap<>();
        try {
            String code = UUID.randomUUID().toString().replaceAll("-", "")
                    .replaceAll("[a-z|A-Z]", "").substring(0, 6);
            smsService.sendSMSCode(code, changPwdPhone);
            redisTemplate.boundValueOps(changPwdPhone).set(code, 60, TimeUnit.SECONDS);
            map = WebUtil.generateModelMap("0", "发送成功");
        } catch (Exception e) {
            map = WebUtil.generateModelMap("1", "发送失败，请重新试一下");
            e.printStackTrace();
        }
        return map;
    }

    public Map alertPwd(Map<String, Object> reqMap) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<>();
            String changPwdName = (String) reqMap.get("changPwdName");
            String changPwdNewPwd = (String) reqMap.get("changPwdNewPwd");
            String changPwdSmsCode = (String) reqMap.get("changPwdSmsCode");
            String changPwdPhone = (String) reqMap.get("changPwdPhone");
            String code = (String) redisTemplate.boundValueOps(changPwdPhone).get();
            if (!StringUtils.isNotBlank(code)) {
                return WebUtil.generateModelMap("1", "验证码已过期，请重新获取，谢谢");
            }
            if (!code.equals(changPwdSmsCode)) {
                return WebUtil.generateModelMap("1", "验证码错误，请重新输入，谢谢");
            }
            SysUser sysUser = sysUserDao.findUser(changPwdName);
            if (sysUser == null) {
                return WebUtil.generateModelMap("1", "用户不存在，请重新输入");
            }
            if (!sysUser.getPhone().equals(changPwdPhone)) {
                return WebUtil.generateModelMap("1", "当前手机号码与用户绑定的号码不一致，请重新输入");
            }
            sysUser.setPassword(bCryptPasswordEncoder.encode(changPwdNewPwd));
            sysUserDao.modifyPassword(sysUser);
            return WebUtil.generateModelMap("0", "重置密码成功，请使用新密码登录");
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtil.generateModelMap("1", "服务器忙，请等一下再重试");
        }
    }

    public Map checkUserName(String userName) {
        SysUser sysUser = sysUserDao.findUser(userName);
        if (sysUser != null) {
            return WebUtil.generateModelMap("1", "用户名已存在，请重新输入");
        }
        return WebUtil.generateModelMap("0", "用户名可用");
    }
}

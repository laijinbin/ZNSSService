package com.ittest.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ittest.dao.DeviceDao;
import com.ittest.entiry.Device;
import com.ittest.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeviceService {

    @Autowired
    private DeviceDao deviceDao;


    public PageInfo<Device> findAll(int pageNum, int pageSize) {
        PageInfo<Device> pageInfo= null;
        try {
            PageHelper.startPage(pageNum,pageSize);
            List<Device> deviceList=deviceDao.findAll();
            pageInfo = new PageInfo<>(deviceList);
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Device device) {
        try {
            device.setIsBind("0");
            device.setCommonDeviceName(device.getFloorNum()+"-"+device.getDormitoryNum()+"-A");
            deviceDao.save(device);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Device findById(String deviceId) {
        Device device=deviceDao.findById(deviceId);
        return device;
    }

    public void update(Device device) {
        try {
            device.setCommonDeviceName(device.getFloorNum()+"-"+device.getDormitoryNum()+"-A");
            deviceDao.update(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String[] deviceIds) {
        List<String> list=new ArrayList<>();
        for (String deviceId : deviceIds) {
            list.add(deviceId);
        }
        deviceDao.delete(list);
    }

    public Map<String, Object> checkDeviceName(String deviceName) {
        Map<String,Object> map=new HashMap<>();
        Device device=deviceDao.findByDeviceName(deviceName);
        if (device!=null){
            return WebUtil.generateFailModelMap("设备名已存在，请重新输入");
        }else {
            return WebUtil.generateModelMap("0","设备名可用");
        }
    }
}

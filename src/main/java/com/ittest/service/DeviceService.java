package com.ittest.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ittest.dao.DeviceDao;
import com.ittest.entiry.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}

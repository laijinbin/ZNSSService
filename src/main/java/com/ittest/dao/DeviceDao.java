package com.ittest.dao;

import com.ittest.entiry.Device;

import java.util.List;

public interface DeviceDao {
    Device findByDeviceName(String deviceName);

    List<Device> findAll();

    void save(Device device);

    Device findById(String deviceId);

    void update(Device device);

    void delete(List<String> list);

    void updateBindInfo(Device device);
}

package com.ittest.dao;

import com.ittest.entiry.Device;

public interface DeviceDao {
    Device findByDeviceName(String deviceName);
}

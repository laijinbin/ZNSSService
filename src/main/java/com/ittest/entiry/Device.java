package com.ittest.entiry;

public class Device {
    private int deviceId;
    private String deviceName;
    private String floorNum;
    private String dormitoryNum;
    private int bindUserId;
    private String commonDeviceName;
    private String isBind;
    private SysUser sysUser;

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public int getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(int bindUserId) {
        this.bindUserId = bindUserId;
    }

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public String getCommonDeviceName() {
        return commonDeviceName;
    }

    public void setCommonDeviceName(String commonDeviceName) {
        this.commonDeviceName = commonDeviceName;
    }


    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getDormitoryNum() {
        return dormitoryNum;
    }

    public void setDormitoryNum(String dormitoryNum) {
        this.dormitoryNum = dormitoryNum;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

package com.ittest.entiry;

import java.io.Serializable;

public class CheckWenShiDu implements Serializable {
    private String commonDeviceName;
    private String wenduMax;
    private String ShiDuMax;

    public String getCommonDeviceName() {
        return commonDeviceName;
    }

    public void setCommonDeviceName(String commonDeviceName) {
        this.commonDeviceName = commonDeviceName;
    }

    public String getWenduMax() {
        return wenduMax;
    }

    public void setWenduMax(String wenduMax) {
        this.wenduMax = wenduMax;
    }

    public String getShiDuMax() {
        return ShiDuMax;
    }

    public void setShiDuMax(String shiDuMax) {
        ShiDuMax = shiDuMax;
    }
}

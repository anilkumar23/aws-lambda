package com.javaworld.awslambda.smarttrack.model;

import java.util.Set;

public class DeviceName {
    private String[] deviceList;

    public String[] getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(String[] deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public String toString() {
        return "{" +
                "deviceList:" + deviceList +
                '}';
    }

    public DeviceName(String[] deviceList) {
        this.deviceList = deviceList;
    }
}

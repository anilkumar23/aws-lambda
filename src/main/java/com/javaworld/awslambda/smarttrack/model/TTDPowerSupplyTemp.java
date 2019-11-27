package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName = "ttdPowerSupplyTemp")
public class TTDPowerSupplyTemp implements Serializable {
    private static final long serialVersionUID = -3534650012619938612L;

    private String id;
    private String deviceId;
    private String deviceName;
    private String tStamp;
    private String ipAddress;
    private String power;
    private String energy;

    public TTDPowerSupplyTemp(String deviceId, String deviceName, String tStamp, String ipAddress, String power, String energy) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.tStamp = tStamp;
        this.ipAddress = ipAddress;
        this.power = power;
        this.energy = energy;
    }

    public TTDPowerSupplyTemp() {
    }

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @DynamoDBAttribute(attributeName = "deviceName")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @DynamoDBAttribute(attributeName = "tStamp")
    public String gettStamp() {
        return tStamp;
    }

    public void settStamp(String tStamp) {
        this.tStamp = tStamp;
    }

    @DynamoDBAttribute(attributeName = "power")
    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    @DynamoDBAttribute(attributeName = "energy")
    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    @DynamoDBAttribute(attributeName = "ipAddress")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", deviceId:'" + deviceId + '\'' +
                ", deviceName:'" + deviceName + '\'' +
                ", tStamp:'" + tStamp + '\'' +
                ", ipAddress:'" + ipAddress + '\'' +
                ", power:'" + power + '\'' +
                ", energy:'" + energy + '\'' +
                '}';
    }
}
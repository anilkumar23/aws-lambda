package com.javaworld.awslambda.widget.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;
import java.sql.DataTruncation;

/**
 * Created by anil.saladi on 9/26/2019.
 */
@DynamoDBTable(tableName = "smart_track")
public class SmartTrack implements Serializable{
    private static final long serialVersionUID = -3534650012619938612L;

    private String deviceId;
    private String deviceName;
    private String timestamp;
    private String humidity;
    private String temperature;

    public SmartTrack(String deviceId, String deviceName, String timestamp, String humidity, String temperature) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.timestamp = timestamp;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public SmartTrack() {
    }

    @DynamoDBHashKey(attributeName = "deviceId")
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
    @DynamoDBAttribute(attributeName = "humidity")
    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    @DynamoDBAttribute(attributeName = "temperature")
    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "deviceId : '" + deviceId + '\'' +
                ", deviceName : '" + deviceName + '\'' +
                ", timestamp : '" + timestamp + '\'' +
                ", humidity : '" + humidity + '\'' +
                ", temperature : '" + temperature + '\'' +
                '}';
    }
}

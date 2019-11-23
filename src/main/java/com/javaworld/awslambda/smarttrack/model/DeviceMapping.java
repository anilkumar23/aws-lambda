package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "device_mapping")
public class DeviceMapping {
    private String subStation;
    private String deviceName;
    private String description;

    public DeviceMapping() {
    }

    public DeviceMapping(String subStation, String deviceName, String description) {
        this.subStation = subStation;
        this.deviceName = deviceName;
        this.description = description;
    }

    @DynamoDBHashKey(attributeName = "subStation")
    @DynamoDBAttribute(attributeName = "subStation")
    public String getSubStation() {
        return subStation;
    }

    public void setSubStation(String subStation) {
        this.subStation = subStation;
    }

    @DynamoDBAttribute(attributeName = "deviceName")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

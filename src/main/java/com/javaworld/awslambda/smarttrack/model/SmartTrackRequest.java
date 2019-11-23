package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created by anil.saladi on 9/29/2019.
 */
@DynamoDBTable(tableName = "smart_track_request")
public class SmartTrackRequest {

    private String timestamp;
    private String deviceName;

    public SmartTrackRequest() {
    }

    public SmartTrackRequest(String timestamp, String deviceName) {
        this.timestamp = timestamp;
        this.deviceName = deviceName;
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute(attributeName = "deviceName")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

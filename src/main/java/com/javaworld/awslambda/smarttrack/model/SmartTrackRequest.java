package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created by anil.saladi on 9/29/2019.
 */
@DynamoDBTable(tableName = "smart_track_request")
public class SmartTrackRequest {

    private String deviceId;
    private String timestamp;

    public SmartTrackRequest() {
    }

    public SmartTrackRequest(String deviceId, String timestamp) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute(attributeName = "deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

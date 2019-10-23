package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;

/**
 * Created by anil.saladi on 10/23/2019.
 */
public class Voltage {
    private String rphvol;
    private String timestamp;

    public Voltage() {
    }

    public Voltage(String rphvol, String timestamp) {
        this.rphvol = rphvol;
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute(attributeName = "rphvol")
    public String getRphvol() {
        return rphvol;
    }

    public void setRphvol(String rphvol) {
        this.rphvol = rphvol;
    }
    @DynamoDBAttribute(attributeName = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

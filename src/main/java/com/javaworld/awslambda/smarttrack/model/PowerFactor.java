package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;

import java.util.List;
import java.util.Set;

public class PowerFactor {
    private List<Double> data;
    private String label;

    public PowerFactor() {
    }

    public PowerFactor(List<Double> data, String label) {
        this.data = data;
        this.label = label;
    }
    @DynamoDBAttribute(attributeName = "data")
    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }
    @DynamoDBAttribute(attributeName = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;

import java.util.Set;

public class Current {
    private Set<Double> data;
    private String label;

    public Current() {
    }

    public Current(Set<Double> data, String label) {
        this.data = data;
        this.label = label;
    }
    @DynamoDBAttribute(attributeName = "data")
    public Set<Double> getData() {
        return data;
    }

    public void setData(Set<Double> data) {
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

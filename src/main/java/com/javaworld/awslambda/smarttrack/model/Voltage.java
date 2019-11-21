package com.javaworld.awslambda.smarttrack.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;

import java.util.List;
import java.util.Set;

/**
 * Created by anil.saladi on 10/23/2019.
 */
public class Voltage {
    private Set<Double> data;
    private String label;

    public Voltage() {
    }

    public Voltage(Set<Double> data, String label) {
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

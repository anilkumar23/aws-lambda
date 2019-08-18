package com.javaworld.awslambda.widget.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="widget")
public class Widget {
    private String id;
    private String name;

    public Widget() {
    }

    public Widget(String id) {
        this.id = id;
    }

    public Widget(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @DynamoDBHashKey(attributeName="id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

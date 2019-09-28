package com.javaworld.awslambda.widget.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.javaworld.awslambda.widget.model.SmartTrack;

public class GetHandler implements RequestHandler<SmartTrack, SmartTrack> {
    @Override
    public SmartTrack handleRequest(SmartTrack smartTrack, Context context) {
        // Create a connection to DynamoDB
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();

        //
        // Low-level interface for retrieving a Widget from DynamoDB
        //
        /*
        DynamoDB dynamoDB = new DynamoDB(client);

        // Get a reference to the Widget table
        Table table = dynamoDB.getTable("Widget");

        // Get our item by ID
        Item item = table.getItem("id", widgetRequest.getId());
        if(item != null) {
            System.out.println(item.toJSONPretty());

            // Return a new Widget object
            return new Widget(widgetRequest.getId(), item.getString("name"));
        }
        else {
            return new Widget();
        }
        */

        //
        // High-level interface for retrieving a Widget from DynamoDB
        //

        // Build a mapper
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        // Load the widget by ID
        smartTrack = mapper.load(SmartTrack.class, smartTrack.getDeviceId());
        if(smartTrack == null) {
            // We did not find a widget with this ID, so return an empty Widget
            context.getLogger().log("No Widget found with ID: " + smartTrack.getDeviceId() + "\n");
            return new SmartTrack();
        }

        // Return the widget
        context.getLogger().log("widget details ==================>" +smartTrack);
        return smartTrack;
    }
}

package com.javaworld.awslambda.widget.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.javaworld.awslambda.widget.model.Widget;
import com.javaworld.awslambda.widget.model.WidgetRequest;

public class GetWidgetHandler implements RequestHandler<WidgetRequest, Widget> {
    @Override
    public Widget handleRequest(WidgetRequest widgetRequest, Context context) {
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
        Widget widget = mapper.load(Widget.class, widgetRequest.getId());
        if(widget == null) {
            // We did not find a widget with this ID, so return an empty Widget
            context.getLogger().log("No Widget found with ID: " + widgetRequest.getId() + "\n");
            return new Widget();
        }

        // Return the widget
        context.getLogger().log("widget details ==================>" +widget);
        return widget;
    }
}

package com.javaworld.awslambda.widget.handlers;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.javaworld.awslambda.widget.model.Widget;

public class CreateWidgetHandler implements RequestHandler<Widget, Widget>  {

    @Override
    public Widget handleRequest(Widget widget, Context context) {
        String DYNAMODB_TABLE_NAME = "widget";
        Regions REGION = Regions.US_WEST_2;
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(REGION));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        //---------create table if not exists----
        DynamoDBTableMapper<Widget,Long,?> table = mapper.newTableMapper(Widget.class);
        table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));

        if(widget != null) {
            try {
                table.saveIfNotExists(widget);
            } catch (ConditionalCheckFailedException e) {
                // handle already existing
                System.out.println(e);
            }
        }
        else {
            context.getLogger().log("widget has null values");
        }

        return widget;
    }

}

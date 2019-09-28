package com.javaworld.awslambda.widget.handlers;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaworld.awslambda.widget.model.SmartTrack;

public class DataCreationHandler implements RequestHandler<SmartTrack, SmartTrack>  {

    @Override
    public SmartTrack handleRequest(SmartTrack smartTrack, Context context) {
        Regions REGION = Regions.US_EAST_1;
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(REGION));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        //---------create table if not exists----
        DynamoDBTableMapper<SmartTrack,String,?> table = mapper.newTableMapper(SmartTrack.class);

        table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));
        JsonParser parser = new JsonParser();
        JsonObject jsonObject =parser.parse(smartTrack.toString()).getAsJsonObject();
        System.out.println(jsonObject);
        if(smartTrack != null) {
            try {
                table.saveIfNotExists(smartTrack);
            } catch (ConditionalCheckFailedException e) {
                System.out.println(e);
            }
        }
        else {
            context.getLogger().log("Pi_Values has null values");
        }
        return smartTrack;
    }

}

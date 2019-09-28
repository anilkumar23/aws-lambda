package com.javaworld.awslambda.widget.client;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaworld.awslambda.widget.model.SmartTrack;

@SuppressWarnings("deprecation")
public class LambdaClient {
    private static final SmartTrack smartTrack = new SmartTrack();

    public static void main(String[] args) {
        Regions REGION = Regions.US_EAST_1;
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(REGION));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

        //---------create table if not exists----
        DynamoDBTableMapper<SmartTrack,String,?> table = mapper.newTableMapper(SmartTrack.class);
        table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));

        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject =parser.parse(smartTrack.toString()).getAsJsonObject();
            System.out.println(jsonObject);
            mapper.save(smartTrack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

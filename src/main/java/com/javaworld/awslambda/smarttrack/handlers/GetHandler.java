package com.javaworld.awslambda.smarttrack.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaworld.awslambda.smarttrack.model.SmartTrack;
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetHandler implements RequestHandler<SmartTrackRequest, List<SmartTrack>> {
    @Override
    public List<SmartTrack> handleRequest(SmartTrackRequest smartTrackRequest, Context context) {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<SmartTrack> smartTrackList = new ArrayList<>();
        try {
            smartTrackList = getDataOfSpecificDeviceId(mapper, smartTrackRequest);
            if (smartTrackList!=null){
                return smartTrackList;
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private List<SmartTrack> getDataOfSpecificDeviceId (DynamoDBMapper mapper, SmartTrackRequest smartTrackRequest) throws Exception {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(smartTrackRequest.getDeviceId()));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("deviceId = :val1").withExpressionAttributeValues(eav);

        List<SmartTrack> smartTracks = mapper.scan(SmartTrack.class, scanExpression);
        List<SmartTrack> getList = new ArrayList<>();
        for (SmartTrack smartTrack : smartTracks) {
            JsonParser jsonParser = new JsonParser();
            JsonObject objectFromString = jsonParser.parse(smartTrack.toString()).getAsJsonObject();
            Gson gson = new Gson();
            smartTrack = gson.fromJson(objectFromString, SmartTrack.class);
            if (smartTrack.getTimestamp().contains(smartTrackRequest.getTimestamp())) {
                getList.add(smartTrack);
            }
        }
        return getList;
    }
}

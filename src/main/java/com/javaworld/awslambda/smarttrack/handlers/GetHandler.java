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
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import com.javaworld.awslambda.smarttrack.model.TTDPowerSupply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetHandler implements RequestHandler<SmartTrackRequest, List<TTDPowerSupply>> {
    @Override
    public List<TTDPowerSupply> handleRequest(SmartTrackRequest smartTrackRequest, Context context) {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<TTDPowerSupply> ttdPowerSupplies = new ArrayList<>();
        try {
            ttdPowerSupplies = getDataOfSpecificDeviceId(mapper, smartTrackRequest);
            if (ttdPowerSupplies != null) {
                return ttdPowerSupplies;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<TTDPowerSupply> getDataOfSpecificDeviceId(DynamoDBMapper mapper, SmartTrackRequest smartTrackRequest) throws Exception {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(smartTrackRequest.getDeviceId()));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("deviceId = :val1").withExpressionAttributeValues(eav);

        List<TTDPowerSupply> ttdPowerSupplies = mapper.scan(TTDPowerSupply.class, scanExpression);
        List<TTDPowerSupply> getList = new ArrayList<>();
        for (TTDPowerSupply ttdPowerSupply : ttdPowerSupplies) {
            JsonParser jsonParser = new JsonParser();
            JsonObject objectFromString = jsonParser.parse(ttdPowerSupply.toString()).getAsJsonObject();
            Gson gson = new Gson();
            ttdPowerSupply = gson.fromJson(objectFromString, TTDPowerSupply.class);
            if (ttdPowerSupply.getTimestamp().contains(smartTrackRequest.getTimestamp())) {
                getList.add(ttdPowerSupply);
            }
        }
        return getList;
    }
}

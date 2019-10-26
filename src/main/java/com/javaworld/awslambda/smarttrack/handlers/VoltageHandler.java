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
import com.javaworld.awslambda.smarttrack.exception.TTDCustomException;
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import com.javaworld.awslambda.smarttrack.model.TTDPowerSupply;
import com.javaworld.awslambda.smarttrack.model.Voltage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anil.saladi on 10/23/2019.
 */
public class VoltageHandler implements RequestHandler<SmartTrackRequest, List<Voltage>> {


    @Override
    public List<Voltage> handleRequest(SmartTrackRequest smartTrackRequest, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<Voltage> voltageList = new ArrayList<>();
        //SmartTrackRequest smartTrackRequest = new SmartTrackRequest(deviceId,timeStamp);
        try {
            voltageList = getVoltageList(mapper, smartTrackRequest);
            if (voltageList != null) {
                return voltageList;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new TTDCustomException(e.getMessage());
        }
    }

    private List<Voltage> getVoltageList(DynamoDBMapper mapper, SmartTrackRequest smartTrackRequest) throws Exception {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(smartTrackRequest.getDeviceId()));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("deviceId = :val1").withExpressionAttributeValues(eav);
        List<TTDPowerSupply> ttdPowerSupplyList = mapper.scan(TTDPowerSupply.class, scanExpression);
        List<TTDPowerSupply> getTtdPowerSupplyList = new ArrayList<>();
        List<Voltage> voltageList = new ArrayList<>();
        int count = 10;
        for (TTDPowerSupply ttdPowerSupply : ttdPowerSupplyList) {
            if (count!=0) {
                Gson gson = new Gson();
                ttdPowerSupply.setPower(ttdPowerSupply.getPower().replaceAll("'", ""));
                ttdPowerSupply.setEnergy(ttdPowerSupply.getEnergy().replaceAll("'", ""));
                JsonParser jsonParser = new JsonParser();

                JsonObject objectFromString = jsonParser.parse(ttdPowerSupply.toString()).getAsJsonObject();

                ttdPowerSupply = gson.fromJson(objectFromString, TTDPowerSupply.class);
                if (ttdPowerSupply.getTimestamp().contains(smartTrackRequest.getTimestamp())) {
                    getTtdPowerSupplyList.add(ttdPowerSupply);
                    String rphvol = null;
                    if (ttdPowerSupply.getPower() != null && ttdPowerSupply.getPower().contains("rphvol")) {
                        String[] s = ttdPowerSupply.getPower().split(",");
                        rphvol = s[1].replaceAll("rphvol:","");
                        count--;
                    }
                    Voltage voltage = new Voltage(rphvol, ttdPowerSupply.getTimestamp());
                    voltageList.add(voltage);
                }
            }else {
                break;
            }
        }

        return voltageList;
    }
}

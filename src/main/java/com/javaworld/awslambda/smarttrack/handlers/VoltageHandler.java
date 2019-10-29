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

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
        eav.put(":tStamp", new AttributeValue().withS(smartTrackRequest.getTimestamp()));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(tStamp, :tStamp)").withExpressionAttributeValues(eav);
        List<TTDPowerSupply> ttdPowerSupplyList = mapper.scan(TTDPowerSupply.class, scanExpression);
        List<TTDPowerSupply> getTtdPowerSupplyList = new ArrayList<>();
        List<Voltage> voltageList = new ArrayList<>();
        int count = 24;
        int c = 0;
        Map<String, String> map = new HashMap<>();
        String rphvol = "";
        for (TTDPowerSupply ttdPowerSupply : ttdPowerSupplyList) {
            if (count != 0) {
                Gson gson = new Gson();
                ttdPowerSupply.setPower(ttdPowerSupply.getPower().replaceAll("'", ""));
                ttdPowerSupply.setEnergy(ttdPowerSupply.getEnergy().replaceAll("'", ""));
                JsonParser jsonParser = new JsonParser();
                JsonObject objectFromString = jsonParser.parse(ttdPowerSupply.toString()).getAsJsonObject();
                ttdPowerSupply = gson.fromJson(objectFromString, TTDPowerSupply.class);

                if (ttdPowerSupply.gettStamp().contains(smartTrackRequest.getTimestamp())) {
                    getTtdPowerSupplyList.add(ttdPowerSupply);

                    if (ttdPowerSupply.getPower() != null && ttdPowerSupply.getPower().contains("rphvol")) {
                        String[] s = ttdPowerSupply.getPower().split(",");

                        if (count == 24) {
                            map.put(ttdPowerSupply.getDeviceId(), s[1].replaceAll("rphvol:", "").replace("V","").trim());
                        } else {
                            if (map.containsKey(ttdPowerSupply.getDeviceId())) {
                                String s3 =  map.get(ttdPowerSupply.getDeviceId());
                                map.put(ttdPowerSupply.getDeviceId(), s3.concat(", " + s[1].replaceAll("rphvol:", "").replace("V","").trim()));
                            } else if (c == 0) {
                                map.put(ttdPowerSupply.getDeviceId(), s[1].replaceAll("rphvol:", "").replace("V","").trim());
                                c++;
                            } else {
                                String newVol = s[1].replaceAll("rphvol:", "").replace("V","").trim();
                                map.put(ttdPowerSupply.getDeviceId(), newVol);
                            }
                        }
                        count--;
                    }
                }
            } else {
                break;
            }
        }
        for (Map.Entry<String, String> s1 : map.entrySet()) {
            String s4[] = s1.getValue().split(",");
            List<Double> list = new ArrayList<>();
            for (String d: s4) {
                list.add(Double.parseDouble(d.trim()));
            }
            Voltage voltage = new Voltage(list, s1.getKey());
            voltageList.add(voltage);
        }
        return voltageList;
    }
}

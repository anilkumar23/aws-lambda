package com.javaworld.awslambda.smarttrack.serviceImpl;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaworld.awslambda.smarttrack.exception.TTDCustomException;
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import com.javaworld.awslambda.smarttrack.model.TTDPowerSupply;
import com.javaworld.awslambda.smarttrack.model.Voltage;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@SuppressWarnings("deprecation")
@Service
public class SmartTrackImpl {
   // private static final Logger logger = LogManager.getLogger(SmartTrackImpl.class.getName());

    public boolean insertData(List<TTDPowerSupply> power) {
        Regions REGION = Regions.US_EAST_1;
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(REGION));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        try {
            DynamoDBTableMapper<TTDPowerSupply, String, ?> table = mapper.newTableMapper(TTDPowerSupply.class);
            table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));
//            logger.info("Connection to the DB started...");
//            logger.info("Connection successfully established...");
        } catch (Exception ex) {
//            logger.error("DBConnection failed due to " + ex.getMessage());
        }
        boolean isDataInserted = false;
        for (TTDPowerSupply ttdPowerSupply : power) {
            if (ttdPowerSupply != null) {
                try {
                    mapper.save(ttdPowerSupply);
//                    logger.info("Successfully inserted the data into DB...");
                    isDataInserted = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
//                    logger.error("Error occur while inserting data..." + ex.getMessage());
                }
            } else {
//                logger.info("Request Body has some null values...");
            }
        }
        return isDataInserted;
    }

   /* public List<TTDPowerSupply> getData(SmartTrackRequest smartTrackRequest) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<TTDPowerSupply> ttdPowerSupplyList = new ArrayList<>();
        try {
            ttdPowerSupplyList = getDataOfSpecificDeviceId(mapper, smartTrackRequest);
            if (ttdPowerSupplyList != null) {
                return ttdPowerSupplyList;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new TTDCustomException(e.getMessage());
        }

    }*/

    public List<Voltage> getVoltageData(String tStamp) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<Voltage> voltageList = new ArrayList<>();
        try {
            voltageList = getVoltageList(mapper, tStamp);
            if (voltageList != null) {
                return voltageList;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new TTDCustomException(e.getMessage());
        }

    }

    public List<TTDPowerSupply> getDeviceDataWithName(String deviceName) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(deviceName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("deviceName = :val1").withExpressionAttributeValues(eav);
        List<TTDPowerSupply> powerSupplyArrayList = mapper.scan(TTDPowerSupply.class, scanExpression);
        return powerSupplyArrayList;
    }

    private List<TTDPowerSupply> getDataOfSpecificDeviceId(DynamoDBMapper mapper, String deviceId) throws Exception {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(deviceId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("deviceId = :val1").withExpressionAttributeValues(eav);
        List<TTDPowerSupply> ttdPowerSupplyList = mapper.scan(TTDPowerSupply.class, scanExpression);
        List<TTDPowerSupply> getTtdPowerSupplyList = new ArrayList<>();
        for (TTDPowerSupply ttdPowerSupply : ttdPowerSupplyList) {
            Gson gson = new Gson();
            ttdPowerSupply.setPower(ttdPowerSupply.getPower().replaceAll("'", ""));
            ttdPowerSupply.setEnergy(ttdPowerSupply.getEnergy().replaceAll("'", ""));
            JsonParser jsonParser = new JsonParser();

            JsonObject objectFromString = jsonParser.parse(ttdPowerSupply.toString()).getAsJsonObject();

            ttdPowerSupply = gson.fromJson(objectFromString, TTDPowerSupply.class);
            getTtdPowerSupplyList.add(ttdPowerSupply);
        }
        return getTtdPowerSupplyList;
    }

    private List<Voltage> getVoltageList(DynamoDBMapper mapper, String timestamp) throws Exception {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":tStamp", new AttributeValue().withS(timestamp));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(tStamp, :tStamp)").withExpressionAttributeValues(eav);
        List<TTDPowerSupply> ttdPowerSupplyList = mapper.scan(TTDPowerSupply.class, scanExpression);
        List<TTDPowerSupply> getTtdPowerSupplyList = new ArrayList<>();
        List<Voltage> voltageList = new ArrayList<>();
        int count = 24;
        int c = 0;
        Map<String, String> map = new HashMap<>();
        for (TTDPowerSupply ttdPowerSupply : ttdPowerSupplyList) {
            if (count != 0) {
                Gson gson = new Gson();
                ttdPowerSupply.setPower(ttdPowerSupply.getPower().replaceAll("'", ""));
                ttdPowerSupply.setEnergy(ttdPowerSupply.getEnergy().replaceAll("'", ""));
                JsonParser jsonParser = new JsonParser();
                JsonObject objectFromString = jsonParser.parse(ttdPowerSupply.toString()).getAsJsonObject();
                ttdPowerSupply = gson.fromJson(objectFromString, TTDPowerSupply.class);

                if (ttdPowerSupply.gettStamp().contains(timestamp)) {
                    getTtdPowerSupplyList.add(ttdPowerSupply);

                    if (ttdPowerSupply.getPower() != null && ttdPowerSupply.getPower().contains("rphvol")) {
                        String[] s = ttdPowerSupply.getPower().split(",");
                        String rPhVol = s[1].replaceAll("rphvol:", "").replace("V", "").trim();
                        if (count == 24) {
                            map.put(ttdPowerSupply.getDeviceId(), rPhVol);
                        } else {
                            if (map.containsKey(ttdPowerSupply.getDeviceId())) {
                                String s3 = map.get(ttdPowerSupply.getDeviceId());
                                map.put(ttdPowerSupply.getDeviceId(), s3.concat(", " + rPhVol));
                            } else if (c == 0) {
                                map.put(ttdPowerSupply.getDeviceId(), rPhVol);
                                c++;
                            } else {
                                String newVol = rPhVol;
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
            for (String d : s4) {
                list.add(Double.parseDouble(d.trim()));
            }
            Voltage voltage = new Voltage(list, s1.getKey());
            voltageList.add(voltage);
        }
        return voltageList;
    }

}

//Inserting data with table ------------
/*
* DynamoDBTableMapper<SmartTrack,String,?> table = mapper.newTableMapper(SmartTrack.class);
table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));
table.saveIfNotExists(smartTrack);
* */
package com.javaworld.awslambda.smarttrack.serviceImpl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaworld.awslambda.smarttrack.exception.TTDCustomException;
import com.javaworld.awslambda.smarttrack.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@SuppressWarnings("deprecation")
@Service
public class SmartTrackImpl {
    // private static final Logger logger =
    // LogManager.getLogger(SmartTrackImpl.class.getName());

    public boolean insertData(List<DeviceMapping> power) {
        Regions REGION = Regions.US_EAST_1;
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(REGION));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        try {
            DynamoDBTableMapper<DeviceMapping, String, ?> table = mapper.newTableMapper(DeviceMapping.class);
            table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));
            // logger.info("Connection to the DB started...");
            // logger.info("Connection successfully established...");
        } catch (Exception ex) {
            // logger.error("DBConnection failed due to " + ex.getMessage());
        }
        boolean isDataInserted = false;
        for (DeviceMapping ttdPowerSupply : power) {
            if (ttdPowerSupply != null) {
                try {
                    mapper.save(ttdPowerSupply);
                    // logger.info("Successfully inserted the data into DB...");
                    isDataInserted = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // logger.error("Error occur while inserting data..." + ex.getMessage());
                }
            } else {
                // logger.info("Request Body has some null values...");
            }
        }
        return isDataInserted;
    }

    /*
     * public List<TTDPowerSupply> getData(SmartTrackRequest smartTrackRequest) {
     * AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
     * DynamoDBMapper mapper = new DynamoDBMapper(client);
     *
     * List<TTDPowerSupply> ttdPowerSupplyList = new ArrayList<>(); try {
     * ttdPowerSupplyList = getDataOfSpecificDeviceId(mapper, smartTrackRequest); if
     * (ttdPowerSupplyList != null) { return ttdPowerSupplyList; } else { return
     * null; } } catch (Exception e) { throw new TTDCustomException(e.getMessage());
     * }
     *
     * }
     */

    public DeviceVariables getVoltageData(SmartTrackRequest smartTrackRequest) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAT5KM23BCHXUNAR6C",
                "Vj6J6t6qbnjPq9WBwVxso44cVjH88dbai6xBJqPP");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
        // AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        // List<Voltage> voltageList = new ArrayList<>();
        DeviceVariables deviceVariables = new DeviceVariables();
        try {
            deviceVariables = getVoltageList(mapper, smartTrackRequest.getTimestamp(), smartTrackRequest.getDeviceName());
            if (deviceVariables != null) {
                return deviceVariables;
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
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("deviceName = :val1")
                .withExpressionAttributeValues(eav);
        List<TTDPowerSupply> powerSupplyArrayList = mapper.scan(TTDPowerSupply.class, scanExpression);
        return powerSupplyArrayList;
    }

    private List<TTDPowerSupply> getDataOfSpecificDeviceId(DynamoDBMapper mapper, String deviceId) throws Exception {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(deviceId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("deviceId = :val1")
                .withExpressionAttributeValues(eav);
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

    private DeviceVariables getVoltageList(DynamoDBMapper mapper, String timestamp, String deviceName) throws Exception {
        System.out.println("Entered getVoltageList method to fetch the voltage of devices =============");
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        List<TTDPowerSupply> getTtdPowerSupplyList = new ArrayList<>();
        List<Voltage> voltageList = new ArrayList<>();
        DeviceVariables deviceVariables = new DeviceVariables();
        Map<String, String[]> map = new HashMap<>();
        int count = 1;
        int c = 0;
        eav.put(":val1", new AttributeValue().withS(deviceName));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("deviceName = :val1")
                .withExpressionAttributeValues(eav);
        List<TTDPowerSupply> ttdPowerSupplyList = mapper.scan(TTDPowerSupply.class, scanExpression);

        for (TTDPowerSupply ttdPowerSupply : ttdPowerSupplyList) {
            if (ttdPowerSupply.gettStamp().contains(timestamp)) {
                Gson gson = new Gson();
                ttdPowerSupply.setPower(ttdPowerSupply.getPower().replaceAll("'", ""));
                ttdPowerSupply.setEnergy(ttdPowerSupply.getEnergy().replaceAll("'", ""));
                JsonParser jsonParser = new JsonParser();
                JsonObject objectFromString = jsonParser.parse(ttdPowerSupply.toString()).getAsJsonObject();
                ttdPowerSupply = gson.fromJson(objectFromString, TTDPowerSupply.class);
                getTtdPowerSupplyList.add(ttdPowerSupply);
                if (ttdPowerSupply.getPower() != null && ttdPowerSupply.getPower().contains("rphvol")) {
                    String[] s = ttdPowerSupply.getPower().split(",");
                    String rPhVol = s[1].replaceAll("rphvol:", "").replace("V", "").trim();
                    String rphcu = s[4].replaceAll("rphcu:", "").replace("V", "").trim();
                    String rphpf = s[13].replaceAll("rphpf:", "").replace("V", "").trim();
                    if (count == 1) {
                        map.put(ttdPowerSupply.getDeviceId(), new String[]{rPhVol, rphcu, rphpf});
                        count = 0;
                    } else {
                        if (map.containsKey(ttdPowerSupply.getDeviceId())) {
                            String s3[] = map.get(ttdPowerSupply.getDeviceId());
                            map.put(ttdPowerSupply.getDeviceId(), new String[]{s3[0].concat(", " + rPhVol),s3[1].concat(", " + rphcu),
                                    s3[2].concat(", " + rphpf)});
                        } else if (c == 0) {
                            map.put(ttdPowerSupply.getDeviceId(), new String[]{rPhVol,rphcu,rphpf});
                            c++;
                        } else {
                            String newVol = rPhVol;
                            map.put(ttdPowerSupply.getDeviceId(), new String[]{rPhVol,rphcu,rphpf});
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, String[]> s1 : map.entrySet()) {
            String s4[] = s1.getValue();
            Set<Double> set = new HashSet<>();
            Set<Double> currSet = new HashSet<>();
            List<Double> pfSet = new ArrayList<>();
            for (int i=0; i<s4.length; i++) {
                String s5[] = s4[i].split(",");
                for (String s6 : s5) {
                    int setFinalCount = set.size();
                    int currFinalCount = currSet.size();
                    int pfFinalSet = pfSet.size();
                    if (setFinalCount <= 23 && i==0) {
                        set.add(Double.parseDouble(s6.trim()));
                        if(setFinalCount == 23) break;
                    } else if (currFinalCount <= 23 && i==1){
                        currSet.add(Double.parseDouble(s6.trim()));
                        if(currFinalCount == 23) break;
                    }else if(pfFinalSet <= 23 && i==2){
                        pfSet.add(Double.parseDouble(s6.trim()));
                        if(pfFinalSet == 23) break;
                    }
                }
            }

            Voltage voltage = new Voltage(set,s1.getKey());
            Current current = new Current(currSet, s1.getKey());
            PowerFactor powerFactor = new PowerFactor(pfSet, s1.getKey());
            Set<Voltage> voltages = new HashSet<>();
            voltages.add(voltage);
            Set<Current> currents = new HashSet<>();
            currents.add(current);
            Set<PowerFactor> powerFactors = new HashSet<>();
            powerFactors.add(powerFactor);
            deviceVariables = new DeviceVariables(voltages, currents, powerFactors);
        }
        System.out.println("fetch voltage list functionality completed =======================");
        return deviceVariables;
    }

    public DeviceName getDeviceNames(String deviceName) {
        deviceName=deviceName.toLowerCase();
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAT5KM23BCHXUNAR6C",
                "Vj6J6t6qbnjPq9WBwVxso44cVjH88dbai6xBJqPP");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":deviceName", new AttributeValue().withS(deviceName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(deviceName, :deviceName)").withExpressionAttributeValues(eav);

        List<DeviceMapping> deviceList = mapper.scan(DeviceMapping.class, scanExpression);
        DeviceName deviceName1=null;
        for (DeviceMapping s:deviceList) {
            String[] str = s.getDeviceName().split(",");
            deviceName1 = new DeviceName(str);
        }
        return deviceName1;
    }
}

// Inserting data with table ------------
/*
 * DynamoDBTableMapper<SmartTrack,String,?> table =
 * mapper.newTableMapper(SmartTrack.class); table.createTableIfNotExists(new
 * ProvisionedThroughput(25L, 25L)); table.saveIfNotExists(smartTrack);
 */
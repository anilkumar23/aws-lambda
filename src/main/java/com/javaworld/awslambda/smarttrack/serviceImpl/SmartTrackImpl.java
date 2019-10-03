package com.javaworld.awslambda.smarttrack.serviceImpl;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaworld.awslambda.smarttrack.model.SmartTrack;
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@SuppressWarnings("deprecation")
@Service
public class SmartTrackImpl {
    private static final Logger logger = LogManager.getLogger(SmartTrackImpl.class.getName());

    public boolean insertData(SmartTrack smartTrack) {
        DynamoDBMapper mapper=null;
        try {
            logger.info("Connection to the DB started...");
            Regions REGION = Regions.US_EAST_1;
            AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
            dynamoDBClient.setRegion(Region.getRegion(REGION));
            mapper = new DynamoDBMapper(dynamoDBClient);
            logger.info("Connection successfully established...");
        }catch (Exception ex){
            logger.error("DBConnection failed due to "+ex.getMessage());
        }
        boolean isDataInserted =false;
        if(smartTrack != null) {
            try {
                mapper.save(smartTrack);
                logger.info("Successfully inserted the data into DB...");
                isDataInserted=true;
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error occur while inserting data..." + ex.getMessage());
            }
        }
        else {
            logger.info("Request Body has some null values...");
        }
        return isDataInserted;
    }

    public List<SmartTrack> getData(SmartTrackRequest smartTrackRequest){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<SmartTrack> smartTrackList = new ArrayList<>();
        try {
            smartTrackList = getDataOfSpecificDeviceId(mapper,smartTrackRequest.getDeviceId());
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

    private List<SmartTrack> getDataOfSpecificDeviceId (DynamoDBMapper mapper, String value) throws Exception {

        System.out.println("FindBooksPricedLessThanSpecifiedValue: Scan ProductCatalog.");

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(value));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("deviceId = :val1").withExpressionAttributeValues(eav);

        List<SmartTrack> smartTracks = mapper.scan(SmartTrack.class, scanExpression);
        List<SmartTrack> getList = new ArrayList<>();
        for (SmartTrack smartTrack : smartTracks) {
            JsonParser jsonParser = new JsonParser();
            JsonObject objectFromString = jsonParser.parse(smartTrack.toString()).getAsJsonObject();
            Gson gson = new Gson();
            smartTrack = gson.fromJson(objectFromString, SmartTrack.class);
            getList.add(smartTrack);
        }
        return getList;
    }

}

//Inserting data with table ------------
/*
* DynamoDBTableMapper<SmartTrack,String,?> table = mapper.newTableMapper(SmartTrack.class);
table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));
table.saveIfNotExists(smartTrack);
* */
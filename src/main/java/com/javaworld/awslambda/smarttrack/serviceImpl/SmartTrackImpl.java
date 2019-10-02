package com.javaworld.awslambda.smarttrack.serviceImpl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaworld.awslambda.smarttrack.model.SmartTrack;
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import org.springframework.stereotype.Service;
import com.amazonaws.services.lambda.runtime.Context;

import java.util.*;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@Service
public class SmartTrackImpl {
  //  private static final Logger logger = LogManager.getLogger(SmartTrackImpl.class.getName());

    public boolean handleRequest(SmartTrack smartTrack) {
        Regions REGION = Regions.US_EAST_1;
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(REGION));
        BasicAWSCredentials awscreds = new BasicAWSCredentials("AKIAT5KM23BCFARSHF7Q",
                "ou5vtTIzRD2WPFD2hMzYsf1Sbite4dWE3FPGEJxK");

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        //---------create table if not exists----
        DynamoDBTableMapper<SmartTrack,String,?> table = mapper.newTableMapper(SmartTrack.class);

        table.createTableIfNotExists(new ProvisionedThroughput(25L, 25L));
        boolean isDataInserted =false;
        if(smartTrack != null) {
            try {
                table.saveIfNotExists(smartTrack);
                isDataInserted=true;
            } catch (ConditionalCheckFailedException e) {
                System.out.println(e);
            }
        }
        else {
            System.out.println("data values are null ====== >");
        }
        return isDataInserted;
    }

    public List<SmartTrack> handleRequest(SmartTrackRequest smartTrackRequest, Context context){
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

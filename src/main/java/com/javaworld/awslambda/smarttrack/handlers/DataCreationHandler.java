package com.javaworld.awslambda.smarttrack.handlers;

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
import com.javaworld.awslambda.smarttrack.model.SmartTrack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DataCreationHandler implements RequestHandler<SmartTrack, SmartTrack>  {
    private final Logger logger = LogManager.getLogger(DataCreationHandler.class.getName());
    @Override
    public SmartTrack handleRequest(SmartTrack smartTrack, Context context) {
        DynamoDBTableMapper<SmartTrack,String,?> table = null;
        try {
            logger.info("Connection to the DB started...");
            Regions REGION = Regions.US_EAST_1;
            AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
            dynamoDBClient.setRegion(Region.getRegion(REGION));
            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
            table = mapper.newTableMapper(SmartTrack.class);
            logger.info("Connection successfully established...");
        }catch (Exception ex){
            logger.error("DBConnection failed due to "+ex.getMessage());
        }
        if(smartTrack != null) {
            try {
                table.saveIfNotExists(smartTrack);
                logger.info("Successfully inserted the data into DB...");
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error occur while inserting data..." + ex.getMessage());
            }
        }
        else {
            logger.info("Request Body has some null values...");
        }
        return smartTrack;
    }

}

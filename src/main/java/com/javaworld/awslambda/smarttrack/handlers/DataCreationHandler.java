package com.javaworld.awslambda.smarttrack.handlers;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.javaworld.awslambda.smarttrack.model.TTDPowerSupply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataCreationHandler implements RequestHandler<TTDPowerSupply, TTDPowerSupply> {
    private final Logger logger = LogManager.getLogger(DataCreationHandler.class.getName());
    @Override
    public TTDPowerSupply handleRequest(TTDPowerSupply ttdPowerSupply, Context context) {
        DynamoDBTableMapper<TTDPowerSupply,String,?> table = null;
        try {
            logger.info("Connection to the DB started...");
            context.getLogger().log("Connection to the DB started...");
            Regions REGION = Regions.US_EAST_1;
            AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
            dynamoDBClient.setRegion(Region.getRegion(REGION));
            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
            table = mapper.newTableMapper(TTDPowerSupply.class);
            logger.info("Connection successfully established...");
            context.getLogger().log("Connection successfully established...");
        }catch (Exception ex){
            logger.error("DBConnection failed due to "+ex.getMessage());
            context.getLogger().log("DBConnection failed due to "+ex.getMessage());
        }
        if(ttdPowerSupply != null) {
            try {
                table.saveIfNotExists(ttdPowerSupply);
                logger.info("Successfully inserted the data into DB...");
                context.getLogger().log("Successfully inserted the data into DB...");
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error occur while inserting data..." + ex.getMessage());
                context.getLogger().log("Error occur while inserting data..." + ex.getMessage());
            }
        }
        else {
            logger.info("Request Body has some null values...");
            context.getLogger().log("Request Body has some null values...");
        }
        return ttdPowerSupply;
    }

}

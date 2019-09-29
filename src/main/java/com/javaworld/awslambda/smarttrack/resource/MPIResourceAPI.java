package com.javaworld.awslambda.smarttrack.resource;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import com.javaworld.awslambda.smarttrack.model.SmartTrack;
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import com.javaworld.awslambda.smarttrack.serviceImpl.SmartTrackImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@RestController
@RequestMapping("/message")
public class MPIResourceAPI {

    @Autowired
    SmartTrackImpl smartTrackImpl;

    @RequestMapping(value = "/send", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpStatus insertData (@RequestBody SmartTrack smartTrack){
        boolean isDataInserted = smartTrackImpl.handleRequest(smartTrack);
        return isDataInserted ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<SmartTrack> getData (@RequestBody SmartTrackRequest smartTrackRequest){

/*        Regions REGION = Regions.US_EAST_1;
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(REGION));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);*/

      /*  DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
                new ProfileCredentialsProvider()));
        ArrayList<AttributeDefinition> attributeDefinitions = new
                ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("id")
                .withAttributeType("S"));

        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("deviceId")
                .withAttributeType("S"));

        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("deviceName")
                .withAttributeType("S"));

        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("timestamp")
                .withAttributeType("S"));

        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("humidity")
                .withAttributeType("S"));

        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("temperature")
                .withAttributeType("S"));

// Key schema of the table
        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement()
                .withAttributeName("id")
                .withKeyType(KeyType.HASH));              //Partition key

        tableKeySchema.add(new KeySchemaElement()
                .withAttributeName("deviceId")
                .withKeyType(KeyType.RANGE));             //Sort key

// Wind index
        GlobalSecondaryIndex windIndex = new GlobalSecondaryIndex()
                .withIndexName("WindIndex")
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits((long) 10)
                        .withWriteCapacityUnits((long) 1))
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL));

        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
        indexKeySchema.add(new KeySchemaElement()
                .withAttributeName("deviceId")
                .withKeyType(KeyType.HASH));              //Partition key

        indexKeySchema.add(new KeySchemaElement()
                .withAttributeName("timestamp")
                .withKeyType(KeyType.RANGE));             //Sort key

        windIndex.setKeySchema(indexKeySchema);
        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName("ClimateInfo")
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits((long) 5)
                        .withWriteCapacityUnits((long) 1))
                .withAttributeDefinitions(attributeDefinitions)
                .withKeySchema(tableKeySchema)
                .withGlobalSecondaryIndexes(windIndex);
        Table table = dynamoDB.createTable(createTableRequest);

        Table indexTable = dynamoDB.getTable("ClimateInfo");
        TableDescription tableDesc = indexTable.describe();
        Iterator<GlobalSecondaryIndexDescription> gsiIter =
                tableDesc.getGlobalSecondaryIndexes().iterator();

        while (gsiIter.hasNext()) {
            GlobalSecondaryIndexDescription gsiDesc = gsiIter.next();
            System.out.println("Index data " + gsiDesc.getIndexName() + ":");
            Iterator<KeySchemaElement> kse7Iter = gsiDesc.getKeySchema().iterator();

            while (kse7Iter.hasNext()) {
                KeySchemaElement kse = kse7Iter.next();
                System.out.printf("\t%s: %s\n", kse.getAttributeName(), kse.getKeyType());
            }
            Projection projection = gsiDesc.getProjection();
            System.out.println("\tProjection type: " + projection.getProjectionType());

            if (projection.getProjectionType().toString().equals("INCLUDE")) {
                System.out.println("\t\tNon-key projected attributes: "
                        + projection.getNonKeyAttributes());
            }

        }

        Index index = indexTable.getIndex("WindIndex");
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#d = :v_deviceId")
                .withNameMap(new NameMap()
                        .with("#d", "deviceId"))
                .withValueMap(new ValueMap()
                        .withString(":deviceId","1"));

        ItemCollection<QueryOutcome> items = index.query(spec);
        Iterator<Item> iter = items.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next().toJSONPretty());
        }*/

        return smartTrackImpl.handleRequest(smartTrackRequest,null);
    }
}

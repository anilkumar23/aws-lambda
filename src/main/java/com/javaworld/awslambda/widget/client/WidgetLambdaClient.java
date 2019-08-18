package com.javaworld.awslambda.widget.client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.javaworld.awslambda.widget.model.Widget;

public class WidgetLambdaClient {

    public static void main(String[] args) {
        // Setup credentials
        Widget widget = new Widget("2",";lkj");

        /*BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIARLAXGQRPIC3XGNZE", "aM93TiDT1Lxfl47u+9+3cJS/VMUFTJdkL3h/17i4");

        // Create an AWSLambda client
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_WEST_2).build();

*/
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        //String s = String.valueOf(client.describeTable("widget"));
        //System.out.println("table details =============>" + s);
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        if(widget != null) {
            //context.getLogger().log("widget details ==================>" +widget);
            widget = mapper.load(Widget.class, widget.getId());
            mapper.save(widget);
        }
        else {
          //  context.getLogger().log("widget has null values");
        }


        // Create an InvokeRequest
       /* InvokeRequest request = new InvokeRequest()
                .withFunctionName("get-widget")
                .withPayload("{ \"id\": \"2\"}");

        try {

            // Execute the InvokeRequest
            InvokeResult result = lambda.invoke(request);

            // We should validate the response
            System.out.println("Status Code: " + result.getStatusCode());

            // Get the response as JSON
            String json = new String(result.getPayload().array(), "UTF-8");

            // Show the response; we could use a library like Jackson to convert this to an object
            System.out.println(json);
        } catch(Exception e) {
            e.printStackTrace();;
        }*/
    }
}

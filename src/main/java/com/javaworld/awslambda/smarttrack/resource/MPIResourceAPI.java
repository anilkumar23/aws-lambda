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
        return smartTrackImpl.handleRequest(smartTrackRequest,null);
    }
}

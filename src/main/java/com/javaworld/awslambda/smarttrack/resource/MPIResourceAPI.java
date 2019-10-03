package com.javaworld.awslambda.smarttrack.resource;

import com.javaworld.awslambda.smarttrack.model.SmartTrack;
import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import com.javaworld.awslambda.smarttrack.serviceImpl.SmartTrackImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@RestController
@RequestMapping("/message")
public class MPIResourceAPI {
    private final Logger logger = LogManager.getLogger(MPIResourceAPI.class.getName());

    @Autowired
    SmartTrackImpl smartTrackImpl;

    @RequestMapping(value = "/send", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpStatus insertData(@RequestBody SmartTrack smartTrack) {
        logger.info("Entered insertData method to dump data into DB with the following data..." + smartTrack.toString());
        boolean isDataInserted = smartTrackImpl.insertData(smartTrack);
        return isDataInserted ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<SmartTrack> getData(@RequestBody SmartTrackRequest smartTrackRequest) {
        logger.info("Entered getData method for retrieving the requested data..." + smartTrackRequest.toString());
        try {
            List<SmartTrack> smartTrackList = smartTrackImpl.getData(smartTrackRequest);
            logger.info("Successfully fetched the requested data...");
            return smartTrackList;
        } catch (Exception ex) {
            logger.error("Error occur while fetching smart-track details..." + ex.getMessage());
            return null;
        }
    }
}

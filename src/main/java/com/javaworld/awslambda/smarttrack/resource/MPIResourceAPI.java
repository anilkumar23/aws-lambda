package com.javaworld.awslambda.smarttrack.resource;

import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import com.javaworld.awslambda.smarttrack.model.TTDPowerSupply;
import com.javaworld.awslambda.smarttrack.serviceImpl.SmartTrackImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public HttpStatus insertData(@RequestBody TTDPowerSupply ttdPowerSupply) {
        logger.info("Entered insertData method to dump data into DB with the following data..." + ttdPowerSupply.toString());
        boolean isDataInserted = smartTrackImpl.insertData(ttdPowerSupply);
        return isDataInserted ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<TTDPowerSupply> getData(@RequestBody SmartTrackRequest smartTrackRequest) {
        logger.info("Entered getData method for retrieving the requested data..." + smartTrackRequest.toString());
        try {
            List<TTDPowerSupply> ttdPowerSupplyList = smartTrackImpl.getData(smartTrackRequest);
            logger.info("Successfully fetched the requested data...");
            return ttdPowerSupplyList;
        } catch (Exception ex) {
            logger.error("Error occur while fetching TTDPowerSupply details..." + ex);
            return null;
        }
    }

    @RequestMapping(value = "/getDeviceListByName/{deviceName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<TTDPowerSupply> getDeviceDataBasedOnName(@PathVariable String deviceName) {
        try {
            List<TTDPowerSupply> ttdPowerSupplyList = smartTrackImpl.getDeviceDataWithName(deviceName);
            logger.info("Successfully fetched the requested data...");
            return ttdPowerSupplyList;
        } catch (Exception ex) {
            logger.error("Error occur while fetching TTDPowerSupply details..." + ex);
            return null;
        }
    }
}

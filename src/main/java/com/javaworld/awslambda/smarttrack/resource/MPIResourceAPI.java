package com.javaworld.awslambda.smarttrack.resource;

import com.javaworld.awslambda.smarttrack.model.SmartTrackRequest;
import com.javaworld.awslambda.smarttrack.model.TTDPowerSupply;
import com.javaworld.awslambda.smarttrack.model.Voltage;
import com.javaworld.awslambda.smarttrack.serviceImpl.SmartTrackImpl;
import com.javaworld.awslambda.smarttrack.util.SmartTrackUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by anil.saladi on 9/28/2019.
 */
@RestController
@RequestMapping("/message")
public class MPIResourceAPI {
   // private final Logger logger = LogManager.getLogger(MPIResourceAPI.class.getName());

    @Autowired
    SmartTrackImpl smartTrackImpl;

    @RequestMapping(value = "/send", method = RequestMethod.POST, headers = "Accept=application/json")
    public HttpStatus insertData(@RequestBody List<TTDPowerSupply> ttdPowerSupplyList) {
       // logger.info("Entered insertData method to dump data into DB with the following data..." + ttdPowerSupply.toString());
        boolean isDataInserted = smartTrackImpl.insertData(ttdPowerSupplyList);
        return isDataInserted ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

/*    @RequestMapping(value = "/get", method = RequestMethod.POST, headers = "Accept=application/json")
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
    }*/


    @PostMapping(value = "/getVoltage" , consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Voltage> getVoltageData(@RequestBody SmartTrackRequest smartTrackRequest, HttpServletResponse response) {
      //  logger.info("Entered getData method for retrieving the requested data...");
        try {
            SmartTrackUtils.setResponseHeader(response);
            List<Voltage> voltageList = smartTrackImpl.getVoltageData(smartTrackRequest.getTimestamp());
            //logger.info("Successfully fetched the requested data...");
            return voltageList;
        } catch (Exception ex) {
          //  logger.error("Error occur while fetching TTDPowerSupply details..." + ex);
            return null;
        }
    }


    @RequestMapping(value = "/getDeviceListByName/{deviceName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<TTDPowerSupply> getDeviceDataBasedOnName(@PathVariable String deviceName) {
        try {
            List<TTDPowerSupply> ttdPowerSupplyList = smartTrackImpl.getDeviceDataWithName(deviceName);
           // logger.info("Successfully fetched the requested data...");
            return ttdPowerSupplyList;
        } catch (Exception ex) {
          //  logger.error("Error occur while fetching TTDPowerSupply details..." + ex);
            return null;
        }
    }
}

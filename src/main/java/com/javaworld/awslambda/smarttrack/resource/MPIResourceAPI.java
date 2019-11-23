package com.javaworld.awslambda.smarttrack.resource;

import com.javaworld.awslambda.smarttrack.model.*;
import com.javaworld.awslambda.smarttrack.serviceImpl.SmartTrackImpl;
import com.javaworld.awslambda.smarttrack.util.SmartTrackUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
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
    public HttpStatus insertData(@RequestBody List<DeviceMapping> ttdPowerSupplyList) {
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


    @RequestMapping(value = "/getVoltage" ,method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Voltage> getVoltageData(@RequestBody SmartTrackRequest smartTrackRequest, HttpServletResponse response) {
      //  logger.info("Entered getData method for retrieving the requested data...");
        System.out.println("Entered getData method for retrieving the requested data...");
        try {
            SmartTrackUtils.setResponseHeader(response);
            List<Voltage> voltageList = smartTrackImpl.getVoltageData(smartTrackRequest);
            System.out.println("Successfully fetched the requested data...");
            //logger.info("Successfully fetched the requested data...");
            return voltageList;
        } catch (Exception ex) {
          //  logger.error("Error occur while fetching TTDPowerSupply details..." + ex);
            return null;
        }
    }

    @RequestMapping(value = "/getDevices" , consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceName getDevices(@RequestParam String subStation, HttpServletResponse response) {
        System.out.println("Entered getData method for retrieving the requested data...");
        try {
            SmartTrackUtils.setResponseHeader(response);
            DeviceName voltageList = smartTrackImpl.getDeviceNames(subStation);
            return voltageList;
        } catch (Exception ex) {
            return null;
        }
    }


    @RequestMapping(value = "/getDeviceListByName", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<TTDPowerSupply> getDeviceDataBasedOnName(@RequestParam String deviceName) {
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

package com.javaworld.awslambda.smarttrack.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.javaworld.awslambda.smarttrack.model.TTDPowerSupply;
import com.javaworld.awslambda.smarttrack.model.Voltage;

import java.util.List;

/**
 * Created by anil.saladi on 10/23/2019.
 */
public class VoltageHandler implements RequestHandler<String, List<Voltage>> {

    @Override
    public List<Voltage> handleRequest(String s, Context context) {
        return null;
    }
}

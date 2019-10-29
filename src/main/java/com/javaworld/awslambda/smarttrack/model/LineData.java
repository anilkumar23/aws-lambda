package com.javaworld.awslambda.smarttrack.model;

import java.util.List;

/**
 * Created by anil.saladi on 10/24/2019.
 */
public class LineData {
    private List<Voltage> voltageList;

    public List<Voltage> getVoltageList() {
        return voltageList;
    }

    public void setVoltageList(List<Voltage> voltageList) {
        this.voltageList = voltageList;
    }

    @Override
    public String toString() {
        return "{" +
                "LineData:" + voltageList +
                '}';
    }
}

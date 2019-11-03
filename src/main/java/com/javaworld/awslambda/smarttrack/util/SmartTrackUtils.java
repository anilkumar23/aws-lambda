package com.javaworld.awslambda.smarttrack.util;

import javax.servlet.http.HttpServletResponse;

public class SmartTrackUtils {

    public static void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key");

    }
}

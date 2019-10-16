package com.javaworld.awslambda.smarttrack.exception;

/**
 * Created by anil.saladi on 10/16/2019.
 */
public class TTDCustomException extends RuntimeException {

    public TTDCustomException() {
    }

    public TTDCustomException(String message) {
        super(message);
    }

}

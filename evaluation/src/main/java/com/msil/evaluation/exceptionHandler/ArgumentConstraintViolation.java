package com.msil.evaluation.exceptionHandler;

public class ArgumentConstraintViolation extends RuntimeException{
    public ArgumentConstraintViolation(String message) {
        super(message);
    }
}

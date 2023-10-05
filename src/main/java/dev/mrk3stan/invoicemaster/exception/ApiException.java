package dev.mrk3stan.invoicemaster.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}

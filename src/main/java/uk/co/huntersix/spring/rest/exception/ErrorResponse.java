package uk.co.huntersix.spring.rest.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private HttpStatus status;

    private String message;
    private String debugMessage;

    ErrorResponse(HttpStatus status) {

        this.status = status;
    }

    ErrorResponse(HttpStatus status, Throwable ex) {
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ErrorResponse(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }


}



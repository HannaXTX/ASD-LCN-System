package me.hkaibni.dto.response;

import java.time.LocalDateTime;

public class ApiResponse {

    private int status;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ApiResponse(int status, String message, Object data,LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public ApiResponse(int status, String message, Object data,LocalDateTime timestamp,String token) {

        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
        this.token = token;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
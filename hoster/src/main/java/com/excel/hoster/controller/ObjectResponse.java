package com.excel.hoster.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ObjectResponse<ResponseObject> {
    private int responseStatus;
    private String responseDetail;
    private String responseTime;
    private ResponseObject responseObject;

    public ObjectResponse(int responseStatus, String responseDetail, ResponseObject responseObject) {
        this.responseStatus = responseStatus;
        this.responseDetail = responseDetail;
        this.responseObject = responseObject;
        this.responseTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseDetail() {
        return responseDetail;
    }

    public void setResponseDetail(String responseDetail) {
        this.responseDetail = responseDetail;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public ResponseObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(ResponseObject responseObject) {
        this.responseObject = responseObject;
    }
}

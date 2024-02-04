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

    public String getResponseDetail() {
        return responseDetail;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public ResponseObject getResponseObject() {
        return responseObject;
    }
}

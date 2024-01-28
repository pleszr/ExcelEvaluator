package com.excel.hoster;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ExcelFileUploadResponse {
    private int responseStatus;
    private String responseDetail;
    private String responseTime;
    private ExcelFile excelFile;

    public ExcelFileUploadResponse(int responseStatus, String responseDetail, ExcelFile excelFile) {
        this.responseStatus = responseStatus;
        this.responseDetail = responseDetail;
        this.excelFile = excelFile;
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

    public ExcelFile getExcelFile() {
        return excelFile;
    }
}

package com.heapdragon.lots;

/**
 * Created by Francesco on 2016-12-23.
 */

public class Log {

    private String subject;
    private String message;
    private String siteKey;
    private String lotKey;

    public Log(String subject, String message, String siteKey, String lotKey) {
        this.subject = subject;
        this.message = message;
        this.siteKey = siteKey;
        this.lotKey = lotKey;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getLotKey() {
        return lotKey;
    }

    public void setLotKey(String lotKey) {
        this.lotKey = lotKey;
    }
}

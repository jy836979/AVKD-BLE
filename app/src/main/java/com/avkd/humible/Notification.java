package com.avkd.humible;

public class Notification {

    String deviceName;
    String ago;
    String message;

    public Notification(String deviceName, String ago, String message) {
        this.deviceName = deviceName;
        this.ago = ago;
        this.message = message;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAgo() {
        return ago;
    }

    public void setAgo(String ago) {
        this.ago = ago;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

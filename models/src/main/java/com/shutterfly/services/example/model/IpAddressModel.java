package com.shutterfly.services.example.model;
public class IpAddressModel {
    private String ipAddress;

    /**
     * get field
     *
     * @return ipAddress
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * set field
     *
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public IpAddressModel(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}

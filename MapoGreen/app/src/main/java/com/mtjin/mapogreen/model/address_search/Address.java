package com.mtjin.mapogreen.model.address_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("region_1depth_name")
    @Expose
    private String region1depthName;
    @SerializedName("region_2depth_name")
    @Expose
    private String region2depthName;
    @SerializedName("region_3depth_name")
    @Expose
    private String region3depthName;
    @SerializedName("region_3depth_h_name")
    @Expose
    private String region3depthHName;
    @SerializedName("h_code")
    @Expose
    private String hCode;
    @SerializedName("b_code")
    @Expose
    private String bCode;
    @SerializedName("mountain_yn")
    @Expose
    private String mountainYn;
    @SerializedName("main_address_no")
    @Expose
    private String mainAddressNo;
    @SerializedName("sub_address_no")
    @Expose
    private String subAddressNo;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("x")
    @Expose
    private String x;
    @SerializedName("y")
    @Expose
    private String y;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getRegion1depthName() {
        return region1depthName;
    }

    public void setRegion1depthName(String region1depthName) {
        this.region1depthName = region1depthName;
    }

    public String getRegion2depthName() {
        return region2depthName;
    }

    public void setRegion2depthName(String region2depthName) {
        this.region2depthName = region2depthName;
    }

    public String getRegion3depthName() {
        return region3depthName;
    }

    public void setRegion3depthName(String region3depthName) {
        this.region3depthName = region3depthName;
    }

    public String getRegion3depthHName() {
        return region3depthHName;
    }

    public void setRegion3depthHName(String region3depthHName) {
        this.region3depthHName = region3depthHName;
    }

    public String getHCode() {
        return hCode;
    }

    public void setHCode(String hCode) {
        this.hCode = hCode;
    }

    public String getBCode() {
        return bCode;
    }

    public void setBCode(String bCode) {
        this.bCode = bCode;
    }

    public String getMountainYn() {
        return mountainYn;
    }

    public void setMountainYn(String mountainYn) {
        this.mountainYn = mountainYn;
    }

    public String getMainAddressNo() {
        return mainAddressNo;
    }

    public void setMainAddressNo(String mainAddressNo) {
        this.mainAddressNo = mainAddressNo;
    }

    public String getSubAddressNo() {
        return subAddressNo;
    }

    public void setSubAddressNo(String subAddressNo) {
        this.subAddressNo = subAddressNo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}

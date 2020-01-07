package com.mtjin.mapogreen.model.address_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoadAddress {

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
    @SerializedName("road_name")
    @Expose
    private String roadName;
    @SerializedName("underground_yn")
    @Expose
    private String undergroundYn;
    @SerializedName("main_building_no")
    @Expose
    private String mainBuildingNo;
    @SerializedName("sub_building_no")
    @Expose
    private String subBuildingNo;
    @SerializedName("building_name")
    @Expose
    private String buildingName;
    @SerializedName("zone_no")
    @Expose
    private String zoneNo;
    @SerializedName("y")
    @Expose
    private String y;
    @SerializedName("x")
    @Expose
    private String x;

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

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getUndergroundYn() {
        return undergroundYn;
    }

    public void setUndergroundYn(String undergroundYn) {
        this.undergroundYn = undergroundYn;
    }

    public String getMainBuildingNo() {
        return mainBuildingNo;
    }

    public void setMainBuildingNo(String mainBuildingNo) {
        this.mainBuildingNo = mainBuildingNo;
    }

    public String getSubBuildingNo() {
        return subBuildingNo;
    }

    public void setSubBuildingNo(String subBuildingNo) {
        this.subBuildingNo = subBuildingNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getZoneNo() {
        return zoneNo;
    }

    public void setZoneNo(String zoneNo) {
        this.zoneNo = zoneNo;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

}
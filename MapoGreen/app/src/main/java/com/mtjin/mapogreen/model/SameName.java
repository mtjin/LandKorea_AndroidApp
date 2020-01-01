package com.mtjin.mapogreen.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SameName {

    @SerializedName("region")
    @Expose
    private List<Object> region = null;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("selected_region")
    @Expose
    private String selectedRegion;

    public List<Object> getRegion() {
        return region;
    }

    public void setRegion(List<Object> region) {
        this.region = region;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSelectedRegion() {
        return selectedRegion;
    }

    public void setSelectedRegion(String selectedRegion) {
        this.selectedRegion = selectedRegion;
    }

}

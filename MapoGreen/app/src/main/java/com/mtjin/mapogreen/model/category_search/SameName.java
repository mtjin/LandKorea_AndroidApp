package com.mtjin.mapogreen.model.category_search;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SameName implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.region);
        dest.writeString(this.keyword);
        dest.writeString(this.selectedRegion);
    }

    public SameName() {
    }

    protected SameName(Parcel in) {
        this.region = new ArrayList<Object>();
        in.readList(this.region, Object.class.getClassLoader());
        this.keyword = in.readString();
        this.selectedRegion = in.readString();
    }

    public static final Creator<SameName> CREATOR = new Creator<SameName>() {
        @Override
        public SameName createFromParcel(Parcel source) {
            return new SameName(source);
        }

        @Override
        public SameName[] newArray(int size) {
            return new SameName[size];
        }
    };
}

package com.mtjin.mapogreen.model.category_search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CategoryResult implements Parcelable {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.meta, flags);
        dest.writeList(this.documents);
    }

    public CategoryResult() {
    }

    protected CategoryResult(Parcel in) {
        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.documents = new ArrayList<Document>();
        in.readList(this.documents, Document.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryResult> CREATOR = new Parcelable.Creator<CategoryResult>() {
        @Override
        public CategoryResult createFromParcel(Parcel source) {
            return new CategoryResult(source);
        }

        @Override
        public CategoryResult[] newArray(int size) {
            return new CategoryResult[size];
        }
    };
}

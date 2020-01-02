package com.mtjin.mapogreen.model.category_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mtjin.mapogreen.model.Document;
import com.mtjin.mapogreen.model.Meta;

import java.util.List;

public class CategoryResult {

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

}

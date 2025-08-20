package com.akash.booklibrary.adminview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelAdminIssuedBooksM {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("issued")
    @Expose
    private List<ModelAdminIssuedBooksDetails> issued;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ModelAdminIssuedBooksDetails> getissued() {
        return issued;
    }

    public void setissued(List<ModelAdminIssuedBooksDetails> issued) {
        this.issued = issued;
    }
}

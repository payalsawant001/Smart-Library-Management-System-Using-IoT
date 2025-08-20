package com.akash.booklibrary.adminview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelAdminIssueRequestM {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("requests")
    @Expose
    private List<ModelAdminIssueRequestDetails> requests;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ModelAdminIssueRequestDetails> getRequests() {
        return requests;
    }

    public void setRequests(List<ModelAdminIssueRequestDetails> requests) {
        this.requests = requests;
    }
}

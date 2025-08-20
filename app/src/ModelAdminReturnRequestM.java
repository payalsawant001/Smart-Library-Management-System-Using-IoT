package com.akash.booklibrary.adminview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelAdminReturnRequestM {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("returns")
    @Expose
    private List<ModelAdminReturnRequestDetails> returns;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ModelAdminReturnRequestDetails> getReturns() {
        return returns;
    }

    public void setReturns(List<ModelAdminReturnRequestDetails> returns) {
        this.returns = returns;
    }
}

package com.akash.booklibrary.adminview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelAdminRecentHistoryM {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("history")
    @Expose
    private List<ModelAdminRecentHistoryDetails> history;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ModelAdminRecentHistoryDetails> getHistory() {
        return history;
    }

    public void setHistory(List<ModelAdminRecentHistoryDetails> history) {
        this.history = history;
    }
}

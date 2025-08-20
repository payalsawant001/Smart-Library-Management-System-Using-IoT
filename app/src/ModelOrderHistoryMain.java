package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelOrderHistoryMain {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orders")
    @Expose
    private List<ModelOrderHistoryOrder> orders;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ModelOrderHistoryOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ModelOrderHistoryOrder> orders) {
        this.orders = orders;
    }
}

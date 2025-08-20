package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelOrderHistoryOrder {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

}

package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelOrderDetailedVMain {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("total_qty")
    @Expose
    private Integer totalQty;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("products")
    @Expose
    private List<ModelOrderDetailedVOrders> products;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ModelOrderDetailedVOrders> getProducts() {
        return products;
    }

    public void setProducts(List<ModelOrderDetailedVOrders> products) {
        this.products = products;
    }
}

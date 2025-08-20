package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelCartListMain {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cart")
    @Expose
    private List<ModelCartListDetails> cart;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ModelCartListDetails> getCart() {
        return cart;
    }

    public void setCart(List<ModelCartListDetails> cart) {
        this.cart = cart;
    }
}

package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCartRemoveItem {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("new_quantity")
    @Expose
    private Integer new_quantity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getNew_quantity() {
        return new_quantity;
    }

    public void setNew_quantity(Integer new_quantity) {
        this.new_quantity = new_quantity;
    }



}

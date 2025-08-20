package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelLogin {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("info")
    @Expose
    private List<Object> info;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Object> getInfo() {
        return info;
    }

    public void setInfo(List<Object> info) {
        this.info = info;
    }
}

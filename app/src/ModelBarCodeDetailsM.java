package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBarCodeDetailsM {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("book")
    @Expose
    private ModelBarCodeDetailsMBook book;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ModelBarCodeDetailsMBook getBook() {
        return book;
    }

    public void setBook(ModelBarCodeDetailsMBook book) {
        this.book = book;
    }

}

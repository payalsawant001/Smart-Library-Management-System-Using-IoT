package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelBookIssuedListM {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("books")
    @Expose
    private List<ModelBookIssuedDetailsM> books;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ModelBookIssuedDetailsM> getBooks() {
        return books;
    }

    public void setBooks(List<ModelBookIssuedDetailsM> books) {
        this.books = books;
    }
}

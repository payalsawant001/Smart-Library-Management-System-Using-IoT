package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBarCodeDetailsMBook {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("book_name")
    @Expose
    private String bookName;
    @SerializedName("book_type")
    @Expose
    private String bookType;
    @SerializedName("Author")
    @Expose
    private String author;
    @SerializedName("side")
    @Expose
    private String side;
    @SerializedName("rack_no")
    @Expose
    private Integer rackNo;
    @SerializedName("platform_no")
    @Expose
    private Integer platformNo;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("book_image")
    @Expose
    private String bookImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getRackNo() {
        return rackNo;
    }

    public void setRackNo(Integer rackNo) {
        this.rackNo = rackNo;
    }

    public Integer getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(Integer platformNo) {
        this.platformNo = platformNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

}

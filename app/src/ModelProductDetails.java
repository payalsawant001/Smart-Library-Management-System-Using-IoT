package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelProductDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("productno")
    @Expose
    private String productno;
    @SerializedName("productname")
    @Expose
    private String productname;
    @SerializedName("productamount")
    @Expose
    private String productamount;
    @SerializedName("productimage")
    @Expose
    private String productimage;
    @SerializedName("productquantity")
    @Expose
    private String productquantity;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public ModelProductDetails(String id, String productno, String productname, String productamount, String productimage, String productquantity, String createdAt) {
        this.id = id;
        this.productno = productno;
        this.productname = productname;
        this.productamount = productamount;
        this.productimage = productimage;
        this.productquantity = productquantity;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductno() {
        return productno;
    }

    public void setProductno(String productno) {
        this.productno = productno;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductamount() {
        return productamount;
    }

    public void setProductamount(String productamount) {
        this.productamount = productamount;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(String productquantity) {
        this.productquantity = productquantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}

package com.akash.booklibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBookIssuedDetailsM {
    @SerializedName("book_name")
    @Expose
    private String bookName;
    @SerializedName("Author")
    @Expose
    private String author;
    @SerializedName("book_type")
    @Expose
    private String bookType;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("book_id")
    @Expose
    private Integer bookId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("issue_date")
    @Expose
    private String issueDate;
    @SerializedName("return_date")
    @Expose
    private String returnDate;
    @SerializedName("actual_return_date")
    @Expose
    private String actualReturnDate;
    @SerializedName("fine")
    @Expose
    private Integer fine;

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    @SerializedName("book_image")
    @Expose
    private String book_image;


    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(String actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Integer getFine() {
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
    }

}

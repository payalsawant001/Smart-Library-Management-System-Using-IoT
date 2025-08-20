package com.akash.booklibrary.adminview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAdminIssuedBooksDetails {
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
    private Object actualReturnDate;
    @SerializedName("fine")
    @Expose
    private Integer fine;
    @SerializedName("book_name")
    @Expose
    private String bookName;
    @SerializedName("book_type")
    @Expose
    private String bookType;
    @SerializedName("Author")
    @Expose
    private String author;
    @SerializedName("book_image")
    @Expose
    private String bookImage;
    @SerializedName("student_name")
    @Expose
    private String studentName;

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

    public Object getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Object actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Integer getFine() {
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
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

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}

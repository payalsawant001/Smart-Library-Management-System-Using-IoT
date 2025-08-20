package com.akash.booklibrary.api;

import com.akash.booklibrary.adminview.model.ModelAdminIssueRequestM;
import com.akash.booklibrary.adminview.model.ModelAdminIssuedBooksM;
import com.akash.booklibrary.adminview.model.ModelAdminRecentHistoryM;
import com.akash.booklibrary.adminview.model.ModelAdminReturnRequestM;
import com.akash.booklibrary.model.ModelBarCodeDetailsM;
import com.akash.booklibrary.model.ModelBookIssuedListM;
import com.akash.booklibrary.model.ModelCartListMain;
import com.akash.booklibrary.model.ModelCartRemoveItem;
import com.akash.booklibrary.model.ModelIssueRequestM;
import com.akash.booklibrary.model.ModelLoginNew;
import com.akash.booklibrary.model.ModelOrderDetailedVMain;
import com.akash.booklibrary.model.ModelOrderHistoryMain;
import com.akash.booklibrary.model.ModelRegisterMain;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelLoginNew> doLoginNew(@Field("username") String username,@Field("password") String password, @Field("action") String action);

    /*@GET("register.php?")
    Call<ModelRegister> doRegistration(@Query("name") String name, @Query("mobile") String mobile, @Query("password") String password);*/
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelRegisterMain> doRegistration(@Field("username") String username, @Field("password") String password, @Field("user_type") String uertype,
                                           @Field("name") String name,@Field("mobile_number") String mobile_number, @Field("action") String action);

    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelBookIssuedListM> getAllBooks(@Field("user_id") String user_id, @Field("action") String action);

    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelBookIssuedListM> getActiveBooks(@Field("user_id") String user_id, @Field("action") String action);

    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelBarCodeDetailsM> getBarCodeDetails(@Field("barcode") String master_card, @Field("action") String action);

    //for book issue request:....
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelIssueRequestM> bookIssueRequestN(@Field("action") String action,@Field("user_id") String user_id,@Field("book_id") String book_id);

    //for book return request:....
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelIssueRequestM> bookReturnRequestN(@Field("action") String action,@Field("borrow_id") String borrow_id);

    //get admin recent book history:...
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelAdminRecentHistoryM> getRecentHistory(@Field("action") String action);//@Field("user_id") String user_id,

    //get admin issued books:...
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelAdminIssuedBooksM> getAdminIssuedBooks(@Field("action") String action);//@Field("user_id") String user_id,

    //get admin issue Request:...
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelAdminIssueRequestM> getAdminIssueRequest(@Field("action") String action);//@Field("user_id") String user_id,

    //get admin approve issue Request:...
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelIssueRequestM> getAdminApproveIssueRequest(@Field("action") String action,@Field("borrow_id") String borrow_id);//@Field("user_id") String user_id,

    //get admin return Request:...
    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelAdminReturnRequestM> getAdminReturnRequest(@Field("action") String action);//@Field("user_id") String user_id,

    /*@Multipart
    @POST("Api.php?apicall=upload")
    Call<UploadResponse> addBookDetails(
            @Part MultipartBody.Part image,
            @Part("desc") RequestBody desc);*/

    @FormUrlEncoded
    @POST("/user_api.php")
    Call<ModelIssueRequestM> addBookDetails(@Field("action") String action);//@Field("user_id") String user_id,


    @Multipart
    @POST("/user_api.php")
    Call<ModelIssueRequestM> fileUpload(
            @Part("action") RequestBody action,
            @Part("book_name") RequestBody book_name,
            @Part("book_type") RequestBody book_type,
            @Part("Author") RequestBody Author,
            @Part("side") RequestBody side,
            @Part("rack_no") RequestBody rack_no,
            @Part("platform_no") RequestBody platform_no,
            @Part("quantity") RequestBody quantity,
            @Part("barcode") RequestBody barcode,
            @Part MultipartBody.Part file);
}

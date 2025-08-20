package com.akash.booklibrary.adminview;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.akash.booklibrary.R;
import com.akash.booklibrary.api.Api;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.databinding.ActivityAddBookBinding;
import com.akash.booklibrary.databinding.ActivityAdminDashboardBinding;
import com.akash.booklibrary.model.ModelIssueRequestM;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBookActivity extends AppCompatActivity {
    private ActivityAddBookBinding binding;

    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;
    public static SharedPreferences loginPreference;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_book);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        TextView title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.name);
        title.setText("ADD BOOK");

        loginPreference = getSharedPreferences("loginPrefsBookLibrary", MODE_PRIVATE);


        binding.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddBookActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        binding.lytMaterialRLSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    fileUpload();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            selectedImageUri = uri;
            //Uri uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            binding.imgPhoto.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validation(){
        if (binding.editBookName.getText().toString().equals("") && binding.editBookName.getText().toString().length()>4) {
            binding.editBookName.setError("Please enter proper book name");
            binding.editBookName.requestFocus();
            return false;
        }
        if (binding.editBookType.getText().toString().equals("") && binding.editBookType.getText().toString().length()>4) {
            binding.editBookType.setError("Please enter proper book type");
            binding.editBookType.requestFocus();
            return false;
        }

        if (binding.editBookAuthor.getText().toString().equals("") && binding.editBookAuthor.getText().toString().length()>4) {
            binding.editBookAuthor.setError("Please enter proper book author name");
            binding.editBookAuthor.requestFocus();
            return false;
        }
        if (binding.editSide.getText().toString().length()<=0) {
            binding.editSide.setError("Please enter valid Side");
            binding.editSide.requestFocus();
            return false;
        }
        if (binding.editRackNo.getText().toString().length() < 1) {
            binding.editRackNo.setError("Please enter valid rack no.");
            binding.editRackNo.requestFocus();
            return false;
        }
        if (binding.editPlatformNo.getText().toString().length() < 1) {
            binding.editPlatformNo.setError("Please enter valid platform no.");
            binding.editPlatformNo.requestFocus();
            return false;
        }

        if (binding.editQuantity.getText().toString().length() < 1) {
            binding.editQuantity.setError("Please enter valid platform no.");
            binding.editQuantity.requestFocus();
            return false;
        }

        return true;
    }

    public void fileUpload() {

        if (networkState.isInternetAvailable(AddBookActivity.this)) {
            pDialog = new ProgressDialog(AddBookActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            Api apiInterface = RetrofitClient.getInstance().getApi();
            File file = new File(selectedImageUri.getPath());
            //create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("book_image", file.getName(), requestFile);

            RequestBody action = RequestBody.create(okhttp3.MultipartBody.FORM, "add_book");
            RequestBody book_name = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editBookName.getText());
            RequestBody book_type = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editBookType.getText());
            RequestBody Author = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editBookAuthor.getText());
            RequestBody side = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editSide.getText());
            RequestBody rack_no = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editRackNo.getText());
            RequestBody platform_no = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editPlatformNo.getText());
            RequestBody quantity = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editQuantity.getText());
            RequestBody barcode = RequestBody.create(okhttp3.MultipartBody.FORM, ""+binding.editBarCodeNumber.getText());

            // finally, execute the request
            Call<ModelIssueRequestM> call = apiInterface.fileUpload(action,book_name,book_type,Author,side,rack_no,platform_no,quantity,barcode, body);
            call.enqueue(new Callback<ModelIssueRequestM>() {
                @Override
                public void onResponse(@NonNull Call<ModelIssueRequestM> call, @NonNull Response<ModelIssueRequestM> response) {
                    ModelIssueRequestM responseModel = response.body();
                    pDialog.dismiss();
                    if(responseModel != null){
                        if (responseModel.getStatus().equals("success")){
                            showCustomDialog(AddBookActivity.this,"Message","Book Details Added Successfully.");
                        }else{
                            Tools.showCustomDialog(AddBookActivity.this,"Message","Something went wrong, please try again later.");
                        }
                    } else{
                        Tools.showCustomDialog(AddBookActivity.this,"Message","Something went wrong, please try again later.");
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ModelIssueRequestM> call, @NonNull Throwable t) {
                    Log.e("response:...","Error End");
                    pDialog.dismiss();
                }
            });
        }else {
            Tools.showNoInternetConnDialog( AddBookActivity.this);
        }
    }

    public void showCustomDialog(final Context context, String title, String content){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_light);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.title)).setText(title);
        ((TextView) dialog.findViewById(R.id.content)).setText(content);

        AppCompatButton bt_close = dialog.findViewById(R.id.bt_Cancel);
        bt_close.setVisibility(View.GONE);
        ((AppCompatButton) dialog.findViewById(R.id.bt_Ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
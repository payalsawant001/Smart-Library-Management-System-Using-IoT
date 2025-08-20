package com.akash.booklibrary.adminview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akash.booklibrary.R;
import com.akash.booklibrary.adminview.adapter.AdapterIssuedBooksList;
import com.akash.booklibrary.adminview.adapter.AdapterRecentHistoryList;
import com.akash.booklibrary.adminview.model.ModelAdminIssuedBooksDetails;
import com.akash.booklibrary.adminview.model.ModelAdminIssuedBooksM;
import com.akash.booklibrary.adminview.model.ModelAdminRecentHistoryDetails;
import com.akash.booklibrary.adminview.model.ModelAdminRecentHistoryM;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.databinding.ActivityIssuedBooksBinding;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class IssuedBooksActivity extends AppCompatActivity {

    private ActivityIssuedBooksBinding binding;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;
    Context TAG = IssuedBooksActivity.this;
    public static SharedPreferences loginPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_issued_books);
        binding = ActivityIssuedBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        TextView title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.name);
        title.setText("ISSUED BOOKS");
        loginPreference = getSharedPreferences("loginPrefsBookLibrary", MODE_PRIVATE);


        getIssuedBookListAdmin();
    }

    private void getIssuedBookListAdmin(){
        if (networkState.isInternetAvailable(IssuedBooksActivity.this)){
            pDialog = new ProgressDialog(IssuedBooksActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            Call<ModelAdminIssuedBooksM> call = RetrofitClient
                    .getInstance().getApi().getAdminIssuedBooks("issued_books");//""+loginPreference.getString("user_id",""),

            call.enqueue(new Callback<ModelAdminIssuedBooksM>() {
                @Override
                public void onResponse(Call<ModelAdminIssuedBooksM> call, retrofit2.Response<ModelAdminIssuedBooksM> response) {
                    pDialog.dismiss();
                    if (response.body()!=null){
                        ModelAdminIssuedBooksM mModelAdminIssuedBooksM = response.body();
                        if(mModelAdminIssuedBooksM!=null){
                            if (mModelAdminIssuedBooksM.getStatus().equals("success")){
                                if (mModelAdminIssuedBooksM.getissued().size()>0){
                                    setRecentTransactionAdapter((ArrayList<ModelAdminIssuedBooksDetails>) mModelAdminIssuedBooksM.getissued());
                                    //filterIssuedBooks((ArrayList<ModelBookIssuedDetailsM>) modelAdminRecentHistoryM.getHistory());
                                }else {
                                    setNoRecords();
                                    //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                                }
                            }else {
                                setNoRecords();
                                //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                            }
                        }else {
                            setNoRecords();
                            // Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                        }
                    }else {
                        setNoRecords();
                        //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                    }
                }

                @Override
                public void onFailure(Call<ModelAdminIssuedBooksM> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("error",""+t.getMessage());
                    setNoRecords();
                    //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                }
            });
        }else {
            setNoRecords();
            Tools.showNoInternetConnDialog( IssuedBooksActivity.this);
        }
    }

    private void setNoRecords(){
        binding.recycleViewBI.setVisibility(View.GONE);
        binding.llNoRecordsBI.setVisibility(View.VISIBLE);
    }

    private void setRecentTransactionAdapter(ArrayList<ModelAdminIssuedBooksDetails> pojoPayAgainReceivers){
        binding.recycleViewBI.setVisibility(View.VISIBLE);
        binding.llNoRecordsBI.setVisibility(View.GONE);
        AdapterIssuedBooksList mAdapter = new AdapterIssuedBooksList(IssuedBooksActivity.this, pojoPayAgainReceivers);
        binding.recycleViewBI.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IssuedBooksActivity.this);
        binding.recycleViewBI.setLayoutManager(linearLayoutManager);
        binding.recycleViewBI.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
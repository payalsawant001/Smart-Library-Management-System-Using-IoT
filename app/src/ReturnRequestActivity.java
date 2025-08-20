package com.akash.booklibrary.adminview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akash.booklibrary.R;
import com.akash.booklibrary.adminview.adapter.AdapterIssuedRequestList;
import com.akash.booklibrary.adminview.adapter.AdapterReturnRequestList;
import com.akash.booklibrary.adminview.model.ModelAdminIssueRequestDetails;
import com.akash.booklibrary.adminview.model.ModelAdminIssueRequestM;
import com.akash.booklibrary.adminview.model.ModelAdminReturnRequestDetails;
import com.akash.booklibrary.adminview.model.ModelAdminReturnRequestM;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.databinding.ActivityIssueRequestBinding;
import com.akash.booklibrary.databinding.ActivityReturnRequestBinding;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ReturnRequestActivity extends AppCompatActivity {
    private ActivityReturnRequestBinding binding;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;
    public static SharedPreferences loginPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_request);
        binding = ActivityReturnRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        TextView title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.name);
        title.setText("RETURN REQUEST");

        loginPreference = getSharedPreferences("loginPrefsBookLibrary", MODE_PRIVATE);


        getReturnRequestListAdmin();
    }

    private void getReturnRequestListAdmin(){
        if (networkState.isInternetAvailable(ReturnRequestActivity.this)){
            pDialog = new ProgressDialog(ReturnRequestActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            Call<ModelAdminReturnRequestM> call = RetrofitClient
                    .getInstance().getApi().getAdminReturnRequest("return_requests");//""+loginPreference.getString("user_id",""),

            call.enqueue(new Callback<ModelAdminReturnRequestM>() {
                @Override
                public void onResponse(Call<ModelAdminReturnRequestM> call, retrofit2.Response<ModelAdminReturnRequestM> response) {
                    pDialog.dismiss();
                    if (response.body()!=null){
                        ModelAdminReturnRequestM mModelAdminIssuedBooksM = response.body();
                        if(mModelAdminIssuedBooksM!=null){
                            if (mModelAdminIssuedBooksM.getStatus().equals("success")){
                                if (mModelAdminIssuedBooksM.getReturns().size()>0){
                                    setRecentTransactionAdapter((ArrayList<ModelAdminReturnRequestDetails>) mModelAdminIssuedBooksM.getReturns());
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
                public void onFailure(Call<ModelAdminReturnRequestM> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("error",""+t.getMessage());
                    setNoRecords();
                    //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                }
            });
        }else {
            setNoRecords();
            Tools.showNoInternetConnDialog( ReturnRequestActivity.this);
        }
    }

    private void setNoRecords(){
        binding.recycleViewARR.setVisibility(View.GONE);
        binding.llNoRecordsARR.setVisibility(View.VISIBLE);
    }

    private void setRecentTransactionAdapter(ArrayList<ModelAdminReturnRequestDetails> pojoPayAgainReceivers){
        binding.recycleViewARR.setVisibility(View.VISIBLE);
        binding.llNoRecordsARR.setVisibility(View.GONE);
        AdapterReturnRequestList mAdapter = new AdapterReturnRequestList(ReturnRequestActivity.this, pojoPayAgainReceivers);
        binding.recycleViewARR.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReturnRequestActivity.this);
        binding.recycleViewARR.setLayoutManager(linearLayoutManager);
        binding.recycleViewARR.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
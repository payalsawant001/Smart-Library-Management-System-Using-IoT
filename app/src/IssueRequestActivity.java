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
import com.akash.booklibrary.adminview.adapter.AdapterIssuedBooksList;
import com.akash.booklibrary.adminview.adapter.AdapterIssuedRequestList;
import com.akash.booklibrary.adminview.model.ModelAdminIssueRequestDetails;
import com.akash.booklibrary.adminview.model.ModelAdminIssueRequestM;
import com.akash.booklibrary.adminview.model.ModelAdminIssuedBooksDetails;
import com.akash.booklibrary.adminview.model.ModelAdminIssuedBooksM;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.databinding.ActivityIssueRequestBinding;
import com.akash.booklibrary.databinding.ActivityIssuedBooksBinding;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class IssueRequestActivity extends AppCompatActivity {
    private ActivityIssueRequestBinding binding;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;
    Context TAG = IssueRequestActivity.this;
    public static SharedPreferences loginPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_issue_request);
        binding = ActivityIssueRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        TextView title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.name);
        title.setText("BOOK ISSUE REQUESTS");
        loginPreference = getSharedPreferences("loginPrefsBookLibrary", MODE_PRIVATE);


        getIssuedBookListAdmin();
    }

    private void getIssuedBookListAdmin(){
        if (networkState.isInternetAvailable(IssueRequestActivity.this)){
            pDialog = new ProgressDialog(IssueRequestActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            Call<ModelAdminIssueRequestM> call = RetrofitClient
                    .getInstance().getApi().getAdminIssueRequest("issue_requests");//""+loginPreference.getString("user_id",""),

            call.enqueue(new Callback<ModelAdminIssueRequestM>() {
                @Override
                public void onResponse(Call<ModelAdminIssueRequestM> call, retrofit2.Response<ModelAdminIssueRequestM> response) {
                    pDialog.dismiss();
                    if (response.body()!=null){
                        ModelAdminIssueRequestM mModelAdminIssuedBooksM = response.body();
                        if(mModelAdminIssuedBooksM!=null){
                            if (mModelAdminIssuedBooksM.getStatus().equals("success")){
                                if (mModelAdminIssuedBooksM.getRequests().size()>0){
                                    setRecentTransactionAdapter((ArrayList<ModelAdminIssueRequestDetails>) mModelAdminIssuedBooksM.getRequests());
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
                public void onFailure(Call<ModelAdminIssueRequestM> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("error",""+t.getMessage());
                    setNoRecords();
                    //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                }
            });
        }else {
            setNoRecords();
            Tools.showNoInternetConnDialog( IssueRequestActivity.this);
        }
    }

    private void setNoRecords(){
        binding.recycleViewBI.setVisibility(View.GONE);
        binding.llNoRecordsBI.setVisibility(View.VISIBLE);
    }

    private void setRecentTransactionAdapter(ArrayList<ModelAdminIssueRequestDetails> pojoPayAgainReceivers){
        binding.recycleViewBI.setVisibility(View.VISIBLE);
        binding.llNoRecordsBI.setVisibility(View.GONE);
        AdapterIssuedRequestList mAdapter = new AdapterIssuedRequestList(IssueRequestActivity.this, pojoPayAgainReceivers);
        binding.recycleViewBI.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IssueRequestActivity.this);
        binding.recycleViewBI.setLayoutManager(linearLayoutManager);
        binding.recycleViewBI.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
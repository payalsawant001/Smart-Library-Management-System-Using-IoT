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
import com.akash.booklibrary.adminview.adapter.AdapterRecentHistoryList;
import com.akash.booklibrary.adminview.model.ModelAdminRecentHistoryDetails;
import com.akash.booklibrary.adminview.model.ModelAdminRecentHistoryM;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.databinding.ActivityRecentHistoryBinding;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class RecentHistoryActivity extends AppCompatActivity {
    private ActivityRecentHistoryBinding binding;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;
    Context TAG = RecentHistoryActivity.this;
    public static SharedPreferences loginPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recent_history);
        binding = ActivityRecentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        TextView title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.name);
        title.setText("RECENT HISTORY");
        loginPreference = getSharedPreferences("loginPrefsBookLibrary", MODE_PRIVATE);


        getRecentBookList();
    }

    private void getRecentBookList(){
        if (networkState.isInternetAvailable(RecentHistoryActivity.this)){
            pDialog = new ProgressDialog(RecentHistoryActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            Call<ModelAdminRecentHistoryM> call = RetrofitClient
                    .getInstance().getApi().getRecentHistory("recent_history");//""+loginPreference.getString("user_id",""),

            call.enqueue(new Callback<ModelAdminRecentHistoryM>() {
                @Override
                public void onResponse(Call<ModelAdminRecentHistoryM> call, retrofit2.Response<ModelAdminRecentHistoryM> response) {
                    pDialog.dismiss();
                    if (response.body()!=null){
                        ModelAdminRecentHistoryM modelAdminRecentHistoryM = response.body();
                        if(modelAdminRecentHistoryM!=null){
                            if (modelAdminRecentHistoryM.getStatus().equals("success")){
                                if (modelAdminRecentHistoryM.getHistory().size()>0){
                                    setRecentTransactionAdapter((ArrayList<ModelAdminRecentHistoryDetails>) modelAdminRecentHistoryM.getHistory());
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
                public void onFailure(Call<ModelAdminRecentHistoryM> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("error",""+t.getMessage());
                    setNoRecords();
                    //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                }
            });
        }else {
            setNoRecords();
            Tools.showNoInternetConnDialog( RecentHistoryActivity.this);
        }
    }

    private void setNoRecords(){
        binding.recycleViewBI.setVisibility(View.GONE);
        binding.llNoRecordsBI.setVisibility(View.VISIBLE);
    }

    private void setRecentTransactionAdapter(ArrayList<ModelAdminRecentHistoryDetails> pojoPayAgainReceivers){
        binding.recycleViewBI.setVisibility(View.VISIBLE);
        binding.llNoRecordsBI.setVisibility(View.GONE);
        AdapterRecentHistoryList mAdapter = new AdapterRecentHistoryList(RecentHistoryActivity.this, pojoPayAgainReceivers);
        binding.recycleViewBI.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecentHistoryActivity.this);
        binding.recycleViewBI.setLayoutManager(linearLayoutManager);
        binding.recycleViewBI.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
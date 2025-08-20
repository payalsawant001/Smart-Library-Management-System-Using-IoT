package com.akash.booklibrary.bookStat;

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

import com.akash.booklibrary.MainActivity;
import com.akash.booklibrary.R;
import com.akash.booklibrary.adapter.AdapterAllBooksList;
import com.akash.booklibrary.adapter.AdapterMyBooksList;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.databinding.ActivityBookDetailsBinding;
import com.akash.booklibrary.databinding.ActivityMyBooksBinding;
import com.akash.booklibrary.model.ModelBookIssuedDetailsM;
import com.akash.booklibrary.model.ModelBookIssuedListM;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MyBooksActivity extends AppCompatActivity {
    private ActivityMyBooksBinding binding;
    public static SharedPreferences loginPreference;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_books);
        binding = ActivityMyBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        TextView title = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.name);
        title.setText("MY BOOKS");

        loginPreference = getSharedPreferences("loginPrefsBookLibrary", Context.MODE_PRIVATE);

        getBookIssuedList();
    }

    private void getBookIssuedList(){
        if (networkState.isInternetAvailable(MyBooksActivity.this)){
            pDialog = new ProgressDialog(MyBooksActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            // Log.e("loginid:...",""+loginPreference.getString("user_id",""));
            Call<ModelBookIssuedListM> call = RetrofitClient
                    .getInstance().getApi().getActiveBooks(""+loginPreference.getString("user_id",""),"get_active_books");

            call.enqueue(new Callback<ModelBookIssuedListM>() {
                @Override
                public void onResponse(Call<ModelBookIssuedListM> call, retrofit2.Response<ModelBookIssuedListM> response) {
                    pDialog.dismiss();
                    if (response.body()!=null){
                        ModelBookIssuedListM pojoOrderDetails = response.body();
                        if(pojoOrderDetails!=null){
                            if (pojoOrderDetails.getStatus().equals("success")){
                                if (pojoOrderDetails.getBooks().size()>0){
                                    setRecentTransactionAdapter((ArrayList<ModelBookIssuedDetailsM>) pojoOrderDetails.getBooks());
                                    //filterIssuedBooks((ArrayList<ModelBookIssuedDetailsM>) pojoOrderDetails.getBooks());
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
                public void onFailure(Call<ModelBookIssuedListM> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("error",""+t.getMessage());
                    setNoRecords();
                    //Tools.showCustomDialog(requireActivity(),"Message","No Book Issued.");
                }
            });
        }else {
            setNoRecords();
            Tools.showNoInternetConnDialog( MyBooksActivity.this);
        }
    }

    private void setNoRecords(){
        binding.recycleViewBI.setVisibility(View.GONE);
        binding.llNoRecordsBI.setVisibility(View.VISIBLE);
    }

    private void setRecentTransactionAdapter(ArrayList<ModelBookIssuedDetailsM> pojoPayAgainReceivers){
        binding.recycleViewBI.setVisibility(View.VISIBLE);
        binding.llNoRecordsBI.setVisibility(View.GONE);
        AdapterMyBooksList mAdapter = new AdapterMyBooksList(MyBooksActivity.this, pojoPayAgainReceivers);
        binding.recycleViewBI.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyBooksActivity.this);
        binding.recycleViewBI.setLayoutManager(linearLayoutManager);
        binding.recycleViewBI.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
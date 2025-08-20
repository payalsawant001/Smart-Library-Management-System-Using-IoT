package com.akash.booklibrary.adminview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.akash.booklibrary.LoginActivity;
import com.akash.booklibrary.MainActivity;
import com.akash.booklibrary.R;
import com.akash.booklibrary.databinding.ActivityAdminDashboardBinding;
import com.akash.booklibrary.databinding.ActivityMainNewBinding;
import com.akash.booklibrary.utils.NetworkState;

public class AdminDashboardActivity extends AppCompatActivity {
    private ActivityAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_admin_dashboard);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        binding.cardViewAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this,AddBookActivity.class));
            }
        });

        binding.cardViewRecentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this,RecentHistoryActivity.class));
            }
        });

        binding.cardViewIssuedBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this,IssuedBooksActivity.class));
            }
        });

        binding.cardViewIssueRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this,IssueRequestActivity.class));
            }
        });

        binding.cardViewReturnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this,ReturnRequestActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        SharedPreferences loginPreference = getSharedPreferences("loginPrefsBookLibrary", MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = loginPreference.edit();
        loginEditor.putBoolean("saveLogin", false);
        loginEditor.commit();
        loginEditor.clear();
        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure, you want to close?");
        builder.setPositiveButton("No", null);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AdminDashboardActivity.super.onBackPressed();
                finish();
            }
        });
        builder.show();
    }
}
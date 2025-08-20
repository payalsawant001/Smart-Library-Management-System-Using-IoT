package com.akash.booklibrary.barcodem;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.akash.booklibrary.R;

public class BarCodeResultActivity extends AppCompatActivity {

    String barCodeResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bar_code_result);
        barCodeResult = getIntent().getStringExtra("barCodeResult");
        if (barCodeResult!=null){
            Toast.makeText(this, ""+barCodeResult, Toast.LENGTH_SHORT).show();
        }

    }
}
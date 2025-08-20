package com.akash.booklibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.akash.booklibrary.R;

import java.util.regex.Pattern;

public class Tools {

    public static void showNoInternetConnDialog(final Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.title)).setText(activity.getResources().getString(R.string.hint_no_internet_title));
        ((TextView) dialog.findViewById(R.id.content)).setText(activity.getResources().getString(R.string.hint_no_internet_message));
        // ((CircularImageView) dialog.findViewById(R.id.image)).setImageResource(R.mipmap.ic_launcher_logo_round);

        AppCompatButton bt_close = dialog.findViewById(R.id.bt_Cancel);
        bt_close.setVisibility(View.GONE);
        ((AppCompatButton) dialog.findViewById(R.id.bt_Ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(context, "Follow Clicked", Toast.LENGTH_SHORT).show();
                val=true;*/
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //return val;
    }

    public static void showCustomDialog(final Context context, String title, String content){

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
        // ((CircularImageView) dialog.findViewById(R.id.image)).setImageResource(R.mipmap.ic_launcher_logo_round);

        AppCompatButton bt_close = dialog.findViewById(R.id.bt_Cancel);
        bt_close.setVisibility(View.GONE);
        ((AppCompatButton) dialog.findViewById(R.id.bt_Ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(context, "Follow Clicked", Toast.LENGTH_SHORT).show();
                val=true;*/
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //return val;
    }

    public static boolean validateNumberPlate(EditText numberPlate){
        final Pattern pattern1 = Pattern.compile("^[A-Z]{2}[\\\\ -]{0,1}[0-9]{2}[\\\\ -]{0,1}[A-HJ-NP-Z]{1,2}[\\\\ -]{0,1}[0-9]{4}$");   // for all states
        final Pattern pattern2 = Pattern.compile("^[0-9]{2}[\\\\ -]{0,1}BH[\\\\ -]{0,1}[0-9]{4}[\\\\ -]{0,1}[A-HJ-NP-Z]{1,2}$");   // for BH Series

        return (pattern1.matcher(numberPlate.getText().toString().trim()).matches() || pattern2.matcher(numberPlate.getText().toString().trim()).matches());
    }
}

package com.akash.booklibrary.adminview.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.akash.booklibrary.R;
import com.akash.booklibrary.adminview.model.ModelAdminIssueRequestDetails;
import com.akash.booklibrary.adminview.model.ModelAdminReturnRequestDetails;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.model.ModelIssueRequestM;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdapterReturnRequestList extends RecyclerView.Adapter<AdapterReturnRequestList.ViewHolder>{
    ArrayList<ModelAdminReturnRequestDetails> list;
    Activity activity;
    public static SharedPreferences loginPreference;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;


    public AdapterReturnRequestList(Activity activity, ArrayList<ModelAdminReturnRequestDetails> pojoLedgerReports) {
        this.activity = activity;
        this.list = pojoLedgerReports;

        loginPreference = activity.getSharedPreferences("loginPrefsBookLibrary",Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AdapterReturnRequestList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_return_request_admin, parent, false);
        AdapterReturnRequestList.ViewHolder viewHolder = new AdapterReturnRequestList.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReturnRequestList.ViewHolder holder, int position) {
        holder.itemView.setTag(list.get(position));
        holder.txtBookName.setText(""+list.get(position).getBookName());
        holder.txtBookType.setText(""+list.get(position).getBookType());
        holder.txtBookAuthor.setText(""+list.get(position).getAuthor());
        holder.txtBookStatus.setText(""+list.get(position).getStatus().toUpperCase());
        holder.txtBookStudent.setText(""+list.get(position).getStudentName());

        holder.txtBookSide.setText(""+list.get(position).getSide());
        holder.txtBookRackNo.setText(""+list.get(position).getRackNo());
        holder.txtBookPlatform.setText(""+list.get(position).getPlatformNo());

        Glide.with(activity).load("https://library.xtrovix.com/"+list.get(position).getBookImage()).fitCenter().into(holder.imgProviderLogo);

        if (list.get(position).getQuantity()>0){
            holder.llDate.setVisibility(View.VISIBLE);
            holder.txtBookAvailability.setText("Yes");
        }else {
            holder.llDate.setVisibility(View.GONE);
            holder.txtBookAvailability.setText("No");
        }

        if (list.get(position).getFine()!=null && list.get(position).getFine()!=0){
            holder.llFine.setVisibility(View.VISIBLE);
            holder.txtLateFine.setText(list.get(position).getFine());
        }else {
            holder.llFine.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(activity, BookDetailsActivity.class);
                //Intent intent = new Intent(context, TransactionReceiptCommonActivity.class);
                intent.putExtra("orderId",list.get(position).getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);*/
            }
        });

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookIssueRequest(""+list.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtBookStudent,txtBookName,txtBookType,txtIssueDate,txtBookAuthor,txtBookStatus,txtDateHeader,txtIssueDateN,txtLateFine,
                txtBookSide,txtBookRackNo,txtBookPlatform,txtBookAvailability;
        public LinearLayout llDate,llIssueDateN,llFine;
        public ImageView imgProviderLogo;
        public AppCompatButton btnApprove;
        public ViewHolder(View rowView) {
            super(rowView);

            txtBookStudent = (TextView) rowView.findViewById(R.id.txtBookStudent);
            txtBookName = (TextView) rowView.findViewById(R.id.txtBookName);
            txtBookType = (TextView) rowView.findViewById(R.id.txtBookType);
            txtIssueDate = (TextView) rowView.findViewById(R.id.txtIssueDate);
            txtIssueDateN = rowView.findViewById(R.id.txtIssueDateN);
            txtBookAuthor = (TextView) rowView.findViewById(R.id.txtBookAuthor);
            txtBookStatus = rowView.findViewById(R.id.txtBookStatus);
            llDate = rowView.findViewById(R.id.llDate);
            txtDateHeader = rowView.findViewById(R.id.txtDateHeader);
            btnApprove = rowView.findViewById(R.id.btnApprove);

            txtLateFine = rowView.findViewById(R.id.txtLateFine);

            llIssueDateN = rowView.findViewById(R.id.llIssueDateN);
            llFine = rowView.findViewById(R.id.llFine);

            imgProviderLogo = rowView.findViewById(R.id.imgProviderLogo);

            txtBookSide = (TextView) rowView.findViewById(R.id.txtBookSide);
            txtBookRackNo = (TextView) rowView.findViewById(R.id.txtBookRackNo);
            txtBookPlatform = (TextView) rowView.findViewById(R.id.txtBookPlatform);
            txtBookAvailability = (TextView) rowView.findViewById(R.id.txtBookAvailability);
        }
    }

    private void bookIssueRequest(String borrowid){
        if (networkState.isInternetAvailable(activity)){
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            Call<ModelIssueRequestM> call = RetrofitClient
                        .getInstance().getApi().getAdminApproveIssueRequest("final_return",""+borrowid);//

            call.enqueue(new Callback<ModelIssueRequestM>() {
                @Override
                public void onResponse(Call<ModelIssueRequestM> call, retrofit2.Response<ModelIssueRequestM> response) {
                    pDialog.dismiss();
                    if (response.body()!=null){
                        ModelIssueRequestM pojoOrderDetails = response.body();
                        if(pojoOrderDetails!=null){
                            showCustomDialog(activity,"Message","Book return request has been approved.");
                        }else {
                            Tools.showCustomDialog(activity,"Message",""+pojoOrderDetails.getMessage());
                        }
                    }else {
                        Tools.showCustomDialog(activity,"Message","Something went wrong, please try again.");
                    }
                }

                @Override
                public void onFailure(Call<ModelIssueRequestM> call, Throwable t) {
                    pDialog.dismiss();
                    Tools.showCustomDialog(activity,"Message","Something went wrong, please try again.");
                }
            });
        }else {
            Tools.showNoInternetConnDialog( activity);
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
        // ((CircularImageView) dialog.findViewById(R.id.image)).setImageResource(R.mipmap.ic_launcher_logo_round);

        AppCompatButton bt_close = dialog.findViewById(R.id.bt_Cancel);
        bt_close.setVisibility(View.GONE);
        ((AppCompatButton) dialog.findViewById(R.id.bt_Ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(context, "Follow Clicked", Toast.LENGTH_SHORT).show();
                val=true;*/
                dialog.dismiss();
                activity.finish();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //return val;
    }
}

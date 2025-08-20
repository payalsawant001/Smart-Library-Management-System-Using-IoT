package com.akash.booklibrary.adminview.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.akash.booklibrary.R;
import com.akash.booklibrary.adminview.model.ModelAdminIssuedBooksDetails;
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.model.ModelIssueRequestM;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdapterIssuedBooksList extends RecyclerView.Adapter<AdapterIssuedBooksList.ViewHolder>{
    ArrayList<ModelAdminIssuedBooksDetails> list;
    Activity activity;
    public static SharedPreferences loginPreference;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;


    public AdapterIssuedBooksList(Activity activity, ArrayList<ModelAdminIssuedBooksDetails> pojoLedgerReports) {
        this.activity = activity;
        this.list = pojoLedgerReports;

        loginPreference = activity.getSharedPreferences("loginPrefsBookLibrary",Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AdapterIssuedBooksList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_history_books, parent, false);
        AdapterIssuedBooksList.ViewHolder viewHolder = new AdapterIssuedBooksList.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIssuedBooksList.ViewHolder holder, int position) {
        holder.itemView.setTag(list.get(position));
        holder.txtBookName.setText(""+list.get(position).getBookName());
        holder.txtBookType.setText(""+list.get(position).getBookType());
        holder.txtBookAuthor.setText(""+list.get(position).getAuthor());
        holder.txtBookStatus.setText(""+list.get(position).getStatus().toUpperCase());
        holder.txtBookStudent.setText(""+list.get(position).getStudentName());

        Glide.with(activity).load("https://library.xtrovix.com/"+list.get(position).getBookImage()).fitCenter().into(holder.imgProviderLogo);

        if (list.get(position).getStatus().equals("requested")){
            holder.llDate.setVisibility(View.GONE);
            holder.btnReturnBook.setVisibility(View.GONE);
            holder.llIssueDateN.setVisibility(View.GONE);
        }else if (list.get(position).getStatus().equals("issued")){
            holder.txtDateHeader.setText("Issue Date: ");
            holder.llDate.setVisibility(View.VISIBLE);
            holder.txtIssueDate.setText(""+list.get(position).getIssueDate());
            holder.btnReturnBook.setVisibility(View.VISIBLE);
            holder.llIssueDateN.setVisibility(View.GONE);
        } else if (list.get(position).getStatus().equals("return_requested")){
            holder.txtDateHeader.setText("Issue Date: ");
            holder.llDate.setVisibility(View.VISIBLE);
            holder.txtIssueDate.setText(""+list.get(position).getIssueDate());
            holder.btnReturnBook.setVisibility(View.GONE);
            holder.llIssueDateN.setVisibility(View.GONE);
        }else if (list.get(position).getStatus().equals("returned")){
            holder.txtDateHeader.setText("Returned Date: ");
            holder.llDate.setVisibility(View.VISIBLE);
            holder.txtIssueDateN.setText(""+list.get(position).getIssueDate());
            holder.txtIssueDate.setText(""+list.get(position).getReturnDate());
            holder.btnReturnBook.setVisibility(View.GONE);
            holder.llIssueDateN.setVisibility(View.VISIBLE);
        }else {
            holder.btnReturnBook.setVisibility(View.GONE);
            holder.llIssueDateN.setVisibility(View.GONE);
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

        holder.btnReturnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookReturnRequest(""+list.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtBookStudent,txtBookName,txtBookType,txtIssueDate,txtBookAuthor,txtBookStatus,txtDateHeader,txtIssueDateN,txtLateFine;
        public LinearLayout llDate,llIssueDateN,llFine;
        public ImageView imgProviderLogo;
        public AppCompatButton btnReturnBook;
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
            btnReturnBook = rowView.findViewById(R.id.btnReturnBook);

            txtLateFine = rowView.findViewById(R.id.txtLateFine);

            llIssueDateN = rowView.findViewById(R.id.llIssueDateN);
            llFine = rowView.findViewById(R.id.llFine);

            imgProviderLogo = rowView.findViewById(R.id.imgProviderLogo);
        }
    }

    private void bookReturnRequest(String borrowid){
        if (networkState.isInternetAvailable(activity)){
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

            Call<ModelIssueRequestM> call = RetrofitClient
                    .getInstance().getApi().bookReturnRequestN("return_request",""+borrowid);//

            call.enqueue(new Callback<ModelIssueRequestM>() {
                @Override
                public void onResponse(Call<ModelIssueRequestM> call, retrofit2.Response<ModelIssueRequestM> response) {
                    pDialog.dismiss();
                    if (response.body()!=null){
                        ModelIssueRequestM pojoOrderDetails = response.body();
                        if(pojoOrderDetails!=null){
                            Tools.showCustomDialog(activity,"Message","Book return request has been submitted.");
                            activity.finish();
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
}

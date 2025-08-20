package com.akash.booklibrary.adapter;

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
import com.akash.booklibrary.api.RetrofitClient;
import com.akash.booklibrary.model.ModelBookIssuedDetailsM;
import com.akash.booklibrary.model.ModelIssueRequestM;
import com.akash.booklibrary.utils.NetworkState;
import com.akash.booklibrary.utils.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdapterAllBooksList extends RecyclerView.Adapter<AdapterAllBooksList.ViewHolder>{
    ArrayList<ModelBookIssuedDetailsM> list;
    Activity activity;
    public static SharedPreferences loginPreference;
    NetworkState networkState = new NetworkState();
    ProgressDialog pDialog;


    public AdapterAllBooksList(Activity activity, ArrayList<ModelBookIssuedDetailsM> pojoLedgerReports) {
        this.activity = activity;
        this.list = pojoLedgerReports;

        loginPreference = activity.getSharedPreferences("loginPrefsBookLibrary",Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AdapterAllBooksList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_lib_books, parent, false);
        AdapterAllBooksList.ViewHolder viewHolder = new AdapterAllBooksList.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllBooksList.ViewHolder holder, int position) {
        holder.itemView.setTag(list.get(position));
        holder.txtBookName.setText(""+list.get(position).getBookName());
        holder.txtBookType.setText(""+list.get(position).getBookType());
        holder.txtBookAuthor.setText(""+list.get(position).getAuthor());
        holder.txtBookStatus.setText(""+list.get(position).getStatus().toUpperCase());

        Glide.with(activity).load("https://library.xtrovix.com/"+list.get(position).getBook_image()).fitCenter().into(holder.imgProviderLogo);

        if (list.get(position).getStatus().equals("requested")){
            holder.llDate.setVisibility(View.GONE);
            holder.btnReturnBook.setVisibility(View.GONE);
        }else if (list.get(position).getStatus().equals("issued")){
            holder.txtDateHeader.setText("Issue Date: ");
            holder.llDate.setVisibility(View.VISIBLE);
            holder.txtIssueDate.setText(""+list.get(position).getIssueDate());
            holder.btnReturnBook.setVisibility(View.VISIBLE);
        } else if (list.get(position).getStatus().equals("return_requested")){
            holder.txtDateHeader.setText("Issue Date: ");
            holder.llDate.setVisibility(View.VISIBLE);
            holder.txtIssueDate.setText(""+list.get(position).getIssueDate());
            holder.btnReturnBook.setVisibility(View.GONE);
        }else if (list.get(position).getStatus().equals("returned")){
            holder.txtDateHeader.setText("Returned Date: ");
            holder.llDate.setVisibility(View.VISIBLE);
            holder.txtIssueDate.setText(""+list.get(position).getReturnDate());
            holder.btnReturnBook.setVisibility(View.GONE);
        }else {
            holder.btnReturnBook.setVisibility(View.GONE);
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

        public TextView txtBookName,txtBookType,txtIssueDate,txtBookAuthor,txtBookStatus,txtDateHeader;
        public LinearLayout llDate;
        public ImageView imgProviderLogo;
        public AppCompatButton btnReturnBook;
        public ViewHolder(View rowView) {
            super(rowView);

            txtBookName = (TextView) rowView.findViewById(R.id.txtBookName);
            txtBookType = (TextView) rowView.findViewById(R.id.txtBookType);
            txtIssueDate = (TextView) rowView.findViewById(R.id.txtIssueDate);
            txtBookAuthor = (TextView) rowView.findViewById(R.id.txtBookAuthor);
            txtBookStatus = rowView.findViewById(R.id.txtBookStatus);
            llDate = rowView.findViewById(R.id.llDate);
            txtDateHeader = rowView.findViewById(R.id.txtDateHeader);
            btnReturnBook = rowView.findViewById(R.id.btnReturnBook);

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

package com.example.mayintarlasi.Helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mayintarlasi.R;


public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {


    public String [] orderdetails2 ;
    public String [] orderdetails3 ;


    public adapter(String [] orderdetails2, String [] orderdetails3) {
        this.orderdetails2 = orderdetails2;
        this.orderdetails3 = orderdetails3;


    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView lblod2,lblod3;


        MyViewHolder(View view) {
            super(view);
            lblod2 = view.findViewById(R.id.txt_record);
            lblod3 = view.findViewById(R.id.txt_designer_name);


        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.lblod2.setText(orderdetails2[position]);
        holder.lblod3.setText(orderdetails3[position]);


    }

    @Override
    public int getItemCount() {
        return orderdetails2.length;
    }
}

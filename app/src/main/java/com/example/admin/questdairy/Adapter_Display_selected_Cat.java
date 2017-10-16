package com.example.admin.questdairy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 8/9/2017.
 */

public class Adapter_Display_selected_Cat extends RecyclerView.Adapter<Adapter_Display_selected_Cat.MyHolder>{


    ArrayList<String> fnm, lnm;
    Context context;
    private final OnItemClickListener onItemClickListener;

    public Adapter_Display_selected_Cat(ArrayList<String> fnm,ArrayList<String> lnm, Context context, OnItemClickListener onItemClickListener) {
        this.fnm = fnm;
        this.lnm = lnm;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disp_selected_cat_layout,null);
        final MyHolder holder = new MyHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(v,holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String a = fnm.get(position);
        String b = lnm.get(position);
        holder.firstname.setText(a+" "+b);
    }

    @Override
    public int getItemCount() {
        return fnm.size();
    }

    class MyHolder extends ViewHolder {
        TextView firstname;
        public MyHolder(View itemView) {
            super(itemView);
            firstname = (TextView)itemView.findViewById(R.id.firstname);
        }
    }
}

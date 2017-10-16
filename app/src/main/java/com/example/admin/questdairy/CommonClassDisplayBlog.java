package com.example.admin.questdairy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Admin on 8/8/2017.
 */
// ******************** adapter class for displaying blogs *********************

public class CommonClassDisplayBlog extends RecyclerView.Adapter<CommonClassDisplayBlog.MyHolder>{

    ArrayList<String> blog_title, blog_content;
    Context context;
    NewInterface newInterface;

    public CommonClassDisplayBlog(ArrayList<String> blog_title, ArrayList<String> blog_content, Context context, NewInterface newInterface) {
        this.blog_title = blog_title;
        this.blog_content = blog_content;
        this.context = context;
        this.newInterface = newInterface;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_blog,null);
        final MyHolder holder = new MyHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newInterface.onClick(v,holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.title.setText(blog_title.get(position));
        holder.content.setText(blog_content.get(position));
    }

    @Override
    public int getItemCount() {
        return blog_title.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        public MyHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            content = (TextView)itemView.findViewById(R.id.content);
        }
    }
}

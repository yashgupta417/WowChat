package com.WowChat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.R;
import com.WowChat.Retrofit.MemoryRead;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    public ArrayList<MemoryRead> feeds;
    Context context;
    private onItemClickListener mlistener;
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public FeedAdapter(Context context,ArrayList<MemoryRead> feeds) {
        this.context=context;
        this.feeds=feeds;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView groupDP;
        TextView groupNameTextView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.feed_image);
            groupDP=itemView.findViewById(R.id.feed_g_dp);
            groupNameTextView=itemView.findViewById(R.id.feed_g_name);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public FeedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_layout,parent,false);
        FeedAdapter.MyViewHolder myViewHolder=new FeedAdapter.MyViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.MyViewHolder holder, int i) {
        if(feeds.get(i).getImage()!=null){
            Glide.with(context).load(feeds.get(i).getImage()).placeholder(R.drawable.loadingc).into(holder.imageView);
        }
        if(feeds.get(i).getGroup().getGroupImage()!=null){
            Glide.with(context).load(feeds.get(i).getGroup().getGroupImage()).placeholder(R.drawable.loadingc).into(holder.groupDP);
            holder.groupNameTextView.setText(feeds.get(i).getGroup().getGroupName());
        }


    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }
}

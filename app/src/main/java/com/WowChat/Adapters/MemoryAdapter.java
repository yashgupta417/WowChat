package com.WowChat.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.R;
import com.WowChat.Retrofit.MemoryRead;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MyViewHolder>  {
    public ArrayList<MemoryRead> memories;

    Context context;
    private ImageAdapter.onItemClickListener mlistener;
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ImageAdapter.onItemClickListener listener){
        mlistener=listener;
    }
    public MemoryAdapter(Context context,ArrayList<MemoryRead> memories) {
        this.context=context;
        this.memories=memories;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView, final ImageAdapter.onItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.memory_image);
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
    public MemoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_item_layout,parent,false);
        MemoryAdapter.MyViewHolder myViewHolder=new MemoryAdapter.MyViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryAdapter.MyViewHolder holder, int i) {
        Glide.with(context).load(memories.get(i).getImage()).placeholder(R.drawable.loadingc).into(holder.imageView);
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
        return memories.size();
    }

}

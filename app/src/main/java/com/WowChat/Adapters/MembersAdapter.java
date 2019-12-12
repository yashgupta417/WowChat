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
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {
    public ArrayList<User> members;

    Context context;
    private onItemClickListener mlistener;
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public MembersAdapter(Context context,ArrayList<User> members) {
        this.context=context;
        this.members=members;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView username;

        public MyViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.wall_member_image);
            username=itemView.findViewById(R.id.wall_member_username);


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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item_layout,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.username.setText("@"+members.get(i).getUsername());
        if(members.get(i).getImage()!=null && members.get(i).getImage()!=""){
            Glide.with(context).load(members.get(i).getImage()).placeholder(R.drawable.loadingc).into(holder.imageView);
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
        return members.size();
    }

}

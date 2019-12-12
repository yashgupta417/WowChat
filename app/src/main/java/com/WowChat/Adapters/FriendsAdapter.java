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
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {
    public ArrayList<UserInfoTable> friends;
    public ArrayList<Boolean> selectedOrNot;
    Context context;
    private onItemClickListener mlistener;
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public FriendsAdapter(Context context,ArrayList<UserInfoTable> friends, ArrayList<Boolean> selectedOrNot) {
        this.context=context;
        this.friends = friends;
        this.selectedOrNot = selectedOrNot;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView nameTextView;
        TextView usernameTextView;
        ImageView tickImageview;
        ConstraintLayout parent;
        public MyViewHolder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.frndImageView);
            nameTextView=itemView.findViewById(R.id.frnd_name);
            usernameTextView=itemView.findViewById(R.id.frnd_username);
            tickImageview=itemView.findViewById(R.id.tick);
            parent=itemView.findViewById(R.id.friends_item_parent);

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
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item_layout,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if(friends.get(i).getPersonImage()!=null && !friends.get(i).getPersonImage().isEmpty()){
            Glide.with(context).load(friends.get(i).getPersonImage()).placeholder(R.drawable.loadingc).into(holder.imageView);
        }
        holder.nameTextView.setText(friends.get(i).getPersonFirstName()+" "+friends.get(i).getPersonLastName());
        holder.usernameTextView.setText("@"+friends.get(i).getPersonUsername());

        if(selectedOrNot.get(i)){
            holder.tickImageview.setVisibility(View.VISIBLE);
            holder.parent.setBackgroundColor(Color.parseColor("#1100BFFF"));

        }else{
            holder.tickImageview.setVisibility(View.INVISIBLE);
            holder.parent.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
        return friends.size();
    }

}

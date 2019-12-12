package com.WowChat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.R;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedFriendsAdapter extends ListAdapter<UserInfoTable, SelectedFriendsAdapter.MyViewHolder> {

    private onItemClickListener mlistener;
    private Context context;

    public SelectedFriendsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
        setHasStableIds(true);
    }
    private static final DiffUtil.ItemCallback<UserInfoTable> DIFF_CALLBACK=new DiffUtil.ItemCallback<UserInfoTable>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserInfoTable oldItem, @NonNull UserInfoTable newItem) {
            return oldItem.getPersonId().equals(newItem.getPersonId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserInfoTable oldItem, @NonNull UserInfoTable newItem) {

            return oldItem.getPersonImage().equals(newItem.getPersonImage());

        }
    };

    public interface onItemClickListener {
        void onItemClick(UserInfoTable userInfoTable);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView username;

        public MyViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.sf_image);
            username=itemView.findViewById(R.id.sf_username);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    listener.onItemClick(getItem(position));
                                }
                            }
                        }
                    });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_friends_item_layout, viewGroup, false);
        SelectedFriendsAdapter.MyViewHolder myViewHolder = new SelectedFriendsAdapter.MyViewHolder(v, mlistener);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            if(getItem(i).getPersonImage()!=null && !getItem(i).getPersonImage().isEmpty()){
                Glide.with(context).load(getItem(i).getPersonImage()).placeholder(R.drawable.loadingc).into(myViewHolder.imageView);
            }
            myViewHolder.username.setText("@"+getItem(i).getPersonUsername());

            Log.i("********",getItem(i).getPersonUsername());
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(getItem(position).getPersonId());
    }

    @Override
    public int getItemViewType(int position) {
        return  Integer.parseInt(getItem(position).getPersonId());
    }

    @Override
    protected UserInfoTable getItem(int position) {
        return super.getItem(position);
    }
}
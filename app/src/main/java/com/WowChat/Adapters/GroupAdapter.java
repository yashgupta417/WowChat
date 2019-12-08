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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.R;
import com.WowChat.Room.Entities.GroupTable;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends ListAdapter<GroupTable,GroupAdapter.MyViewHolder> {

    private GroupAdapter.onItemClickListener mlistener;
    private Context context;

    public GroupAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
        setHasStableIds(true);
    }
    private static final DiffUtil.ItemCallback<GroupTable> DIFF_CALLBACK=new DiffUtil.ItemCallback<GroupTable>() {
        @Override
        public boolean areItemsTheSame(@NonNull GroupTable oldItem, @NonNull GroupTable newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull GroupTable oldItem, @NonNull GroupTable newItem) {
            return false;
        }
    };

    public interface onItemClickListener {
        void onItemClick(UserInfoTable userInfoTable);
    }

    public void setOnItemClickListener(GroupAdapter.onItemClickListener listener) {
        mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView, final GroupAdapter.onItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.group_image);
            textView=itemView.findViewById(R.id.group_name);

        }
    }

    @NonNull
    @Override
    public GroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_layout_item, viewGroup, false);
        GroupAdapter.MyViewHolder myViewHolder = new GroupAdapter.MyViewHolder(v, mlistener);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(getItem(i).getName());
       // Glide.with(context).load(getItem(i).getImage()).placeholder(R.drawable.placeholder).into(myViewHolder.imageView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    protected GroupTable getItem(int position) {
        return super.getItem(position);
    }
}

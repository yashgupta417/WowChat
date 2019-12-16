package com.WowChat.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.User;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupQueryAdapter extends ListAdapter<GroupRead,GroupQueryAdapter.MyViewHolder> {

    private onItemClickListener mlistener;
    private Context context;

    public GroupQueryAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
        setHasStableIds(true);

    }
    private static final DiffUtil.ItemCallback<GroupRead> DIFF_CALLBACK=new DiffUtil.ItemCallback<GroupRead>() {
        @Override
        public boolean areItemsTheSame(@NonNull GroupRead oldItem, @NonNull GroupRead newItem) {
            return oldItem.getGroupId().equals(newItem.getGroupId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull GroupRead oldItem, @NonNull GroupRead newItem) {
            return oldItem.getGroupImage().equals(newItem.getGroupImage()) &&
                    oldItem.getMembers().size()==newItem.getMembers().size();
        }
    };

    public interface onItemClickListener {

        void onGroupClick(GroupRead groupRead);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView members;
        public MyViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.g_q_image);
            name=itemView.findViewById(R.id.g_q_name);
            members=itemView.findViewById(R.id.g_q_members_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onGroupClick(getItem(position));
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_query_item_layout, viewGroup, false);
        GroupQueryAdapter.MyViewHolder myViewHolder = new GroupQueryAdapter.MyViewHolder(v, mlistener);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        GroupRead group=getItem(i);
        Glide.with(context).load(group.getGroupImage()).placeholder(R.drawable.loadingc).into(myViewHolder.imageView);
        myViewHolder.name.setText(group.getGroupName());
        myViewHolder.members.setText(Integer.toString(group.getMembers().size())+" members");

    }
    //https://stackoverflow.com/questions/42646950/the-correct-way-of-implementing-getitemid-in-recyclerview-adapter
    @Override
    public long getItemId(int position) {
        return getItem(position).getGroupId();
    }


}
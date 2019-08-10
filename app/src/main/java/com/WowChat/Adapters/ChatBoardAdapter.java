package com.WowChat.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.Repository.MyRepository;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;
import com.WowChat.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatBoardAdapter extends ListAdapter<com.WowChat.Room.Entities.UserInfoTable, ChatBoardAdapter.MyViewHolder> {

    private onItemClickListener mlistener;
    private Context context;

    public ChatBoardAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
    }
    private static final DiffUtil.ItemCallback<UserInfoTable> DIFF_CALLBACK=new DiffUtil.ItemCallback<UserInfoTable>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserInfoTable oldItem, @NonNull UserInfoTable newItem) {
            return oldItem.getPersonId()==newItem.getPersonId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserInfoTable oldItem, @NonNull UserInfoTable newItem) {

            return oldItem.getPersonImage().equals(newItem.getPersonImage())&&
                    oldItem.getLatestMesage().equals(newItem.getLatestMesage())&&
                    oldItem.getPersonFirstName().equals(newItem.getPersonFirstName()) &&
                    oldItem.getUnseenMessageCount().equals(newItem.getUnseenMessageCount());

        }
    };

    public interface onItemClickListener {
        void onItemClick(UserInfoTable userInfoTable);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView latestMessageTextView;
        CircleImageView circleImageView;
        TextView timeTextView;
        TextView unseenCountTextview;

        public MyViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.chat_name);
            latestMessageTextView = itemView.findViewById(R.id.chat_latestMessage);
            circleImageView = itemView.findViewById(R.id.chat_image);
            timeTextView=itemView.findViewById(R.id.chat_time);
            unseenCountTextview=itemView.findViewById(R.id.chat_unseen_msg_count);

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

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_layout, viewGroup, false);
        ChatBoardAdapter.MyViewHolder myViewHolder = new ChatBoardAdapter.MyViewHolder(v, mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.nameTextView.setText(getItem(i).getPersonFirstName());
        myViewHolder.latestMessageTextView.setText(getItem(i).getLatestMesage());
        myViewHolder.timeTextView.setText(getItem(i).getLatestMessageTime());
        Integer unseenCount=getItem(i).getUnseenMessageCount();
        if(unseenCount==null || unseenCount==0){
            myViewHolder.unseenCountTextview.setVisibility(View.INVISIBLE);
            myViewHolder.unseenCountTextview.setText("0");
        }else{

            myViewHolder.unseenCountTextview.setVisibility(View.VISIBLE);
            myViewHolder.unseenCountTextview.setText(Integer.toString(unseenCount)+" New Messages");
        }

        if(!getItem(i).getPersonImage().equals("")){
            Glide.with(context).load(getItem(i).getPersonImage()).placeholder(R.drawable.user_img).into(myViewHolder.circleImageView);

        }else{
            Glide.with(context).load(R.drawable.user_img).into(myViewHolder.circleImageView).onLoadFailed(context.getDrawable(R.drawable.user_img));
        }

    }

    public UserInfoTable getUserInfoAt(int position){
        return getItem(position);
    }

}
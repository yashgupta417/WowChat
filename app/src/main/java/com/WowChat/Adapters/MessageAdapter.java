package com.WowChat.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.R;
import com.WowChat.Room.Entities.MessageTable;

public class MessageAdapter extends ListAdapter<MessageTable, com.WowChat.Adapters.MessageAdapter.MessageViewHolder> {
    private com.WowChat.Adapters.MessageAdapter.onItemClickListener mlistener;
    private Context context;
    String me_id;
    public MessageAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
        setHasStableIds(true);
        SharedPreferences sharedPreferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        me_id=sharedPreferences.getString("id","");

    }
    private static final DiffUtil.ItemCallback<MessageTable> DIFF_CALLBACK=new DiffUtil.ItemCallback<MessageTable>() {
        @Override
        public boolean areItemsTheSame(@NonNull MessageTable oldItem, @NonNull MessageTable newItem) {
            return oldItem.getMessageid()== newItem.getMessageid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MessageTable oldItem, @NonNull MessageTable newItem) {
            return oldItem.getText().equals(newItem.getText()) &&
                    oldItem.getSender().equals(newItem.getSender()) &&
                    oldItem.getRecipient().equals(newItem.getRecipient()) &&
                    oldItem.getStatus().equals(newItem.getStatus());
        }
    };

    public interface onItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(com.WowChat.Adapters.MessageAdapter.onItemClickListener listener) {
        mlistener = listener;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextViewMe;
        TextView messageTextViewFriend;
        TextView timeMe;
        TextView timeFriend;
        TextView statusFriend;
        TextView statusMe;
        LinearLayout linearLayoutMe;
        LinearLayout linearLayoutFriend;

        public MessageViewHolder(@NonNull View itemView, final com.WowChat.Adapters.MessageAdapter.onItemClickListener listener) {
            super(itemView);
            messageTextViewMe = itemView.findViewById(R.id.message_textView_me);
            messageTextViewFriend=itemView.findViewById(R.id.message_textView_friend);
            linearLayoutMe=itemView.findViewById(R.id.message_ll_me);
            linearLayoutFriend=itemView.findViewById(R.id.message_ll_friend);
            timeMe=itemView.findViewById(R.id.message_time_me);
            timeFriend=itemView.findViewById(R.id.message_time_friend);
            statusMe=itemView.findViewById(R.id.message_status_me);
            statusFriend=itemView.findViewById(R.id.message_status_friend);
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    listener.onItemClick(position);
                                }
                            }
                        }
                    });
        }
    }

    @NonNull
    @Override
    public com.WowChat.Adapters.MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
           v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item_layout, viewGroup, false);


         com.WowChat.Adapters.MessageAdapter.MessageViewHolder myViewHolder = new com.WowChat.Adapters.MessageAdapter.MessageViewHolder(v, mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.WowChat.Adapters.MessageAdapter.MessageViewHolder myViewHolder, int i) {
        Log.i("****msg",getItem(i).getText());
        if(getItem(i).getSender().equals(me_id)){
            myViewHolder.linearLayoutMe.animate().alpha(1);
            myViewHolder.messageTextViewMe.setText(getItem(i).getText());
            myViewHolder.timeMe.setText(getItem(i).getTimeofmessaging());
            myViewHolder.statusMe.setText(" "+getItem(i).getStatus());
        }
        else{
            myViewHolder.linearLayoutFriend.animate().alpha(1);
            myViewHolder.messageTextViewFriend.setText(getItem(i).getText());
            myViewHolder.timeFriend.setText(getItem(i).getTimeofmessaging());
//            myViewHolder.statusFriend.setText(getItem(i).getStatus());
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public MessageTable getMessageTableAt(int position){
        return getItem(position);
    }


}

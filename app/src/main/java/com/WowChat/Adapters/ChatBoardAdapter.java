package com.WowChat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.Repository.MyRepository;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;
import com.WowChat.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatBoardAdapter extends ListAdapter<UserInfoTable, ChatBoardAdapter.MyViewHolder> {

    private onItemClickListener mlistener;
    private Context context;

    public ChatBoardAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
        setHasStableIds(true);
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
        void onItemLongClick(UserInfoTable userInfoTable);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView lastNameTextView;
        TextView latestMessageTextView;
        CircleImageView circleImageView;
        TextView timeTextView;
        ImageView isGroup;
        TextView unseenCountTextview;
        ConstraintLayout parent;

        public MyViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            parent=itemView.findViewById(R.id.chatitem_parent);
            nameTextView = itemView.findViewById(R.id.chat_name);
            latestMessageTextView = itemView.findViewById(R.id.chat_latestMessage);
            circleImageView = itemView.findViewById(R.id.chat_image);
            timeTextView=itemView.findViewById(R.id.chat_time);
            unseenCountTextview=itemView.findViewById(R.id.chat_unseen_msg_count);
            lastNameTextView=itemView.findViewById(R.id.last_name);
            isGroup=itemView.findViewById(R.id.isgroup);

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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(getItem(position));
                        }
                    }
                    return true;
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
        myViewHolder.lastNameTextView.setText(getItem(i).getPersonLastName());
        if(getItem(i).getIsGroup()==1){
            myViewHolder.isGroup.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.isGroup.setVisibility(View.GONE);
        }
        if(getItem(i).getLatestMesage()==null || getItem(i).getLatestMesage().equals("")) {
            myViewHolder.latestMessageTextView.setText("Image");

        }else{
            myViewHolder.latestMessageTextView.setText(getItem(i).getLatestMesage());
        }

        SimpleDateFormat timePreciseformat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat AMOrPMFormat=new SimpleDateFormat("a");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        String currDate=dateformat.format(c.getTime());

        if(currDate.equals(getItem(i).getLatestMessagedate())){

            String t=getItem(i).getLatestMessageTime();
            String amorpm=getItem(i).getLatestMessageAMorPM();
            Log.i("%%%",t.substring(0,5)+ amorpm);
            myViewHolder.timeTextView.setText(t.substring(0,5)+ " "+amorpm.toLowerCase());
        }else{
            Log.i("%%%",getItem(i).getLatestMessagedate());
            myViewHolder.timeTextView.setText(getItem(i).getLatestMessagedate());
        }



        Integer unseenCount=getItem(i).getUnseenMessageCount();
        if(unseenCount==null || unseenCount==0){
            //myViewHolder.parent.setBackgroundColor(Color.parseColor("#FFFFFF"));
            myViewHolder.unseenCountTextview.setVisibility(View.INVISIBLE);
            myViewHolder.unseenCountTextview.setText("0");
            myViewHolder.timeTextView.setTextColor(Color.GRAY);
            myViewHolder.latestMessageTextView.setTypeface(null, Typeface.NORMAL);

        }else{
            //myViewHolder.parent.setBackgroundColor(Color.parseColor("#1100BFFF"));
            myViewHolder.timeTextView.setTextColor(Color.parseColor("#00BFFF"));
            myViewHolder.unseenCountTextview.setVisibility(View.VISIBLE);
            myViewHolder.unseenCountTextview.setText(Integer.toString(unseenCount));
            myViewHolder.latestMessageTextView.setTypeface(null, Typeface.BOLD);
        }

        if(!getItem(i).getPersonImage().equals("")){
            Glide.with(context).load(getItem(i).getPersonImage()).placeholder(R.drawable.loadingc).into(myViewHolder.circleImageView);

        }else{
            Glide.with(context).load(R.drawable.user_img).into(myViewHolder.circleImageView).onLoadFailed(context.getDrawable(R.drawable.user_img));
        }

    }


    public UserInfoTable getUserInfoAt(int position){
        return getItem(position);
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
    protected UserInfoTable getItem(int position) {
        return super.getItem(position);
    }
}
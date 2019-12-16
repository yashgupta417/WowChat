package com.WowChat.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.R;
import com.WowChat.Retrofit.GroupMessage;
import com.WowChat.Room.Entities.GroupMessageTable;
import com.WowChat.Room.Entities.GroupTable;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageAdapter extends ListAdapter<GroupMessageTable,GroupMessageAdapter.MyViewHolder> {
    private GroupMessageAdapter.onItemClickListener mlistener;
    private Context context;
    private String me_id;


    public GroupMessageAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
        setHasStableIds(true);
        SharedPreferences sharedPreferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        me_id=sharedPreferences.getString("id","");
    }
    private static final DiffUtil.ItemCallback<GroupMessageTable> DIFF_CALLBACK=new DiffUtil.ItemCallback<GroupMessageTable>() {
        @Override
        public boolean areItemsTheSame(@NonNull GroupMessageTable oldItem, @NonNull GroupMessageTable newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull GroupMessageTable oldItem, @NonNull GroupMessageTable newItem) {
            return false;
        }
    };

    public interface onItemClickListener {
        void onItemClick(GroupMessageTable groupMessageTable);
    }

    public void setOnItemClickListener(GroupMessageAdapter.onItemClickListener listener) {
        mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextViewMe;
        TextView messageTextViewFriend;
        TextView timeMe;
        TextView timeFriend;
        TextView statusFriend;
        TextView statusMe;
        LinearLayout linearLayoutMe;
        LinearLayout linearLayoutFriend;
        ImageView imageMe;
        ImageView imageFriend;
        TextView date;
        TextView event;
        TextView friendName;
        CircleImageView friendDp;
        public MyViewHolder(@NonNull View itemView, final GroupMessageAdapter.onItemClickListener listener) {
            super(itemView);
            messageTextViewMe = itemView.findViewById(R.id.group_message_textView_me);
            messageTextViewFriend=itemView.findViewById(R.id.group_message_textView_friend);
            linearLayoutMe=itemView.findViewById(R.id.group_message_ll_me);
            linearLayoutFriend=itemView.findViewById(R.id.group_message_ll_friend);
            timeMe=itemView.findViewById(R.id.group_message_time_me);
            timeFriend=itemView.findViewById(R.id.group_message_time_friend);
            statusMe=itemView.findViewById(R.id.group_message_status_me);
            statusFriend=itemView.findViewById(R.id.group_message_status_friend);
            imageMe=itemView.findViewById(R.id.group_message_image_me);
            imageFriend=itemView.findViewById(R.id.group_message_image_friend);
            date=itemView.findViewById(R.id.group_message_date);
            event=itemView.findViewById(R.id.group_event);
            friendName=itemView.findViewById(R.id.friend_name);
            friendDp=itemView.findViewById(R.id.friend_dp);

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
    public GroupMessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_message_item_layout, viewGroup, false);
        GroupMessageAdapter.MyViewHolder myViewHolder = new GroupMessageAdapter.MyViewHolder(v, mlistener);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull GroupMessageAdapter.MyViewHolder myViewHolder, int i) {


        if(i!=0 && getItem(i).getDateofmessaging().equals(getItem(i-1).getDateofmessaging())){
            myViewHolder.date.setVisibility(View.GONE);
        }else {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String currDate=dateformat.format(c.getTime());

            String msgDate=getItem(i).getDateofmessaging();
            if(currDate.equals(msgDate)){
                myViewHolder.date.setText("Today");
            }else {
                myViewHolder.date.setText(msgDate);
            }
        }
        if(getItem(i).getEvent()!=null && !getItem(i).getEvent().isEmpty()){
            myViewHolder.event.setText(getItem(i).getEvent());
            myViewHolder.linearLayoutFriend.setVisibility(View.GONE);
            myViewHolder.linearLayoutMe.setVisibility(View.GONE);
            myViewHolder.friendDp.setVisibility(View.GONE);
            return;
        }else {
            myViewHolder.event.setVisibility(View.GONE);
        }
        if(getItem(i).getSenderId().equals(me_id)){



            myViewHolder.linearLayoutFriend.setVisibility(View.GONE);
            myViewHolder.friendDp.setVisibility(View.GONE);
            if(getItem(i).getImage()==null){
                myViewHolder.imageMe.setVisibility(View.GONE);
            }else{
                Glide.with(context).load(getItem(i).getImage()).placeholder(R.drawable.loadingc).into(myViewHolder.imageMe);
            }
            if(getItem(i).getText()==null || getItem(i).getText().equals("")){
                myViewHolder.messageTextViewMe.setVisibility(View.GONE);

            }else{
                myViewHolder.messageTextViewMe.setText(getItem(i).getText());
            }
            myViewHolder.linearLayoutMe.setVisibility(View.VISIBLE);
            myViewHolder.timeMe.setText(getItem(i).getTimeofmessaging().substring(0,5)+" "+getItem(i).getAMorPM().toLowerCase());

            myViewHolder.statusMe.setText(" "+getItem(i).getStatus());

        }
        else{
            if(i==0 || !getItem(i).getSenderId().equals(getItem(i-1).getSenderId())){
                myViewHolder.friendDp.setVisibility(View.VISIBLE);
                myViewHolder.friendName.setVisibility(View.VISIBLE);
                myViewHolder.friendName.setText(getItem(i).getSenderName());
                Glide.with(context).load(getItem(i).getSenderImage()).placeholder(R.drawable.user_img).into(myViewHolder.friendDp);
            }else {
                myViewHolder.friendName.setVisibility(View.GONE);
                myViewHolder.friendDp.setVisibility(View.INVISIBLE);
            }
            myViewHolder.linearLayoutMe.setVisibility(View.GONE);
            if(getItem(i).getImage()==null){
                myViewHolder.imageFriend.setVisibility(View.GONE);
            }else{
                Glide.with(context).load(getItem(i).getImage()).placeholder(R.drawable.loadingc).into(myViewHolder.imageFriend);
            }
            if(getItem(i).getText()==null || getItem(i).getText().equals("")){
                myViewHolder.messageTextViewFriend.setVisibility(View.GONE);

            }else{
                myViewHolder.messageTextViewFriend.setText(getItem(i).getText());
            }
            myViewHolder.linearLayoutFriend.setVisibility(View.VISIBLE);
            myViewHolder.timeFriend.setText(getItem(i).getTimeofmessaging().substring(0,5)+" "+getItem(i).getAMorPM().toLowerCase());
        }
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
    public GroupMessageTable getItem(int position) {
        return super.getItem(position);
    }
}

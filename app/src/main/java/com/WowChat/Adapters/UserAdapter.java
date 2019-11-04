package com.WowChat.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.WowChat.Retrofit.User;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends ListAdapter<User, UserAdapter.MyViewHolder> {

    private onItemClickListener mlistener;
    private Context context;
    private String myUsername;

    public UserAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
        setHasStableIds(true);
        SharedPreferences sharedPreferences=context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        myUsername=sharedPreferences.getString("username","");
    }
    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK=new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUsername().equals(newItem.getUsername());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return
                    oldItem.getFirstName().equals(newItem.getFirstName()) &&
                    oldItem.getUsername().equals(newItem.getUsername()) &&
                    oldItem.getLastName().equals(newItem.getLastName());
        }
    };

    public interface onItemClickListener {
        void onItemClick(User user);
        void onMessageClick(User user);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        CircleImageView imageView;
        TextView usernameTextView;
        ImageView message;
        public MyViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.search_name);
            imageView=itemView.findViewById(R.id.search_image);
            usernameTextView=itemView.findViewById(R.id.search_username);
            message=itemView.findViewById(R.id.s_message);
            itemView.setOnClickListener(new View.OnClickListener() {
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
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( listener!= null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMessageClick(getItem(position));
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_query_item, viewGroup, false);
        UserAdapter.MyViewHolder myViewHolder = new UserAdapter.MyViewHolder(v, mlistener);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        User user=getUserAt(i);
        myViewHolder.nameTextView.setText(user.getFirstName()+" "+user.getLastName());
        myViewHolder.usernameTextView.setText("@"+user.getUsername());
        myViewHolder.message.setVisibility(View.VISIBLE);
        myViewHolder.message.setEnabled(true);
        if(user.getUsername().equals(myUsername)){
            myViewHolder.message.setVisibility(View.INVISIBLE);
            myViewHolder.message.setEnabled(false);
        }
        if(user.getImage()!=null){
            Glide.with(context).load(user.getImage()).into(myViewHolder.imageView);
        }
        else {
            Glide.with(context).load(R.drawable.user_img).into(myViewHolder.imageView);
        }

    }

    public User getUserAt(int position){
        return getItem(position);
    }

}
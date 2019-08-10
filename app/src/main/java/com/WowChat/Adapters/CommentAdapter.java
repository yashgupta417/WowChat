package com.WowChat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.WowChat.R;
import com.WowChat.Retrofit.CommentsOnThisShot;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends ListAdapter<CommentsOnThisShot, com.WowChat.Adapters.CommentAdapter.CommentViewHolder> {

    private onItemClickListener mlistener;
    private Context context;
    public CommentAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
    }
    private static final DiffUtil.ItemCallback<CommentsOnThisShot> DIFF_CALLBACK=new DiffUtil.ItemCallback<CommentsOnThisShot>() {
        @Override
        public boolean areItemsTheSame(@NonNull CommentsOnThisShot oldItem, @NonNull CommentsOnThisShot newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommentsOnThisShot oldItem, @NonNull CommentsOnThisShot newItem) {
            return false;
        }
    };

    public interface onItemClickListener {
        void onItemClick(CommentsOnThisShot commentsOnThisShot);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener = listener;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView text;
        CircleImageView commentByImage;


        public CommentViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            username=itemView.findViewById(R.id.coment_username);
            text=itemView.findViewById(R.id.comment_text);
            commentByImage=itemView.findViewById(R.id.comment_image);


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
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_on_this_shot_item_layout, viewGroup, false);
        com.WowChat.Adapters.CommentAdapter.CommentViewHolder myViewHolder = new com.WowChat.Adapters.CommentAdapter.CommentViewHolder(v, mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder myViewHolder, int i) {
        myViewHolder.username.setText("@"+getItem(i).getCommentedBy().getUsername());
        myViewHolder.text.setText(getItem(i).getText());
        Glide.with(context).load(getItem(i).getCommentedBy().getImage()).into(myViewHolder.commentByImage);

    }

    public CommentsOnThisShot getCommentAt(int position){
        return getItem(position);
    }

}
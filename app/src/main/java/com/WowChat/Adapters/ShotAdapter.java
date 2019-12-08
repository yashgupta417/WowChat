package com.WowChat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.Retrofit.User;
import com.bumptech.glide.Glide;
import com.WowChat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShotAdapter extends ListAdapter<com.WowChat.Retrofit.Shot, ShotAdapter.ShotViewHolder> {

    private onItemClickListener mlistener;
    private Context context;
    public ShotAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
    }
    private static final DiffUtil.ItemCallback<com.WowChat.Retrofit.Shot> DIFF_CALLBACK=new DiffUtil.ItemCallback<com.WowChat.Retrofit.Shot>() {
        @Override
        public boolean areItemsTheSame(@NonNull com.WowChat.Retrofit.Shot oldItem, @NonNull com.WowChat.Retrofit.Shot newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull com.WowChat.Retrofit.Shot oldItem, @NonNull com.WowChat.Retrofit.Shot newItem) {
            return false;
        }
    };

    public interface onItemClickListener {
        void onItemClick(com.WowChat.Retrofit.Shot shot);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener = listener;
    }

    public class ShotViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        TextView title;
        ImageView image;
        CircleImageView shotByImage;
        TextView shotBy;

//        BottomSheetBehavior bottomSheetBehavior;
//        RecyclerView recyclerView;


        public ShotViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            text=itemView.findViewById(R.id.shot_text);
            title=itemView.findViewById(R.id.shot_title);
            image=itemView.findViewById(R.id.shot_image);
            shotBy=itemView.findViewById(R.id.shot_by);
            shotByImage=itemView.findViewById(R.id.shot_by_image);

//            recyclerView=itemView.findViewById(R.id.recyclerView);
//            bottomSheetBehavior=BottomSheetBehavior.from(recyclerView);
//            bottomSheetBehavior.setPeekHeight(50);
//            bottomSheetBehavior.setHideable(true);


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
    public ShotViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shot_item_layout, viewGroup, false);
        ShotAdapter.ShotViewHolder myViewHolder = new ShotAdapter.ShotViewHolder(v, mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShotViewHolder myViewHolder, int i) {
        myViewHolder.text.setText(getItem(i).getText());
        myViewHolder.title.setText(getItem(i).getTitle());
        if(getItem(i).getImage()==null){
            myViewHolder.image.setVisibility(View.GONE);
        }else{
            Glide.with(context).load(getItem(i).getImage()).placeholder(R.drawable.placeholder).into(myViewHolder.image);
        }
        User by=getItem(i).getBy();
        myViewHolder.shotBy.setText("@"+by.getUsername());
        Glide.with(context).load(by.getImage()).placeholder(R.drawable.user_img).into(myViewHolder.shotByImage);



//        myViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        myViewHolder.recyclerView.setHasFixedSize(true);
//        CommentAdapter adapter = new CommentAdapter(context);
//        myViewHolder.recyclerView.setAdapter(adapter);
//        adapter.submitList(getItem(i).getCommentsOnThisShot());

    }

    public Shot getShotAt(int position){
        return getItem(position);
    }

}
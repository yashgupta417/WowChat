package com.WowChat.ModalBottomSheet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.WowChat.Activities.GalleryActivity;
import com.WowChat.Activities.NewGroupActivity;
import com.WowChat.LoadingDialog;
import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Repository.MyRepository;
import com.WowChat.Retrofit.GroupMessage;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.RetrofitClient;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EditGroupBottomSheetDialog extends BottomSheetDialogFragment {

    private String groupId;

    public EditGroupBottomSheetDialog(String groupId){
        this.groupId=groupId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_group_bottom_sheet_layout, container, false);
        TextView textView=v.findViewById(R.id.ed_grp_btm_sht_textView1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGroupDp();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    Uri image;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(getContext(), GalleryActivity.class);
                startActivityForResult(intent,1);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            image=data.getData();
            if(image!=null) {
                updateGroupDp(groupId, image);
            }
        }
    }
    public void chooseGroupDp(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
            }else{
                Intent intent=new Intent(getContext(),GalleryActivity.class);
                startActivityForResult(intent,1);

            }
        }
    }
    public void updateGroupDp(String group_id,Uri image){
        final LoadingDialog dialog=new LoadingDialog(getActivity());
        dialog.showDialog();
        File file = new File(image.toString());
        File compressimagefile=null;
        try {
            compressimagefile =new Compressor(getContext()).compressToFile(file);
        } catch (IOException e) {
            compressimagefile=file;
            e.printStackTrace();
        }

        final RequestBody requestBody = RequestBody.create( compressimagefile, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("group_image",file.getName(),requestBody);

        RetrofitClient retrofitClient=new RetrofitClient();
        Call<GroupRead> call=retrofitClient.jsonPlaceHolderApi.updateGroupDP(group_id,imagePart);
        call.enqueue(new Callback<GroupRead>() {
            @Override
            public void onResponse(Call<GroupRead> call, Response<GroupRead> response) {
                dialog.hideDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                GroupRead group=response.body();
                //GroupRepository repository=new GroupRepository(getActivity().getApplication());
                //repository.insertOrUpdateGroup(Integer.toString(group.getGroupId()),group.getGroupName(),group.getGroupImage());
                MyRepository repository=new MyRepository(getActivity().getApplication());
                repository.updateImage(group.getGroupImage(),Integer.toString(group.getGroupId()));
                mListener.onGroupDpUpdated(group.getGroupImage());
                Toast.makeText(getContext(), "Group DP changed", Toast.LENGTH_SHORT).show();
                dismiss();
            }

            @Override
            public void onFailure(Call<GroupRead> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
    private OnImageUpdatedListener mListener;
    public interface OnImageUpdatedListener {
        void onGroupDpUpdated(String image);
    }

    public void setOnImageUpdatedListener(OnImageUpdatedListener listener){
        mListener=listener;
    }
}

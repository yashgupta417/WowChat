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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.WowChat.Activities.GalleryActivity;
import com.WowChat.LoadingDialog;
import com.WowChat.R;
import com.WowChat.Repository.MyRepository;
import com.WowChat.Retrofit.MemoryWrite;
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

public class AddMemoryBottomSheetDialog extends BottomSheetDialogFragment {
    private String groupId;
    private String myId;
    ImageView imageView;
    TextView post;
    EditText text;
    public AddMemoryBottomSheetDialog(String groupId,String myId) {
        this.groupId=groupId;
        this.myId=myId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_memory_bottom_sheet_layout, container, false);
        imageView=v.findViewById(R.id.memory_add_image);
        post=v.findViewById(R.id.add_memory_post);
        text=v.findViewById(R.id.memory_add_edittext);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGroupDp();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Write Something", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image!=null ){
                    postMemory();
                }
            }
        });
        return v;
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
                post.setEnabled(true);
                post.setAlpha(1f);
                post.setTextColor(getResources().getColor(R.color.wowblue));
                Glide.with(getContext()).load(image.getPath()).into(imageView);
                //updateGroupDp(groupId, image);
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

    public void postMemory(){
        final LoadingDialog dialog=new LoadingDialog(getActivity());
        dialog.showDialog();
        File file = new File(image.getPath());
        File compressimagefile = null;
        try {
            compressimagefile = new Compressor(getContext()).compressToFile(file);
        } catch (IOException e) {
            compressimagefile = file;
            e.printStackTrace();
        }

        final RequestBody requestBody = RequestBody.create(compressimagefile, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody textPart = RequestBody.create(text.getText().toString(), MediaType.parse("multipart/form-data"));
        RequestBody memberPart = RequestBody.create(myId, MediaType.parse("multipart/form-data"));
        RequestBody groupPart = RequestBody.create(groupId, MediaType.parse("multipart/form-data"));
        RetrofitClient client=new RetrofitClient();
        Call<MemoryWrite> call=client.jsonPlaceHolderApi.postMemory(textPart,groupPart,memberPart,imagePart);
        call.enqueue(new Callback<MemoryWrite>() {
            @Override
            public void onResponse(Call<MemoryWrite> call, Response<MemoryWrite> response) {
                dialog.hideDialog();
                dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MemoryWrite> call, Throwable t) {
                dialog.hideDialog();
                dismiss();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

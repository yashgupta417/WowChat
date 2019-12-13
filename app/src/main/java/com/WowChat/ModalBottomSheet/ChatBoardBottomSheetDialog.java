package com.WowChat.ModalBottomSheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Repository.MyRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChatBoardBottomSheetDialog extends BottomSheetDialogFragment {

    private String personId;

    public ChatBoardBottomSheetDialog(String personId)
    {
        this.personId=personId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatboard_bottom_sheet_layout, container, false);
        TextView deleteTextView=v.findViewById(R.id.chatBoard_bottomsheet_delete);
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChat();
                dismiss();
            }
        });
        return v;
    }

    public void deleteChat(){
        MyRepository repository=new MyRepository(getActivity().getApplication());
        repository.deleteChat(personId);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}

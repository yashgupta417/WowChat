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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class GroupBottomSheetDialog extends BottomSheetDialogFragment {

    private String groupId;

    public GroupBottomSheetDialog(String groupId) {
        this.groupId=groupId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.group_bottoom_modal_sheet_layout, container, false);
        TextView deleteTextView=v.findViewById(R.id.g_bottomsheet_delete);
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroup();
                dismiss();
            }
        });
        return v;
    }

    public void deleteGroup(){
        GroupRepository repository=new GroupRepository(getActivity().getApplication());
        repository.deleteGroup(groupId);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}

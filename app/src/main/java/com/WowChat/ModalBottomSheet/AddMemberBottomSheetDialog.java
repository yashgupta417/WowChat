package com.WowChat.ModalBottomSheet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.Activities.NewGroupActivity;
import com.WowChat.Adapters.FriendsAdapter;
import com.WowChat.LoadingDialog;
import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Repository.MyRepository;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.UserInfoTable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberBottomSheetDialog extends BottomSheetDialogFragment {

    private String groupId,myId;
    public ArrayList<UserInfoTable> selectedFriends,friends,allFriends;
    public  ArrayList<User> members;
    public ArrayList<Boolean> selectedOrNot;
    TextView done;
    public AddMemberBottomSheetDialog(String groupId,ArrayList<User> members,String myId) {
        this.groupId=groupId;
        this.members=members;
        this.myId=myId;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_member_bottom_sheet_layout, container, false);
        selectedOrNot=new ArrayList<Boolean>();
        friends=new ArrayList<UserInfoTable>();
        selectedFriends=new ArrayList<UserInfoTable>();
        final MyRepository repository=new MyRepository(getActivity().getApplication());
        allFriends=new ArrayList<UserInfoTable>(repository.getAllFriends());
        for(UserInfoTable x: allFriends){
            int flag=0;
            for(User y:members){
                if(x.getPersonId().equals(Integer.toString(y.getId()))){
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                friends.add(x);
            }
        }
        for(int i = 0; i< friends.size(); i++){
            selectedOrNot.add(false);
        }
        done=v.findViewById(R.id.add_member_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFriendsCopy=new ArrayList<UserInfoTable>(selectedFriends);
                dialog=new LoadingDialog(getActivity());
                dialog.showDialog();
                addMember(selectedFriends.get(0).getPersonId(),groupId);
            }
        });
        setUpRecycleView(v);
        return v;
    }
    LoadingDialog dialog;
    public void setUpRecycleView(View v){

        RecyclerView friendsRecyclerView=v.findViewById(R.id.wall_add_member_recyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsRecyclerView.setHasFixedSize(true);
        final FriendsAdapter adapter=new FriendsAdapter(getContext(), friends,selectedOrNot);
        friendsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new FriendsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                adapter.selectedOrNot.set(position,!adapter.selectedOrNot.get(position));
                adapter.notifyItemChanged(position);
                if(adapter.selectedOrNot.get(position)){
                    selectedFriends.add(adapter.friends.get(position));
                }else{
                    int index=selectedFriends.indexOf(adapter.friends.get(position));
                    selectedFriends.remove(index);
                }

                if(selectedFriends.size()==0){
                    done.setEnabled(false);
                    done.setAlpha(0.2f);
                }else{
                    done.setEnabled(true);
                    done.setAlpha(1f);
                }

            }
        });
    }
    ArrayList<UserInfoTable> selectedFriendsCopy;

    public void addMember(String user_id, final String group_id){
        RetrofitClient retrofitClient=new RetrofitClient();
        Call<User> call=retrofitClient.jsonPlaceHolderApi.addMember(user_id, group_id,myId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedFriends.remove(0);
                if(selectedFriends.size()==0){
                    mListener.onGroupMembersAdded(selectedFriendsCopy);
                    dialog.hideDialog();
                    dismiss();
                }else{
                    addMember(selectedFriends.get(0).getPersonId(),group_id);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
    private OnMembersAddedListener mListener;
    public interface OnMembersAddedListener {
        void onGroupMembersAdded(ArrayList<UserInfoTable> newMembers);
    }

    public void setOnMembersAddedListener(OnMembersAddedListener listener){
        mListener=listener;
    }
}

package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.WowChat.Adapters.MessageAdapter;
import com.WowChat.Repository.MyRepository;
import com.WowChat.Retrofit.Message;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.Entities.UserInfoTable;
import com.WowChat.ViewModel.ChatActivityViewModel;
import com.bumptech.glide.Glide;
import com.WowChat.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private com.WowChat.ViewModel.ChatActivityViewModel viewModel;
    CircleImageView circleImageView;
    TextView textView;
    EditText textMessage;
    Integer friendId;
    Integer me_id;
    String meUsername;
    String friendImageURL,friendFirstName,friendUsername,friendEmail,friendLastName;
    Button button;
    ImageView heart;
    public static RecyclerView recyclerView;
    MessageAdapter adapter;
    TextToSpeech textToSpeech;
    TextView lastSeen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        circleImageView=findViewById(R.id.message_toolbar_image);
        textView=findViewById(R.id.message_toolbar_name);
        textMessage=findViewById(R.id.textMessage);
        button=findViewById(R.id.send_button);
        heart=findViewById(R.id.heart);
        lastSeen=findViewById(R.id.last_seen);

        friendImageURL=getIntent().getStringExtra("image");
        friendFirstName=getIntent().getStringExtra("firstName");
        friendUsername=getIntent().getStringExtra("username");
        friendId=Integer.parseInt(getIntent().getStringExtra("id"));
        friendLastName=getIntent().getStringExtra("lastName");
        friendEmail=getIntent().getStringExtra("email");


        textView.setText(friendFirstName);
        if(friendImageURL.equals("")){
            Glide.with(this).load(R.drawable.user_img).into(circleImageView);
        }else{
            Glide.with(this).load(friendImageURL).placeholder(R.drawable.user_img).into(circleImageView);
        }



        final SharedPreferences sharedPreferences=this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        meUsername=sharedPreferences.getString("username","");
        me_id=Integer.parseInt(sharedPreferences.getString("id",""));


         recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new MessageAdapter(this);
        recyclerView.setAdapter(adapter);
        viewModel= ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        viewModel.getMessages(Integer.toString(friendId)).observe(this, new Observer<List<MessageTable>>() {
            @Override
            public void onChanged(final List<MessageTable> messageTables) {
                recyclerView.scrollToPosition(messageTables.size()-1);
                adapter.submitList(messageTables);



            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
                Log.i("*****",Integer.toString(adapter.getItemCount()-1));
            }
        });
        editTextWork();
        seenWork();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                getLastSeen();
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(r,2000);


    }
    public void editTextWork(){
        textMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=s.toString();
                if(text.trim().isEmpty()){
                    button.setEnabled(false);
                    heart.setEnabled(true);
                    heart.setVisibility(View.VISIBLE);
                    button.setVisibility(View.INVISIBLE);

                }else{
                    button.setEnabled(true);
                    heart.setEnabled(false);
                    heart.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void seenWork(){
        viewModel.getUnseenMessages(Integer.toString(friendId)).observe(this, new Observer<List<MessageTable>>() {
            @Override
            public void onChanged(final List<MessageTable> messageTables) {
                for(int i=0;i<messageTables.size();i++){
                    MyRepository myRepository=new MyRepository(getApplication());
                    myRepository.updateMessageStatus(messageTables.get(i).getMessageid(),"Seen");
                    myRepository.setUnseenCount(Integer.toString(friendId));
                   RetrofitClient retrofitClient=new RetrofitClient();
                   Call<Message> call=retrofitClient.jsonPlaceHolderApi.updateMessageStatus(messageTables.get(i).getMessageid(),"Seen");
                   call.enqueue(new Callback<Message>() {
                       @Override
                       public void onResponse(Call<Message> call, Response<Message> response) {

                       }

                       @Override
                       public void onFailure(Call<Message> call, Throwable t) {

                       }
                   });
                }
            }
        });
    }
    public void setUpListening(){
        final SharedPreferences sharedPreferences=this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    float speed= (float) (sharedPreferences.getInt("speed",50)/50.00);
                    float pitch= (float) (sharedPreferences.getInt("pitch",50)/50.00);
                    Log.i("####speed1:",  String.valueOf(speed));
                    Log.i("####pitch1:",  String.valueOf(pitch));
                    if (speed<0.01) speed=0.01f;
                    if (pitch<0.01) pitch=0.01f;
                    Log.i("####speed2:",  String.valueOf(speed));
                    Log.i("####pitch2:",  String.valueOf(pitch));
                    textToSpeech.setSpeechRate(speed);
                    textToSpeech.setPitch(pitch);

                }
            }
        });
        adapter.setOnItemClickListener(new MessageAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String text=adapter.getMessageTableAt(position).getText();
                textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH, null,null);
            }
        });
    }
    public void goToOtherProfile(View view){
        Intent intent=new Intent(getApplicationContext(),OtherProfileActivity.class);
        intent.putExtra("image",friendImageURL);
        intent.putExtra("first_name",friendFirstName);
        intent.putExtra("last_name",friendLastName);
        startActivity(intent);
    }
    public void onClickShot(View view){
        Intent intent=new Intent(getApplicationContext(), ShotsActivity.class);
        intent.putExtra("me_id",Integer.toString(me_id));
        intent.putExtra("friend_id",Integer.toString(friendId));
        startActivity(intent);
    }
    public void onClickSendButton(View view){

        sendWork(textMessage.getText().toString());
        textMessage.setText("");
    }
    public void getLastSeen(){

        RetrofitClient retrofitClient=new RetrofitClient();
        Call<User> call=retrofitClient.jsonPlaceHolderApi.getLastSeen(friendUsername);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user=response.body();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat timePreciseformat = new SimpleDateFormat("hh:mm:ss a");
                SimpleDateFormat dateformat = new SimpleDateFormat("d MMM yyyy");
                String currentDateString=dateformat.format(c.getTime());
                String currentTimeString=timePreciseformat.format(c.getTime());
                String dateString=user.getLastSeenDate();
                String timeString=user.getLastSeenTime();

                try {
                    Date currentDate = dateformat.parse(currentDateString);
                    Date currentTime=timePreciseformat.parse(currentTimeString);
                    Date time=timePreciseformat.parse(timeString);
                    Date date=dateformat.parse(dateString);
                    String timeToDisplay=timeString.substring(0,5)+" "+timeString.substring(9);
                    if(currentDate.compareTo(date)==0){
                        long diff=currentTime.getTime()-time.getTime();
                        int diffInSecs=(int)diff/1000;
                        if(diffInSecs<10)
                            lastSeen.setText("online");
                        else if(diffInSecs>=10 && diffInSecs<=15)
                            lastSeen.setText(" ");
                        else
                            lastSeen.setText("last seen: "+timeToDisplay);

                    }else{
                        lastSeen.setText("last seen at"+dateString);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    lastSeen.setText(e.getMessage());
                }





            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    public void onClickHeart(View view){
        sendWork("\uD83D\uDC4B");
    }
    public void sendWork(String text){
        //https://stackoverflow.com/questions/5369682/get-current-time-and-date-on-android , 2nd or 3rd answer
        Calendar c = Calendar.getInstance();
        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat timePreciseformat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat dateformat = new SimpleDateFormat("d MMM yyyy");

        String time = timeformat.format(c.getTime());
        String timePrecise=timePreciseformat.format(c.getTime());
        String date=dateformat.format(c.getTime());
        String id=UUID.randomUUID()+date+timePrecise;
        Message message=new Message(id,text,me_id,friendId,date,time);



        UserInfoTable userInfoTable=new UserInfoTable(friendUsername,
                    friendFirstName,
                    friendLastName,
                    friendEmail,
                    friendImageURL,
                    Integer.toString(friendId),
                    text,
                    time,
                    date);

        viewModel.updateOrCreateUserInfo(userInfoTable);

        final MessageTable messageTable=new MessageTable(id,text,Integer.toString(me_id),Integer.toString(friendId),date,time);
        viewModel.insertMessage(messageTable);


         RetrofitClient retrofitClient=new RetrofitClient();
        Call<Message> call=retrofitClient.jsonPlaceHolderApi.sendMessage(message);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ChatActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ChatActivity.this, "sent", Toast.LENGTH_SHORT).show();
                Message rMessage=response.body();
                MessageTable messageToBeUpdated=new MessageTable(rMessage.getId(),
                                                                 rMessage.getText(),
                                                                 Integer.toString(rMessage.getSender()),
                                                                Integer.toString(rMessage.getRecipient()),
                                                                rMessage.getDate(),
                                                                rMessage.getTime());
                messageToBeUpdated.setStatus("On Server");
                viewModel.updateMessage(messageToBeUpdated);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

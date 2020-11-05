package com.WowChat.Retrofit;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @POST("login/")
    Call<User> loginAndGetToken(@Body User user);

    @POST("signup/")
    Call<User> signUp(@Body User user);

    @POST("logout/{id}/")
    Call<User> logout(@Path("id") String id);

    @GET("user_detail/{username}")
    Call<User> getUserDetails(@Path("username") String username);

    @GET("get_last_seen/")
    Call<User> getLastSeen(@Query("username") String username);

    @Multipart
    @PATCH("user_detail/{username}/")
    Call<User> updateProfileImage(@Path("username") String username, @Part MultipartBody.Part image);


    @PATCH("user_detail/{username}/")
    Call<User> updateLastSeen(@Body User user,@Path("username") String username);

    @POST("message_list/")
    Call<Message> sendMessage(@Body Message message);

    @POST("create_fcm_token/")
    Call<FCMToken> createFCMToken(@Body FCMToken fcmToken);

    @PATCH("update_fcm_token/")
    Call<FCMToken> updateFCMToken(@Query("registration_id") String registration_id, @Query("user") String user);

    @Multipart
    @POST("message_list/")
    Call<Message> sendImageMessage(@Part("id") RequestBody id,
                                @Part("text") RequestBody text,
                                @Part("sender") RequestBody sender,
                                @Part("recipient") RequestBody recipient,
                                @Part("date") RequestBody date,
                                @Part("time") RequestBody time,
                                @Part("amorpm") RequestBody amorpm,
                                @Part MultipartBody.Part image);

    @POST("update_message_status/")
    Call<Message> updateMessageStatus(@Query("id")String id,@Query("status") String status);

    @GET("user_list/")
    Call<List<User>> queryUsers(@Query("query") String q);

    @POST("group_list/")
    Call<GroupWrite> createGroup(@Body GroupWrite groupWrite);

    @POST("add_member/")
    Call<User> addMember(@Query("user_id") String userId,@Query("group_id") String groupId,@Query("member_id") String myId);

    @POST("send_group_message/")
    Call<GroupMessage> sendGroupMessage(@Body GroupMessage message);

    @GET("group_detail/{group_id}")
    Call<GroupRead> getGroupDetails(@Path("group_id") String group_id);

    @Multipart
    @POST("send_group_message/")
    Call<GroupMessage> sendGroupImageMessage(@Part("id") RequestBody id,
                                   @Part("text") RequestBody text,
                                   @Part("sender") RequestBody sender,
                                   @Part("group") RequestBody group,
                                   @Part("date") RequestBody date,
                                   @Part("time") RequestBody time,
                                   @Part("amorpm") RequestBody amorpm,
                                   @Part MultipartBody.Part image);

    @Multipart
    @PATCH("group_detail/{group_id}/")
    Call<GroupRead> updateGroupDP(@Path("group_id") String groupId, @Part MultipartBody.Part image);

//    @Multipart
//    @POST("memory_create/")
//    Call<MemoryWrite> postMemory(@Part("text") RequestBody text,
//                                             @Part("group") RequestBody group,
//                                             @Part("member_posted") RequestBody memberPosted,
//                                             @Part MultipartBody.Part image);
//    @GET("memory_list/")
//    Call<List<MemoryRead>> getMemories(@Query("group_id") String groupId);

    @GET("group_query/")
    Call<List<GroupRead>> queryGroups(@Query("query") String groupName);

    @POST("follow_group/")
    Call<User> followGroup(@Query("user_id") String userId,@Query("group_id") String groupId);

//    @GET("feeds/")
//    Call<List<MemoryRead> > getFeeds(@Query("user_id") String userId);
}

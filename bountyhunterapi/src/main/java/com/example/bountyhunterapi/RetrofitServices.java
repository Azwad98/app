package com.example.bountyhunterapi;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the HTTP operations that will be used by retrofit.
 * Retrofit will automatically generate the body of the declared methods
 */
public interface RetrofitServices {

    @FormUrlEncoded
    @POST("/users/")
    Call<Void> registerUser(@Field("firstName") String firstName, @Field("lastName") String lastName, @Field("username") String username, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/users/login")
    Call<Token> loginUser(@Field("username") String username, @Field("password") String password);

    //Creates the GET request that will be sent to request the JSON data
    @GET("/users/{id}")
    Call<User> getUser(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @GET("/users/me")
    Call<User> getLoggedInUser(@Header("Authorization") String authKey);

    @FormUrlEncoded
    @PUT("/users/{id}")
    Call<User> updateUser(@Header("Authorization") String authKey, @Path("id") UUID userID, @Field("firstName") String firstName, @Field("lastName") String lastName, @Field("username") String username, @Field("email") String email);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/users/{id}", hasBody = true)
    Call<Void> deleteUser(@Header("Authorization") String authKey, @Path("id") UUID userID, @Field("password") String password);

    @GET("/users/search/{username}")
    Call<UserList> searchUser(@Header("Authorization") String authKey, @Path("username") String username);

    @FormUrlEncoded
    @PATCH("/users/resetpassword")
    Call<Void> resetPasswordRequest(@Field("email") String email);

    @FormUrlEncoded
    @POST("/users/resetpassword/{token}")
    Call<Void> resetPassword(@Path("token") String token, @Field("newpassword") String newPassword);

    @FormUrlEncoded
    @PUT("/users/{id}/changepassword")
    Call<Void> changePassword(@Header("Authorization") String authKey, @Path("id") UUID userID, @Field("password") String currentPassword, @Field("newPassword") String newPassword);

    @GET("/users/stats/{id}/fugitive")
    Call<FugitiveStatList> getFugitiveStats(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @GET("/users/stats/{id}/bountyhunter")
    Call<BountyHunterStatList> getBountyHunterStats(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @GET("/users/friends/{id}/followers")
    Call<FriendList> getFriendsFollowers(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @GET("/users/friends/{id}/following")
    Call<FriendList> getFriendsFollowing(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @POST("/users/friends/{id}/friend")
    Call<Void> addFriend(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @DELETE("/users/friends/{id}/friend")
    Call<Void> removeFriend(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @POST("/users/friends/{id}/block")
    Call<Void> blockFriend(@Header("Authorization") String authKey, @Path("id") UUID userID);

    @DELETE("/users/friends/{id}/block")
    Call<Void> unblockFriend(@Header("Authorization") String authKey, @Path("id") UUID userID);

}
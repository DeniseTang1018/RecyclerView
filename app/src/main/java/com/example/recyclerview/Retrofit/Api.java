package com.example.recyclerview.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @FormUrlEncoded
    @POST("login")
    Call<User> login(

            @Field("Username") String userName,
            @Field("Password") String userPassword
    );

    @GET("events")
    Call<List<ListItem>> getEvents(
            @Header("Authorization") String authToken
    );

    @GET("speakers/{id}")
    Call<Speaker> getSpeaker(
            @Header("Authorization") String authToken,
            @Query("id") int id
    );

     @GET("events")
    Call<Event> getEvent(
            @Header("Authorization")String authToken,
            @Query("id") int id
     );

}

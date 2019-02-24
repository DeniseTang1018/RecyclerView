package com.example.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerview.Retrofit.Event;
import com.example.recyclerview.Retrofit.RetrofitClient;
import com.example.recyclerview.Retrofit.Speaker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSpeaker;
    private RecyclerView.Adapter adapter;
    private List<Speaker> speakerList;
    private ImageView imageViewD;
    private TextView textViewDName;
    private TextView textViewDTime;
    private TextView textViewDLocation;
    private TextView textViewDDescrip;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewD = (ImageView) findViewById(R.id.ivDetail);
        textViewDName = (TextView) findViewById(R.id.tvDeatilTitle);
        textViewDTime = (TextView)findViewById(R.id.tvDetailTime);
        textViewDLocation = (TextView)findViewById(R.id.tvDetailLocation);
        textViewDDescrip = (TextView)findViewById(R.id.tvDetailDescrip);
        speakerList = new ArrayList<>();

        Intent intent = getIntent();

        recyclerViewSpeaker = (RecyclerView) findViewById(R.id.rvSpeakers);
        int id = intent.getIntExtra("id",0);
        Call<Event> call = RetrofitClient.getInstance().getApi().getEvent("supersecrettoken", id);
        Log.d(" check id number " ,""+id);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Log.d(" check bollean " ,""+response.isSuccessful());

                if(response.isSuccessful()){

                    event = response.body();
                    Picasso.get().load(event.getImageUrl()).fit().centerInside().into(imageViewD);
                    textViewDDescrip.setText(event.getEventDescription());
                    textViewDName.setText(event.getTitle());
                    Date sDate = null;
                    Date eDate = null;
                    String eventTime = "";
                    try {

                        sDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").parse(event.getStartDateTime());

                        eDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").parse(event.getEndDateTime());
                        eventTime = new SimpleDateFormat("yyyy-MM-dd KK:mm a").format(sDate)+ " - " + new SimpleDateFormat("KK:mm a").format(eDate) ;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    textViewDTime.setText(eventTime);
                    textViewDLocation.setText(event.getLocation());



                    for(int i=0; i<event.getSpeakers().size();i++) {
                        Call<Speaker> callSpeaker = RetrofitClient.getInstance().getApi().getSpeaker("supersecrettoken", event.getSpeakers().get(i).getId());
                        callSpeaker.enqueue(new Callback<Speaker>() {
                            @Override
                            public void onResponse(Call<Speaker> call, Response<Speaker> response) {
                                if(response.isSuccessful()){
                                    speakerList.add(response.body());
                                    initRecyclerView();
                                }
                            }

                            @Override
                            public void onFailure(Call<Speaker> call, Throwable t) {

                            }
                        });
                    }
                }
                else
                {
                    try {
                        Log.v("Error ",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.d("Detaile Activity Error",t.getMessage());
                Toast.makeText(DetailActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void initRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
        adapter = new SpeakerAdapter(speakerList, DetailActivity.this);
        recyclerViewSpeaker.setLayoutManager(layoutManager);
        recyclerViewSpeaker.setAdapter(adapter);
    }
}

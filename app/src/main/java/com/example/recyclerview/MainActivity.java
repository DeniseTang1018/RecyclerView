package com.example.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.example.recyclerview.Retrofit.ListItem;
import com.example.recyclerview.Retrofit.RetrofitClient;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener  {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    //private MyAdapter adapter;
    private List<ListItem> listItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        recyclerView.setHasFixedSize(true);

        Call<List<ListItem>> call = RetrofitClient.getInstance().getApi().getEvents("supersecrettoken");
        call.enqueue(new Callback<List<ListItem>>() {
            @Override
            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                if(response.isSuccessful()){
                    listItems = response.body();
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    initRecyclerView();

                }
            }

            @Override
            public void onFailure(Call<List<ListItem>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"token failed",Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id",listItems.get(position).getId());
        startActivity(intent);

    }
    private void initRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        adapter = new MyAdapter(listItems, MainActivity.this,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

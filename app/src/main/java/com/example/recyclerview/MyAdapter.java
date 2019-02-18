package com.example.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerview.Retrofit.ListItem;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;




    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ListItem listItem = listItems.get(i);
        viewHolder.textViewTitle.setText(listItem.getTitle());
        Date sDate = null;
        Date eDate = null;
        String eventTime = "";
        try {

            sDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").parse(listItem.getStartDateTime());

            eDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").parse(listItem.getEndDateTime());
            eventTime = new SimpleDateFormat("yyyy-MM-dd KK:mm a").format(sDate)+ " - " + new SimpleDateFormat("KK:mm a").format(eDate) ;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.textViewTime.setText(eventTime);
        Picasso.get().load(listItem.getImageUrl()).fit().centerInside().into(viewHolder.imageView);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You click "+ listItem.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewTitle ;
        public TextView textViewTime;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = (TextView) itemView.findViewById(R.id.tvTime);
            textViewTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            imageView= (ImageView)  itemView.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }

}

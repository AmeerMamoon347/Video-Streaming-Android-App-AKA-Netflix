package com.example.livevideostreaming.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.livevideostreaming.MainScreens.MovieDetails;
import com.example.livevideostreaming.MainScreens.Movies;
import com.example.livevideostreaming.Models.AllCategory;
import com.example.livevideostreaming.Models.CategoryItemList;
import com.example.livevideostreaming.R;

import org.w3c.dom.Text;

import java.util.*;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.viewHolder> {

    Context context;
    List<CategoryItemList> categoryItemList;
    List<CategoryItemList> moviesItemList;

    public ItemRecyclerViewAdapter(Context context, List<CategoryItemList> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
        this.moviesItemList = new ArrayList<>(categoryItemList);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
       ImageView imageView;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categroy_rowItem);

        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(categoryItemList.get(position).getImageUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetails.class);
                intent.putExtra("id",categoryItemList.get(position).getId());
                intent.putExtra("imageUrl",categoryItemList.get(position).getImageUrl());
                intent.putExtra("fileUrl",categoryItemList.get(position).getFileUrl());
                intent.putExtra("movieName",categoryItemList.get(position).getMovieName());
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }


}

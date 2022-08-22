package com.example.livevideostreaming.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.livevideostreaming.MainScreens.MovieDetails;
import com.example.livevideostreaming.MainScreens.Movies;
import com.example.livevideostreaming.MainScreens.TvSeries;
import com.example.livevideostreaming.Models.AllCategory;
import com.example.livevideostreaming.Models.CategoryItemList;
import com.example.livevideostreaming.R;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.mainViewHolder> {

    Context context;
    List<AllCategory> allCategoryList;

    public MainRecyclerViewAdapter(Context context, List<AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
    }

    public class mainViewHolder extends  RecyclerView.ViewHolder {

        TextView tv_Category;
        RecyclerView main_ItemRecyclerView;

        public mainViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Category = itemView.findViewById(R.id.tv_category);
            main_ItemRecyclerView = itemView.findViewById(R.id.mainItemRecyclerView);
        }
    }

    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainscreen__itemlayout,parent,false);
        return new mainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mainViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.tv_Category.setText(allCategoryList.get(position).getCategoryTitle());
            setRecyclerView(holder.main_ItemRecyclerView,allCategoryList.get(position).getCategoryItemList());

    }

    private void setRecyclerView(RecyclerView recyclerView, List<CategoryItemList> categoryItemList) {
      ItemRecyclerViewAdapter itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(context,categoryItemList);
      recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
      recyclerView.setAdapter(itemRecyclerViewAdapter);


    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

}

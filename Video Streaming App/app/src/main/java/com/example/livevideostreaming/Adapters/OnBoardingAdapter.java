package com.example.livevideostreaming.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livevideostreaming.R;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.viewHolder> {

    Context context;

    public OnBoardingAdapter(Context context)
    {
        this.context = context;
    }

    class viewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.onboarding_itemview,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        if(position == 0)
        {
            holder.imageView.setImageResource(R.drawable.onboard_2);
        }
        if(position == 1)
        {
            holder.imageView.setImageResource(R.drawable.onboard_5);
        }
        if(position == 2)
        {
            holder.imageView.setImageResource(R.drawable.onboard_3);
        }
        if(position == 3)
        {
            holder.imageView.setImageResource(R.drawable.onboard_4);
        }




    }



    @Override
    public int getItemCount() {
        return 4;
    }
}

package com.example.livevideostreaming.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livevideostreaming.Models.AllCategory;
import com.example.livevideostreaming.Models.CategoryItemList;
import com.example.livevideostreaming.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.searchViewHolder>
        implements Filterable
{     Context context;
      List<AllCategory> allCategoryList;
      List<AllCategory> fullList;

    public SearchRecyclerViewAdapter(Context context, List<AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
        fullList = new ArrayList<>(allCategoryList);
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }

    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<AllCategory> filter_result = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0)
            {
                filter_result.addAll(allCategoryList);
            } else
            {
                String pattern = charSequence.toString().toLowerCase().trim();
                for(AllCategory item : fullList)
                {
                    if(item.getCategoryTitle().toLowerCase().contains(pattern))
                    {
                       filter_result.add(item);
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filter_result;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allCategoryList.clear();
            allCategoryList.addAll((List)results.values);

            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_itemlayout,parent,false);
        return new searchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchViewHolder holder, int position) {

        setItemRecyclerView(holder.recyclerView,allCategoryList.get(position).getCategoryItemList());

    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

    public class searchViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        public searchViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.searchRecyclerView);

        }
    }

    private void setItemRecyclerView(RecyclerView recyclerView,List<CategoryItemList> categoryItemList)
    {
        ItemRecyclerViewAdapter itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(context,categoryItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerViewAdapter);
    }



}

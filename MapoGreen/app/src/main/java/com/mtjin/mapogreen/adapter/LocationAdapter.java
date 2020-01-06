package com.mtjin.mapogreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtjin.mapogreen.R;
import com.mtjin.mapogreen.model.Document;
import com.mtjin.mapogreen.model.category_search.CategoryResult;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    Context context;
    ArrayList<CategoryResult> filteredItems;
    ArrayList<CategoryResult> items;

    public LocationAdapter(ArrayList<CategoryResult> items, Context context){
        this.context =  context;
        this.filteredItems = items;
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }


    public  void addItem(CategoryResult item){
        filteredItems.add(item);
    }


    public void clear(){
        filteredItems.clear();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_location, viewGroup, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int i) {
        final CategoryResult model = filteredItems.get(i);
    }


    public class LocationViewHolder extends RecyclerView.ViewHolder {

        public LocationViewHolder(@NonNull final View itemView) {
            super(itemView);
        }
    }
}

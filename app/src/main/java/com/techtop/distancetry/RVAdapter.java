package com.techtop.distancetry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<TripData> list = new ArrayList<>();

    public RVAdapter(Context ctx)
    {
        this.context = ctx;
    }

    public void setItems(ArrayList<TripData> td)
    {
        list.addAll(td);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        return new TripDataVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        TripDataVH vh = (TripDataVH) holder;
        TripData td = list.get(position);
        vh.trip_date.setText(td.getStartTime());
        vh.t_date.setText("Date: " +td.getDateTxt());
        vh.end_time.setText("End Time: "+td.getStartTime());
        vh.fare.setText("Total Fare: "+td.getTotFare());
        vh.hire_distance.setText("Distance: "+td.getDistance());
        vh.hire_time.setText("Hire Time: "+td.getTotHireTime());
        vh.waiting_time.setText("Waiting Time: "+td.getWaitTime());
        vh.wait_fee.setText("Waiting Fee (For 1min): "+td.getWaitRate());
        vh.start_time.setText("Start Time: "+td.getStartTime());
        vh.onekm_fee.setText("Fee for 1km: "+td.getOneKmRateTxt());
        vh.twokm_fee.setText("Fee for 2km: "+ td.getTwoKmRate());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

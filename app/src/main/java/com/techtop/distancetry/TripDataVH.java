package com.techtop.distancetry;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripDataVH extends RecyclerView.ViewHolder
{
    public TextView t_date,trip_date,fare,waiting_time,hire_time,hire_distance,onekm_fee,twokm_fee,start_time,end_time, wait_fee;
    public TripDataVH(@NonNull View itemView) {
        super(itemView);
        t_date = itemView.findViewById(R.id.date);
        trip_date = itemView.findViewById(R.id.txt_date);
        fare = itemView.findViewById(R.id.tothiretxt);
        waiting_time = itemView.findViewById(R.id.wating_time);

        hire_time = itemView.findViewById(R.id.hire_time);
        hire_distance = itemView.findViewById(R.id.hire_distance);
        onekm_fee= itemView.findViewById(R.id.onekm_fee);
        twokm_fee = itemView.findViewById(R.id.twokm_fee);
        start_time = itemView.findViewById(R.id.start_time);
        end_time = itemView.findViewById(R.id.end_time);
        wait_fee  =itemView.findViewById(R.id.waiting_fee);

    }
}

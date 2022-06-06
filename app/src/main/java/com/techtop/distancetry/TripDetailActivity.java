package com.techtop.distancetry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripDetailActivity extends AppCompatActivity {


    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter adapter;
    DAOTripData dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        getSupportActionBar().hide(); //hide the title bar



        swipeRefreshLayout = findViewById(R.id.swip);


        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter = new RVAdapter(this);
        recyclerView.setAdapter(adapter);

        dao = new DAOTripData();
        loadData();
    }

    private void loadData() {
        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TripData> tds = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    TripData td = data.getValue(TripData.class);
                    tds.add(td);
                }
                adapter.setItems(tds);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
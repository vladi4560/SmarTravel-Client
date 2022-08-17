package com.vladi.karasove.smartravel.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vladi.karasove.smartravel.Activities.ViewDialogRating;
import com.vladi.karasove.smartravel.Adapters.TripAdapter;
import com.vladi.karasove.smartravel.CallBacks.CallBackItemClick;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Helpers.Util;
import com.vladi.karasove.smartravel.Objects.Trip;
import com.vladi.karasove.smartravel.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryFragment extends Fragment {

    private AppCompatActivity activity;
    private RecyclerView History_RecyclerView_Trips;
    private TripAdapter tripAdapter;
    private DataManger dataManger;

    CallBackItemClick callBackItemClick = new CallBackItemClick() {
        @Override
        public void itemClick() {
            getParentFragmentManager().beginTransaction().replace(R.id.main_fragment,DaysPathTripFragment.class,null).commit();
        }

        @Override
        public void itemDelete(int position) {
        }
    };


    public Fragment setActivity(AppCompatActivity activity){
        this.activity=activity;
        return this;
    }

    public HistoryFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= LayoutInflater.from(getContext()).inflate(R.layout.fragment_history, container, false);
        dataManger=DataManger.getInstance();
        findViews(view);
        checkTripForRate();
        return view;
    }

    private void findViews(View view) {
        History_RecyclerView_Trips = view.findViewById(R.id.History_RecyclerView_Trips);
        tripAdapter = new TripAdapter(this.activity, dataManger.getHistoryTripList(),callBackItemClick);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.activity, 2);
        History_RecyclerView_Trips.setLayoutManager(mLayoutManager);
        History_RecyclerView_Trips.addItemDecoration(new Util(2, Util.dpToPx(10,getResources()), true));
        History_RecyclerView_Trips.setItemAnimator(new DefaultItemAnimator());
        History_RecyclerView_Trips.setAdapter(tripAdapter);
        tripAdapter.notifyDataSetChanged();
    }

    private void checkTripForRate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
        Date date = new Date();
        if(!dataManger.getHistoryTripList().isEmpty()){
            for (Trip trip:dataManger.getHistoryTripList()) {
                if(!trip.getIsRate()){
                    ViewDialogRating dialogRating=new ViewDialogRating();
                    dialogRating.showDialog(activity,trip,trip.getRates(),callback_viewDialog);
                }
            }
        }
    }

    ViewDialogRating.Callback_ViewDialog callback_viewDialog = new ViewDialogRating.Callback_ViewDialog() {
        @Override
        public void timeToRate(Trip trip) {

        }
    };


}
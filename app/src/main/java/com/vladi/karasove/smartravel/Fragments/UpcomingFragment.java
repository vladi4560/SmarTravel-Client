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

import com.vladi.karasove.smartravel.Adapters.TripAdapter;
import com.vladi.karasove.smartravel.CallBacks.CallBackCreateTrip;
import com.vladi.karasove.smartravel.CallBacks.CallBackItemClick;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Helpers.Util;
import com.vladi.karasove.smartravel.R;

public class UpcomingFragment extends Fragment {

    private AppCompatActivity activity;
    private RecyclerView Upcoming_RecyclerView_Trips;
    private TripAdapter tripAdapter;
    private DataManger dataManger;

    CallBackCreateTrip callBackCreateTrip = new CallBackCreateTrip() {
        @Override
        public void createTrip() {
            tripAdapter.notifyDataSetChanged();
        }
    };

    CallBackItemClick callBackItemClick = new CallBackItemClick() {
        @Override
        public void itemClick() {
            getParentFragmentManager().beginTransaction().replace(R.id.main_fragment, DaysPathTripFragment.class, null).commit();
        }

        @Override
        public void itemDelete(int position) {
            tripAdapter.notifyItemRemoved(position);
        }
    };

    public Fragment setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    public UpcomingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_upcomings, container, false);
        dataManger = DataManger.getInstance();
        dataManger.setCallBackCreateTrip(callBackCreateTrip);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        Upcoming_RecyclerView_Trips = view.findViewById(R.id.Upcoming_RecyclerView_Trips);
        tripAdapter = new TripAdapter(this.activity, dataManger.getUpcomingTripList(), callBackItemClick);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.activity, 2);
        Upcoming_RecyclerView_Trips.setLayoutManager(mLayoutManager);
        Upcoming_RecyclerView_Trips.addItemDecoration(new Util(2, Util.dpToPx(10, getResources()), true));
        Upcoming_RecyclerView_Trips.setItemAnimator(new DefaultItemAnimator());
        Upcoming_RecyclerView_Trips.setAdapter(tripAdapter);
        tripAdapter.notifyDataSetChanged();
    }

}
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

import com.google.android.material.appbar.MaterialToolbar;
import com.vladi.karasove.smartravel.Adapters.DaysAdapter;
import com.vladi.karasove.smartravel.CallBacks.CallBackItemClick;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Helpers.Util;
import com.vladi.karasove.smartravel.R;

public class DaysPathTripFragment extends Fragment {

    private AppCompatActivity activity;
    private MaterialToolbar materialToolbar;

    private RecyclerView daysPathTrip_RecyclerView;
    private DaysAdapter daysAdapter;
    private DataManger dataManger;
    private String dayTitle =  " ";

    CallBackItemClick callBackItemClick = new CallBackItemClick() {
        @Override
        public void itemClick() {
            dayTitle = "Day " + DataManger.getInstance().getCurrentDayTrip().getDayNumber();
            materialToolbar.setTitle(dataManger.getCurrentTrip().getName()+" - "+dayTitle);
            getParentFragmentManager().beginTransaction().replace(R.id.main_fragment,DayTripFragment.class,null).commit();
        }

        @Override
        public void itemDelete(int position) {
        }
    };

    public Fragment setActivity(AppCompatActivity activity){
        this.activity=activity;
        return this;
    }

    public DaysPathTripFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManger = DataManger.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_days_path_trip, container, false);
        findViews(view);
        return view;
    }


    private void findViews(View view) {
        materialToolbar = getActivity().findViewById(R.id.main_Toolbar_Top);
        materialToolbar.setTitle(dataManger.getCurrentTrip().getName()+" - "+dayTitle);

        daysPathTrip_RecyclerView = view.findViewById(R.id.daysPathTrip_RecyclerView);
        daysAdapter = new DaysAdapter(this.activity, dataManger.getCurrentTrip().getDayTripList() , callBackItemClick);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.activity, 1);
        daysPathTrip_RecyclerView.setLayoutManager(mLayoutManager);
        daysPathTrip_RecyclerView.addItemDecoration(new Util(1, Util.dpToPx(10,getResources()), true));
        daysPathTrip_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        daysPathTrip_RecyclerView.setAdapter(daysAdapter);
        daysAdapter.notifyDataSetChanged();
    }

}

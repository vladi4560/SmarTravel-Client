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

import com.vladi.karasove.smartravel.Adapters.PlacesAdapter;
import com.vladi.karasove.smartravel.CallBacks.CallBackListPlaces;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Helpers.Util;
import com.vladi.karasove.smartravel.Objects.Place;
import com.vladi.karasove.smartravel.R;

import java.util.ArrayList;
import java.util.List;

public class PlacesListFragment extends Fragment {

    private AppCompatActivity activity;
    private CallBackListPlaces callBackListPlaces;
    private RecyclerView placesList_RecyclerView;
    private PlacesAdapter placesAdapter;
    private List<Place> placesList;
    private DataManger dataManger;

    public PlacesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_places_list, container, false);
        dataManger = DataManger.getInstance();
        findViews(view);
        return view;
    }


    private void findViews(View view) {
        placesList_RecyclerView = view.findViewById(R.id.placesList_RecyclerView);
        placesList = new ArrayList<>(dataManger.getCurrentDayTrip().getPlacesList());
        placesAdapter = new PlacesAdapter(this.activity, placesList,callBackListPlaces);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.activity, 1);
        placesList_RecyclerView.setLayoutManager(mLayoutManager);
        placesList_RecyclerView.addItemDecoration(new Util(1, Util.dpToPx(10,getResources()), true));
        placesList_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        placesList_RecyclerView.setAdapter(placesAdapter);
    }


    public void setActivity(AppCompatActivity activity) {
        this.activity=activity;
    }

    public void setCallBackListPlaces (CallBackListPlaces callBackListPlaces){
        this.callBackListPlaces=callBackListPlaces;
    }

    public CallBackListPlaces getCallBackListPlaces() {
        return callBackListPlaces;
    }

    public List<Place> getPlacesList() {
        return placesList;
    }
}
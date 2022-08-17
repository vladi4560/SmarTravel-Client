package com.vladi.karasove.smartravel.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vladi.karasove.smartravel.CallBacks.CallBackListPlaces;
import com.vladi.karasove.smartravel.Objects.Place;
import com.vladi.karasove.smartravel.R;

import java.util.List;

public class DayTripFragment extends Fragment implements OnMapReadyCallback {

    private AppCompatActivity activity;
    private PlacesListFragment placesListFragment;
    private GoogleMap mMap;

    public Fragment setActivity(AppCompatActivity activity){
        this.activity=activity;
        return this;
    }

    public DayTripFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_daytrip, container, false);
        findViews();
        return view;
    }

    CallBackListPlaces callBackListPlaces = new CallBackListPlaces() {
        @Override
        public void rowSelected(double longitude, double latitude) {
            LatLng point = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(" " ));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17.0f));
        }
    };

    private void findViews() {
        placesListFragment = new PlacesListFragment();
        placesListFragment.setActivity(this.activity);
        placesListFragment.setCallBackListPlaces(callBackListPlaces);
        getParentFragmentManager().beginTransaction().add(R.id.framePlaces, placesListFragment).commit();

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getParentFragmentManager().beginTransaction().add(R.id.frameMap,supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        List<Place> placeList=placesListFragment.getPlacesList();
        for (Place place:placeList) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(place.getLocation().getLat(), place.getLocation().getLng()))
                    .title(place.getName()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeList.get(0).getLocation().getLat(), placeList.get(0).getLocation().getLng()),14.0f));
    }
}
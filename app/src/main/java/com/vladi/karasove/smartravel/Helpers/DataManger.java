package com.vladi.karasove.smartravel.Helpers;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.internal.LinkedTreeMap;
import com.vladi.karasove.smartravel.CallBacks.CallBackCreateTrip;
import com.vladi.karasove.smartravel.Objects.DayTrip;
import com.vladi.karasove.smartravel.Objects.Trip;
import com.vladi.karasove.smartravel.Objects.User;
import com.vladi.karasove.smartravel.retrofit.RetrofitService;
import com.vladi.karasove.smartravel.serverObjects.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataManger {

    private static DataManger single_Instance_dataManger=null;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount account;

    private CallBackCreateTrip callBackCreateTrip;

    public static final String CLIENT_MANAGER_EMAIL = "androidClient@email.com";
    public static final String CLIENT_MANAGER_DOMAIN = "2022b.maya.gembom";

    public FetchDataFromUrl fetchDataFromUrl;

    private int instanceTripCounter=0;
    private Uri resultUri;
    private Map<String,String> myInstances;
    private Location currentLocation;
    private User currentUser;
    private Trip currentTrip;
    private DayTrip currentDayTrip;
    private RetrofitService retrofitService;

    private List<Trip> upcomingTripList;
    private List<Trip> historyTripList;

    public CallBackCreateTrip getCallBackCreateTrip() {
        return callBackCreateTrip;
    }

    public DataManger setCallBackCreateTrip(CallBackCreateTrip callBackCreateTrip) {
        this.callBackCreateTrip = callBackCreateTrip;
        return this;
    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }

    private DataManger() {
        fetchDataFromUrl = new FetchDataFromUrl();
        retrofitService=new RetrofitService();
        currentLocation = new Location(0.0,0.0);
        currentUser = new User();
        currentTrip = new Trip();
        myInstances =  new LinkedTreeMap<>();
        upcomingTripList =new ArrayList<Trip>();
        historyTripList =new ArrayList<Trip>();
    }

    public static DataManger getInstance(){
        return single_Instance_dataManger;
    }

    public static DataManger initHelper(){
        if(single_Instance_dataManger==null)
            single_Instance_dataManger= new DataManger();
        return single_Instance_dataManger;
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public DayTrip getCurrentDayTrip() {
        return currentDayTrip;
    }

    public DataManger setCurrentDayTrip(DayTrip currentDayTrip) {
        this.currentDayTrip = currentDayTrip;
        return this;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public DataManger setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
        return this;
    }

    public DataManger setGso(GoogleSignInOptions gso) {
        this.gso = gso;
        return this;
    }

    public DataManger setAccount(GoogleSignInAccount account) {
        this.account = account;
        return this;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Map<String, String> getMyInstances() {
        return myInstances;
    }

    public Uri getResultUri() {
        return resultUri;
    }

    public DataManger setResultUri(Uri resultUri) {
        this.resultUri = resultUri;
        return this;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public DataManger setCurrentLocation(double longitude, double latitude) {
        this.currentLocation.setLat(latitude);
        this.currentLocation.setLng(longitude);
        return this;
    }

    public List<Trip> getUpcomingTripList() {
        return upcomingTripList;
    }

    public int getInstanceTripCounter() {
        return instanceTripCounter;
    }

    public DataManger setInstanceTripCounter(int instanceTripCounter) {
        this.instanceTripCounter = instanceTripCounter;
        return this;
    }

    public List<Trip> getHistoryTripList() {
        return historyTripList;
    }

}
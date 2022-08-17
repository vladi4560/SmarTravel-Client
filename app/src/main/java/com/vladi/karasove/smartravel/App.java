package com.vladi.karasove.smartravel;


import android.app.Application;

import com.vladi.karasove.smartravel.Helpers.DataManger;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataManger.initHelper();
    }
}
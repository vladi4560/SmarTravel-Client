package com.vladi.karasove.smartravel.Objects;

import java.util.List;

public class DayTrip {

    private String title;
    private int dayNumber;
    private String date;
    private List<Place> placesList;

    public DayTrip(int dayNumber, String date) {
        this.dayNumber = dayNumber;
        this.date = date;
        this.title = "DAY "+this.dayNumber+"  "+date;
    }

    public String getTitle() {
        return title;
    }

    public DayTrip setTitle() {
        this.title = "DAY "+this.dayNumber+"  "+date;
        return this;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public DayTrip setDate(String date) {
        this.date = date;
        return this;
    }

    public List<Place> getPlacesList() {
        return placesList;
    }
}
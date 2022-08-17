package com.vladi.karasove.smartravel.Objects;

import com.vladi.karasove.smartravel.serverObjects.Location;

public class Place {
    private String name;
    private String category;
    private Location location;

    public Place(String name, String category, Location location) {
        this.name = name;
        this.category = category;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Place setName(String name) {
        this.name = name;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", location=" + location +
                '}';
    }
}

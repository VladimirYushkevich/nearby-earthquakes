package com.yushkevich.earthquakes;

public class Earthquake {
    private final String title;
    private final int distance;

    public Earthquake(String title, int distance) {
        this.title = title;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return title + " || " + distance;
    }
}

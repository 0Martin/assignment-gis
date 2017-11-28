package com.example.martin.mapbox;

public class Location {
    private double x;
    private double y;
    private String name;
    private String amenity;
    private int index;

    public Location(){
    }

    public Location(double x, double y, String name, String amenity, int index) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.amenity = amenity;
        this.index = index;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
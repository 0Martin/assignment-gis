package com.example.demo;

public class Location {
    private Point point;
    private String name;
    private String amenity;
    private int index;

    public Location(){
    	point = new Point();
    }

    public Location(double x, double y, String name, int index) {
        this.point.setX(x);
        this.point.setY(y);
        this.name = name;
        this.index = index;
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
    
    public double getX() {
        return point.getX();
    }

    public void setX(double x) {
        this.point.setX(x);
    }

    public double getY() {
        return point.getY();
    }

    public void setY(double y) {
        this.point.setY(y);
    }
    
    public void setIndex(int index) {
    	this.index = index;
    }
    
    public int getIndex() {
    	return index;
    }
}

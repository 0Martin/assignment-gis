package com.example.martin.mapbox;

import java.util.ArrayList;

public class Way {
    private ArrayList<Point> points;

    public Way(){
        points = new ArrayList<>();
    }

    public Way(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public void setPoints(ArrayList<Point> points) {
        this.points.addAll(points);
    }

}

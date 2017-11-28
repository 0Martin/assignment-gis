package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Way {
	private ArrayList<Point> points;
	
	public Way() {
		points = new ArrayList();
	}
	
	public ArrayList<Point> getPoints(){
		return points;
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	public void addPoints(ArrayList<Point> points) {
		points.addAll(points);
	}
	
}

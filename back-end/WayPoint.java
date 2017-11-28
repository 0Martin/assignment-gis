package com.example.demo;

public class WayPoint {
private Point point;
private int path;
	
	public WayPoint() {
		point = new Point();
	}
	
	public WayPoint(int path, Point point) {
		this.path = path;
		this.point = point;
	}

	public int getPath() {
		return path;
	}
	
	public void setPath(int path) {
		this.path = path;
	}
	
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

}

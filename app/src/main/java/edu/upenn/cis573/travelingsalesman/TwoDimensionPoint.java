package edu.upenn.cis573.travelingsalesman;

import android.graphics.Point;

/**
 * Created by abhishek on 9/27/15.
 */
public class TwoDimensionPoint {
    public static double distance(Point p1, Point p2)
    {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist;
    }
}

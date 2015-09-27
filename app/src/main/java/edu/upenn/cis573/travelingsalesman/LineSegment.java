package edu.upenn.cis573.travelingsalesman;

import android.graphics.Point;

/**
 * Created by abhishek on 9/27/15.
 */
public class LineSegment
{
    private Point startPoint;
    private Point endPoint;

    public LineSegment(Point start, Point end)
    {
        startPoint = start;
        endPoint = end;
    }

    public Point getStartPoint()
    {
        return startPoint;
    }

    public void setStartPoint(Point start)
    {
        startPoint = start;
    }

    public Point getEndPoint()
    {
        return endPoint;
    }

    public void setEndPoint(Point end)
    {
        endPoint = end;
    }

}

package edu.upenn.cis573.travelingsalesman;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by abhishek on 9/27/15.
 */
public class Segments
{
    private ArrayList<LineSegment> lineSegments;

    public Segments()
    {
        lineSegments = new ArrayList<LineSegment>();
    }

    public void addLineSegment(Point start, Point end)
    {
        LineSegment newLineSegment = new LineSegment(start, end);
        lineSegments.add(newLineSegment);
    }

    public void addLineSegment(LineSegment lineSegment)
    {
        lineSegments.add(lineSegment);
    }

    public LineSegment get(int index)
    {
        return lineSegments.get(index);
    }

    public Point getStartPoint(int index)
    {
        LineSegment current = lineSegments.get(index);
        return current.getStartPoint();
    }

    public Point getEndPoint(int index)
    {
        LineSegment current = lineSegments.get(index);
        return current.getEndPoint();
    }

    public double pathLength()
    {
        double myPathLength = 0;
        for(LineSegment lineSegment : lineSegments) {
            Point p1 = lineSegment.getStartPoint();
            Point p2 = lineSegment.getEndPoint();
            double dist = TwoDimensionPoint.distance(p1, p2);
            myPathLength += dist;
        }
        return myPathLength;
    }

    public void removeLineSegment(int index)
    {
        lineSegments.remove(index);
    }

    public void clear()
    {
        lineSegments.clear();
    }
    public int size()
    {
        return lineSegments.size();
    }

    public ArrayList<LineSegment> getLineSegments()
    {
        return lineSegments;
    }

    public void setLineSegments(ArrayList<LineSegment> lines)
    {
        lineSegments = lines;
    }

}

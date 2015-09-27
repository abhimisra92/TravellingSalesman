package edu.upenn.cis573.travelingsalesman;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.widget.Toast;
import java.util.*;


public class GameView extends View {

    protected ArrayList<Point> coords = new ArrayList<>();
    protected Segments segments = new Segments();
    private Point firstPoint;
    protected Point[] mapPoints;
    protected int spinnerNum;
    protected int attempt = 0;
    protected static final Point[] mapPositions;
    protected Stroke stroke = new Stroke();
    // these points are all hardcoded to fit the UPenn campus map on a Nexus 5
    static {
        mapPositions = new Point[13];
        mapPositions[0] = new Point(475, 134);
        mapPositions[1] = new Point(141, 271);
        mapPositions[2] = new Point(272, 518);
        mapPositions[3] = new Point(509, 636);
        mapPositions[4] = new Point(584, 402); // 1324 instead of 584
        mapPositions[5] = new Point(834, 243); // 1452 instead of 834
        mapPositions[6] = new Point(667, 253); // 1667 instead of 667
        mapPositions[7] = new Point(750,  670);
        mapPositions[8] = new Point(1020, 380);
        mapPositions[9] = new Point(870, 250);
        mapPositions[10] = new Point(540, 477);
        mapPositions[11] = new Point(828, 424);
        mapPositions[12] = new Point(998, 66); // 1427 instead of 998
    }

    public GameView(Context context) {
        super(context);
        //init();
    }

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        //init();
    }

    public static double calculatePathDistance(ArrayList<Point> points) {

        double total = 0;
        // get the distance between the intermediate points
        for (int i = 0; i < points.size()-1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            double dist = TwoDimensionPoint.distance(p1, p2);
            total += dist;
        }

        // then need to go back to the beginning
        Point p1 = points.get(points.size()-1);
        Point p2 = points.get(0);
        double dist = TwoDimensionPoint.distance(p1, p2);
        total += dist;

        return total;

    }

    protected void init() {
        //spinnerNum = MainActivity.numLocations;

        setBackgroundResource(R.drawable.campus);

        Log.v("GAME VIEW", "init");

        mapPoints = new Point[spinnerNum];

        /* Chooses spinnerNum points randomly from the set of points specified by the mapPositions array
         * and stores them in the mapPoints array. The set is used to ensure that a previously chosen
         * point is not chosen again.
         */
        Set set = new HashSet();
        Random rn = new Random();
        for (int i = 0; i < spinnerNum; i++) {
            int randomNum = rn.nextInt(mapPositions.length);
            while (set.contains(randomNum)) {
                randomNum = rn.nextInt(mapPositions.length);
            }
            set.add(randomNum);
            mapPoints[i] = mapPositions[randomNum];
        }
    }

    /*This method is called to set the initial value for the spinner item.
     */
    public void setNumLocations(int numLocations)
    {
        Log.v("Game View", "setNumLocations");
        spinnerNum = numLocations;
        init();
        invalidate();
    }

    /*
     * This method is automatically invoked when the View is displayed.
     * It is also called after you call "invalidate" on this object.
     */
    protected void onDraw(Canvas canvas) {

        // draws the stroke
        if (stroke.isValidStroke()) {
            if (coords.size() > 1)
            {
                for (int i = 0; i < coords.size()-1; i++) {
                    Point point1 = coords.get(i);
                    Point point2 = coords.get(i+1);
                    stroke.setColor(Color.YELLOW);
                    stroke.setStrokeWidth(10);
                    canvas.drawLine(point1.x, point1.y, point2.x, point2.y, stroke.getPaint());
                }
            }
        }

        // draws the line segments
        for (int i = 0; i < segments.size(); i++) {
            Point startPoint = segments.getStartPoint(i);
            Point endPoint = segments.getEndPoint(i);
            stroke.setColor(Color.RED);
            stroke.setStrokeWidth(10);
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, stroke.getPaint());
        }

        // draws the points on the map
        stroke.setColor(Color.RED);

        for (int i = 0; i < mapPoints.length; i++) {
            int x = mapPoints[i].x;
            int y = mapPoints[i].y;
            canvas.drawRect(x, y, x+20, y+20, stroke.getPaint());
        }

        // detects whether the segments form a circuit - but there's a bug!
        boolean isCircuit = true;
        HashMap<Point, Integer> connections = new HashMap<Point, Integer>();
        for (LineSegment lineSegment : segments.getLineSegments()) {
            Point p1 = lineSegment.getStartPoint();
            Point p2 = lineSegment.getEndPoint();
            Integer value = connections.get(p1);
            if (value == null)
                value = 0;
            value++;
            connections.put(p1, value);

            value = connections.get(p2);
            if (value == null)
                value = 0;
            value++;
            connections.put(p2, value);
        }

        if (segments.size() == 0) {
            isCircuit = false;
        } else {
            for (int v : connections.values()) {
                if (v != 2) {
                    isCircuit = false;
                    break;
                }
            }
        }


        // see if user has solved the problem
        if ((segments.size() == mapPoints.length) && isCircuit) {
            ArrayList<Point> shortestPath = ShortestPath.shortestPath(mapPoints);
            double shortestPathLength = calculatePathDistance(shortestPath);

            double myPathLength = segments.pathLength();

            Log.v("RESULT", "Shortest path length is " + shortestPathLength + "; my path is " + myPathLength);

            double diff = shortestPathLength - myPathLength;
            if (Math.abs(diff) < 0.01) {
                Toast.makeText(getContext(), "You found the shortest path!", Toast.LENGTH_LONG).show();
                attempt = 0;
            }
            else {
                attempt++;
                // after the 3rd failed attempt, show the solution
                if (attempt >= 3) {
                    // draw the solution
                    for (int i = 0; i < shortestPath.size() - 1; i++) {
                        Point a = shortestPath.get(i);
                        Point b = shortestPath.get(i + 1);
                        stroke.setColor(Color.YELLOW);
                        stroke.setStrokeWidth(10);
                        canvas.drawLine(a.x+10, a.y+10, b.x+10, b.y+10, stroke.getPaint());
                    }
                    Point a = shortestPath.get(shortestPath.size()-1);
                    Point b = shortestPath.get(0);
                    stroke.setColor(Color.YELLOW);
                    stroke.setStrokeWidth(10);
                    canvas.drawLine(a.x+10, a.y+10, b.x+10, b.y+10, stroke.getPaint());

                    Toast.makeText(getContext(), "Nope, sorry! Here's the solution.", Toast.LENGTH_LONG).show();
                }
                else {
                    int offset = (int) (Math.abs(diff) / shortestPathLength * 100);
                    // so that we don't say that the path is 0% too long
                    if (offset == 0) {
                        offset = 1;
                    }
                    Toast.makeText(getContext(), "Nope, not quite! Your path is about " + offset + "% too long.", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (segments.size() == mapPoints.length && !isCircuit) {
            Toast.makeText(getContext(), "That's not a circuit! Select Clear from the menu and start over", Toast.LENGTH_LONG).show();
        }

    }

    /*
     * This method is automatically called when the user touches the screen.
     */
    public boolean onTouchEvent(MotionEvent event) {

        Point p = new Point();
        p.x = ((int)event.getX());
        p.y = ((int)event.getY());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            // only add the segment if the touch point is within 30 of any of the other points
            for (int i = 0; i < mapPoints.length; i++) {
                double dx = p.x - mapPoints[i].x;
                double dy = p.y - mapPoints[i].y;
                double dist = TwoDimensionPoint.distance(p, mapPoints[i]);
                if (dist < 30) {
                    // the "+10" part is a bit of a fudge factor because the point itself is the
                    // upper-left corner of the little red box but we want the center
                    p.x = mapPoints[i].x+10;
                    p.y = mapPoints[i].y+10;
                    //xCoords.add(p.x);
                    //yCoords.add(p.y);
                    coords.add(p);
                    firstPoint = p;
                    stroke.setIsValidStroke(true);
                    break;
                }
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (stroke.isValidStroke()) {
                coords.add(p);
                //xCoords.add(p.x);
                //yCoords.add(p.y);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (stroke.isValidStroke()) {
                coords.clear();
                //xCoords.clear();
                //yCoords.clear();
                // only add the segment if the release point is within 30 of any of the other points
                for (int i = 0; i < mapPoints.length; i++) {
                    double dx = p.x - mapPoints[i].x;
                    double dy = p.y - mapPoints[i].y;
                    double dist = TwoDimensionPoint.distance(p, mapPoints[i]);

                    if (dist < 30) {
                        p.x = mapPoints[i].x + 10;
                        p.y = mapPoints[i].y + 10;

                        if (firstPoint.x != p.x && firstPoint.y != p.y) {
                            segments.addLineSegment(firstPoint, p);
                        }
                        break;
                    }
                }
            }
            stroke.setIsValidStroke(false);
        }
        else {
            return false;
        }


        // forces a redraw of the View
        invalidate();

        return true;
    }

    public void clearSegments() {
        segments.clear();
    }

    public int getSegmentsSize() {
        return segments.size();
    }

    public void removeSegment(int i) {
        segments.removeLineSegment(i);
    }
}

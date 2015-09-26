package edu.upenn.cis573.travelingsalesman;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by abhishek on 9/25/15.
 */
public class Stroke {

    private int color;
    private float width;
    private boolean isValidStroke;
    private Paint paint;

    public Stroke()
    {
        //set the default values of color, width and isValidStroke.
        color = Color.YELLOW;
        width = 10.0f;
        isValidStroke = false;
        paint = new Paint();
    }
    public int getColor() {
        return color;
    }

    public void setColor(int col) {
        color = col;
        paint.setColor(color);
    }

    public float getWidth() {
        return width;
    }

    public void setStrokeWidth(float wid) {
        width = wid;
        paint.setStrokeWidth(width);
    }

    public boolean isValidStroke() {
        return isValidStroke;
    }

    public void setIsValidStroke(boolean isValidStrk) {
        isValidStroke = isValidStrk;
    }

    public Paint getPaint()
    {
        return paint;
    }

}

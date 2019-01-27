package com.example.acid.crimson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class PaintView extends SurfaceView implements SurfaceHolder.Callback{

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public static enum DrawingMode {
        Line, Circle, Rect, Spline, FILL
    }

    private final static String TAG = "SURFACE";

    Path path;
    Thread drawing;
    SurfaceHolder holder;
    int backColor;

    private int size;
    Paint brush;

    ArrayList<Path> objects = new ArrayList<>();
    DrawingMode mode = DrawingMode.Rect;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public PaintView(Context context) {
        super(context);
        init();
    }

    public void setColor(int color){
        brush.setColor(color);
    }

    private void init() {
        holder = getHolder();
        brush = new Paint();
        brush.setStrokeWidth(5);
        brush.setColor(Color.BLACK);
        brush.setStyle(Paint.Style.STROKE);
        backColor = Color.WHITE;

    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    Point pressedDot = new Point();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressedDot.set((int) x, (int) y);
                Path path = new Path();
                path.moveTo(x, y);
                objects.add(path);
                break;
            case MotionEvent.ACTION_MOVE:
                switch (mode) {
                    case Line:
                        path = new Path();
                        path.moveTo(pressedDot.x, pressedDot.y);
                        objects.set(objects.size() - 1, path);
                        objects.get(objects.size() - 1).lineTo(x, y);
                        break;
                    case Circle:
                        path = new Path();
                        path.moveTo(pressedDot.x, pressedDot.y);
                        objects.set(objects.size() - 1, path);
                        objects.get(objects.size() - 1)
                                .addCircle(pressedDot.x, pressedDot.y,
                                        (float) Math.sqrt((pressedDot.x - x) * (pressedDot.x - x) +
                                                (pressedDot.y - y) * (pressedDot.y - y)),
                                        Path.Direction.CW);
                        break;
                    case Rect:
                        path = new Path();
                        path.moveTo(pressedDot.x, pressedDot.y);
                        float x1, y1, x2, y2;
                        if (pressedDot.x > x){
                            x1 = x;
                            y1 = pressedDot.y;
                        }

                        objects.set(objects.size() - 1, path);
                        objects.get(objects.size() - 1).addRect(pressedDot.x, pressedDot.y,
                                x, y, Path.Direction.CCW);
                        break;

                    case Spline:
                        objects.get(objects.size() - 1).lineTo(x, y);
                        objects.get(objects.size() - 1).moveTo(x, y);
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        update();
        return true;
    }

    public void update() {
        Canvas mat = holder.lockCanvas();
        mat.drawColor(backColor);
        for (Path obj : objects) {
            mat.drawPath(obj, brush);
        }
        holder.unlockCanvasAndPost(mat);
    }

    public void setMode(DrawingMode mode) {
        this.mode = mode;
    }

    public void clear(){
        objects = new ArrayList<>();
        update();
    }

    public void redo() {
        if (!objects.isEmpty())
            objects.remove(objects.size() - 1);

        update();
    }

    public void setSize(int size){
        brush.setStrokeWidth(size);
    }

    public void setColor(int r, int g, int b) {
        brush.setColor(Color.rgb(r, g, b));
    }
}
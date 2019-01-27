package com.example.acid.crimson;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DrawThread extends Thread implements View.OnTouchListener {

    private final int REDRAW_TIME    = 10; //частота обновления экрана - 10 мс
    private final int ANIMATION_TIME = 1_500; //анимация - 1,5 сек
    private float x1, y1, x2, y2, r;
    private final SurfaceHolder mSurfaceHolder; //нужен, для получения canvas

    private boolean mRunning; //запущен ли процесс
    private long    mStartTime; //время начала анимации
    private long    mPrevRedrawTime; //предыдущее время перерисовки

    private Paint mPaint;
    private ArgbEvaluator mArgbEvaluator;

    public DrawThread(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        mRunning = false;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mArgbEvaluator = new ArgbEvaluator();
    }

    public void setRunning(boolean running) { //запускает и останавливает процесс
        mRunning = running;
        mPrevRedrawTime = getTime();
    }

    public long getTime() {
        return System.nanoTime() / 1_000_000;
    }

    @Override
    public void run() {
        Canvas canvas;
        mStartTime = getTime();

        while (mRunning) {
            long curTime = getTime();
            long elapsedTime = curTime - mPrevRedrawTime;
            if (elapsedTime < REDRAW_TIME) //проверяет, прошло ли 10 мс
                continue;
//если прошло, перерисовываем картинку
            canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas(); //получаем canvas
                synchronized (mSurfaceHolder) {
                    draw(canvas); //функция рисования
                }
            }
            catch (NullPointerException e) {/*если canvas не доступен*/}
            finally {
                if (canvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(canvas); //освобождаем canvas
            }

            mPrevRedrawTime = curTime;
        }
    }

    private void draw(Canvas canvas) {
        r = (float) Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2 - y1), 2));
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(x1, y1, r, mPaint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX(); y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX(); y2 = event.getY();
                break;
        }
        return false;
    }
}

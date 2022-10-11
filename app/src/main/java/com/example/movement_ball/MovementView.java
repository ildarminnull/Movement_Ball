package com.example.movement_ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static java.lang.Math.abs;

public class MovementView extends SurfaceView implements SurfaceHolder.Callback {

    private int xPos;
    private int yPos;

    public int xBuffer = 0;
    public int yBuffer = 0;

    public static int xA;
    public static int yA;

    private int xVel = 0;
    private int yVel = 0;

    public int width;
    public int height;

    private final int circleRadius;
    private final Paint circlePaint;

    UpdateThread updateThread;

    public MovementView(Context context) {

        super(context);
        getHolder().addCallback(this);

        circleRadius = 50;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);

//        xVel = 0;
//        yVel = 0;
//        xBuffer = 0;
//        yBuffer = 0;

    }
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    public void updatePhysics() {

        if (abs(xBuffer) < abs(xA)) {
            xVel += xA*2;
        }else{
            xVel += xA - xBuffer;
        }
        if (abs(yBuffer) < abs(yA)) {
            yVel += yA*2;
        }else{
            yVel += yA - yBuffer;
        }

        xPos += xVel;
        yPos += yVel;

        if (yPos - circleRadius < 0 || yPos + circleRadius > height) {
            if (yPos - circleRadius < 0) {
                yPos = circleRadius;
            }else{
                yPos = height - circleRadius;
            }
            yVel *= -1;
        }
        if (xPos - circleRadius < 0 || xPos + circleRadius > width) {
            if (xPos - circleRadius < 0) {
                xPos = circleRadius;
            } else {
                xPos = width - circleRadius;
            }
            xVel *= -1;
        }
        xBuffer = xA;
        yBuffer = yA;
    }

    public void surfaceCreated(SurfaceHolder holder) {

        Rect surfaceFrame = holder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();

        xPos = width / 2;
        yPos = height / 2;

        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;

        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
    }
}

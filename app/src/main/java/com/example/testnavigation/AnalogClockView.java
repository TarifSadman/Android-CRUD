package com.example.testnavigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class AnalogClockView extends View {
    private Paint paint;
    private float centerX, centerY;
    private float radius;
    private Calendar calendar;

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        calendar = Calendar.getInstance();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2.0f;
        centerY = h / 2.0f;
        radius = Math.min(centerX, centerY) * 0.9f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(centerX, centerY, radius, paint);

        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8);
        float hourAngle = (hour % 12 + minute / 60.0f) * 30;
        canvas.drawLine(centerX, centerY, centerX + radius * 0.5f * (float) Math.cos(Math.toRadians(hourAngle - 90)),
                centerY + radius * 0.5f * (float) Math.sin(Math.toRadians(hourAngle - 90)), paint);

        paint.setStrokeWidth(6);
        float minuteAngle = (minute + second / 60.0f) * 6;
        canvas.drawLine(centerX, centerY, centerX + radius * 0.7f * (float) Math.cos(Math.toRadians(minuteAngle - 90)),
                centerY + radius * 0.7f * (float) Math.sin(Math.toRadians(minuteAngle - 90)), paint);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        float secondAngle = second * 6;
        canvas.drawLine(centerX, centerY, centerX + radius * 0.8f * (float) Math.cos(Math.toRadians(secondAngle - 90)),
                centerY + radius * 0.8f * (float) Math.sin(Math.toRadians(secondAngle - 90)), paint);

        postInvalidateDelayed(1000);
    }
}

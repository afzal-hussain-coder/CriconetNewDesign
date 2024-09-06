package com.pb.criconet.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CircleTextSpan extends ReplacementSpan {
    private int circleColor;
    private String text;
    private Paint circlePaint;
    private Paint textPaint;
    private int radius;

    public CircleTextSpan(int circleColor, String text, int radius) {
        this.circleColor = circleColor;
        this.text = text;
        this.radius = radius;

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);  // Text color inside the circle
        textPaint.setTextSize(radius);    // Adjust text size
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return 2 * radius;  // Diameter of the circle
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        // Draw the circle
        int centerX = (int) (x + radius);
        int centerY = (bottom + top) / 2;
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Draw the text inside the circle
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float baseline = centerY - (fontMetrics.descent + fontMetrics.ascent) / 2;
        canvas.drawText(this.text, centerX, baseline, textPaint);
    }
}

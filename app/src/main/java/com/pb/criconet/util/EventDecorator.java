package com.pb.criconet.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.pb.criconet.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {
    private final int color;
    private final HashSet<CalendarDay> dates;
    private final String latter;
    Context mContext;

    // Constructor to initialize color and dates
    public EventDecorator(int color, HashSet<CalendarDay> dates,String latter,Context mContext) {
        this.color = color;
        this.dates = dates;
        this.latter=latter;
        this.mContext = mContext;
    }

    // Check if the day should be decorated
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    // Apply the decoration by adding the custom span
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CircleTextSpan(color,latter));
    }

    // Inner class for custom span to draw the circle and text
    private class CircleTextSpan implements LineBackgroundSpan {
        private final int color;
        private final Paint circlePaint;
        private final Paint textPaint;
        private final String text;

        // Constructor to initialize paints
        CircleTextSpan(int color,String text) {
            this.color = color;
            this.text = text;

            // Initialize the paint object for the circle
            circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            circlePaint.setColor(color);

            // Initialize the paint object for the text
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            if(text.equalsIgnoreCase("A")){
                textPaint.setColor(Color.WHITE);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                textPaint.setTypeface(ResourcesCompat.getFont(mContext, R.font.opensans_bold));
            }else{
                textPaint.setColor(mContext.getResources().getColor(R.color.blue_intro_color));
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }

            // Text color inside the circle
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(35f);
           // Adjust size as necessary
        }



        @Override
        public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
            int circleX = (left + right) / 2;
            int circleY = baseline + 38; // Adjust this value to position the circle below the date text
            int radius = 23; // Adjust the radius of the circle as needed

            // Draw the circle
            canvas.drawCircle(circleX, circleY, radius, circlePaint);

            // Draw the text "A" inside the circle
            Rect textBounds = new Rect();
            textPaint.getTextBounds(this.text, 0, 1, textBounds);
            float textY = circleY + textBounds.height() / 2;
            canvas.drawText(this.text, circleX, textY, textPaint);
        }
    }
}

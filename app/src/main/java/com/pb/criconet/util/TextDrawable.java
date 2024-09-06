package com.pb.criconet.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class TextDrawable {
    public static Drawable createCircleWithTextBelow(Context context, String text, int circleColor, int textColor) {
        int radius = 20; // Circle radius in pixels
        int width = radius * 2;
        int height = radius * 2 + 50; // Extra space for text

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Draw circle
        paint.setColor(circleColor);
        canvas.drawCircle(radius, radius, radius, paint);

        // Draw text below the circle
        paint.setColor(textColor);
        paint.setTextSize(10); // Adjust text size
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, radius, radius * 2 + 30, paint); // Position text below circle

        return new BitmapDrawable(context.getResources(), bitmap);
    }
    }


package com.example.myapplication.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.annotation.Nullable;

public class CircularImageView extends AppCompatImageView {
    private float width, height;
    private float radius;
    private Paint paint;
    private Bitmap image;
    public CircularImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getMeasuredWidth()/2.0f;
        height = getMeasuredHeight()/2.0f;
        radius  = (float) (width*1.0)/2;

        canvas.drawCircle((float)width, (float) height, radius, paint);
        super.onDraw(canvas);
    }
}

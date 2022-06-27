package com.jihad.aitools.feature_recognizedigitalink.presentation.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class InkView extends View {

    private Paint brush;
    public static Path path;

    public InkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public InkView(Context context) {
        super(context);
       init();
    }

    private void init(){
        brush = new Paint();
        path = new Path();

        brush.setAntiAlias(true);
        brush.setColor(Color.parseColor("#7075DC"));
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(8f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate();
                return true;

            default: return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, brush);
        invalidate();
    }
}

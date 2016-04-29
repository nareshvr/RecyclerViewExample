package ducere.lechal.pod.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressView extends View {
    private int paintColor = Color.argb(100, 255, 255, 255);
    private Paint drawPaint;
    private int progress = 100;
    final RectF oval = new RectF();

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        getPaint();
    }

    // Setup paint with color and stroke styles
    private void getPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int cx = width / 2;
        int cy = height / 2;
        float radius = (float) (cx * 0.9);

        drawPaint.setStrokeWidth((float) (width * 0.08));

        oval.set(cx - radius, cy - radius, cx + radius, cy + radius);
        canvas.drawArc(oval, -90, (float) (360 * progress / 100), false, drawPaint);
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
        drawPaint.setColor(paintColor);
        invalidate();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
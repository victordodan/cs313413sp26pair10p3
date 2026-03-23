package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int old_color = this.paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(old_color);

        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Paint.Style old_style = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);
        f.getShape().accept(this);
        paint.setStyle(old_style);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()){
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        Paint.Style old_style = paint.getStyle();
        paint.setStyle(Paint.Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(old_style);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {

        int n = s.getPoints().size();
        if (n < 2) return null;

        final float[] pts = new float[4*n];

        for(int k = 0; k < n-1; k++){
            Point p1 = s.getPoints().get(k);
            Point p2 = s.getPoints().get((k+1)%n);
            pts[k*4] = p1.getX();
            pts[k*4+1] = p1.getY();
            pts[k*4+2] = p2.getX();
            pts[k*4+3] = p2.getY();
        }

        canvas.drawLines(pts, paint);
        return null;
    }
}

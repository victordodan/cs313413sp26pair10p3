package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MIN_VALUE;
        int minY = Integer.MIN_VALUE;
        int maxX = Integer.MAX_VALUE;
        int maxY = Integer.MAX_VALUE;

        for(Shape s : g.getShapes()) {
            Location box = s.accept(this);
            Rectangle r = (Rectangle) box.getShape();

            int x1 = box.getX();
            int y1 = box.getY();
            int x2 = x1 + r.getWidth();
            int y2 = y1 + r.getHeight();

            minX = Math.min(minX, x1);
            minY = Math.min(minY, y1);
            maxX = Math.max(maxX, x2);
            maxY = Math.max(maxY, y2);
        }
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }

    @Override
    public Location onLocation(final Location l) {
        Location location_box = l.getShape().accept(this);
        return new Location(
                location_box.getX() + l.getX(),
                location_box.getY() + l.getY(),
                location_box.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = s.getPoints().get(0).getX();
        int minY = s.getPoints().get(0).getX();
        int maxX = minX;
        int maxY = minY;
        for(Point point : s.getPoints()){
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }
}

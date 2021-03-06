package robots.teacher_pack.models;

import java.awt.Graphics2D;
import java.util.List;

public interface CollisionModel
{
	static final double offset = 5;

	boolean isInside(Point p);

	boolean isInsideBounder(Point p);

	List<Point> getBoundingPoints();

	boolean intersectsLine(Point start, Point end);

	void draw(Graphics2D g);
}

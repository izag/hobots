package robots.teacher_pack.models;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;

public interface CollisionModel
{
	static final double offset = 5;

	boolean isInside(Point p);

	boolean isInsideBounder(Point p);

	boolean isIntersectsLine(Line2D.Double line);

	List<Point> getBoundingPoints();

	void draw(Graphics2D g);
}

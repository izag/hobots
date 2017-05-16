package robots.teacher_pack.models;

import java.awt.Graphics2D;
import java.util.List;

public interface CollisionModel
{
	static final double offset = 10;

	boolean isInside(Point p);

	boolean isInsideBounder(Point p);

	List<Point> getBoundingPoints();

	void draw(Graphics2D g);
}

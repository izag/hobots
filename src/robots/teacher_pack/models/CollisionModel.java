package robots.teacher_pack.models;

import java.awt.Graphics2D;

public interface CollisionModel
{
	boolean is_inside(Point p);

	void draw(Graphics2D g);
}

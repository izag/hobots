package robots.teacher_pack.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import robots.teacher_pack.utils.GraphicsUtils;

public class Circle implements CollisionModel
{
	private Point m_center;
	private double m_radius;

	public Circle(Point center, double radius)
	{
		this.m_center = center;
		this.m_radius = radius;
	}

	@Override
	public boolean is_inside(Point p)
	{
		double dx = p.x() - this.m_center.x();
		double dy = p.y() - this.m_center.y();

		return dx * dx + dy * dy <= this.m_radius * this.m_radius;
	}

	@Override
	public void draw(Graphics2D g)
	{
		int x = (int) this.m_center.x();
		int y = (int) this.m_center.y();
    	int d = (int) this.m_radius * 2;

    	AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.BLUE);
        GraphicsUtils.fillOval(g, x, y, d, d);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, x, y, d, d);
	}
}

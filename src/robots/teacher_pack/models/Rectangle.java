package robots.teacher_pack.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

public class Rectangle implements CollisionModel
{
	private Point m_top_left, m_bottom_right;

	private int m_x;
	private int m_y;
	private int m_width;
	private int m_height;

	public Rectangle(Point top_left, Point bottom_right)
	{
		this.m_top_left = new Point(Math.min(top_left.x(), bottom_right.x()), Math.min(top_left.y(), bottom_right.y()));
		this.m_bottom_right = new Point(Math.max(top_left.x(), bottom_right.x()), Math.max(top_left.y(), bottom_right.y()));

		this.m_x = (int) m_top_left.x();
		this.m_y = (int) m_top_left.y();
		this.m_width = (int) (m_bottom_right.x() - m_top_left.x());
		this.m_height = (int) (m_bottom_right.y() - m_top_left.y());
	}

	@Override
	public boolean isInside(Point p)
	{
		return this.m_top_left.x() <= p.x() && this.m_bottom_right.x() >= p.x() && this.m_top_left.y() <= p.y() && this.m_bottom_right.y() >= p.y();
	}

	@Override
	public void draw(Graphics2D g)
	{
		AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
		g.setTransform(t);

		g.setColor(Color.BLUE);
		g.fillRect(this.m_x, this.m_y, this.m_width, this.m_height);

		g.setColor(Color.BLACK);
		g.drawRect(this.m_x, this.m_y, this.m_width, this.m_height);
	}

	@Override
	public boolean isInsideBounder(Point p)
	{
		return this.m_top_left.x() - CollisionModel.offset <= p.x() && this.m_bottom_right.x() + CollisionModel.offset >= p.x() && this.m_top_left.y() - CollisionModel.offset <= p.y() && this.m_bottom_right.y() + CollisionModel.offset >= p.y();
	}

	@Override
	public List<Point> getBoundingPoints()
	{
		// TODO Auto-generated method stub
		return null;
	}
}

package robots.teacher_pack.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
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
		List<Point> bounders = new LinkedList<>();

		bounders.add(new Point(this.m_top_left.x() - CollisionModel.offset * 2, this.m_top_left.y() - CollisionModel.offset * 2));
		bounders.add(new Point(this.m_bottom_right.x() + CollisionModel.offset * 2, this.m_top_left.y() - CollisionModel.offset * 2));
		bounders.add(new Point(this.m_bottom_right.x() + CollisionModel.offset * 2, this.m_bottom_right.y() + CollisionModel.offset * 2));
		bounders.add(new Point(this.m_top_left.x() - CollisionModel.offset * 2, this.m_bottom_right.y() + CollisionModel.offset * 2));

		return bounders;
	}

	@Override
	public boolean intersectsLine(Point start, Point end)
	{
		Rectangle2D boundingRect = new Rectangle2D.Double (
				this.m_top_left.x() - CollisionModel.offset,
				this.m_top_left.y() - CollisionModel.offset,
				this.m_bottom_right.x() - this.m_top_left.x() + CollisionModel.offset * 2,
				this.m_bottom_right.y() - this.m_top_left.y() + CollisionModel.offset * 2
		);

		return boundingRect.intersectsLine(new Line2D.Double(start.x(), start.y(), end.x(), end.y()));
	}
}

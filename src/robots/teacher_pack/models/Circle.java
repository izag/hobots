package robots.teacher_pack.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

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
	public boolean isInside(Point p)
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

	@Override
	public boolean isInsideBounder(Point p)
	{
		double dx = p.x() - this.m_center.x();
		double dy = p.y() - this.m_center.y();
		double r = this.m_radius + CollisionModel.offset;

		return dx * dx + dy * dy <= r * r;
	}

	@Override
	public List<Point> getBoundingPoints()
	{
		List<Point> bounders = new LinkedList<>();

		bounders.add(new Point(this.m_center.x() - this.m_radius - CollisionModel.offset * 2, this.m_center.y() - this.m_radius - CollisionModel.offset * 2));
		bounders.add(new Point(this.m_center.x() + this.m_radius + CollisionModel.offset * 2, this.m_center.y() - this.m_radius - CollisionModel.offset * 2));
		bounders.add(new Point(this.m_center.x() + this.m_radius + CollisionModel.offset * 2, this.m_center.y() + this.m_radius + CollisionModel.offset * 2));
		bounders.add(new Point(this.m_center.x() - this.m_radius - CollisionModel.offset * 2, this.m_center.y() + this.m_radius + CollisionModel.offset * 2));

		return bounders;
	}

	@Override
	public boolean intersectsLine(Point start, Point end)
	{
		Rectangle2D boundingRect = new Rectangle2D.Double (
				this.m_center.x() - this.m_radius - CollisionModel.offset,
				this.m_center.y() - this.m_radius - CollisionModel.offset,
				2 * this.m_radius + CollisionModel.offset * 2,
				2 * this.m_radius + CollisionModel.offset * 2
		);

		return boundingRect.intersectsLine(new Line2D.Double(start.x(), start.y(), end.x(), end.y()));
	}
}

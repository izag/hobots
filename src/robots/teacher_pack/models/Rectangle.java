package robots.teacher_pack.models;

public class Rectangle implements CollisionModel
{
	private Point m_top_left, m_bottom_right;
	private Point m_center;

	public Rectangle(Point top_left, Point bottom_right)
	{
		this.m_top_left = new Point(Math.min(top_left.x(), bottom_right.x()), Math.max(top_left.y(), bottom_right.y()));
		this.m_bottom_right = new Point(Math.max(top_left.x(), bottom_right.x()), Math.min(top_left.y(), bottom_right.y()));
		this.m_center = new Point((top_left.x() + bottom_right.x()) / 2, (top_left.y() + bottom_right.y()) / 2);
	}

	@Override
	public boolean is_inside(Point p)
	{
		return this.m_top_left.x() < p.x() && this.m_bottom_right.x() > p.x() && this.m_top_left.y() > p.y() && this.m_bottom_right.y() < p.y();
	}

	public Point center()
	{
		return m_center;
	}

}

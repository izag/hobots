package robots.teacher_pack.models;

public class Point
{
	private double m_x, m_y;

	public Point(double x, double y)
	{
		m_x = x;
		m_y = y;
	}

	public double x()
	{
		return m_x;
	}

	public double y()
	{
		return m_y;
	}

	public Point add(Point other)
	{
		return new Point(m_x + other.m_x, m_y + other.m_y);
	}

	public Point sub(Point other)
	{
		return new Point(m_x - other.m_x, m_y - other.m_y);
	}

	public Point mul(double alpha)
	{
		return new Point(m_x * alpha, m_y * alpha);
	}

	public Point add(double direction, double distance)
	{
		return new Point(m_x + distance * Math.cos(direction), m_y + distance * Math.sin(direction));
	}

	@Override
	public String toString()
	{
		return "(" + m_x + ", " + m_y + ")";
	}
}

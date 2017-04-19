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

	@Override
	public String toString()
	{
		return "(" + m_x + ", " + m_y + ")";
	}
}

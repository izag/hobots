package robots.teacher_pack.utils;

import java.util.Date;
import java.util.Random;

import robots.teacher_pack.models.Point;

public class Utils
{
	private static Random m_random = new Random(new Date().getTime());

	public static double angleTo(Point from, Point to)
	{
		double diffX = to.x() - from.x();
		double diffY = to.y() - from.y();

		return Math.atan2(diffY, diffX);
	}

	public static double applyLimits(double value, double min, double max)
	{
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	public static double distance(Point p1, Point p2)
	{
		double diffX = p1.x() - p2.x();
		double diffY = p1.y() - p2.y();
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}

	public static double asNormalizedRadians(double angle)
	{
		while (angle <= -Math.PI)
			angle += 2 * Math.PI;

		while (angle > Math.PI)
			angle -= 2 * Math.PI;

		return angle;
	}

	public static double randomDouble(double value)
	{
		return (m_random.nextDouble() - 0.5) * value * 2;
	}

	public static double randomInt(int value)
	{
		return m_random.nextInt(value);
	}
}

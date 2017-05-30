package robots.teacher_pack.models;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Field
{
	private Robot m_currentRobot;

	private List<CollisionModel> m_collisions;
	private List<Robot> m_robots;
	public static final int frequency = 10;

	public Field()
	{
		this.m_collisions = new ArrayList<>();
		this.m_robots = new ArrayList<>();

		this.buildCollisionMap();
	}

	private void buildCollisionMap()
	{
		this.m_collisions.add(new Rectangle(new Point(200, 200), new Point(300, 300)));
		this.m_collisions.add(new Rectangle(new Point(300, 200), new Point(400, 600)));
		this.m_collisions.add(new Rectangle(new Point(200, 500), new Point(300, 600)));
		this.m_collisions.add(new Circle(new Point(500, 500), 50));
	}

	public void addRobot(Robot robot)
	{
		if (this.m_currentRobot == null)
			this.m_currentRobot = robot;

		this.m_robots.add(robot);
	}

	public List<Robot> getRobots()
	{
		return this.m_robots;
	}

	public List<CollisionModel> getCollisions()
	{
		return this.m_collisions;
	}

	public void setTargetPosition(java.awt.Point p)
	{
		for (IRobot robot : this.m_robots)
			robot.setTargetPosition(new Point(p.x, p.y));
	}

	public Robot currentRobot()
	{
		return m_currentRobot;
	}

	public boolean isCollision(Point p)
	{
		for (CollisionModel barrier : this.m_collisions)
		{
			if (barrier.isInside(p))
				return true;
		}

		return false;
	}

	public boolean isOffsetCollision(Point p)
	{
		for (CollisionModel barrier : this.m_collisions)
		{
			if (barrier.isInsideBounder(p))
				return true;
		}

		return false;
	}

	public boolean isOffsetCollision(Line2D.Double line)
	{
		for (CollisionModel barrier : this.m_collisions)
		{
			if (barrier.isIntersectsLine(line))
				return true;
		}

		return false;
	}

	public void start()
	{
		for (Robot robot : this.m_robots)
			robot.start();
	}
}

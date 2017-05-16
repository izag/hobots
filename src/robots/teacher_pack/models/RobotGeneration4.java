package robots.teacher_pack.models;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import robots.teacher_pack.utils.Utils;

public class RobotGeneration4 extends Robot
{
	private int m_randomSteps;
	private Queue<Point> m_path;

	public RobotGeneration4(int id, Field field)
	{
		super(id, field);

		this.m_randomSteps = 0;
		this.m_path = new ConcurrentLinkedQueue<Point>();
	}

	public RobotGeneration4(int id, Field field, Point position, Point target)
	{
		super(id, field);

		this.m_position = position;
		this.m_target = target;
		this.m_randomSteps = 0;
		this.m_path = new ConcurrentLinkedQueue<Point>();
	}

	@Override
	protected double maxVelocity()
	{
		return 1.0;
	}

	@Override
	protected double maxAngularVelocity()
	{
		return 0.05;
	}

	@Override
	public void setTargetPosition(Point p)
	{
		this.m_path.add(p);
	}

	@Override
	public void make_step()
	{
		double distance = Utils.distance(m_target, this.position());

		if (distance < this.maxVelocity() / 2)
		{
			Point nextPoint = this.m_path.poll();

			if (nextPoint == null)
				return;

			this.m_target = nextPoint;
			this.m_stepsCount = 0;

			double R = this.maxVelocity() / this.maxAngularVelocity();

			Point center1 = this.m_position.add(this.m_direction + Math.PI / 2, R);
			Point center2 = this.m_position.add(this.m_direction - Math.PI / 2, R);

			if (new Circle(center1, R).isInside(this.m_target) || new Circle(center2, R).isInside(this.m_target))
				this.m_randomSteps = (int) R;

			return;
		}

		double velocity = this.maxVelocity();

		if (velocity > distance)
			velocity = distance;

		double angleToTarget = Utils.angleTo(this.position(), m_target);

		double angle = angleToTarget - this.direction();

		if (angle < -Math.PI)
			angle += 2 * Math.PI;
		else if (angle > Math.PI)
			angle -= 2 * Math.PI;

		Point old_position = this.position();

		if (this.counter() % 1000 == 999)
			this.m_randomSteps = (int) (this.maxVelocity() / this.maxAngularVelocity());

		if (this.m_randomSteps > 0)
		{
			angle = 0;
			this.m_randomSteps--;
		}

		this.move(velocity, angle);

		Point new_position = this.position();

		while (m_field.isOffsetCollision(new_position))
		{
			this.setPosition(old_position);
			this.move(velocity, this.maxAngularVelocity());
			new_position = this.position();
		}
	}
}

package robots.teacher_pack.models;

import robots.teacher_pack.utils.Utils;

public class RobotGeneration3 extends Robot
{
	private int m_randomSteps;

	public RobotGeneration3(int id, Field field)
	{
		super(id, field);
		this.m_randomSteps = 0;
	}

	public RobotGeneration3(int id, Field field, Point position, Point target)
	{
		super(id, field);

		this.m_position = position;
		this.m_randomSteps = 0;

		this.setTargetPosition(target);
	}

	@Override
	protected double maxVelocity()
	{
		return 5.0;
	}

	@Override
	protected double maxAngularVelocity()
	{
		return 0.05;
	}

	@Override
	public void setTargetPosition(Point p)
	{
		super.setTargetPosition(p);

		double R = this.maxVelocity() / this.maxAngularVelocity();

		Point center1 = this.m_position.add(this.m_direction + Math.PI / 2, R);
		Point center2 = this.m_position.add(this.m_direction - Math.PI / 2, R);

		if (new Circle(center1, R).isInside(p) || new Circle(center2, R).isInside(p))
			this.m_randomSteps = (int) R;
	}

	@Override
	public void makeStep()
	{
		double distance = Utils.distance(m_target, this.position());

		if (distance < this.maxVelocity() / 2)
			return;

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

		while (m_field.isCollision(new_position))
		{
			this.setPosition(old_position);
			this.move(velocity, this.maxAngularVelocity());
			new_position = this.position();
		}
	}
}

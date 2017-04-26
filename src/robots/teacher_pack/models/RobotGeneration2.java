package robots.teacher_pack.models;

import robots.teacher_pack.utils.Utils;

public class RobotGeneration2 extends Robot
{
	private int m_randomSteps;

	public RobotGeneration2(int id, Field field)
	{
		super(id, field);
		this.m_randomSteps = 0;
	}

	public RobotGeneration2(int id, Field field, Point position, Point target)
	{
		super(id, field);

		this.m_position = position;
		this.m_target = target;
		this.m_randomSteps = 0;
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
	public void make_step()
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

		if (this.counter() % 100 == 99)
			this.m_randomSteps = 5;

		if (this.m_randomSteps > 0)
		{
			angle = Utils.randomDouble(this.maxAngularVelocity());
			this.m_randomSteps--;
		}

		this.move(velocity, angle);

		Point new_position = this.position();

		while (m_field.is_collision(new_position))
		{
			this.setPosition(old_position);
			this.move(velocity, Utils.randomDouble(this.maxAngularVelocity()));
			new_position = this.position();
		}
	}
}

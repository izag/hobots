package robots.teacher_pack.models;

import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import robots.teacher_pack.utils.Utils;

public abstract class Robot extends Observable implements IRobot
{
	protected int m_stepsCount;

	protected Point m_position;
	protected volatile double m_direction = 0;
	protected Point m_target;
	protected Field m_field;
	protected int m_id;

	private final Timer m_timer;

	protected Robot(int id, Field field)
	{
		this.m_id = id;
		this.m_field = field;
		this.m_position = new Point(100, 100);
		this.m_target = new Point(150, 100);

    	this.m_timer = new Timer("Robot events generator", true);
	}

	protected abstract double maxVelocity();

	protected abstract double maxAngularVelocity();

	@Override
	public final int id()
	{
		return this.m_id;
	}

	@Override
	public final Point position()
	{
		return this.m_position;
	}

	@Override
	public final double direction()
	{
		return this.m_direction;
	}

    @Override
	public final int counter()
	{
		return this.m_stepsCount;
	}

	@Override
	public void setPosition(Point pos)
	{
		this.m_position = pos;
	}

	@Override
	public void setTargetPosition(Point p)
	{
		this.m_target = p;
		this.m_stepsCount = 0;
	}

	@Override
	public Point target()
	{
		return this.m_target;
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

	    this.move(velocity, angle);
	}

	protected final void move(double velocity, double angularVelocity)
    {
        velocity = Utils.applyLimits(velocity, 0, this.maxVelocity());
        angularVelocity = Utils.applyLimits(angularVelocity, -this.maxAngularVelocity(), this.maxAngularVelocity());

        double newX = m_position.x() + velocity / angularVelocity * (Math.sin(m_direction  + angularVelocity) - Math.sin(m_direction));

        if (!Double.isFinite(newX))
            newX = m_position.x() + velocity * Math.cos(m_direction);

        double newY = m_position.y() - velocity / angularVelocity * (Math.cos(m_direction  + angularVelocity) - Math.cos(m_direction));

        if (!Double.isFinite(newY))
            newY = m_position.y() + velocity * Math.sin(m_direction);

        m_position = new Point(newX, newY);
        m_direction = Utils.asNormalizedRadians(m_direction + angularVelocity);

        this.m_stepsCount++;

        setChanged();
        notifyObservers();
    }

	public final void start()
	{
		this.m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
            	Point old_position = Robot.this.position();

            	Robot.this.makeStep();

                Point new_position = Robot.this.position();

                if (Robot.this.m_field.isCollision(new_position))
                	Robot.this.setPosition(old_position);
            }
        }, 0, Field.frequency);
	}

	@Override
	public void draw(Graphics2D g)
	{
	}
}
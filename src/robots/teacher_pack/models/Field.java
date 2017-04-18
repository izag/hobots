package robots.teacher_pack.models;

import java.util.Timer;
import java.util.TimerTask;

import robots.teacher_pack.utils.Utils;

public class Field
{
	private Robot m_robot;
	private Point m_target;
	private Rectangle m_collision;
	public static final int frequency = 10;

	private final Timer m_timer;

	public Field()
	{
		m_robot = new Robot();
		m_target = new Point(150, 100);
		m_collision = new Rectangle(new Point(200, 200), new Point(300, 300));

		m_timer = new Timer("Model events generator", true);
	}

	public void setTargetPosition(java.awt.Point p)
    {
		m_target = new Point(p.x, p.y);
		update();
    }

	public void start()
	{
		m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                update();
            }
        }, 0, Field.frequency);
	}

	public void update()
    {
        double distance = Utils.distance(m_target, m_robot.position());

        if (distance < 0.5)
            return;

        double velocity = Robot.maxVelocity;

        if (velocity > distance)
        	velocity = distance;

        double angleToTarget = Utils.angleTo(m_robot.position(), m_target);

        double angle = angleToTarget - m_robot.direction();

        if (angle < -Math.PI)
        	angle += 2 * Math.PI;
        else if (angle > Math.PI)
        	angle -= 2 * Math.PI;

        Point old_position = m_robot.position();

        m_robot.move(velocity, angle, Field.frequency);

        Point new_position = m_robot.position();

        while (m_collision.is_inside(new_position))
        {
        	m_robot.setPosition(old_position);
        	m_robot.move(velocity, angle, Field.frequency);
        	new_position = m_robot.position();
        }
    }

	public Robot robot()
	{
		return m_robot;
	}

	public Point target()
	{
		return m_target;
	}

	public Rectangle collision()
	{
		return m_collision;
	}
}

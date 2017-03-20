package robots.teacher_pack.models;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import robots.teacher_pack.utils.Utils;

public class Field
{
	private Robot m_robot;
	private Point m_target;
	private Rectangle m_collision;

	private final Timer m_timer;

	private volatile FieldChangeListener[] m_activeListeners;
	private final ArrayList<FieldChangeListener> m_listeners;

	public Field()
	{
		m_robot = new Robot();
		m_target = new Point(150, 100);
		m_collision = new Rectangle(new Point(200, 200), new Point(300, 300));

		m_timer = new Timer("Model events generator", true);
		m_listeners = new ArrayList<FieldChangeListener>();
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
        }, 0, 40);
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

        m_robot.move(velocity, angle, 10);

        Point new_position = m_robot.position();

        while(m_collision.is_inside(new_position))
        {
        	m_robot.setPosition(old_position);
        	m_robot.move(velocity, angle, 10);
        	new_position = m_robot.position();
        }

        FieldChangeListener [] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new FieldChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }

        for (FieldChangeListener listener : activeListeners)
            listener.onFieldChanged();
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

	public void registerListener(FieldChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(FieldChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }
}

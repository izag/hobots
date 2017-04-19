package robots.teacher_pack.models;

import java.util.Timer;
import java.util.TimerTask;

public class Field
{
	private Robot m_robot;

	private Rectangle m_collision;
	public static final int frequency = 10;

	private final Timer m_timer;

	public Field()
	{
		m_robot = new Robot(this);
		m_collision = new Rectangle(new Point(200, 200), new Point(300, 300));
		m_timer = new Timer("Model events generator", true);
	}

	public void setTargetPosition(java.awt.Point p)
    {
		m_robot.setTargetPosition(new Point(p.x, p.y));
    }

	public void start()
	{
		m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                m_robot.make_step();
            }
        }, 0, Field.frequency);
	}


	public Robot currentRobot()
	{
		return m_robot;
	}

	public Rectangle collision()
	{
		return m_collision;
	}
}

package robots.teacher_pack.models;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Field
{
	private Robot m_robot;

	private List<CollisionModel> m_collisions;
	public static final int frequency = 10;

	private final Timer m_timer;

	public Field()
	{
		this.m_robot = new Robot(this);
		this.m_collisions = new ArrayList<>();
		this.m_timer = new Timer("Model events generator", true);

		this.buildCollisionMap();
	}

	private void buildCollisionMap()
	{
		this.m_collisions.add(new Rectangle(new Point(200, 200), new Point(300, 300)));
		this.m_collisions.add(new Rectangle(new Point(300, 200), new Point(400, 600)));
		this.m_collisions.add(new Rectangle(new Point(200, 500), new Point(300, 600)));
		this.m_collisions.add(new Circle(new Point(500, 500), 50));
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
            	Point old_position = m_robot.position();

            	m_robot.make_step();

                Point new_position = m_robot.position();

                if (Field.this.is_collision(new_position))
                	m_robot.setPosition(old_position);
            }
        }, 0, Field.frequency);
	}


	public Robot currentRobot()
	{
		return m_robot;
	}

	public boolean is_collision(Point p)
	{
		for (CollisionModel barrier : this.m_collisions)
		{
			if (barrier.is_inside(p))
				return true;
		}

		return false;
	}


    public void drawCollisionMap(Graphics2D g)
    {
    	for (CollisionModel barrier : this.m_collisions)
    		barrier.draw(g);
    }
}

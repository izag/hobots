package robots.teacher_pack.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Date;
import java.util.Observable;
import java.util.Random;

import robots.teacher_pack.utils.GraphicsUtils;
import robots.teacher_pack.utils.Utils;

public class Robot extends Observable
{
	private Point m_position;
    private volatile double m_direction = 0;
    private Point m_target;
	private final Random m_random;
	private Field m_field;
	private int m_magicCounter;
	private int m_randomSteps;

    static final double maxVelocity = 5.0;
    static final double maxAngularVelocity = 0.05;

    public Robot(Field field)
    {
    	m_position = new Point(100, 100);
    	m_target = new Point(150, 100);
    	m_random = new Random(new Date().getTime());
    	m_field = field;
    	m_magicCounter = 0;
    	m_randomSteps = 0;
    }

    public Point position()
    {
    	return m_position;
    }

    public double direction()
    {
    	return m_direction;
    }

    public void setPosition(Point pos)
    {
    	m_position = pos;
    }

	public void setTargetPosition(Point p)
    {
		m_target = p;
    }

	public Point target()
	{
		return m_target;
	}

	public int counter()
	{
		return m_magicCounter;
	}

	public void make_step()
    {
        double distance = Utils.distance(m_target, this.position());

        if (distance < Robot.maxVelocity / 2)
            return;

        double velocity = Robot.maxVelocity;

        if (velocity > distance)
        	velocity = distance;

        double angleToTarget = Utils.angleTo(this.position(), m_target);

        double angle = angleToTarget - this.direction();

        if (angle < -Math.PI)
        	angle += 2 * Math.PI;
        else if (angle > Math.PI)
        	angle -= 2 * Math.PI;

        Point old_position = this.position();

        if (this.m_magicCounter % 100 == 99)
        	this.m_randomSteps = 5;

        if (this.m_randomSteps > 0)
        {
        	angle = this.randomAngle();
        	this.m_randomSteps--;
        }

        this.move(velocity, angle);

        Point new_position = this.position();

        while (m_field.is_collision(new_position))
        {
        	this.setPosition(old_position);
        	this.move(velocity, maxAngularVelocity);
        	new_position = this.position();
        }

        this.m_magicCounter++;
    }

    private void move(double velocity, double angularVelocity)
    {
//    	Logger.debug("AVelocity1: " + angularVelocity);

        velocity = Utils.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = Utils.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

//        Logger.error("AVelocity2: " + angularVelocity);

        double newX = m_position.x() + velocity / angularVelocity * (Math.sin(m_direction  + angularVelocity) - Math.sin(m_direction));

        if (!Double.isFinite(newX))
            newX = m_position.x() + velocity * Math.cos(m_direction);

        double newY = m_position.y() - velocity / angularVelocity * (Math.cos(m_direction  + angularVelocity) - Math.cos(m_direction));

        if (!Double.isFinite(newY))
            newY = m_position.y() + velocity * Math.sin(m_direction);

        m_position = new Point(newX, newY);
        m_direction = Utils.asNormalizedRadians(m_direction + angularVelocity);

        setChanged();
        notifyObservers();
    }

    private double randomAngle()
    {
    	return (this.m_random.nextDouble() - 0.5) * Robot.maxAngularVelocity * 2;
    }

    public void draw(Graphics2D g)
    {
        int robotCenterX = (int) this.m_position.x();
        int robotCenterY = (int) this.m_position.y();
        AffineTransform t = AffineTransform.getRotateInstance(this.m_direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        GraphicsUtils.fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        GraphicsUtils.fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    public void drawTarget(Graphics2D g)
    {
    	int x = (int) this.m_target.x();
    	int y = (int) this.m_target.y();

    	AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        GraphicsUtils.fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, x, y, 5, 5);
    }
}

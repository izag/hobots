package robots.teacher_pack.models;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Observable;

import robots.teacher_pack.log.Logger;
import robots.teacher_pack.utils.GraphicsUtils;
import robots.teacher_pack.utils.Utils;

public class Robot extends Observable
{
	private Point m_position;
    private volatile double m_direction = 0;

    static final double maxVelocity = 0.1;
    static final double maxAngularVelocity = 0.1;

    public Robot()
    {
    	m_position = new Point(100, 100);
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

    void move(double velocity, double angularVelocity, double duration)
    {
    	Logger.debug("AVelocity1: " + angularVelocity);

        velocity = Utils.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = Utils.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        Logger.error("AVelocity2: " + angularVelocity);

        double newX = m_position.x() + velocity / angularVelocity * (Math.sin(m_direction  + angularVelocity * duration) - Math.sin(m_direction));

        if (!Double.isFinite(newX))
            newX = m_position.x() + velocity * duration * Math.cos(m_direction);

        double newY = m_position.y() - velocity / angularVelocity * (Math.cos(m_direction  + angularVelocity * duration) - Math.cos(m_direction));

        if (!Double.isFinite(newY))
            newY = m_position.y() + velocity * duration * Math.sin(m_direction);

        m_position = new Point(newX, newY);
        m_direction = Utils.asNormalizedRadians(m_direction + angularVelocity * duration);

        setChanged();
        notifyObservers();
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
}

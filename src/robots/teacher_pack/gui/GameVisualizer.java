package robots.teacher_pack.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import robots.teacher_pack.models.CollisionModel;
import robots.teacher_pack.models.Field;
import robots.teacher_pack.models.IRobot;
import robots.teacher_pack.utils.GraphicsUtils;

public class GameVisualizer extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final Field m_field;

	private final Timer m_timer;

	public GameVisualizer(Field field)
	{
		m_field = field;

		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				m_field.setTargetPosition(e.getPoint());
			}
		});

		setDoubleBuffered(true);

		m_timer = new Timer("View updater", true);

		m_timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				onRedrawEvent();
			}
		}, 0, 50);
	}

	private void onRedrawEvent()
	{
		EventQueue.invokeLater(this::repaint);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		for (IRobot robot : m_field.getRobots())
			this.drawRobot(g2d, robot);

		for (CollisionModel barrier : m_field.getCollisions())
			barrier.draw(g2d);

		g2d.dispose();
	}

	public void drawRobot(Graphics2D g, IRobot robot)
	{
		int robotCenterX = (int) robot.position().x();
		int robotCenterY = (int) robot.position().y();

		Color color = new Color(robot.id() * 173 % 256, robot.id() * 341 % 256, robot.id() * 1023 % 256);

		AffineTransform t = AffineTransform.getRotateInstance(robot.direction(), robotCenterX, robotCenterY);
		g.setTransform(t);
		g.setColor(color);
		GraphicsUtils.fillOval(g, robotCenterX, robotCenterY, 30, 10);
		g.setColor(Color.BLACK);
		GraphicsUtils.drawOval(g, robotCenterX, robotCenterY, 30, 10);
		g.setColor(Color.WHITE);
		GraphicsUtils.fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
		g.setColor(Color.BLACK);
		GraphicsUtils.drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);

		this.drawTarget(g, (int) robot.target().x(), (int) robot.target().y(), color);

		robot.draw(g);
	}

	public void drawTarget(Graphics2D g, int x, int y, Color color)
	{
		AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
		g.setTransform(t);
		g.setColor(color);
		GraphicsUtils.fillOval(g, x, y, 5, 5);
		g.setColor(Color.BLACK);
		GraphicsUtils.drawOval(g, x, y, 5, 5);
	}
}

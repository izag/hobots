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

import robots.teacher_pack.models.Field;
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

        Graphics2D g2d = (Graphics2D)g;

        m_field.currentRobot().draw(g2d);
        m_field.currentRobot().drawTarget(g2d);
        this.drawSquare(g2d, (int) m_field.collision().center().x(), (int) m_field.collision().center().y());

        g2d.dispose();
    }

    private void drawSquare(Graphics2D g, int x, int y)
    {
    	AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.BLUE);
        GraphicsUtils.fillSquare(g, x, y, 100);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawSquare(g, x, y, 100);
    }
}

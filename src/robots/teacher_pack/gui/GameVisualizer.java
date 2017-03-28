package robots.teacher_pack.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import robots.teacher_pack.models.Field;
import robots.teacher_pack.models.FieldChangeListener;
import robots.teacher_pack.utils.GraphicsUtils;

public class GameVisualizer extends JPanel implements FieldChangeListener
{
	private static final long serialVersionUID = 1L;
	private final Field m_field;

    public GameVisualizer(Field field)
    {
    	m_field = field;

    	m_field.registerListener(this);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
            	m_field.setTargetPosition(e.getPoint());
            }
        });

        setDoubleBuffered(true);
    }

    @Override
	public void onFieldChanged()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        m_field.robot().draw(g2d);
        drawTarget(g2d, (int) m_field.target().x(), (int) m_field.target().y());
        drawSquare(g2d, (int) m_field.collision().center().x(), (int) m_field.collision().center().x());

        g2d.dispose();
    }

    private void drawTarget(Graphics2D g, int x, int y)
    {
    	AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        GraphicsUtils.fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, x, y, 5, 5);
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

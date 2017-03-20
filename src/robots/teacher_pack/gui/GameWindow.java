package robots.teacher_pack.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import robots.teacher_pack.models.Field;
import robots.teacher_pack.utils.StateUtils;

public class GameWindow extends JInternalFrame implements ComponentWithState
{
	private static final long serialVersionUID = 1L;
	private final GameVisualizer m_visualizer;
    public GameWindow(Field field)
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer(field);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        addComponentListener(new ComponentListener()
        {
            @Override
			public void componentShown(ComponentEvent e)
            {
            	EventQueue.invokeLater(() -> { m_visualizer.repaint(); repaint(); });
			}

            @Override
			public void componentResized(ComponentEvent e)
            {
            	EventQueue.invokeLater(() -> { m_visualizer.repaint(); repaint(); });
			}

			@Override
			public void componentHidden(ComponentEvent arg0)
			{
				EventQueue.invokeLater(() -> { m_visualizer.repaint(); repaint(); });
			}

			@Override
			public void componentMoved(ComponentEvent arg0)
			{
				EventQueue.invokeLater(() -> { m_visualizer.repaint(); repaint(); });
			}
        });
    }

    @Override
	public void saveComponentState(Map<String, String> state)
    {
        StateUtils.saveComponentState(this, state);
    }

    @Override
	public void loadComponentState(Map<String, String> state)
    {
        StateUtils.loadComponentState(this, state);
    }
}

package robots.teacher_pack.gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import robots.teacher_pack.models.Field;

public class GameWindow extends JInternalFrame
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

//		addComponentListener(new ComponentListener()
//		{
//			@Override
//			public void componentShown(ComponentEvent e)
//			{
//				EventQueue.invokeLater(() ->
//				{
//					m_visualizer.repaint();
//					repaint();
//				});
//			}
//
//			@Override
//			public void componentResized(ComponentEvent e)
//			{
//				EventQueue.invokeLater(() ->
//				{
//					m_visualizer.repaint();
//					repaint();
//				});
//			}
//
//			@Override
//			public void componentHidden(ComponentEvent arg0)
//			{
//				EventQueue.invokeLater(() ->
//				{
//					m_visualizer.repaint();
//					repaint();
//				});
//			}
//
//			@Override
//			public void componentMoved(ComponentEvent arg0)
//			{
//				EventQueue.invokeLater(() ->
//				{
//					m_visualizer.repaint();
//					repaint();
//				});
//			}
//		});
	}
}

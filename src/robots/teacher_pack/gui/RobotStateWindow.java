package robots.teacher_pack.gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import robots.teacher_pack.models.Robot;

public class RobotStateWindow extends JInternalFrame implements Observer
{
	private static final long serialVersionUID = 1L;
	private JTextArea m_content;
	private Robot m_robot;

	public RobotStateWindow(String caption, Robot observable)
    {
        super(caption, true, true, true, true);

        m_content = new JTextArea("");
        m_content.setSize(300, 300);
        m_content.setPreferredSize(m_content.getSize());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_content, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        m_robot = observable;
    }

	@Override
	public void update(Observable obj, Object arg)
	{
		if (obj != m_robot)
			return;

		m_content.setText("Position: " + m_robot.position() + "\nDirection: " + m_robot.direction() + "\nCounter: " + m_robot.counter());
	}
}

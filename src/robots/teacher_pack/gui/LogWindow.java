package robots.teacher_pack.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import robots.teacher_pack.log.LogChangeListener;
import robots.teacher_pack.log.LogEntry;
import robots.teacher_pack.log.LogLevel;
import robots.teacher_pack.log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener
{
	private static final long serialVersionUID = 1L;
	private LogWindowSource m_logSource;
    private JTextArea m_logContent;
    private LogLevel m_level;

    public LogWindow(LogWindowSource logSource, String caption, LogLevel level)
    {
        super(caption, true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new JTextArea("");
        m_logContent.setSize(200, 500);

        m_level = level;

        JScrollPane panel = new JScrollPane(m_logContent);
        this.setPreferredSize(m_logContent.getSize());
        this.getContentPane().add(panel, BorderLayout.CENTER);

        this.pack();
        this.updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
        	if (entry.getLevel().level() < m_level.level())
        		continue;

            content.append(entry.getMessage()).append("\n");
        }

        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}

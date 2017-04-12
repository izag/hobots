package robots.teacher_pack.log;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class LogWindowSource
{
    private ArrayBlockingQueue<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength)
    {
    	this.m_messages = new ArrayBlockingQueue<>(iQueueLength);
    	this.m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(this.m_listeners)
        {
            this.m_listeners.add(listener);
            this.m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(this.m_listeners)
        {
        	this.m_listeners.remove(listener);
        	this.m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);

        boolean success = false;

        while (!success)
        {
	        try
	        {
	        	this.m_messages.add(entry);
	        	success = true;
	        }
	        catch (IllegalStateException ex)
	        {
	        	this.m_messages.poll();
	        }
        }

        LogChangeListener [] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (this.m_listeners)
            {
                if (this.m_activeListeners == null)
                {
                    activeListeners = this.m_listeners.toArray(new LogChangeListener [0]);
                    this.m_activeListeners = activeListeners;
                }
            }
        }

        for (LogChangeListener listener : activeListeners)
            listener.onLogChanged();
    }

    public int size()
    {
        return this.m_messages.size();
    }

    public Iterable<LogEntry> all()
    {
        return this.m_messages;
    }
}

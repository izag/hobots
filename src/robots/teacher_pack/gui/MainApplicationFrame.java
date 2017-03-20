package robots.teacher_pack.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import robots.teacher_pack.log.LogLevel;
import robots.teacher_pack.log.Logger;
import robots.teacher_pack.models.Field;
import robots.teacher_pack.utils.MapUtils;
import robots.teacher_pack.utils.SubMapView;

public class MainApplicationFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final JDesktopPane m_desktopPane = new JDesktopPane();
	private final LogWindow m_logDebug;
	private final LogWindow m_logError;
	private final GameWindow m_gameWindow;

	public MainApplicationFrame(Field field)
	{
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

		setContentPane(m_desktopPane);

		m_logDebug = addWindow(createLogDebug(LogLevel.Debug));
		m_gameWindow = addWindow(new GameWindow(field), 400, 400);
		m_logError = addWindow(createLogError(LogLevel.Error));

		// bug: only 1 game window repaints
		// addWindow(new GameWindow(field), 400, 400);

		RobotStateWindow stateWindow = addWindow(new RobotStateWindow("Robot-1", field.robot()));

		field.robot().addObserver(stateWindow);

		setJMenuBar(generateMenuBar());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				onWindowClose();
			}
		});

		loadSavedState();
	}

	protected void onWindowClose()
	{
		if (JOptionPane.showConfirmDialog(this, "Выйти из программы?", "Подтвердите действие", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;

		saveState();
		setVisible(false);
		dispose();
		System.exit(0);
	}

	private final static File SETTINGS_FILE_NAME = new File(System.getProperty("user.home"), "robots_settings.cfg");

	private void saveState()
	{
		HashMap<String, String> state = new HashMap<>();

//		m_logError.saveComponentState(new SubMapView(state, "logError"));
//		m_gameWindow.saveComponentState(new SubMapView(state, "gameWindow"));
		MapUtils.saveMap(SETTINGS_FILE_NAME, state);
	}

	private void loadSavedState()
	{
		Map<String, String> state = MapUtils.loadMap(SETTINGS_FILE_NAME);
		m_logDebug.loadComponentState(new SubMapView(state, "logDebug"));
		m_logError.loadComponentState(new SubMapView(state, "logError"));
		m_gameWindow.loadComponentState(new SubMapView(state, "gameWindow"));
	}

	protected LogWindow createLogError(LogLevel level)
	{
		LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), "Протокол работы", level);
		logWindow.setLocation(10, 10);
		logWindow.setSize(300, 800);
		setMinimumSize(logWindow.getSize());
		logWindow.pack();
		return logWindow;
	}

	protected LogWindow createLogDebug(LogLevel level)
	{
		LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), "Положение робота", level);
		logWindow.setLocation(200, 10);
		logWindow.setSize(400, 800);
		setMinimumSize(logWindow.getSize());
		logWindow.pack();
		return logWindow;
	}

	private <TWindow extends JInternalFrame> TWindow addWindow(TWindow frame)
	{
		m_desktopPane.add(frame);
		frame.setVisible(true);
		return frame;
	}

	private <TWindow extends JInternalFrame> TWindow addWindow(TWindow frame, int width, int height)
	{
		frame.setSize(width, height);
		frame.setPreferredSize(frame.getSize());
		return addWindow(frame);
	}

	private JMenuItem createMenuItem(String text, int mnemonic, KeyStroke accelerator, ActionListener action)
	{
		JMenuItem menuItem = new JMenuItem(text, mnemonic);

		if (accelerator != null)
			menuItem.setAccelerator(accelerator);

		menuItem.addActionListener(action);
		return menuItem;
	}

	private JMenuItem generateLookAndFeelMenu()
	{
		JMenu lookAndFeelMenu = new JMenu("Режим отображения");
		lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
		lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

		lookAndFeelMenu.add(createMenuItem("Системная схема", KeyEvent.VK_S, null,
				(event) -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName())));

		lookAndFeelMenu.add(createMenuItem("Универсальная схема", KeyEvent.VK_U, null,
				(event) -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())));

		lookAndFeelMenu.add(createMenuItem("Универсальная схема (Nimbus)", KeyEvent.VK_N, null,
						(event) -> setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel")));

		return lookAndFeelMenu;
	}

	private JMenuItem generateTestMenu()
	{
		JMenu testMenu = new JMenu("Тесты");
		testMenu.setMnemonic(KeyEvent.VK_T);
		testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

		testMenu.add(createMenuItem("Сообщение в лог", KeyEvent.VK_S, null,
				(event) -> Logger.debug("Новая строка")));

		return testMenu;
	}

	private JMenuBar generateMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(generateLookAndFeelMenu());
		menuBar.add(generateTestMenu());
		menuBar.add(createMenuItem("Выход", KeyEvent.VK_X, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK),
				(event) -> Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
						new WindowEvent(this,WindowEvent.WINDOW_CLOSING))));

		return menuBar;
	}

	private void setLookAndFeel(String className)
	{
		try
		{
			UIManager.setLookAndFeel(className);
			SwingUtilities.updateComponentTreeUI(this);
			invalidate();
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			// just ignore
		}
	}
}

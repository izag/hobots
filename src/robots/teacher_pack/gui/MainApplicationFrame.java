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
import robots.teacher_pack.utils.StateUtils;
import robots.teacher_pack.utils.SubMapView;

public class MainApplicationFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final JDesktopPane m_desktopPane = new JDesktopPane();
	private final Field m_field;

	public MainApplicationFrame(Field field)
	{
		int inset = 100;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		System.out.println(screenSize);

		m_desktopPane.setPreferredSize(new Dimension(screenSize.width - inset, screenSize.height - inset));

		setContentPane(m_desktopPane);

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

		this.m_field = field;

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

		int counter = 0;

		for (JInternalFrame frame : this.m_desktopPane.getAllFrames())
		{
			counter++;

			StateUtils.saveComponentState(frame, new SubMapView(state, frame.getClass().getSimpleName() + "-" + counter));
			System.out.println(frame.getClass().getSimpleName() + "-" + counter);
		}

		state.keySet().stream().forEach(System.out::println);

		MapUtils.saveMap(SETTINGS_FILE_NAME, state);
	}

	private void loadSavedState()
	{
		Map<String, String> state = MapUtils.loadMap(SETTINGS_FILE_NAME);

		if (state.isEmpty())
		{
			loadDefaultState();
			return;
		}

		for (String prefix : MapUtils.getPrefixes(state))
		{
			System.out.println(prefix);
			String type = prefix.split("-")[0];

			JInternalFrame frame = null;

			switch (type)
			{
				case "GameWindow":
					frame = addWindow(new GameWindow(this.m_field));
					break;
				case "LogWindow":
					frame = addWindow(createLogWindow(LogLevel.Debug));
					break;
				case "RobotStateWindow":
				{
					if (this.m_field.currentRobot() == null)
						break;

					RobotStateWindow stateWindow = addWindow(new RobotStateWindow("Robot-1", this.m_field.currentRobot()));
					this.m_field.currentRobot().addObserver(stateWindow);
					frame = stateWindow;
					break;
				}
				default:
					continue;
			}

			if (frame == null)
				continue;

			StateUtils.loadComponentState(frame, new SubMapView(state, prefix));
		}
	}

	private void loadDefaultState()
	{
		addWindow(new GameWindow(this.m_field), 400, 400);
		addWindow(createLogWindow(LogLevel.Error));

		RobotStateWindow stateWindow = addWindow(new RobotStateWindow("Robot-1", this.m_field.currentRobot()));
		this.m_field.currentRobot().addObserver(stateWindow);
	}

	protected LogWindow createLogWindow(LogLevel level)
	{
		LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), "Положение робота", level);
		logWindow.setLocation(200, 10);
		setMinimumSize(logWindow.getSize());
		logWindow.pack();
		Logger.debug("Протокол работает");
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

	private JMenuItem generateAddWindowMenu()
	{
		JMenu addMenu = new JMenu("Добавить окно");
		addMenu.setMnemonic(KeyEvent.VK_W);
		addMenu.getAccessibleContext().setAccessibleDescription("Добавление новых окон");

		addMenu.add(createMenuItem("Протокол ошибок", KeyEvent.VK_E, null,
				(event) -> addWindow(createLogWindow(LogLevel.Error))));

		addMenu.add(createMenuItem("Протокол отладки", KeyEvent.VK_D, null,
				(event) -> addWindow(createLogWindow(LogLevel.Debug))));

		addMenu.add(createMenuItem("Игровое окно", KeyEvent.VK_G, null,
				(event) -> addWindow(new GameWindow(this.m_field), 400, 400)));

		addMenu.add(createMenuItem("Состояние робота", KeyEvent.VK_S, null, (event) -> {
			if (this.m_field.currentRobot() == null)
				return;

			RobotStateWindow stateWindow = addWindow(new RobotStateWindow("Robot-1", this.m_field.currentRobot()));
			this.m_field.currentRobot().addObserver(stateWindow);
		}));

		return addMenu;
	}

	private JMenuBar generateMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(generateLookAndFeelMenu());
		menuBar.add(generateTestMenu());
		menuBar.add(generateAddWindowMenu());
		menuBar.add(createMenuItem("Выход", KeyEvent.VK_X, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK),
			(event) -> Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
				new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));

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

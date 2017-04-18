package robots.teacher_pack.gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import robots.teacher_pack.models.Field;

public class RobotsProgram
{
    public static void main(String[] args)
    {
		try
		{
//			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Field field = new Field();

		SwingUtilities.invokeLater(() -> {
		    MainApplicationFrame frame = new MainApplicationFrame(field);
		    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		    frame.pack();
		    frame.setVisible(true);
		});

		field.start();
	}
}

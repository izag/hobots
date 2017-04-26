package robots.teacher_pack.gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import robots.teacher_pack.models.Field;
import robots.teacher_pack.models.Point;
import robots.teacher_pack.models.RobotGeneration2;
import robots.teacher_pack.models.RobotGeneration3;
import robots.teacher_pack.models.RobotGeneration4;
import robots.teacher_pack.utils.Utils;

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

		Point commonPosition = new Point(Utils.randomInt(1000), Utils.randomInt(1000));
		Point commonTarget = new Point(Utils.randomInt(1000), Utils.randomInt(1000));

		while (field.is_collision(commonTarget))
			commonTarget = new Point(Utils.randomInt(1000), Utils.randomInt(1000));

		while (field.is_collision(commonPosition))
			commonPosition = new Point(Utils.randomInt(1000), Utils.randomInt(1000));

		field.addRobot(new RobotGeneration2(1, field, commonPosition, commonTarget));
		field.addRobot(new RobotGeneration3(2, field, commonPosition, commonTarget));
		field.addRobot(new RobotGeneration4(3, field, commonPosition, commonTarget));

		SwingUtilities.invokeLater(() -> {
		    MainApplicationFrame frame = new MainApplicationFrame(field);
		    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		    frame.pack();
		    frame.setVisible(true);
		});

		field.start();
	}
}

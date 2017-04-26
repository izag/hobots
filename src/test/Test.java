package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test
{

	public static void main(String[] args)
	{
		new Test();
	}

	public Test()
	{
		EventQueue.invokeLater(() ->
		{
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
			{
				ex.printStackTrace();
			}

			CustomDesktopPane pane = new CustomDesktopPane();
			JInternalFrame inFrm = new JInternalFrame("Ontop", true, true, true, true);
			inFrm.setSize(200, 200);
			inFrm.setLocation(150, 150);
			inFrm.setVisible(true);
			inFrm.getContentPane().add(new Visualizer());
			pane.add(inFrm);

			JInternalFrame inFrm2 = new JInternalFrame("Ontop2", true, true, true, true);
			inFrm2.setSize(200, 200);
			inFrm2.setLocation(150, 150);
			inFrm2.setVisible(true);
			inFrm2.getContentPane().add(new Visualizer());
			pane.add(inFrm2);

			JFrame frame = new JFrame("Testing");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(pane);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

	public class Visualizer extends JPanel
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Rectangle rectangle = new Rectangle(100, 100, 30, 30);
			Graphics2D gg = (Graphics2D) g;

			gg.setColor(Color.BLUE);
			gg.fill(rectangle);

			gg.setColor(Color.BLACK);
			gg.draw(rectangle);

			gg.dispose();
		}
	}

	public class CustomDesktopPane extends JDesktopPane
	{
		private static final long serialVersionUID = 1L;

		public CustomDesktopPane()
		{
			setFont(UIManager.getFont("Label.font").deriveFont(24f));
		}

		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(800, 800);
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			String text = "All your base are belong to us";
			FontMetrics fm = g2d.getFontMetrics();
			int x = (getWidth() - fm.stringWidth(text)) / 2;
			int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
			g2d.setColor(Color.WHITE);
			g2d.drawString(text, x, y);
			g2d.dispose();
		}

	}

}
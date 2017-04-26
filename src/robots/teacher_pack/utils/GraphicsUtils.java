package robots.teacher_pack.utils;

import java.awt.Graphics;

public class GraphicsUtils
{
	public static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
	{
		g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
	}

	public static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
	{
		g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
	}

	public static void fillSquare(Graphics g, int centerX, int centerY, int diam)
	{
		g.fillRect(centerX - diam / 2, centerY - diam / 2, diam, diam);
	}

	public static void drawSquare(Graphics g, int centerX, int centerY, int diam)
	{
		g.drawRect(centerX - diam / 2, centerY - diam / 2, diam, diam);
	}
}

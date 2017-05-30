package robots.teacher_pack.models;

import java.awt.Graphics2D;

public interface IRobot
{
	int id();

	Point position();

	double direction();

	void setPosition(Point pos);

	void setTargetPosition(Point p);

	Point target();

	void makeStep();

	int counter();

	void draw(Graphics2D g);
}
package robots.teacher_pack.models;

public interface IRobot
{
	int id();

	Point position();

	double direction();

	void setPosition(Point pos);

	void setTargetPosition(Point p);

	Point target();

	void make_step();

	int counter();
}
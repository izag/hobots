package robots.teacher_pack.models;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import robots.teacher_pack.utils.Utils;

public class RobotGeneration4 extends Robot
{
	private int m_randomSteps;
	private Queue<Point> m_path;
	private List<Vertex> m_vertices;

	public RobotGeneration4(int id, Field field)
	{
		super(id, field);

		this.m_randomSteps = 0;
		this.m_path = new ConcurrentLinkedQueue<>();
	}

	public RobotGeneration4(int id, Field field, Point position, Point target)
	{
		super(id, field);

		this.m_path = new ConcurrentLinkedQueue<>();
		this.m_randomSteps = 0;
		this.m_position = position;

		this.setTargetPosition(target);
	}

	@Override
	protected double maxVelocity()
	{
		return 1.0;
	}

	@Override
	protected double maxAngularVelocity()
	{
		return 0.05;
	}

	@Override
	public void setTargetPosition(Point p)
	{
		this.buildPath(p);
		this.m_target = this.m_path.poll();

		if (this.m_target == null)
			this.m_target = this.m_position;
	}

	@Override
	public void makeStep()
	{
		double distance = Utils.distance(m_target, this.position());

		if (distance < this.maxVelocity() / 2)
		{
			Point nextPoint = this.m_path.poll();

			if (nextPoint == null)
				return;

			this.m_target = nextPoint;
			this.m_stepsCount = 0;

			double R = this.maxVelocity() / this.maxAngularVelocity();

			Point center1 = this.m_position.add(this.m_direction + Math.PI / 2, R);
			Point center2 = this.m_position.add(this.m_direction - Math.PI / 2, R);

			if (new Circle(center1, R).isInside(this.m_target) || new Circle(center2, R).isInside(this.m_target))
				this.m_randomSteps = (int) R;

			return;
		}

		double velocity = this.maxVelocity();

		if (velocity > distance)
			velocity = distance;

		double angleToTarget = Utils.angleTo(this.position(), m_target);

		double angle = angleToTarget - this.direction();

		if (angle < -Math.PI)
			angle += 2 * Math.PI;
		else if (angle > Math.PI)
			angle -= 2 * Math.PI;

		Point old_position = this.position();

		if (this.counter() % 1000 == 999)
			this.m_randomSteps = (int) (this.maxVelocity() / this.maxAngularVelocity());

		if (this.m_randomSteps > 0)
		{
			angle = 0;
			this.m_randomSteps--;
		}

		this.move(velocity, angle);

		Point new_position = this.position();

		while (m_field.isOffsetCollision(new_position))
		{
			this.setPosition(old_position);
			this.move(velocity, this.maxAngularVelocity());
			new_position = this.position();
		}
	}

	@Override
	public void draw(Graphics2D g)
	{
//		if (this.m_vertices == null)
//			return;
//
//		for (Vertex v : this.m_vertices)
//		{
//			int x = (int) v.point().x();
//			int y = (int) v.point().y();
//			AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
//			g.setTransform(t);
//			g.setColor(Color.GRAY);
//			GraphicsUtils.fillOval(g, x, y, 5, 5);
//			g.setColor(Color.BLACK);
//			GraphicsUtils.drawOval(g, x, y, 5, 5);
//		}
//
//		for (int i = 0; i < this.m_vertices.size(); i++)
//		{
//			for (int j = i + 1; j < this.m_vertices.size(); j++)
//			{
//				Point start = this.m_vertices.get(i).point();
//				Point end = this.m_vertices.get(j).point();
//
//				if (this.m_field.isOffsetCollision(start, end))
//					continue;
//
//				g.draw(new Line2D.Double(start.x(), start.y(), end.x(), end.y()));
//			}
//		}

		Point prev = this.m_target;
		for (Point p : this.m_path)
		{
			g.draw(new Line2D.Double(prev.x(), prev.y(), p.x(), p.y()));
			prev = p;
		}
	}

	private void buildPath(Point dest)
	{
		this.m_vertices = this.m_field.getCollisions().stream()
			.flatMap((barrier) -> barrier.getBoundingPoints().stream())
			.filter((point) -> !this.m_field.isOffsetCollision(point))
			.map((point) -> new Vertex(point, Double.MAX_VALUE))
			.collect(Collectors.toList());

		Vertex source = new Vertex(this.m_position, 0.0);
		Vertex destination = new Vertex(dest, Double.MAX_VALUE);

		this.m_vertices.add(source);
		this.m_vertices.add(destination);

		// dijkstra
		PriorityQueue<Vertex> q = new PriorityQueue<>();

		for (Vertex v : this.m_vertices)
			q.add(v);

		while (!q.isEmpty())
		{
			Vertex u = q.poll();

			if (u == destination)
				break;

			for (Object o : q.toArray())
			{
				Vertex v = (Vertex) o;

				Point start = u.point();
				Point end = v.point();

				if (this.m_field.isOffsetCollision(start, end))
					continue;

				double alt = u.dist + Utils.distance(v.point(), u.point());

				if (alt >= v.dist)
					continue;

				q.remove(v);
				v.dist = alt;
				v.prev = u;
				q.add(v);
			}
		}

		LinkedList<Point> path = new LinkedList<>();

		Vertex v = destination;
		while (v.prev != null)
		{
			path.add(v.point());
			v = v.prev;
		}

		Collections.reverse(path);

		this.m_path.clear();
		path.stream().forEach((point) -> this.m_path.add(point));
	}
}

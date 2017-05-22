package robots.teacher_pack.models;

public class Vertex implements Comparable<Vertex>
{
	private Point point;
	public double dist;
	public Vertex prev;

	public Vertex(Point point, double dist)
	{
		this.point = point;
		this.dist = dist;
	}

	public Point point()
	{
		return this.point;
	}

	@Override
	public int compareTo(Vertex o)
	{
		return Double.compare(this.dist, o.dist);
	}

	@Override
	public String toString()
	{
		return this.point + " - " + this.dist;
	}

//	@Override
//	public boolean equals(Object o)
//	{
//		if (!(o instanceof Vertex))
//			return false;
//
//		return this.number == ((Vertex)o).number;
//	}
//
//	@Override
//	public int hashCode()
//	{
//		return this.number;
//	}
}

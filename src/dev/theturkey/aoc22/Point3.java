package dev.theturkey.aoc22;

public record Point3(int x, int y, int z)
{
	public static Point3 newInstance(String[] values)
	{
		return new Point3(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
	}

	public Point3 sub(Point3 p)
	{
		return new Point3(this.x - p.x, this.y - p.y, this.z - p.z);
	}

	public Point3 add(Point3 p)
	{
		return new Point3(this.x + p.x, this.y + p.y, this.z + p.z);
	}

	public Point3 invert()
	{
		return new Point3(-this.x, -this.y, -this.z);
	}


	@Override
	public String toString()
	{
		return "Point3{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
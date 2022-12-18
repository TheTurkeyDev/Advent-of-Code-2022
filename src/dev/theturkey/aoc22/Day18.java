package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day18 extends AOCPuzzle
{
	public Day18()
	{
		super("18");
	}

	private static final List<Point3> DIRS = new ArrayList<>(Arrays.asList(
			new Point3(1, 0, 0),
			new Point3(-1, 0, 0),
			new Point3(0, 1, 0),
			new Point3(0, -1, 0),
			new Point3(0, 0, 1),
			new Point3(0, 0, -1)
	));

	@Override
	void solve(List<String> input)
	{
		List<Point3> cubes = new ArrayList<>();
		for(String s : input)
			cubes.add(Point3.newInstance(s.split(",")));

		lap(getSurfaceArea(cubes));

		int minX = cubes.stream().min(Comparator.comparingInt(Point3::x)).get().x();
		int maxX = cubes.stream().max(Comparator.comparingInt(Point3::x)).get().x();
		int minY = cubes.stream().min(Comparator.comparingInt(Point3::y)).get().y();
		int maxY = cubes.stream().max(Comparator.comparingInt(Point3::y)).get().y();
		int minZ = cubes.stream().min(Comparator.comparingInt(Point3::z)).get().z();
		int maxZ = cubes.stream().max(Comparator.comparingInt(Point3::z)).get().z();

		for(int x = minX; x <= maxX; x++)
		{
			for(int y = minY; y <= maxY; y++)
			{
				for(int z = minZ; z <= maxZ; z++)
				{
					Point3 p = new Point3(x, y, z);
					if(cubes.contains(p))
						continue;

					boolean pocket = true;
					for(Point3 dir : DIRS)
					{
						Point3 offset = new Point3(p.x(), p.y(), p.z());
						do
						{
							offset = offset.add(dir);
						}
						while(!cubes.contains(offset) && offset.x() >= minX && offset.x() <= maxX && offset.z() >= minZ && offset.z() <= maxZ && offset.y() >= minY && offset.y() <= maxY);

						if(!cubes.contains(offset))
						{
							pocket = false;
							break;
						}
					}

					if(pocket)
					{
						List<Point3> queue = new ArrayList<>();
						queue.add(p);
						while(queue.size() > 0)
						{
							Point3 point = queue.remove(0);
							if(!cubes.contains(point))
								cubes.add(point);
							for(Point3 dir : DIRS)
							{
								Point3 offset = point.add(dir);
								if(!cubes.contains(offset) && !queue.contains(offset))
									queue.add(offset);
							}
						}
					}
				}
			}
		}
		lap(getSurfaceArea(cubes));
	}

	private int getSurfaceArea(List<Point3> cubes)
	{
		int sides = 0;
		for(Point3 cube : cubes)
		{
			for(Point3 dir : DIRS)
			{
				Point3 offset = cube.add(dir);
				if(!cubes.contains(offset))
					sides++;
			}
		}

		return sides;
	}
}
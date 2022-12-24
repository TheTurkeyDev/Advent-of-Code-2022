package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day24 extends AOCPuzzle
{
	public Day24()
	{
		super("24");
	}

	private static int WIDTH = 0;
	private static int HEIGHT = 0;

	@Override
	void solve(List<String> input)
	{
		Point start = new Point(0, 1);
		Point end = new Point(36, 100);
		//		Point end = new Point(5, 6);
		Point goal = end;
		List<Blizzard> blizzards = new ArrayList<>();
		WIDTH = input.get(0).length() - 2;
		HEIGHT = input.size() - 2;
		for(int row = 0; row < input.size(); row++)
		{
			String s = input.get(row);
			for(int col = 0; col < s.length(); col++)
			{
				Point p = new Point(row, col);
				char c = s.charAt(col);
				if(c == '#')
					blizzards.add(new Blizzard(p, Direction.NORTH, true));
				else if(c == '^')
					blizzards.add(new Blizzard(p, Direction.NORTH, false));
				else if(c == '>')
					blizzards.add(new Blizzard(p, Direction.EAST, false));
				else if(c == 'v')
					blizzards.add(new Blizzard(p, Direction.SOUTH, false));
				else if(c == '<')
					blizzards.add(new Blizzard(p, Direction.WEST, false));
			}
		}

		Set<Point> points = new HashSet<>();
		points.add(start);
		int traverseCount = 0;
		int steps = 0;

		while(traverseCount < 3)
		{
			blizzards = tickBlizzards(blizzards);
			Set<Point> newPoints = new HashSet<>();
			for(Point p : points)
			{
				Point east = Direction.EAST.move(p);
				if(!blizzardsContains(blizzards, east))
					newPoints.add(east);

				Point south = Direction.SOUTH.move(p);
				if(south.row() < input.size() && !blizzardsContains(blizzards, south))
					newPoints.add(south);

				if(!blizzardsContains(blizzards, p))
					newPoints.add(p);

				Point north = Direction.NORTH.move(p);
				if(north.row() >= 0 && !blizzardsContains(blizzards, north))
					newPoints.add(north);

				Point west = Direction.WEST.move(p);
				if(!blizzardsContains(blizzards, west))
					newPoints.add(west);
			}

			boolean atEnd = false;
			for(Point p : newPoints)
			{
				if(p.equals(goal))
				{
					atEnd = true;
					break;
				}
			}

			if(atEnd)
			{
				traverseCount++;
				if(traverseCount == 1)
					lap(steps + 1);
				goal = traverseCount % 2 == 1 ? start : end;
				newPoints.clear();
				newPoints.add(traverseCount % 2 == 1 ? end : start);
			}

			points = newPoints;
			steps++;
		}

		lap(steps);
	}

	private boolean blizzardsContains(List<Blizzard> blizzards, Point p)
	{
		for(Blizzard b : blizzards)
			if(b.p.equals(p))
				return true;
		return false;
	}

	private List<Blizzard> tickBlizzards(List<Blizzard> blizzards)
	{
		List<Blizzard> toReturn = new ArrayList<>();
		for(Blizzard b : blizzards)
		{
			if(b.wall)
			{
				toReturn.add(new Blizzard(b.p, b.direction, true));
				continue;
			}
			Point newPoint = b.direction.move(b.p);
			if(newPoint.col() > WIDTH)
				newPoint = new Point(newPoint.row(), 1);
			if(newPoint.col() <= 0)
				newPoint = new Point(newPoint.row(), WIDTH);
			if(newPoint.row() > HEIGHT)
				newPoint = new Point(1, newPoint.col());
			if(newPoint.row() <= 0)
				newPoint = new Point(HEIGHT, newPoint.col());
			toReturn.add(new Blizzard(newPoint, b.direction, false));
		}
		return toReturn;
	}

	private static class Blizzard
	{
		public Point p;
		public Direction direction;
		public boolean wall;

		public Blizzard(Point p, Direction direction, boolean wall)
		{
			this.p = p;
			this.direction = direction;
			this.wall = wall;
		}
	}

	private enum Direction
	{
		NORTH(new Point(-1, 0)),
		SOUTH(new Point(1, 0)),
		EAST(new Point(0, 1)),
		WEST(new Point(0, -1));

		public final Point change;

		Direction(Point change)
		{
			this.change = change;
		}

		public Point move(Point p)
		{
			return new Point(p.row() + change.row(), p.col() + change.col());
		}
	}
}
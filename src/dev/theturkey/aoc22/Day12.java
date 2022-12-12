package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12 extends AOCPuzzle
{
	public Day12()
	{
		super("12");
	}

	public static Map<Point, Integer> grid = new HashMap<>();
	public static int WIDTH;
	public static int HEIGHT;

	@Override
	void solve(List<String> input)
	{
		HEIGHT = input.size();
		WIDTH = input.get(0).length();
		Point start = null;
		Point end = null;
		for(int row = 0; row < input.size(); row++)
		{
			String s = input.get(row);
			for(int col = 0; col < s.length(); col++)
			{
				Point p = new Point(row, col);
				char c = s.charAt(col);
				if(c == 'S')
				{
					start = p;
					c = 'a';
				}
				if(c == 'E')
				{
					end = p;
					c = 'z';
				}
				grid.put(p, c - 'a');
			}
		}

		if(start == null || end == null)
		{
			System.out.println("You dun messed up!");
			return;
		}
		floodFill(start, end, true);
		floodFill(start, end, false);
	}

	public void floodFill(Point start, Point end, boolean part1)
	{
		Map<Point, Integer> shortestPath = new HashMap<>();
		shortestPath.put(start, 0);
		List<Point> queue = new ArrayList<>();
		queue.add(start);
		while(queue.size() > 0)
		{
			Point p = queue.remove(0);

			if(p.row() != 0)
				checkPoint(p, new Point(p.row() - 1, p.col()), shortestPath, queue, part1);
			if(p.row() != HEIGHT - 1)
				checkPoint(p, new Point(p.row() + 1, p.col()), shortestPath, queue, part1);
			if(p.col() != 0)
				checkPoint(p, new Point(p.row(), p.col() - 1), shortestPath, queue, part1);
			if(p.col() != WIDTH - 1)
				checkPoint(p, new Point(p.row(), p.col() + 1), shortestPath, queue, part1);
		}
		lap(shortestPath.get(end));
	}

	public void checkPoint(Point p, Point dir, Map<Point, Integer> shortestPath, List<Point> queue, boolean part1)
	{
		int gridHeight = grid.get(dir);
		if(gridHeight - grid.get(p) <= 1)
		{
			int pathLen = shortestPath.get(p) + 1;
			if(shortestPath.getOrDefault(dir, Integer.MAX_VALUE) > pathLen)
			{
				queue.add(dir);
				shortestPath.put(dir, !part1 && gridHeight == 0 ? 0 : pathLen);
			}
		}
	}
}

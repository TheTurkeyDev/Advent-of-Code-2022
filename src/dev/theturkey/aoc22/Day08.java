package dev.theturkey.aoc22;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day08 extends AOCPuzzle
{
	public Day08()
	{
		super("8");
	}

	private int width = 0;
	private int height = 0;

	@Override
	void solve(List<String> input)
	{
		width = input.get(0).length();
		height = input.size();
		Map<Point, Character> grid = new HashMap<>();
		Set<Point> visible = new HashSet<>();
		for(int row = 0; row < height; row++)
		{
			String s = input.get(row);
			for(int col = 0; col < width; col++)
				grid.put(new Point(row, col), s.charAt(col));
		}

		long largest = 0;
		for(Point p : grid.keySet())
		{
			ViewData viewData = canSeeEdge(grid, p, grid.get(p));
			if(viewData.visible)
				visible.add(p);

			long prod = 1;
			for(int i : viewData.viewDist)
				prod *= i;
			if(prod > largest)
				largest = prod;
		}

		lap(visible.size());
		lap(largest);
	}

	private ViewData canSeeEdge(Map<Point, Character> grid, Point p, char pointChar)
	{
		int[] viewDist = new int[4];
		boolean[] canSee = new boolean[4];
		Arrays.fill(canSee, true);
		for(int i = p.col() - 1; i >= 0; i--)
		{
			if(grid.get(new Point(p.row(), i)) >= pointChar)
			{
				canSee[0] = false;
				viewDist[0] = p.col() - i;
				break;
			}
		}

		if(canSee[0])
			viewDist[0] = p.col();

		for(int i = p.col() + 1; i < width; i++)
		{
			if(grid.get(new Point(p.row(), i)) >= pointChar)
			{
				canSee[1] = false;
				viewDist[1] = i - p.col();
				break;
			}
		}

		if(canSee[1])
			viewDist[1] = width - p.col() - 1;

		for(int i = p.row() - 1; i >= 0; i--)
		{
			if(grid.get(new Point(i, p.col())) >= pointChar)
			{
				canSee[2] = false;
				viewDist[2] = p.row() - i;
				break;
			}
		}

		if(canSee[2])
			viewDist[2] = p.row();

		for(int i = p.row() + 1; i < height; i++)
		{
			if(grid.get(new Point(i, p.col())) >= pointChar)
			{
				canSee[3] = false;
				viewDist[3] = i - p.row();
				break;
			}
		}

		if(canSee[3])
			viewDist[3] = height - p.row() - 1;

		boolean see = false;
		for(boolean b : canSee) see = see || b;

		return new ViewData(viewDist, see);
	}

	public record ViewData(int[] viewDist, boolean visible)
	{

	}
}

package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day15 extends AOCPuzzle
{
	public Day15()
	{
		super("15");
	}

	@Override
	void solve(List<String> input)
	{
		List<InputLine> inputLines = new ArrayList<>();

		for(String s : input)
		{
			int c1 = s.indexOf(",");
			int sx = Integer.parseInt(s.substring(12, c1));
			int colon = s.indexOf(":");
			int sy = Integer.parseInt(s.substring(c1 + 4, colon));
			int c2 = s.indexOf(",", colon);
			int bx = Integer.parseInt(s.substring(colon + 25, s.indexOf(",", c2)));
			int by = Integer.parseInt(s.substring(c2 + 4));
			inputLines.add(new InputLine(sx, sy, bx, by));
		}

		int lineToFind = 2000000;
		Set<Integer> beaconsToExclude = new HashSet<>();
		Set<Integer> invalidPoints = new HashSet<>();
		for(InputLine il : inputLines)
		{
			if(il.by == lineToFind)
				beaconsToExclude.add(il.bx);

			int remaining = getRemainingOnLine(il, lineToFind);

			if(remaining < 0)
				continue;

			for(int i = 0; i <= remaining; i++)
			{
				invalidPoints.add(il.sx + i);
				invalidPoints.add(il.sx - i);
			}
		}

		invalidPoints.removeAll(beaconsToExclude);

		lap(invalidPoints.size());

		int max = 4000000;

		for(int i = 0; i < max; i++)
		{
			int x = 0;
			while(x <= max)
			{
				int newX = x;
				for(InputLine il : inputLines)
				{
					newX = skipOverLine(il, x, i);
					if(x != newX)
						break;
				}

				if(newX == x)
				{
					lap(((long)x * max) + i);
					i = max + 1;
					break;
				}
				x = newX;
			}
		}
	}


	private int getRemainingOnLine(InputLine il, int y)
	{
		int dist = Math.abs(il.sx - il.bx) + Math.abs(il.sy - il.by);
		return dist - il.sy > y ? (il.sy - y) : (y - il.sy);
	}
	private int skipOverLine(InputLine il, int x, int y)
	{
		int remaining = getRemainingOnLine(il, y);

		if(x >= il.sx - remaining && x <= il.sx + remaining)
			return il.sx + remaining + 1;
		return x;
	}

	private record InputLine(int sx, int sy, int bx, int by)
	{

	}
}

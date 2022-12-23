package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day23 extends AOCPuzzle
{
	public Day23()
	{
		super("23");
	}

	@Override
	void solve(List<String> input)
	{
		Map<Point, Elf> elves = new HashMap<>();
		for(int row = 0; row < input.size(); row++)
		{
			String s = input.get(row);
			for(int col = 0; col < s.length(); col++)
			{
				if(s.charAt(col) == '#')
				{
					Point p = new Point(row, col);
					elves.put(p, new Elf(p));
				}
			}
		}

		boolean moved = true;
		int round = 1;
		while(moved)
		{
			List<Point> badLoc = new ArrayList<>();
			Map<Point, Elf> toMove = new HashMap<>();
			for(Elf e : elves.values())
			{
				Point p = e.getChoicePos(elves);
				if(!badLoc.contains(p))
				{
					if(toMove.containsKey(p))
					{
						Elf removed = toMove.remove(p);
						toMove.put(removed.pos, removed);
						toMove.put(e.pos, e);
						badLoc.add(p);
					}
					else
					{
						toMove.put(p, e);
					}
				}
				else
				{
					toMove.put(e.pos, e);
				}
			}
			elves = toMove;
			moved = false;
			for(Point p : elves.keySet())
			{
				Elf e = elves.get(p);
				if(!e.pos.equals(p))
					moved = true;
				e.pos = p;
			}

			//Part 2
			if(!moved)
				lap(round);

			//Part 1
			if(round == 10)
				lap(getEmpty(elves));
			round++;
		}
	}

	private int getEmpty(Map<Point, Elf> elves)
	{
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for(Point p : elves.keySet())
		{
			if(p.row() > maxY)
				maxY = p.row();
			if(p.row() < minY)
				minY = p.row();
			if(p.col() > maxX)
				maxX = p.col();
			if(p.col() < minX)
				minX = p.col();
		}

		int empty = 0;
		for(int row = minY; row <= maxY; row++)
		{
			for(int col = minX; col <= maxX; col++)
			{
				Point p = new Point(row, col);
				boolean contains = elves.containsKey(p);
				if(!contains)
					empty++;
			}
		}

		return empty;
	}

	public static class Elf
	{
		public Point pos;
		public List<Direction> choices = new ArrayList<>();

		public Elf(Point pos)
		{
			this.pos = pos;
			choices.add(Direction.NORTH);
			choices.add(Direction.SOUTH);
			choices.add(Direction.WEST);
			choices.add(Direction.EAST);
		}

		public Point getChoicePos(Map<Point, Elf> elves)
		{
			boolean skip = true;
			for(int row = -1; row < 2; row++)
			{
				for(int col = -1; col < 2; col++)
				{
					if(row == 0 && col == 0)
						continue;
					if(elves.containsKey(new Point(pos.row() + row, pos.col() + col)))
					{
						skip = false;
						break;
					}
				}
				if(!skip)
					break;
			}

			if(skip)
			{
				choices.add(choices.remove(0));
				return pos;
			}

			for(int i = 0; i < choices.size(); i++)
			{
				Direction d = choices.get(i);
				switch(d)
				{
					case NORTH ->
					{
						Point north = d.move(pos);
						if(elves.containsKey(new Point(north.row(), north.col() + 1)))
							break;
						if(elves.containsKey(north))
							break;
						if(elves.containsKey(new Point(north.row(), north.col() - 1)))
							break;
						choices.add(choices.remove(0));
						return north;
					}
					case SOUTH ->
					{
						Point south = d.move(pos);
						if(elves.containsKey(new Point(south.row(), south.col() + 1)))
							break;
						if(elves.containsKey(south))
							break;
						if(elves.containsKey(new Point(south.row(), south.col() - 1)))
							break;
						choices.add(choices.remove(0));
						return south;
					}
					case WEST ->
					{
						Point west = d.move(pos);
						if(elves.containsKey(new Point(west.row() - 1, west.col())))
							break;
						if(elves.containsKey(west))
							break;
						if(elves.containsKey(new Point(west.row() + 1, west.col())))
							break;
						choices.add(choices.remove(0));
						return west;
					}
					case EAST ->
					{
						Point east = d.move(pos);
						if(elves.containsKey(new Point(east.row() - 1, east.col())))
							break;
						if(elves.containsKey(east))
							break;
						if(elves.containsKey(new Point(east.row() + 1, east.col())))
							break;
						choices.add(choices.remove(0));
						return east;
					}
				}
			}
			choices.add(choices.remove(0));
			return pos;
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
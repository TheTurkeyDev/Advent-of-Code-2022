package dev.theturkey.aoc22;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 extends AOCPuzzle
{
	private static final int BOX_SIZE = 50;

	public Day22()
	{
		super("22");
	}

	@Override
	void solve(List<String> input)
	{
		lap(part1(input));
		lap(part2(input));
	}

	private long part1(List<String> input)
	{
		char[] instructions = input.get(input.size() - 1).toCharArray();

		Point pos = new Point(0, 0);
		Direction dir = Direction.RIGHT;

		String l1 = input.get(0);
		while(l1.charAt(pos.col()) == ' ')
			pos = new Point(pos.row(), pos.col() + 1);

		boolean number = true;
		StringBuilder currIns = new StringBuilder();
		for(char let : instructions)
		{
			boolean isNum = let >= '0' && let <= '9';
			if(isNum && number)
			{
				currIns.append(let);
			}
			else if(!isNum && number)
			{
				//Done
				number = false;
				int steps = Integer.parseInt(currIns.toString());
				for(int j = 0; j < steps; j++)
				{
					Point movedTo = dir.move(pos);
					if(movedTo.row() < 0 || movedTo.row() >= input.size() || movedTo.col() < 0 || movedTo.col() >= input.get(movedTo.row()).length())
					{
						pos = wrap(dir, pos, movedTo, input);
						continue;
					}

					char c = input.get(movedTo.row()).charAt(movedTo.col());
					if(c == ' ')
						pos = wrap(dir, pos, movedTo, input);
					else if(c != '#')
						pos = movedTo;

				}
				currIns = new StringBuilder();
				currIns.append(let);
			}
			else if(!isNum)
			{
				currIns.append(let);
			}
			else if(isNum)
			{
				//Done
				number = true;
				String step = currIns.toString();
				for(char c : step.toCharArray())
					dir = dir.rotate(c == 'R');
				currIns = new StringBuilder();
				currIns.append(let);
			}
		}

		return (1000L * (pos.row() + 1)) + (4L * (pos.col() + 1)) + dir.value;
	}

	private long part2(List<String> input)
	{
		char[] instructions = input.get(input.size() - 1).toCharArray();

		Point pos = new Point(0, 0);
		Direction dir = Direction.RIGHT;

		String l1 = input.get(0);
		while(l1.charAt(pos.col()) == ' ')
			pos = new Point(pos.row(), pos.col() + 1);

		boolean number = true;
		StringBuilder currIns = new StringBuilder();
		for(int i = 0; i <instructions.length; i++)
		{
			System.out.println(i);
			char let = instructions[i];
			boolean isNum = let >= '0' && let <= '9';
			if(isNum && number)
			{
				currIns.append(let);
			}
			else if(!isNum && number)
			{
				//Done
				number = false;
				int steps = Integer.parseInt(currIns.toString());
				for(int j = 0; j < steps; j++)
				{
					int faceFrom = getFace(pos);
					Point movedTo = dir.move(pos);
					if(movedTo.row() < 0 || movedTo.row() >= input.size() || movedTo.col() < 0 || movedTo.col() >= input.get(movedTo.row()).length())
					{
						Holder h = wrap2(dir, pos);
						pos = h.point;
						dir = h.dir;
						continue;
					}

					int faceTo = getFace(movedTo);
					if(faceFrom != faceTo)
					{
						Holder h = wrap2(dir, pos);
						pos = h.point;
						dir = h.dir;
					}
					else
					{

						char c = input.get(movedTo.row()).charAt(movedTo.col());
						if(c == ' ')
						{
							Holder h = wrap2(dir, pos);
							pos = h.point;
							dir = h.dir;
						}
						else if(c != '#')
						{
							pos = movedTo;
						}
					}

				}
				currIns = new StringBuilder();
				currIns.append(let);
			}
			else if(!isNum)
			{
				currIns.append(let);
			}
			else if(isNum)
			{
				//Done
				number = true;
				String step = currIns.toString();
				for(char c : step.toCharArray())
					dir = dir.rotate(c == 'R');
				currIns = new StringBuilder();
				currIns.append(let);
			}
		}

		return (1000 * (pos.row() + 1)) + (4 * (pos.col() + 1)) + dir.value;
	}

	private Point wrap(Direction going, Point pos, Point movedTo, List<String> input)
	{
		Direction opp = going.opposite();
		movedTo = opp.move(movedTo);
		char c2 = input.get(movedTo.row()).charAt(movedTo.col());
		while(c2 != ' ')
		{
			movedTo = opp.move(movedTo);
			if(movedTo.row() < 0 || movedTo.row() >= input.size() || movedTo.col() < 0 || movedTo.col() >= input.get(movedTo.row()).length())
				break;
			c2 = input.get(movedTo.row()).charAt(movedTo.col());
		}
		movedTo = going.move(movedTo);
		c2 = input.get(movedTo.row()).charAt(movedTo.col());
		return c2 == '#' ? pos : movedTo;
	}

	private Holder wrap2(Direction going, Point pos)
	{
		int faceFrom = getFace(pos);
		int faceTo = getFaceInDir(faceFrom, going);
		Holder h = changeFace(faceFrom, faceTo, pos, going);
		return new Holder(h.dir.move(h.point), h.dir);
	}

	private int getFace(Point p)
	{
		int r50 = p.row() / BOX_SIZE;
		int c50 = p.col() / BOX_SIZE;
		if(r50 == 0 && c50 == 1)
			return 1;
		if(r50 == 0 && c50 == 2)
			return 2;
		if(r50 == 1 && c50 == 1)
			return 3;
		if(r50 == 2 && c50 == 0)
			return 4;
		if(r50 == 2 && c50 == 1)
			return 5;
		if(r50 == 3 && c50 == 0)
			return 6;
		return -1;
	}

	private int getFaceInDir(int face, Direction dir)
	{
		if(face == 1)
		{
			return switch(dir)
					{
						case UP -> 6;
						case DOWN -> 3;
						case LEFT -> 4;
						case RIGHT -> 2;
					};
		}
		if(face == 2)
		{
			return switch(dir)
					{
						case UP -> 6;
						case DOWN -> 3;
						case LEFT -> 1;
						case RIGHT -> 5;
					};
		}
		if(face == 3)
		{
			return switch(dir)
					{
						case UP -> 1;
						case DOWN -> 5;
						case LEFT -> 4;
						case RIGHT -> 2;
					};
		}
		if(face == 4)
		{
			return switch(dir)
					{
						case UP -> 3;
						case DOWN -> 6;
						case LEFT -> 1;
						case RIGHT -> 5;
					};
		}
		if(face == 5)
		{
			return switch(dir)
					{
						case UP -> 3;
						case DOWN -> 6;
						case LEFT -> 4;
						case RIGHT -> 2;
					};
		}
		if(face == 6)
		{
			return switch(dir)
					{
						case UP -> 4;
						case DOWN -> 2;
						case LEFT -> 1;
						case RIGHT -> 5;
					};
		}
		return -1;
	}

	private Holder changeFace(int from, int to, Point fromP, Direction dir)
	{
		//Face 1
		if((from == 1 && to == 2) || (from == 2 && to == 1))
		{
			return new Holder(fromP, dir);
		}
		if((from == 1 && to == 3) || (from == 3 && to == 1))
		{
			return new Holder(fromP, dir);
		}
		if((from == 1 && to == 4) || (from == 4 && to == 1))
		{
			Point newP = new Point((3 * BOX_SIZE) - fromP.row(), from == 1 ? -1 : BOX_SIZE - 1);
			return new Holder(newP, dir.opposite());
		}
		if(from == 1 && to == 6)
		{
			Point newP = new Point(fromP.col() + (2 * BOX_SIZE), -1);
			return new Holder(newP, Direction.RIGHT);
		}
		if(from == 6 && to == 1)
		{
			Point newP = new Point(-1, fromP.row() - (2 * BOX_SIZE));
			return new Holder(newP, Direction.DOWN);
		}

		//Face 2
		if(from == 2 && to == 3)
		{
			Point newP = new Point(fromP.col() - BOX_SIZE, 2 * BOX_SIZE);
			return new Holder(newP, Direction.LEFT);
		}
		if(from == 3 && to == 2)
		{
			Point newP = new Point(BOX_SIZE, fromP.row() + BOX_SIZE);
			return new Holder(newP, Direction.UP);
		}
		if(from == 2 && to == 5)
		{
			Point newP = new Point((BOX_SIZE - fromP.row()) + (2 * BOX_SIZE), (2 * BOX_SIZE));
			return new Holder(newP, dir.opposite());
		}
		if(from == 5 && to == 2)
		{
			Point newP = new Point(((3 * BOX_SIZE) - fromP.row()) - 1, (3 * BOX_SIZE));
			return new Holder(newP, dir.opposite());
		}
		if(from == 2 && to == 6)
		{
			Point newP = new Point(4 * BOX_SIZE, fromP.col() - (2 * BOX_SIZE));
			return new Holder(newP, dir);
		}
		if(from == 6 && to == 2)
		{
			Point newP = new Point(-1, fromP.col() + (2 * BOX_SIZE));
			return new Holder(newP, dir);
		}

		//Face 3
		if((from == 3 && to == 5) || (from == 5 && to == 3))
		{
			return new Holder(fromP, dir);
		}
		if(from == 3 && to == 4)
		{
			Point newP = new Point((2 * BOX_SIZE) - 1, fromP.row() - BOX_SIZE);
			return new Holder(newP, Direction.DOWN);
		}
		if(from == 4 && to == 3)
		{
			Point newP = new Point(fromP.col() + BOX_SIZE, BOX_SIZE - 1);
			return new Holder(newP, Direction.RIGHT);
		}

		//Face 4
		if((from == 4 && to == 5) || (from == 5 && to == 4))
		{
			return new Holder(fromP, dir);
		}
		if((from == 4 && to == 6) || (from == 6 && to == 4))
		{
			return new Holder(fromP, dir);
		}

		//Face 5
		if(from == 5 && to == 6)
		{
			Point newP = new Point(fromP.col() + (2 * BOX_SIZE), BOX_SIZE);
			return new Holder(newP, Direction.LEFT);
		}
		if(from == 6 && to == 5)
		{
			Point newP = new Point(3 * BOX_SIZE, fromP.row() - (2 * BOX_SIZE));
			return new Holder(newP, Direction.UP);
		}
		return new Holder(fromP, dir);
	}

	private record Holder(Point point, Direction dir)
	{
	}

	private enum Direction
	{
		UP(new Point(-1, 0), 3),
		DOWN(new Point(1, 0), 1),
		LEFT(new Point(0, -1), 2),
		RIGHT(new Point(0, 1), 0);

		public final Point change;
		public final int value;

		Direction(Point change, int value)
		{
			this.change = change;
			this.value = value;
		}

		public Point move(Point p)
		{
			return new Point(p.row() + change.row(), p.col() + change.col());
		}

		public Direction opposite()
		{
			return switch(this)
					{
						case UP -> DOWN;
						case DOWN -> UP;
						case LEFT -> RIGHT;
						case RIGHT -> LEFT;
					};
		}

		public Direction rotate(boolean cw)
		{
			return switch(this)
					{
						case UP -> cw ? RIGHT : LEFT;
						case DOWN -> cw ? LEFT : RIGHT;
						case LEFT -> cw ? UP : DOWN;
						case RIGHT -> cw ? DOWN : UP;
					};
		}
	}
}
package dev.theturkey.aoc22;


import java.util.Objects;

public record Point(int row, int col)
{
	@Override
	public String toString()
	{
		return "Point{" +
				"row=" + row +
				", col=" + col +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Point point = (Point) o;
		return row == point.row && col == point.col;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(row, col);
	}
}
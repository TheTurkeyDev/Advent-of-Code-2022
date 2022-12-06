package dev.theturkey.aoc22;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 extends AOCPuzzle
{
	public Day06()
	{
		super("6");
	}

	@Override
	void solve(List<String> input)
	{
		String line = input.get(0);
		lap(findMarker(line, 4));
		lap(findMarker(line, 14));
	}

	public int findMarker(String input, int length)
	{
		int index = 0;
		String curr = input.substring(0, length);
		while(hasDups(curr, length))
		{
			index++;
			curr = input.substring(index, index + length);
		}
		return index + length;
	}

	public boolean hasDups(String curr, int exp)
	{
		Set<Character> chars = new HashSet<>();
		for(char c : curr.toCharArray())
			chars.add(c);
		return chars.size() != exp;
	}
}

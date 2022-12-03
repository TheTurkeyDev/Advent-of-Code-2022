package dev.theturkey.aoc22;

import java.util.List;

public class Day03 extends AOCPuzzle
{
	public Day03()
	{
		super("3");
	}

	@Override
	void solve(List<String> input)
	{
		int sum = 0;
		for(String s : input)
			sum += andStringsToNum(s.substring(0, s.length() / 2), s.substring(s.length() / 2));
		lap(sum);

		sum = 0;
		for(int i = 0; i < input.size(); i += 3)
			sum += andStringsToNum(input.get(i), input.get(i + 1), input.get(i + 2));
		lap(sum);
	}

	public int andStringsToNum(String... strs)
	{
		String and = strs[0];
		for(int i = 1; i < strs.length; i++)
			and = andStrings(and, strs[i]);
		char c = and.charAt(0);
		return (c - (c <= 'Z' ? 'A' : 'a')) + (c <= 'Z' ? 27 : 1);
	}

	public String andStrings(String s1, String s2)
	{
		StringBuilder builder = new StringBuilder();
		char[] s2Chars = s2.toCharArray();
		for(char c : s1.toCharArray())
		{
			for(char c2 : s2Chars)
			{
				if(c == c2)
				{
					builder.append(c);
					break;
				}
			}
		}

		return builder.toString();
	}
}

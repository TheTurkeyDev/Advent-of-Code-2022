package dev.theturkey.aoc22;

import java.util.List;

public class Day04 extends AOCPuzzle
{
	public Day04()
	{
		super("4");
	}

	@Override
	void solve(List<String> input)
	{
		int contains = 0;
		int overlap = 0;
		for(String s : input)
		{
			String[] parts = s.split(",");
			String[] sec1Parts = parts[0].split("-");
			String[] sec2Parts = parts[1].split("-");
			int sec1Lower = Integer.parseInt(sec1Parts[0]);
			int sec1Upper = Integer.parseInt(sec1Parts[1]);
			int sec2Lower = Integer.parseInt(sec2Parts[0]);
			int sec2Upper = Integer.parseInt(sec2Parts[1]);

			if(sec1Lower <= sec2Lower && sec1Upper >= sec2Upper)
				contains++;
			else if(sec1Lower >= sec2Lower && sec1Upper <= sec2Upper)
				contains++;

			if((sec1Lower <= sec2Lower && sec1Upper >= sec2Lower) || (sec1Lower <= sec2Upper && sec1Upper >= sec2Upper))
				overlap++;
			else if((sec2Lower <= sec1Lower && sec2Upper >= sec1Lower) || (sec2Lower <= sec1Upper && sec2Upper >= sec1Upper))
				overlap++;
		}
		lap(contains);
		lap(overlap);
	}
}

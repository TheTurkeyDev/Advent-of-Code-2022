package dev.theturkey.aoc22;

import java.util.List;

public class Day02 extends AOCPuzzle
{
	public Day02()
	{
		super("2");
	}

	@Override
	void solve(List<String> input)
	{
		long score = 0;
		long score2 = 0;
		for(String s : input)
		{
			int opp = s.charAt(0) - 'A';
			int val2 = s.charAt(2) - 'X';

			//Part 1
			if(opp - val2 == -1 || (opp == 2 && val2 == 0))
				score += 6;
			else if(opp == val2)
				score += 3;

			score += val2 + 1;

			//Part 2
			if(val2 == 0)
				score2 += (opp == 0 ? 3 : opp);
			else if(val2 == 1)
				score2 += 3 + opp + 1;
			else if(val2 == 2)
				score2 += 6 + ((opp + 1) % 3) + 1;
		}

		lap(score);
		lap(score2);
	}
}

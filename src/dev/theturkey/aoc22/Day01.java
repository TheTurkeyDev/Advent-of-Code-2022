package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.List;

public class Day01 extends AOCPuzzle
{
	public Day01()
	{
		super("1");
	}

	@Override
	void solve(List<String> input)
	{
		List<Integer> elfCals = new ArrayList<>();
		int elfNum = 0;
		for(String s : input)
		{
			if(s.trim().equalsIgnoreCase(""))
			{
				elfNum++;
				continue;
			}

			if(elfCals.size() == elfNum)
				elfCals.add(0);

			int cal = Integer.parseInt(s);
			int curr = elfCals.get(elfNum);
			elfCals.set(elfNum, curr + cal);
		}

		elfCals.sort((a, b) -> b - a);

		lap(elfCals.get(0));
		lap(elfCals.get(0) + elfCals.get(1) + elfCals.get(2));
	}

	//This is just extra and me trying to find a quicker solve time
	public void solveQuick(List<String> input)
	{
		int[] top3ElfCals = new int[3];
		int elfCals = 0;
		for(String s : input)
		{
			if(s.trim().equalsIgnoreCase(""))
			{
				for(int i = 0; i < top3ElfCals.length; i++)
				{
					if(top3ElfCals[i] < elfCals)
					{
						int temp = top3ElfCals[i];
						top3ElfCals[i] = elfCals;
						elfCals = temp;
					}
				}

				elfCals = 0;
				continue;
			}

			elfCals += Integer.parseInt(s);
		}

		lap(top3ElfCals[0]);
		lap(top3ElfCals[0] + top3ElfCals[1] + top3ElfCals[2]);
	}
}

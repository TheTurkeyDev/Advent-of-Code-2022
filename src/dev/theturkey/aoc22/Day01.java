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
		List<Integer> top3ElfCals = new ArrayList<>();
		int elfCals = 0;
		for(String s : input)
		{
			if(s.trim().equalsIgnoreCase(""))
			{
				boolean added = false;
				for(int i = 0; i < top3ElfCals.size(); i++)
				{
					if(top3ElfCals.get(i) < elfCals)
					{
						added = true;
						top3ElfCals.add(i, elfCals);
						if(top3ElfCals.size() == 4)
							top3ElfCals.remove(3);
						break;
					}
				}

				if(!added && top3ElfCals.size() < 3)
					top3ElfCals.add(elfCals);

				elfCals = 0;
				continue;
			}

			elfCals += Integer.parseInt(s);
		}

		lap(top3ElfCals.get(0));
		lap(top3ElfCals.get(0) + top3ElfCals.get(1) + top3ElfCals.get(2));
	}
}

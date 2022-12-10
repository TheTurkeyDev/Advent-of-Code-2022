package dev.theturkey.aoc22;

import java.util.List;

public class Day10 extends AOCPuzzle
{
	private static final boolean HIGH_CONTRAST_OUTPUT = false;

	public Day10()
	{
		super("10");
	}

	@Override
	void solve(List<String> input)
	{
		StringBuilder outputBuilder = new StringBuilder();
		long part1 = 0;
		long register = 1;
		long cycle = 0;
		int index = 0;
		int delay = 0;
		while(index < input.size())
		{
			outputBuilder.append(Math.abs(register - (cycle % 40)) <= 1 ? (HIGH_CONTRAST_OUTPUT ? "█" : "#") : (HIGH_CONTRAST_OUTPUT ? " " : "."));
			cycle++;

			if(cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220)
				part1 += (register * cycle);

			String[] parts = input.get(index).split(" ");
			switch(parts[0])
			{
				case "noop" -> index++;
				case "addx" ->
				{
					if(delay == 0)
					{
						delay = 1;
					}
					else
					{
						register += Integer.parseInt(parts[1]);
						delay = 0;
						index++;
					}
				}
			}
		}

		lap(part1);

		String output = outputBuilder.toString();
		for(int i = 0; i < output.length() / 40; i++)
			System.out.println(output.substring(i * 40, (i + 1) * 40));
		lap("");
	}
}

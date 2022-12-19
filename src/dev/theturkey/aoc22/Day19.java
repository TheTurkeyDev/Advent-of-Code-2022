package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day19 extends AOCPuzzle
{
	public Day19()
	{
		super("19");
	}

	@Override
	void solve(List<String> input)
	{
		List<Blueprint> blueprints = new ArrayList<>();
		for(String s : input)
		{
			int colon = s.indexOf(":");
			String resIns = s.substring(colon).trim();
			String[] parts = resIns.split("\\.");
			int oreOreCost = Integer.parseInt(parts[0].substring(23, 24));
			int clayOreCost = Integer.parseInt(parts[1].substring(23, 24));
			int obsidianOreCost = Integer.parseInt(parts[2].substring(27, 28));
			int obsidianClayCost = Integer.parseInt(parts[2].substring(37, parts[2].indexOf(" ", 37)));
			int geodeOreCost = Integer.parseInt(parts[3].substring(24, 25));
			int geodeObsidianCost = Integer.parseInt(parts[3].substring(34, parts[3].indexOf(" ", 34)));
			BlueprintData data = new BlueprintData(oreOreCost, clayOreCost, obsidianOreCost, obsidianClayCost, geodeOreCost, geodeObsidianCost);
			blueprints.add(new Blueprint(data));
		}

		int value = 0;
		for(int b = 0; b < blueprints.size(); b++)
		{
			Blueprint bp = new Blueprint(blueprints.get(b));
			int geodes = bp.getMaxGeodes(24);
			value += geodes * (b + 1);
			Blueprint.CACHE.clear();
			Blueprint.MAX = 0;
		}
		lap(value);

		value = 1;
		for(int b = 0; b < 3; b++)
		{
			Blueprint bp = new Blueprint(blueprints.get(b));
			int geodes = bp.getMaxGeodes(32);
			value *= geodes;
			Blueprint.CACHE.clear();
			Blueprint.MAX = 0;
		}
		lap(value);
	}

	private static class BlueprintData
	{
		public int oreOreCost;
		public int clayOreCost;
		public int obsidianOreCost;
		public int obsidianClayCost;
		public int geodeOreCost;
		public int geodeObsidianCost;

		public int[] maxRobots;

		public BlueprintData(int oreOreCost, int clayOreCost, int obsidianOreCost, int obsidianClayCost, int geodeOreCost, int geodeObsidianCost)
		{
			this.oreOreCost = oreOreCost;
			this.clayOreCost = clayOreCost;
			this.obsidianOreCost = obsidianOreCost;
			this.obsidianClayCost = obsidianClayCost;
			this.geodeOreCost = geodeOreCost;
			this.geodeObsidianCost = geodeObsidianCost;
			maxRobots = new int[]{
					Math.max(Math.max(oreOreCost, clayOreCost), Math.max(oreOreCost, geodeOreCost)),
					obsidianClayCost,
					geodeObsidianCost,
					Integer.MAX_VALUE
			};
		}
	}

	private static class Blueprint
	{
		public BlueprintData bpd;
		public int minute;

		public int[] robotsMaking = new int[]{0, 0, 0, 0};
		public int[] robots = new int[]{1, 0, 0, 0};
		// Ore, Clay, Obsidian, Geodes
		public int[] resources = new int[]{0, 0, 0, 0};

		public Blueprint(BlueprintData bpd)
		{
			minute = 0;
			this.bpd = bpd;
		}

		public Blueprint(Blueprint bp)
		{
			minute = bp.minute;
			bpd = bp.bpd;
			robotsMaking = Arrays.copyOf(bp.robotsMaking, bp.robotsMaking.length);
			robots = Arrays.copyOf(bp.robots, bp.robots.length);
			resources = Arrays.copyOf(bp.resources, bp.resources.length);
		}

		private static final Map<Blueprint, Integer> CACHE = new HashMap<>();
		private static int MAX = 0;

		public int getMaxGeodes(int maxMinutes)
		{
			if(minute == maxMinutes)
				return resources[3];

			if(CACHE.containsKey(this))
				return CACHE.get(this);

			List<Blueprint> bps = new ArrayList<>();
			if(resources[2] >= bpd.geodeObsidianCost && resources[0] >= bpd.geodeOreCost && robots[3] < bpd.maxRobots[3])
			{
				Blueprint bp = new Blueprint(this);
				bp.robotsMaking[3]++;
				bp.resources[2] -= bp.bpd.geodeObsidianCost;
				bp.resources[0] -= bp.bpd.geodeOreCost;
				bps.add(bp);
			}
			if(resources[1] >= bpd.obsidianClayCost && resources[0] >= bpd.obsidianOreCost && robots[2] < bpd.maxRobots[2])
			{
				Blueprint bp = new Blueprint(this);
				bp.robotsMaking[2]++;
				bp.resources[1] -= bp.bpd.obsidianClayCost;
				bp.resources[0] -= bp.bpd.obsidianOreCost;
				bps.add(bp);
			}
			if(resources[0] >= bpd.clayOreCost && robots[1] < bpd.maxRobots[1])
			{
				Blueprint bp = new Blueprint(this);
				bp.robotsMaking[1]++;
				bp.resources[0] -= bp.bpd.clayOreCost;
				bps.add(bp);
			}
			if(resources[0] >= bpd.oreOreCost && robots[0] < bpd.maxRobots[0])
			{
				Blueprint bp = new Blueprint(this);
				bp.robotsMaking[0]++;
				bp.resources[0] -= bp.bpd.oreOreCost;
				bps.add(bp);
			}

			if(!(resources[1] > bpd.maxRobots[1] && robots[2] == 0))
				bps.add(new Blueprint(this));

			int max = resources[3];
			for(Blueprint bp : bps)
			{
				for(int i = 0; i < bp.robots.length; i++)
					bp.resources[i] += bp.robots[i];

				for(int i = 0; i < bp.robots.length; i++)
				{
					bp.robots[i] += bp.robotsMaking[i];
					bp.robotsMaking[i] = 0;
				}
				bp.minute += 1;

				boolean canMake = false;
				for(int i = 0; i < maxMinutes - minute; i++)
				{
					int bots = bp.robots[3] + i + 2;
					int withMins = maxMinutes - (bp.minute + i);
					if(MAX <= bp.resources[3] + (bots * withMins))
					{
						canMake = true;
						break;
					}
				}
				if(!canMake)
					continue;

				int geodes = bp.getMaxGeodes(maxMinutes);
				CACHE.put(bp, geodes);
				if(geodes > max)
					max = geodes;
			}
			if(MAX < max)
				MAX = max;
			return max;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Blueprint cacheKey = (Blueprint) o;
			return minute == cacheKey.minute && Arrays.equals(robots, cacheKey.robots) && Arrays.equals(resources, cacheKey.resources);
		}

		@Override
		public int hashCode()
		{
			int result = Objects.hash(minute);
			result = 31 * result + Arrays.hashCode(robots);
			result = 31 * result + Arrays.hashCode(resources);
			return result;
		}
	}
}
package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day16 extends AOCPuzzle
{
	public Day16()
	{
		super("16");
	}

	private static final Map<String, Node> valves = new HashMap<>();
	private static final Map<String, Map<String, Integer>> pathFinding = new HashMap<>();

	@Override
	void solve(List<String> input)
	{

		for(String s : input)
		{
			Node n = new Node();
			n.valve = s.substring(6, s.indexOf(" ", 6));
			int semiC = s.indexOf(";");
			n.flowRate = Integer.parseInt(s.substring(23, semiC));
			int preSpace = s.indexOf(" ", s.indexOf("valve"));
			String[] tunnels = s.substring(preSpace).replaceAll(" ", "").split(",");
			n.connects.addAll(Arrays.asList(tunnels));
			valves.put(n.valve, n);
		}

		// Build up a map that holds every node and how far away each node is from it
		for(String s : valves.keySet())
		{
			List<String> toFind = new ArrayList<>(valves.keySet());
			List<QueuedNode> nodeQueue = new ArrayList<>();
			nodeQueue.add(new QueuedNode(s, ""));
			Map<String, Integer> paths = new HashMap<>();
			while(toFind.size() > 0)
			{
				QueuedNode qNode = nodeQueue.remove(0);
				if(!toFind.contains(qNode.node))
					continue;
				// We don't care about the node we are building the map of
				if(!qNode.from.isEmpty())
				{
					int distTo = paths.getOrDefault(qNode.from, 0) + 1;
					paths.put(qNode.node, distTo);
				}
				toFind.remove(qNode.node);
				Node node = valves.get(qNode.node);
				for(String adj : node.connects)
					if(toFind.contains(adj))
						nodeQueue.add(new QueuedNode(adj, qNode.node));
			}
			pathFinding.put(s, paths);
		}

		List<String> nonZeroValves = new ArrayList<>();
		for(String v : valves.keySet())
			if(valves.get(v).flowRate > 0)
				nonZeroValves.add(v);


		lap(getMaxPressurePart1("AA", 0, nonZeroValves));
	}

	private int getMaxPressurePart1(String nodeAt, int minute, List<String> nonZeroValves)
	{
		if(minute >= 30)
			return 0;

		int maxFlow = 0;
		Map<String, Integer> distances = pathFinding.get(nodeAt);
		for(int i = 0; i < nonZeroValves.size(); i++)
		{
			String valve = nonZeroValves.get(i);
			if(valve.equals(nodeAt))
				continue;
			Node vNode = valves.get(valve);
			int dist = distances.get(valve);
			int minAdj = minute + dist + 1;
			nonZeroValves.remove(valve);
			int nodeFlow = ((30 - Math.min(minAdj, 30)) * vNode.flowRate);
			int flow = nodeFlow + getMaxPressurePart1(valve, minAdj, nonZeroValves);
			nonZeroValves.add(i, valve);
			if(flow > maxFlow)
				maxFlow = flow;
		}

		return maxFlow;
	}

	private record QueuedNode(String node, String from)
	{

	}

	public static class Node
	{
		public String valve;
		public int flowRate;
		public Set<String> connects = new HashSet<>();

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Node node = (Node) o;
			return valve.equals(node.valve);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(valve);
		}
	}
}

package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 extends AOCPuzzle
{
	public Day13()
	{
		super("13");
	}

	@Override
	void solve(List<String> input)
	{
		List<CompNode> nodes = new ArrayList<>();
		int sum = 0;
		for(int i = 0; i < input.size(); i += 3)
		{
			Parser parser = new Parser(input.get(i), 0);
			CompList list1 = CompList.parse(parser);
			nodes.add(list1);
			parser = new Parser(input.get(i + 1), 0);
			CompList list2 = CompList.parse(parser);
			nodes.add(list2);
			if(compare(list1, list2) > 0)
				sum += ((i / 3) + 1);
		}
		lap(sum);
		CompNode div2 = CompList.parse(new Parser("[[2]]", 0));
		nodes.add(div2);
		CompNode div6 = CompList.parse(new Parser("[[6]]", 0));
		nodes.add(div6);

		nodes.sort(this::compare);
		Collections.reverse(nodes);

		int div2Index = 0;
		int div6Index = 0;
		for(int i = 0; i < nodes.size(); i++)
		{
			CompNode compNode = nodes.get(i);
			if(compNode == div2)
				div2Index = i + 1;
			if(compNode == div6)
				div6Index = i + 1;
		}
		lap(div2Index * div6Index);
	}

	private int compare(CompNode n1, CompNode n2)
	{
		if(n1 instanceof CompNumber num1 && n2 instanceof CompNumber num2)
		{
			return num2.value - num1.value;
		}
		else if(n1 instanceof CompList l1 && n2 instanceof CompList l2)
		{
			for(int i = 0; i < l1.nodes.size(); i++)
			{
				if(l2.nodes.size() == i)
					return -1;
				int val = compare(l1.nodes.get(i), l2.nodes.get(i));
				if(val != 0)
					return val;
			}

			return l1.nodes.size() < l2.nodes.size() ? 1 : 0;
		}
		else if(n1 instanceof CompNumber num1)
		{
			CompList list = new CompList();
			list.nodes.add(num1);
			return compare(list, n2);
		}
		else
		{
			CompList list = new CompList();
			list.nodes.add(n2);
			return compare(n1, list);
		}
	}

	public static class Parser
	{
		public String s;
		public int index;

		public Parser(String s, int index)
		{
			this.s = s;
			this.index = index;
		}
	}

	public static class CompNode
	{

	}

	public static class CompList extends CompNode
	{
		public List<CompNode> nodes = new ArrayList<>();

		public static CompList parse(Parser parser)
		{
			CompList list = new CompList();
			parser.index++;
			char c = parser.s.charAt(parser.index);
			while(c != ']')
			{
				if(c == ',')
					parser.index++;
				else if(c == '[')
					list.nodes.add(CompList.parse(parser));
				else
					list.nodes.add(CompNumber.parse(parser));
				c = parser.s.charAt(parser.index);
			}
			parser.index++;

			return list;
		}
	}

	public static class CompNumber extends CompNode
	{
		public int value;

		public CompNumber(int value)
		{
			this.value = value;
		}

		public static CompNumber parse(Parser parser)
		{
			int comma = parser.s.indexOf(",", parser.index);
			comma = comma == -1 ? parser.s.length() : comma;
			int sqrBracket = parser.s.indexOf("]", parser.index);
			sqrBracket = sqrBracket == -1 ? parser.s.length() : sqrBracket;
			String numStr = parser.s.substring(parser.index, Math.min(comma, sqrBracket));
			parser.index += numStr.length();
			return new CompNumber(Integer.parseInt(numStr));
		}

	}
}

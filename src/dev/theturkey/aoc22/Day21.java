package dev.theturkey.aoc22;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 extends AOCPuzzle
{
	public Day21()
	{
		super("21");
	}

	@Override
	void solve(List<String> input)
	{
		Map<String, Monkey> monkeys = new HashMap<>();
		for(String s : input)
		{
			String id = s.substring(0, 4);
			Monkey monkey;
			if(s.length() > 10)
				monkey = new MonkeyOperation();
			else
				monkey = new MonkeyNumber();
			monkey.id = id;
			monkeys.put(id, monkey);
		}

		for(String s : input)
		{
			String id = s.substring(0, 4);
			Monkey monkey = monkeys.get(id);
			String val = s.substring(6);
			if(s.length() > 10)
			{
				MonkeyOperation monkeyOperation = (MonkeyOperation) monkey;
				monkeyOperation.left = monkeys.get(val.substring(0, 4));
				monkeyOperation.right = monkeys.get(val.substring(7));
				monkeyOperation.op = val.charAt(5);
			}
			else
			{
				((MonkeyNumber) monkey).number = Integer.parseInt(val);
			}
		}

		MonkeyOperation root = (MonkeyOperation) monkeys.get("root");

		lap(root.getValue());

		MonkeyNumber human = (MonkeyNumber) monkeys.get("humn");
		human.number = 1;

		long left = root.left.getValue();
		long right = root.right.getValue();
		long low = 0;
		long high = 100000000000000L;

		while(left != right && high != low)
		{
			long val = ((high - low) / 2) + low;
			human.number = val;
			left = root.left.getValue();
			right = root.right.getValue();

			if(left > right)
			{
				low = val;
			}
			else if(left < right)
			{
				high = val;
			}
			else
			{
				high--;
				low++;
			}
		}

		lap(human.number);
	}

	private static abstract class Monkey
	{
		public String id;

		public abstract long getValue();
	}

	private static class MonkeyNumber extends Monkey
	{
		public long number;

		public long getValue()
		{
			return number;
		}
	}

	private static class MonkeyOperation extends Monkey
	{
		public Monkey left;
		public Monkey right;
		public char op;

		public long getValue()
		{
			long leftVal = left.getValue();
			long rightVal = right.getValue();
			return switch(op)
					{
						case '+' -> leftVal + rightVal;
						case '-' -> leftVal - rightVal;
						case '*' -> leftVal * rightVal;
						case '/' -> leftVal / rightVal;
						default -> leftVal;
					};
		}
	}
}
package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.List;

public class Day11 extends AOCPuzzle
{
	private static long MASTER_DIVISOR = 1;

	public Day11()
	{
		super("11");
	}

	@Override
	void solve(List<String> input)
	{
		List<Monkey> monkeys = new ArrayList<>();
		for(int i = 0; i <= input.size() / 7; i++)
		{
			int index = i * 7;
			Monkey monkey = new Monkey();
			monkey.number = i;
			String[] items = input.get(index + 1).substring(18).split(",");
			for(String s : items)
				monkey.items.add(Long.parseLong(s.trim()));

			String operationString = input.get(index + 2);
			String opAmount = operationString.substring(25).trim();
			if(operationString.charAt(23) == '*')
				monkey.operation = number -> number * (opAmount.equals("old") ? number : Integer.parseInt(opAmount));
			else if(operationString.charAt(23) == '+')
				monkey.operation = number -> number + (opAmount.equals("old") ? number : Integer.parseInt(opAmount));

			monkey.test = new Test();
			monkey.test.divisor = Integer.parseInt(input.get(index + 3).substring(21));
			monkey.test.trueMonkey = Integer.parseInt(input.get(index + 4).substring(29));
			monkey.test.falseMonkey = Integer.parseInt(input.get(index + 5).substring(30));
			monkeys.add(monkey);
			MASTER_DIVISOR *= monkey.test.divisor;
		}

		for(int i = 0; i < 10000; i++)
		{
			for(Monkey monkey : monkeys)
			{
				for(int j = monkey.items.size() - 1; j >= 0; j--)
				{
					long value = monkey.items.remove(j);
					value = monkey.operation.preformOperation(value) % MASTER_DIVISOR;
					//value = value / 3; # Toggle this for part 1 vs part 2
					monkey.test.test(value, monkeys);
				}
			}

			if(i == 19)
			{
				monkeys.sort((a, b) -> b.test.tested - a.test.tested);
				lap((long) monkeys.get(0).test.tested * (long) monkeys.get(1).test.tested);
			}
		}

		monkeys.sort((a, b) -> b.test.tested - a.test.tested);
		lap((long) monkeys.get(0).test.tested * (long) monkeys.get(1).test.tested);
	}

	public class Monkey
	{
		public int number;
		public List<Long> items = new ArrayList<>();
		public Operation operation;
		public Test test;
	}

	private interface Operation
	{
		long preformOperation(long number);
	}

	private class Test
	{
		public int tested = 0;
		public int divisor;
		public int trueMonkey;
		public int falseMonkey;

		public void test(long value, List<Monkey> monkeys)
		{
			tested++;
			monkeys.get(value % divisor == 0 ? trueMonkey : falseMonkey).items.add(value);
		}
	}
}

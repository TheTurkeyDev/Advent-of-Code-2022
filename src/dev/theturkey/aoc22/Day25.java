package dev.theturkey.aoc22;

import java.util.List;

public class Day25 extends AOCPuzzle
{
	public Day25()
	{
		super("25");
	}

	@Override
	void solve(List<String> input)
	{
		long sum = 0;
		for(String s : input)
			sum += snafuToDec(s);
		lap(decToSNAFU(sum));
	}

	public long snafuToDec(String snaf)
	{
		long dec = 0;
		for(int i = snaf.length() - 1; i >= 0; i--)
		{
			int num = switch(snaf.charAt(i))
					{
						case '2' -> 2;
						case '1' -> 1;
						case '-' -> -1;
						case '=' -> -2;
						default -> 0;
					};
			dec += num * Math.pow(5, (snaf.length() - 1 - i));
		}
		return dec;
	}

	public String decToSNAFU(long dec)
	{
		int startPower = 0;
		while(dec > Math.pow(5, startPower))
			startPower++;
		startPower--;
		StringBuilder snafu = new StringBuilder();
		for(int i = startPower; i >= 0; i--)
		{
			long pow = (long) Math.pow(5, i);
			snafu.append(dec / pow);
			dec %= pow;
		}

		boolean carry = false;
		for(int i = snafu.length() - 1; i >= 0; i--)
		{
			char c = snafu.charAt(i);

			if(carry)
				c++;

			carry = false;

			if(c == '3')
			{
				snafu.setCharAt(i, '=');
				carry = true;
			}
			else if(c == '4')
			{
				snafu.setCharAt(i, '-');
				carry = true;
			}
			else if(c == '5')
			{
				snafu.setCharAt(i, '0');
				carry = true;
			}
			else {
				snafu.setCharAt(i, c);
			}
		}

		String toReturn = snafu.toString();
		if(carry)
			toReturn = "1" + toReturn;

		return toReturn;
	}
}
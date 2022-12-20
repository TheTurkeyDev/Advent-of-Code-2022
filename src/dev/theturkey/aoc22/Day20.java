package dev.theturkey.aoc22;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day20 extends AOCPuzzle
{
	private static final int DECRYPT_KEY = 811589153;

	public Day20()
	{
		super("20");
	}

	@Override
	void solve(List<String> input)
	{
		List<Num> file = new ArrayList<>();
		for(int i = 0; i < input.size(); i++)
			file.add(new Num(input.get(i), i));

		List<Num> output = mix(file, file);
		lap(getAnswer(output));

		for(Num num : file)
			num.value *= DECRYPT_KEY;

		output = new ArrayList<>(file);
		for(int i = 0; i < 10; i++)
			output = mix(file, output);
		lap(getAnswer(output));
	}

	public long getAnswer(List<Num> out)
	{
		int zeroIndex = 0;
		for(int i = 0; i < out.size(); i++)
		{
			if(out.get(i).value == 0)
			{
				zeroIndex = i;
				break;
			}
		}

		int thousandth = (zeroIndex + 1000) % out.size();
		int twoThousandth = (zeroIndex + 2000) % out.size();
		int threeThousandth = (zeroIndex + 3000) % out.size();
		return out.get(thousandth).value + out.get(twoThousandth).value + out.get(threeThousandth).value;
	}

	public List<Num> mix(List<Num> ogFile, List<Num> toMix)
	{
		List<Num> output = new ArrayList<>(toMix);

		for(Num num : ogFile)
		{
			int ogIndex = output.indexOf(num);
			output.remove(num);
			long newIndex = (ogIndex + num.value) % output.size();
			if(newIndex < 0)
				newIndex = newIndex + output.size();
			output.add((int) newIndex, num);
		}
		return output;
	}

	private static class Num
	{
		public int index;
		public long value;

		public Num(String value, int index)
		{
			this.value = Long.parseLong(value);
			this.index = index;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Num num = (Num) o;
			return index == num.index && value == num.value;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(index, value);
		}

		@Override
		public String toString()
		{
			return String.valueOf(value);
		}
	}
}
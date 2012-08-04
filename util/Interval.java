package util;

public class Interval<Key extends Comparable>
{
	public final Key low;
	public final Key high;

	public Interval(Key low, Key high) 
	{
		if (less(high, low))
		{
			throw new RuntimeException("Illegal argument");
		}
		this.low = low;
		this.high = high;
	}

	private boolean less(Key x, Key y) 
	{
		return x.compareTo(y) < 0;
	}

	public boolean contains(Key x) 
	{
		return !less(x, low) && !less(high, x);
	}
}

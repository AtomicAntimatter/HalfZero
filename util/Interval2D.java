package util;

public class Interval2D<Key extends Comparable>
{
	public final Interval<Key> intervalX;
	public final Interval<Key> intervalY;

	public Interval2D(Interval<Key> intervalX, Interval<Key> intervalY) 
	{
		this.intervalX = intervalX;
		this.intervalY = intervalY;
	}

	public boolean contains(Key x, Key y) 
	{
		return intervalX.contains(x) && intervalY.contains(y);
	}
}

package halfzero;

import java.util.HashMap;
import java.util.Set;

public interface GridIO<E extends java.io.Serializable> 
{
	/*
	 * Maybe do all of this on a different thread
	 */
	
	
	//Return a set of Tiles that are within the bounds.
	//This method should be fast
	//These tiles are stored in an external file
	//If some tiles are in cache, don't take from file. Append cache to set.
	Grid<E> get(int x0, int x1, int y0, int y1);
	
	//This checks the cache for tiles that have not been used in awhile
	//A tile's time spent in cache is proportional to frequency accessed
	//This method is called once every 200ms
	void cleanCache();
	
	//This method sets the maximum size of the cache
	void setCacheSize(int size);
	
	//If the cache is not full, fill it with low frequency tiles
	//Fill it according to movement prediction
	//0 = no movement, 1 = up, 2 = down, 3 = left, 4 = right
	void preloadCache(int movement, int x0, int x1, int y0, int y1);
	
	//This method pushes the column of tiles to the IO. 
	//The IO handles the columns and pieces afterwards, as sometimes the 
	//Entire map may not be stored in active memory on creation
	//In the HashMap, the key is the Tile's location, the value is the Tile.
	void load(java.util.Map columns);
	
	//This method call means the tile loading is complete and the 
	//IO can figure out how to handle its organization when writing to
	//external file.
	void writeMap();
}

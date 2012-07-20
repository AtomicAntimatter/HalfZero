package halfzero;

import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Map
{
	private final int TILE_HEIGHT = 38, TILE_WIDTH = 76;
	private float blueDescend = 1;
	private java.util.Map<String, Tile> tileMap = new HashMap();
	private final int MAP_TILE_COUNT; 
	
	public Map(final int MAP_LENGTH, final int MAP_WIDTH)
	{
		MAP_TILE_COUNT = MAP_LENGTH * MAP_WIDTH;
		
		for(int i = 0; i < MAP_WIDTH; i++)
		{
			for(int j = MAP_LENGTH; j > 0; j--)
			{
				int tileX = ((j+i)*TILE_WIDTH/2) + Display.getWidth()/2 - TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4;
				int tileY = ((j-i)*TILE_HEIGHT/2) + Display.getHeight()/2 - TILE_HEIGHT/2 - TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4;
				addTile(tileX, tileY, TILE_WIDTH, TILE_HEIGHT);
			}
		}	
	}
	
	private void addTile(int x, int y, int width, int height)
	{
		blueDescend -= (float)(1.0f/MAP_TILE_COUNT);
		tileMap.put(x + " " + y, new Tile(x,y,width,height,blueDescend));		
	}
	
	public Tile getTile(int spotX, int spotY)
	{
		return tileMap.get(spotX + " " + spotY); 
	}
	
	public synchronized void renderMap()
	{
		Iterator i = tileMap.entrySet().iterator();
		while(i.hasNext())
		{
			java.util.Map.Entry<String, Tile> pair = (java.util.Map.Entry<String, Tile>)i.next();
			pair.getValue().renderTile();
		}
	}
	
	public class Tile
	{
		private int x1 = 0, x2 = 0, x3 = 0, x4 = 0;
		private int y1 = 0, y2 = 0, y3 = 0, y4 = 0;
		private float red = 0, blue = 0, green = 0;
		
		public Tile(int x, int y, int width, int  height, float _blue)
		{
			x1 = x;
			y1 = y+height/2;
			
			x2 = x+width/2;
			y2 = y;
			
			x3 = x;
			y3 = y-height/2;
			
			x4 = x-width/2;
			y4 = y;
			
			blue = _blue;
		}
		
		public void renderTile()
		{
			GL11.glColor3f(red, green, blue);
			GL11.glPushMatrix();
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2i(x1, y1);
				GL11.glVertex2i(x2, y2);
				GL11.glVertex2i(x3, y3);
				GL11.glVertex2i(x4, y4);
				GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
}

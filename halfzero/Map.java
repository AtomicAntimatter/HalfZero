package halfzero;

import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Map
{
	private final int TILE_HEIGHT = 38, TILE_WIDTH = 76;
	private java.util.Map<int[], Tile> tileMap = new HashMap();
	private final int MAP_LENGTH, MAP_WIDTH; 
	private int offsetX = 0, offsetY = 0;
	
	public Map(final int _MAP_LENGTH, final int _MAP_WIDTH)
	{
		MAP_LENGTH = _MAP_LENGTH;
		MAP_WIDTH = _MAP_WIDTH;	
		//float blueDescend = 1;
		
		for(int i = 0; i < MAP_WIDTH; i++)
		{
			for(int j = MAP_LENGTH; j > 0; j--)
			{
				int x = ((j+i)*TILE_WIDTH/2) + Display.getWidth()/2 - TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4;
				int y = ((j-i)*TILE_HEIGHT/2) + Display.getHeight()/2 - TILE_HEIGHT/2 - TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4;
				
				/* If you want to see how it is rendered descending
				blueDescend -= (float)(1.0f/(MAP_LENGTH*MAP_WIDTH));
				float[] colors = {0, 0, blueDescend};
				tileMap.put(x + " " + y, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
				*/
				
				float[] colors = {(float)Math.random(), (float)Math.random(), (float)Math.random()};
				tileMap.put(new int[]{x, y}, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
			}
		}	
	}
	
	public void moveMap(final float moveX, final float moveY)
	{
		GL11.glTranslatef(moveX, moveY, 0);
		offsetX -= moveX;
		offsetY -= moveY;
	}
	
	public void renderMap()
	{
		Iterator i = tileMap.entrySet().iterator();
		while(i.hasNext())
		{
			java.util.Map.Entry<int[], Tile> pair = (java.util.Map.Entry<int[], Tile>)i.next();
			if(isOnScreen(pair.getKey()))
			{
				pair.getValue().renderTile();
			}
		}
	}
	
	private boolean isOnScreen(int[] tp)
	{	
		if(((tp[0] - offsetX) < -TILE_WIDTH)||((tp[0] - offsetX) > Display.getWidth()+TILE_WIDTH)
			||((tp[1] - offsetY) < -TILE_HEIGHT)||((tp[1] - offsetY) > Display.getHeight()+TILE_HEIGHT))
		{
			return false;
		}
		
		return true;
	}
	
	//For visual reference
	public void renderCrosshair()
	{
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
		GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(offsetX, Display.getHeight()/2 + offsetY);
			GL11.glVertex2i(Display.getWidth()+offsetX, Display.getHeight()/2 + offsetY);
			GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(Display.getWidth()/2 + offsetX, offsetY);
			GL11.glVertex2i(Display.getWidth()/2 + offsetX, Display.getHeight() + offsetY);
			GL11.glEnd();
		GL11.glPopMatrix();	
	}
	
	public class Tile
	{
		private int x1 = 0, x2 = 0, x3 = 0, x4 = 0;
		private int y1 = 0, y2 = 0, y3 = 0, y4 = 0;
		private float red = 0, green = 0, blue = 0;
		
		public Tile(int x, int y, int width, int  height, float[] colors)
		{
			x1 = x;
			y1 = y+height/2;
			
			x2 = x+width/2;
			y2 = y;
			
			x3 = x;
			y3 = y-height/2;
			
			x4 = x-width/2;
			y4 = y;
			
			red = colors[0];
			green = colors[1];
			blue = colors[2];
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

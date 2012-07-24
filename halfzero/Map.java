package halfzero;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

public class Map
{
	private final int TILE_HEIGHT = 38, TILE_WIDTH = 76;
	private final int MAP_LENGTH, MAP_WIDTH; 
	private int offsetX = 0, offsetY = 0;
	private java.util.Map<Line2D.Float[], java.util.Map<int[], Tile>> tileMaps = new HashMap();
	private final Rectangle2D screenRect = new Rectangle2D.Float(0,0,Display.getWidth(),Display.getHeight());
	
	public Map(final int _MAP_LENGTH, final int _MAP_WIDTH)
	{
		MAP_LENGTH = _MAP_LENGTH;
		MAP_WIDTH = _MAP_WIDTH;	
		//float blueDescend = 1;
		
		HashMap<int[], Tile> tileColumn = new HashMap();
		
		for(int i = 0; i < MAP_WIDTH; i++)
		{
			Point tC = new Point(0,0), rC = tC, lC = tC, bC = tC;
			Line2D.Float l1, l2;
			
			for(int j = MAP_LENGTH; j > 0; j--)
			{
				int x = ((j+i)*TILE_WIDTH/2) + Display.getWidth()/2 - TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4;
				int y = ((j-i)*TILE_HEIGHT/2) + Display.getHeight()/2 - TILE_HEIGHT/2 - TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4;
				
				/* If you want to see how it is rendered descending
				blueDescend -= (float)(1.0f/(MAP_LENGTH*MAP_WIDTH));
				float[] colors = {0, 0, blueDescend};
				tileMap.put(new int[]{x, y}, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
				*/
				
				if(j == MAP_LENGTH)
				{
					tC = new Point(x, y + TILE_HEIGHT/2);
					rC = new Point(x + TILE_WIDTH/2, y);
				}
				if(j == 1)
				{
					lC = new Point(x - TILE_WIDTH/2, y);
					bC = new Point(x, y - TILE_HEIGHT/2);		
				}
				
				float[] colors = {(float)Math.random(), (float)Math.random(), (float)Math.random()};
				tileColumn.put(new int[]{x, y}, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
			}
			
			l1 = new Line2D.Float(tC, lC);
			l2 = new Line2D.Float(rC, bC);
			tileMaps.put(new Line2D.Float[]{l1, l2}, new HashMap(tileColumn)); 
			tileColumn.clear();
		}	
	}
	
	public void moveMap(final float moveX, final float moveY)
	{
		glTranslatef(moveX, moveY, 0);
		screenRect.setRect(new Rectangle2D.Float(offsetX, offsetY, Display.getWidth(), Display.getHeight()));
		offsetX -= moveX;
		offsetY -= moveY;
	}
	
	public void renderMap()
	{
		Iterator i = tileMaps.entrySet().iterator();
		while(i.hasNext())
		{
			java.util.Map.Entry<Line2D.Float[], HashMap<int[], Tile>> pair1 = (java.util.Map.Entry<Line2D.Float[], HashMap<int[], Tile>>)i.next();
			if(isBlockOnScreen(pair1.getKey()))
			{
				Iterator j = pair1.getValue().entrySet().iterator();
				while(j.hasNext())
				{
					java.util.Map.Entry<int[], Tile> pair2 = (java.util.Map.Entry<int[], Tile>)j.next();

					if(isTileOnScreen(pair2.getKey())) 
					{
						pair2.getValue().renderTile();
					}
				}
			}	
		}
	}
	
	private boolean isBlockOnScreen(Line2D.Float[] bp)
	{
		if(bp[0].intersects(screenRect)||bp[1].intersects(screenRect))
		{
			return true;
		}
		return false;
	}
	
	private boolean isTileOnScreen(int[] tp)
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
		glColor3f(0.5f, 0.5f, 1.0f);
		glPushMatrix();
			glBegin(GL_LINES);
			glVertex2i(offsetX, Display.getHeight()/2 + offsetY);
			glVertex2i(Display.getWidth()+offsetX, Display.getHeight()/2 + offsetY);
			glEnd();
		glPopMatrix();
		
		glPushMatrix();
			glBegin(GL_LINES);
			glVertex2i(Display.getWidth()/2 + offsetX, offsetY);
			glVertex2i(Display.getWidth()/2 + offsetX, Display.getHeight() + offsetY);
			glEnd();
		glPopMatrix();	
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
			glColor3f(red, green, blue);
			glPushMatrix();
				glBegin(GL_QUADS);
				glVertex2i(x1, y1);
				glVertex2i(x2, y2);
				glVertex2i(x3, y3);
				glVertex2i(x4, y4);
				glEnd();
			glPopMatrix();
		}
	}
}

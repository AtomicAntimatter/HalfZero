package halfzero;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Map
{
	private final int TILE_HEIGHT = 38, TILE_WIDTH = 76;
	private final int MAP_LENGTH, MAP_WIDTH; 
	private int offsetX = 0, offsetY = 0;
	//private java.util.Map<Line2D.Float[], java.util.Map<int[], Tile>> tileMaps = new HashMap();
	//private final Rectangle2D screenRect = new Rectangle2D.Float(0,0,Display.getWidth(),Display.getHeight());
	private float zoomFactor = 1; 
	private final int centerX = Display.getWidth()/2, centerY = Display.getHeight()/2;
	private java.util.Map<int[], Tile> tileMap = new HashMap();
	
	public Map(final int _MAP_LENGTH, final int _MAP_WIDTH)
	{
		MAP_LENGTH = _MAP_LENGTH;
		MAP_WIDTH = _MAP_WIDTH;	
		
		//HashMap<int[], Tile> tileColumn = new HashMap();
		
		for(int i = 0; i < MAP_WIDTH; i++)
		{
			//Point tC = new Point(0,0), rC = tC, lC = tC, bC = tC;
			//Line2D.Float l1, l2;
			
			for(int j = MAP_LENGTH; j > 0; j--)
			{
				int x = ((j+i)*TILE_WIDTH/2) - TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4;
				int y = ((j-i)*TILE_HEIGHT/2) - TILE_HEIGHT/2 - TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4;
				
				/*
				if(j == MAP_LENGTH)
				{
					tC = new Point(x, y + TILE_HEIGHT/2);
					rC = new Point(x + TILE_WIDTH/2, y);
				}
				if(j == 1)
				{
					lC = new Point(x - TILE_WIDTH/2, y);
					bC = new Point(x, y - TILE_HEIGHT/2);		
				}*/
				
				float[] colors = {(float)Math.random(), (float)Math.random(), (float)Math.random()};
				//tileColumn.put(new int[]{x, y}, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
				tileMap.put(new int[]{x, y}, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
			}
			
			//l1 = new Line2D.Float(tC, lC);
			//l2 = new Line2D.Float(rC, bC);
			//tileMaps.put(new Line2D.Float[]{l1, l2}, new HashMap(tileColumn)); 
			//tileColumn.clear();
		}	
	}
	
	public void zoomMap(boolean in, int delta)
	{
		if(in)
		{
			zoomFactor += 0.001f*delta;
		}
		else
		{
			zoomFactor -= 0.001f*delta;
		}	
	}
	
	public void moveMap(final float moveX, final float moveY)
	{
		if((moveX == 0)&&(moveY == 0)) return;
		
		GL11.glTranslatef(moveX, moveY, 0);
		//screenRect.setRect(new Rectangle2D.Float(offsetX, offsetY, Display.getWidth(), Display.getHeight()));
		offsetX -= moveX;
		offsetY -= moveY;
	}
	
	public void renderMap()
	{
		/*
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
		}*/
		
		Iterator i = tileMap.entrySet().iterator();
		while(i.hasNext())
		{
			java.util.Map.Entry<int[], Tile> pair = (java.util.Map.Entry<int[], Tile>)i.next();
			if(isTileOnScreen(pair.getKey()))
			{
				pair.getValue().renderTile();
			}
		}
	}
	
	/*
	private boolean isBlockOnScreen(Line2D.Float[] bp)
	{
		if(bp[0].intersects(screenRect)||bp[1].intersects(screenRect))
		{
			return true;
		}
		return false;
	}*/
	
	private boolean isTileOnScreen(int[] tp)
	{	
		/*
		if(((tp[0] - offsetX + zoomOffsetX) < -TILE_WIDTH)
			||((tp[0] - offsetX + zoomOffsetX) > Display.getWidth()+TILE_WIDTH)
			||((tp[1] - offsetY + zoomOffsetY) < -TILE_HEIGHT)
			||((tp[1] - offsetY + zoomOffsetY) > Display.getHeight()+TILE_HEIGHT))
		{
			return false;
		}*/
		
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
		private final int x1, x2, x3, x4;
		private final int y1, y2, y3, y4;
		public final int x, y, h, w;
		private float red = 0, green = 0, blue = 0;
		
		public Tile(final int _x, final int _y, final int _w, final int _h, final float[] colors)
		{
			x = _x; y = _y; h = _h; w = _w;

			x1 = x; 
			y1 = y+_h/2;			
			x2 = x+w/2; 
			y2 = y;
			x3 = x; 
			y3 = y-h/2;
			x4 = x-w/2; 
			y4 = y;
			
			red = colors[0]; green = colors[1]; blue = colors[2];
		}
		
		public void renderTile()
		{
			GL11.glColor3f(red, green, blue);
			GL11.glPushMatrix();
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(x1*zoomFactor + centerX, y1*zoomFactor + centerY);
				GL11.glVertex2f(x2*zoomFactor + centerX, y2*zoomFactor + centerY);
				GL11.glVertex2f(x3*zoomFactor + centerX, y3*zoomFactor + centerY);
				GL11.glVertex2f(x4*zoomFactor + centerX, y4*zoomFactor + centerY);
				GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
}

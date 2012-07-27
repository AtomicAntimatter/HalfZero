package halfzero;

import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Map
{
	private final int TILE_HEIGHT = 38, TILE_WIDTH = 76;
	private final int MAP_LENGTH, MAP_WIDTH; 
	private int offsetX = 0, offsetY = 0;
	private float zoomFactor = 1; 
	private float centerX, centerY;
	//private java.util.Map<int[], Tile> tileMap = new HashMap();
        private Grid<Tile> map;
	
	public Map(final int _MAP_LENGTH, final int _MAP_WIDTH)
	{
		MAP_LENGTH = _MAP_LENGTH;
		MAP_WIDTH = _MAP_WIDTH;	
		
		centerX = Display.getWidth()/2 - TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4;
		centerY = Display.getHeight()/2 - TILE_HEIGHT/2 - TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4;
                
                map = new ListGrid<Tile>(MAP_LENGTH, MAP_WIDTH, java.util.LinkedList.class);
		
		for(int i = 0; i < MAP_WIDTH; i++)
		{	
			for(int j = MAP_LENGTH - 1; j >= 0; j--)
			{
				int x = ((j+i)*TILE_WIDTH/2);
				int y = ((j-i)*TILE_HEIGHT/2);
							
				float[] colors = {(float)Math.random(), (float)Math.random(), (float)Math.random()};
				//tileMap.put(new int[]{x, y}, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
                                map.set(i, j, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
			}
		}	
	}
	
	public void zoomMap(int delta)
	{
		zoomFactor += 0.001f*delta;
		centerX = Display.getWidth()/2 - zoomFactor*(TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4 + offsetX);
		centerY = Display.getHeight()/2 - zoomFactor*(TILE_HEIGHT/2 + TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4 + offsetY);
	}
	
	public void moveMap(final float moveX, final float moveY)
	{
		if((moveX == 0)&&(moveY == 0)) return;
		
		offsetX -= moveX;
		offsetY -= moveY;
		
		centerX = Display.getWidth()/2 - zoomFactor*(TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4 + offsetX);
		centerY = Display.getHeight()/2 - zoomFactor*(TILE_HEIGHT/2 + TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4 + offsetY);
	}
	
	public void renderMap()
	{		
		Iterator<Tile> i = map.iterator();
		while(i.hasNext())
                    i.next().renderTile();
	}

	public void renderCrosshair()
	{
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
		GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(0, Display.getHeight()/2);
			GL11.glVertex2i(Display.getWidth(), Display.getHeight()/2);
			GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(Display.getWidth()/2, 0);
			GL11.glVertex2i(Display.getWidth()/2, Display.getHeight());
			GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public class Tile implements java.io.Serializable
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

package halfzero;

import org.lwjgl.opengl.GL11;

public class Map
{
	private Tile[][] tileMap;
	private int numX, numY;
	
	public Map(int _numTilesX, int _numTilesY)
	{
		numX = _numTilesX;
		numY = _numTilesY;
		tileMap = new Tile[numX][numY];	
	}
	
	public void addTile(int x, int y, int width, int height)
	{
		int a = 0, b = 0;
		while(tileMap[a][b] != null)
		{
			a++;
			if(a == numX)
			{
				a = 0;
				b++;
			}
		}

		tileMap[a][b] = new Tile(x,y,width,height);
	}
	
	public Tile getTile(int spotX, int spotY)
	{
		return tileMap[spotX][spotY]; 
	}
	
	public class Tile
	{
		private int x1 = 0, x2 = 0, x3 = 0, x4 = 0;
		private int y1 = 0, y2 = 0, y3 = 0, y4 = 0;
		private float red = 0, blue = 0, green = 0;
		public Tile(int x, int y, int width, int  height)
		{
			x1 = x;
			y1 = y+height/2;
			
			x2 = x+width/2;
			y2 = y;
			
			x3 = x;
			y3 = y-height/2;
			
			x4 = x-width/2;
			y4 = y;
			
			red = (float)Math.random();
			blue = (float)Math.random();
			green = (float)Math.random();
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

package halfzero;

import halfzero.util.Coordinate;
import halfzero.util.Grid;
import halfzero.util.HashGrid;
import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import static halfzero.util.Functions.*;

public class Map
{
	private final int TILE_HEIGHT = 38, TILE_WIDTH = 76;
	private final int MAP_LENGTH, MAP_WIDTH; 
        private final int FUDGE = 3;
	private int offsetX = 0, offsetY = 0, width, height;
        private final int[] boundX = {0,0}, boundY = {0,0};
        private Tile center;
	private float zoomFactor = 1; 
	private float centerX, centerY;
        private final float cX, cY;
	private java.util.Map<int[], Tile> tileMap = new HashMap();
        private Grid<Tile> map;
	
	public Map(final int _MAP_LENGTH, final int _MAP_WIDTH)
	{
		MAP_LENGTH = _MAP_LENGTH;
		MAP_WIDTH = _MAP_WIDTH;
		
		centerX = cX = Display.getWidth()/2 - TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4;
		centerY = cY = Display.getHeight()/2 - TILE_HEIGHT/2 - TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4;
                
                map = new HashGrid<Tile>(MAP_LENGTH, MAP_WIDTH);
		
		for(int i = 0; i < MAP_WIDTH; i++)
		{	
			for(int j = MAP_LENGTH - 1; j >= 0; j--)
			{
				int x = ((j+i)*TILE_WIDTH/2);
				int y = ((j-i)*TILE_HEIGHT/2);
							
				float[] colors = {(float)Math.random(), (float)Math.random(), (float)Math.random()};
				//tileMap.put(new int[]{x, y}, new Tile(x, y, TILE_WIDTH, TILE_HEIGHT, colors));
                                map.set(i, j, new Tile(this, x, y, i, j, TILE_WIDTH, TILE_HEIGHT, colors));
			}
		}
                
                Iterator<Tile> i = map.iterator();
                while(i.hasNext())
                    i.next().updatePoints();
                
                findZoom();
	}
	
	public void zoomMap(int delta)
	{
		zoomFactor = Math.max(Math.min(zoomFactor+0.001f*delta, 3f), 0.35f);
		centerX = Display.getWidth()/2 - zoomFactor*(TILE_WIDTH*(MAP_LENGTH+MAP_WIDTH)/4 + offsetX);
		centerY = Display.getHeight()/2 - zoomFactor*(TILE_HEIGHT/2 + TILE_HEIGHT*(MAP_LENGTH - MAP_WIDTH)/4 + offsetY);
                findZoom();
        }
	
	public void moveMap(float moveX, float moveY)
	{
		if((moveX == 0)&&(moveY == 0)) return;
                
                if(center.i() <= 0) {
                    moveX = moveX<0?moveX:0;
                    moveY = moveY>0?moveY:0;
                }
                else if(center.i() >= map.width()-1) {
                    moveX = moveX>0?moveX:0;
                    moveY = moveY<0?moveY:0;
                }
                if(center.j() <= 0) {
                    moveX = moveX<0?moveX:0;
                    moveY = moveY<0?moveY:0;
                }
                else if(center.j() >= map.height()-1) {
                    moveX = moveX>0?moveX:0;
                    moveY = moveY>0?moveY:0;
                }
		offsetX -= moveX;
		offsetY -= moveY;
		
		centerX = cX - zoomFactor*offsetX;
		centerY = cY - zoomFactor*offsetY;

                findBounds();
	}
        
        @SuppressWarnings("CallToThreadDumpStack")
        private void findZoom() {
            if(center == null) return;
            int x=-1,y=-1, t=-1;
            try {
                for (nil((x = center.i()) + (t = center.j())); map.get(x, t).isOnscreenLegacy(); x++);
                for (nil((t = center.i()) + (y = center.j())); map.get(t, y).isOnscreenLegacy(); y++);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            width = x - center.i() + FUDGE;
            height = y - center.j() + FUDGE;
            findBounds();
        }
        
        private void findBounds() {
            boundX[1] = width + center.i(); boundY[1] = height + center.j();
            boundX[0] = center.i() - width; boundY[0] = center.j() - height;            
        }
        
	public void renderMap()
	{	
		Iterator<Tile> i = map.iterator();
		while(i.hasNext()){
			Tile t = i.next();
			if(t.isOnscreen())
			{
                            t.updatePoints();
                            t.renderTile();
			}
		}   
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
	
	public static class Tile implements java.io.Serializable
	{
		private final int x1, x2, x3, x4;
		private final int y1, y2, y3, y4;
                private final float[] xs = {0,0,0,0}, ys = {0,0,0,0};
		public final int x, y, h, w;
                public final Coordinate co;
		private float red = 0, green = 0, blue = 0;
                private boolean onscreen = false;
		private final Map m;
                
		public Tile(Map _m, final int _x, final int _y, final int _i, final int _j, final int _w, final int _h, final float[] colors)
		{
			m = _m;
                        x = _x; y = _y; h = _h; w = _w;
                        co = new Coordinate(_i, _j);
                        
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
                
                public boolean isOnscreen() {
                    //return isOnscreenLegacy();
                    return isOnscreenNew();
                }
		
		public boolean isOnscreenLegacy()
		{
                    final float zf = m.zoomFactor,
                                cx = m.centerX,
                                cy = m.centerY;
                    return (((x*zf + cx)>-m.TILE_WIDTH*zf/2)
                            &&((x*zf + cx)< Display.getWidth()+m.TILE_WIDTH*zf/2)
                            &&((y*zf + cy) < Display.getHeight()+m.TILE_HEIGHT*zf/2)
                            &&((x1*zf + cy) > -m.TILE_HEIGHT*zf/2));
		}
                
                public boolean isOnscreenNew() {
                    return co.x >= m.boundX[0] && co.y >= m.boundY[0]
                        && co.x <= m.boundX[1] && co.y <= m.boundY[1];
                }
                
                private float transX(final float x) {
                    return m.zoomFactor * x + m.centerX;
                } 
                private float transY(final float y) {
                    return m.zoomFactor * y + m.centerY;
                }
                
                public void updatePoints () {
                    xs[0] = transX(x1);
                    ys[0] = transY(y1);
                    xs[1] = transX(x2);
                    ys[1] = transY(y2);
                    xs[2] = transX(x3);
                    ys[2] = transY(y3);
                    xs[3] = transX(x4);
                    ys[3] = transY(y4);
                    if(pnpoly(4, xs, ys, Display.getWidth()/2, Display.getHeight()/2))
                        m.center = this;
                }
		
		public void renderTile()
                {     
                    if(m.center == this)
                        GL11.glColor3f(1, 1, 1);
                    else GL11.glColor3f(red,green,blue);
                    
			GL11.glPushMatrix();
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(xs[0], ys[0]);
				GL11.glVertex2f(xs[1], ys[1]);
				GL11.glVertex2f(xs[2], ys[2]);
				GL11.glVertex2f(xs[3], ys[3]);
				GL11.glEnd();
			GL11.glPopMatrix();
                        
		}
                
                public int i() {
                    return co.x;
                }
                public int j() {
                    return co.y;
                }
                
                public boolean hasNeighbor(Direction d) {
                    return neighbor(d) != null;
                }
                
                public Tile neighbor(Direction d) {
                    try {
                        return m.map.get(co.x + d.dx, co.y + d.dy);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return null;
                    }
                }
                
            public enum Direction {
                N (0, 1),
                E (1, 0),
                S (N),
                W (E),
                NE(N, E),
                SW(S, W),
                NW(N, W),
                SE(S, E),
                NNW(N, NW),
                NNE(N, NE),
                ENE(E, NE),
                ESE(E, SE),
                SSE(NNW),
                SSW(NNE),
                WSW(ENE),
                WNW(ESE);
                
                private final int dx, dy;

                private Direction(final int _dx, final int _dy) {
                    dx = _dx; dy = _dy;
                }
                private Direction(Direction d) {
                    dx = -d.dx; dy = -d.dy;
                }
                private Direction(Direction d, Direction e) {
                    dx = d.dx+e.dx; dy = d.dy+e.dy;
                }
            }
        }
        
}

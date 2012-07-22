package halfzero;

import halfzero.util.GridList.Entry;
import java.util.Iterator;
import halfzero.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Color;

import static org.lwjgl.opengl.GL11.*;
import static halfzero.util.Functions.*;

public class HalfZero 
{   
	private static long lastFrame;
	private static final String GAME_TITLE = "HalfZero";
	private static final int FRAMERATE = 60, WIDTH=10, HEIGHT=10;
	private static boolean finished;
	
	private static enum State {INTRO, MAIN_MENU, GAME};
	private static State state = State.GAME;
	
        private static GridList<Tile> tiles;
        
	@SuppressWarnings("CallToThreadDumpStack")
	public static void main(String[] argv) 
	{
                tiles = new GridList<Tile>(WIDTH, HEIGHT);
                
                Iterator<GridList<Tile>.Entry> i = tiles.entryIterator();
                
                while(i.hasNext()) {
                    GridList<Tile>.Entry e = i.next();
                    Tile t = new Tile();
                    t.color = new Color(randInt(255), randInt(255), randInt(255));
                    e.setValue(t);
                }
                
		try
		{
			init();
			run();
		} catch(Exception e)
		{
			e.printStackTrace();
			Sys.alert(GAME_TITLE, "An error occured and the game will exit.");
		} finally
		{
			cleanup();
		}
	}
	
	private static void init()
	{
		Display.setTitle(GAME_TITLE);
		
		try
		{
			Display.setFullscreen(true);
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch (LWJGLException ex) 
		{
			Logger.getLogger(HalfZero.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		getDelta();
	}
	
	private static void run() 
	{
		while (!finished) 
		{
			Display.update();
			
			if(Display.isCloseRequested())
			{
				finished = true;
			}
			else if(Display.isVisible() || Display.isDirty())
			{
				switch(state)
				{
					case INTRO:
						break;
					case MAIN_MENU:
						break;
					case GAME:
						gameLogic(getDelta());
						gameRender();
						break;
				}
				Display.sync(FRAMERATE);
			}		
		}
	}

	private static void gameLogic(int delta) 
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			finished = true;
		}
		
		while(Keyboard.next())
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				finished = true;
			}
		}
	}
	
	private static int getDelta() 
	{
		long time = (Sys.getTime() * 1000) / Sys.getTimerResolution();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	private static void gameRender() 
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glColor3f(0.5f, 0.5f, 1.0f);
                
                
                
	}	
	
	private static void cleanup()
	{
		Display.destroy();
	}
}
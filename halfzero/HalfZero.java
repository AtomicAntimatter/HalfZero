package halfzero;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class HalfZero 
{   
	private static long lastFrame;
	private static final String GAME_TITLE = "HalfZero";
	private static final int FRAMERATE = 60, WIDTH=10, HEIGHT=10;
	private static boolean finished;
	
	private static enum State {INTRO, MAIN_MENU, GAME};
	private static State state = State.GAME;
	private static Map gameMap;
	private static final int MAP_LENGTH = 500, MAP_WIDTH = 500;

	
        
	@SuppressWarnings("CallToThreadDumpStack")
	public static void main(String argv[]) 
	{
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
			Display.setFullscreen(false);
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
		
		gameMap = new Map(MAP_LENGTH, MAP_WIDTH);	
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
		else if(Keyboard.isKeyDown(Keyboard.KEY_ADD))
		{
			gameMap.zoomMap(delta);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT))
		{
			gameMap.zoomMap(-delta);
		}
		
		while(Keyboard.next())
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				finished = true;
			}
		}
		
		gameMap.moveMap(
				(Keyboard.isKeyDown(Keyboard.KEY_LEFT)?delta/2:0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)?-delta/2:0),
				(Keyboard.isKeyDown(Keyboard.KEY_UP)?-delta/2:0) 
				+ (Keyboard.isKeyDown(Keyboard.KEY_DOWN)?delta/2:0));
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
		
		gameMap.renderMap();
		gameMap.renderCrosshair();
		
		glFlush();
	}	
	
	private static void cleanup()
	{
		Display.destroy();
	}
}
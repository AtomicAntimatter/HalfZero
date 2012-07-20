package halfzero;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class HalfZero 
{
	private static long lastFrame;
	private static final String GAME_TITLE = "HalfZero";
	private static final int FRAMERATE = 60;
	private static boolean finished;
	
	private static enum State {INTRO, MAIN_MENU, GAME};
	private static State state = State.GAME;
	private static Map gameMap;
	private static final int MAP_LENGTH = 10, MAP_WIDTH = 25;
	
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
			Display.setFullscreen(true);
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch (LWJGLException ex) 
		{
			Logger.getLogger(HalfZero.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
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
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor3f(0.5f, 0.5f, 1.0f);

		gameMap.renderMap();
		
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
		/* Draws a crosshair for reference*/
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
		/**/ 
		
		GL11.glFlush();
	}	
	
	private static void cleanup()
	{
		Display.destroy();
	}
}
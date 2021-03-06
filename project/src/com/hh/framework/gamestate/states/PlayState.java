package com.hh.framework.gamestate.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.hh.Game;
import com.hh.framework.*;
import com.hh.framework.GameObject.ObjectLayer;
import com.hh.framework.Handler.RemovalConditions;
import com.hh.framework.gamestate.GameState;
import com.hh.graphics.ArtAssets;
import com.hh.objects.*;
import com.hh.objects.bg.*;
import com.hh.objects.enemies.*;
import com.hh.objects.powerups.BalloonPack;
// import com.hh.objects.enemies.*;
import com.hh.objects.powerups.HydrogenMolecule;
import com.hh.objects.powerups.HydrogenTank;
import com.hh.objects.vfx.ControlsGraphic;

/**
 * COSC3550 Spring 2014 Created : Feb. 25, 2014 Last Updated : Mar. 19, 2014
 * Purpose: Defines the Main Play state for the game
 * 
 * @author Mark Schlottke & Charlie Beckwith
 */
@SuppressWarnings("unused")
/*TODO: Remember to remove before final build*/
public class PlayState extends GameState
{
  public static Handler handler;

  public static Camera cam;

  public Player player;

  private float playTime;

  private boolean genGround = false;

  private int xStart, yStartUp, yStartDown;

  private int cloudYMin = 0;

  private final int meter = 30;

  private ArtAssets art;

  private RenderHelper renderHelp;

  public boolean restart;

  private final int hydrogenCloudSize = 5;

  public int maxAltitude = 0;

  private int[] prevHydroCloud = { 0, 0 };

  private int hydroCloudCounter = 0;

  public PlayState()
  {
    handler = new Handler();
    handler.setRemovalConditions(Arrays.asList(RemovalConditions.OffscreenLeft,
        RemovalConditions.OffscreenTop, RemovalConditions.OffscreenBottom, RemovalConditions.Dead));
    cam = new Camera(0, 0);
    art = Game.getArtAssets();
    renderHelp = new RenderHelper();
    playTime = 0;
    restart = true;
  }

  public void tick()
  {
    if (restart)
    {

      restart();
      restart = false;
    }

    handler.tick();
    if (player.getAltitude() > maxAltitude)
    {
      maxAltitude = player.getAltitude();
    }
    cam.tick(player);
    generateScene();
  }

  public void render(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;

    g.setColor(new Color(109, 136, 253));
    g.fillRect(0, 0, Game.width, Game.height);

    g2d.translate(cam.getX(), cam.getY()); // begin cam
    // Begin Drawing
    handler.render(g);
    // End Drawing
    g2d.translate(-cam.getX(), -cam.getY()); // end cam

    displayHUD(g2d);
  }

  private void displayHUD(Graphics2D g)
  {
    int tankStart = 30;
    int tankEnd = tankStart + 139;
    int tankOffset = 0;
    int timerStart = 400;
    int altStart = 525;
    int balloonsStart = 705;

    if (!Game.isPaused() && player.getAltitude() >= 10)
    {
      playTime += GameTime.delta();
    }

    // Draw the HUD Background
    g.drawImage(art.hud, 0, 0, Game.width, Game.height, null);

    // Draw the Empty Hydrogen Tank
    g.drawImage(art.emptytank, tankStart, 15, 140, 30, null);

    // Fill in the Hydrogen Tank with the Hydrogen Levels
    float percent = 70 - 70 * player.getHydrogenLevelPercent();
    for (int i = 70; i >= percent + 1; i--)
    {
      g.drawImage(art.fulltank_sheet.getFrame(i), tankEnd - (2 * (70 - i)), 15, 2, 30, null);
    }

    // Write the Percentage of Hydrogen in the Tank
    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.setColor(Color.black);
    int displayedPercent = (int) (100 * player.getHydrogenLevelPercent());
    if (displayedPercent / 100 >= 1)
      tankOffset = 45;
    else if (displayedPercent / 10 >= 1)
      tankOffset = 55;
    else
      tankOffset = 60;

    renderHelp.outlinedText(g, new Font("Arial", Font.BOLD, 20),
        (int) (100 * player.getHydrogenLevelPercent()) + "%", 0.6f, Color.black, Color.white,
        tankStart + tankOffset, 36);

    // Draw Overall Score
    renderHelp.outlinedText(g, new Font("Arial", Font.BOLD, 28), "" + player.calculateScore(),
        1.25f, Color.black, Color.white, timerStart - 180, 37);

    // Draw the Flight time
    Date date = new Date((long) (playTime * 1000));
    String formattedDate = new SimpleDateFormat("mm:ss").format(date);
    renderHelp.outlinedText(g, new Font("Arial", Font.BOLD, 28), formattedDate, 1.25f, Color.black,
        Color.white, timerStart, 37);

    // Draw the Altitude
    int altitude = player.getAltitude();
    String altitudeStr = altitude + "m";
    renderHelp.outlinedText(g, new Font("Arial", Font.BOLD, 28), "ALT:" + altitudeStr, 1.25f,
        Color.black, Color.white, altStart, 37);

    // Draw the Extra Balloon Count
    g.drawImage(art.balloon_sheet.getFrame(0), balloonsStart, 15, 20, 30, null);
    renderHelp.outlinedText(g, new Font("Arial", Font.BOLD, 28), ":" + player.getExtraBalloons(),
        1.25f, Color.black, Color.white, balloonsStart + 23, 37);
  }

  /**
   * Clears the necessary objects and reinitializes them to enable a restart
   * of the game
   */
  public void restart()
  {
    prevHydroCloud[0] = 0;
    prevHydroCloud[1] = 0;
    handler.clearObjects();
    player = new Player(100, 380, 55, 64, new Vector2D(0, 50));
    handler.addObject(player);
    cam = new Camera(0, 0);
    cam.tick(player);
    playTime = 0;
    maxAltitude = 0;
    xStart = (int) cam.getX() - Game.width;
    yStartDown = cloudYMin;
    generateScene();
    handler.addObject(new ControlsGraphic(0,0,ObjectLayer.middleground));
  }

  private void generateScene()
  {
    int offset = 75;
    Rectangle sceneWindow = new Rectangle((int) (player.getX() - Game.width - offset),
        (int) (player.getY() - Game.height - offset), (int) (offset + Game.width) * 2,
        (int) (offset + Game.height) * 2);
    handler.setSceneWindow(sceneWindow);
    int preYStartUp = yStartUp;
    int preYStartDown = yStartDown;

    for (float i = sceneWindow.x; i < sceneWindow.x + sceneWindow.width; i += 75)
    {
      yStartUp = preYStartUp;
      yStartDown = preYStartDown;

      if (i > xStart)
      {
        generateGround((int) i);
        for (float j = (sceneWindow.y + sceneWindow.height) < cloudYMin ? (sceneWindow.y + sceneWindow.height)
            : cloudYMin; j > sceneWindow.y; j -= meter)
        {

          generateCloud((int) i, (int) j);

          generateEnemy((int) i, (int) j);
          yStartUp = (int) j;
        }
        xStart += 70;
      } else if (player.getVelocity().dy < 0) // Player Rising
      {
        if (player.getY() < 400 - Game.height)
        {
          genGround = true;
        }

        while (yStartUp > sceneWindow.y)
        {
          generateCloud((int) i, (int) yStartUp);
          generateEnemy((int) i, (int) yStartUp);
          yStartUp -= meter;

          if (yStartDown - yStartUp >= Game.height * 2.5)
          {
            yStartDown -= meter;
          }
        }
      } else if (player.getVelocity().dy > 0) // Player Falling
      {
        if (genGround && player.getY() > 400 - Game.height)
        {
          if ((i + 75) >= (sceneWindow.x + sceneWindow.width))
          {
            genGround = false;
          }

          generateGround((int) i);
        }

        while (yStartDown < (sceneWindow.y + sceneWindow.height) && yStartDown < cloudYMin)
        {
          generateCloud((int) i, (int) yStartDown);
          yStartDown += meter;

          if (yStartDown - yStartUp >= Game.height * 2.5)
          {
            yStartUp += meter;
          }
        }
      }
    }
  }

  private void generateGround(int x)
  {
    handler.addObject(new Ground(x - 5, 400, 75, 300));
  }

  private void generateCloud(int x, int y)
  {

    float xVel = -15 * (Game.Rand.nextInt(3) + 1);
    switch (Game.Rand.nextInt(100))
    {
    case 0:
    case 1:
    case 2:
      if (hydroCloudCounter > 5)
      {
        generateHydrogenCloud(x, y);
        prevHydroCloud[0] = x;
        prevHydroCloud[1] = y;
        hydroCloudCounter = 0;
      }
      handler.addObject(new Cloud(x, y, 192, 96, new Vector2D(xVel, 0), true, false));
      hydroCloudCounter++;
      break;
    case 3:
    case 4:
    case 5:
      handler.addObject(new Cloud(x, y, 192, 96, new Vector2D(xVel, 0), true, false));
      break;
    case 6:
    case 7:
    case 8:
      handler.addObject(new Cloud(x, y, 192, 96, new Vector2D(xVel, 0), false, false));
      break;
    }
  }

  private void generateHydrogenCloud(int x, int y)
  {

    //Generating a random number of clouds spaced out by a random amount. 
    int sizeVariable = Game.Rand.nextInt(25) + 25;
    for (int t = 0; t < 720; t++)
    {
      if (t % sizeVariable == 0)
      {
        handler.addObject(new HydrogenMolecule(x, y, 25, 25));
      }
    }

    if (Game.Rand.nextInt(35) == 1)
    {
      handler.addObject(new HydrogenTank(x, y, 90, 90));
    } else if (Game.Rand.nextInt(20) == 1)
    {
      handler.addObject(new BalloonPack(x, y, 90, 90));
    }

  }

  private void generateEnemy(int x, int y)
  {
    float xVel = -15 * (Game.Rand.nextInt(3) + 1);

    switch (Game.Rand.nextInt(400))
    {
    case 0:
    case 1:
    case 2:
      handler.addObject(new Bird(x, y, 48, 48, new Vector2D(xVel, 0)));
      break;
    case 3:
      handler.addObject(new Plane(x, y, 250, 128, new Vector2D(xVel * 3, 0)));
    }
  }

  public Camera getCamera()
  {
    return cam;
  }

  public float getPlayTime()
  {
    return playTime;
  }

}

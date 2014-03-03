package com.hh.framework.gamestate.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import com.hh.Game;
import com.hh.framework.Camera;
import com.hh.framework.GameObject;
import com.hh.framework.Handler;
import com.hh.framework.Vector2D;
import com.hh.framework.gamestate.GameState;
import com.hh.objects.*;
import com.hh.objects.bg.*;
import com.hh.objects.enemies.*;
import com.hh.objects.powerups.HydrogenMolecule;

public class PlayState extends GameState
{
  public static Handler handler;
  public static Camera cam;

  public Player player;
  private int xStart, yStart;
  private int cloudYMin = 300 - (30 * 10);
  private final int meter = 30;

  public PlayState()
  {
    handler = new Handler();
    cam = new Camera(0, 0);
  }

  public void tick()
  {
    float origY = player.getY();
    removeOffscreenObjects();

    handler.tick();
    cam.tick(player);

    float screenBottom = -cam.getY() + Game.HEIGHT;
    float screenTop = -cam.getY() - Game.HEIGHT * 2;
    // TODO: Generate Clouds in the direction of the players Y velocity
    /*for (float i = -cam.getX() - Game.WIDTH; i < xStart; i += 75)
    {
      if (player.getVelocity().DY > 0)
      {
      } else
      {
      }
    }*/

    for (int i = xStart; i < (-cam.getX() + Game.WIDTH); i += 75)
    {
      generateGround(xStart);

      for (float j = screenBottom < cloudYMin ? screenBottom : cloudYMin; j > screenTop; j -= meter)
      {
        generateCloud(i, (int) j);
      }

      xStart += 75;
    }

    // TODO: Generate the Enemies
  }

  public void render(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;

    g.setColor(new Color(109, 136, 253));
    g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

    g2d.translate(cam.getX(), cam.getY()); // begin cam
    // Begin Drawing
    handler.render(g);
    // End Drawing
    g2d.translate(-cam.getX(), -cam.getY()); // end cam
  }

  /**
   * Clears the necessary objects and reinitializes them to enable a restart
   * of the game
   */
  public void restart()
  {
    handler.clearObjects();
    player = new Player(100, 100, 64, 64, new Vector2D(0, 50));
    handler.addObject(player);
    cam = new Camera(0, 0);

    xStart = (int) cam.getX() - Game.WIDTH;
    yStart = cloudYMin;

    for (int i = xStart; i < (xStart + Game.WIDTH * 3); i += 75)
    {
      generateGround(i);
      for (float j = yStart; j > yStart - Game.HEIGHT; j -= meter)
      {
        generateCloud(i, (int) j);
      }
    }

    handler.addObject(new Bird(-cam.getX(), -cam.getY(), 48, 48, new Vector2D(0, 0)));

    xStart = xStart + Game.WIDTH * 3;
    yStart = yStart - Game.HEIGHT;
  }

  private void removeOffscreenObjects()
  {
    int left = (int) (player.getX() - Game.WIDTH);

    for (GameObject go : handler.getObjects())
    {
      // Remove objects outside of the scene
      if ((go.getX() + go.getWidth()) < left || !go.isAlive())
      {
        handler.removeObject(go);
      }
    }
  }

  private void generateGround(int x)
  {
    handler.addObject(new Ground(x, 400, 75, 300));
  }

  private void generateCloud(int x, int y)
  {
    Random rand = new Random();

    switch (rand.nextInt(30))
    {
    case 2:
      handler.addObject(new HydrogenMolecule(x + 10, y, 50, 50));
      handler.addObject(new Cloud(x, y, 192, 96, new Vector2D(-15 * (rand.nextInt(3) + 1), 0),
          true, false));
      break;
    case 3:
      handler.addObject(new Cloud(x, y, 192, 96, new Vector2D(-15 * (rand.nextInt(3) + 1), 0),
          true, false));
      break;
    case 8:
      handler.addObject(new Cloud(x, y, 192, 96, new Vector2D(-15 * (rand.nextInt(3) + 1), 0),
          false, false));
      break;
    }
  }

  public Camera getCamera()
  {
    return cam;
  }

}

package com.hh.objects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

import com.hh.Game;
import com.hh.framework.*;
import com.hh.framework.gamestate.states.PlayState;
import com.hh.graphics.Animation;
import com.hh.graphics.ArtAssets;
import com.hh.graphics.SpriteSheet.spriteID;
import com.hh.input.KeyBinding;
import com.hh.input.KeyInput;

/**
 * COSC3550 Spring 2014 Homework 3
 * 
 * Created : Feb. 7, 2014 Last Updated : Feb. 11, 2014 Purpose: Defines the Dust
 * Bunny
 * 
 * @author Mark Schlottke
 */
@SuppressWarnings("unused")
public class Player extends GameObject
{
  private final float GRAVITY = 200f;

  private Animation RIGHT, CURRENT;
  private ArtAssets art;
  private float BUOYANCY;
  private Color HUE;
  private float startx, starty;
  private LinkedList<String> debugOptions;

  public Player(float x, float y, int width, int height, Vector2D v)
  {
    super(x, y, width - 30, height, v, ObjectID.Player, ObjectLayer.foreground);
    startx = x;
    starty = y;
    ALIVE = true;
    Random rand = new Random();
    HUE = new Color(50 + rand.nextInt(200), 50 + rand.nextInt(200), 50 + rand.nextInt(200));
    BUOYANCY = 0.0f;
    art = Game.getArtAssets();
    initAnimations();
  }

  public void tick()
  {
    if (ALIVE)
    {
      boolean collision = collision();

      if (KeyInput.KEYSDOWN.contains(KeyBinding.INFLATE.VALUE()))
      {
        BUOYANCY -= 5.1f;
      }

      if (KeyInput.KEYSDOWN.contains(KeyBinding.DEFLATE.VALUE()))
      {
        BUOYANCY += 1.1f;
      }

      if (BUOYANCY > 0)
      {
        BUOYANCY = 0;
      }
      if (BUOYANCY < -(GRAVITY * 2))
      {
        BUOYANCY = -(GRAVITY * 2);
      }

      if (V.DX < 500 && !collision)
      {
        V.DX += 1;
      }

      V.DY = BUOYANCY + GRAVITY;

      X += V.DX * GameTime.delta();
      Y += V.DY * GameTime.delta();

      CURRENT = RIGHT;
      CURRENT.runAnimation();

      // Simulate a slow leak in the balloon
      BUOYANCY += 1;
    }
  }

  public boolean collision()
  {
    boolean col = false;

    for (GameObject go : PlayState.handler.getObjects())
    {
      if (go != this && go.getID() == ObjectID.Tile && (Y + (HEIGHT / 2) - 14) >= go.getY()
          && X > go.getX() && X < go.getX() + go.getWidth())
      {
        col = true;

        Y = (go.getY() - (HEIGHT / 2) + 14);
        V.DY = 0;

        if (V.DX > 0)
          V.DX -= (V.DX * 0.01);
      }
    }

    return col;
  }

  public void render(Graphics g)
  {
    if (ALIVE)
    {
      Graphics2D g2d = (Graphics2D) g.create();

      // BufferedImage image = art.hueImg(CURRENT.getAnimationFrame(),
      // WIDTH, HEIGHT, HUE);
      if (Game.isDebug())
        debugOptions(g2d);
      g2d.drawImage(CURRENT.getAnimationFrame(), (int) (X - (WIDTH / 2)), (int) (Y - (HEIGHT / 2)),
          WIDTH + HEIGHT / 3, HEIGHT, null);
    }
  }

  private void debugOptions(Graphics2D g2d)
  {
    initDebug();
    g2d.setColor(Color.BLACK);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    float relativeZeroX = -PlayState.cam.getX();
    float relativeZeroY = -PlayState.cam.getY();
    int row = 1;
    for (String x : debugOptions)
    {
      g2d.drawString(x, relativeZeroX, relativeZeroY + 11 * row);
      row++;
    }

  }

  private void initDebug()
  {
    debugOptions = new LinkedList<String>();
    String BouyancyDebug = new String().concat("Bouyancy = " + (int) BUOYANCY);
    String PositionDebug = new String().concat("XPosition: " + (int) X + " || YPosition: "
        + (int) Y);
    String VelocityDebug = new String().concat("XVelocity: " + (int) V.DX);
    String XYOffset = new String().concat("XOffset = " + PlayState.cam.getX() + " || YOffset = "
        + PlayState.cam.getY());
    debugOptions.add(BouyancyDebug);
    debugOptions.add(PositionDebug);
    debugOptions.add(VelocityDebug);
    debugOptions.add(XYOffset);
  }

  /**
   * initialize Animations
   */
  private void initAnimations()
  {

    RIGHT = new Animation(10, art.getSpriteFrame(spriteID.HANK, 0), art.getSpriteFrame(
        spriteID.HANK, 1));
    CURRENT = new Animation(3, art.getSpriteFrame(spriteID.HANK, 0));
  }
}

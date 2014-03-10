package com.hh.framework.gamestate.states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.hh.Game;
import com.hh.framework.Camera;
import com.hh.framework.GameObject;
import com.hh.framework.GameTime;
import com.hh.framework.Handler;
import com.hh.framework.Vector2D;
import com.hh.framework.GameObject.ObjectID;
import com.hh.framework.gamestate.GameState;
import com.hh.graphics.ArtAssets;
import com.hh.objects.*;
import com.hh.objects.bg.*;
import com.hh.objects.enemies.*;
import com.hh.objects.powerups.HydrogenMolecule;

public class PlayState extends GameState
{
  public static Handler handler;
  public static Camera cam;

  public Player player;

  private float playTime;
  private int xStart, yStartUp, yStartDown;
  private int cloudYMin = 0;
  private final int meter = 30;
  private ArtAssets art;

  public PlayState()
  {
    handler = new Handler();
    cam = new Camera(0, 0);
    art = Game.getArtAssets();
    playTime = 0;
  }

  public void tick()
  {
    removeOffscreenObjects();

    handler.tick();
    cam.tick(player);

    float screenBottom = -cam.getY() + Game.HEIGHT;
    float screenTop = -cam.getY() - Game.HEIGHT;
    float screenLeft = -cam.getX() - Game.WIDTH;
    int preYStartUp = yStartUp;
    int preYStartDown = yStartDown;

    for (float i = xStart; i > screenLeft; i -= 75)
    {
      if (player.getVelocity().DY < 0) // Player Rising
      {
        yStartUp = preYStartUp;
        yStartDown = preYStartDown;
        while (yStartUp > screenTop)
        {
          generateCloud((int) i, (int) yStartUp);
          yStartUp -= meter;

          if (yStartDown - yStartUp >= Game.HEIGHT * 2)
          {
            yStartDown -= meter;
          }
        }
      } else if (player.getVelocity().DY > 0) // Player Falling
      {
        yStartUp = preYStartUp;
        yStartDown = preYStartDown;
        while (yStartDown < screenBottom && yStartDown < cloudYMin)
        {
          generateCloud((int) i, (int) yStartDown);
          yStartDown += meter;

          if (yStartDown - yStartUp >= Game.HEIGHT * 2)
          {
            yStartUp += meter;
          }
        }
      }
    }
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

    displayHUD(g2d);
  }

  private void displayHUD(Graphics2D g)
  {
    int tankStart = 30;
    int tankEnd = tankStart + 139;
    int timerStart = Game.WIDTH/2 - 50;
    int balloonsStart = 705;

    if (!Game.isPaused())
    {
      playTime += GameTime.delta();
    }

    // Draw the HUD Background
    g.drawImage(art.hud, 0, 0, Game.WIDTH, Game.HEIGHT, null);
    
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
      g.drawString((int) (100 * player.getHydrogenLevelPercent()) + "%", tankStart + 45, 36);
    else if (displayedPercent / 10 >= 1)
      g.drawString((int) (100 * player.getHydrogenLevelPercent()) + "%", tankStart + 55, 36);
    else
      g.drawString((int) (100 * player.getHydrogenLevelPercent()) + "%", tankStart + 60, 36);
    
    // Draw the Play time
    Date date = new Date((long)(playTime*1000));
    String formattedDate = new SimpleDateFormat("mm:ss").format(date);
    renderText(g, new Font("Arial", Font.BOLD, 28), formattedDate, timerStart, 37, Color.black, Color.white);

    // Draw the Extra Balloon Count
    g.drawImage(art.balloon_sheet.getFrame(0), balloonsStart, 15, 20, 30, null);
    renderText(g, new Font("Arial", Font.BOLD, 28), ":"+player.getExtraBalloons(), balloonsStart + 23, 37, Color.black, Color.white);
  }
  
  private void renderText(Graphics2D g2d, Font font, String line, int x, int y, Color outline, Color textColor)
  {
    g2d.setFont(font);

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    FontRenderContext fontRendContext = g2d.getFontRenderContext();
    if (line.length() > 0)
    {
      TextLayout text = new TextLayout(line, font, fontRendContext);
      Shape shape = text.getOutline(null);
      AffineTransform affineTransform = new AffineTransform();
      affineTransform = g2d.getTransform();
      affineTransform.translate(x, y);
      Graphics2D g2dd = (Graphics2D) g2d.create();
      g2dd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2dd.transform(affineTransform);
      g2dd.setColor(outline);
      g2dd.setStroke(new BasicStroke(3f));
      g2dd.draw(shape);
      g2dd.setClip(shape);
    }

    g2d.setColor(textColor);
    g2d.setStroke(new BasicStroke(1.0f));
    g2d.drawString(line, x, y);
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
    cam.tick(player);
    playTime = 0;

    xStart = (int) cam.getX() - Game.WIDTH;
    float screenTop = -cam.getY() - Game.HEIGHT;

    for (int i = xStart; i < (xStart + Game.WIDTH * 3); i += 75)
    {
      generateGround(i);

      yStartUp = yStartDown = cloudYMin;
      while (yStartUp > screenTop)
      {
        generateCloud(i, (int) yStartUp);
        yStartUp -= meter;
      }
    }

    xStart = xStart + Game.WIDTH * 3;
  }

  private void removeOffscreenObjects()
  {
    int left = (int) (player.getX() - Game.WIDTH);
    int top = (int) (player.getY() - Game.HEIGHT * 2);
    int bottom = (int) (player.getY() + Game.HEIGHT * 2);

    for (GameObject go : handler.getObjects())
    {
      // Remove objects outside of the scene
      if ((go.getX() + go.getWidth()) < left || !go.isAlive())
      {
        handler.removeObject(go);
      }

      if (player.getVelocity().DY > 0 && go.getY() < top) // Player Falling
      {
        handler.removeObject(go);
      } else if (player.getVelocity().DY < 0 && go.getY() > bottom && go.getID() != ObjectID.Ground) // Player Rising
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
    float xVel = -15 * (rand.nextInt(3) + 1);

    switch (rand.nextInt(100))
    {
    case 0:
    case 1:
    case 2:
      handler.addObject(new HydrogenMolecule(x + 10, y, 50, 50));
      handler.addObject(new Cloud(x, y, 192, 96, new Vector2D(xVel, 0), true, false));
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
    case 9:
      //handler.addObject(new Bird(x, y, 48, 48, new Vector2D(-30, 0)));
      break;
    }
  }

  public Camera getCamera()
  {
    return cam;
  }

}

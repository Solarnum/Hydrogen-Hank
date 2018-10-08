package com.hh.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.List;

import com.hh.Game;
import com.hh.framework.*;
import com.hh.graphics.ArtAssets;

/**
 * COSC3550 Spring 2014
 * 
 * Created : Mar. 5, 2014 
 * Last Updated : Mar. 5, 2014 
 * Purpose: Defines the Tab for the High Scores state
 * 
 * @author Mark Schlottke
 */
public class ScoreTab extends GameObject
{
  private BufferedImage IMG, IMGSelected;
  private boolean selected;
  private ArtAssets art;
  private RenderHelper renderHelp = new RenderHelper();
  private Font font;
  String text = "";
  Score.ScoreType ScoreType;

  public ScoreTab(String text, Score.ScoreType ScoreType, float x, float y, int width, int height,
      boolean selected)
  {
    super(x, y, width, height, ObjectID.Tile, ObjectLayer.background);
    alive = true;
    art = Game.getArtAssets();
    IMG = art.tab_sheet.getFrame(1);
    IMGSelected = art.tab_sheet.getFrame(0);
    font = new Font("Arial", Font.BOLD, 20);
    this.selected = selected;
    this.text = text;
    this.ScoreType = ScoreType;
  }

  @Override
  public void tick()
  {
  }

  @Override
  public void render(Graphics g)
  {
    if (alive)
    {
      List<Score> scores = Game.getScoreKeeper().getScores(ScoreType);
      Graphics2D g2d = (Graphics2D) g;
      FontMetrics metrics = g.getFontMetrics(font);
      int textX = (int) (x - (metrics.stringWidth(text) / 2));
      int textY = (int) y - (metrics.getHeight() / 4);
      int position = 150;

      if (!selected)
      {
        g2d.drawImage(IMG, (int) (x - width / 2), (int) (y - height / 2), (int) width,
            (int) height, null);
        renderHelp.outlinedText(g2d, font, text, 1f, Color.black, Color.white, textX, textY);
      } else
      {
        g2d.drawImage(IMGSelected, (int) (x - width / 2), (int) (y - height / 2), (int) width,
            (int) height, null);
        renderHelp.outlinedText(g2d, font, text, 1f, Color.black, Color.red, textX, textY);
        for (int i = 0; i < scores.size() && i < 8; i++)
        {
          Score score = scores.get(i);
          String entry = score.getName() + " : " + String.valueOf(score.getValue());
          int entryX = ((Game.width / 2) - (metrics.stringWidth(entry) / 2));
          renderHelp.outlinedText((Graphics2D) g, font, entry, 0.9f, Color.black, Color.DARK_GRAY,
              entryX, position);
          position += 50;
        }
      }
    }
  }

  @Override
  public Area boundingBox()
  {
    return new Area(new Rectangle((int) (x - width / 2), (int) (y - height / 2), (int) width,
        (int) height));
  }

  public boolean isHoveringOver()
  {
    Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
    mouseLoc.x -= Game.getPosition().x;
    mouseLoc.y -= Game.getPosition().y + 25; // 25 pixels is roughly the size of the top of the
                                             // window

    return this.boundingBox().contains(mouseLoc);
  }

  public boolean isSelected()
  {
    return this.selected;
  }

  public void setSelected(boolean selected)
  {
    this.selected = selected;
  }
}

package com.hh.states;

import java.awt.Graphics;

import com.hh.Game;
import com.hh.framework.GameState;
import com.hh.graphics.ArtAssets;

public class PauseState extends GameState
{
  private ArtAssets art = Game.getArtAssets();
  public void tick()
  {
  }

  public void render(Graphics g)
  {
    Game.playState.render(g);
    g.drawImage(art.pauseScreen,
        (Game.WIDTH / 2 - art.pauseScreen.getWidth() / 2), (Game.HEIGHT / 2 - 150), null);
  }

}

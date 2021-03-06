package com.hh.framework.gamestate.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.hh.Game;
import com.hh.framework.RenderHelper;
import com.hh.framework.gamestate.GameState;
import com.hh.graphics.ArtAssets;

/**
 * COSC3550 Spring 2014
 * 
 * Created : Feb. 25, 2014 
 * Last Updated : Mar. 19, 2014 
 * Purpose: Defines the Paused state for the game
 * 
 * @author Mark Schlottke & Charlie Beckwith
 */
public class PauseState extends GameState
{
	private ArtAssets art = Game.getArtAssets();
	private RenderHelper renderHelp = new RenderHelper();
	private float boxHeight = 0;
	private float boxWidth = 0;

	public void tick()
	{
		if (boxHeight < Game.height / 2)
			boxHeight += Game.height / 25;
		else
			boxHeight = Game.height;

		if (boxWidth < Game.width / 2)
			boxWidth += Game.width / 25;
		else
			boxWidth = Game.width;
	}

	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		// TODO: Find a better fix for the shaky pause screen...
		Game.playState.render(g); // Solves the shaky pause screen issue

		renderHelp.tintedBox(g2d, Color.black, 0.7f, (int) (Game.width / 2 - boxWidth),
		    (int) (Game.height / 2 - boxHeight), (int) (boxWidth * 2), (int) (boxHeight * 2));

		if (boxWidth >= Game.width / 2 && boxHeight >= Game.height / 2)
		{
			g.drawImage(art.pauseScreen, (Game.width / 2 - art.pauseScreen.getWidth() / 2),
			    (Game.height / 2 - 150), null);
		}
	}

}

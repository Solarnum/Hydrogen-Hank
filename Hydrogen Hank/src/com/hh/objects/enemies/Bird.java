/**
 * 
 */
package com.hh.objects.enemies;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import com.hh.Game;
import com.hh.framework.GameTime;
import com.hh.framework.Vector2D;
import com.hh.graphics.Animation;
import com.hh.graphics.ArtAssets;
import com.hh.graphics.SpriteSheet.spriteID;
import com.hh.objects.Enemy;

/**
 * COSC3550 Spring 2014
 * 
 * Created : Feb. 28, 2014 
 * Last Updated : Mar. 19, 2014 
 * Purpose: Defines a bird enemy
 * 
 * @author Mark Schlottke & Charlie Beckwith
 */
public class Bird extends Enemy
{
	private Animation ANIM;
	private ArtAssets art;

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param v
	 */
	public Bird(float x, float y, int width, int height, Vector2D v)
	{
		super(x, y, width, height, v, EnemyType.Bird);
		art = Game.getArtAssets();
		initAnimations();
		alive = true;
	}

	@Override
	public void tick()
	{
		// this.X = -PlayState.cam.getX()+400;
		// this.Y = -PlayState.cam.getY()+350;
		x += v.dx * GameTime.delta();
		ANIM.runAnimation();
	}

	@Override
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		if (alive)
		{
			g2d.drawImage(ANIM.getAnimationFrame(), (int) center.getX(), (int) center.getY(),
			    (int) width, (int) height, null);
		}

		super.render(g);
	}

	@Override
	public Area boundingBox()
	{
		float offsetX = width * 0.0412f;
		float offsetY = height * 0.25f;

		return new Area(new Rectangle((int) (center.getX() + offsetX), (int) (center.getY() + offsetY),
		    (int) (width - offsetX * 2), (int) (height - offsetY * 2)));
	}

	/**
	 * initialize Animations
	 */
	private void initAnimations()
	{
		ANIM = new Animation(3, art.getSpriteFrame(spriteID.BIRD, 0), art.getSpriteFrame(spriteID.BIRD,
		    1), art.getSpriteFrame(spriteID.BIRD, 2), art.getSpriteFrame(spriteID.BIRD, 3));
	}

}

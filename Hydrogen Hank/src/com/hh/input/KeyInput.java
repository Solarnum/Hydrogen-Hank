package com.hh.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import com.hh.*;

/**
 * COSC3550 Spring 2014 Homework 3
 * 
 * Created : Feb. 7, 2014 Last Updated : Feb. 10, 2014 Purpose: Gives the player control over
 * aspects of the game
 * 
 * @author Mark Schlottke
 */
public class KeyInput extends KeyAdapter
{
	public static LinkedList<Integer> keysDown;

	/**
	 * Initializes the list of keys held down
	 */
	public KeyInput()
	{
		keysDown = new LinkedList<Integer>();
	}

	/**
	 * Determines the action to perform upon a key press
	 */
	public void keyPressed(KeyEvent e)
	{
		if (!keysDown.contains(e.getKeyCode()))
		{
			keysDown.add(e.getKeyCode());
		}

		if (e.getKeyCode() == KeyBinding.RESTART.VALUE())
		{
			Game.doRestart();
		}
	}

	/**
	 * Determines the action to perform upon a key release
	 */
	public void keyReleased(KeyEvent e)
	{
		if (keysDown.contains(e.getKeyCode()))
		{
			keysDown.remove((Integer) e.getKeyCode());
		}

		if (e.getKeyCode() == KeyBinding.PAUSE.VALUE())
		{
			Game.togglePause();
		}
	}
}

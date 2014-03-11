package com.hh.framework;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * COSC3550 Spring 2014 Homework 3
 * 
 * Created : Feb. 7, 2014 Last Updated : Feb. 8, 2014 Purpose: Helper class to handle to the
 * gameobject list
 * 
 * @author Mark Schlottke
 */
public class Handler
{
	private LinkedGameObjects objects = new LinkedGameObjects();
	private ArrayList<GameObject> toRemove = new ArrayList<GameObject>();

	private GameObject tempObj;

	/**
	 * Advances all of the gameobjects in the list
	 */
	public void tick()
	{
		for (int i = 0; i < objects.size(); i++)
		{
			tempObj = objects.get(i);
			tempObj.tick();
		}
	}

	/**
	 * Renders and Draws all of the gameobjects in the list then removes dead gameobjects from the
	 * list
	 */
	public void render(Graphics g)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			tempObj = objects.get(i);
			tempObj.render(g);
		}

		for (GameObject go : toRemove)
		{

			objects.remove(go);

		}
		toRemove.clear();
	}

	/**
	 * Removes all of the gameobjects from the list
	 */
	public void clearObjects()
	{
		objects.clear();
	}

	/**
	 * Inserts the gameobject at the specified index of the list
	 * 
	 * @param object
	 *          - gameobject to insert
	 * @param index
	 *          - position to insert it at
	 */
	public void insertObject(GameObject object, int index)
	{
		objects.add(index, object);

	}

	/**
	 * Adds a gameobject to the end of the list
	 * 
	 * @param object
	 *          - gameobject to add
	 */
	public void addObject(GameObject object)
	{
		objects.add(object);

	}

	/**
	 * Removes a gameobject from the list
	 * 
	 * @param object
	 *          - gameobject to remove
	 */
	public void removeObject(GameObject object)
	{

		toRemove.add(object);
	}

	/**
	 * Gets the gameobject list Used for collision detection primarily
	 */
	public LinkedList<GameObject> getObjects()
	{
		return objects;
	}
}

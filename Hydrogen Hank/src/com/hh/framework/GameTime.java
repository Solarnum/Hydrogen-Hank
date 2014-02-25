package com.hh.framework;

/**
 * COSC3550 Spring 2014 Homework 3
 * 
 * Created : Feb. 7, 2014
 * Last Updated : Feb. 7, 2014
 * Purpose: Maintains the runtime of the game and provides the 
 * delta time for movement of gameobjects
 * 
 * @author Mark Schlottke
 */
public class GameTime
{
  private static long START, PREVIOUS, CURRENT;
  private static final float NS = 1000000000.0f;

  /**
   * Initializes the gametime object
   */
  public static void start()
  {
    START = CURRENT = PREVIOUS = System.nanoTime();
  }

  /**
   * Updates the gametime object to the current time
   */
  public static void update()
  {
    PREVIOUS = CURRENT;
    CURRENT = System.nanoTime();
  }

  /**
   * Gets the current runtime of the game
   * @return
   */
  public static float runtime()
  {
    return (CURRENT - START) / NS;
  }

  /**
   * Gets the difference in time between the the last update and now
   * @return
   */
  public static float delta()
  {
    return (CURRENT - PREVIOUS) / NS;
  }
}

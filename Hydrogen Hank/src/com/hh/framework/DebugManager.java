/**
 * COSC3550 Spring 2014
 * 
 * Created : March. 20, 2014 
 * Last Updated : Mar. 21, 2014 
 * Purpose: Debug Manager for different debug options in game.
 * 
 * @author Mark Schlottke & Charlie Beckwith
 */


package com.hh.framework;

import com.hh.Game;

public class DebugManager
{

  public static boolean showBounds = false;

  public static boolean debugMode = false;
  public static boolean showBoundsAll=false;
  public static boolean infiniteHelium = false;
  public static boolean infiniteBalloons = false;
  public static boolean muteSound = false;

  public DebugManager()
  {
    if (Game.debugOptions().contains("-showbounds"))
      showBounds = true;
    if(Game.debugOptions().contains("-showbounds-all")){
    	showBounds = true;
    	showBoundsAll = true;
    }
    if (Game.debugOptions().contains("-inf_helium"))
      infiniteHelium = true;
    if (Game.debugOptions().contains("-mute"))
      muteSound = true;
    if (Game.debugOptions().contains("-inf_balloons"))
      infiniteBalloons = true;
    if (Game.debugOptions().contains("Info"))
      debugMode = true;
    System.out.println("DebugManager called " + showBounds);
  }

}

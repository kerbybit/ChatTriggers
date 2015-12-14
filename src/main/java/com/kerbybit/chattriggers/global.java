package com.kerbybit.chattriggers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.glass.events.KeyEvent;

public class global {
	public static String lastmsg = "";
	public static int tick = 0;
	public static List<String> chatQueue = new ArrayList<String>();
	public static int chatDelay = 0;
	public static List<String> commandQueue = new ArrayList<String>();
	
	public static List<String> killfeed = new ArrayList<String>();
	public static List<Integer> killfeedDelay = new ArrayList<Integer>();
	
	public static List<String> notify = new ArrayList<String>();
	public static List<Integer> notifyAnimate = new ArrayList<Integer>();
	public static List<Float> notifyOffset = new ArrayList<Float>();
	
	public static boolean debug = false;

	
	//////////////////Trigger matrix////////////////////
	// layout
	// get(0) = trigger type (chat/key press/minecraft event)
	// get(1) = trigger (eg: "{s}Guild >" or "ctrl+b")
	// get(2+)= events  (eg: "say Hi" or "sound QuakePro.DoubleKill")
	////////////////////////////////////////////////////
	public static List<List<String>> trigger = new ArrayList<List<String>>();
	
	
	///////////////////String matrix/////////////////////
	// layout
	// get(0) = string name
	// get(1) = saved string
	/////////////////////////////////////////////////////
	public static List<List<String>> USR_string = new ArrayList<List<String>>();
	
	
	/////////////////Array matrix///////////////////////
	// layout
	// get(0) = array name
	// get(1+) = array values
	////////////////////////////////////////////////////
	public static List<List<String>> USR_array = new ArrayList<List<String>>();
	
	
	
	//////////////////Settings matrix///////////////////
	// layout
	// get(0) = color
	// get(1) = colorname
	////////////////////////////////////////////////////
	public static List<String> settings = new ArrayList<String>();
}

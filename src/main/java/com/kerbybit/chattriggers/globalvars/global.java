package com.kerbybit.chattriggers.globalvars;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class global {
	public static List<String> asyncEvents = new ArrayList<String>();
	
	public static Boolean worldLoaded = false;
	public static Boolean worldFirstLoad = true;
	public static Boolean worldIsLoaded = false;
	public static String connectedToServer = "";
	public static String versionURL = "";
	public static String currentDate = "null";
	
	public static int playerHealth = -1;
	
	public static String importURL = "";
	public static Boolean canImport = true;
	public static List<String> neededImports = new ArrayList<String>();
	
	public static List<String> chatHistory = new ArrayList<String>();
	public static int tick = 0;
	public static List<String> chatQueue = new ArrayList<String>();
	public static int chatDelay = 0;
	public static List<String> commandQueue = new ArrayList<String>();
	
	public static List<String> killfeed = new ArrayList<String>();
	public static List<Integer> killfeedDelay = new ArrayList<Integer>();
	
	public static List<List<String>> waitEvents = new ArrayList<List<String>>();
	public static List<Integer> waitTime = new ArrayList<Integer>();
	
	public static List<String> notify = new ArrayList<String>();
	public static List<List<Float>> notifyAnimation = new ArrayList<List<Float>>();
	public static int notifySize = 0;
	
	public static boolean debug = false;
	public static boolean canSave = true;
	
	public static boolean showGUI = false;
	public static boolean showAltInputGui = false;
	
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
	// get(2) = list
	/////////////////////////////////////////////////////
	public static List<List<String>> USR_string = new ArrayList<List<String>>();
	public static List<List<String>> TMP_string = new ArrayList<List<String>>();
	public static List<List<String>> backupUSR_strings = new ArrayList<List<String>>();
	public static List<List<String>> backupTMP_strings = new ArrayList<List<String>>();
	public static List<String> temporary_replace = new ArrayList<String>();
	public static List<String> temporary_replacement = new ArrayList<String>();
	
	
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

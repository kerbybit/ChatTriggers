package com.kerbybit.chattriggers.globalvars;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class global {
	public static List<String> asyncEvents = new ArrayList<String>();

	public static Boolean worldLoaded = false;
	public static Boolean worldFirstLoad = true;
	public static Boolean worldIsLoaded = false;
	public static String connectedToServer = "";
	public static String versionURL = "";
	public static String currentDate = "null";
	public static String canUseURL1 = "";
	public static String canUseURL2 = "";
	public static Boolean canUse = true;
    public static Boolean hasWatermark = true;
    public static String hasWatermarkURL = "";
	
	public static int playerHealth = -1;
    public static int fps = 0;
    public static int fpscounter = 0;
    public static int fpslow = 50;
    public static int fpshigh = 60;
    public static String fpslowcol = "&c";
    public static String fpsmedcol = "&e";
    public static String fpshighcol = "&a";

    public static Double clicks = 0.0;
    public static List<Double> clicks_ave = new ArrayList<Double>();
    public static Double clicks_max = 0.0;

    public static long fpsSysTime = Minecraft.getSystemTime();
    public static long sysTime = Minecraft.getSystemTime();
	
	public static String importURL = "";
	public static Boolean canImport = true;
	public static List<String> neededImports = new ArrayList<String>();

	public static HashMap<String, String> imported = new HashMap<String, String>();

    public static List<String> bugReport = new ArrayList<String>();
    public static String lastCommand = "";
    public static String bugLastCommand = "";
    public static String lastEvent = "";
    public static String bugLastEvent = "";

    //key - URL
    //value - json string
    public static HashMap<String, String> jsonURL = new HashMap<String, String>();
	
	public static List<String> chatHistory = new ArrayList<String>();
	public static int tick = 0;
	public static int ticksElapsed = 0;
	public static List<String> chatQueue = new ArrayList<String>();
	public static int chatDelay = 0;
	public static List<String> commandQueue = new ArrayList<String>();
	
	public static List<String> killfeed = new ArrayList<String>();
	public static List<Integer> killfeedDelay = new ArrayList<Integer>();
    public static double killfeed_x = 0;
    public static double killfeed_y = 0;
	
	public static List<List<String>> waitEvents = new ArrayList<List<String>>();
	public static List<Integer> waitTime = new ArrayList<Integer>();
	
	public static List<String> notify = new ArrayList<String>();
	public static List<List<Float>> notifyAnimation = new ArrayList<List<Float>>();
	public static int notifySize = 0;
	
	public static boolean debug = false;
	public static boolean debugChat = false;
	public static List<String> copyText = new ArrayList<String>();
	public static boolean canSave = true;
	
	public static boolean showGUI = false;
	public static boolean showAltInputGui = false;
    public static boolean showDisplayGui = false;
	
	//////////////////Trigger matrix////////////////////
	// layout
	// get(0) = trigger type (chat/key press/minecraft event)
	// get(1) = trigger (eg: "{s}Guild >" or "ctrl+b")
	// get(2+)= events  (eg: "say Hi" or "sound QuakePro.DoubleKill")
	////////////////////////////////////////////////////
	public static List<List<String>> trigger = new ArrayList<List<String>>();
	public static List<List<String>> chatTrigger = new ArrayList<List<String>>();
	public static List<List<String>> tickTrigger = new ArrayList<List<String>>();
	public static List<Integer> tickTriggerTime = new ArrayList<Integer>();
	public static List<List<String>> onWorldLoadTrigger = new ArrayList<List<String>>();
	public static List<List<String>> onWorldFirstLoadTrigger = new ArrayList<List<String>>();
	public static List<List<String>> onServerChangeTrigger = new ArrayList<List<String>>();
	public static List<List<String>> onNewDayTrigger = new ArrayList<List<String>>();
	public static List<List<String>> onRightClickPlayerTrigger = new ArrayList<List<String>>();
    public static List<List<String>> onChatTrigger = new ArrayList<List<String>>();
    public static List<List<String>> function = new ArrayList<List<String>>();
    public static List<List<String>> onUnknownError = new ArrayList<List<String>>();
	
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

	////////////////////List Map////////////////////////
    // layout
    // key = name
    // value = list
    ////////////////////////////////////////////////////
    public static HashMap<String, List<String>> lists = new HashMap<String, List<String>>();



    ////////////////Display map//////////////////////
    // layout
    // key = display name
    // value = list of values
    /////////////////////////////////////////////////
    ////////////////Display xy///////////////////////
    // layout
    // key = display name
    // value = x y array
    /////////////////////////////////////////////////
	public static HashMap<String,List<String>> displays = new HashMap<String,List<String>>();
    public static HashMap<String,List<String>> shown_displays = new HashMap<String,List<String>>();
    public static HashMap<String,Double[]> displays_xy = new HashMap<String,Double[]>();


    /////////////////////Json map///////////////////////
    // layout
    // key = json name
    // value = json object
    ////////////////////////////////////////////////////
    public static HashMap<String, JsonObject> jsons = new HashMap<String, JsonObject>();
	
	//////////////////Settings matrix///////////////////
	// layout
	// get(0) = color
	// get(1) = color name
    // get(2) = version
    // get(3) = killfeed pos
    // get(4) = isBeta
    // get(5) = lastOpened
    // get(6) = t
    // get(7) = tr
    // get(8) = notification speed
    // get(9) = killfeed fade
    // get(10) = show killfeed in notifications
    ////////////////////////////////////////////////////
	public static List<String> settings = new ArrayList<String>();
    public static int settingsNotificationSpeed = 10;

    public static String[] append(String[] array, String value) {
        String[] result = Arrays.copyOf(array, array.length+1);
        result[result.length-1] = value;
        return result;
    }
}

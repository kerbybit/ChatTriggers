package com.kerbybit.chattriggers.globalvars;

import java.util.*;

public class global {
	public static HashMap<Integer, List<String>> asyncMap = new HashMap<Integer, List<String>>();
	public static int asyncID = 0;

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
    public static Boolean inverted = false;
	
	public static int playerHealth = -1;
    public static int fpslow = 50;
    public static int fpshigh = 60;
    public static String fpslowcol = "&c";
    public static String fpsmedcol = "&e";
    public static String fpshighcol = "&a";

    public static Double clicks = 0.0;
    public static List<Integer> secondClicks = new ArrayList<Integer>();
    public static List<Double> clicks_ave = new ArrayList<Double>();
    public static Double clicks_max = 0.0;

    public static Double rclicks = 0.0;
    public static List<Double> rclicks_ave = new ArrayList<Double>();
    public static Double rclicks_max = 0.0;
	
	public static String importURL = "";
	public static Boolean canImport = true;
	public static List<String> neededImports = new ArrayList<String>();

	public static HashMap<String, String> imported = new HashMap<String, String>();

    public static List<String> bugReport = new ArrayList<String>();
    public static String lastCommand = "";
    public static String bugLastCommand = "";
    public static String lastEvent = "";
    public static String bugLastEvent = "";
	
	public static List<String> chatHistory = new ArrayList<String>();
	public static int tick = 0;
	public static int ticksElapsed = 0;
	public static List<String> chatQueue = new ArrayList<String>();
	public static int chatDelay = 0;
	public static List<String> commandQueue = new ArrayList<String>();
	
	public static List<String> killfeed = new ArrayList<String>();
	public static List<Integer> killfeedDelay = new ArrayList<Integer>();
    public static List<String> killfeed_history = new ArrayList<String>();
	
	public static List<List<String>> waitEvents = new ArrayList<List<String>>();
	public static List<Integer> waitTime = new ArrayList<Integer>();
	
	public static List<String> notify = new ArrayList<String>();
	public static List<List<Float>> notifyAnimation = new ArrayList<List<Float>>();
	public static int notifySize = 0;
    public static List<String> notify_history = new ArrayList<String>();
	
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
	public static Map<String, String> Async_string = new HashMap<String, String>();
	public static Map<String, String> backupAsync_string = new HashMap<String, String>();
	public static List<List<String>> backupUSR_strings = new ArrayList<List<String>>();
	public static List<List<String>> backupTMP_strings = new ArrayList<List<String>>();
	public static List<String> temporary_replace = new ArrayList<String>();
	public static List<String> temporary_replacement = new ArrayList<String>();

    public static String[] append(String[] array, String value) {
        String[] result = Arrays.copyOf(array, array.length+1);
        result[result.length-1] = value;
        return result;
    }
}

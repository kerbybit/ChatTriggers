package com.kerbybit.chattriggers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;

public class file {
	public static void loadVersion(String url) {
		try {
			URL web = new URL(url);
			InputStream fis = web.openStream();
			List<String> lines = new ArrayList<String>();
			String line = null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			
			if (!global.settings.get(2).equals("null")) {
				if (!lines.get(0).equals(global.settings.get(2))) {
					chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------"));
					if (global.settings.get(4).equals("false")) {
						chat.warn(chat.color("red", "You are running on an outdated version of ChatTriggers!"));
						List<String> TMP_out = new ArrayList<String>();
						TMP_out.add("text:'http://kerbybit.github.io/ChatTriggers/download',color:red,hoverEvent:{action:'show_text',value:'Click to download update'},clickEvent:{action:'open_url',value:'http://kerbybit.github.io/ChatTriggers/download'}");
						chat.sendJson(TMP_out);
						chat.warn(chat.color("red", "Current stable version: " + lines.get(0)));
					} else {
						chat.warn(chat.color("red", "You are running on an outdated version of ChatTriggers!"));
						List<String> TMP_out = new ArrayList<String>();
						TMP_out.add("text:'http://kerbybit.github.io/ChatTriggers/download',color:red,hoverEvent:{action:'show_text',value:'Click to download update'},clickEvent:{action:'open_url',value:'http://kerbybit.github.io/ChatTriggers/download'}");
						chat.sendJson(TMP_out);
						chat.warn(chat.color("red", "Current beta version: " + lines.get(0)));
					}
					chat.warn(chat.color("red", "Your version: " + global.settings.get(2)));
					chat.warn(chat.color("red", "You will only see this message once until the next update"));
					chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------&r" + global.settings.get(0) + "^"));
					global.settings.set(2,lines.get(0));
					file.saveAll();
				}
			} else {
				global.settings.set(2, lines.get(0));
				file.saveAll();
			}
		} catch (MalformedURLException e) {
			chat.warn(chat.color("red", "Can't grab update! Report this to kerbybit ASAP"));
			e.printStackTrace();
		} catch (IOException e) {
			chat.warn(chat.color("red", "Can't grab update! Report this to kerbybit ASAP"));
			e.printStackTrace();
		}
	}
	
	
	public static void loadImports(String dest) {
		File dir = new File(dest);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				System.out.println(file.getName());
				if (file.getName().endsWith(".txt")) {
					try {global.trigger.addAll(loadTriggers(dest + file.getName()));}
					catch (IOException e) {chat.warn(chat.color("red", "Unable to load import!"));}
				}
			}
		}
	}
	
	
	public static String importJsonFile(String type, String fileName, String toImport) {
		String returnString = "Something went wrong!";
		try {
			List<String> lines = new ArrayList<String>();
			String line = null;
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			
			String jsonString = "";
			for (String value : lines) {jsonString += value;}
			
			if (toImport.contains("=>")) {
				if (type.equalsIgnoreCase("ARRAY")) {
					String arrayToSave = toImport.substring(0,toImport.indexOf("=>"));
					
					int whatArray = -1;
					for (int i=0; i<global.USR_array.size(); i++) {
						if (arrayToSave.equals(global.USR_array.get(i))) {
							whatArray = i;
						}
					}
					
					if (whatArray == -1) {
						List<String> temporary = new ArrayList<String>();
						temporary.add(arrayToSave);
						global.USR_array.add(temporary);
						whatArray = global.USR_array.size()-1;
					}
					
					String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
					
					String check = "\""+jsonGet+"\""+":\"";
					if (jsonString.contains(check)) {
						returnString = "[";
						while (jsonString.contains(check)) {
							String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
							global.USR_array.get(whatArray).add(jsonGot);
							jsonString = jsonString.replaceFirst(check+jsonGot+"\"", "");
							returnString += jsonGot+",";
						}
						returnString = returnString.substring(0,returnString.length()-1)+"]";
					} else {
						returnString = "No "+jsonGet+" in json!";
					}
				} else if (type.equalsIgnoreCase("STRING")) {
					String stringToSave = toImport.substring(0,toImport.indexOf("=>"));
					
					for (int i=0; i<global.USR_string.size(); i++) {
						if (stringToSave.equals(global.USR_string.get(i).get(0))) {
							String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
							
							String check = "\""+jsonGet+"\":\"";
							if (jsonString.contains(check)) {
								String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
								global.USR_string.get(i).set(1, jsonGot);
								returnString = jsonGot;
							}
						}
					}
				}
			} else {
				returnString = "No array! use 'array=>nodes'";
			}
		} catch (UnsupportedEncodingException e) {
			returnString = "Unsupported encoding!";
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			returnString = "File not found!";
			e.printStackTrace();
		} catch (IOException e) {
			returnString = "IO exception!";
			e.printStackTrace();
		}
		return returnString;
	}
	
	public static String importJsonURL(String type, String url, String toImport) { //TODO
		String returnString = "Something went wrong!";
		try {
			URL web = new URL(url);
			InputStream fis = web.openStream();
			List<String> lines = new ArrayList<String>();
			String line = null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			
			String jsonString = "";
			for (String value : lines) {jsonString += value;}
			
			if (toImport.contains("=>")) {
				if (type.equalsIgnoreCase("ARRAY")) {
					String arrayToSave = toImport.substring(0,toImport.indexOf("=>"));
					
					int whatArray = -1;
					for (int i=0; i<global.USR_array.size(); i++) {
						if (arrayToSave.equals(global.USR_array.get(i))) {
							whatArray = i;
						}
					}
					
					if (whatArray == -1) {
						List<String> temporary = new ArrayList<String>();
						temporary.add(arrayToSave);
						global.USR_array.add(temporary);
						whatArray = global.USR_array.size()-1;
					}
					
					String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
					
					String check = "\""+jsonGet+"\":\"";
					if (jsonString.contains(check)) {
						returnString = "[";
						while (jsonString.contains(check)) {
							String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
							global.USR_array.get(whatArray).add(jsonGot);
							jsonString = jsonString.replaceFirst(check+jsonGot+"\"", "");
							returnString += jsonGot+",";
						}
						returnString = returnString.substring(0, returnString.length()-1) + "]";
					} else {
						returnString = "No "+jsonGet+" in json!";
					}
				} else if (type.equalsIgnoreCase("STRING")) {
					String stringToSave = toImport.substring(0,toImport.indexOf("=>"));
					
					for (int i=0; i<global.USR_string.size(); i++) {
						if (stringToSave.equals(global.USR_string.get(i).get(0))) {
							String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
							
							String check = "\""+jsonGet+"\":\"";
							if (jsonString.contains(check)) {
								String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
								global.USR_string.get(i).set(1, jsonGot);
								returnString = jsonGot;
							}
						}
					}
				}
			} else {
				returnString = "No array! use 'array=>nodes'";
			}
		} catch (UnsupportedEncodingException e) {
			returnString = "Unsupported encoding!";
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			returnString = "File not found!";
			e.printStackTrace();
		} catch (IOException e) {
			returnString = "IO exception!";
			e.printStackTrace();
		}
		return returnString;
	}
	
	public static void saveExport(List<List<String>> trigger, List<List<String>> USR_string, String fileName) throws IOException {
		String username = Minecraft.getMinecraft().thePlayer.getDisplayNameString();
		List<String> tmp_list = new ArrayList<String>();
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		writer.println("");
		writer.println("TRIGGERS");
		for (int i=0; i<trigger.size(); i++) {
			writer.println("trigger:"+trigger.get(i).get(1).replace("{string[", "{string[" + username + "-").replace("{string<", "{string<" + username + "-"));
			writer.println("type:"+trigger.get(i).get(0));
			String extraSpaces = "";
			for (int j=2; j<trigger.get(i).size(); j++) {
				if (trigger.get(i).get(j).toUpperCase().startsWith("END")) {
					extraSpaces = "";
				}
				writer.println(extraSpaces + "  event:"+trigger.get(i).get(j).replace("{string[", "{string[" + username + "-").replace("{string<", "{string<" + username + "-"));
				if (trigger.get(i).get(j).toUpperCase().startsWith("CHOOSE")) {
					extraSpaces = "  ";
				}
				String trig_check = trigger.get(i).get(j);
				trig_check = trig_check.replace("{string<", "{string[");
				trig_check = trig_check.replace(">", "]");
				while (trig_check.contains("{string[")) {
					trig_check = trig_check.substring(trig_check.indexOf("{string["), trig_check.length());
					String trig_string = trig_check.substring(trig_check.indexOf("{string[") + 8, trig_check.indexOf("]"));
					trig_check = trig_check.replace("{string[" + trig_string + "]", "");
					tmp_list.add(trig_string);
				}
			}
			String trig_check = trigger.get(i).get(1);
			trig_check = trig_check.replace("{string<", "{string[");
			trig_check = trig_check.replace(">", "]");
			while (trig_check.contains("{string[")) {
				trig_check = trig_check.substring(trig_check.indexOf("{string["), trig_check.length());
				String trig_string = trig_check.substring(trig_check.indexOf("{string[") + 8, trig_check.indexOf("]"));
				trig_check = trig_check.replace("{string[" + trig_string + "]", "");
				tmp_list.add(trig_string);
			}
		}
		writer.println("");writer.println("");writer.println("");
		writer.println("STRINGS");
		Set<String> uniqueTMP_lists = new HashSet<String>(tmp_list);
		for (String value : uniqueTMP_lists) {
			for (int i=0; i<USR_string.size(); i++) {
				if (value.equals(USR_string.get(i).get(0))) {
					String string_check = USR_string.get(i).get(1);
					string_check = string_check.replace("{string<", "{string[");
					string_check = string_check.replace(">", "]");
					while (string_check.contains("{string[")) {
						string_check = string_check.substring(string_check.indexOf("{string["), string_check.length());
						String string_string = string_check.substring(string_check.indexOf("{string[") + 8, string_check.indexOf("]"));
						string_check = string_check.replace("{string[" + string_string + "]", "");
						tmp_list.add(string_string);
					}
				}
			}
		}
		Set<String> uniqueTMP_lists2 = new HashSet<String>(tmp_list);
		for (String value : uniqueTMP_lists2) {
			for (int i=0; i<USR_string.size(); i++) {
				if (value.equals(USR_string.get(i).get(0))) {
					writer.println("string:" + username + "-" + USR_string.get(i).get(0));
					writer.println("  value:" + USR_string.get(i).get(1).replace("{string[", "{string[" + username + "-").replace("{string<", "{string<" + username + "-"));
				}
			}
		}
		
		writer.close();
	}
	
	public static void loadImport(String url) {
		List<List<String>> tmp_triggers = new ArrayList<List<String>>();
		List<List<String>> tmp_strings = new ArrayList<List<String>>();
		List<String> templist = new ArrayList<String>();
		try {
			URL web = new URL(url);
			InputStream fis = web.openStream();
			List<String> lines = new ArrayList<String>();
			String line = null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			
			int j=-1;
			for (int i=0; i<lines.size()-1; i++) {
				if (lines.get(i).startsWith("trigger:") && lines.get(i+1).startsWith("type:")) {
					List<String> tmp_list = new ArrayList<String>();
					tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("type:") + 5, lines.get(i+1).length()));
					tmp_list.add(lines.get(i).substring(lines.get(i).indexOf("trigger:") + 8, lines.get(i).length()));
					tmp_triggers.add(tmp_list);
					j++;
				}
				if (lines.get(i).trim().startsWith("event:") && j>-1) {
					tmp_triggers.get(j).add(lines.get(i).substring(lines.get(i).indexOf("  event:") + 8, lines.get(i).length()));
				}
				if (lines.get(i).contains("string:") && lines.get(i+1).startsWith("  value:")) {
					List<String> tmp_list = new ArrayList<String>();
					tmp_list.add(lines.get(i).substring(lines.get(i).indexOf("string:") + 7, lines.get(i).length()));
					tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("  value:") + 8, lines.get(i+1).length()));
					tmp_strings.add(tmp_list);
				}
			}
			
			global.trigger.addAll(tmp_triggers);
			global.USR_string.addAll(tmp_strings);
			
			chat.warn("&7Imported " + global.settings.get(0) + tmp_triggers.size() + " &7triggers");
			chat.warn("&7and " + global.settings.get(0) + tmp_strings.size() + " &7strings");
			
		} catch (MalformedURLException e) {
			chat.warn(chat.color("red", "Not a valid import!"));
			e.printStackTrace();
		} catch (IOException e) {
			chat.warn(chat.color("red", "Not a valid import!"));
			e.printStackTrace();
		}
	}
	
	public static void saveTriggers(List<List<String>> trigger, String fileName) throws IOException {
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		List<String> lists = new ArrayList<String>();
		
		for (int i=0; i<trigger.size(); i++) {
			if (trigger.get(i).get(1).contains("{list=") && trigger.get(i).get(1).contains("}")) {
				lists.add(trigger.get(i).get(1).substring(trigger.get(i).get(1).indexOf("{list="), trigger.get(i).get(1).indexOf("}",trigger.get(i).get(1).indexOf("{list="))+1));
			}
			if (trigger.get(i).get(1).contains("<list=") && trigger.get(i).get(1).contains(">")) {
				lists.add(trigger.get(i).get(1).substring(trigger.get(i).get(1).indexOf("<list="), trigger.get(i).get(1).indexOf(">",trigger.get(i).get(1).indexOf("<list="))+1));
			}
		}
		
		writer.println("");
		Set<String> uniqueLists = new HashSet<String>(lists);
		for (String value : uniqueLists) {
			if (!value.equals("")) {
				writer.println(">>" + value);
				for (int i=0; i<trigger.size(); i++) {
					if (trigger.get(i).get(1).contains(value)) {
						writer.println("trigger:"+trigger.get(i).get(1));
						writer.println("type:"+trigger.get(i).get(0));
						
						int tabbed_logic=0;
						for (int j=2; j<trigger.get(i).size(); j++) {
							String extraSpaces = "";
							String TMP_c = trigger.get(i).get(j);
							
							if (TMP_c.equalsIgnoreCase("END")
							|| TMP_c.toUpperCase().startsWith("ELSE")) { 
								tabbed_logic--;
							}
							
								for (int k=0; k<tabbed_logic; k++) {extraSpaces+= "  ";}
								writer.println(extraSpaces + "  event:"+trigger.get(i).get(j));
							
							if (TMP_c.toUpperCase().startsWith("IF") 
							|| TMP_c.toUpperCase().startsWith("FOR")
							|| TMP_c.toUpperCase().startsWith("CHOOSE")
							|| TMP_c.toUpperCase().startsWith("WAIT")
							|| TMP_c.toUpperCase().startsWith("ELSE")
							|| TMP_c.toUpperCase().startsWith("ASYNC")) {
								tabbed_logic++;
							}
						}
					}
				}
				writer.println("<<"); writer.println(""); writer.println("");
			}
		}
		writer.println("NO LIST");
		for (int i=0; i<trigger.size(); i++) {
			if (!(trigger.get(i).get(1).contains("{list=") || trigger.get(i).get(1).contains("<list="))) {
				writer.println("trigger:"+trigger.get(i).get(1));
				writer.println("type:"+trigger.get(i).get(0));
				
				int tabbed_logic = 0;
				for (int j=2; j<trigger.get(i).size(); j++) {
					String extraSpaces = "";
					String TMP_c = trigger.get(i).get(j);
					
					if (TMP_c.equalsIgnoreCase("END")
					|| TMP_c.toUpperCase().startsWith("ELSE")) { 
						tabbed_logic--;
					}
					
						for (int k=0; k<tabbed_logic; k++) {extraSpaces+= "  ";}
						writer.println(extraSpaces + "  event:"+trigger.get(i).get(j));
					
					if (TMP_c.toUpperCase().startsWith("IF") 
					|| TMP_c.toUpperCase().startsWith("FOR")
					|| TMP_c.toUpperCase().startsWith("CHOOSE")
					|| TMP_c.toUpperCase().startsWith("WAIT")
					|| TMP_c.toUpperCase().startsWith("ELSE")
					|| TMP_c.toUpperCase().startsWith("ASYNC")) {
						tabbed_logic++;
					}
						
				}
			}
		}
		writer.println(""); writer.println(""); writer.println("");
		writer.println("donut delete this line");
		writer.close();
	}
	
	public static void saveStrings(List<List<String>> listName, String fileName) throws IOException {
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		for (int i=0; i<listName.size(); i++) {
			writer.println("string:"+listName.get(i).get(0));
			writer.println("  value:"+listName.get(i).get(1));
		}
		writer.close();
	}
	
	public static void saveSettings(List listName, String fileName) throws IOException {
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		writer.println("color:"+listName.get(0));
		writer.println("colorName:"+listName.get(1));
		writer.println("version:"+listName.get(2));
		writer.println("killfeed pos:"+listName.get(3));
		writer.println("isBeta:"+listName.get(4));
		writer.close();
	}
	
	public static List<List<String>> loadTriggers(String fileName) throws IOException {
		List<List<String>> tmp_triggers = new ArrayList<List<String>>();
		List<String> lines = new ArrayList<String>();
		String line = null;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();

		int j=-1;
		for (int i=0; i<lines.size()-1; i++) {
			if (lines.get(i).startsWith("trigger:")) {
				List<String> tmp_list = new ArrayList<String>();
				if (lines.get(i+1).startsWith("type:")) {
					tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("type:") + 5, lines.get(i+1).length()));
				} else {
					chat.warn(chat.color("red","No trigger type specified for") + chat.color("gray",lines.get(i).substring(lines.get(i).indexOf("trigger:") + 8, lines.get(i).length())));
					chat.warn(chat.color("red", "Set type to") + chat.color("gray", "other"));
					tmp_list.add("other");
				}
				tmp_list.add(lines.get(i).substring(lines.get(i).indexOf("trigger:") + 8, lines.get(i).length()));
				tmp_triggers.add(tmp_list);
				j++;
			}
			if (lines.get(i).trim().startsWith("event:") && j>-1) {
				tmp_triggers.get(j).add(lines.get(i).substring(lines.get(i).indexOf("  event:") + 8, lines.get(i).length()));
			}
		}
		return tmp_triggers;
	}
	
	public static List<List<String>> loadStrings(String fileName) throws IOException {
		List<List<String>> tmp_strings = new ArrayList<List<String>>();
		List<String> lines = new ArrayList<String>();
		String line = null;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		
		for (int i=0; i<lines.size(); i++) {
			if (lines.get(i).contains("string:") && lines.get(i+1).startsWith("  value:")) {
				List<String> tmp_list = new ArrayList<String>();
				tmp_list.add(lines.get(i).substring(lines.get(i).indexOf("string:") + 7, lines.get(i).length()));
				tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("  value:") + 8, lines.get(i+1).length()));
				tmp_strings.add(tmp_list);
			}
		}
		
		return tmp_strings;
	}
	
	public static List<String> loadSettings(String fileName) throws IOException {
		List<String> tmp_settings = new ArrayList<String>();
		List<String> lines = new ArrayList<String>();
		String line = null;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		
		for (int i=0; i<lines.size(); i++) {
			if (lines.get(i).startsWith("color:")) {
				tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("color:") + 6));
			}
			if (lines.get(i).startsWith("colorName:")) {
				tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("colorName:") + 10));
			}
			if (lines.get(i).startsWith("version:")) {
				tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("version:") + 8));
			}
			if (lines.get(i).startsWith("killfeed pos:")) {
				tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("killfeed pos:")+13));
			}
			if (lines.get(i).startsWith("isBeta:")) {
				tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("isBeta:")+7));
			}
		}
		
		return tmp_settings;
	}
	
	public static void saveAll() throws IOException {
		saveTriggers(global.trigger, "./mods/ChatTriggers/triggers.txt");
		saveStrings(global.USR_string, "./mods/ChatTriggers/strings.txt");
		saveSettings(global.settings, "./mods/ChatTriggers/settings.txt");
	}
	
	public static void startup() throws ClassNotFoundException {
		chat.warn(chat.color("gray", "Loading chat triggers..."));
		if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold");}
		try {
			global.trigger = loadTriggers("./mods/ChatTriggers/triggers.txt");
			loadImports("./mods/ChatTriggers/Imports");
			global.USR_string = loadStrings("./mods/ChatTriggers/strings.txt");
			global.settings = loadSettings("./mods/ChatTriggers/settings.txt");
			if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold"); global.settings.add("null");}
			if (global.settings.size() < 3) {global.settings.add("null");}
			if (global.settings.size() < 4) {global.settings.add("top-left");}
			if (global.settings.size() < 5) {global.settings.add("false");}
			chat.warn(chat.color(global.settings.get(0), "Chat triggers loaded"));
		} catch (IOException e1) {
			chat.warn(chat.color("red", "Error loading files!"));
			chat.warn(chat.color("gold", "Setting up new files"));
			File checkFile = new File("./mods/ChatTriggers");
			if (checkFile.exists()) {
				try {FileUtils.deleteDirectory(checkFile);} 
				catch (IOException e11) {chat.warn(chat.color("red","Error deleting old files!")); e11.printStackTrace();}
				checkFile.mkdir();
			} else {checkFile.mkdir();}
			
			if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold"); global.settings.add("null");}
			if (global.settings.size() < 3) {global.settings.add("null");}
			if (global.settings.size() < 4) {global.settings.add("top-left");}
			if (global.settings.size() < 5) {global.settings.add("false");}
			
			try {file.saveAll(); chat.warn(chat.color("green", "New files created!"));} 
			catch (IOException e111) {chat.warn(chat.color("red", "Error saving files! report this to kerbybit ASAP!")); e111.printStackTrace();}
		}
		if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold"); global.settings.add("null");}
		if (global.settings.size() < 3) {global.settings.add("null");}
		if (global.settings.size() < 4) {global.settings.add("top-left");}
		if (global.settings.size() < 5) {global.settings.add("false");}
		try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
	}
}
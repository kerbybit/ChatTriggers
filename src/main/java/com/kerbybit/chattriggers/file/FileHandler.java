package com.kerbybit.chattriggers.file;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;

public class FileHandler {
	public static void loadImports(String dest) {
		File dir = new File(dest);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (file.getName().endsWith(".txt")) {
					try {global.trigger.addAll(loadTriggers(dest + file.getName(), true));}
					catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Unable to load import!")); e.printStackTrace();}
				}
			}
		}
	}
	
	public static void getImport(String filename) {
		global.importURL = filename;
		if (global.canImport==true) {
			global.canImport=false;
			Thread t1 = new Thread(new Runnable() {
			     public void run() {
			    	ChatHandler.warn(ChatHandler.color("gray", "Getting import..."));
			 		try {
			 			String url = global.importURL;
			 			String file = new File(global.importURL).getName();
			 			URL web = new URL(url);
			 			if (global.debug==true) {ChatHandler.warn(ChatHandler.color("&7", "Getting import from "+global.importURL));}
			 			InputStream fis = web.openStream();
			 			List<String> lines = new ArrayList<String>();
			 			String line = null;
			 			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			 			while ((line = bufferedReader.readLine()) != null) {
			 				lines.add(line);
			 			}
			 			bufferedReader.close();
			 			
			 			if (global.debug==true) {ChatHandler.warn(ChatHandler.color("&7", "Setting up files to save"));}
			 			File dir = new File("./mods/ChatTriggers/Imports/");
			 			if (!dir.exists()) {dir.mkdir();}
			 			File fin = new File("./mods/ChatTriggers/Imports/"+file);
			 			if (!fin.exists()) {fin.createNewFile();}
			 			
			 			if (global.debug==true) {ChatHandler.warn(ChatHandler.color("&7", "Saving file to "+fin.getName()));}
			 			PrintWriter writer = new PrintWriter(fin,"UTF-8");
			 			for (String value : lines) {writer.println(value);}
			 			writer.close();
			 			if (global.debug==true) {ChatHandler.warn(ChatHandler.color("&7", "Loading imports into triggers"));}
			 			
			 			try {saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Something went wrong while loading the files after an import!"));}
			 			
			 			ChatHandler.warn(ChatHandler.color(global.settings.get(0), "Got "+file+" successfully!"));
			 		} catch (MalformedURLException e) {
			 			ChatHandler.warn(ChatHandler.color("red", "Not a valid import! bad URL"));
			 			e.printStackTrace();
			 		} catch (IOException e) {
			 			ChatHandler.warn(ChatHandler.color("red", "Not a valid import! IO exception"));
			 			e.printStackTrace();
			 		}
			 		global.canImport=true;
			     }
			});
			t1.start();
		} else {
			ChatHandler.warn(ChatHandler.color("red", "You are trying to do this too quick! slow down!"));
		}
	}
	
	public static String exportJsonFile(String fileName, String arrayName, String nodeName) throws IOException {
		String returnString = "Something went wrong!";
		int arrayNum = -1;
		
		for (int i=0; i<global.USR_array.size(); i++) {
			if (arrayName.equals(global.USR_array.get(i).get(0))) {
				arrayNum = i;
			}
		}
		
		File dir = new File(fileName);
		if (!dir.exists()) {dir.createNewFile();}
		
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		
		if (arrayNum==-1) {
			writer.println("{");
			writer.println("}");
			returnString = "{}";
		} else {
			writer.println("{");
			returnString = ("{");
			for (int i=1; i<global.USR_array.get(arrayNum).size(); i++) {
				String hasComma = "";
				if (i!=global.USR_array.get(arrayNum).size()-1) {hasComma = ",";}
				writer.println("     \""+nodeName+"\":\""+global.USR_array.get(arrayNum).get(i)+"\""+hasComma);
				returnString += ("\""+nodeName+"\":\""+global.USR_array.get(arrayNum).get(i)+"\""+hasComma);
			}
			writer.println("}");
			returnString += ("}");
		}
		writer.close();
		
		return returnString;
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
			jsonString = jsonString.replace("[", "openSquareF6cyUQp9openSquare").replace("]", "closeSquareF6cyUQp9closeSquare")
					.replace("+", "plusF6cyUQp9plus").replace("-", "minusF6cyUQp9minus");
			
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
							global.USR_array.get(whatArray).add(jsonGot.replace("openSquareF6cyUQp9openSquare","[").replace("closeSquareF6cyUQp9closeSquare","]")
									.replace("plusF6cyUQp9plus", "+").replace("minusF6cyUQp9minus", "-"));
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
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (stringToSave.equals(global.TMP_string.get(i).get(0))) {
							String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
							
							String check = "\""+jsonGet+"\":\"";
							if (jsonString.contains(check)) {
								String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
								global.TMP_string.get(i).set(1, jsonGot);
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
	
	public static String importJsonURL(String type, String url, String toImport) {
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
			jsonString = jsonString.replace("[", "openSquareF6cyUQp9openSquare").replace("]", "closeSquareF6cyUQp9closeSquare")
					.replace("+", "plusF6cyUQp9plus").replace("-", "minusF6cyUQp9minus");
			
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
							global.USR_array.get(whatArray).add(jsonGot.replace("openSquareF6cyUQp9openSquare","[").replace("closeSquareF6cyUQp9closeSquare","]")
									.replace("plusF6cyUQp9plus", "+").replace("minusF6cyUQp9minus", "-"));
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
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (stringToSave.equals(global.TMP_string.get(i).get(0))) {
							String jsonGet = toImport.substring(toImport.indexOf("=>")+2, toImport.length());
							
							String check = "\""+jsonGet+"\":\"";
							if (jsonString.contains(check)) {
								String jsonGot = jsonString.substring(jsonString.indexOf(check) + check.length(), jsonString.indexOf("\"", jsonString.indexOf(check)+check.length()));
								global.TMP_string.get(i).set(1, jsonGot);
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
	
	public static void saveTriggers(List<List<String>> trigger, String fileName) throws IOException {
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		List<String> lists = new ArrayList<String>();
		
		for (int i=0; i<trigger.size(); i++) {
			if (trigger.get(i).get(1).contains("<list=") && trigger.get(i).get(1).contains(">") && !trigger.get(i).get(1).contains("<imported>")) {
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
			if (!trigger.get(i).get(1).contains("<list=") && !trigger.get(i).get(1).contains("<imported>")) {
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
			if (listName.get(i).size()==3) {
				writer.println("  list:"+listName.get(i).get(2));
			}
		}
		writer.close();
	}
	
	public static void saveSettings(List listName, String fileName) throws IOException {
		PrintWriter writer = new PrintWriter(fileName,"UTF-8");
		if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold"); global.settings.add("null");}
		if (global.settings.size() < 3) {global.settings.add("null");}
		if (global.settings.size() < 4) {global.settings.add("top-left");}
		if (global.settings.size() < 5) {global.settings.add("false");}
		writer.println("color:"+listName.get(0));
		writer.println("colorName:"+listName.get(1));
		writer.println("version:"+listName.get(2));
		writer.println("killfeed pos:"+listName.get(3));
		writer.println("isBeta:"+listName.get(4));
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		writer.println("lastOpened:"+dateFormat.format(date));
		writer.close();
	}
	
	public static List<List<String>> loadTriggers(String fileName, Boolean isImport) throws IOException {
		List<List<String>> tmp_triggers = new ArrayList<List<String>>();
		List<String> lines = new ArrayList<String>();
		String line = null;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();

		int j=-1;
		for (int i=0; i<lines.size(); i++) {
			if (lines.get(i).startsWith("trigger:")) {
				List<String> tmp_list = new ArrayList<String>();
				if (i < lines.size()-1) {
					if (lines.get(i+1).startsWith("type:")) {
						tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("type:") + 5, lines.get(i+1).length()));
					} else {
						ChatHandler.warn(ChatHandler.color("red","No trigger type specified for") + " " + ChatHandler.color("gray",lines.get(i).substring(lines.get(i).indexOf("trigger:") + 8, lines.get(i).length())));
						ChatHandler.warn(ChatHandler.color("red", "Set type to") + " " + ChatHandler.color("gray", "other"));
						tmp_list.add("other");
					}
				} else {
					ChatHandler.warn(ChatHandler.color("red","No trigger type specified for") + " " + ChatHandler.color("gray",lines.get(i).substring(lines.get(i).indexOf("trigger:") + 8, lines.get(i).length())));
					ChatHandler.warn(ChatHandler.color("red", "Set type to") + " " + ChatHandler.color("gray", "other"));
					tmp_list.add("other");
				}
				String importTag = "";
				if (isImport) {importTag="<imported>";}
				tmp_list.add(importTag+lines.get(i).substring(lines.get(i).indexOf("trigger:") + 8, lines.get(i).length()));
				
				tmp_triggers.add(tmp_list);
				j++;
			}
			
			if (lines.get(i).trim().startsWith("event:") && j>-1) {
				String tmp_event = lines.get(i).substring(lines.get(i).indexOf("event:") + 6);
				tmp_triggers.get(j).add(tmp_event);
			}
			
			if (lines.get(i).trim().startsWith("!")) {
				String importFunction = lines.get(i).trim().substring(lines.get(i).trim().indexOf("!")+1);
				if (importFunction.toUpperCase().startsWith("CREATE STRING ") || importFunction.toUpperCase().startsWith("CREATESTRING ")) {
					String sn = "SOMETHING WENT SUPER WRONG!!";
					String ln = "";
					if (importFunction.toUpperCase().startsWith("CREATE STRING ")) {
						sn = importFunction.substring(importFunction.toUpperCase().indexOf("CREATE STRING ")+14);
					} else {
						sn = importFunction.substring(importFunction.toUpperCase().indexOf("CREATESTRING ")+13);
					}
					
					
					String sv = "";
					String svo = "";
					if (sn.toUpperCase().contains("ONCE WITH ")) {
						svo = sn.substring(sn.toUpperCase().indexOf("ONCE WITH ")+10);
					} else if (sn.toUpperCase().contains("ONCEWITH ")) {
						svo = sn.substring(sn.toUpperCase().indexOf("ONCEWITH ")+9);
					} else if (sn.toUpperCase().contains("WITH ")) {
						sv = sn.substring(sn.toUpperCase().indexOf("WITH ")+5);
					}
					
					if (sn.contains(" ")) {sn = sn.substring(0, sn.indexOf(" ")).trim();}
					if (global.debug==true) {
						if (!sv.equals("")) {ChatHandler.warn(ChatHandler.color("gray", "Importing string "+sn+" with value "+sv));}
						else {ChatHandler.warn(ChatHandler.color("gray", "Importing string "+sn+" with no value"));}
					}
					
					if (sn.contains("<list=") && sn.contains(">")) {
						ln = sn.substring(sn.indexOf("<list=")+6, sn.indexOf(">",sn.indexOf("<list=")));
						sn = sn.replace("<list="+ln+">", "");
					}
					
					Boolean canCreate = true;
					for (int k=0; k<global.USR_string.size(); k++) {
						if (global.USR_string.get(k).get(0).equals(sn)) {
							canCreate=false;
							if (!sv.equals("")) {
								global.USR_string.get(k).set(1, sv);
								if (!ln.equals("")) {
									if (global.USR_string.get(k).size()==3) {global.USR_string.get(k).set(2, ln);}
									else {global.USR_string.get(k).add(ln);}
								}
								if (global.debug==true) {ChatHandler.warn(ChatHandler.color("gray", "Set value "+sv+" in string "+sn));}
							} else {if (global.debug==true) {ChatHandler.warn(ChatHandler.color("gray", "String already exsists"));}}
							if (!svo.equals("")) {
								if (global.USR_string.get(k).equals("")) {
									global.USR_string.get(k).set(1, svo);
									if (!ln.equals("")) {
										if (global.USR_string.get(k).size()==3) {global.USR_string.get(k).set(2, ln);}
										else {global.USR_string.get(k).add(ln);}
									}
									if (global.debug==true) {ChatHandler.warn(ChatHandler.color("gray", "Set value "+sv+" in string "+sn));}
								} else {if (global.debug==true) {ChatHandler.warn(ChatHandler.color("gray", "String already has value"));}}
							}
						}
					}
					if (canCreate==true) {
						List<String> temporary = new ArrayList<String>();
						temporary.add(sn);
						if (sv.equals("") && !svo.equals("")) {temporary.add(svo);} 
						else {temporary.add(sv);}
						if (!ln.equals("")) {temporary.add(ln);}
						global.USR_string.add(temporary);
						if (global.debug==true) {
							if (sv!="") {ChatHandler.warn(ChatHandler.color("gray", "Created string "+sn+" with value "+sv));} 
							else {ChatHandler.warn(ChatHandler.color("gray", "Created string "+sn+" with no value"));}
						}
					}
					//try {saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
				} else if (importFunction.toUpperCase().startsWith("REQUIRES ")) {
					String importValue = importFunction.substring(importFunction.indexOf("REQUIRES ")+9);
					String[] importValues = importValue.trim().split(" ");
					for (int k=0; k<importValues.length; k++) {
						if (global.debug==true) {ChatHandler.warn(ChatHandler.color("gray", "Importing "+importValues[k]));}
						File dir = new File("./mods/ChatTriggers/Imports/"+importValues[k]+".txt");
						if (!dir.exists()) {
							global.neededImports.add(importValues[k]);
						} else {
							if (global.debug==true) {ChatHandler.warn(ChatHandler.color("gray", "Import already exsists"));}
						}
					}
				} else if (importFunction.toUpperCase().startsWith("DELETE STRING ") || importFunction.toUpperCase().startsWith("DELETESTRING")) {
					String sn = "SOMETHING WENT SUPER WRONG!!";
					if (importFunction.toUpperCase().startsWith("DELETE STRING ")) {
						sn = importFunction.substring(importFunction.toUpperCase().indexOf("DELETE STRING ")+14);
					} else {
						sn = importFunction.substring(importFunction.toUpperCase().indexOf("DELETESTRING ")+13);
					}
					sn = sn.trim();
					int toRemove = -1;
					for (int k=0; k<global.USR_string.size(); k++) {
						if (sn.equals(global.USR_string.get(k).get(0))) {
							toRemove = k;
						}
					}
					if (toRemove!=-1) {
						if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Removing "+global.USR_string.get(toRemove)));}
						global.USR_string.remove(toRemove);
					}
				}
			}
		}
		
		for (int i=0; i<tmp_triggers.size(); i++) {
			CommandReference.addToTriggerList(tmp_triggers.get(i));
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
			try {
				if (i<=lines.size()-3) {
					if (lines.get(i).contains("string:") && lines.get(i+1).startsWith("  value:") && lines.get(i+2).startsWith("  list:")) {
						List<String> tmp_list = new ArrayList<String>();
						tmp_list.add(lines.get(i).substring(lines.get(i).indexOf("string:") + 7));
						tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("  value:") + 8));
						tmp_list.add(lines.get(i+2).substring(lines.get(i+2).indexOf("  list:")+7));
						tmp_strings.add(tmp_list);
					} else if (lines.get(i).contains("string:") && lines.get(i+1).startsWith("  value:")) {
						List<String> tmp_list = new ArrayList<String>();
						tmp_list.add(lines.get(i).substring(lines.get(i).indexOf("string:") + 7));
						tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("  value:") + 8));
						tmp_strings.add(tmp_list);
					}
				} else {
					if (lines.get(i).contains("string:") && lines.get(i+1).startsWith("  value:")) {
						List<String> tmp_list = new ArrayList<String>();
						tmp_list.add(lines.get(i).substring(lines.get(i).indexOf("string:") + 7));
						tmp_list.add(lines.get(i+1).substring(lines.get(i+1).indexOf("  value:") + 8));
						tmp_strings.add(tmp_list);
					}
				}
			} catch (Exception e) {e.printStackTrace(); ChatHandler.warn(ChatHandler.color("red", "something went wrong while loading strings!"));}
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
			if (lines.get(i).startsWith("color:")) {tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("color:") + 6));}
			if (lines.get(i).startsWith("colorName:")) {tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("colorName:") + 10));}
			if (lines.get(i).startsWith("version:")) {tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("version:") + 8));}
			if (lines.get(i).startsWith("killfeed pos:")) {tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("killfeed pos:")+13));}
			if (lines.get(i).startsWith("isBeta:")) {tmp_settings.add(lines.get(i).substring(lines.get(i).indexOf("isBeta:")+7));}
			if (lines.get(i).startsWith("lastOpened:")) {global.currentDate = lines.get(i).substring(lines.get(i).indexOf("lastOpened:")+11);}
		}
		
		return tmp_settings;
	}
	
	public static void saveAll() throws IOException {
		if (global.canSave==true) {
			saveTriggers(global.trigger, "./mods/ChatTriggers/triggers.txt");
			saveStrings(global.USR_string, "./mods/ChatTriggers/strings.txt");
			saveSettings(global.settings, "./mods/ChatTriggers/settings.txt");
			
			CommandReference.clearTriggerList();
			
			global.trigger = FileHandler.loadTriggers("./mods/ChatTriggers/triggers.txt", false);
			global.USR_string = FileHandler.loadStrings("./mods/ChatTriggers/strings.txt");
			global.settings = FileHandler.loadSettings("./mods/ChatTriggers/settings.txt");
			loadImports("./mods/ChatTriggers/Imports/");
		} else {
			ChatHandler.warn(ChatHandler.color("red", "These changes are not getting saved!"));
			ChatHandler.warn(ChatHandler.color("red", "do </trigger load> to leave testing"));
		}
	}
	
	public static void startup() throws ClassNotFoundException {
		ChatHandler.warn(ChatHandler.color("gray", "Loading chat triggers..."));
		if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold");}
		try {
			CommandReference.clearTriggerList();
			global.trigger = loadTriggers("./mods/ChatTriggers/triggers.txt", false);
			global.USR_string = loadStrings("./mods/ChatTriggers/strings.txt");
			global.settings = loadSettings("./mods/ChatTriggers/settings.txt");
			loadImports("./mods/ChatTriggers/Imports/");
			if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold"); global.settings.add("null");}
			if (global.settings.size() < 3) {global.settings.add("null");}
			if (global.settings.size() < 4) {global.settings.add("top-left");}
			if (global.settings.size() < 5) {global.settings.add("false");}
			if (global.settings.size() < 6) {global.settings.add("null");}
			ChatHandler.warn(ChatHandler.color(global.settings.get(0), "Chat triggers loaded"));
		} catch (IOException e1) {
			ChatHandler.warn(ChatHandler.color("red", "Error loading files!"));
			ChatHandler.warn(ChatHandler.color("gold", "Setting up new files"));
			File checkFile = new File("./mods/ChatTriggers");
			if (checkFile.exists()) {
				try {FileUtils.deleteDirectory(checkFile);} 
				catch (IOException e11) {ChatHandler.warn(ChatHandler.color("red","Error deleting old files!")); e11.printStackTrace();}
				checkFile.mkdir();
			} else {checkFile.mkdir();}
			
			if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold"); global.settings.add("null");}
			if (global.settings.size() < 3) {global.settings.add("null");}
			if (global.settings.size() < 4) {global.settings.add("top-left");}
			if (global.settings.size() < 5) {global.settings.add("false");}
			if (global.settings.size() < 6) {global.settings.add("null");}
			
			try {saveAll(); ChatHandler.warn(ChatHandler.color("green", "New files created!"));} 
			catch (IOException e111) {ChatHandler.warn(ChatHandler.color("red", "Error saving files! report this to kerbybit ASAP!")); e111.printStackTrace();}
		}
		if (global.settings.size() < 1) {global.settings.add("&6"); global.settings.add("gold"); global.settings.add("null");}
		if (global.settings.size() < 3) {global.settings.add("null");}
		if (global.settings.size() < 4) {global.settings.add("top-left");}
		if (global.settings.size() < 5) {global.settings.add("false");}
		if (global.settings.size() < 6) {global.settings.add("null");}
		try {saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
	}
	
	public static void firstFileLoad() {
		if (global.tick==0) {
			global.tick=1;
			try {startup();} catch (ClassNotFoundException e) {e.printStackTrace();}

	    	if (global.settings.get(4).equals("false")) {UpdateHandler.loadVersion("http://chattriggers.kerbybit.com/download/version.txt");} 
	    	else {UpdateHandler.loadVersion("http://chattriggers.kerbybit.com/download/betaversion.txt");}
	    	
	    	UpdateHandler.getCanUse("http://kerbybit.com/blacklist/");
		}
	}
	
	public static void tickImports() {
		if (global.neededImports.size()>0 && global.canImport==true) {
			if (global.canSave) {
				FileHandler.getImport("http://chattriggers.kerbybit.com/exports/"+global.neededImports.remove(0)+".txt");
				
			} else {
				global.neededImports.clear();
				ChatHandler.warn(ChatHandler.color("red", "cannot get imports while in test mode"));
				ChatHandler.warn(ChatHandler.color("red", "</trigger load> to leave testing mode"));
			}
		}
	}
}
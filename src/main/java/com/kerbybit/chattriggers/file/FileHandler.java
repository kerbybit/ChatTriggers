package com.kerbybit.chattriggers.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kerbybit.chattriggers.globalvars.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.globalvars.global;

public class FileHandler {
    private static List<String> loadFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<String>();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();

        return lines;
    }

	public static void loadImports() {
		File dir = new File("./mods/ChatTriggers/Imports/");
		if (!dir.exists()) {
			if (!dir.mkdir()) {ChatHandler.warn(ChatHandler.color("red", "Unable to create file!"));}
		}
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					if (file.getName().endsWith(".txt")) {
						try {global.trigger.addAll(loadTriggers("./mods/ChatTriggers/Imports/" + file.getName(), true, file.getName()));}
						catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Unable to load import!")); e.printStackTrace();}
					}
				}
			}
		}
	}
	
	private static void getImport(String filename) {
		global.importURL = filename;
		if (global.canImport) {
			global.canImport=false;
			Thread t1 = new Thread(new Runnable() {
			     public void run() {
			    	ChatHandler.warn(ChatHandler.color("gray", "Getting import..."));
			 		try {
			 		    String importName = global.importURL.replace("http://chattriggers.kerbybit.com/exports/","").replace(".txt","");
			 		    String currentVersion = "null";

			 			String url = global.importURL;
			 			String file = new File(global.importURL).getName();
			 			URL web = new URL(url);
			 			if (global.debug) {ChatHandler.warn(ChatHandler.color("&7", "Getting import from "+global.importURL));}
			 			InputStream fis = web.openStream();
			 			List<String> lines = new ArrayList<String>();
			 			String line;
			 			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			 			while ((line = bufferedReader.readLine()) != null) {
			 				lines.add(line);
			 			}
			 			bufferedReader.close();

                        web = new URL("http://ct.kerbybit.com/exports/meta/"+importName+".json");
                        fis = web.openStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                        String check = "\"packVersion\":\"";
                        while ((line = bufferedReader.readLine()) != null) {
                            if (line.contains(check)) {
                                currentVersion = line.substring(line.indexOf(check)+check.length(), line.length()-2);
                            }
                        }

			 			if (global.debug) {ChatHandler.warn(ChatHandler.color("&7", "Setting up files to save"));}
			 			File dir = new File("./mods/ChatTriggers/Imports/");
			 			if (!dir.exists()) {if (!dir.mkdir()) {ChatHandler.warn(ChatHandler.color("red", "Unable to create file!"));}}
			 			File fin = new File("./mods/ChatTriggers/Imports/"+file);
			 			if (!fin.exists()) {if (!fin.createNewFile()) {ChatHandler.warn(ChatHandler.color("red", "Unable to create file!"));}}
			 			
			 			if (global.debug) {ChatHandler.warn(ChatHandler.color("&7", "Saving file to "+fin.getName()));}
			 			PrintWriter writer = new PrintWriter(fin,"UTF-8");
			 			for (String value : lines) {writer.println(value);}
			 			writer.println("!VERSION " + currentVersion);
			 			writer.close();
			 			if (global.debug) {ChatHandler.warn(ChatHandler.color("&7", "Loading imports into triggers"));}

						File fcheck = new File("./mods/ChatTriggers/Imports/DisabledImports/"+file);
						if (fcheck.exists()) {if (!fcheck.delete()) {ChatHandler.warn(ChatHandler.color("red", "Something went wrong while deleting the disabled import " + file + "!"));}}

			 			try {saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Something went wrong while loading the files after an import!"));}
			 			
			 			ChatHandler.warn(ChatHandler.color(Settings.col[0], "Got "+file+" successfully!"));
			 		} catch (MalformedURLException e) {
			 			ChatHandler.warn(ChatHandler.color("red", "Not a valid import! bad URL"));
			 			e.printStackTrace();
			 		} catch (IOException e) {
			 			ChatHandler.warn(ChatHandler.color("red", "Not a valid import! IO exception"));
			 			e.printStackTrace();
			 		}
			 		global.canImport=true;
			 		global.worldLoaded = true;
			     }
			});
			t1.start();
		} else {
			ChatHandler.warn(ChatHandler.color("red", "You are trying to do this too quick! slow down!"));
		}
	}
	
	private static void saveTriggers(List<List<String>> trigger) throws IOException {
		PrintWriter writer = new PrintWriter("./mods/ChatTriggers/triggers.txt","UTF-8");
		List<String> lists = new ArrayList<String>();

        for (List<String> value : trigger) {
			if (value.get(1).contains("<list=") && value.get(1).contains(">") && !value.get(1).contains("<imported>")) {
				lists.add(value.get(1).substring(value.get(1).indexOf("<list="), value.get(1).indexOf(">",value.get(1).indexOf("<list="))+1));
			}
		}
		
		writer.println("");
		Set<String> uniqueLists = new HashSet<String>(lists);
		for (String value : uniqueLists) {
			if (!value.equals("")) {
				writer.println(">>" + value);
                for (List<String> trig : trigger) {
					if (trig.get(1).contains(value)) {
					    if (Settings.oldFormatting) {
					        writer.println("trigger:" + trig.get(1));
					        writer.println("type:" + trig.get(0));
                        } else {
                            writer.println("trigger "+trig.get(0)+"("+trig.get(1)+") {");
                        }
						
						int tabbed_logic=0;
						for (int j=2; j<trig.size(); j++) {
							StringBuilder extraSpaces = new StringBuilder();
							String TMP_c = trig.get(j);
							
							if (TMP_c.equalsIgnoreCase("END")
							|| TMP_c.toUpperCase().startsWith("ELSE")) { 
								tabbed_logic--;
							}
							
								for (int k=0; k<tabbed_logic; k++) {extraSpaces.append("  ");}
								if (Settings.oldFormatting) {
							        writer.println(extraSpaces + "  event:"+trig.get(j));
                                } else {
                                    writer.println(extraSpaces + "  "+trig.get(j));
                                }

							
							if (TMP_c.toUpperCase().startsWith("IF") 
							|| TMP_c.toUpperCase().startsWith("FOR")
							|| TMP_c.toUpperCase().startsWith("CHOOSE")
							|| TMP_c.toUpperCase().startsWith("WAIT")
							|| TMP_c.toUpperCase().startsWith("ELSE")
							|| TMP_c.toUpperCase().startsWith("ASYNC")) {
								tabbed_logic++;
							}
						}
						if (!Settings.oldFormatting) {
                            writer.println("}");
                        }
					}
				}
				writer.println("<<"); writer.println(""); writer.println("");
			}
		}
		writer.println();
        for (List<String> trig : trigger) {
			if (!trig.get(1).contains("<list=") && !trig.get(1).contains("<imported>")) {
                if (Settings.oldFormatting) {
                    writer.println("trigger:" + trig.get(1));
                    writer.println("type:" + trig.get(0));
                } else {
                    writer.println("trigger "+trig.get(0)+"("+trig.get(1)+") {");
                }
				
				int tabbed_logic = 0;
				for (int j=2; j<trig.size(); j++) {
					StringBuilder extraSpaces = new StringBuilder();
					String TMP_c = trig.get(j);
					
					if (TMP_c.equalsIgnoreCase("END")
					|| TMP_c.toUpperCase().startsWith("ELSE")) { 
						tabbed_logic--;
					}
					
						for (int k=0; k<tabbed_logic; k++) {extraSpaces.append("  ");}
						if (Settings.oldFormatting) {
                            writer.println(extraSpaces + "  event:"+trig.get(j));
                        } else {
                            writer.println(extraSpaces + "  "+trig.get(j));
                        }

					
					if (TMP_c.toUpperCase().startsWith("IF") 
					|| TMP_c.toUpperCase().startsWith("FOR")
					|| TMP_c.toUpperCase().startsWith("CHOOSE")
					|| TMP_c.toUpperCase().startsWith("WAIT")
					|| TMP_c.toUpperCase().startsWith("ELSE")
					|| TMP_c.toUpperCase().startsWith("ASYNC")) {
                        tabbed_logic++;
                    }
				}
				if (!Settings.oldFormatting) {
                    writer.println("}");
                }
			}
		}
		writer.println("");
		writer.close();
	}
	
	private static void saveStrings(List<List<String>> listName) throws IOException {
		PrintWriter writer = new PrintWriter("./mods/ChatTriggers/strings.txt","UTF-8");
        for (List<String> string : listName) {
			writer.println("string:"+string.get(0));
			writer.println("  value:"+string.get(1));
			if (string.size()==3) {
				writer.println("  list:"+string.get(2));
			}
		}
		writer.close();
	}
	
	private static void saveSettings() throws IOException {
		PrintWriter writer = new PrintWriter("./mods/ChatTriggers/settings.txt","UTF-8");

		writer.println("color:"+Settings.col[0]);
		writer.println("colorName:"+Settings.col[1]);
		writer.println("version:"+Settings.CTversion);
		writer.println("killfeed pos:"+Settings.killfeedPosition[0] + " " + Settings.killfeedPosition[1]);
		writer.println("isBeta:"+Settings.isBeta);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		writer.println("lastOpened:"+dateFormat.format(date));
        writer.println("t:"+Settings.commandT);
        writer.println("tr:"+Settings.commandTR);
        writer.println("notification speed:"+Settings.notifySpeed);
        writer.println("killfeed fade:"+Settings.killfeedFade);
        writer.println("show killfeed in notifications:"+Settings.killfeedInNotify);
        writer.println("fpslow:"+global.fpslowcol + " " + global.fpslow);
        writer.println("fpsmed:"+global.fpsmedcol);
        writer.println("fpshigh:"+global.fpshighcol + " " + global.fpshigh);
        writer.println("old formatting:"+Settings.oldFormatting);

		writer.close();
	}
	
	public static List<List<String>> loadTriggers(String fileName, Boolean isImport, String importName) throws IOException {
		List<List<String>> tmp_triggers = new ArrayList<List<String>>();
		List<String> lines = loadFile(fileName);

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
			Boolean isTrigger = false;
			if (lines.get(i).startsWith("trigger ") && lines.get(i).contains("(") && lines.get(i).endsWith(") {")) {
			    String type = lines.get(i).substring(lines.get(i).indexOf("trigger ")+8, lines.get(i).indexOf("("));
			    String trigger = lines.get(i).substring(lines.get(i).indexOf("(")+1, lines.get(i).length()-3);

			    String importTag = "";
			    if (isImport) {
			        importTag="<imported>";
                }

                String typeExtra = "";
                if (lines.get(i+1) != null) {
			        if (lines.get(i+1).trim().startsWith("///")) {
			            typeExtra = " " + lines.get(i+1).trim().substring(3);
                    }
                }

                List<String> tmp_list = new ArrayList<String>();
                tmp_list.add(type+typeExtra);
			    tmp_list.add(importTag+trigger);

			    tmp_triggers.add(tmp_list);

			    isTrigger = true;
			    j++;
            }

            if (lines.get(i).startsWith("trigger ") && lines.get(i).contains("(") && lines.get(i).contains(") {") && lines.get(i).endsWith("}")) {
			    String type = lines.get(i).substring(lines.get(i).indexOf("trigger ")+8, lines.get(i).indexOf("("));
			    String trigger = lines.get(i).substring(lines.get(i).indexOf("(")+1, lines.get(i).indexOf(") {", lines.get(i).indexOf("(")));
			    String event = lines.get(i).substring(lines.get(i).indexOf(") {", lines.get(i).indexOf("("))+3, lines.get(i).length()-1);

                String importTag = "";
                if (isImport) {
                    importTag="<imported>";
                }

                List<String> tmp_list = new ArrayList<String>();
                tmp_list.add(type);
                tmp_list.add(importTag+trigger);
                tmp_list.add(event);

                tmp_triggers.add(tmp_list);

                isTrigger = true;
                j++;
            }
			
			if (lines.get(i).trim().startsWith("event:") && j>-1) {
				String tmp_event = lines.get(i).substring(lines.get(i).indexOf("event:") + 6);
				tmp_triggers.get(j).add(tmp_event);
			}
			if (!isTrigger) {
                if (!lines.get(i).trim().startsWith("//") && !lines.get(i).trim().equals("")) {
                    for (String event : CommandReference.getAllEventTypes()) {
                        if (!event.equals("")) {
                            if (lines.get(i).trim().startsWith(event + " ") || lines.get(i).trim().equals(event)) {
                                if (j != -1) {
                                    String tmp_event = lines.get(i).trim();
                                    tmp_triggers.get(j).add(tmp_event);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
			
			if (lines.get(i).trim().startsWith("!")) {
				String importFunction = lines.get(i).trim().substring(lines.get(i).trim().indexOf("!")+1);
				if (importFunction.toUpperCase().startsWith("CREATE STRING ") || importFunction.toUpperCase().startsWith("CREATESTRING ")) {
					String sn;
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
					if (global.debug) {
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
								if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Set value "+sv+" in string "+sn));}
							} else {if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "String already exists"));}}
							if (!svo.equals("")) {
								if (global.USR_string.get(k).get(1).equals("")) {
									global.USR_string.get(k).set(1, svo);
									if (!ln.equals("")) {
										if (global.USR_string.get(k).size()==3) {global.USR_string.get(k).set(2, ln);}
										else {global.USR_string.get(k).add(ln);}
									}
									if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Set value "+sv+" in string "+sn));}
								} else {if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "String already has value"));}}
							}
						}
					}
					if (canCreate) {
						List<String> temporary = new ArrayList<String>();
						temporary.add(sn);
						if (sv.equals("") && !svo.equals("")) {temporary.add(svo);} 
						else {temporary.add(sv);}
						if (!ln.equals("")) {temporary.add(ln);}
						global.USR_string.add(temporary);
						if (global.debug) {
							if (!sv.equals("")) {ChatHandler.warn(ChatHandler.color("gray", "Created string "+sn+" with value "+sv));}
							else {ChatHandler.warn(ChatHandler.color("gray", "Created string "+sn+" with no value"));}
						}
					}
					//try {saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
				} else if (importFunction.toUpperCase().startsWith("REQUIRES ")) {
					String importValue = importFunction.substring(importFunction.indexOf("REQUIRES ")+9);
					String[] importValues = importValue.trim().split(" ");
                    for (String value : importValues) {
						if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Importing "+value));}
						File dir = new File("./mods/ChatTriggers/Imports/"+value+".txt");
						if (!dir.exists()) {
							global.neededImports.add(value);
						} else {
							if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Import already exsists"));}
						}
					}
				} else if (importFunction.toUpperCase().startsWith("DELETE STRING ") || importFunction.toUpperCase().startsWith("DELETESTRING")) {
					String sn;
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
				} else if (importFunction.toUpperCase().startsWith("VERSION ")) {
				    String version = importFunction.substring(importFunction.toUpperCase().indexOf("VERSION ")+8);
                    if (importName != null) {
                        global.imported.put(importName, version);
                    }
                }
			}
		}

        for (List<String> value : tmp_triggers) {
			CommandReference.addToTriggerList(value);
		}
		
		
		return tmp_triggers;
	}
	
	public static List<List<String>> loadStrings() throws IOException {
		List<List<String>> tmp_strings = new ArrayList<List<String>>();
		List<String> lines = loadFile("./mods/ChatTriggers/strings.txt");
		
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
	
	public static void loadSettings() throws IOException {
		List<String> lines = loadFile("./mods/ChatTriggers/settings.txt");

        for (String l : lines) {
			if (l.startsWith("color:")) {Settings.col[0] = l.substring(l.indexOf("color:") + 6);}
			if (l.startsWith("colorName:")) {Settings.col[1] = l.substring(l.indexOf("colorName:") + 10);}
			if (l.startsWith("version:")) {Settings.CTversion = l.substring(l.indexOf("version:") + 8);}
			if (l.startsWith("killfeed pos:")) {
                String temp = l.substring(l.indexOf("killfeed pos:")+13);
                try {
                    String[] xy = temp.split(" ");
                    Settings.killfeedPosition[0] = Double.parseDouble(xy[0]);
                    Settings.killfeedPosition[1] = Double.parseDouble(xy[1]);
                } catch (Exception e) {
                    ScaledResolution var5 = new ScaledResolution(Minecraft.getMinecraft());
                    float width = var5.getScaledWidth();
                    float height = var5.getScaledHeight();
                    if (temp.equalsIgnoreCase("TR") || temp.equalsIgnoreCase("TOP-RIGHT")) {
                        Settings.killfeedPosition[0] = 5.0/width;
                        Settings.killfeedPosition[1] = 5.0/height;
                    } else {
                        Settings.killfeedPosition[0] = (width - 5.0)/width;
                        Settings.killfeedPosition[1]= 5.0/height;
                    }
                }
            }
			if (l.startsWith("isBeta:")) {
                Settings.isBeta = l.substring(l.indexOf("isBeta:")+7).trim().equalsIgnoreCase("true");
            }
			if (l.startsWith("lastOpened:")) {
			    global.currentDate = l.substring(l.indexOf("lastOpened:")+11);
			    Settings.lastOpened = global.currentDate;
			}
            if (l.startsWith("t:")) {
                Settings.commandT = l.substring(l.indexOf("t:")+2).trim().equalsIgnoreCase("true");
			}
            if (l.startsWith("tr:")) {
			    Settings.commandTR = l.substring(l.indexOf("tr:")+3).trim().equalsIgnoreCase("true");
			}
            if (l.startsWith("notification speed:")) {
                Settings.notifySpeed = Integer.parseInt(l.substring(l.indexOf("notification speed:")+19));
                try {
                    Settings.notifySpeed = Integer.parseInt(l.substring(l.indexOf("notification speed:")+19));
                    if (Settings.notifySpeed <= 1) {
                        Settings.notifySpeed = 10;
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ChatHandler.warn(ChatHandler.color("red","You need to set the notification speed to an integer greater than 0!"));
                }
            }
            if (l.startsWith("killfeed fade:")) {
			    Settings.killfeedFade = l.substring(l.indexOf("killfeed fade:")+14).trim().equalsIgnoreCase("true");
			}
            if (l.startsWith("show killfeed in notifications:")) {
			    Settings.killfeedInNotify = l.substring(l.indexOf("show killfeed in notifications:")+31).trim().equalsIgnoreCase("true");
			}
            if (l.startsWith("fpslow:")) {
                String[] get = l.substring(l.indexOf("fpslow:")+7).split(" ");
                if (get.length == 2) {
                    global.fpslowcol = get[0];
                    global.fpslow = Integer.parseInt(get[1]);
                } else {
                    global.fpslowcol = "&c";
                    global.fpslow = 50;
                    throw new NumberFormatException();
                }
            }
            if (l.startsWith("fpsmed:")) {
                String[] get = l.substring(l.indexOf("fpsmed:")+7).split(" ");
                if (get.length == 1) {
                    global.fpsmedcol = get[0];
                } else {
                    global.fpsmedcol = "&e";
                    throw new NumberFormatException();
                }
            }
            if (l.startsWith("fpshigh:")) {
                String[] get = l.substring(l.indexOf("fpshigh:")+8).split(" ");
                if (get.length == 2) {
                    global.fpshighcol = get[0];
                    global.fpshigh = Integer.parseInt(get[1]);
                } else {
                    global.fpshighcol = "&a";
                    global.fpshigh = 60;
                    throw new NumberFormatException();
                }
            }
            if (l.startsWith("old formatting:")) {
			    String get = l.substring(l.indexOf("old formatting:")+15).trim();
			    Settings.oldFormatting = get.equalsIgnoreCase("true");
            }
		}
	}
	
	public static void saveAll() throws IOException {
		if (global.canSave) {
			saveTriggers(global.trigger);
			saveStrings(global.USR_string);
			saveSettings();
			
			CommandReference.clearTriggerList();
			
			global.trigger = FileHandler.loadTriggers("./mods/ChatTriggers/triggers.txt", false, null);
			global.USR_string = FileHandler.loadStrings();
			FileHandler.loadSettings();
			loadImports();
		} else {
			ChatHandler.warn(ChatHandler.color("red", "These changes are not getting saved!"));
			ChatHandler.warn(ChatHandler.color("red", "do </trigger load> to leave testing"));
		}
	}
	
	private static void startup() throws ClassNotFoundException {
		ChatHandler.warn(ChatHandler.color("gray", "Loading ChatTriggers..."));
		try {
			CommandReference.clearTriggerList();
			global.trigger = loadTriggers("./mods/ChatTriggers/triggers.txt", false, null);
			global.USR_string = loadStrings();
			loadSettings();
			loadImports();
			ChatHandler.warn(ChatHandler.color(Settings.col[0], "ChatTriggers loaded"));
		} catch (IOException e1) {
			ChatHandler.warn(ChatHandler.color("gold", "Setting up missing files..."));
			File checkFile = new File("./mods/ChatTriggers");
			if (!checkFile.exists()) {
			    if (!checkFile.mkdir()) {
			        ChatHandler.warn(ChatHandler.color("red", "Something went wrong while creating the files!"));
			    }
			}

			
			try {saveAll(); ChatHandler.warn(ChatHandler.color("green", "New files created!"));} 
			catch (IOException e111) {ChatHandler.warn(ChatHandler.color("red", "Error saving files!")); e111.printStackTrace();}
            ChatHandler.warn(ChatHandler.color(Settings.col[0], "ChatTriggers loaded"));
		}
		try {saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
	}
	
	public static void firstFileLoad() {
		if (global.tick==0) {
			global.tick=1;
			try {startup();} catch (ClassNotFoundException e) {e.printStackTrace();}

	    	if (Settings.isBeta) {UpdateHandler.loadVersion("http://chattriggers.kerbybit.com/download/betaversion.txt");}
	    	else {UpdateHandler.loadVersion("http://chattriggers.kerbybit.com/download/version.txt");}

            UpdateHandler.getCanUse("http://www.kerbybit.com/blacklist/", "http://www.kerbybit.com/enabledmods/", "http://ct.kerbybit.com/creators/");
		}
	}
	
	public static void tickImports() {
		if (global.neededImports.size()>0 && global.canImport) {
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
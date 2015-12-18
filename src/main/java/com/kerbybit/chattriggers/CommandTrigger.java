package com.kerbybit.chattriggers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class CommandTrigger extends CommandBase {

	public String getName() {return "trigger";}
	
	public int getRequiredPermissionLevel() {return 0;}

	public String getCommandUsage(ICommandSender sender) {return "/trigger [create/add/list] <...>";}

	public void execute(ICommandSender sender, String[] args) throws CommandException {doCommand(args, false);}
	
	public static void doCommand(String args[], Boolean silent) {
		if (args.length == 0) {
			chat.warn(chat.color("red", "/trigger [create/add/list] <...>"));
			chat.warn(chat.color("red", "/trigger [string/run] <...>"));
			chat.warn(chat.color("red", "/trigger [save/load]"));
			chat.warn(chat.color("red", "/trigger [import/export] <...>"));
		} else if (args[0].equalsIgnoreCase("IMPORT")) {
			commandImport(args, silent);
		} else if (args[0].equalsIgnoreCase("EXPORT")) {
			commandExport(args, silent);
		} else if (args[0].equalsIgnoreCase("RUN")) {
			commandRun(args, silent);
		} else if (args[0].equalsIgnoreCase("CREATE")) {
			commandCreate(args, silent);
		} else if (args[0].equalsIgnoreCase("DELETE")) {
			commandDelete(args, silent);
		} else if (args[0].equalsIgnoreCase("ADD")) {
			commandAdd(args, silent);
		} else if (args[0].equalsIgnoreCase("REMOVE")) {
			commandRemove(args, silent);
		} else if (args[0].equalsIgnoreCase("STRING")) {
			commandString(args, silent);
		} else if (args[0].equalsIgnoreCase("LIST")) {
			commandList(args, silent);
		} else if (args[0].equalsIgnoreCase("SETTINGS")) {
			commandSettings(args, silent);
		} else if (args[0].equalsIgnoreCase("SAVE")) {
			commandSave(args, silent);
		} else if (args[0].equalsIgnoreCase("LOAD")) {
			commandLoad(args, silent);
		} else {
			chat.warn(chat.color("red", "/trigger [create/add/list] <...>"));
			chat.warn(chat.color("red", "/trigger [string/run] <...>"));
			chat.warn(chat.color("red", "/trigger [save/load]"));
			chat.warn(chat.color("red", "/trigger [import/export] <...>"));
		}
	}
	
	public static void commandImport(String args[], Boolean silent) {
		String toImport = args[1];
		chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------"));
		chat.warn(chat.color("gray", "Importing") + chat.color(global.settings.get(1), toImport));
		file.loadImport("http://65.27.55.168:88/Import/server/php/files/" + toImport + ".txt");
		chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------&r" + global.settings.get(0) + "^"));
	}
	
	public static void commandExport(String args[], Boolean silent) {
		String TMP_list = args[1];
		List<List<String>> TMP_triggers = new ArrayList<List<String>>();
		for (int i=0; i<global.trigger.size(); i++) {
			String TMP_trig = global.trigger.get(i).get(1);
			if (TMP_trig.contains("{list=" + TMP_list + "}")) {
				List<String> TMP_tset = new ArrayList<String>();
				TMP_tset.addAll(global.trigger.get(i));
				String TMP_t = TMP_tset.get(1);
				TMP_t = TMP_t.replace("{list=" + TMP_list + "}",  "{list=" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "-" + TMP_list + "}");
				TMP_tset.set(1, TMP_t);
				TMP_triggers.add(TMP_tset);
			}
		}
		chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------"));
		chat.warn(chat.color("gray", "Saving list") + chat.color(global.settings.get(0),TMP_list) + chat.color("gray", "for export"));
		try {
			File checkFile = new File("./mods/ChatTriggersExport");
			if (checkFile.exists()) {
				file.saveExport(TMP_triggers, global.USR_string, "./mods/ChatTriggersExport/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "-" + TMP_list + ".txt");
			} else {
				checkFile.mkdir();
				file.saveExport(TMP_triggers, global.USR_string, "./mods/ChatTriggersExport/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "-" + TMP_list + ".txt");
			}
			chat.warn(chat.color("gray", "Head over to"));
			List<String> TMP_out = new ArrayList<String>();
			TMP_out.add("text:' http://kerbybit.github.io/ChatTriggers/import/upload/',color:" + global.settings.get(1) + ",hoverEvent:{action:'show_text',value:'Click to open URL'},clickEvent:{action:'open_url',value:'http://kerbybit.github.io/ChatTriggers/import/upload/'}");
			chat.sendJson(TMP_out);
			chat.warn(chat.color("gray", "to upload"));
			chat.warn(chat.color(global.settings.get(0), chat.color(global.settings.get(0), " ./minecraft/mods/ChatTriggersExport/" + Minecraft.getMinecraft().thePlayer.getDisplayNameString() + "-" + TMP_list + ".txt")));
		} catch (IOException e) {
			chat.warn(chat.color("red", "Error saving export"));
			e.printStackTrace();
		}
		chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------&r" + global.settings.get(0) + "^"));
	}
	
	public static void commandRun(String args[], Boolean silent) {
		if (args.length < 2) {
			chat.warn(chat.color("red", "/trigger run [trigger]"));
		} else {
			String TMP_e = "";
			for (int i=1; i<args.length; i++) {
				if (i==args.length-1) {TMP_e += args[i];} 
				else {TMP_e += args[i] + " ";}
			}
			ClientChatReceivedEvent e = null;
			try {
				int num = Integer.parseInt(args[1]);
				if (num >= 0 && num < global.trigger.size()) {
					List<String> TMP_events = new ArrayList<String>();
					for (int i=2; i<global.trigger.get(num).size(); i++) {
						TMP_events.add(global.trigger.get(num).get(i));
					}
					events.doEvents(TMP_events, e);
				}
			} catch (NumberFormatException e1) {
				for (int k=0; k<global.trigger.size(); k++) {
					String TMP_trig = global.trigger.get(k).get(1);
					String TMP_list = "";
					TMP_trig = TMP_trig.replace("{s}", ""); 
					TMP_trig = TMP_trig.replace("{c}", ""); 
					TMP_trig = TMP_trig.replace("{e}", "");
					if (TMP_trig.contains("{list=")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("=")+1, TMP_trig.indexOf("}")); TMP_trig = TMP_trig.replace("{list="+TMP_list+"}","");}
					if (TMP_trig.equals(TMP_e)) {
						List<String> TMP_events = new ArrayList<String>();
						for (int i=2; i<global.trigger.get(k).size(); i++) {
							TMP_events.add(global.trigger.get(k).get(i));
						}
						events.doEvents(TMP_events, e);
					} else {
						String TMP_trigtype = global.trigger.get(k).get(0);
						if (TMP_trigtype.equalsIgnoreCase("OTHER")) {
							if (TMP_trig.contains("(") && TMP_trig.endsWith(")")) {
								String TMP_trigtest = TMP_trig.substring(0,TMP_trig.indexOf("("));
								if (TMP_e.startsWith(TMP_trigtest) && TMP_e.endsWith(")")) {
									String TMP_argsIn = TMP_e.substring(TMP_e.indexOf("(")+1, TMP_e.length()-1);
									String TMP_argsOut = TMP_trig.substring(TMP_trig.indexOf("(")+1, TMP_trig.length()-1);
									String[] argsIn = TMP_argsIn.split(",");
									String[] argsOut = TMP_argsOut.split(",");
									if (argsIn.length == argsOut.length) {
										List<String> TMP_events = new ArrayList<String>();
										for (int j=2; j<global.trigger.get(k).size(); j++) {
											TMP_events.add(global.trigger.get(k).get(j));
										}
										events.doEvents(TMP_events, e, argsOut, argsIn);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void commandCreate(String args[], Boolean silent) {
		if (args.length < 3) {
			chat.warn(chat.color("red", "/trigger create [type] [trigger]"));
		} else {
			String TMP_type = args[1];
			String TMP_trig = "";
			for (int i=2; i<args.length; i++) {
				if (i==args.length-1) {TMP_trig += args[i];}
				else {TMP_trig += args[i] + " ";}
			}
			List<String> TMP_l = new ArrayList<String>();
			TMP_l.add(TMP_type);
			TMP_l.add(TMP_trig);
			if (TMP_type.equalsIgnoreCase("CHAT") 
			|| TMP_type.equalsIgnoreCase("OTHER")) {
				global.trigger.add(TMP_l);
				int TMP_num = global.trigger.size() - 1;
				if (silent==false) {
					List<String> TMP_out = new ArrayList<String>();
					TMP_trig = TMP_trig.replace("'", "\\'");
					TMP_type = TMP_type.replace("'", "\\'");
					TMP_out.add("text:'Created trigger ',color:gray");
					TMP_out.add("text:'" + TMP_trig + " ',color:"+global.settings.get(1));
					TMP_out.add("text:'with trigger type ',color:gray");
					TMP_out.add("text:'" + TMP_type + " ',color:"+global.settings.get(1));
					TMP_out.add("text:'(" + TMP_num + ")',color:gray,hoverEvent:{action:'show_text',value:'Add an event'},clickEvent:{action:'suggest_command',value:'/trigger add " + TMP_num + " '}");
					chat.sendJson(TMP_out);
				}
				try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
			} else {
				chat.warn(chat.color("red", "Not a valid trigger type"));
			}
		}
	}
	
	public static void commandDelete(String args[], Boolean silent) {
		if (args.length < 2) {
			chat.warn(chat.color("red", "/trigger delete [trigger number]"));
		} else {
			int num = -1;
			try {num = Integer.parseInt(args[1]);} 
			catch(NumberFormatException e) {num = -1;}
			if (num>-1 && num<global.trigger.size()) {
				String TMP_rem = global.trigger.remove(num).get(1);
				if (silent==false) {
					chat.warnUnformatted(chat.color("gray", "Deleted trigger") + chat.color(global.settings.get(0), TMP_rem) + chat.color("gray", "and all of its events"));
				}
				try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
			} else {
				chat.warn(chat.color("red", "/trigger delete [trigger number]"));
			}
		}
	}
	
	public static void commandAdd(String args[], Boolean silent) {
		if (args.length < 3) {
			chat.warn(chat.color("red", "/trigger add [trigger number] [event] [event argument(s)]"));
		} else {
			int num = -1;
			try {num = Integer.parseInt(args[1]);} 
			catch(NumberFormatException e) {num = -1;}
			String TMP_e = "";
			for (int i=2; i<args.length; i++) {
				if (i==args.length-1) {TMP_e += args[i];}
				else {TMP_e += args[i] + " ";}
			}
			if (num>-1 && num<global.trigger.size()) {
				String TMP_etype = TMP_e;
				if (TMP_e.contains(" ")) {TMP_etype = TMP_e.substring(0,TMP_e.indexOf(" "));}
				
				if (TMP_etype.equalsIgnoreCase("CHAT") 
				|| TMP_etype.equalsIgnoreCase("SAY") 
				|| TMP_etype.equalsIgnoreCase("CANCEL") 
				|| TMP_etype.equalsIgnoreCase("CHOOSE") 
				|| TMP_etype.equalsIgnoreCase("KILLFEED")
				|| TMP_etype.equalsIgnoreCase("NOTIFY")
				|| TMP_etype.equalsIgnoreCase("TRIGGER") 
				|| TMP_etype.equalsIgnoreCase("SOUND")
				|| TMP_etype.equalsIgnoreCase("COMMAND")
				|| TMP_etype.equalsIgnoreCase("DO")
				|| TMP_etype.equalsIgnoreCase("IF")
				|| TMP_etype.equalsIgnoreCase("ELSE")
				|| TMP_etype.equalsIgnoreCase("ELSEIF")
				|| TMP_etype.equalsIgnoreCase("FOR")
				|| TMP_etype.equalsIgnoreCase("WAIT")
				|| TMP_etype.equalsIgnoreCase("END")) {
					global.trigger.get(num).add(TMP_e);
					if (silent==false) {
						chat.warnUnformatted(chat.color("gray", "Added event") + chat.color(global.settings.get(0), TMP_e) + chat.color("gray", "to trigger") + chat.color(global.settings.get(0), global.trigger.get(num).get(1)));
					}
					try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
				} else {
					chat.warn(chat.color("red", "Not a valid event type!"));
				}
				
			} else {
				chat.warn(chat.color("red", "/trigger add [trigger number] [event] [event argument(s)]"));
			}
		}
	}
	
	public static void commandRemove(String args[], Boolean silent) {
		if (args.length < 3) {
			chat.warn(chat.color("red", "/trigger remove [trigger number] [event number]"));
		} else {
			int num = -1;
			try {num = Integer.parseInt(args[1]);} 
			catch(NumberFormatException e) {num = -1;}
			if (num>-1 && num<global.trigger.size()) {
				int num2 = 1;
				try {num2 = Integer.parseInt(args[2]);}
				catch(NumberFormatException e) {num2 = 1;}
				if (num2>1 && num2<global.trigger.get(num).size()) {
					String TMP_rem = global.trigger.get(num).remove(num2);
					if (silent==false) {
						chat.warnUnformatted(chat.color("gray", "Removed event") + chat.color(global.settings.get(0), TMP_rem) + chat.color("gray", "from trigger") + chat.color(global.settings.get(0), global.trigger.get(num).get(1)));
					}
					try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
				} else {
					chat.warn(chat.color("red", "/trigger remove [trigger number] [event number]"));
				}
			} else {
				chat.warn(chat.color("red", "/trigger remove [trigger number] [event number]"));
			}
		}
	}
	
	public static void commandString(String args[], Boolean silent) {
		if (args.length < 2) {
			chat.warn(chat.color("red", "/trigger string [create/set/list]"));
		} else {
			if (args[1].equalsIgnoreCase("CREATE")) {
				if (args.length != 3) {
					chat.warn(chat.color("red", "/trigger string create [string name]"));
				} else {
					String TMP_sn = args[2];
					List<String> TMP_l = new ArrayList<String>();
					TMP_l.add(TMP_sn);
					TMP_l.add("");
					global.USR_string.add(TMP_l);
					if (silent==false) {
						chat.warnUnformatted(chat.color("gray", "Added string") + chat.color(global.settings.get(0), TMP_sn));
					}
					try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
				}
			} else if (args[1].equalsIgnoreCase("DELETE")) {
				if (args.length < 3) {
					chat.warn(chat.color("red", "/trigger string delete [string name]"));
				} else {
					int num = -1;
					try {num = Integer.parseInt(args[2]);}
					catch(NumberFormatException e) {
						for (int i=0; i<global.USR_string.size(); i++) {
							if (args[2].equals(global.USR_string.get(i).get(0))) {
								num = i;
							}
						}
					}
					if (num>-1 && num<global.USR_string.size()) {
						String TMP_rem = global.USR_string.remove(num).get(0);
						if (silent==false) {
							chat.warnUnformatted(chat.color("gray", "Deleted string") + chat.color(global.settings.get(0), TMP_rem));
						}
						try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
					}
				}
			} else if (args[1].equalsIgnoreCase("SET")) {
				if (args.length < 4) {
					chat.warn(chat.color("red", "/trigger string set [string name] [string]"));
				} else {
					int num = -1;
					try {num = Integer.parseInt(args[2]);} 
					catch(NumberFormatException e) {
						for (int i=0; i<global.USR_string.size(); i++) {
							if (args[2].equals(global.USR_string.get(i).get(0))) {
								num = i;
							}
						}
					}
					String TMP_s = "";
					for (int i=3; i<args.length; i++) {
						if (i==args.length-1) {TMP_s += args[i];}
						else {TMP_s += args[i] + " ";}
					}
					if (num>-1 && num<global.USR_string.size()) {
						if (silent==false) {
							chat.warnUnformatted(chat.color("gray", "Set value") + chat.color(global.settings.get(0), TMP_s) + chat.color("gray", "in string") + chat.color(global.settings.get(0), global.USR_string.get(num).get(0)));
						}
						if (TMP_s.equals("{null}")) {TMP_s = "";}
						global.USR_string.get(num).set(1,TMP_s);
						try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
					} else {
						chat.warn(chat.color("red", "/trigger string set [string number] [string]"));
					}
				}
			} else if (args[1].equalsIgnoreCase("LIST")) {
				if (args.length==2) {
					chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------"));
					if (global.USR_string.size() == 0) {
						chat.warnUnformatted(chat.color("red", "No strings created"));
						chat.warnUnformatted(chat.color("red", "Do </trigger string> to get started"));
					} else {
						for (int i=0; i<global.USR_string.size(); i++) {
							List<String> TMP_out = new ArrayList<String>();
							String TMP_sn = global.USR_string.get(i).get(0);
							TMP_sn = TMP_sn.replace("'", "\\'");
							TMP_out.add("text:'" + i + "> ',color:gray,hoverEvent:{action:'show_text',value:'Set string'},clickEvent:{action:'suggest_command',value:'/trigger string set " + i + " '}");
							TMP_out.add("text:'" + TMP_sn + " ',color:"+global.settings.get(1));
							TMP_out.add("text:'-',color:red,hoverEvent:{action:'show_text',value:'Delete string'},clickEvent:{action:'suggest_command',value:'/trigger string delete " + i + " [enter to confirm]'}");
							chat.sendJson(TMP_out);
							chat.warnUnformatted(chat.color("gray", "  " + global.USR_string.get(i).get(1)));
						}
					}
					chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------&r" + global.settings.get(0) + "^"));
				}
			}
		}
	}
	
	public static void commandList(String args[], Boolean silent) {
		if (args.length==1) {
			chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------"));
			if (global.trigger.size() == 0) {
				chat.warn(chat.color("red", "No triggers created"));
				chat.warn(chat.color("red", "Do </trigger> to get started"));
			} else {
				List<String> TMP_lists = new ArrayList<String>();
				for (int i=0; i<global.trigger.size(); i++) {
					String TMP_type = global.trigger.get(i).get(0);
					String TMP_trig = global.trigger.get(i).get(1);
					String TMP_list = "";
					String TMP_w    = "";
					
					if (TMP_trig.contains("{s}")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("{s}", "");}
					if (TMP_trig.contains("{c}")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("{c}", "");}
					if (TMP_trig.contains("{e}")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("{e}", "");}
					if (TMP_trig.contains("<s>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<s>", "");}
					if (TMP_trig.contains("<c>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<c>", "");}
					if (TMP_trig.contains("<e>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<e>", "");}
					if (TMP_trig.contains("<start>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<start>", "");}
					if (TMP_trig.contains("<contain>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<contain>", "");}
					if (TMP_trig.contains("<end>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<end>", "");}
					
					if (TMP_trig.contains("{list=") && TMP_trig.contains("}")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("{list=")+6, TMP_trig.indexOf("}", TMP_trig.indexOf("{list="))); TMP_trig = TMP_trig.replace("{list="+TMP_list+"}","");}
					if (TMP_trig.contains("<list=") && TMP_trig.contains(">")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<list=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))); TMP_trig = TMP_trig.replace("<list="+TMP_list+">","");}
					TMP_lists.add(TMP_list);
					
					if (TMP_list.equals("")) {
						List<String> TMP_out = new ArrayList<String>();
						TMP_type = TMP_type.replace("'", "\\'");
						TMP_trig = TMP_trig.replace("'", "\\'");
						TMP_list = TMP_list.replace("'", "\\'");
						TMP_out.add("text:'" + i + "> ',color:gray,hoverEvent:{action:'show_text',value:'Add an event'},clickEvent:{action:'suggest_command',value:'/trigger add " + i + " '}");
						TMP_out.add("text:'" + TMP_type + " ',color:dark_gray");
						TMP_out.add("text:'" + TMP_trig + " ',color:"+global.settings.get(1));
						if (!TMP_list.equals("")) {TMP_out.add("text:'" + TMP_list + " ',color:gray");}
						if (!TMP_w.equals("")) {TMP_out.add("text:'" + TMP_w + " ',color:dark_gray");}
						TMP_out.add("text:'-',color:red,hoverEvent:{action:'show_text',value:'Remove trigger'},clickEvent:{action:'suggest_command',value:'/trigger delete " + i + " [enter to confirm]'}");
						chat.sendJson(TMP_out);
						
						int tabbed_logic = 0;
						
						for (int j=2; j<global.trigger.get(i).size(); j++) {
							
							String TMP_extraspaces = "";
							
							String TMP_e = global.trigger.get(i).get(j);
							String TMP_c = "";
							if (!TMP_e.contains(" ")) {TMP_c = TMP_e; TMP_e = "";}
							else {TMP_c = TMP_e.substring(0, TMP_e.indexOf(" ")); TMP_e = TMP_e.substring(TMP_e.indexOf(" ") + 1, TMP_e.length());}
							
							if (TMP_c.equalsIgnoreCase("END")
							|| TMP_c.toUpperCase().startsWith("ELSE")) { 
								tabbed_logic--;
							}
							
							
								for (int k=0; k<tabbed_logic; k++) {TMP_extraspaces+= "  ";}
								List<String> TMP_eventout = new ArrayList<String>();
								TMP_c = TMP_c.replace("'", "\\'");
								TMP_e = TMP_e.replace("'", "\\'");
								TMP_eventout.add("text:'" + TMP_extraspaces + "   '");
								TMP_eventout.add("text:'-',color:red,hoverEvent:{action:'show_text',value:'Remove event'},clickEvent:{action:'suggest_command',value:'/trigger remove " + i + " " + j + " [enter to confirm]'}");
								TMP_eventout.add("text:' " + TMP_c + " ',color:dark_gray");
								TMP_eventout.add("text:'" + TMP_e + "',color:gray");
								chat.sendJson(TMP_eventout);
							
							
							if (TMP_c.toUpperCase().startsWith("IF") 
							|| TMP_c.toUpperCase().startsWith("FOR")
							|| TMP_c.toUpperCase().startsWith("CHOOSE")
							|| TMP_c.toUpperCase().startsWith("WAIT")
							|| TMP_c.toUpperCase().startsWith("ELSE")) {
								tabbed_logic++;
							}
						}
					}
				}
				Set<String> uniqueTMP_lists = new HashSet<String>(TMP_lists);
				for (String value : uniqueTMP_lists) {
					if (!value.equals("")) {
						List<String> TMP_out = new ArrayList<String>();
						TMP_out.add("text:'List> ',color:gray,hoverEvent:{action:'show_text',value:'Show " + value + " triggers'},clickEvent:{action:'run_command',value:'/trigger list " + value + " '}");
						TMP_out.add("text:'" + value + " ',color:"+global.settings.get(1));
						chat.sendJson(TMP_out);
					}
				}
			}
			chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------&r" + global.settings.get(0) + "^"));
		} else {
			String TMP_check = "";
			for (int i=1; i<args.length; i++) {
				if (i==args.length-1) {TMP_check += args[i];}
				else {TMP_check += args[i] + " ";}
			}
			chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------"));
			if (global.trigger.size() == 0) {
				chat.warn(chat.color("red", "No triggers created"));
				chat.warn(chat.color("red", "Do </trigger> to get started"));
			} else {
				int TMP_test = 0;
				for (int i=0; i<global.trigger.size(); i++) {
					String TMP_type = global.trigger.get(i).get(0);
					String TMP_trig = global.trigger.get(i).get(1);
					String TMP_list = "";
					String TMP_w    = "";
					
					if (TMP_trig.contains("{s}")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("{s}", "");}
					if (TMP_trig.contains("{c}")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("{c}", "");}
					if (TMP_trig.contains("{e}")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("{e}", "");}
					if (TMP_trig.contains("<s>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<s>", "");}
					if (TMP_trig.contains("<c>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<c>", "");}
					if (TMP_trig.contains("<e>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<e>", "");}
					if (TMP_trig.contains("<start>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<start>", "");}
					if (TMP_trig.contains("<contain>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<contain>", "");}
					if (TMP_trig.contains("<end>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<end>", "");}
					
					if (TMP_trig.contains("{list=") && TMP_trig.contains("}")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("{list=")+6, TMP_trig.indexOf("}", TMP_trig.indexOf("{list="))); TMP_trig = TMP_trig.replace("{list="+TMP_list+"}","");}
					if (TMP_trig.contains("<list=") && TMP_trig.contains(">")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<list=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))); TMP_trig = TMP_trig.replace("<list="+TMP_list+">","");}
					
					if (TMP_check.equals(TMP_list)) {
						TMP_test++;
						
						List<String> TMP_out = new ArrayList<String>();
						TMP_type = TMP_type.replace("'", "\\'");
						TMP_trig = TMP_trig.replace("'", "\\'");
						TMP_list = TMP_list.replace("'", "\\'");
						TMP_out.add("text:'" + i + "> ',color:gray,hoverEvent:{action:'show_text',value:'Add an event'},clickEvent:{action:'suggest_command',value:'/trigger add " + i + " '}");
						TMP_out.add("text:'" + TMP_type + " ',color:dark_gray");
						TMP_out.add("text:'" + TMP_trig + " ',color:"+global.settings.get(1));
						if (!TMP_list.equals("")) {TMP_out.add("text:'" + TMP_list + " ',color:gray");}
						if (!TMP_w.equals("")) {TMP_out.add("text:'" + TMP_w + " ',color:dark_gray");}
						TMP_out.add("text:'-',color:red,hoverEvent:{action:'show_text',value:'Remove trigger'},clickEvent:{action:'suggest_command',value:'/trigger delete " + i + " [enter to confirm]'}");
						chat.sendJson(TMP_out);
						
						int tabbed_logic=0;
						
						for (int j=2; j<global.trigger.get(i).size(); j++) {
							
							String TMP_extraspaces = "";
							
							String TMP_e = global.trigger.get(i).get(j);
							String TMP_c = "";
							if (!TMP_e.contains(" ")) {TMP_c = TMP_e; TMP_e = "";}
							else {TMP_c = TMP_e.substring(0, TMP_e.indexOf(" ")); TMP_e = TMP_e.substring(TMP_e.indexOf(" ") + 1, TMP_e.length());}
							
							if (TMP_c.equalsIgnoreCase("END")
							|| TMP_c.toUpperCase().startsWith("ELSE")) { 
								tabbed_logic--;
							}
							
								for (int k=0; k<tabbed_logic; k++) {TMP_extraspaces+= "  ";}
								List<String> TMP_eventout = new ArrayList<String>();
								TMP_c = TMP_c.replace("'", "\\'");
								TMP_e = TMP_e.replace("'", "\\'");
								TMP_eventout.add("text:'" + TMP_extraspaces + "   '");
								TMP_eventout.add("text:'-',color:red,hoverEvent:{action:'show_text',value:'Remove event'},clickEvent:{action:'suggest_command',value:'/trigger remove " + i + " " + j + " [enter to confirm]'}");
								TMP_eventout.add("text:' " + TMP_c + " ',color:dark_gray");
								TMP_eventout.add("text:'" + TMP_e + "',color:gray");
								chat.sendJson(TMP_eventout);
								
							if (TMP_c.toUpperCase().startsWith("IF") 
							|| TMP_c.toUpperCase().startsWith("FOR")
							|| TMP_c.toUpperCase().startsWith("CHOOSE")
							|| TMP_c.toUpperCase().startsWith("WAIT")
							|| TMP_c.toUpperCase().startsWith("ELSE")) {
								tabbed_logic++;
							}
							
						}
					}
				}
				if (TMP_test==0) {
					chat.warn(chat.color("red", "There is no list with name " + TMP_check));
					chat.warn(chat.color("red", "Create list " + TMP_check + " by using <list=" + TMP_check + "> when you create a trigger"));
				}
			}
			chat.warn(chat.color(global.settings.get(0), "&m---------------------------------------------------&r" + global.settings.get(0) + "^"));
		}
	}
	
	public static void commandSettings(String args[], Boolean silent) {
		if (args.length < 2) {
			chat.warn(chat.color("red", "/trigger settings [debug/color]"));
		} else {
			if (args[1].equalsIgnoreCase("DEBUG")) {
				if (global.debug==false) {chat.warn(chat.color("gray", "Toggled debug mode") + chat.color(global.settings.get(0), "on")); global.debug=true;}
				else {chat.warn(chat.color("gray", "Toggled debug mode") + chat.color(global.settings.get(0), "off")); global.debug=false;}
			} else if (args[1].equalsIgnoreCase("COLOR")) {
				if (args.length < 3) {
					chat.warn(chat.color("red", "/trigger settings color [color]"));
				} else {
					args[2] = args[2].replace("_", "");
					args[2] = args[2].replace("&", "");
					if (args.length==4) {args[2] = args[2]+args[3];}
					if      (args[2].equalsIgnoreCase("0") || args[2].equalsIgnoreCase("BLACK"))       {global.settings.set(0, "&0"); global.settings.set(1, "black");        chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("DARKBLUE"))    {global.settings.set(0, "&1"); global.settings.set(1, "dark_blue");    chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("2") || args[2].equalsIgnoreCase("DARKGREEN"))   {global.settings.set(0, "&2"); global.settings.set(1, "dark_green");   chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("3") || args[2].equalsIgnoreCase("DARKAQUA"))    {global.settings.set(0, "&3"); global.settings.set(1, "dark_aqua");    chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("4") || args[2].equalsIgnoreCase("DARKRED"))     {global.settings.set(0, "&4"); global.settings.set(1, "dark_red");     chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("5") || args[2].equalsIgnoreCase("DARKPURPLE"))  {global.settings.set(0, "&5"); global.settings.set(1, "dark_purple");  chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("6") || args[2].equalsIgnoreCase("GOLD"))        {global.settings.set(0, "&6"); global.settings.set(1, "gold");         chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("7") || args[2].equalsIgnoreCase("GRAY"))        {global.settings.set(0, "&7"); global.settings.set(1, "gray");         chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("8") || args[2].equalsIgnoreCase("DARKGRAY"))    {global.settings.set(0, "&8"); global.settings.set(1, "dark_gray");    chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("9") || args[2].equalsIgnoreCase("BLUE"))        {global.settings.set(0, "&9"); global.settings.set(1, "blue");         chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("a") || args[2].equalsIgnoreCase("GREEN"))       {global.settings.set(0, "&a"); global.settings.set(1, "green");        chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("b") || args[2].equalsIgnoreCase("AQUA"))        {global.settings.set(0, "&b"); global.settings.set(1, "aqua");         chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("c") || args[2].equalsIgnoreCase("RED"))         {global.settings.set(0, "&c"); global.settings.set(1, "red");          chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("d") || args[2].equalsIgnoreCase("LIGHTPURPLE")) {global.settings.set(0, "&d"); global.settings.set(1, "light_purple"); chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("e") || args[2].equalsIgnoreCase("YELLOW"))      {global.settings.set(0, "&e"); global.settings.set(1, "yellow");       chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else if (args[2].equalsIgnoreCase("f") || args[2].equalsIgnoreCase("WHITE"))       {global.settings.set(0, "&f"); global.settings.set(1, "white");        chat.warn(chat.color(global.settings.get(0), "Changed color"));}
					else {chat.warn(chat.color("red", "Not a valid color"));}
					try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
				}
			} else {
				chat.warn(chat.color("red", "/trigger settings [debug/color]"));
			}
		}
	}
	
	public static void commandSave(String args[], Boolean silent) {
		try {
			file.saveAll();
			global.trigger = file.loadTriggers("./mods/ChatTriggers/triggers.txt");
			global.USR_string = file.loadStrings("./mods/ChatTriggers/strings.txt");
			global.settings = file.loadSettings("./mods/ChatTriggers/settings.txt");
			chat.warn(chat.color(global.settings.get(0), "Organized and saved files"));
		} catch (IOException e) {
			chat.warn(chat.color("red", "Error saving triggers!"));
		}
	}
	
	public static void commandLoad(String args[], Boolean silent) {
		try {
			global.trigger = file.loadTriggers("./mods/ChatTriggers/triggers.txt");
			global.USR_string = file.loadStrings("./mods/ChatTriggers/strings.txt");
			global.settings = file.loadSettings("./mods/ChatTriggers/settings.txt");
			chat.warn(global.settings.get(0) + "Files loaded");
		} catch (IOException e) {chat.warn(chat.color("red", "Error loading triggers!"));}
	}
	
	

}

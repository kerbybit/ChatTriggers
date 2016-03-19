package com.kerbybit.chattriggers;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class chat {
	public static void warnUnformatted(String cht) {
		cht = cht.replace("'", "\\'");
		cht = cht.replace("\"", "\\\"");
		String TMP_o = "[\"\",";
		TMP_o += "{\"text\":\"" + cht +  "\"}";
		TMP_o += "]";
		ITextComponent TMP_out = ITextComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}
	
	public static void warnUnformClick(String cht) {
		cht = cht.replace("'('", "LeftParF6cyUQp9LeftPar");
		cht = cht.replace("')'", "RightParF6cyUQp9RightPar");
		cht = cht.replace("','", "commaF6cyUQp9comma");
		cht = cht.replace("'", "\\'");
		cht = cht.replace("\"", "\\\"");
		
		while (cht.contains("clickable(") && cht.contains(")")) {
			String TMP_1splt = "";
			String TMP_2splt = "";
			if (cht.indexOf("clickable(") != 0) {TMP_1splt = ",";}
			if (cht.indexOf(")") != cht.length()) {TMP_2splt = ",";}
			String TMP_clk = cht.substring(cht.indexOf("clickable(") + 10, cht.indexOf(")", cht.indexOf("clickable(")));
			
			
			
			if (TMP_clk.contains("(")) {
				TMP_clk = cht.substring(cht.indexOf("clickable(")+10, cht.indexOf(")", cht.indexOf(")")+1));
				String TMP_subcheck = TMP_clk.substring(TMP_clk.indexOf("(")+1,TMP_clk.indexOf(")"));
				String TMP_subcheckReplace = TMP_subcheck.replace(",", "CommaF6cyUQp9Comma");
				TMP_clk = TMP_clk.replace(TMP_subcheck, TMP_subcheckReplace);
			}
			
			String[] TMP_args = TMP_clk.split(",");
			if (TMP_args.length > 0) {
				String TMP_txt = TMP_args[0].replace("CommaF6cyUQp9Comma", ",");
				if (TMP_args.length > 1) {
					String TMP_clkevnt = TMP_args[1].replace("CommaF6cyUQp9Comma", ",");
					if (TMP_args.length > 2) {
						String TMP_clkval = TMP_args[2].replace("CommaF6cyUQp9Comma", ",");
						if (TMP_args.length > 3) {
							String TMP_hovtxt = TMP_args[3].replace("CommaF6cyUQp9Comma", ",");
							cht = cht.replace("clickable(" + TMP_clk.replace("CommaF6cyUQp9Comma", ",") + ")", "\"},{\"text\":\"" + TMP_txt + "\",\"clickEvent\":{\"action\":\"" + TMP_clkevnt + "\",\"value\":\"" + TMP_clkval + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + TMP_hovtxt + "\"}},{\"text\":\"");
						} else {
							cht = cht.replace("clickable(" + TMP_clk.replace("CommaF6cyUQp9Comma", ",") + ")", "\"},{\"text\":\"" + TMP_txt + "\",\"clickEvent\":{\"action\":\"" + TMP_clkevnt + "\",\"value\":\"" + TMP_clkval + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + TMP_clkval + "\"}},{\"text\":\"");
						}
					}
				}
			}
		}
		
		cht = cht.replace("LeftParF6cyUQp9LeftPar", "(");
		cht = cht.replace("RightParF6cyUQp9RightPar", ")");
		cht = cht.replace("commaF6cyUQp9comma", ",");
		
		String TMP_o = "[\"\",";
		TMP_o += "{\"text\":\"" + cht +  "\"}";
		TMP_o += "]";
		ITextComponent TMP_out = ITextComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}
	
	public static void sendJson(List<String> out) {
		String TMP_o = "[\"\",";
		for (int i=0; i<out.size(); i++) {
			TMP_o += "{" + out.get(i) + "}";
			if (i != out.size()-1) {TMP_o += ",";}
		}
		TMP_o += "]";
		ITextComponent TMP_out = ITextComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}
	
	public static void warn(String cht) {
		cht = cht.replace("'('", "LeftParF6cyUQp9LeftPar");
		cht = cht.replace("')'", "RightParF6cyUQp9RightPar");
		cht = cht.replace("'", "\\'");
		cht = cht.replace("\"", "\\\"");
		cht = cht.replace("[", "\u005B");
		cht = cht.replace("]", "\u005D");
		cht = cht.replace("{", "\u007B");
		cht = cht.replace("}", "\u007D");
		
		
		while (cht.contains("clickable(") && cht.contains(")")) {
			String TMP_1splt = "";
			String TMP_2splt = "";
			if (cht.indexOf("clickable(") != 0) {TMP_1splt = ",";}
			if (cht.indexOf(")") != cht.length()) {TMP_2splt = ",";}
			String TMP_clk = cht.substring(cht.indexOf("clickable(") + 10, cht.indexOf(")", cht.indexOf("clickable(")));
			
			
			
			if (TMP_clk.contains("(")) {
				TMP_clk = cht.substring(cht.indexOf("clickable(")+10, cht.indexOf(")", cht.indexOf(")", cht.indexOf("clickable("))+1));
				String TMP_subcheck = TMP_clk.substring(TMP_clk.indexOf("(")+1,TMP_clk.indexOf(")"));
				String TMP_subcheckReplace = TMP_subcheck.replace(",", "CommaF6cyUQp9Comma");
				TMP_clk = TMP_clk.replace(TMP_subcheck, TMP_subcheckReplace);
			}
			
			String[] TMP_args = TMP_clk.split(",");
			if (TMP_args.length > 0) {
				String TMP_txt = TMP_args[0].replace("CommaF6cyUQp9Comma", ",");
				if (TMP_args.length > 1) {
					String TMP_clkevnt = TMP_args[1].replace("CommaF6cyUQp9Comma", ",");
					if (TMP_args.length > 2) {
						String TMP_clkval = TMP_args[2].replace("CommaF6cyUQp9Comma", ",");
						if (TMP_args.length > 3) {
							String TMP_hovtxt = TMP_args[3].replace("CommaF6cyUQp9Comma", ",");
							cht = cht.replace("clickable(" + TMP_clk.replace("CommaF6cyUQp9Comma", ",") + ")", "\"},{\"text\":\"" + TMP_txt + "\",\"clickEvent\":{\"action\":\"" + TMP_clkevnt + "\",\"value\":\"" + TMP_clkval + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + TMP_hovtxt + "\"}},{\"text\":\"");
						} else {
							cht = cht.replace("clickable(" + TMP_clk.replace("CommaF6cyUQp9Comma", ",") + ")", "\"},{\"text\":\"" + TMP_txt + "\",\"clickEvent\":{\"action\":\"" + TMP_clkevnt + "\",\"value\":\"" + TMP_clkval + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + TMP_clkval + "\"}},{\"text\":\"");
						}
					}
				}
			}
		}
		
		while (cht.contains("hover(") && cht.contains(")")) {
			String TMP_1splt = "";
			String TMP_2splt = "";
			if (cht.indexOf("hover(") != 0) {TMP_1splt = ",";}
			if (cht.indexOf(")") != cht.length()) {TMP_2splt = ",";}
			String TMP_clk = cht.substring(cht.indexOf("hover(") + 6, cht.indexOf(")", cht.indexOf("hover(")));
			
			
			
			if (TMP_clk.contains("(")) {
				TMP_clk = cht.substring(cht.indexOf("hover(")+6, cht.indexOf(")", cht.indexOf(")", cht.indexOf("hover("))+1));
				String TMP_subcheck = TMP_clk.substring(TMP_clk.indexOf("(")+1,TMP_clk.indexOf(")"));
				String TMP_subcheckReplace = TMP_subcheck.replace(",", "CommaF6cyUQp9Comma");
				TMP_clk = TMP_clk.replace(TMP_subcheck, TMP_subcheckReplace);
			}
			
			String[] TMP_args = TMP_clk.split(",");
			if (TMP_args.length > 0) {
				String TMP_txt = TMP_args[0].replace("CommaF6cyUQp9Comma", ",");
				if (TMP_args.length > 1) {
					String TMP_val = TMP_args[1].replace("CommaF6cyUQp9Comma", ",");
					cht = cht.replace("hover("+TMP_clk.replace("CommaF6cyUQp9Comma", ",")+")", "\"},{\"text\":\"" + TMP_txt + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + TMP_val + "\"}},{\"text\":\"");
				}
			}
		}
		
		cht = cht.replace("LeftParF6cyUQp9LeftPar", "(");
		cht = cht.replace("RightParF6cyUQp9RightPar", ")");
		
		cht = addFormatting(cht);
		
		
		
		String TMP_o = "[\"\",";
		TMP_o += "{\"text\":\"" + cht +  "\"}";
		TMP_o += "]";
		ITextComponent TMP_out = ITextComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}
	
	public static String color(String clr, String msg) {
		String[] tmp = msg.split(" ");
		String formatted = "";
		String chatColor = TextFormatting.WHITE.toString();
		
		if      (clr.trim().equalsIgnoreCase("&0") || clr.trim().equalsIgnoreCase("BLACK"))       {chatColor = TextFormatting.BLACK.toString();}
		else if (clr.trim().equalsIgnoreCase("&1") || clr.trim().equalsIgnoreCase("DARKBLUE"))    {chatColor = TextFormatting.DARK_BLUE.toString();}
		else if (clr.trim().equalsIgnoreCase("&2") || clr.trim().equalsIgnoreCase("DARKGREEN"))   {chatColor = TextFormatting.DARK_GREEN.toString();}
		else if (clr.trim().equalsIgnoreCase("&3") || clr.trim().equalsIgnoreCase("DARKAQUA"))    {chatColor = TextFormatting.DARK_AQUA.toString();}
		else if (clr.trim().equalsIgnoreCase("&4") || clr.trim().equalsIgnoreCase("DARKRED"))     {chatColor = TextFormatting.DARK_RED.toString();}
		else if (clr.trim().equalsIgnoreCase("&5") || clr.trim().equalsIgnoreCase("DARKPURPLE"))  {chatColor = TextFormatting.DARK_PURPLE.toString();}
		else if (clr.trim().equalsIgnoreCase("&6") || clr.trim().equalsIgnoreCase("GOLD"))        {chatColor = TextFormatting.GOLD.toString();}
		else if (clr.trim().equalsIgnoreCase("&7") || clr.trim().equalsIgnoreCase("GRAY"))        {chatColor = TextFormatting.GRAY.toString();}
		else if (clr.trim().equalsIgnoreCase("&8") || clr.trim().equalsIgnoreCase("DARKGRAY"))    {chatColor = TextFormatting.DARK_GRAY.toString();}
		else if (clr.trim().equalsIgnoreCase("&9") || clr.trim().equalsIgnoreCase("BLUE"))        {chatColor = TextFormatting.BLUE.toString();}
		else if (clr.trim().equalsIgnoreCase("&a") || clr.trim().equalsIgnoreCase("GREEN"))       {chatColor = TextFormatting.GREEN.toString();}
		else if (clr.trim().equalsIgnoreCase("&b") || clr.trim().equalsIgnoreCase("AQUA"))        {chatColor = TextFormatting.AQUA.toString();}
		else if (clr.trim().equalsIgnoreCase("&c") || clr.trim().equalsIgnoreCase("RED"))         {chatColor = TextFormatting.RED.toString();}
		else if (clr.trim().equalsIgnoreCase("&d") || clr.trim().equalsIgnoreCase("LIGHTPURPLE")) {chatColor = TextFormatting.LIGHT_PURPLE.toString();}
		else if (clr.trim().equalsIgnoreCase("&e") || clr.trim().equalsIgnoreCase("YELLOW"))      {chatColor = TextFormatting.YELLOW.toString();}
		else if (clr.trim().equalsIgnoreCase("&f") || clr.trim().equalsIgnoreCase("WHITE"))       {chatColor = TextFormatting.WHITE.toString();}

		for (int i = 0; i < tmp.length; i++) {formatted += chatColor + tmp[i] + " ";}
		
		return formatted.trim();
	}
	
	public static void onClientTick() {
		if (global.chatDelay <= 0) {
			if (global.chatQueue.size() > 0) {
				String cht = global.chatQueue.remove(0);
				
				cht = cht.replace("{me}", Minecraft.getMinecraft().thePlayer.getName());
				
				while (cht.contains("{string[")) {
					String TMP_test = cht.substring(0, cht.indexOf("]"));
					String TMP_s = TMP_test.substring(cht.indexOf("{string[") + 8, cht.indexOf("]"));
					String TMP_sn = "";
					for (int i=0; i<global.USR_string.size(); i++) {
						if (TMP_s.equals(global.USR_string.get(i).get(0))) {
							TMP_sn = global.USR_string.get(i).get(1);
						}
					}
					cht = cht.replace("{string[" + TMP_s + "]}", TMP_sn);
				}
				
				Minecraft.getMinecraft().thePlayer.sendChatMessage(cht);
				global.chatDelay = 20;
			}
		} else {
			global.chatDelay--;
		}
		
		if (global.commandQueue.size() > 0) {
			String cht = global.commandQueue.remove(0);
			if (cht.startsWith("trigger ")) {
				cht = cht.substring(8, cht.length());
				String[] args = cht.split(" ");
				if (global.debug==true) {CommandTrigger.doCommand(args, false);}
				else {CommandTrigger.doCommand(args, true);}
			} else {
				Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + cht);
			}
		}
	}
	
	public static String removeFormatting(String msg) {
		msg = msg.replace(TextFormatting.BLACK.toString(), "&0");
		msg = msg.replace(TextFormatting.DARK_BLUE.toString(), "&1");
		msg = msg.replace(TextFormatting.DARK_GREEN.toString(), "&2");
		msg = msg.replace(TextFormatting.DARK_AQUA.toString(), "&3");
		msg = msg.replace(TextFormatting.DARK_RED.toString(), "&4");
		msg = msg.replace(TextFormatting.DARK_PURPLE.toString(), "&5");
		msg = msg.replace(TextFormatting.GOLD.toString(), "&6");
		msg = msg.replace(TextFormatting.GRAY.toString(), "&7");
		msg = msg.replace(TextFormatting.DARK_GRAY.toString(), "&8");
		msg = msg.replace(TextFormatting.BLUE.toString(), "&9");
		msg = msg.replace(TextFormatting.GREEN.toString(), "&a");
		msg = msg.replace(TextFormatting.AQUA.toString(), "&b");
		msg = msg.replace(TextFormatting.RED.toString(), "&c");
		msg = msg.replace(TextFormatting.LIGHT_PURPLE.toString(), "&d");
		msg = msg.replace(TextFormatting.YELLOW.toString(), "&e");
		msg = msg.replace(TextFormatting.WHITE.toString(), "&f");
		msg = msg.replace(TextFormatting.OBFUSCATED.toString(), "&k");
		msg = msg.replace(TextFormatting.BOLD.toString(), "&l");
		msg = msg.replace(TextFormatting.STRIKETHROUGH.toString(), "&m");
		msg = msg.replace(TextFormatting.UNDERLINE.toString(), "&n");
		msg = msg.replace(TextFormatting.ITALIC.toString(), "&o");
		msg = msg.replace(TextFormatting.RESET.toString(), "&r");
		return msg;
	}
	
	public static String addFormatting(String msg) {
		msg = msg.replace("&0", TextFormatting.BLACK.toString());
		msg = msg.replace("&1", TextFormatting.DARK_BLUE.toString());
		msg = msg.replace("&2", TextFormatting.DARK_GREEN.toString());
		msg = msg.replace("&3", TextFormatting.DARK_AQUA.toString());
		msg = msg.replace("&4", TextFormatting.DARK_RED.toString());
		msg = msg.replace("&5", TextFormatting.DARK_PURPLE.toString());
		msg = msg.replace("&6", TextFormatting.GOLD.toString());
		msg = msg.replace("&7", TextFormatting.GRAY.toString());
		msg = msg.replace("&8", TextFormatting.DARK_GRAY.toString());
		msg = msg.replace("&9", TextFormatting.BLUE.toString());
		msg = msg.replace("&a", TextFormatting.GREEN.toString());
		msg = msg.replace("&b", TextFormatting.AQUA.toString());
		msg = msg.replace("&c", TextFormatting.RED.toString());
		msg = msg.replace("&d", TextFormatting.LIGHT_PURPLE.toString());
		msg = msg.replace("&e", TextFormatting.YELLOW.toString());
		msg = msg.replace("&f", TextFormatting.WHITE.toString());
		msg = msg.replace("&k", TextFormatting.OBFUSCATED.toString());
		msg = msg.replace("&l", TextFormatting.BOLD.toString());
		msg = msg.replace("&m", TextFormatting.STRIKETHROUGH.toString());
		msg = msg.replace("&n", TextFormatting.UNDERLINE.toString());
		msg = msg.replace("&o", TextFormatting.ITALIC.toString());
		msg = msg.replace("&r", TextFormatting.RESET.toString());
		return msg;
	}
}

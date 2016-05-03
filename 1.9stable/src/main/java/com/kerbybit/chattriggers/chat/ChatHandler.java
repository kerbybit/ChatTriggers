package com.kerbybit.chattriggers.chat;

import java.util.List;

import com.kerbybit.chattriggers.commands.CommandTrigger;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ChatHandler {
	
	public static void warnBreak(int type) {
		String dashes = "";
		float chatWidth = Minecraft.getMinecraft().gameSettings.chatWidth;
		float chatScale = Minecraft.getMinecraft().gameSettings.chatScale;
		int numdash = (int) Math.floor(((((280*(chatWidth))+40)/320) * (1/chatScale))*52);
		for (int j=0; j<numdash; j++) {dashes += "-";}
		if (type==0) {
			warn(color(global.settings.get(0), "&m-"+dashes));
		} else if (type==1) {
			warn(color(global.settings.get(0), "&m"+dashes+"&r" + global.settings.get(0) + "^"));
		}
		
	}
	
	public static void warnUnformatted(String cht) {
		cht = cht.replace("'", "\\'").replace("\"","\\\"");
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
		cht = cht.replace("'", "\\'").replace("\"","\\\"");
		cht = cht.replace("[", "\u005B");
		cht = cht.replace("]", "\u005D");
		cht = cht.replace("{", "\u007B");
		cht = cht.replace("}", "\u007D");
		
		
		while (cht.contains("clickable(") && cht.contains(")")) {
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
							cht = cht.replace("clickable(" + TMP_clk.replace("CommaF6cyUQp9Comma", ",") + ")", "\"},{\"text\":\"" + TMP_txt + "\",\"clickEvent\":{\"action\":\"" + TMP_clkevnt + "\",\"value\":\"" + TMP_clkval + "\"}},{\"text\":\"");
						}
					}
				}
			}
		}
		
		while (cht.contains("hover(") && cht.contains(")")) {
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
		TMP_o += "{\"text\":\"" +
				cht.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
					.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
					.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")")
				+  "\"}";
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

		for (String value : tmp) {formatted += chatColor + value + " ";}
		
		return formatted.trim();
	}
	
	public static void onClientTick() {
		if (global.chatDelay <= 0) {
			if (global.chatQueue.size() > 0) {
				String cht = global.chatQueue.remove(0).replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
						.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
						.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				
				Minecraft.getMinecraft().thePlayer.sendChatMessage(cht);
				global.chatDelay = 20;
			}
		} else {
			global.chatDelay--;
		}
		
		if (global.commandQueue.size() > 0) {
			String cht = global.commandQueue.remove(0);
			if (cht.startsWith("trigger ") || (cht.startsWith("t "))) {
				if (cht.startsWith("trigger ")) {cht = cht.substring(8);}
				else {cht = cht.substring(2);}
				String[] args = cht.split(" ");
				if (global.debug) {CommandTrigger.doCommand(args, false);}
				else {CommandTrigger.doCommand(args, true);}
			} else {
				Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + 
						cht.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")"));
			}
		}
	}
	
	public static String removeFormatting(String msg) {
		msg = msg.replace(TextFormatting.BLACK.toString(), "&0")
			.replace(TextFormatting.DARK_BLUE.toString(), "&1")
			.replace(TextFormatting.DARK_GREEN.toString(), "&2")
			.replace(TextFormatting.DARK_AQUA.toString(), "&3")
			.replace(TextFormatting.DARK_RED.toString(), "&4")
			.replace(TextFormatting.DARK_PURPLE.toString(), "&5")
			.replace(TextFormatting.GOLD.toString(), "&6")
			.replace(TextFormatting.GRAY.toString(), "&7")
			.replace(TextFormatting.DARK_GRAY.toString(), "&8")
			.replace(TextFormatting.BLUE.toString(), "&9")
			.replace(TextFormatting.GREEN.toString(), "&a")
			.replace(TextFormatting.AQUA.toString(), "&b")
			.replace(TextFormatting.RED.toString(), "&c")
			.replace(TextFormatting.LIGHT_PURPLE.toString(), "&d")
			.replace(TextFormatting.YELLOW.toString(), "&e")
			.replace(TextFormatting.WHITE.toString(), "&f")
			.replace(TextFormatting.OBFUSCATED.toString(), "&k")
			.replace(TextFormatting.BOLD.toString(), "&l")
			.replace(TextFormatting.STRIKETHROUGH.toString(), "&m")
			.replace(TextFormatting.UNDERLINE.toString(), "&n")
			.replace(TextFormatting.ITALIC.toString(), "&o")
			.replace(TextFormatting.RESET.toString(), "&r");
		return msg;
	}
	
	public static String addFormatting(String msg) {
		msg = msg.replace("&0", TextFormatting.BLACK.toString())
			.replace("&1", TextFormatting.DARK_BLUE.toString())
			.replace("&2", TextFormatting.DARK_GREEN.toString())
			.replace("&3", TextFormatting.DARK_AQUA.toString())
			.replace("&4", TextFormatting.DARK_RED.toString())
			.replace("&5", TextFormatting.DARK_PURPLE.toString())
			.replace("&6", TextFormatting.GOLD.toString())
			.replace("&7", TextFormatting.GRAY.toString())
			.replace("&8", TextFormatting.DARK_GRAY.toString())
			.replace("&9", TextFormatting.BLUE.toString())
			.replace("&a", TextFormatting.GREEN.toString())
			.replace("&b", TextFormatting.AQUA.toString())
			.replace("&c", TextFormatting.RED.toString())
			.replace("&d", TextFormatting.LIGHT_PURPLE.toString())
			.replace("&e", TextFormatting.YELLOW.toString())
			.replace("&f", TextFormatting.WHITE.toString())
			.replace("&k", TextFormatting.OBFUSCATED.toString())
			.replace("&l", TextFormatting.BOLD.toString())
			.replace("&m", TextFormatting.STRIKETHROUGH.toString())
			.replace("&n", TextFormatting.UNDERLINE.toString())
			.replace("&o", TextFormatting.ITALIC.toString())
			.replace("&r", TextFormatting.RESET.toString());
		return msg;
	}
}

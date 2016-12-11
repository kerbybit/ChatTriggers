package com.kerbybit.chattriggers.chat;

import java.util.List;

import com.kerbybit.chattriggers.commands.CommandTrigger;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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
		cht = cht.replace("'", "\\'");
		String TMP_o = "['',";
		TMP_o += "{text:'" + cht +  "'}";
		TMP_o += "]";
		IChatComponent TMP_out = IChatComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}
	
	public static void sendJson(List<String> out) {
		String TMP_o = "['',";
		for (int i=0; i<out.size(); i++) {
			TMP_o += "{" + out.get(i) + "}";
			if (i != out.size()-1) {TMP_o += ",";}
		}
		TMP_o += "]";
		IChatComponent TMP_out = IChatComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}
	
	public static void warn(String cht) {
        //fix link
        cht = removeFormatting(cht);
        while (cht.contains("{link[") && cht.contains("],[") && cht.contains("]}")) {
            String prev_color = "";
            if (cht.indexOf("{link[")!=0) {
                System.out.println(cht);
                String testfor = cht.substring(0, cht.indexOf("{link["));
                while (testfor.contains("&0") || testfor.contains("&1") || testfor.contains("&2")
                        || testfor.contains("&3") || testfor.contains("&4") || testfor.contains("&5")
                        || testfor.contains("&6") || testfor.contains("&7") || testfor.contains("&8")
                        || testfor.contains("&9") || testfor.contains("&a") || testfor.contains("&b")
                        || testfor.contains("&c") || testfor.contains("&d") || testfor.contains("&e")
                        || testfor.contains("&f")) {
                    System.out.println(testfor);
                    testfor = testfor.substring(testfor.indexOf("&"));
                    if (testfor.startsWith("&0")) {prev_color+="&0";}
                    if (testfor.startsWith("&1")) {prev_color+="&1";}
                    if (testfor.startsWith("&2")) {prev_color+="&2";}
                    if (testfor.startsWith("&3")) {prev_color+="&3";}
                    if (testfor.startsWith("&4")) {prev_color+="&4";}
                    if (testfor.startsWith("&5")) {prev_color+="&5";}
                    if (testfor.startsWith("&6")) {prev_color+="&6";}
                    if (testfor.startsWith("&7")) {prev_color+="&7";}
                    if (testfor.startsWith("&8")) {prev_color+="&8";}
                    if (testfor.startsWith("&9")) {prev_color+="&9";}
                    if (testfor.startsWith("&a")) {prev_color+="&a";}
                    if (testfor.startsWith("&b")) {prev_color+="&b";}
                    if (testfor.startsWith("&c")) {prev_color+="&c";}
                    if (testfor.startsWith("&d")) {prev_color+="&d";}
                    if (testfor.startsWith("&e")) {prev_color+="&e";}
                    if (testfor.startsWith("&f")) {prev_color+="&f";}
                    testfor = testfor.substring(2);
                }
            }
            String tmp_string = cht.substring(cht.indexOf("{link[")+6, cht.indexOf("]}", cht.indexOf("{link[")));
            String first = tmp_string.substring(0, tmp_string.indexOf("],["));
            String second = tmp_string.substring(tmp_string.indexOf("],[")+3);
            cht = cht.replace("{link[" + first + "],[" + second + "]}", "clickable("+prev_color+first+",open_url,"+deleteFormatting(second)+",open link)"+prev_color);
        }

		cht = cht.replace("'('", "LeftParF6cyUQp9LeftPar");
		cht = cht.replace("')'", "RightParF6cyUQp9RightPar");
		cht = cht.replace("'", "\\'");
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
							cht = cht.replace("clickable(" + TMP_clk.replace("CommaF6cyUQp9Comma", ",") + ")", "'},{text:'" + TMP_txt + "',clickEvent:{action:'" + TMP_clkevnt + "',value:'" + TMP_clkval + "'},hoverEvent:{action:'show_text',value:'" + TMP_hovtxt + "'}},{text:'");
						} else {
							cht = cht.replace("clickable(" + TMP_clk.replace("CommaF6cyUQp9Comma", ",") + ")", "'},{text:'" + TMP_txt + "',clickEvent:{action:'" + TMP_clkevnt + "',value:'" + TMP_clkval + "'}},{text:'");
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
					cht = cht.replace("hover("+TMP_clk.replace("CommaF6cyUQp9Comma", ",")+")", "'},{text:'" + TMP_txt + "',hoverEvent:{action:'show_text',value:'" + TMP_val + "'}},{text:'");
				}
			}
		}
		
		cht = cht.replace("LeftParF6cyUQp9LeftPar", "(");
		cht = cht.replace("RightParF6cyUQp9RightPar", ")");
		
		cht = addFormatting(cht);
		
		
		
		String TMP_o = "['',";
		TMP_o += "{text:'" + 
				cht.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
					.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
					.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")")
                    .replace("AmpF6cyUQp9Amp","&")
                    .replace("\\n","NewLineF6cyUQp9NewLine")
                    .replace("\\'","SingleQuoteF6cyUQp9SingleQuote")
                    .replace("\\","\\\\")
                    .replace("BackslashF6cyUQp9Backslash","\\\\")
                    .replace("NewLineF6cyUQp9NewLine","\\n")
                    .replace("SingleQuoteF6cyUQp9SingleQuote","\\'")
                    .replace("'", "\'")
				+  "'}";
		TMP_o += "]";
		IChatComponent TMP_out = IChatComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out); 
	}
	
	public static String color(String clr, String msg) {
		String[] tmp = msg.split(" ");
		String formatted = "";
		String chatColor = EnumChatFormatting.WHITE.toString();
		
		if      (clr.trim().equalsIgnoreCase("&0") || clr.trim().equalsIgnoreCase("BLACK"))       {chatColor = EnumChatFormatting.BLACK.toString();}
		else if (clr.trim().equalsIgnoreCase("&1") || clr.trim().equalsIgnoreCase("DARKBLUE"))    {chatColor = EnumChatFormatting.DARK_BLUE.toString();}
		else if (clr.trim().equalsIgnoreCase("&2") || clr.trim().equalsIgnoreCase("DARKGREEN"))   {chatColor = EnumChatFormatting.DARK_GREEN.toString();}
		else if (clr.trim().equalsIgnoreCase("&3") || clr.trim().equalsIgnoreCase("DARKAQUA"))    {chatColor = EnumChatFormatting.DARK_AQUA.toString();}
		else if (clr.trim().equalsIgnoreCase("&4") || clr.trim().equalsIgnoreCase("DARKRED"))     {chatColor = EnumChatFormatting.DARK_RED.toString();}
		else if (clr.trim().equalsIgnoreCase("&5") || clr.trim().equalsIgnoreCase("DARKPURPLE"))  {chatColor = EnumChatFormatting.DARK_PURPLE.toString();}
		else if (clr.trim().equalsIgnoreCase("&6") || clr.trim().equalsIgnoreCase("GOLD"))        {chatColor = EnumChatFormatting.GOLD.toString();}
		else if (clr.trim().equalsIgnoreCase("&7") || clr.trim().equalsIgnoreCase("GRAY"))        {chatColor = EnumChatFormatting.GRAY.toString();}
		else if (clr.trim().equalsIgnoreCase("&8") || clr.trim().equalsIgnoreCase("DARKGRAY"))    {chatColor = EnumChatFormatting.DARK_GRAY.toString();}
		else if (clr.trim().equalsIgnoreCase("&9") || clr.trim().equalsIgnoreCase("BLUE"))        {chatColor = EnumChatFormatting.BLUE.toString();}
		else if (clr.trim().equalsIgnoreCase("&a") || clr.trim().equalsIgnoreCase("GREEN"))       {chatColor = EnumChatFormatting.GREEN.toString();}
		else if (clr.trim().equalsIgnoreCase("&b") || clr.trim().equalsIgnoreCase("AQUA"))        {chatColor = EnumChatFormatting.AQUA.toString();}
		else if (clr.trim().equalsIgnoreCase("&c") || clr.trim().equalsIgnoreCase("RED"))         {chatColor = EnumChatFormatting.RED.toString();}
		else if (clr.trim().equalsIgnoreCase("&d") || clr.trim().equalsIgnoreCase("LIGHTPURPLE")) {chatColor = EnumChatFormatting.LIGHT_PURPLE.toString();}
		else if (clr.trim().equalsIgnoreCase("&e") || clr.trim().equalsIgnoreCase("YELLOW"))      {chatColor = EnumChatFormatting.YELLOW.toString();}
		else if (clr.trim().equalsIgnoreCase("&f") || clr.trim().equalsIgnoreCase("WHITE"))       {chatColor = EnumChatFormatting.WHITE.toString();}

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
		msg = msg.replace(EnumChatFormatting.BLACK.toString(), "&0")
			.replace(EnumChatFormatting.DARK_BLUE.toString(), "&1")
			.replace(EnumChatFormatting.DARK_GREEN.toString(), "&2")
			.replace(EnumChatFormatting.DARK_AQUA.toString(), "&3")
			.replace(EnumChatFormatting.DARK_RED.toString(), "&4")
			.replace(EnumChatFormatting.DARK_PURPLE.toString(), "&5")
			.replace(EnumChatFormatting.GOLD.toString(), "&6")
			.replace(EnumChatFormatting.GRAY.toString(), "&7")
			.replace(EnumChatFormatting.DARK_GRAY.toString(), "&8")
			.replace(EnumChatFormatting.BLUE.toString(), "&9")
			.replace(EnumChatFormatting.GREEN.toString(), "&a")
			.replace(EnumChatFormatting.AQUA.toString(), "&b")
			.replace(EnumChatFormatting.RED.toString(), "&c")
			.replace(EnumChatFormatting.LIGHT_PURPLE.toString(), "&d")
			.replace(EnumChatFormatting.YELLOW.toString(), "&e")
			.replace(EnumChatFormatting.WHITE.toString(), "&f")
			.replace(EnumChatFormatting.OBFUSCATED.toString(), "&k")
			.replace(EnumChatFormatting.BOLD.toString(), "&l")
			.replace(EnumChatFormatting.STRIKETHROUGH.toString(), "&m")
			.replace(EnumChatFormatting.UNDERLINE.toString(), "&n")
			.replace(EnumChatFormatting.ITALIC.toString(), "&o")
			.replace(EnumChatFormatting.RESET.toString(), "&r");
		return msg;
	}
	
	public static String addFormatting(String msg) {
		msg = msg.replace("&0", EnumChatFormatting.BLACK.toString())
			.replace("&1", EnumChatFormatting.DARK_BLUE.toString())
			.replace("&2", EnumChatFormatting.DARK_GREEN.toString())
			.replace("&3", EnumChatFormatting.DARK_AQUA.toString())
			.replace("&4", EnumChatFormatting.DARK_RED.toString())
			.replace("&5", EnumChatFormatting.DARK_PURPLE.toString())
			.replace("&6", EnumChatFormatting.GOLD.toString())
			.replace("&7", EnumChatFormatting.GRAY.toString())
			.replace("&8", EnumChatFormatting.DARK_GRAY.toString())
			.replace("&9", EnumChatFormatting.BLUE.toString())
			.replace("&a", EnumChatFormatting.GREEN.toString())
			.replace("&b", EnumChatFormatting.AQUA.toString())
			.replace("&c", EnumChatFormatting.RED.toString())
			.replace("&d", EnumChatFormatting.LIGHT_PURPLE.toString())
			.replace("&e", EnumChatFormatting.YELLOW.toString())
			.replace("&f", EnumChatFormatting.WHITE.toString())
			.replace("&k", EnumChatFormatting.OBFUSCATED.toString())
			.replace("&l", EnumChatFormatting.BOLD.toString())
			.replace("&m", EnumChatFormatting.STRIKETHROUGH.toString())
			.replace("&n", EnumChatFormatting.UNDERLINE.toString())
			.replace("&o", EnumChatFormatting.ITALIC.toString())
			.replace("&r", EnumChatFormatting.RESET.toString());
		return msg;
	}

    public static String deleteFormatting(String msg) {
        msg = msg.replace(EnumChatFormatting.BLACK.toString(), "")
                .replace(EnumChatFormatting.DARK_BLUE.toString(), "")
                .replace(EnumChatFormatting.DARK_GREEN.toString(), "")
                .replace(EnumChatFormatting.DARK_AQUA.toString(), "")
                .replace(EnumChatFormatting.DARK_RED.toString(), "")
                .replace(EnumChatFormatting.DARK_PURPLE.toString(), "")
                .replace(EnumChatFormatting.GOLD.toString(), "")
                .replace(EnumChatFormatting.GRAY.toString(), "")
                .replace(EnumChatFormatting.DARK_GRAY.toString(), "")
                .replace(EnumChatFormatting.BLUE.toString(), "")
                .replace(EnumChatFormatting.GREEN.toString(), "")
                .replace(EnumChatFormatting.AQUA.toString(), "")
                .replace(EnumChatFormatting.RED.toString(), "")
                .replace(EnumChatFormatting.LIGHT_PURPLE.toString(), "")
                .replace(EnumChatFormatting.YELLOW.toString(), "")
                .replace(EnumChatFormatting.WHITE.toString(), "")
                .replace(EnumChatFormatting.OBFUSCATED.toString(), "")
                .replace(EnumChatFormatting.BOLD.toString(), "")
                .replace(EnumChatFormatting.STRIKETHROUGH.toString(), "")
                .replace(EnumChatFormatting.UNDERLINE.toString(), "")
                .replace(EnumChatFormatting.ITALIC.toString(), "")
                .replace(EnumChatFormatting.RESET.toString(), "")
                .replace("&0", "")
                .replace("&1", "")
                .replace("&2", "")
                .replace("&3", "")
                .replace("&4", "")
                .replace("&5", "")
                .replace("&6", "")
                .replace("&7", "")
                .replace("&8", "")
                .replace("&9", "")
                .replace("&a", "")
                .replace("&b", "")
                .replace("&c", "")
                .replace("&d", "")
                .replace("&e", "")
                .replace("&f", "")
                .replace("&k", "")
                .replace("&l", "")
                .replace("&m", "")
                .replace("&n", "")
                .replace("&o", "")
                .replace("&r", "");
        return msg;
    }
}

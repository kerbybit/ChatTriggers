package com.kerbybit.chattriggers.chat;

import java.util.List;

import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import static java.lang.StrictMath.round;

public class ChatHandler {
	
	public static void warnBreak(int type) {
		StringBuilder dashes = new StringBuilder();
		float chatWidth = Minecraft.getMinecraft().gameSettings.chatWidth;
		float chatScale = Minecraft.getMinecraft().gameSettings.chatScale;
		int numdash = (int) Math.floor(((((280*(chatWidth))+40)/320) * (1/chatScale))*52);
		for (int j=0; j<numdash; j++) {dashes.append("-");}
		if (type==0) {
			warn(color(Settings.col[0], "&m-"+dashes));
		} else if (type==1) {
			warn(color(Settings.col[0], "&m"+dashes+"&r" + Settings.col[0] + "^"));
		}
	}
	
	public static void warnUnformatted(String cht) {
		cht = cht.replace("'", "\\'")
                .replace("\\", "\\\\");
		String TMP_o = "['',";
		TMP_o += "{text:'" + cht +  "'}";
		TMP_o += "]";
		IChatComponent TMP_out = IChatComponent.Serializer.jsonToComponent(TMP_o);
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}
	
	public static void sendJson(List<String> out) {
		StringBuilder TMP_o = new StringBuilder("['',");
		for (int i=0; i<out.size(); i++) {
			TMP_o.append("{").append(out.get(i)).append("}");
			if (i != out.size()-1) {TMP_o.append(",");}
		}
		TMP_o.append("]");
		IChatComponent TMP_out = IChatComponent.Serializer.jsonToComponent(TMP_o.toString());
		Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
	}

	private static String removeExtras(String cht) {
	    return cht.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")")
                .replace("stringOpenBracketReplacementF6cyUQp9stringOpenBracketReplacement", "(")
                .replace("stringCloseBracketReplacementF6cyUQp9stringCloseBracketReplacement", ")")
                .replace("AmpF6cyUQp9Amp","&")
                .replace("TripleDotF6cyUQp9TripleDot","...")
                .replace("\\n","NewLineF6cyUQp9NewLine")
                .replace("\\'","SingleQuoteF6cyUQp9SingleQuote")
                .replace("\\","\\\\")
                .replace("BackslashF6cyUQp9Backslash","\\\\")
                .replace("NewLineF6cyUQp9NewLine","\\n")
                .replace("SingleQuoteF6cyUQp9SingleQuote","\\'")
                .replace("'", "\'")
                .replace("\0","");
    }

    private static String removeClickableExtras(String cht) {
	    while (cht.contains("clickable(") && cht.contains(",") && cht.contains(")")) {
	        String first = cht.substring(0, cht.indexOf("clickable("));

            String TMP_clk = cht.substring(cht.indexOf("clickable(") + 10, cht.indexOf(")", cht.indexOf("clickable(")));

            if (TMP_clk.contains("(")) {
                TMP_clk = cht.substring(cht.indexOf("clickable(")+10, cht.indexOf(")", cht.indexOf(")", cht.indexOf("clickable("))+1));
                String TMP_subcheck = TMP_clk.substring(TMP_clk.indexOf("(")+1,TMP_clk.indexOf(")"));
                String TMP_subcheckReplace = TMP_subcheck.replace(",", "CommaF6cyUQp9Comma");
                TMP_clk = TMP_clk.replace(TMP_subcheck, TMP_subcheckReplace);
            }
            String text = TMP_clk.substring(0, TMP_clk.indexOf(","));
            text = text.replace("CommaF6cyUQp9Comma", ",");

	        String last = cht.substring(cht.indexOf(")", cht.indexOf(TMP_clk)+TMP_clk.length())+1);
	        cht = first + EnumChatFormatting.RESET + text + last;
        }
        return cht;
    }

    private static String removeHoverExtras(String cht) {
        while (cht.contains("hover(") && cht.contains(",") && cht.contains(")")) {
            String first = cht.substring(0, cht.indexOf("hover("));
            String text = EnumChatFormatting.RESET + cht.substring(cht.indexOf("hover(")+6, cht.indexOf(",", cht.indexOf("hover(")));
            String last = cht.substring(cht.indexOf(")", cht.indexOf("hover("))+1);
            cht = first + text + last;
        }
        return cht;
    }

    private static String removeLinkExtras(String cht) {
        while (cht.contains("{link[") && cht.contains("]stringCommaReplacementF6cyUQp9stringCommaReplacement[") && cht.contains("]}")) {
            String first = cht.substring(0, cht.indexOf("{link["));
            String text = cht.substring(cht.indexOf("{link[")+6, cht.indexOf("]stringCommaReplacementF6cyUQp9stringCommaReplacement[", cht.indexOf("{link[")));
            String last = cht.substring(cht.indexOf("]}", cht.indexOf("{link["))+2);
            cht = first + text + last;
        }
        return cht;
    }

    private static int getChatWidth(String cht) {
        cht = removeClickableExtras(cht);
        cht = removeHoverExtras(cht);
        cht = removeLinkExtras(cht);
        cht = removeExtras(cht);

        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(addFormatting(cht));
    }

	private static String center(String cht) {
	    cht = cht.replaceAll("(?i)<center>", "");

	    float chatWidth = getChatWidth(cht);
        float fullWidth = Minecraft.getMinecraft().gameSettings.chatWidth * 320;

        if (chatWidth < fullWidth) {
            StringBuilder spaces = new StringBuilder();
            float spaceWidth = Minecraft.getMinecraft().fontRendererObj.getCharWidth(' ');
            float centerWidth = (fullWidth - chatWidth) / 2;

            int numberSpaces = round(centerWidth / spaceWidth);
            for (int i = 0; i < numberSpaces; i++) {
                spaces.append(" ");
            }
            return spaces + cht;
        }
        return cht;


    }

    private static String fixLinks(String cht) {
        while (cht.contains("{link[") && cht.contains("]stringCommaReplacementF6cyUQp9stringCommaReplacement[") && cht.contains("]}")) {
            StringBuilder prev_color = new StringBuilder();
            if (cht.indexOf("{link[")!=0) {
                String testfor = cht.substring(0, cht.indexOf("{link["));
                while (testfor.contains("&0") || testfor.contains("&1") || testfor.contains("&2")
                        || testfor.contains("&3") || testfor.contains("&4") || testfor.contains("&5")
                        || testfor.contains("&6") || testfor.contains("&7") || testfor.contains("&8")
                        || testfor.contains("&9") || testfor.contains("&a") || testfor.contains("&b")
                        || testfor.contains("&c") || testfor.contains("&d") || testfor.contains("&e")
                        || testfor.contains("&f")) {
                    testfor = testfor.substring(testfor.indexOf("&"));
                    if (testfor.startsWith("&0")) {prev_color.append("&0");}
                    if (testfor.startsWith("&1")) {prev_color.append("&1");}
                    if (testfor.startsWith("&2")) {prev_color.append("&2");}
                    if (testfor.startsWith("&3")) {prev_color.append("&3");}
                    if (testfor.startsWith("&4")) {prev_color.append("&4");}
                    if (testfor.startsWith("&5")) {prev_color.append("&5");}
                    if (testfor.startsWith("&6")) {prev_color.append("&6");}
                    if (testfor.startsWith("&7")) {prev_color.append("&7");}
                    if (testfor.startsWith("&8")) {prev_color.append("&8");}
                    if (testfor.startsWith("&9")) {prev_color.append("&9");}
                    if (testfor.startsWith("&a")) {prev_color.append("&a");}
                    if (testfor.startsWith("&b")) {prev_color.append("&b");}
                    if (testfor.startsWith("&c")) {prev_color.append("&c");}
                    if (testfor.startsWith("&d")) {prev_color.append("&d");}
                    if (testfor.startsWith("&e")) {prev_color.append("&e");}
                    if (testfor.startsWith("&f")) {prev_color.append("&f");}
                    testfor = testfor.substring(2);
                }
            }
            String tmp_string = cht.substring(cht.indexOf("{link[")+6, cht.indexOf("]}", cht.indexOf("{link[")));
            String first = tmp_string.substring(0, tmp_string.indexOf("]stringCommaReplacementF6cyUQp9stringCommaReplacement["));
            String second = tmp_string.substring(tmp_string.indexOf("]stringCommaReplacementF6cyUQp9stringCommaReplacement[")+54);
            cht = cht.replace("{link[" + first + "]stringCommaReplacementF6cyUQp9stringCommaReplacement[" + second + "]}", "clickable("+prev_color+first+",open_url,"+deleteFormatting(second)+",open link)"+prev_color);
        }
	    return cht;
    }

	public static void warn(String color, String chat) {
	    warn(color(color, chat));
    }
	public static void warn(String cht) {
	    cht = removeFormatting(cht);

	    if (cht.toUpperCase().contains("<CENTER>"))
	        cht = center(cht);

	    //fix links
	    cht = fixLinks(cht);

		cht = cht.replace("'('", "LeftParF6cyUQp9LeftPar")
                .replace("')'", "RightParF6cyUQp9RightPar")
                .replace("','", "CommaReplacementF6cyUQp9CommaReplacement")
                .replace("'", "\\'")
                .replace("[", "\u005B")
                .replace("]", "\u005D")
                .replace("{", "\u007B")
                .replace("}", "\u007D");

		
		
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
		
		cht = cht.replace("LeftParF6cyUQp9LeftPar", "(")
                .replace("RightParF6cyUQp9RightPar", ")")
                .replace("CommaReplacementF6cyUQp9CommaReplacement", ",");
		
		cht = addFormatting(cht);
		
		
		
		String TMP_o = "['',";
		TMP_o += "{text:'" +
				cht.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
					.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
					.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")")
                    .replace("stringOpenBracketReplacementF6cyUQp9stringOpenBracketReplacement", "(")
                    .replace("stringCloseBracketReplacementF6cyUQp9stringCloseBracketReplacement", ")")
                    .replace("AmpF6cyUQp9Amp","&")
                    .replace("TripleDotF6cyUQp9TripleDot","...")
                    .replace("\\n","NewLineF6cyUQp9NewLine")
                    .replace("\\'","SingleQuoteF6cyUQp9SingleQuote")
                    .replace("\\","\\\\")
                    .replace("BackslashF6cyUQp9Backslash","\\\\")
                    .replace("NewLineF6cyUQp9NewLine","\\n")
                    .replace("SingleQuoteF6cyUQp9SingleQuote","\\'")
                    .replace("'", "\'")
                    .replace("\0","")
				+  "'}";
		TMP_o += "]";
		IChatComponent TMP_out = IChatComponent.Serializer.jsonToComponent(TMP_o);
        try {
            Minecraft.getMinecraft().thePlayer.addChatMessage(TMP_out);
        } catch (NullPointerException e) {
            //world isn't loaded
            System.out.println(TMP_out);
        }
	}
	
	public static String color(String clr, String msg) {
		String[] tmp = msg.split(" ");
		StringBuilder formatted = new StringBuilder();
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

		for (String value : tmp) {formatted.append(chatColor).append(value).append(" ");}
		
		return formatted.toString().trim();
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
            if (!global.hasWatermark) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/" +
                        cht.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                                .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                                .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")"));
            }
		}
	}
	
	public static String removeFormatting(String msg) {
		return msg.replace(EnumChatFormatting.BLACK.toString(), "&0")
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
	}

    public static String ignoreFormatting(String msg) {
        return msg.replace(EnumChatFormatting.BLACK.toString(), "AmpF6cyUQp9Amp0")
                .replace(EnumChatFormatting.DARK_BLUE.toString(), "AmpF6cyUQp9Amp1")
                .replace(EnumChatFormatting.DARK_GREEN.toString(), "AmpF6cyUQp9Amp2")
                .replace(EnumChatFormatting.DARK_AQUA.toString(), "AmpF6cyUQp9Amp3")
                .replace(EnumChatFormatting.DARK_RED.toString(), "AmpF6cyUQp9Amp4")
                .replace(EnumChatFormatting.DARK_PURPLE.toString(), "AmpF6cyUQp9Amp5")
                .replace(EnumChatFormatting.GOLD.toString(), "AmpF6cyUQp9Amp6")
                .replace(EnumChatFormatting.GRAY.toString(), "AmpF6cyUQp9Amp7")
                .replace(EnumChatFormatting.DARK_GRAY.toString(), "AmpF6cyUQp9Amp8")
                .replace(EnumChatFormatting.BLUE.toString(), "AmpF6cyUQp9Amp9")
                .replace(EnumChatFormatting.GREEN.toString(), "AmpF6cyUQp9Ampa")
                .replace(EnumChatFormatting.AQUA.toString(), "AmpF6cyUQp9Ampb")
                .replace(EnumChatFormatting.RED.toString(), "AmpF6cyUQp9Ampc")
                .replace(EnumChatFormatting.LIGHT_PURPLE.toString(), "AmpF6cyUQp9Ampd")
                .replace(EnumChatFormatting.YELLOW.toString(), "AmpF6cyUQp9Ampe")
                .replace(EnumChatFormatting.WHITE.toString(), "AmpF6cyUQp9Ampf")
                .replace(EnumChatFormatting.OBFUSCATED.toString(), "AmpF6cyUQp9Ampk")
                .replace(EnumChatFormatting.BOLD.toString(), "AmpF6cyUQp9Ampl")
                .replace(EnumChatFormatting.STRIKETHROUGH.toString(), "AmpF6cyUQp9Ampm")
                .replace(EnumChatFormatting.UNDERLINE.toString(), "AmpF6cyUQp9Ampn")
                .replace(EnumChatFormatting.ITALIC.toString(), "AmpF6cyUQp9Ampo")
                .replace(EnumChatFormatting.RESET.toString(), "AmpF6cyUQp9Ampr")
                .replace("&0", "AmpF6cyUQp9Amp0")
                .replace("&1", "AmpF6cyUQp9Amp1")
                .replace("&2", "AmpF6cyUQp9Amp2")
                .replace("&3", "AmpF6cyUQp9Amp3")
                .replace("&4", "AmpF6cyUQp9Amp4")
                .replace("&5", "AmpF6cyUQp9Amp5")
                .replace("&6", "AmpF6cyUQp9Amp6")
                .replace("&7", "AmpF6cyUQp9Amp7")
                .replace("&8", "AmpF6cyUQp9Amp8")
                .replace("&9", "AmpF6cyUQp9Amp9")
                .replace("&a", "AmpF6cyUQp9Ampa")
                .replace("&b", "AmpF6cyUQp9Ampb")
                .replace("&c", "AmpF6cyUQp9Ampc")
                .replace("&d", "AmpF6cyUQp9Ampd")
                .replace("&e", "AmpF6cyUQp9Ampe")
                .replace("&f", "AmpF6cyUQp9Ampf")
                .replace("&k", "AmpF6cyUQp9Ampk")
                .replace("&l", "AmpF6cyUQp9Ampl")
                .replace("&m", "AmpF6cyUQp9Ampm")
                .replace("&n", "AmpF6cyUQp9Ampn")
                .replace("&o", "AmpF6cyUQp9Ampo")
                .replace("&r", "AmpF6cyUQp9Ampr");
    }
	
	public static String addFormatting(String msg) {
		return msg.replace("&0", EnumChatFormatting.BLACK.toString())
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
	}

    public static String deleteFormatting(String msg) {
        return msg.replace(EnumChatFormatting.BLACK.toString(), "")
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
    }
}

package com.kerbybit.chattriggers.objects;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.triggers.StringFunctions;
import com.kerbybit.chattriggers.triggers.StringHandler;
import com.kerbybit.chattriggers.util.BookData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.HashMap;

public class BookHandler {
	private static HashMap<String, BookData> books = new HashMap<>();

	public static NBTTagList createNBTPage(String text, boolean format) {
		NBTTagList textList = new NBTTagList();

		if (format) {
			text = ChatHandler.addFormatting(text);
		}

		while (text.contains("spacer[") && text.contains("]")) {
			int spaces = Integer.parseInt(text.substring(text.indexOf("spacer[") + 7, text.indexOf("]", text.indexOf("spacer["))));
			StringBuilder replacer = new StringBuilder();

			for (int i = 0; i < spaces; i++) {
				replacer.append(" ");
			}

			text = text.replace("spacer[" + spaces + "]", replacer.toString());
		}

		while (text.contains("clickable[") && text.contains("]")) {
			if (text.indexOf("clickable[") > 0) {
				NBTTagCompound textCompound = new NBTTagCompound();
				String toAdd = text.substring(0, text.indexOf("clickable["));
				textCompound.setTag("text", new NBTTagString(toAdd));
				textList.appendTag(textCompound);
				text = text.substring(text.indexOf("clickable["));
			}

			String param = text.substring(text.indexOf("clickable[") + 10, text.indexOf("]", text.indexOf("clickable[")));
			String[] params = param.split(",");
			params = addEscaped(params);

			NBTTagCompound toAppend = new NBTTagCompound();
			toAppend.setTag("text", new NBTTagString(params[0]));

			NBTTagCompound clickEvent = new NBTTagCompound();
			clickEvent.setTag("action", new NBTTagString(params[1]));
			clickEvent.setTag("value", new NBTTagString(params[2]));
			toAppend.setTag("clickEvent", clickEvent);

			if (params.length == 4) {
				NBTTagCompound hoverEvent = new NBTTagCompound();
				hoverEvent.setTag("action", new NBTTagString("show_text"));
				hoverEvent.setTag("value", new NBTTagString(params[3]));
				toAppend.setTag("hoverEvent", hoverEvent);
			}

			textList.appendTag(toAppend);
			text = text.replace("clickable[" + param + "]", "");
		}

		while (text.contains("hover[") && text.contains("]")) {
			if (text.indexOf("hover[") > 0) {
				NBTTagCompound textCompound = new NBTTagCompound();
				String toAdd = text.substring(0, text.indexOf("hover["));
				textCompound.setTag("text", new NBTTagString(toAdd));
				textList.appendTag(textCompound);
				text = text.substring(text.indexOf("hover["));
			}

			String param = text.substring(text.indexOf("hover[") + 6, text.indexOf("]", text.indexOf("hover[")));
			String[] params = param.split(",");
			params = addEscaped(params);

			NBTTagCompound toAppend = new NBTTagCompound();
			toAppend.setTag("text", new NBTTagString(params[0]));

			NBTTagCompound clickEvent = new NBTTagCompound();
			clickEvent.setTag("action", new NBTTagString("show_text"));
			clickEvent.setTag("value", new NBTTagString(params[1]));
			toAppend.setTag("hoverEvent", clickEvent);

			textList.appendTag(toAppend);
			text = text.replace("hover[" + param + "]", "");
		}

		if (text.length() > 0) {
			NBTTagCompound toAppend = new NBTTagCompound();
			toAppend.setTag("text", new NBTTagString(text));
			textList.appendTag(toAppend);
		}

		System.out.println(textList.toString());
		return textList;
	}

	private static String[] addEscaped(String[] args) {
		String[] toReturn = new String[args.length];

		for (int i = 0; i < args.length; i++) {
			toReturn[i] = args[i].replace("&com", ",");
		}

		return toReturn;
	}

	public static String bookFunctions(String args, boolean isAsync) {
		while (args.contains("{book[") && args.contains("]}.add(") && args.contains(")")) {
			int bookIndex = args.indexOf("{book[");
			int addIndex = args.indexOf("]}.add", bookIndex);
			int endIndex = args.indexOf(")", addIndex);

			String bookName = args.substring(bookIndex + 6, addIndex);
			String textToAdd = args.substring(addIndex + 7, endIndex);

			books.putIfAbsent(bookName, new BookData(bookName));
			BookData bookData = books.get(bookName);

			StringFunctions.nestedArgs(textToAdd, null, isAsync);

			bookData.writePage(textToAdd);
			args = args.replace("{book[" + bookName + "]}.add(" + textToAdd + ")", "");
		}

		while (args.contains("{book[") && args.contains("]}.clear()")) {
			int bookIndex = args.indexOf("{book[");
			int addIndex = args.indexOf("]}.clear()", bookIndex);

			String bookName = args.substring(bookIndex + 6, addIndex);

			books.putIfAbsent(bookName, new BookData(bookName));
			BookData bookData = books.get(bookName);

			bookData.clearPages();
			args = args.replace("{book[" + bookName + "]}.clear()", "");
		}

		while (args.contains("{book[") && args.contains("]}.display(") && args.contains(")")) {
			int bookIndex = args.indexOf("{book[");
			int addIndex = args.indexOf("]}.display(", bookIndex);
			int endIndex = args.indexOf(")", addIndex);
			String ender = ")";

			Integer pageToOpen = 0;
			if (args.charAt(endIndex - 1) != '(') {
				pageToOpen = Integer.parseInt(args.substring(addIndex + 11, endIndex));
				ender = args.substring(addIndex + 11, endIndex) + ")";
			}

			String bookName = args.substring(bookIndex + 6, addIndex);

			books.putIfAbsent(bookName, new BookData(bookName));
			BookData bookData = books.get(bookName);

			bookData.display(pageToOpen);
			args = args.replace("{book[" + bookName + "]}.display(" + ender, "");
		}

		while (args.contains("{book[") && args.contains("]}.close()")) {
			int bookIndex = args.indexOf("{book[");
			int addIndex = args.indexOf("]}.close()", bookIndex);
			String bookName = args.substring(bookIndex + 6, addIndex);

			if (books.containsKey(bookName)) {
				GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;

				if (currentScreen instanceof GuiScreenBook) {
					GuiScreenBook currentBook = ((GuiScreenBook) currentScreen);

					String bookTitle = ReflectionHelper.getPrivateValue(GuiScreenBook.class, currentBook,
							"bookTitle", "field_146482_z");

					if (bookTitle.equals("CT" + bookName)) {
						Minecraft.getMinecraft().displayGuiScreen(null);
					}
				}
			}

			args = args.replace("{book[" + bookName + "]}.close()", "");
		}

		return args;
	}
}

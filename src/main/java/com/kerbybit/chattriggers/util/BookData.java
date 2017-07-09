package com.kerbybit.chattriggers.util;

import com.kerbybit.chattriggers.objects.BookHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class BookData {
	private ItemStack backingBook;
	private NBTTagCompound bookPageData;
	private String name;

	public BookData(NBTTagCompound bookPageData, String name) {
		this.name = name;

		if (!bookPageData.hasKey("author")) {
			bookPageData.setTag("author", new NBTTagString(Minecraft.getMinecraft().thePlayer.getName()));
		}

		if (!bookPageData.hasKey("title")) {
			bookPageData.setTag("title", new NBTTagString("CT" + this.name));
		}

		if (!bookPageData.hasKey("pages")) {
			bookPageData.setTag("pages", new NBTTagList());
		}

		this.bookPageData = bookPageData;
		this.backingBook = new ItemStack(Items.written_book);
		this.backingBook.setTagCompound(bookPageData);
	}


	public BookData(String name) {
		this(new NBTTagCompound(), name);
	}

	public void display(Integer pageToOpen) {
		GuiScreenBook guiScreenBook = new GuiScreenBook(Minecraft.getMinecraft().thePlayer,
				this.backingBook, false);

		if (pageToOpen < 1) pageToOpen = 1;
		int tagCount = ((NBTTagList) bookPageData.getTag("pages")).tagCount();
		if (pageToOpen > tagCount) pageToOpen = tagCount;

		ReflectionHelper.setPrivateValue(GuiScreenBook.class, guiScreenBook, pageToOpen - 1, "field_146484_x");
		Minecraft.getMinecraft().displayGuiScreen(guiScreenBook);
	}

	public void writePage(String text) {
		this.writePage(BookHandler.createNBTPage(text, true));
	}

	public void writePage(NBTTagList nbt) {
		NBTBase base = this.bookPageData.getTag("pages");

		if (base.getId() == (byte)9) {
			NBTTagList tagList = ((NBTTagList) base);

			StringBuilder tagListSB = new StringBuilder("[\"\",");
			String tagListToString;

			for (int i = 0; i < nbt.tagCount(); i++) {
				tagListSB.append(nbt.get(i)).append(",");
			}
			tagListToString = tagListSB.toString();

			if (tagListToString.endsWith(",")) {
				tagListToString = tagListToString.substring(0, tagListToString.length() - 1);
			}
			tagListToString += "]";

			tagList.appendTag(new NBTTagString(tagListToString));
			this.bookPageData.setTag("pages", tagList);
		}

		this.backingBook.setTagCompound(this.bookPageData);
		System.out.println(this.bookPageData);
	}

	public void clearPages() {
		this.bookPageData.removeTag("pages");
		this.bookPageData.setTag("pages", new NBTTagList());
	}
}

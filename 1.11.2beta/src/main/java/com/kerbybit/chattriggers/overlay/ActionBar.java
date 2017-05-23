package com.kerbybit.chattriggers.overlay;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class ActionBar {
    public static void showActionBar(String text) {
        GuiIngame gui = new GuiIngame(Minecraft.getMinecraft());
        gui.setRecordPlayingMessage(text
                .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")"));
    }
}

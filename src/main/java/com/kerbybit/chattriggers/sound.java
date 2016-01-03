package com.kerbybit.chattriggers;

import net.minecraft.client.Minecraft;

public class sound {
	public static void play(String snd, int vol, int pitch) {
		Minecraft.getMinecraft().thePlayer.playSound(snd, vol, pitch);
	}
}

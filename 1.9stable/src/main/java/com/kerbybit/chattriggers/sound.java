package com.kerbybit.chattriggers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvent;

public class sound {
	public static void play(SoundEvent snd, int vol, int pitch) {
		Minecraft.getMinecraft().thePlayer.playSound(snd, vol, pitch);
	}
}

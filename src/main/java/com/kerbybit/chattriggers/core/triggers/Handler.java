package com.kerbybit.chattriggers.core.triggers;

import com.kerbybit.chattriggers.core.events.CTEvent;

import java.util.ArrayList;

public class Handler {
	ArrayList<CTEvent> events;

	public void handleEvents(){
		for (CTEvent event : events) {
			event.call();
		}
	}
}

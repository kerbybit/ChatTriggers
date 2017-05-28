package com.kerbybit.chattriggers.core.triggers;

import java.util.ArrayList;

public abstract class Trigger {
	ArrayList<Handler> handlers;

	public Trigger() {
		handlers = new ArrayList<>();
	}

	public void addHandler(Handler handler) {
		handlers.add(handler);
	}

	public void triggered() {
		for (Handler handler : handlers) {
			handler.handleEvents();
		}
	}
}

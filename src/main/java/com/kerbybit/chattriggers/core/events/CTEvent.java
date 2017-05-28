package com.kerbybit.chattriggers.core.events;

public abstract class CTEvent {
	String name;
	String[] args;

	public abstract void call();
}

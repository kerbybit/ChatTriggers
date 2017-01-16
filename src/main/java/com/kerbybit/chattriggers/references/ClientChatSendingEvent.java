package com.kerbybit.chattriggers.references;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ClientChatSendingEvent extends Event {
    private IChatComponent message;
    public ClientChatSendingEvent(IChatComponent message) {
        this.setMessage(message);
    }
    public IChatComponent getMessage() {
        return message;
    }
    public void setMessage(IChatComponent message) {
        this.message = message;
    }
}

package io.github.nevalackin;

import best.azura.eventbus.core.EventBus;
import io.github.nevalackin.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public enum Supernova {

	INSTANCE;

	public static final String CLIENT_NAME = "Supernova";
	private final Minecraft mc = Minecraft.getMinecraft();
	private EventBus eventBus;

	public void startup() {
		eventBus = new EventBus();
		ModuleManager.INSTANCE.init();
		eventBus.register(this);
	}

	public void chat(String message) {
		if (mc.thePlayer == null) return;
		mc.thePlayer.addChatMessage(new ChatComponentText(message));
	}

	public void chat(String prefix, String message) {
		if (mc.thePlayer == null) return;
		if (prefix.equals("")) prefix = "§dSupernova§f | §7";
		mc.thePlayer.addChatMessage(new ChatComponentText(prefix + " " + message));
	}

	public EventBus getEventBus() {
		return eventBus;
	}
}

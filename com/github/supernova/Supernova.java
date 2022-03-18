package com.github.supernova;

import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.network.EventSendPacket;
import com.github.supernova.gui.guiscreen.click.GuiClickGui;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.oauth.AuthService;
import com.github.supernova.util.client.Alt;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;

public enum Supernova {

	INSTANCE;

	public static final String CLIENT_NAME = "Supernova";
	public final Minecraft mc = Minecraft.getMinecraft();
	private EventBus eventBus;
	private GuiClickGui clickGui;
	private AuthService authService;
	private ArrayList<Alt> altArrayList = new ArrayList<>();

	public GuiClickGui getClickGui() {
		return clickGui;
	}
	public ArrayList<Alt> getAltList() {
		return  altArrayList;
	}
	public void setAltList(ArrayList<Alt> altList) {
		this.altArrayList = altList;
	}

	public void startup() {
		authService = new AuthService();
		eventBus = new EventBus();
		ModuleManager.INSTANCE.init();
		clickGui = new GuiClickGui();
		eventBus.register(this);
		new Alt("olen.omenaxdxd@gmail.com","volvov700").login();
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

	@EventHandler
	public Listener<EventSendPacket> eventSendPacketListener = event -> {
		if (event.getPacket() instanceof C01PacketChatMessage) {
			C01PacketChatMessage c01PacketChatMessage = (C01PacketChatMessage) event.getPacket();
			event.setCancelled(c01PacketChatMessage.getMessage().startsWith("."));
		}
	};

	public EventBus getEventBus() {
		return eventBus;
	}
}

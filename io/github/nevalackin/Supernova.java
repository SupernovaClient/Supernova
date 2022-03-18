package io.github.nevalackin;

import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import io.github.nevalackin.events.network.EventSendPacket;
import io.github.nevalackin.gui.guiscreen.click.GuiClickGui;
import io.github.nevalackin.modules.ModuleManager;
import io.github.nevalackin.oauth.AuthService;
import io.github.nevalackin.util.client.Alt;
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
		new Alt("david.de.qwertz@web.de","David1david!").login();
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

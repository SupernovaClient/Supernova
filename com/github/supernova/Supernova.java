package com.github.supernova;

import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.network.EventSendPacket;
import com.github.supernova.gui.guiscreen.click.GuiClickGui;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.util.client.AccountType;
import com.github.supernova.util.client.Alt;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public enum Supernova {

	INSTANCE;

	public static final String CLIENT_NAME = "Supernova";
	public final Minecraft mc = Minecraft.getMinecraft();
	private EventBus eventBus;
	private GuiClickGui clickGui;
	private MicrosoftAuthenticator authService;
	private ArrayList<Alt> altArrayList = new ArrayList<>();
	private JsonParser parser = new JsonParser();

	public GuiClickGui getClickGui() {
		return clickGui;
	}

	public ArrayList<Alt> getAltList() {
		return altArrayList;
	}

	public void setAltList(ArrayList<Alt> altList) {
		this.altArrayList = altList;
	}

	public MicrosoftAuthenticator getAuthenticator() {
		return this.authService;
	}

	public void saveAltList(File file) {

	}

	public void updateAltList(File file) {
		if (file.exists()) {
			try {
				List<String> lines = Files.readAllLines(file.toPath());
				StringBuilder builder = new StringBuilder();
				lines.forEach(builder::append);
				JsonObject object = (JsonObject) parser.parse(builder.toString());
				JsonArray altArray = object.getAsJsonArray("alts");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startup() {
		authService = new MicrosoftAuthenticator();
		eventBus = new EventBus();
		ModuleManager.INSTANCE.init();
		clickGui = new GuiClickGui();
		eventBus.register(this);
		//new Alt("olen.omenaxdxd@gmail.com","volvov700").login();
		new Alt("joeybouwmans@gmail.com","Joejoe20081610pepper").login();
		//microsoftLogin();
		//new Alt("mataishangthorn@gmail.com", "Drakengard2").login();
	}

	private void microsoftLogin() {
		authService.loginWithAsyncWebview().thenAccept((auth) -> {
			new Alt(AccountType.MICROSOFT, auth.getProfile().getName(), auth.getProfile().getId(), auth.getAccessToken()).login();
		});
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

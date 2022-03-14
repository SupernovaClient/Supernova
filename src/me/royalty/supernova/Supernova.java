package me.royalty.supernova;

import lombok.Getter;
import me.royalty.supernova.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Supernova {

    private final Minecraft mc = Minecraft.getMinecraft();
    public static final Supernova INSTANCE = new Supernova();

    public void startup() {
        ModuleManager.INSTANCE.init();
    }

    public void chat(String message) {
        if(mc.thePlayer == null) return;
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }
    public void chat(String prefix, String message) {
        if(mc.thePlayer == null) return;
        if(prefix.equals("")) prefix = "§dSupernova§f | §7";
        mc.thePlayer.addChatMessage(new ChatComponentText(prefix +" "+ message));
    }

}

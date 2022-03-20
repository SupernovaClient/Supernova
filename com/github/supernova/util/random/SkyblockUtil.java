package com.github.supernova.util.random;

import com.github.supernova.Supernova;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import org.lwjgl.Sys;

import java.util.List;

public class SkyblockUtil {
    public static String getSkyblockID(ItemStack item) {
        String id = item.getTagCompound().getCompoundTag("ExtraAttributes").getString("id");
        return id.equals("") ? null : id;
    }
    public static int getSpeedPercentage() {
        NetHandlerPlayClient play = Supernova.INSTANCE.mc.getNetHandler();
        for(NetworkPlayerInfo info : play.getPlayerInfoMap()) {
            if(info == null) continue;
            if(info.getDisplayName() == null) continue;
            IChatComponent displayName = info.getDisplayName();
            if(displayName.getUnformattedText() == null) continue;
            if(displayName.getUnformattedText().contains(" Speed: ✦")) {
                if(displayName.getSiblings() != null) {
                    List<IChatComponent> siblings = displayName.getSiblings();
                    if(siblings.get(1) != null) {
                        return Integer.parseInt(siblings.get(1).getUnformattedText().substring(1));
                    }
                }
            }
        }

        return 0;
    }
}

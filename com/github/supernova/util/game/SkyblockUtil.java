package com.github.supernova.util.game;

import com.github.supernova.Supernova;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        return 100;
    }

    public static boolean isOnIsland() {
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if(objective != null) {
            Scoreboard sb = objective.getScoreboard();
            for(Score score : sb.getSortedScores(objective)) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                if(scoreplayerteam == null) continue;
                ChatComponentText text = new ChatComponentText(ScorePlayerTeam
                        .formatPlayerName(scoreplayerteam, score.getPlayerName())
                        .replaceAll(score.getPlayerName(),""));
                System.out.println(text.getFormattedText());
            }
        }
        return true;
    }
}

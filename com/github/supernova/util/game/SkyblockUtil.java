package com.github.supernova.util.game;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.Supernova;
import com.github.supernova.events.network.EventReceivePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class SkyblockUtil {



    public static String getSkyblockID(ItemStack item) {
        String id = item.getTagCompound().getCompoundTag("ExtraAttributes").getString("id");
        return id.equals("") ? null : id;
    }
    public static double getSpeedPercentage() {
        return Minecraft.getMinecraft().thePlayer.getAttributeMap().getAttributeInstanceByName("generic.movementSpeed").getBaseValue()*1000;
    }

    public static boolean isOnIsland() {
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if(objective != null) {
            Scoreboard sb = objective.getScoreboard();
            for(Score score : sb.getSortedScores(objective)) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                if(scoreplayerteam == null) continue;
                if(ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName())
                        .replaceAll(score.getPlayerName(),"").contains("Your Isla")) {
                    return true;
                }
            }
        }
        return false;
    }
}

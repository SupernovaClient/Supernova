package com.github.supernova.gui.notifications;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.Supernova;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {

    static Minecraft mc = Minecraft.getMinecraft();
    private final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    static final float NOTIF_WIDTH = 80;
    static final float NOTIF_HEIGHT = 20;

    public void push(String title, String description, long length) {
        notifications.add(new Notification(title,description,length));
        notifications.sort(Comparator.comparingLong(Notification::timeLeft));
    }

    public void drawNotifications() {
        int index = 1;
        for(Notification notif : notifications) {
            if(notif.timeLeft() <= -500) {
                notifications.remove(notif);
                continue;
            }
            drawNotif(notif, index);
            index++;
        }
    }
    private void drawNotif(Notification notification, int index) {
        ScaledResolution sr = new ScaledResolution(mc);
        int sf = sr.getScaleFactor();
        float basePosX = sr.getScaledWidth() - getWidth();
        float posX;
        if(notification.timeElapsed() >= notification.length) {
            long uhm = Math.min(notification.timeElapsed() - notification.length,200);
            float percentage = uhm / 200f;
            posX = basePosX + getWidth() * percentage;
        } else {
            long uhm = Math.min(notification.timeElapsed(),200);
            float percentage = uhm / 200f;
            posX = basePosX + getWidth() * (1 - percentage);
        }
        float posY = sr.getScaledHeight() - 20 - ((getHeight()) * index) - (5*index);
        float percentElapsed =  Math.min(1,(float) notification.timeElapsed() / notification.length);
        RenderUtil.drawRectWidth(posX,posY,getWidth(),getHeight(),0xDD1a1a1a);
        RenderUtil.drawRectOutlineWidth(posX,posY,getWidth(),getHeight(), 0xFF101010, 1.75f);
        RenderUtil.drawRectWidth(posX,posY+getHeight()-2,getWidth() - getWidth()*percentElapsed,2,notification.type.colour);
        mc.blockyFontObj.drawStringWithShadow(notification.title,posX+5,posY+5,0xFFDADADA);
        mc.blockyFontObj.drawStringWithShadow(notification.description,posX+5,posY+18,0xFFDADADA);
    }

    private float getWidth() {
        return NOTIF_WIDTH * new ScaledResolution(mc).getScaleFactor();
    }
    private float getHeight() {
        return NOTIF_HEIGHT * new ScaledResolution(mc).getScaleFactor();
    }

    public void push(String title, String description, long length, NotificationType type) {
        notifications.add(new Notification(title,description,length, type));
        notifications.sort(Comparator.comparingLong(Notification::timeLeft));
    }

    public enum NotificationType {
        SUCCESS(0xFF30FF30),
        WARNING(0xFFFFDD30),
        ERROR(0xFFFF3030),
        NONE(0xFFDDDDDD);
        int colour;
        NotificationType(int colour) {
            this.colour = colour;
        }
    }
}

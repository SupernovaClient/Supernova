package com.github.supernova.gui.notifications;

public class Notification {

    long initTime;
    long length;
    String title;
    String description;
    NotificationManager.NotificationType type;

    public Notification(String title, String description, long length, NotificationManager.NotificationType type) {
        this.length = length;
        this.title = title;
        this.description = description;
        this.type = type;
        this.initTime = System.currentTimeMillis();
    }
    public Notification(String title, String description, long length) {
        this.length = length;
        this.title = title;
        this.description = description;
        this.type = NotificationManager.NotificationType.NONE;
        this.initTime = System.currentTimeMillis();
    }
    public Notification(String title, String description) {
        this.length = 5000;
        this.title = title;
        this.description = description;
        this.type = NotificationManager.NotificationType.NONE;
        this.initTime = System.currentTimeMillis();
    }
    public long timeLeft() {
        return length - (System.currentTimeMillis() - initTime);
    }
    public long timeElapsed() {
        return System.currentTimeMillis() - initTime;
    }
    public boolean isExpired() {
        return timeLeft() <= 0;
    }
}

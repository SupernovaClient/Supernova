package com.github.supernova.util.client;

import com.github.supernova.Supernova;
import com.github.supernova.gui.notifications.NotificationManager;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.lwjgl.Sys;

import java.net.Proxy;

public class Alt {

    private boolean invalid, cracked;
    private String username, password, displayName;
    private AccountType type;

    private String microsoftID;
    private String microsoftAccessToken;

    public Alt(String username, String password) {
        this.username = username;
        this.password = password;
        displayName = username;
        cracked = password.equals("");
        invalid = false;
        type = AccountType.MOJANG;
    }

    public Alt(String username) {
        this.username = username;
        this.password = "";
        displayName = username;
        cracked = true;
        invalid = false;
        type = AccountType.MOJANG;
    }

    public Alt(AccountType type, String name, String id, String accessToken) {
        this.type = type;
        this.username = name;
        displayName = username;
        microsoftID = id;
        microsoftAccessToken = accessToken;
    }

    private void savePassword(String password) {
        //TODO: Encrypting password when saving to file
        this.password = password;
    }
    private String getPassword() {
        return password;
    }
    public boolean isInvalid() {
        return invalid;
    }
    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
    public boolean isCracked() {
        return cracked;
    }
    public void setCracked(boolean cracked) {
        this.cracked = cracked;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void login() {
        if(type == AccountType.MICROSOFT) {
            Minecraft.getMinecraft().session = new Session(username, microsoftID, microsoftAccessToken, "mojang");
            return;
        }
        if(cracked || getPassword().equals("")) {
            Supernova.INSTANCE.mc.session = new Session(username, "", "", "mojang");
            invalid = false;
            displayName = username;
            cracked = true;
            Supernova.INSTANCE.getNotifManager().push("Cracked Login", "Logged into "+displayName, 5000, NotificationManager.NotificationType.NONE);
        } else {
            new Thread("Alt Login"){
                public void run() {
                    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
                    auth.setUsername(username);
                    auth.setPassword(getPassword());
                    try {
                        auth.logIn();
                        Supernova.INSTANCE.mc.session = new Session(auth.getSelectedProfile().getName(),auth.getSelectedProfile().getId().toString(),auth.getAuthenticatedToken(),"mojang");
                        displayName = Supernova.INSTANCE.mc.session.getUsername();
                        invalid = false;
                        Supernova.INSTANCE.getNotifManager().push("Login Success", "Logged into "+displayName, 5000, NotificationManager.NotificationType.SUCCESS);
                    } catch (AuthenticationException e) {
                        invalid = true;
                        Supernova.INSTANCE.getNotifManager().push("Login Failed", "Invalid Details", 5000, NotificationManager.NotificationType.ERROR);
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}

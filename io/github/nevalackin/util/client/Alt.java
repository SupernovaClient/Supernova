package io.github.nevalackin.util.client;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import io.github.nevalackin.Supernova;
import io.github.nevalackin.gui.notifications.NotificationManager;
import net.minecraft.util.Session;

import java.net.Proxy;

public class Alt {

    private boolean invalid, cracked;
    private String username, password, displayName;

    public Alt(String username, String password) {
        this.username = username;
        this.password = password;
        displayName = username;
        cracked = password.equals("");
        invalid = false;
    }

    public Alt(String username) {
        this.username = username;
        this.password = "";
        displayName = username;
        cracked = true;
        invalid = false;
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
        if(cracked || getPassword().equals("")) {
            Supernova.INSTANCE.mc.session = new Session(username, "", "", "mojang");
            invalid = false;
            displayName = username;
            cracked = true;
            NotificationManager.push("Cracked Login", "Logged into "+displayName, 5000, NotificationManager.NotificationType.NONE);
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
                        NotificationManager.push("Login Success", "Logged into "+displayName, 5000, NotificationManager.NotificationType.SUCCESS);
                    } catch (AuthenticationException e) {
                        invalid = true;
                        NotificationManager.push("Login Failed", "Invalid Details", 5000, NotificationManager.NotificationType.ERROR);
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}

package me.royalty.supernova.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import me.royalty.supernova.events.render.EventRender2D;
import me.royalty.supernova.gui.GuiHUD;
import me.royalty.supernova.modules.Category;
import me.royalty.supernova.modules.Module;

public class HUD extends Module {

    private final GuiHUD hud;

    public HUD() {
        super("HUD", "Show stuffs", Category.RENDER, true);
        hud = new GuiHUD(this);
        setEnabled(true);
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        hud.render();
    }
}

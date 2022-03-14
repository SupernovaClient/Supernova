package io.github.nevalackin.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import io.github.nevalackin.events.render.EventRender2D;
import io.github.nevalackin.gui.GuiHUD;
import io.github.nevalackin.modules.Category;
import io.github.nevalackin.modules.Module;

public class HUD extends Module {

	private final GuiHUD hud = new GuiHUD(this);

	public HUD() {
		super("HUD", "Show stuffs", Category.RENDER, true);
		setEnabled(true);
	}

	@EventHandler
	public final Listener<EventRender2D> eventRender2DListener = eventRender2D -> {
		hud.render();
	};

}

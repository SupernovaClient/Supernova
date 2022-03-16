package io.github.nevalackin.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import io.github.nevalackin.events.render.EventRender2D;
import io.github.nevalackin.events.render.EventRender3D;
import io.github.nevalackin.gui.GuiHUD;
import io.github.nevalackin.modules.Category;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.value.impl.ColourValue;

import java.util.List;

public class HUD extends Module {

	private final GuiHUD hud = new GuiHUD(this);

	public ColourValue hudColourValue = new ColourValue("HUD Colour", 0xFFAA66BB);

	public HUD() {
		super("HUD", "Show stuffs", Category.RENDER, true);
		setValues(hudColourValue);
		setEnabled(true);
	}

	@EventHandler
	public final Listener<EventRender2D> eventRender2DListener = eventRender2D -> {
		hud.render(eventRender2D.getPartialTicks());
	};

	@EventHandler
	public final Listener<EventRender3D> eventRender3DListener = eventRender3D -> {
		hud.renderWorld(eventRender3D.getPartialTicks());
	};

}

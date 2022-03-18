package com.github.supernova.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.events.render.EventRender3D;
import com.github.supernova.gui.GuiHUD;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.value.impl.ColourValue;

@ModuleAnnotation(name = "HUD", displayName = "HUD", description = "Show this", category = Category.RENDER)
public class HUD extends Module {

	private final GuiHUD hud = new GuiHUD(this);

	public ColourValue hudColourValue = new ColourValue("HUD Colour", 0xFFAA66BB);

	public HUD() {
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

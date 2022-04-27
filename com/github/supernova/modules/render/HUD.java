package com.github.supernova.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.misc.MouseClickedEvent;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.events.render.EventRender3D;
import com.github.supernova.gui.GuiHUD;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.value.impl.ColourValue;
import com.github.supernova.value.impl.EnumValue;

@SuppressWarnings("unused")
@ModuleAnnotation(name = "HUD", displayName = "HUD", description = "Show this", category = Category.RENDER)
public class HUD extends Module {

	private final GuiHUD hud = new GuiHUD(this);
	private String watermark = "Supernova";

	public String getWatermark() {
		return watermark;
	}
	public void setWatermark(String watermark) {
		if(watermark.equals("")) {
			this.watermark = "Supernova";
		} else {
			this.watermark = watermark;
		}
	}

	public ColourValue hudColourValue = new ColourValue("HUD Colour", 0xffffc4fc);
	public EnumValue<WatermarkMode> watermarkModeValue = new EnumValue<>("Watermark", WatermarkMode.CSGO);

	public HUD() {
		setValues(hudColourValue, watermarkModeValue);
		setEnabled(true);
	}

	@EventHandler
	public final Listener<EventRender2D> eventRender2DListener = eventRender2D -> hud.render();

	public enum WatermarkMode implements ModeEnum {
		SUPERNOVA("Supernova"),
		CSGO("CSGO");

		@Override
		public String getName() {
			return name;
		}
		private final String name;
		WatermarkMode(String name) {
			this.name = name;
		}
	}
}

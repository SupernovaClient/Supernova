package com.github.supernova.gui;

import com.github.supernova.Supernova;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.modules.render.HUD;
import com.github.supernova.util.render.ColourUtil;
import com.github.supernova.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GuiHUD {

	private final HUD HUDModule;
	private final Minecraft mc = Minecraft.getMinecraft();

	public GuiHUD(HUD hud) {
		this.HUDModule = hud;
	}

	public void render() {
		ScaledResolution sr = new ScaledResolution(mc);
		renderWatermark();
		renderArrayList(sr.getScaledWidth() - 1);
	}

	public void renderWorld() {
	}

	private void renderWatermark() {
		String watermarkString = Supernova.CLIENT_NAME + " | UID 000 | "+System.getProperty("user.name");
		int width = mc.blockyFontObj.getStringWidth(watermarkString);
		RenderUtil.drawRectWidth((float) 4, (float) 4, width + 4, mc.blockyFontObj.FONT_HEIGHT + 3, 0x802D2D2D);
		RenderUtil.drawRectOutlineWidth((float) 4, (float) 4, width + 4, mc.blockyFontObj.FONT_HEIGHT + 3,
				HUDModule.hudColourValue.getInt(), 1.5f);
		mc.blockyFontObj.drawStringWithShadow(watermarkString, (float) 4 + 2, (float) 4 + 2, 0xFFFFFFFF);
	}

	private void renderArrayList(float baseX) {
		ArrayList<Module> enabledModules = ModuleManager.INSTANCE.getEnabledModules();
		enabledModules = enabledModules.stream().filter(Module::isVisible).collect(Collectors.toCollection(ArrayList::new));
		enabledModules = enabledModules.stream().sorted(Comparator.comparingInt((module) -> -mc.blockyFontObj.getStringWidth(module.getModuleDisplayName())))
				.collect(Collectors.toCollection(ArrayList::new));

		int spacing = mc.blockyFontObj.FONT_HEIGHT + 1;
		float y = (float) 4;
		int count = 1;

		for (Module module : enabledModules) {

			int currentColour = HUDModule.hudColourValue.getCurrentValue(count*40).getRGB();
			int width = mc.blockyFontObj.getStringWidth(module.getModuleDisplayName());

			RenderUtil.drawRectWidth(baseX - width - 8, y - 1.3f, width + 5, mc.blockyFontObj.FONT_HEIGHT + 0.8f, 0x60444444);
			RenderUtil.drawRectWidth(baseX - 4, y - 1.3f, 2, mc.blockyFontObj.FONT_HEIGHT + 0.8f, currentColour);
			mc.blockyFontObj.drawStringWithShadow(module.getModuleDisplayName(), baseX - width - 6, y - 1, currentColour);

			y += spacing;
			count++;
		}
	}


}

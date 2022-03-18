package io.github.nevalackin.gui;

import io.github.nevalackin.Supernova;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.modules.ModuleManager;
import io.github.nevalackin.modules.render.HUD;
import io.github.nevalackin.util.render.ColourUtil;
import io.github.nevalackin.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class GuiHUD {

	private HUD HUDModule;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiHUD(HUD hud) {
		this.HUDModule = hud;
	}

	public void render(float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		renderWatermark(4, 4, sr);
		renderArrayList(sr.getScaledWidth() - 1, 4, sr);
	}

	public void renderWorld(float partialTicks) {
	}

	private void renderWatermark(float x, float y, ScaledResolution sr) {
		String watermarkString = Supernova.CLIENT_NAME + " | UID 000 | Royalty";
		int width = mc.blockyFontObj.getStringWidth(watermarkString);
		RenderUtil.drawRectWidth(x, y, width + 4, mc.blockyFontObj.FONT_HEIGHT + 3, 0x802D2D2D);
		RenderUtil.drawRectOutlineWidth(x, y, width + 4, mc.blockyFontObj.FONT_HEIGHT + 3,
				HUDModule.hudColourValue.getInt(), 1.5f);
		mc.blockyFontObj.drawStringWithShadow(watermarkString, x + 2, y + 2, 0xFFFFFFFF);
	}

	private void renderArrayList(float baseX, float baseY, ScaledResolution sr) {
		ArrayList<Module> enabledModules = ModuleManager.INSTANCE.getEnabledModules();
		enabledModules = enabledModules.stream().filter(Module::isVisible).collect(Collectors.toCollection(ArrayList::new));
		enabledModules = enabledModules.stream().sorted(Comparator.comparingInt((module) -> -mc.blockyFontObj.getStringWidth(module.getModuleDisplayName())))
				.collect(Collectors.toCollection(ArrayList::new));

		int spacing = mc.blockyFontObj.FONT_HEIGHT + 1;
		float y = baseY;
		int count = 1;

		for (Module module : enabledModules) {
			int currentColour = ColourUtil.interpolateColorsDynamic(10, count * 20, new Color(0xFFAA77FF), new Color(0xFFAA77FF).darker().darker()).getRGB();
			int width = mc.blockyFontObj.getStringWidth(module.getModuleDisplayName());

			RenderUtil.drawRectWidth(baseX - width - 8, y - 1.3f, width + 5, mc.blockyFontObj.FONT_HEIGHT + 0.8f, 0x60444444);
			RenderUtil.drawRectWidth(baseX - 4, y - 1.3f, 2, mc.blockyFontObj.FONT_HEIGHT + 0.8f, currentColour);
			mc.blockyFontObj.drawStringWithShadow(module.getModuleDisplayName(), baseX - width - 6, y - 1, currentColour);

			y += spacing;
			count++;
		}
	}


}

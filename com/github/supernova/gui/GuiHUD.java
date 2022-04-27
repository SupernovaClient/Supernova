package com.github.supernova.gui;

import com.github.supernova.gui.impl.DraggableElement;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.modules.render.HUD;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.util.font.FontManager;
import com.github.supernova.util.input.MouseUtil;
import com.github.supernova.util.render.RenderUtil;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiHUD {

	private final HUD HUDModule;
	private final Minecraft mc = Minecraft.getMinecraft();
	public static final List<DraggableElement> draggableElementList = new ArrayList<>();

	public GuiHUD(HUD hud) {
		this.HUDModule = hud;
	}

	public void render() {
		renderWatermark();
		renderArrayList();
		for(DraggableElement element : Lists.reverse(draggableElementList)) {
			element.drawElement();
		}
	}

	private void renderWatermark() {
		switch (HUDModule.watermarkModeValue.getCurrentValue()) {
			case SUPERNOVA:
				String supernovaWatermarkString = HUDModule.getWatermark() + " | UID 000 | "+System.getProperty("user.name");
				int supernovaWidth = mc.blockyFontObj.getStringWidth(supernovaWatermarkString);
				RenderUtil.drawRectWidth((float) 4, (float) 4, supernovaWidth + 4, mc.blockyFontObj.FONT_HEIGHT + 3, 0x802D2D2D);
				RenderUtil.drawRectOutlineWidth((float) 4, (float) 4, supernovaWidth + 4, mc.blockyFontObj.FONT_HEIGHT + 3,
						HUDModule.hudColourValue.getInt(), 1.5f);
				mc.blockyFontObj.drawStringWithShadow(supernovaWatermarkString, (float) 4 + 2, (float) 4 + 2, 0xFFFFFFFF);
				break;
			case CSGO:
				String csgoWatermarkString = HUDModule.getWatermark() + " | UID 000 | "+System.getProperty("user.name");
				int csgoWidth = FontManager.consolas15.getStringWidth(csgoWatermarkString);
				RenderUtil.drawCustomBox(4,4, csgoWidth+6, FontManager.consolas15.getFontHeight()+5,HUDModule.hudColourValue.getInt(),2.5f);
				FontManager.consolas15.drawString(csgoWatermarkString, 6,8.5f,0xFFDADADA,true);
				break;
		}
	}

	private void renderArrayList() {

		ScaledResolution sr = new ScaledResolution(mc);
		float posX = sr.getScaledWidth() - 4 , posY = 5;

		ArrayList<Module> enabledModules = ModuleManager.INSTANCE.getEnabledModules();
		enabledModules = enabledModules.stream().filter(Module::isVisible).collect(Collectors.toCollection(ArrayList::new));
		enabledModules = enabledModules.stream().sorted(Comparator.comparingInt((module) -> -mc.curvedFontObj.getStringWidth(module.getModuleDisplayName())))
				.collect(Collectors.toCollection(ArrayList::new));

		int spacing = mc.curvedFontObj.FONT_HEIGHT + 2;
		int count = 1;
		for (Module module : enabledModules) {

			int currentColour = HUDModule.hudColourValue.getCurrentValue(count*40).getRGB();
			int stringWidth = mc.curvedFontObj.getStringWidth(module.getModuleDisplayName());

			RenderUtil.drawRectWidth(posX - stringWidth-10, posY-1, stringWidth+8, mc.curvedFontObj.FONT_HEIGHT+2, 0x70707070);
			RenderUtil.drawRectWidth(posX - 5, posY-1, 3, mc.curvedFontObj.FONT_HEIGHT+2, currentColour);
			mc.curvedFontObj.drawStringWithShadow(module.getModuleDisplayName(), posX - stringWidth - 8, posY, currentColour);

			posY += spacing;
			count++;
		}
	}


}

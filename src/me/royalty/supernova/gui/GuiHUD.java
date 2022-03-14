package me.royalty.supernova.gui;

import lombok.Getter;
import lombok.Setter;
import me.royalty.supernova.Supernova;
import me.royalty.supernova.modules.Module;
import me.royalty.supernova.modules.ModuleManager;
import me.royalty.supernova.modules.render.HUD;
import me.royalty.supernova.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GuiHUD {

    private HUD HUDModule;
    private Minecraft mc = Minecraft.getMinecraft();
    @Getter @Setter
    private String clientName = "Supernova";
    public GuiHUD(HUD hud) {
        this.HUDModule = hud;
    }

    public void render() {
        renderWatermark(4,4);
        renderArrayList(8,8);
    }

    private void renderWatermark(float x, float y) {
        String watermarkString = clientName + " | UID 000 | Royalty";
        int width = mc.blockyFontObj.getStringWidth(watermarkString);
        RenderUtil.drawRectWidth(x,y,width+4,mc.blockyFontObj.FONT_HEIGHT+3,0x802D2D2D);
        RenderUtil.drawRectOutlineWidth(x,y,width+4, mc.blockyFontObj.FONT_HEIGHT+3,
                RenderUtil.astolfoColour(0,10000).getRGB(), 0.8f);
        mc.blockyFontObj.drawStringWithShadow(watermarkString, x+2, y+2, 0xFFFFFFFF);
    }
    private void renderArrayList(float baseX, float baseY) {
        boolean right = baseX <= mc.displayWidth /2f;
        boolean bottom = baseY <= mc.displayHeight /2f;
        ArrayList<Module> enabledModules = ModuleManager.INSTANCE.getEnabledModules();
        enabledModules.forEach((module -> Supernova.INSTANCE.chat(module.getModuleDisplayName())));
        enabledModules = enabledModules.stream().filter(Module::isVisible).collect(Collectors.toCollection(ArrayList::new));
        enabledModules = enabledModules.stream().sorted(Comparator.comparingInt( (module) -> mc.blockyFontObj.getStringWidth(module.getModuleDisplayName())))
                .collect(Collectors.toCollection(ArrayList::new));

        int spacing = mc.blockyFontObj.FONT_HEIGHT+1;
        float y = baseY;
        float x;
        int count = 1;
        for(Module module : enabledModules) {
            int currentColour = 0xFFFFFFFF;
            x = right ? baseX - mc.blockyFontObj.getStringWidth(module.getModuleDisplayName()) : baseX;
            mc.blockyFontObj.drawString(module.getModuleDisplayName(),x,y,currentColour);
            Supernova.INSTANCE.chat("", module.getModuleDisplayName());


            y += spacing;
            count++;
        }
    }
}

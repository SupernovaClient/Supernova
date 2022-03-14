package io.github.nevalackin.gui.guiscreen.click.impl;

import io.github.nevalackin.gui.guiscreen.click.GuiClickGui;
import io.github.nevalackin.gui.guiscreen.click.impl.module.ModuleDropdown;
import io.github.nevalackin.modules.Category;
import io.github.nevalackin.modules.ModuleManager;
import io.github.nevalackin.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.io.IOException;
import java.util.ArrayList;

public class CategoryDropdown extends Component{

    GuiClickGui parent;
    ArrayList<ModuleDropdown> moduleDropdowns = new ArrayList<>();
    Category category;

    float posX, posY;

    public CategoryDropdown(GuiClickGui parent, Category category, float baseX, float baseY) {
        this.parent = parent;
        this.category = category;
        ModuleManager.INSTANCE.getModulesByCategory(category).forEach( (module) -> moduleDropdowns.add(new ModuleDropdown(this, module)));
        this.posX = baseX;
        this.posY = baseY;
    }

    public Category getCategory() {
        return this.category;
    }

    @Override
    public float getComponentHeight() {
        float height = COMPONENT_HEIGHT;
        if(expanded) {
            for (ModuleDropdown moduleDropdown : moduleDropdowns) {
                height += moduleDropdown.getComponentHeight();
            }
        }
        return height;
    }
    boolean expanded = true;
    @Override
    public void render(float posX, float posY) {
        float startX = posX;
        float startY = posY;
        RenderUtil.drawRectOutlineWidth(posX,posY,getComponentWidth(),getComponentHeight(),RenderUtil.astolfoColour(0, 10000).getRGB(), 0.8f);
        RenderUtil.drawRectWidth(posX, posY, COMPONENT_WIDTH, COMPONENT_HEIGHT, 0xFF303030);
        mc.blockyFontObj.drawStringWithShadow(category.name,posX+4,posY + 1 + (COMPONENT_HEIGHT /2f - mc.blockyFontObj.FONT_HEIGHT /2f) ,0xFFDADADA);
        if(expanded && moduleDropdowns.size() > 0) {
            for (ModuleDropdown moduleDropdown : moduleDropdowns) {
                posY += moduleDropdown.getComponentHeight();
                moduleDropdown.render(posX, posY);
            }
            parent.drawGradientRect(startX,startY+COMPONENT_HEIGHT, startX+COMPONENT_WIDTH, startY+COMPONENT_HEIGHT+5, 0xFF303030, 0x00404040);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(expanded) {
            for(ModuleDropdown moduleDropdown : moduleDropdowns) {
                moduleDropdown.mouseClicked(mouseX,mouseY,mouseButton);
            }
        }
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(expanded) {
            for(ModuleDropdown moduleDropdown : moduleDropdowns) {
                moduleDropdown.mouseReleased(mouseX,mouseY,state);
            }
        }
    }
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(expanded) {
            for(ModuleDropdown moduleDropdown : moduleDropdowns) {
                moduleDropdown.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
            }
        }
    }

    public float getX() {
        return posX;
    }

    public void setX(float posX) {
        this.posX = posX;
    }

    public float getY() {
        return posY;
    }

    public void setY(float posY) {
        this.posY = posY;
    }
}

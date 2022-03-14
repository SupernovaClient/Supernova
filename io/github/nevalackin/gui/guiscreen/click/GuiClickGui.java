package io.github.nevalackin.gui.guiscreen.click;

import io.github.nevalackin.gui.guiscreen.click.impl.CategoryDropdown;
import io.github.nevalackin.gui.guiscreen.click.impl.Component;
import io.github.nevalackin.modules.Category;
import io.github.nevalackin.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class GuiClickGui extends GuiScreen {

    ArrayList<CategoryDropdown> categoryDropdowns = new ArrayList<>();

    public GuiClickGui() {
        initDropdowns();
    }

    private void initDropdowns() {
        float baseX = 20;
        float baseY = 50;
        for(Category category : Category.values()) {
            categoryDropdowns.add(new CategoryDropdown(this, category, baseX, baseY));
            baseX += Component.COMPONENT_WIDTH + 10;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0,0,mc.displayWidth,mc.displayHeight,0x44222222);
        for(CategoryDropdown categoryDropdown : categoryDropdowns) {
            categoryDropdown.render(categoryDropdown.getX(), categoryDropdown.getY());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(CategoryDropdown categoryDropdown : categoryDropdowns) {
            categoryDropdown.mouseClicked(mouseX,mouseY,mouseButton);
        }
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(CategoryDropdown categoryDropdown : categoryDropdowns) {
            categoryDropdown.mouseReleased(mouseX,mouseY,state);
        }
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for(CategoryDropdown categoryDropdown : categoryDropdowns) {
            categoryDropdown.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

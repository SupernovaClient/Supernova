package com.github.supernova.gui.guiscreen.click;

import com.github.supernova.gui.guiscreen.click.impl.CategoryDropdown;
import com.github.supernova.gui.guiscreen.click.impl.Component;
import com.github.supernova.modules.Category;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
        Collections.reverse(categoryDropdowns);
        for(CategoryDropdown categoryDropdown : categoryDropdowns)
            categoryDropdown.render(categoryDropdown.getX(), categoryDropdown.getY());

        Collections.reverse(categoryDropdowns);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(CategoryDropdown categoryDropdown :  categoryDropdowns) {
            if(categoryDropdown.hovered(mouseX,mouseY)) {
                categoryDropdown.mouseClicked(mouseX, mouseY, mouseButton);
                return;
            }
        }
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(CategoryDropdown categoryDropdown : categoryDropdowns)
            categoryDropdown.mouseReleased(mouseX, mouseY, state);
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for(CategoryDropdown categoryDropdown : categoryDropdowns)
            categoryDropdown.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

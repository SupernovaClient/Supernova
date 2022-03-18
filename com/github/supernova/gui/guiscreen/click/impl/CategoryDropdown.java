package com.github.supernova.gui.guiscreen.click.impl;

import com.github.supernova.gui.guiscreen.click.GuiClickGui;
import com.github.supernova.gui.guiscreen.click.impl.module.ModuleDropdown;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.util.input.MouseUtil;
import com.github.supernova.util.render.RenderUtil;

import javax.vecmath.Vector2f;
import java.io.IOException;
import java.util.ArrayList;

public class CategoryDropdown extends Component {

	GuiClickGui parent;
	ArrayList<ModuleDropdown> moduleDropdowns = new ArrayList<>();
	Category category;

	float posX, posY;

	public CategoryDropdown(GuiClickGui parent, Category category, float baseX, float baseY) {
		this.parent = parent;
		this.category = category;
		ModuleManager.INSTANCE.getModulesByCategory(category).forEach((module) -> moduleDropdowns.add(new ModuleDropdown(this, module)));
		this.posX = baseX;
		this.posY = baseY;
	}

	public Category getCategory() {
		return this.category;
	}

	@Override
	public float getComponentHeight() {
		float height = COMPONENT_HEIGHT;
		if (expanded) {
			for (ModuleDropdown moduleDropdown : moduleDropdowns) {
				height += moduleDropdown.getComponentHeight();
			}
		}
		return height;
	}

	Vector2f startMousePos = null;
	boolean dragging = false;
	boolean expanded = true;

	@Override
	public void render(float posX, float posY) {
		if (dragging) {
			Vector2f mouse = MouseUtil.getMousePos();
			float newPosX = mouse.x - startMousePos.x;
			float newPosY = mouse.y - startMousePos.y;
			float diffX = newPosX - this.posX;
			float diffY = newPosY - this.posY;
			this.posX = this.posX + (diffX / 1.5f);
			this.posY = this.posY + (diffY / 1.5f);
		}

		float startX = posX, startY = posY;
		RenderUtil.drawRectOutlineWidth(posX, posY, getComponentWidth(), getComponentHeight(), RenderUtil.astolfoColour(0, 10000).getRGB(), 0.8f);
		RenderUtil.drawRectWidth(posX, posY, COMPONENT_WIDTH, COMPONENT_HEIGHT, 0xFF303030);
		mc.blockyFontObj.drawStringWithShadow(category.name, posX + 4, posY + 1 + (COMPONENT_HEIGHT / 2f - mc.blockyFontObj.FONT_HEIGHT / 2f), 0xFFDADADA);
		posY += COMPONENT_HEIGHT;
		if (expanded && moduleDropdowns.size() > 0) {
			for (ModuleDropdown moduleDropdown : moduleDropdowns) {
				moduleDropdown.render(posX, posY);
				posY += moduleDropdown.getComponentHeight();
			}
			parent.drawGradientRect(startX, startY + COMPONENT_HEIGHT, startX + COMPONENT_WIDTH, startY + COMPONENT_HEIGHT + 5, 0xFF303030, 0x00404040);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (hoveredComponent(mouseX, mouseY)) {
			if (mouseButton == 1) {
				expanded = !expanded;
			}
			if (mouseButton == 0) {
				dragging = true;
				startMousePos = new Vector2f(mouseX - posX, mouseY - posY);
			}
		} else if (expanded) {
			for (ModuleDropdown moduleDropdown : moduleDropdowns) {
				if (moduleDropdown.hovered(mouseX, mouseY)) {
					moduleDropdown.mouseClicked(mouseX, mouseY, mouseButton);
					return;
				}
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (dragging) {
			dragging = false;
		}
		if (hoveredComponent(mouseX, mouseY)) {
		} else if (expanded) {
			for (ModuleDropdown moduleDropdown : moduleDropdowns) {
				if (moduleDropdown.hovered(mouseX, mouseY)) {
					moduleDropdown.mouseReleased(mouseX, mouseY, state);
					return;
				}
			}
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (hoveredComponent(mouseX, mouseY)) {
		} else if (expanded) {
			for (ModuleDropdown moduleDropdown : moduleDropdowns) {
				if (moduleDropdown.hovered(mouseX, mouseY)) {
					moduleDropdown.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
					return;
				}
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

	public boolean hovered(int mouseX, int mouseY) {
		return mouseX > posX && mouseX < posX + getComponentWidth() &&
				mouseY > posY && mouseY < posY + getComponentHeight();
	}

	public boolean hoveredComponent(int mouseX, int mouseY) {
		return mouseX > posX && mouseX < posX + COMPONENT_WIDTH &&
				mouseY > posY && mouseY < posY + COMPONENT_HEIGHT;
	}

	public boolean hovered(float mouseX, float mouseY) {
		return hovered((int) mouseX, (int) mouseY);
	}

	public boolean hoveredComponent(float mouseX, float mouseY) {
		return hoveredComponent((int) mouseX, (int) mouseY);
	}
}

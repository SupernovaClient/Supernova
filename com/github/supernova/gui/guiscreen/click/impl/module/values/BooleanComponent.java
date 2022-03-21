package com.github.supernova.gui.guiscreen.click.impl.module.values;

import com.github.supernova.gui.guiscreen.click.impl.module.ModuleDropdown;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.modules.render.HUD;
import com.github.supernova.util.input.MouseUtil;
import com.github.supernova.util.render.ColourUtil;
import com.github.supernova.util.render.RenderUtil;
import com.github.supernova.value.impl.BooleanValue;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.io.IOException;

public class BooleanComponent extends ValueComponent {

	private ModuleDropdown parent;
	private BooleanValue value;


	public BooleanComponent(ModuleDropdown parent, BooleanValue value) {
		this.parent = parent;
		this.value = value;
	}

	private void updateValue() {
		value.setCurrentValue(value.getCurrentValue());
	}

	boolean setValue = false;
	private float posX, posY;

	@Override
	public void render(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
		HUD hudModule = (HUD) ModuleManager.INSTANCE.get(HUD.class);
		RenderUtil.drawRectWidth(posX, posY, getComponentWidth(), getComponentHeight(), 0xFF3A3A3A);
		posX += 3;
		posY += 3;

		float minXPos = posX + 1.2f;
		float maxXPos = posX + getComponentWidth() - 7.2f;
		Vector2f mouse = MouseUtil.getMousePos();

		if (setValue) {
			if (hoveredToggle((int) mouse.x, (int) mouse.y) && Mouse.isButtonDown(0)) {
				updateValue();
			}
		}
		float currentXPos = getComponentWidth() - 24;

		currentXPos += minXPos;
		RenderUtil.drawRectWidth(currentXPos - 0.5f, posY + 1, 14, getComponentHeight() - 8,
				value.getCurrentValue() ? hudModule.hudColourValue.getCurrentValue().getRGB() : 0xAAAAAAAA);
		RenderUtil.drawRectOutlineWidth(currentXPos - 0.5f, posY + 1, 14, getComponentHeight() - 8,
				0xDD222222, 1);
		mc.blockyFontObj.drawStringWithShadow(value.getValueName(), (int) posX + 2, (int) posY + 4,
				hoveredComponent((int) mouse.x, (int) mouse.y) ? 0xFFEEEEEE : 0xFFDADADA);

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (hoveredToggle(mouseX, mouseY)) {
				if (mouseButton == 0) {
					value.setCurrentValue(!value.getCurrentValue());
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		setValue = false;
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

	public boolean hovered(int mouseX, int mouseY) {
		return mouseX > posX && mouseX < posX + getComponentWidth() &&
				mouseY > posY && mouseY < posY + getComponentHeight();
	}

	public boolean hoveredToggle(int mouseX, int mouseY) {
		return mouseX >= posX + 3 && mouseX <= posX + 3 + getComponentWidth() - 6 &&
				mouseY >= posY + 3 && mouseY <= posY + 3 + getComponentHeight() - 6;
	}

	public boolean hoveredComponent(int mouseX, int mouseY) {
		return mouseX > posX && mouseX < posX + COMPONENT_WIDTH &&
				mouseY > posY && mouseY < posY + COMPONENT_HEIGHT;
	}

	@Override
	public float getComponentHeight() {
		return 22;
	}

}

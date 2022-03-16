package io.github.nevalackin.gui.guiscreen.click.impl.module;

import io.github.nevalackin.gui.guiscreen.click.impl.CategoryDropdown;
import io.github.nevalackin.gui.guiscreen.click.impl.Component;
import io.github.nevalackin.gui.guiscreen.click.impl.module.values.*;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.util.input.MouseUtil;
import io.github.nevalackin.util.render.RenderUtil;
import io.github.nevalackin.value.Value;
import io.github.nevalackin.value.impl.*;
import net.minecraft.client.renderer.GlStateManager;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ModuleDropdown extends Component {

    private ArrayList<ValueComponent> valueComponents = new ArrayList<>();
    private CategoryDropdown parent;
    private Module module;

    public ModuleDropdown(CategoryDropdown parent, Module module) {
        this.parent = parent;
        this.module = module;
        for(Value<?> value : module.getValues()) {
            if(value instanceof NumberValue) {
                valueComponents.add(new NumberComponent(this, (NumberValue) value));
            } else if (value instanceof BooleanValue) {
                valueComponents.add(new BooleanComponent(this, (BooleanValue) value));
            } else if (value instanceof ColourValue) {
                valueComponents.add(new ColourComponent(this, (ColourValue) value));
            } else if (value instanceof EnumValue<?>) {
                valueComponents.add(new ModeComponent(this, (EnumValue<?>) value));
            } else if (value instanceof MultiEnumValue<?>) {
                valueComponents.add(new MultiModeComponent(this, (MultiEnumValue<?>) value));
            }
        }
    }

    public Module getModule() {
        return this.module;
    }

    boolean expanded = false;
    @Override
    public float getComponentHeight() {
        float height = COMPONENT_HEIGHT;
        if(expanded) {
            for (ValueComponent component : valueComponents) {
                height += component.getComponentHeight();
            }
        }
        return height;
    }

    private float posX, posY;
    @Override
    public void render(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        Vector2f mouse = MouseUtil.getMousePos();
        int componentColour = module.isEnabled() ? 0xFF404070 : 0xFF404040;
        componentColour = hoveredComponent((int)mouse.x, (int)mouse.y) ?
                new Color(componentColour).brighter().getRGB() : componentColour;
        RenderUtil.drawRectWidth(posX, posY, COMPONENT_WIDTH, COMPONENT_HEIGHT, componentColour);
        mc.blockyFontObj.drawStringWithShadow(module.getModuleName(),posX+4,posY + (COMPONENT_HEIGHT /2f - mc.blockyFontObj.FONT_HEIGHT /2f) ,0xFFDADADA);
        posY += COMPONENT_HEIGHT;
        if(valueComponents.size() > 0) {
            String expandedString = expanded ? "-" : ">";
            int expandedColour = expanded ? 0xFFBABABA : 0xFFDADADA;
            expandedColour = hoveredComponent((int)mouse.x, (int)mouse.y) ?
                    new Color(expandedColour).brighter().getRGB() : expandedColour;
            mc.blockyFontObj.drawStringWithShadow(expandedString, this.posX + COMPONENT_WIDTH - 10, this.posY + 1 + (COMPONENT_HEIGHT /2f - mc.blockyFontObj.FONT_HEIGHT /2f),expandedColour);
            if (expanded) {
                for (ValueComponent valueComponent : valueComponents) {
                    valueComponent.render(posX, posY);
                    posY += valueComponent.getComponentHeight();
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        System.out.println("click");
        if(hoveredComponent(mouseX,mouseY)) {
            if(mouseButton == 0) {
                module.toggleModule();
            } else if (mouseButton == 1) {
                expanded = !expanded;
            }
        } else if(expanded) {
            for(ValueComponent valueComponent : valueComponents) {
                valueComponent.mouseClicked(mouseX,mouseY,mouseButton);
            }
        }
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(expanded) {
            for(ValueComponent valueComponent : valueComponents) {
                valueComponent.mouseReleased(mouseX,mouseY,state);
            }
        }
    }
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(expanded) {
            for(ValueComponent valueComponent : valueComponents) {
                valueComponent.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
            }
        }
    }

    public boolean hovered(int mouseX, int mouseY) {
        return mouseX > posX && mouseX < posX + getComponentWidth() &&
                mouseY > posY && mouseY < posY + getComponentHeight();
    }
    public boolean hoveredComponent(int mouseX, int mouseY) {
        return mouseX > posX && mouseX < posX + COMPONENT_WIDTH &&
                mouseY > posY && mouseY < posY + COMPONENT_HEIGHT;
    }
}

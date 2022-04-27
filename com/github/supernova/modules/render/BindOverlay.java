package com.github.supernova.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.misc.MouseClickedEvent;
import com.github.supernova.events.misc.MouseReleasedEvent;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.gui.GuiHUD;
import com.github.supernova.gui.impl.DraggableElement;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.util.font.FontManager;
import com.github.supernova.util.input.MouseUtil;
import com.github.supernova.util.render.RenderUtil;
import com.github.supernova.value.impl.ColourValue;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.stream.Collectors;

@ModuleAnnotation(name = "Bind Overlay", description = "Shows Binds and Toggled State", displayName = "Bind Overlay", category = Category.RENDER)
public class BindOverlay extends Module {

    ColourValue barColourValue = new ColourValue("Bar Colour", 0xffffc4fc);

    public BindOverlay() {
        setValues(barColourValue);
    }

    ArrayList<Module> modules = ModuleManager.INSTANCE.getBindedModules();
    DraggableElement bindElement = new DraggableElement(10,10, 100,10,
            0) {

        private final float textHeight = FontManager.monoFont15.getFontHeight();

        @Override
        public void drawElement() {
            updateElement();
            RenderUtil.drawCustomBox(getPosX(), getPosY(), getWidth(), getHeight(), barColourValue.getInt(), 2.5f);
            FontManager.monoFont15.drawString("Status", getPosX()+3, getPosY()+5, 0xFFDADADA, false);
            ArrayList<String> stringArrayList = new ArrayList<>();
            for(Module module : modules) {
                String text = "["+ Keyboard.getKeyName(module.getKeyCode())+"] "+module.getModuleName()+": "+module.isEnabled();
                stringArrayList.add(text);
            }
            if(stringArrayList.isEmpty()) {
                stringArrayList.add("No Modules Bound");
                stringArrayList.add("Bind some using .bind <module> <key>");
            }
            setHeight(20+(stringArrayList.size()*(textHeight)));
            float currentY = getPosY()+7+textHeight;
            setWidth(0);
            for(String text : stringArrayList) {
                int width = FontManager.monoFont15.getStringWidth(text)+8;
                if(getWidth() < width) {
                    setWidth(width);
                }
            }
            for(String text : stringArrayList) {
                if(text.equals("No Modules Bound")) {
                    int strWidth = FontManager.monoFont15.getStringWidth("No Modules Bound");
                    FontManager.monoFont15.drawString(text, getPosX() + (getWidth() / 2f) - (strWidth / 2f), currentY, 0xFFDADADA, false);
                } else {
                    FontManager.monoFont15.drawString(text, getPosX() + 2, currentY, 0xFFDADADA, false);
                }
                currentY += textHeight;
            }
        }
    };

    @EventHandler
    public final Listener<EventRender2D> eventRender2DListener = event -> {
        bindElement.drawElement();
    };

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = event -> {
        modules = ModuleManager.INSTANCE.getBindedModules().stream()
                .filter(mod -> !(mod instanceof BindOverlay) && !(mod instanceof ClickGUI))
                .collect(Collectors.toCollection(ArrayList::new));
    };
    @EventHandler
    public final Listener<MouseClickedEvent> mouseClickedEventListener = event -> {
        if(event.shouldUse())
            event.setUsed( bindElement.onMouseClick(event.getMouseX(), event.getMouseY(), event.getMouseButton()) );
    };
    @EventHandler
    public final Listener<MouseReleasedEvent> mouseReleasedEventListener = event -> {
        bindElement.onMouseReleased();
    };

}

package com.github.supernova.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.value.impl.BooleanValue;
import com.github.supernova.value.impl.EnumValue;
import com.github.supernova.value.impl.NumberValue;

@ModuleAnnotation(name = "World", description = "Does stuff", category = Category.RENDER, displayName = "World")
public class World extends Module {

    public final BooleanValue changeTime = new BooleanValue("Change Time", true);
    public final NumberValue timeValue = new NumberValue("Time", 6000, 0, 24000, 1000);
    public final BooleanValue changeWeather = new BooleanValue("Change Weather", true);

    public World() {
        setValues(changeTime, timeValue);
    }

    @EventHandler
    public final Listener<EventUpdate> updateListener = event -> {
        if(changeTime.getCurrentValue()) {
            mc.theWorld.setWorldTime(timeValue.getInt());
        }
    };

    enum WeatherTypes implements ModeEnum {
        SUNNY("Sunny");

        public final String name;
        WeatherTypes(String name) {
            this.name = name;
        }
        @Override
        public String getName() {
            return name;
        }
    }

}

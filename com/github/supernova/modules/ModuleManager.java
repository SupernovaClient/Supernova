package com.github.supernova.modules;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.Supernova;
import com.github.supernova.events.misc.EventKey;
import com.github.supernova.modules.combat.BowAura;
import com.github.supernova.modules.macro.CropNuker;
import com.github.supernova.modules.render.ClickGUI;
import com.github.supernova.modules.render.DebugESP;
import com.github.supernova.modules.render.HUD;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleManager {

	public static final ModuleManager INSTANCE = new ModuleManager();

	private final ArrayList<Module> modules = new ArrayList<>();
	private boolean initiated;

	public void init() {
		if (initiated) return;
		initiated = true;
		Supernova.INSTANCE.getEventBus().register(this);

		modules.add(new HUD());
		modules.add(new ClickGUI());
		modules.add(new CropNuker());
		modules.add(new DebugESP());
		modules.add(new BowAura());
	}

	@EventHandler
	public Listener<EventKey> eventKeyListener = eventKey -> {
		for (Module m : modules) {
			if (m.getKeyCode() == eventKey.getKeyCode()) {
				m.toggleModule();
			}
		}
	};

	public ArrayList<Module> getModules() {
		return this.modules;
	}

	public Module get(Class<? extends Module> m) {
		return getModules().stream().filter(module -> module.getClass() == m).findFirst().orElse(null);
	}

	public Module get(String name) {
		return getModules().stream().filter(mod -> mod.getModuleName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public ArrayList<Module> getModulesByCategory(Category c) {
		return getModules().stream().filter(mod -> mod.getCategory() == c).collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<Module> getEnabledModules() {
		return getModules().stream().filter(Module::isEnabled).collect(Collectors.toCollection(ArrayList::new));
	}
}

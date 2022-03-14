package me.royalty.supernova.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.royalty.supernova.events.misc.EventKey;
import me.royalty.supernova.modules.render.HUD;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleManager {

    public static final ModuleManager INSTANCE = new ModuleManager();

    private final ArrayList<Module> modules = new ArrayList<>();
    private boolean initiated;

    public void init() {
        if(initiated) return;
        initiated = true;
        EventManager.register(this, EventKey.class);

        modules.add(new HUD());
    }

    @EventTarget
    public void onKey(EventKey event) {
        for (Module m : modules) {
            if (m.getKeyCode() == event.getKeyCode()) {
                m.toggleModule();
            }
        }
    }

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

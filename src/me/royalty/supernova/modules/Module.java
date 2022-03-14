package me.royalty.supernova.modules;

import com.darkmagician6.eventapi.EventManager;
import lombok.Getter;
import lombok.Setter;
import me.royalty.supernova.value.Value;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;

public class Module {

    protected final Minecraft mc;
    @Getter @Setter
    private final String moduleName;
    @Getter
    private final String moduleDescription;
    @Getter
    private final Category category;
    @Getter @Setter
    protected boolean enabled;
    @Getter @Setter
    protected boolean visible;
    @Getter @Setter
    private int keyCode = 0;
    @Getter @Setter
    private String moduleDisplayName;
    private ArrayList<Value<?>> moduleValues = new ArrayList<>();


    public Module(String moduleName, String moduleDescription, Category category, boolean visible) {
        this.moduleName = moduleName;
        this.moduleDisplayName = moduleName;
        this.moduleDescription = moduleDescription;
        this.visible = visible;
        this.category = category;
        mc = Minecraft.getMinecraft();
    }

    public ArrayList<Value<?>> getValues() {
        return this.moduleValues;
    }

    public void setValues(Value<?>... values) {
        this.moduleValues = new ArrayList<>(Arrays.asList(values));
    }

    public void toggleModule() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }
    public void setEnabled(boolean enabled) {
        if(this.enabled == enabled) return;
        this.enabled = enabled;
        if (this.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void onEnable() {
        EventManager.register(this);
    }
    public void onDisable() {
        EventManager.unregister(this);
    }
}

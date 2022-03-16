package io.github.nevalackin.modules;

import io.github.nevalackin.Supernova;
import io.github.nevalackin.value.Value;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;

public class Module {
	protected Minecraft mc;
	private String moduleName;
	private String moduleDescription;
	private Category category;
	protected boolean enabled;
	protected boolean visible;
	private int keyCode = 0;

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public void setModuleDisplayName(String moduleDisplayName) {
		this.moduleDisplayName = moduleDisplayName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getModuleDescription() {
		return moduleDescription;
	}

	public Category getCategory() {
		return category;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isVisible() {
		return visible;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public String getModuleDisplayName() {
		return moduleDisplayName;
	}

	public ArrayList<Value<?>> getValues() {
		return moduleValues;
	}

	private String moduleDisplayName;
	private ArrayList<Value<?>> moduleValues = new ArrayList<>();


	public Module() {
		if (!this.getClass().isAnnotationPresent(ModuleAnnotation.class)) return;
		ModuleAnnotation annotation = this.getClass().getAnnotation(ModuleAnnotation.class);
		this.moduleName = annotation.name();
		this.moduleDisplayName = annotation.displayName();
		this.moduleDescription = annotation.description();
		this.visible = annotation.visible();
		this.category = annotation.category();
		mc = Minecraft.getMinecraft();
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
		if (this.enabled == enabled) return;
		this.enabled = enabled;
		if (this.enabled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}

	public void onEnable() {
		Supernova.INSTANCE.getEventBus().register(this);
	}

	public void onDisable() {
		Supernova.INSTANCE.getEventBus().unregister(this);
	}
}

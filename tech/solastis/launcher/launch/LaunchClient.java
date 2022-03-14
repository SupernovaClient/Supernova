package tech.solastis.launcher.launch;

import net.minecraft.client.main.Main;

import java.io.File;

public class LaunchClient {
	
	
	private static final String APPDATA = System.getenv("APPDATA");
	private static final File WORKING_DIRECTORY = new File(APPDATA, ".minecraft/");
	
	public static void launch() {
		Main.main(new String[]{
				"--version", "1.8.9",
				"--accessToken", "0", "--assetIndex", "1.8",
				"--userProperties", "{}",
				"--gameDir", new File(WORKING_DIRECTORY, ".").getAbsolutePath(),
				"--assetsDir", new File(WORKING_DIRECTORY, "assets/").getAbsolutePath(), "--session"
		});
	}
	
	public static void main(String[] args) {
		launch();
	}
}

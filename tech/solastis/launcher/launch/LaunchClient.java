package tech.solastis.launcher.launch;

import net.minecraft.client.main.Main;

import java.io.File;

public class LaunchClient {

	private static final String APPDATA = System.getenv("APPDATA");
	private static final File WORKING_DIRECTORY_WIN = new File(APPDATA, ".minecraft/");

	private static final boolean isMac = System.getProperty("os.name").contains("Mac");
	private static final String USER_HOME = System.getProperty("user.home");
	private static final File WORKING_DIRECTORY_MAC = new File(USER_HOME, "Library/Application Support/minecraft/");
	
	public static void launch() {

		final File WORKING_DIRECTORY = isMac ? WORKING_DIRECTORY_MAC : WORKING_DIRECTORY_WIN;

		Main.main(new String[]{
				"--version", "1.8.9",
				"--accessToken", "0", "--assetIndex", "1.8",
				"--userProperties", "{}",
				"--gameDir", new File(WORKING_DIRECTORY, ".").getAbsolutePath(),
				"--assetsDir", new File(WORKING_DIRECTORY, "assets/").getAbsolutePath()
		});
	}
	
	public static void main(String[] args) {
		launch();
	}
}

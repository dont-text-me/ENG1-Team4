package com.isometric.game.desktop;

//import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.isometric.game.GameScreen;
import com.isometric.game.Isometric;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(GameScreen.WIDTH, GameScreen.HEIGHT);
		config.setWindowSizeLimits(GameScreen.WIDTH, GameScreen.HEIGHT, 3840, 2160);
		config.useVsync(true);
//		DO NOT CHANGE THIS UNLESS YOU WANT THE WORLD'S FASTEST CANNONBALLS
		config.setForegroundFPS(60);
		new Lwjgl3Application(new Isometric(), config);
	}
}

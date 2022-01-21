package com.isometric.game.desktop;

//import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.isometric.game.GUI;
import com.isometric.game.GameScreen;
import com.isometric.game.Isometric;

public class DesktopLauncher1 {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(GameScreen.WIDTH, GameScreen.HEIGHT);
		config.useVsync(true);
		new Lwjgl3Application(new GUI(), config);
	}
}

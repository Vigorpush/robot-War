package view;

import controller.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LobbyView {

	private static final String title = "Robot Wars";
	private Scene lobbyScene;
	
	public Scene init() {
		VBox startScreen = new VBox(40);
		Button joinGame = new Button("Scene Change");
	
		Button hostGame = new Button("Host Game");
		Button exitGame = new Button("Exit Game");
		startScreen.getChildren().addAll(joinGame, hostGame, exitGame);
		startScreen.setAlignment(Pos.CENTER);
		lobbyScene = new Scene(startScreen);
		return lobbyScene;
	}
}

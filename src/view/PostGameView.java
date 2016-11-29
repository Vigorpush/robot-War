package view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PostGameView {

	Scene postGameScene;
	
	public Scene init() {
		HBox postGameBox = new HBox();
		Label test = new Label("You have finished the game!");
		postGameBox.getChildren().add(test);
		postGameScene = new Scene(postGameBox);
		return postGameScene;
	}

}

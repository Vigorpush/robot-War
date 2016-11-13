package view;

import controller.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartView {

	private static final String title = "Robot Wars";
	private Scene startScene;

	public Scene init() {
		VBox startScreen = new VBox(40);
		Button joinGame = new Button("Join Game");
		joinGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				joinGame();
			}
		});
		Button hostGame = new Button("Host Game");
		hostGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				hostGame();
			}
		});
		Button exitGame = new Button("Exit Game");
		exitGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Game controller = new Game();
				controller.exitGame();
						
			}
		});
		startScreen.getChildren().addAll(joinGame, hostGame, exitGame);
		startScreen.setAlignment(Pos.CENTER);
		startScene = new Scene(startScreen);
		return startScene;
	}

	private void joinGame() {
		Stage selectGame = new Stage();
		VBox selectBox = new VBox(20);
		HBox nameBox = new HBox(10);
		HBox addressBox = new HBox(10);
		HBox buttonBox = new HBox(30);
		Label nameLabel = new Label("Name: ");
		TextField nameTxt = new TextField();
		nameBox.getChildren().addAll(nameLabel, nameTxt);
		Label addressLabel = new Label("Address: ");
		TextField addressTxt = new TextField();
		addressBox.getChildren().addAll(addressLabel, addressTxt);
		Button closeBtn = new Button("Close");
		closeBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				selectGame.close();
			}
		});
		Button joinBtn = new Button("Join");
		joinBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Game controller = new Game();
				if (controller.joinGame(nameTxt.getText(), addressTxt.getText())) {
					selectGame.close();
				}

			}
		});
		buttonBox.getChildren().addAll(closeBtn, joinBtn);
		selectBox.getChildren().addAll(nameBox, addressBox, buttonBox);
		selectBox.setAlignment(Pos.CENTER);
		Scene selectScene = new Scene(selectBox);
		selectGame.setScene(selectScene);
		selectGame.initModality(Modality.APPLICATION_MODAL);
		selectGame.show();
	}

	private void hostGame() {
		Stage selectGame = new Stage();
		VBox selectBox = new VBox(20);
		HBox nameBox = new HBox(10);
		HBox buttonBox = new HBox(30);
		Label nameLabel = new Label("Name: ");
		TextField nameTxt = new TextField();
		nameBox.getChildren().addAll(nameLabel, nameTxt);
		Button closeBtn = new Button("Close");
		closeBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				selectGame.close();
			}
		});
		Button joinBtn = new Button("Join");
		joinBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Game controller = new Game();
				controller.hostGame(nameTxt.getText());
				selectGame.close();
			}
		});
		buttonBox.getChildren().addAll(closeBtn, joinBtn);
		selectBox.getChildren().addAll(nameBox, buttonBox);
		selectBox.setAlignment(Pos.CENTER);
		Scene selectScene = new Scene(selectBox);
		selectGame.setScene(selectScene);
		selectGame.initModality(Modality.APPLICATION_MODAL);
		selectGame.show();
	}

}

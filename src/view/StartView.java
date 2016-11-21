package view;

import java.io.File;

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

/**
 * Class that creates the initial GUI that the user will see.
 * @author Niklaas Zang jiawei
 *
 */
@SuppressWarnings("unused")
public class StartView {

	private static final String title = "Robot Wars";
	private Scene startScene;
	

	

	/**
	 * Method for creating the StartView Scene
	 * @return the startScene
	 */
	public Scene init() {
		VBox startScreen = new VBox(40);
		Button joinGame = new Button("Join Game");
		joinGame.getStyleClass().add("join_game_button");
		
		joinGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				joinGame();
			}
		});
		Button hostGame = new Button("Host Game");
		hostGame.getStyleClass().add("host_game_button");
		hostGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				hostGame();
			}
		});
		Button exitGame = new Button("Exit Game");
		exitGame.getStyleClass().add("exit_game_button");
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
	
	/**
	 * joinGame creates a popup window that asks for a username and an IP Address and passes them onto the controller
	 */
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
				//If the controller approves, the popup closes.
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
		File file = new File("src/controller/Miniwindow.css");
		selectGame.getScene().getStylesheets().clear();
		selectGame.getScene().getStylesheets().add("file:///"+file.getAbsolutePath().replace("\\", "/"));
		closeBtn.getStyleClass().add("join_button");
		joinBtn.getStyleClass().add("close_button");
		nameLabel.getStyleClass().add("text_label");
		addressLabel.getStyleClass().add("text_label");
		//Modality prevents the user from clicking on the main Stage while the popup is up
		selectGame.initModality(Modality.APPLICATION_MODAL);
		selectGame.show();
	}

	/**
	 * hostGame creates a popup window that asks for a username, and passes it onto the controller
	 */
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
		
		File file = new File("src/controller/Miniwindow.css");
		selectGame.getScene().getStylesheets().clear();
		selectGame.getScene().getStylesheets().add("file:///"+file.getAbsolutePath().replace("\\", "/"));
		closeBtn.getStyleClass().add("join_button");
		joinBtn.getStyleClass().add("close_button");
		nameLabel.getStyleClass().add("text_label");
		selectGame.initModality(Modality.APPLICATION_MODAL);
		selectGame.show();
	}

}

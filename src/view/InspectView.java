package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InspectView {

	private InspectTableViewController tc;
	public void init(final Stage primaryStage, int x, int y) {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		VBox dialogVbox = new VBox(20);
		tc  = new InspectTableViewController(x,y);
		Button closeButton = new Button("Close");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				dialog.close();
			}
		});
		dialogVbox.getChildren().addAll(tc.inspectTable, closeButton);
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.show();
	}
}

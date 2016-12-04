package view;


import java.sql.ResultSet;
import java.sql.SQLException;

import controller.Game;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tableRow.RobotHealth;

public class HealthTableViewController {
	// Table
	public TableView<RobotHealth> robotHealthTable = new TableView<RobotHealth>();

	// Table columns
	protected TableColumn<RobotHealth, String> robot1Column = new TableColumn<RobotHealth, String>("Scout");
	protected TableColumn<RobotHealth, String> robot2Column = new TableColumn<RobotHealth, String>("Sniper");
	protected TableColumn<RobotHealth, String> robot3Column = new TableColumn<RobotHealth, String>(
			"Tank");

	// database helper

	private ObservableList<RobotHealth> robotHealthData = FXCollections.observableArrayList();

	/**
	 * 
	 * Constructor for the robotHealthTableViewController class.
	 */
	public HealthTableViewController() {
		// When the activity log is instantiated, then pull all the information
		// from the database.
		retrieveRobotHealthData();
		this.initialize();
		robotHealthTable.setMaxHeight(53);
		robotHealthTable.setMaxWidth(280);
		robotHealthTable.setItems(robotHealthData);
		robotHealthTable.setFocusTraversable(false);
	}

	/**
	 * 
	 * Purpose: Refresh the table on the GUI
	 */
	public void refreshTable() {
		this.robotHealthData.clear();
		this.robotHealthTable.getColumns().clear();
		this.retrieveRobotHealthData();
		this.initialize();
	}

	/**
	 * 
	 * Purpose: Query the database for all the participant's allergies
	 */
	private void retrieveRobotHealthData() {

		// Select all everything
		Game controller = new Game();
		int[] healths = controller.getRobotHealths();

		String robot1Health;
		String robot2Health;
		String robot3Health;


			robot1Health = ""+healths[0];
			robot2Health = ""+healths[1];
			robot3Health = ""+healths[2];

			// Remove extra 0's at the end of the timestamp
			// dosage = dosage.substring(0, dosage.length() - 7);

			RobotHealth healthRow = new RobotHealth(robot2Health, robot1Health, robot3Health);

			// Add the log to the list
			robotHealthData.add(healthRow);

		

	}

	/**
	 * 
	 * Purpose: to create the table and the columns
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		robot1Column.setCellValueFactory(cellData -> cellData.getValue().getRobot2Health());
		robot1Column.setMinWidth(75);
		robot1Column.setResizable(false);

		robot2Column.setCellValueFactory(cellData -> cellData.getValue().getRobot1Health());
		robot2Column.setMinWidth(75);
		robot2Column.setResizable(false);

		robot3Column.setCellValueFactory(cellData -> cellData.getValue().getRobot3Health());
		robot3Column.setMinWidth(75);
		robot3Column.setResizable(false);

		robotHealthTable.getColumns().addListener(new ListChangeListener<Object>() {
			@Override
			public void onChanged(Change change) {
				change.next();
				// if the column was changed
				if (change.wasReplaced()) {
					// clear all columns
					robotHealthTable.getColumns().clear();
					// re-add the columns in order
					robotHealthTable.getColumns().addAll(robot1Column, robot2Column, robot3Column);
				}
			}
		});
		robotHealthTable.getColumns().addAll(robot1Column, robot2Column, robot3Column);

	}

}

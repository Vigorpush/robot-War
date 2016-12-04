package view;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.Game;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import model.Robot;
import tableRow.InspectRow;

public class InpsectTableViewController {
	// Table
	public TableView<InspectRow> inspectTable = new TableView<InspectRow>();

	// Table columns
	protected TableColumn<InspectRow, String> colorColumn = new TableColumn<InspectRow, String>("Color");
	protected TableColumn<InspectRow, String> modelColumn = new TableColumn<InspectRow, String>("Model");
	protected TableColumn<InspectRow, String> healthColumn = new TableColumn<InspectRow, String>(
			"Health");
	private String[] numberColors = new String[]{"Red", "Green", "Blue", "Yellow", "Purple", "Orange"};
	// database helper

	private ObservableList<InspectRow> robotData = FXCollections.observableArrayList();

	/**
	 * 
	 * Constructor for the robotHealthTableViewController class.
	 */
	public InpsectTableViewController(int x, int y) {
		// When the activity log is instantiated, then pull all the information
		// from the database.
		retrieveInspectRowData(x,y);
		this.initialize();
		//inspectTable.setMaxHeight(53);
		inspectTable.setMaxWidth(280);
		inspectTable.setItems(robotData);
		inspectTable.setFocusTraversable(false);
	}

	/**
	 * 
	 * Purpose: Refresh the table on the GUI
	 */
	public void refreshTable() {
		/*this.robotHealthData.clear();
		this.inspectTable.getColumns().clear();
		this.retrieveInspectRowData();
		this.initialize();
		*/
	}

	/**
	 * 
	 * Purpose: Query the database for all the participant's allergies
	 */
	private void retrieveInspectRowData(int x, int y) {

		// Select all everything
		Game controller = new Game();
		
		List<Robot> test = controller.gameBoard.gameBoard[x][y].robotList;
		String color;
        String model;
        String health;
        for(int i = 0; i < test.size(); i++)
        {
        	color = numberColors[test.get(i).teamNumber];
        	model = test.get(i).getClass().getSimpleName();
        	health = ""+test.get(i).health;
        	InspectRow row = new InspectRow(color, model, health);
        	robotData.add(row);
        	
        }		

	}

	/**
	 * 
	 * Purpose: to create the table and the columns
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		colorColumn.setCellValueFactory(cellData -> cellData.getValue().getColor());
		colorColumn.setMinWidth(75);
		colorColumn.setResizable(false);

		modelColumn.setCellValueFactory(cellData -> cellData.getValue().getModel());
		modelColumn.setMinWidth(75);
		modelColumn.setResizable(false);

		healthColumn.setCellValueFactory(cellData -> cellData.getValue().getHealth());
		healthColumn.setMinWidth(75);
		healthColumn.setResizable(false);

		inspectTable.getColumns().addListener(new ListChangeListener<Object>() {
			@Override
			public void onChanged(Change change) {
				change.next();
				// if the column was changed
				if (change.wasReplaced()) {
					// clear all columns
					inspectTable.getColumns().clear();
					// re-add the columns in order
					inspectTable.getColumns().addAll(colorColumn, modelColumn, healthColumn);
				}
			}
		});
		inspectTable.getColumns().addAll(colorColumn, modelColumn, healthColumn);

	}

}

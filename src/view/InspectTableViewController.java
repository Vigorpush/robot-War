package view;



import java.util.List;

import controller.Game;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Robot;
import tableRow.InspectRow;

public class InspectTableViewController {
	// Table
	public TableView<InspectRow> inspectTable = new TableView<InspectRow>();

	// Table columns
	protected TableColumn<InspectRow, String> colorColumn = new TableColumn<InspectRow, String>("Color");
	protected TableColumn<InspectRow, String> modelColumn = new TableColumn<InspectRow, String>("Model");
	protected TableColumn<InspectRow, String> healthColumn = new TableColumn<InspectRow, String>(
			"Health");
	private String[] numberColors = new String[]{"Red", "Green", "Blue", "Yellow", "Purple", "Orange"};

	private ObservableList<InspectRow> robotData = FXCollections.observableArrayList();

	/**
	 * 
	 * Constructor for the robotHealthTableViewController class.
	 */
	public InspectTableViewController(int x, int y) {
		retrieveInspectRowData(x,y);
		this.initialize();
		inspectTable.setMaxWidth(225);
		inspectTable.setItems(robotData);
		inspectTable.setFocusTraversable(false);
	}

	/**
	 * 
	 * Purpose: retrieves the information about the robots on a tile
	 */
	private void retrieveInspectRowData(int x, int y) {
		
		List<Robot> test = Game.gameBoard.gameBoard[x][y].robotList;
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
        inspectTable.setMinHeight(test.size()*30);
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

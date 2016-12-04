package tableRow;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * Purpose: represent the visible information of a robot after it has been inspected
 * 
 * @author Niklaas
 * @version 1.0
 *
 */
public class InspectRow {

	private StringProperty color;
    private StringProperty model;
    private StringProperty health;

    public InspectRow(String color, String model, String health)
    {
        this.color = new SimpleStringProperty(color);
        this.model = new SimpleStringProperty(model);
        this.health = new SimpleStringProperty(health);
    }

    /**
     * 
     * Purpose: return the color of the robot
     * 
     * @return
     */
    public StringProperty getColor()
    {
        return color;
    }

    /**
     * 
     * Purpose: return the model of the robot
     * 
     * @return
     */
    public StringProperty getModel()
    {
        return model;
    }

    /**
     * 
     * Purpose: return the health of the robot
     * 
     * @return
     */
    public StringProperty getHealth()
    {
        return health;
    }

}

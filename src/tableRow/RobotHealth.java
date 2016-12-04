package tableRow;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * Purpose: represent an allergy object and also have helper methods to create,
 * delete, and update allergies
 * 
 * @author Team CIMP
 * @version 1.0
 *
 */
public class RobotHealth
{
    private StringProperty robot1Health;
    private StringProperty robot2Health;
    private StringProperty robot3Health;

    public RobotHealth(String robot1, String robot2, String robot3)
    {
        this.robot1Health = new SimpleStringProperty(robot1);
        this.robot2Health = new SimpleStringProperty(robot2);
        this.robot3Health = new SimpleStringProperty(robot3);
    }

    /**
     * 
     * Purpose: return the name of the persons allergies
     * 
     * @return
     */
    public StringProperty getRobot1Health()
    {
        return robot1Health;
    }

    /**
     * 
     * Purpose: return the dosage of the medication that the person is taking
     * 
     * @return
     */
    public StringProperty getRobot2Health()
    {
        return robot2Health;
    }

    /**
     * 
     * Purpose: return the times that the person is to receive the medication
     * 
     * @return
     */
    public StringProperty getRobot3Health()
    {
        return robot3Health;
    }

}
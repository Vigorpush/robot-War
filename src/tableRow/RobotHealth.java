package tableRow;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * Purpose: represents the health of a player's robots
 * 
 * @author Niklaas
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
     * Purpose: return the health of the scout
     * 
     * @return
     */
    public StringProperty getRobot1Health()
    {
        return robot1Health;
    }

    /**
     * 
     * Purpose: return the health of the sniper
     * 
     * @return
     */
    public StringProperty getRobot2Health()
    {
        return robot2Health;
    }

    /**
     * 
     * Purpose: return the health of the tank
     * 
     * @return
     */
    public StringProperty getRobot3Health()
    {
        return robot3Health;
    }

}
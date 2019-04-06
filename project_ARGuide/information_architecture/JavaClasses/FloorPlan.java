import java.util.*;

/**
 * Describes the plan of a floor in a building
 * The int parameter, floor, is the number of the respective floor
 * 
 * @author stefan
 *
 */
public class FloorPlan {
/**
 * The number of this floor
 */
  public int floor;
  /**
   * A list of the different locations (i.e rooms,bathrooms,hallways) which are found this floor
   */

  public List<Object> locations;
  
  /**
   * The adjacency matrix of the floor graph
   */

  public Integer[][] floorGraph;
 

}
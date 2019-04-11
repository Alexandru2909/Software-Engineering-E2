import java.util.Map;

/**
 * This class refers to the user of the application
 * @author stefan
 *
 */
public class User {
 /**
  * The current position of this user
  */
  public Location currentLocation;
  /**
   * The destination where this user wants to arrive
   */

  public Location destination;
  /**
   * Sets the current position of this user
   * @param currentloc the current position, given as input 
   */
  
  public void setCurrentLocation(Location currentloc) {
	  
  }
  
  /**
   * Sets the destination where this user wants to arrive
   * @param destination the location where the user wants to arrive ,
   *        given as input
   */
  
  public void setDestinationn(Location destination) {
	  
  }
  
  /**
   * This sends to this user the shortest path from the current position 
   * to the destination
   * @param SPG the shortest path generator 
   */
  public Map<Integer, Object[]> getShortestPath(ShortestPathGenerator SPG) {
	  return SPG.sendSP();
  }

}
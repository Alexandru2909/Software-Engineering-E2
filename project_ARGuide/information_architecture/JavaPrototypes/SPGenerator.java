import java.util.Map;

/**
 * Computes the shortest path between a given location and a destination 
 * within a building
 * @author stefan
 *
 */
public class ShortestPathGenerator {
/**
 * The respective building plan and working schedule
 */
  public BuildingPlan  myBuildingPlan;
  public WorkingSchedule  myWorkingSchedule;
  public Map<Integer,Object[]> path = null;

  /**
   * Computes the shortest path
   */

  public void computeSP() {
  }
  /**
   * Send the shortest path
   * @return path a map of the form floor-locations, the locations array being sorted in 
   *         the order of which the user must visit them
   */

  public Map<Integer, Object[]> sendSP() {
	  return path;
  }

}

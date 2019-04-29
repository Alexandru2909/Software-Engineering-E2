/**
 * This class handles the processes which are being made over a building plan
 * @author stefan
 *
 */
import java.util.Map;
public class BuildingPlanProc throws BuildingPlanException {
  /**
   * Parses the building plan 
   * @param building the building plan we want to parse
   * @return graph a map of the form floor->adjacency matrix of the floor graph
   */

  public Map<Integer,Integer[][]> parseBP(BuildingPlan building) {
	  Map<Integer,Integer[][]> graph = null;
	  return graph;
  }
  /**
   * Saves the given building plan
   * @param plan the building plan we want to save
   */
  public void saveBP(BuildingPlan plan) {
  }
  /**
   * Removes the given building plan
   * @param plan the building plan we want to remove
   */

  public void removeBP(BuildingPlan plan) {
  }
  /**
   * Updates the given building plan
   * @param plan the building plan we want to update
   */

  public void updateBP(BuildingPlan plan) {
  }

}

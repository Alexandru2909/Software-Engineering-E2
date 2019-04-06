import java.util.Vector;
/**
 * This class reffers to the building plan of the building we want
 * to add in our database.
 * The vector myFloorPlan contains the structure of every floor
 * in our building.
 *
 * @author stefan
 *
 */

public class BuildingPlan {

  public Vector<FloorPlan>  myFloorPlan;
  
  /**
   * This method is called for finding out the status of a process
   * @param process the process which we want to know if it has successfully finished or not
   * @return status false in case of failure, true otherwise
   */
   
  public Boolean getStatus(BuildingPlanProc process) {
	  Boolean status=false;
	  return status;
  }
  /**
   * This method is called to send a request 
   * 
   * @param process the process we want to be made
   * 
   */

  public void sendRequest(String process) {
  }

}
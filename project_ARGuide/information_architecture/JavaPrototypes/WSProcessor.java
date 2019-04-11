import java.util.Map;
/**
 * This class handles the processes which are being made over a working schedule
 * @author stefan
 *
 */
public class WSProcessor {
 /**
   * Parses the working schedule 
   * @param schedule the working schedule we want to parse
   * @return timetable a map of the form Day-(Hour,Subject) representing the timetable
   */

  public Map<String, Map<Integer,String> > parseWorkingSchedule(WorkingSchedule schedule) {
	  Map<String, Map<Integer,String> > timetable = null;
	  return timetable;
  }
  /**
   * Saves the given working schedule
   * @param schedule the working schedule we want to save 
   */
  public void saveWorkingSchedule(WorkingSchedule schedule) {
  }
  /**
   * Updates the given working schedule
   * @param schedule the working schedule we want to update 
   */

  public void updateWorkingSchedule(WorkingSchedule schedule) {
  }
  /**
   * Removes the given working schedule
   * @param schedule the working schedule we want to remove
   */

  public void removeWorkingSchedule(WorkingSchedule schedule) {
  }

}

import java.util.*;

/**
 * Class Room describes the rooms of the building we are referring to
 * The roomNumber parameter refers to the number of the room
 * The timetable parameter refers to the timetable of the room
 * @author stefan
 *
 */
public class Room {
  
  public int roomNumber;
  /**
   * A map of the form Day-(Hour,Subject) representing the timetable
   */
  public Map<String, Map<Integer,String> > timetable;

}
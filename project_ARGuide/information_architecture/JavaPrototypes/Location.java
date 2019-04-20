/**
 * A specific location in a building
 * @author stefan
 *
 */

public class Location {

  public int id;
  /**
 * The floor on which this location is found
 */
  public int floor;
  /**
   * The number of the room, if it is the case
   */

  public string name;
  /**
   * Returns the floor of this location
   */
  
  public int getId(){
      return this.id;
  };
  
  public int getFloor(){
      return this.floor;
  };
  
  public string getName(){
      return this.name;
  };
  
  public void setId(int idd){
      this.id=idd;
  };
  
  public void setFloor(int floorr){
      this.floor=floorr;
  };
  
  public void setName(string namee){
      this.name=namee;
  };


}

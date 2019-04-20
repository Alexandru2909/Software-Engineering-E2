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
  
  public int getid(){
      return this.id;
  };
  
  public int getfloor(){
      return this.floor;
  };
  
  public string getname(){
      return this.name;
  };
  
  public void setid(int idd){
      this.id=idd;
  };
  
  public void setfloor(int floorr){
      this.floor=floorr;
  };
  
  public void setname(string namee){
      this.name=namee;
  };


}

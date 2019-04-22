import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.*;

/**
 * Computes the shortest path between a given location and a destination 
 * within a building
 * @author stefan
 *
 */
public class ShortestPathGenerator {
/**
 * The respective building plan and working schedule
 * 
 */
  private final User user;
  public  final BuildingGraph  myBuildingGraph;
  public List<Location> shortestPath = new ArrayList<Location>();
  
  public ShortestPathGenerator(User user,BuildingGraph g) {
	  this.user=user;
	  this.myBuildingGraph=g;
  }
  
  public List<Location> getShortestPath(){
	  return this.shortestPath;
  }
  
  public void setShortestPath() {
	  this.shortestPath=aStar(user.getCurrentLocation(),user.getDestination());
  }
  
  public List<Location> aStar(Location start,Location destination){
	  
	  class Properties{
		  int g=0;
		  int h=0;
		  int f=0;
		  int x;
		  int y;
		  Location parent;
		  
		  public Properties(int x,int y) {
			  this.x=x;
			  this.y=y;
		  }
	  }
	  
	  Map<Integer,Properties> locations = new HashMap<Integer,Properties>();
	  List<List<Location>> buildingGraph = this.myBuildingGraph.getGraph();
	  List<Location> path = new ArrayList<Location>();
	  	  
	  List<Location> openSet = new ArrayList<Location>();
	  List<Location> closedSet = new ArrayList<Location>();
	  
	  openSet.add(start);
	  
	  
	  while(!openSet.isEmpty())
	  {
		  int lowestf=0;
		  for(int i=0;i<openSet.size();i++) {
			  if( locations.get(openSet.get(i).id).f < locations.get(openSet.get(lowestf).id).f ) {
				  lowestf=i;
			  }
		  }
		  
		  Location current = openSet.get(lowestf);
		  
		  if(current==destination) {
			  Location temp=current;
			  
			  while(temp!=null)
			  {
				  path.add(locations.get(temp).parent);
				  temp=locations.get(temp).parent;
			  }
			  
		  }
		  
		  openSet.remove(current);
		  closedSet.add(current);
		  
		  List<Location> neighbours=new ArrayList<Location>();
		  
		  for(int i=0;i<buildingGraph.size();i++)
		  {
			  if(buildingGraph.get(i).get(0)==current)
				  for(int j=0;j<buildingGraph.get(i).size();j++)
				  {
					  neighbours.add(buildingGraph.get(j).get(0));
					 
				  }
		  }
		  
		  
		  for(int i=0;i<neighbours.size();i++) {
			  Location neighbour=neighbours.get(i);
			  
			  if(closedSet.contains(neighbour)) 
				  continue;
				  
			  int tempG = locations.get(current).g+1;
				  
			  if(!openSet.contains(neighbour)) 
				  openSet.add(neighbour);
			  else 
				  if ( tempG >= locations.get(neighbour).g) 
					continue;
			  
			  locations.get(neighbour).h = Math.abs(locations.get(neighbour).x-locations.get(destination).x)+Math.abs(locations.get(neighbour).y-locations.get(destination).y);
			  locations.get(neighbour).f = locations.get(neighbour).g+locations.get(neighbour).h;
			  locations.get(neighbour).parent = current;
			  
			  
		  }
			  
		  
	  }
	  return path;
	 
	  
	  
  }
}
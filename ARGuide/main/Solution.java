/**
 * 
 */
package main;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Paul-Reftu
 *
 */
public class Solution {

	/**
	 * 
	 */
	public Solution() {}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			ARGuide arGuide = new ARGuide("ARGuide/database/faculty.db", "ARGuide/schedules/facultySchedule.json", "ARGuide/buildingPlan/jsonFormat/buildingPlan.json");
			List<String> classroomNames = arGuide.selectAllClassroomNames();
			
			System.out.println("All classrooms: ");
			for (String classroomName : classroomNames)
				System.out.println(classroomName);
			
		} catch (ClassNotFoundException | JSONResourceException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
	}

}

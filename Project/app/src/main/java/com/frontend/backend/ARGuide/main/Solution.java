package com.frontend.backend.ARGuide.main;

import android.app.Activity;

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
	public Solution() {
	}

	/**
	 * @param args the arguments of the main function call
	 */
	public static void main(String[] args) {
		try {
			ARGuide arGuide = new ARGuide("faculty_uaic_cs",
					"app/src/main/java/com/frontend/backend/ARGuide/database/faculty.db",
					"app/src/main/java/com/frontend/backend/ARGuide/schedules/facultySchedule.json",
					"app/src/main/java/com/frontend/backend/ARGuide/buildingPlan/jsonFormat/buildingPlan.json");
			List<String> classroomNames = arGuide.selectAllClassroomNames();

			System.out.println("All classrooms: ");
			for (String classroomName : classroomNames)
				System.out.println(classroomName);
			System.out.println();

			/*
			 * Each returned entry in the list has the following format:
			 * "<day> <starting_time> <ending_time> <course_name>"
			 * each individual entry above is stored in a list of strings INSIDE the main list of strings that has
			 * all schedule entries
			 */
			List<List<String>> C909_schedule = arGuide.selectClassroomSchedule("C909");

			System.out.println("Schedule of room C909 is: ");
			for (List<String> scheduleEntry : C909_schedule) {
				for (String dataEntry : scheduleEntry) {
					System.out.print(dataEntry + " ");
				}
				System.out.println();
			}
		} catch (JSONResourceException | SQLException e) {
			e.printStackTrace();
		}


		return;
	}

}
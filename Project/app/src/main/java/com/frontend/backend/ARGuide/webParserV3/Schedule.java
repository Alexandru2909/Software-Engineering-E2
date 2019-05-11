/**
 * 
 */
package webParserV3;

import java.util.LinkedList;

/**
 * @author Paul-Reftu
 *
 */
public class Schedule {
	/*
	 * list of schedules of each room
	 */
	private LinkedList<DataRecord> roomSchedules;
	
	/**
	 * 
	 */
	public Schedule() {
		roomSchedules = new LinkedList<DataRecord>();
	}

	/**
	 * @return the roomSchedules
	 */
	public LinkedList<DataRecord> getRoomSchedules() {
		return roomSchedules;
	}

	/**
	 * @param roomSchedules the roomSchedules to set
	 */
	public void setRoomSchedules(LinkedList<DataRecord> roomSchedules) {
		this.roomSchedules = roomSchedules;
	}

	/*
	 * @param roomSchedule the new room schedule to be added
	 * adds a new room schedule to the front of the list
	 */
	public void add(DataRecord roomSchedule) {
		roomSchedules.push(roomSchedule);
	}
}

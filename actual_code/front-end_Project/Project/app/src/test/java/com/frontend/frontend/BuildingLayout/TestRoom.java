package com.frontend.frontend.BuildingLayout;

import static org.junit.Assert.assertEquals;

import org.junit.*;

public class TestRoom {

	@Test
	public void displayRoomInfoTest() {
		Rooms testRoom = new Rooms("1",1);
		testRoom.setInfo("place holder info");
		assertEquals("place holder info",testRoom.getInfo());
	}
	
	@Test
	public void getIdTest() {
		Rooms testRoom = new Rooms("1",1);
		assertEquals("1",testRoom.getId());
	}
	
	@Test
	public void getFloorNumberTest() {
		Rooms testRoom = new Rooms("1",1);
		assertEquals(1,testRoom.getFloorNumber());
	}
	
	@Test
	public void setIdTest() {
		Rooms testRoom = new Rooms("1",1);
		testRoom.setId("2");
		assertEquals("2",testRoom.getId());
	}

	@Test
	public void setFloorNumberTest() {
		Rooms testRoom = new Rooms("1",1);
		testRoom.setFloorNumber(2);
		assertEquals(2,testRoom.getFloorNumber());
	}

}

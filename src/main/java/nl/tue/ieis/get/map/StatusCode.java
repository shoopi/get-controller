package main.java.nl.tue.ieis.get.map;

public enum StatusCode {
	
	TRANSPORTATION_RUNNING(1),
	TRANSPORTATION_FINISHED(0),
	TRANSPORTATION_SUSPENDED(-1),
	TRANSPORTATION_UNEXPECTED_EVENT(2),
	TRANSPORTATION_SUSPENDED_REPLANNED(3),
	TRANSPORTATION_SUSPENDED_REMOVED(4),
	TRANSPORTATION_FLIGHT_WARNING(5),
	TRANSPORTATION_FLIGHT_DIVERSION(6),
	TRANSPORTATION_LOCK_CLOSED(7),
	TRANSPORTATION_CONGESTION(8),
	TRANSPORTATION_DEADLINE_VIOLATION(9),

	TRUCK_FREE(800),
	TRUCK_BUSY(801),
	TRUCK_BUSY_REPLANNED(803),
	TRUCK_UNEXPECTED_POINT(802),

	TRAIN_FREE(400),
	TRAIN_BUSY(401),
	
	
	BARGE_FREE(600),
	BARGE_BUSY(601),

	
	//FLIGHT_FREE(200),
	FLIGHT_BUSY(201),
	FLIGHT_DIVERSION(202);
		
	private final int value;
	
	private StatusCode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
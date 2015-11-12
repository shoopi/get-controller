package main.java.nl.tue.ieis.get.map;

public enum Icon {

	TRUCK_FREE_ICON("/imgs/truck5.png"),
	TRUCK_BUSY_ICON("/imgs/truck4.png"),
	
	FLIGHT_BUSY_ICON("/imgs/plane_map2.gif"),
	FLIGHT_DIVERTED_ICON("/imgs/plane_map_diversion2.gif"),
	
	TRAIN_BUSY_ICON("/imgs/train.png"),
	TRAIN_FREE_ICON("/imgs/train_free.png"),
	
	BARGE_FREE_ICON("/imgs/ship_free.png"),
	BARGE_BUSY_ICON("/imgs/ship.png"),

	UNEXPECTED_POINT_MAP("/imgs/direction_down.png"),
	
	WARNING_ON_MAP("/imgs/warning.gif"),
	
	EVENT_FLIGHT_WARNING_ICON("/imgs/warningIcon.png"),
	EVENT_FLIGHT_DIVERSION_ICON("/imgs/close.png"),
	EVENT_ARRIVED_AT_POINT_ICON("/imgs/arrived.png"),
	EVENT_START_AT_POINT_ICON("/imgs/started.png"),
	EVENT_DEADLINE_ICON("/imgs/deadline.png"),
	EVENT_PREDICTED_DEADLINE_ICON("/imgs/clock.png"),
	EVENT_TRAFFIC_ICON("/imgs/traffic.png"),
	EVENT_VEHICLE_INCIDENT_ICON("/imgs/emergency_icon.jpg"),
	EVENT_BARGE_CLOSE_TERMIANL("/imgs/harbor.png"),
	
	PROCESS_MODEL_OK("/imgs/ok.png"),
	PROCESS_MODEL_WARNING("/imgs/war.png"),
	PROCESS_MODEL_ERROR("/imgs/nok.png"),
	
	SIDEBAR_SHOW_ALL("/imgs/Cars2.png"),
	SIDEBAR_TRUCK("/imgs/truck_icon2.jpg"),
	SIDEBAR_TRUCK_REPLANNED("/imgs/truck_icon2_replanned.png"),	
	SIDEBAR_FLIGHT("/imgs/plane.png"),
	SIDEBAR_TRAIN("/imgs/train_icon.jpg"),
	SIDEBAR_SHIP("/imgs/ship_icon.jpg"),
	SIDEBAR_UNEXPECTED_EVENT("/imgs/error_icon2.png"),
	SIDEBAR_FLIGHT_DIVERSION("/imgs/plane_error.png"),
	SIDEBAR_FLIGHT_WARNING("/imgs/plane_warning.png"),
	SIDEBAR_FLIGHT_REPLAN("/imgs/plane_replan.png"),
	SIDEBAR_LOCK_CLOSED("/imgs/lockClosed_icon.png"),
	SIDEBAR_TRUCK_DEADLINE("/imgs/truck_icon2_deadline.png"),
	SIDEBAR_TRUCK_CONGESTION("/imgs/truck_icon2_traffic.png"),
	SIDEBAR_ERROR("/imgs/cross_icon.jpg");
	
	private final String url;
	
	private Icon(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}

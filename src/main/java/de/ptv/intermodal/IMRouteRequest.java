package main.java.de.ptv.intermodal;

public class IMRouteRequest {
	IMStopOff[] StopOffs;
	IMRoutingOptions Options;
	
	
	public IMStopOff[] getStopOffs() {
		return StopOffs;
	}

	public void setStopOffs(IMStopOff[] stopOffs) {
		StopOffs = stopOffs;
	}

	public IMRoutingOptions getOptions() {
		return Options;
	}
	
	public void setOptions(IMRoutingOptions options) {
		Options = options;
	}
	
	
	
}

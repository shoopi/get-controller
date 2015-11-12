package main.java.de.ptv.intermodal;


public class IMRoutingOptions {
	
	/* container type:
	 * 	1=TEU (20ft container)
	 *	2=FEU (40ft container) 
	 *	Default FEU 
	 */
	int ContainerTypeId = 2;

	//TODO: This should be changed to DATE()
	String StartTime;
	
	
	/*
	 * true = RollingHighway (RoLa in German -> Rollenda Landstrasse) or RoRo Ferries (Roll on / Roll Off) 
	 *	default = false 
	 */
	boolean Accompanied;
	
	//default = false 
	boolean RegardDrivingHours;
	
	//default 10000
	int MaxCosts = 10000;
		 	 
	//default 0
	int TimeCostWeight = 0;

	//(=99 if all relevant alternatives should be calculated) 
	int NoOfAlternatives;
		 	
	IMOperator[] ExcludedOperators;
	
	TransportMode[] ExcludedTransportModes;
	
	String 	FreightTariffFile;
	
	Transport 	Transport;

	
	
	
	public IMOperator[] getExcludedOperators() {
		return ExcludedOperators;
	}

	public TransportMode[] getExcludedTransportModes() {
		return ExcludedTransportModes;
	}

	public void setExcludedOperators(IMOperator[] excludedOperators) {
		ExcludedOperators = excludedOperators;
	}

	public void setExcludedTransportModes(TransportMode[] excludedTransportModes) {
		ExcludedTransportModes = excludedTransportModes;
	}

	public int getContainerTypeId() {
		return ContainerTypeId;
	}

	public String getStartTime() {
		return StartTime;
	}

	public boolean isAccompanied() {
		return Accompanied;
	}

	public boolean isRegardDrivingHours() {
		return RegardDrivingHours;
	}

	public int getMaxCosts() {
		return MaxCosts;
	}

	public int getTimeCostWeight() {
		return TimeCostWeight;
	}

	public int getNoOfAlternatives() {
		return NoOfAlternatives;
	}


	public String getFreightTariffFile() {
		return FreightTariffFile;
	}

	public Transport getTransport() {
		return Transport;
	}

	public void setContainerTypeId(int containerTypeId) {
		ContainerTypeId = containerTypeId;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public void setAccompanied(boolean accompanied) {
		Accompanied = accompanied;
	}

	public void setRegardDrivingHours(boolean regardDrivingHours) {
		RegardDrivingHours = regardDrivingHours;
	}

	public void setMaxCosts(int maxCosts) {
		MaxCosts = maxCosts;
	}

	public void setTimeCostWeight(int timeCostWeight) {
		TimeCostWeight = timeCostWeight;
	}

	public void setNoOfAlternatives(int noOfAlternatives) {
		NoOfAlternatives = noOfAlternatives;
	}

	public void setFreightTariffFile(String freightTariffFile) {
		FreightTariffFile = freightTariffFile;
	}

	public void setTransport(Transport transport) {
		Transport = transport;
	}
	
}

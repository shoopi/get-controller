package main.java.nl.tue.ieis.get.map;

import java.util.ArrayList;
import java.util.List;

import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;

public class Helper {

    private TransportOrderDataManagement tpdm = new TransportOrderDataManagement();

    public List<Asset> traceAllAssets() {
		List<Asset> allAssets = new ArrayList<Asset>();
    	try{
    		allAssets = tpdm.loadAllRunningAssets();
    	} catch(Exception e) {System.out.println(e.getMessage());}
    	return allAssets;
    }
    
    public List<Asset> traceFreeAssets() {
		List<Asset> freeAssets = new ArrayList<Asset>();
    	try {
    		List<Asset> allAssets = tpdm.loadAllRunningAssets();
    		for(Asset asset : allAssets) {
    			if(asset.getStatus() == StatusCode.TRUCK_FREE.getValue()) {
    					//|| asset.getStatus() == StatusCode.TRAIN_FREE.getValue() || asset.getStatus() == StatusCode.FLIGHT_FREE.getValue()
    				freeAssets.add(asset);
    			}
    		}
    	} catch(Exception e) {System.out.println(e.getMessage()); }
		return freeAssets;
	}
    
    public List<Asset> loadAssetByCaseID(String caseID) {
    	try {
    		List<Asset> assets = new ArrayList<Asset>();
	    	String truck = tpdm.loadTruckIdByCaseId(caseID);
	    	String barge = tpdm.loadBargeIdByCaseId(caseID);
	    	String train = tpdm.loadTrainIdByCaseId(caseID);
	    	String flight = tpdm.loadFlightIdByCaseId(caseID);

	    	Asset truckAsset = tpdm.loadAssetById(truck);
	    	if(truckAsset != null) {
	    		assets.add(truckAsset);
	    	}
	    	Asset flightAsset = tpdm.loadAssetById(flight);
	    	if(flightAsset != null) {
	    		assets.add(flightAsset);
	    	}
	    	Asset bargesAsset = tpdm.loadAssetById(barge);
	    	if(bargesAsset != null) {
	    		assets.add(bargesAsset);
	    	}
	    	Asset trainAsset = tpdm.loadAssetById(train);
	    	if(trainAsset != null) {
	    		assets.add(trainAsset);
	    	}
	    	return assets;
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
		return null;
    }
    
    public List<Asset> traceAllFlights() {
    	return tpdm.loadAllFlights();
    }
    
    public List<Asset> traceAllFreeBarges() {
    	return tpdm.loadAllFreeBarges();
    }
    
    public List<Asset> traceAllBusyBarges() {
    	return tpdm.loadAllRunningBarges();
    }
}

package main.java.nl.tue.ieis.get.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import main.java.nl.tue.ieis.get.data.TransportOrderDataManagement;
import main.java.nl.tue.ieis.get.map.Asset;
import main.java.nl.tue.ieis.get.map.StatusCode;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox.Button;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

public class ReleaseAssetController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 8759951979458505590L;
	private TransportOrderDataManagement tpdm = new TransportOrderDataManagement();
	private List<Asset> requests = new ArrayList<Asset>();
	
	@Wire	private Listbox requestListbox;
	@Wire	private Window 	releaseWin;
	@Wire 	private Button 	releaseAllBtn;
	@Wire	private Timer 	suspendedAssetTimer;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		updateSuspendedAssetList();
	}

	private void updateSuspendedAssetList() {
		requestListbox.setMultiple(true);
		List<Asset> assets = tpdm.loadAllAssetsByStatus(StatusCode.FLIGHT_DIVERSION.getValue());
		for(Asset asset : assets) {
			if(!requests.contains(asset))
				requests.add(asset);
		}
		ListModelList<Asset> model = new ListModelList<Asset>(requests);
		model.setMultiple(true);
		requestListbox.setModel(model);
	}
	
	@Listen("onClick = #releaseAllBtn")
    public void releaseAsset() {
		Set<Listitem> selectedItems = requestListbox.getSelectedItems();
		boolean refreshRequired = false;
		for(Listitem i : selectedItems) {
        	Asset asset = (Asset)i.getValue();
        	if(asset.getStatus() == StatusCode.FLIGHT_DIVERSION.getValue()) {
        		tpdm.updateAssetStatus(asset.getMobOpId(), StatusCode.FLIGHT_BUSY.getValue());
        		if(tpdm.loadCaseIdByFlight(asset.getMobOpId()) != null && tpdm.loadCaseIdByFlight(asset.getMobOpId()).size() > 0) {
        			for(String caseId : tpdm.loadCaseIdByFlight(asset.getMobOpId()) ) {
        				tpdm.updateTransportStatus(caseId, StatusCode.TRANSPORTATION_RUNNING.getValue());
        			}
        		}
        	}//else if for other status
        	refreshRequired = true;
		}
		if(refreshRequired)
			releaseWin.detach();
    }
	
	@Listen("onTimer = #suspendedAssetTimer")
	public void updateTrafficList() {
		if(releaseWin.isVisible()) {
			updateSuspendedAssetList();
			suspendedAssetTimer.stop();
		}
	}
	
}
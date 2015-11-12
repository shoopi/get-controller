package main.java.nl.tue.ieis.get.activiti.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.Expression;

public class AssetSelection implements JavaDelegate {

	private Expression  truckId;
	public void execute(DelegateExecution execution) throws Exception {
		execution.setVariable("truckId", "57011");
	}
	public Expression getTruckId() {
		return truckId;
	}
	public void setTruckId(Expression truckId) {
		this.truckId = truckId;
	}

}

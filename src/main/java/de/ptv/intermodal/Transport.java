package main.java.de.ptv.intermodal;

public class Transport {
	
	double Weight;
	boolean Perishable;

	public double getWeight() {
		return Weight;
	}
	public boolean isPerishable() {
		return Perishable;
	}
	public void setWeight(double weight) {
		Weight = weight;
	}
	public void setPerishable(boolean perishable) {
		Perishable = perishable;
	}
	
}

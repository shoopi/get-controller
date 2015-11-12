package main.java.nl.tue.ieis.get.map;

import java.util.Date;

public class AddressTime {
	private Address address;
	private Date date;
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public AddressTime(Address address, Date date) {
		super();
		this.address = address;
		this.date = date;
	}
	public AddressTime() {
		this.date = new Date();
		this.address = new Address();
	}
	
}

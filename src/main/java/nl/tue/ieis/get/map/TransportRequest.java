package main.java.nl.tue.ieis.get.map;

import java.util.UUID;

public class TransportRequest {
	private String id;
	private String case_id;
	private AddressTime source;
	private AddressTime dest;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AddressTime getSource() {
		return source;
	}
	public void setSource(AddressTime source) {
		this.source = source;
	}
	public AddressTime getDest() {
		return dest;
	}
	public void setDest(AddressTime dest) {
		this.dest = dest;
	}
	/*
	public TransportRequest(String id, AddressTime source, AddressTime dest) {
		super();
		this.id = id;
		this.source = source;
		this.dest = dest;
	}
	*/
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public TransportRequest(String id, String case_id, AddressTime source,
			AddressTime dest) {
		super();
		this.id = id;
		this.case_id = case_id;
		this.source = source;
		this.dest = dest;
	}
	public TransportRequest() {
		this.id = UUID.randomUUID().toString();
		this.source = new AddressTime();
		this.dest = new AddressTime();

	}
}

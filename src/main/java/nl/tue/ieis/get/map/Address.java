package main.java.nl.tue.ieis.get.map;

public class Address {
	private String name;
	private String street;
	private String number;
	private String zipcode;
	private String city;
	private String country;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public Address(String name, String street, String number, String zipcode, String city,
			String country) {
		super();
		this.name = name;
		this.street = street;
		this.number = number;
		this.zipcode = zipcode;
		this.city = city;
		this.country = country;
	}
	
	public Address() {
		this.name = "";
		this.street = "";
		this.number = "";
		this.zipcode = "";
		this.city = "";
		this.country = "";
	}
	@Override
	public String toString() {
		String address = "";
		if(name != null)
			address = address + name + ", ";
		if(street != null)
			address = address + street + ", ";
		if(number != null)
			address = address + number + ", ";
		if(zipcode != null)
			address = address + zipcode + ", ";
		if(city != null)
			address = address + city + ", ";
		if(country != null)
			address = address + country + ", ";
		return address;
	 }
}

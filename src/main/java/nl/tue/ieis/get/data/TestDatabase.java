package main.java.nl.tue.ieis.get.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class TestDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//TransportOrderDataManagement tpm = new TransportOrderDataManagement();
		//tpm.createTable();
		//tpm.addTransportOrder("idhv", "route", "time1", "time2", 1);
		//tpm.updateStatus("idhv", 9);
		//System.out.println(tpm.estimateNewCaseId());
		
		Scanner in1;
		try {
			in1 = new Scanner(new FileReader("routes/route1.txt"));
			System.out.println(in1.nextLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

	}

}

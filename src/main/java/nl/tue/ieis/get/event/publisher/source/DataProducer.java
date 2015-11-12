package main.java.nl.tue.ieis.get.event.publisher.source;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.google.gson.Gson;

import main.java.de.ptv.intermodal.IMPoint;
import main.java.de.ptv.intermodal.IMRoute;
import main.java.nl.jdr.traces.Traces;


public class DataProducer {

	public static void main(String[] args) {
		createXMLFile("5246.xml", 5246);
		createXMLFile("7256.xml", 7256);
		createXMLFile("1849.xml", 1849);
	}

	private static void createXMLFile(String outputFileName, int source) {
		List<File> files = retrieveJdRData("C:/Users/spourmir/Dropbox/Get Service Data/trace/trace");
		List<Traces.Trace> truckTraces = new ArrayList<Traces.Trace>(); 
		
		for(int f = 0; f < 200 ; f++) {
			File file = files.get(f);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Traces.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Traces traces = (Traces)jaxbUnmarshaller.unmarshal(file);
				try {
					for(Traces.Trace trace : traces.getTrace()) {
						if(trace.getSource() == source) {
							if(trace.getSpeed() > 0) {
								truckTraces.add(trace);
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} catch (JAXBException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		
		//sort array based on the time.
		Collections.sort(truckTraces, new Comparator<Traces.Trace>() {
			public int compare(Traces.Trace t1, Traces.Trace t2) {
				return t1.getTime().compareTo(t2.getTime());
			}
		});
		
		JAXBContext jc;
		try {
			Traces t = new Traces();
			t.setTrace(truckTraces);
			jc = JAXBContext.newInstance(Traces.class);
	        Marshaller m = jc.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        OutputStream os = new FileOutputStream(outputFileName);
	        m.marshal(t, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<File> retrieveJdRData(String JdRDataPath) {
		List<File> output = new ArrayList<File>();
		File folder = new File(JdRDataPath);
		File[] listOfFiles = folder.listFiles();
		List<File> fileList = Arrays.asList(listOfFiles);
		for (File file : fileList) {
			if (file.isFile()) {
				if (file.getName().endsWith(".xml")) {
					output.add(file);
			  	}
		  	}
		}
		return output;
	}
	
	public static List<IMRoute> createRouteFromJdrData(int fileCounter) {
		List<File> files = retrieveJdRData("C:/Users/spourmir/Dropbox/Get Service Data/trace/trace");
		List<IMRoute> output = new ArrayList<IMRoute>();
		
		List<IMPoint> a7256 = new ArrayList<IMPoint>();
		List<IMPoint> a5246 = new ArrayList<IMPoint>();
		List<IMPoint> a7193 = new ArrayList<IMPoint>();
		List<IMPoint> a7262 = new ArrayList<IMPoint>();
		List<IMPoint> a1849 = new ArrayList<IMPoint>();
		
		for(int f = 0; f < fileCounter ; f++) {
			File file = files.get(f);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Traces.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Traces traces = (Traces)jaxbUnmarshaller.unmarshal(file);
				try {
					for(Traces.Trace trace : traces.getTrace()) {
						if(trace.getSource() == 7256) {
							IMPoint imPoint = new IMPoint();
							imPoint.setLatitude((trace.getCoordinate().getLatitude()).doubleValue());
							imPoint.setLongitude((trace.getCoordinate().getLongitude()).doubleValue());
							a7256.add(imPoint);
						} else if (trace.getSource() == 5246) {
							IMPoint imPoint = new IMPoint();
							imPoint.setLatitude((trace.getCoordinate().getLatitude()).doubleValue());
							imPoint.setLongitude((trace.getCoordinate().getLongitude()).doubleValue());
							a5246.add(imPoint);
						} else if (trace.getSource() == 7193) {
							IMPoint imPoint = new IMPoint();
							imPoint.setLatitude((trace.getCoordinate().getLatitude()).doubleValue());
							imPoint.setLongitude((trace.getCoordinate().getLongitude()).doubleValue());
							a7193.add(imPoint);
						} else if (trace.getSource() == 7262) {
							IMPoint imPoint = new IMPoint();
							imPoint.setLatitude((trace.getCoordinate().getLatitude()).doubleValue());
							imPoint.setLongitude((trace.getCoordinate().getLongitude()).doubleValue());
							a7262.add(imPoint);
						} else if (trace.getSource() == 1849) {
							IMPoint imPoint = new IMPoint();
							imPoint.setLatitude((trace.getCoordinate().getLatitude()).doubleValue());
							imPoint.setLongitude((trace.getCoordinate().getLongitude()).doubleValue());
							a1849.add(imPoint);
						}
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} catch (JAXBException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		IMRoute r1 = new IMRoute();
		IMPoint[] p1 = new IMPoint[a7256.size()]; 
		r1.setPolygon(a7256.toArray(p1));
		output.add(r1);
		
		IMRoute r2 = new IMRoute();
		IMPoint[] p2 = new IMPoint[a5246.size()]; 
		r2.setPolygon(a5246.toArray(p2));
		output.add(r2);
		
		IMRoute r3 = new IMRoute();
		IMPoint[] p3 = new IMPoint[a7193.size()]; 
		r3.setPolygon(a7193.toArray(p3));
		output.add(r3);
		
		IMRoute r4 = new IMRoute();
		IMPoint[] p4 = new IMPoint[a7256.size()]; 
		r4.setPolygon(a7262.toArray(p4));
		output.add(r4);
		
		IMRoute r5 = new IMRoute();
		IMPoint[] p5 = new IMPoint[a1849.size()]; 
		r5.setPolygon(a1849.toArray(p5));
		output.add(r5);
		
		for(IMRoute route : output) {
			Gson gson = new Gson();
			String json = gson.toJson(route);
			System.out.println(json);
			System.out.println();
		}
		
		System.out.println(output.size() + " route has been added.");
		return output;
	}

}

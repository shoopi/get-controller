package main.java.nl.tue.ieis.get.event.publisher.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import main.java.nl.tue.ieis.get.event.type.vehiclePosition.VehiclePosition;

import org.apache.axis2.AxisFault;

import main.java.de.hpi.EventProcessingPlatformWebserviceStub;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub.ImportEvents;
import main.java.nl.jdr.traces.Traces;
import main.java.nl.jdr.traces.Traces.Trace;

public class CreateVehicleEventFromJdRData {

	public static void main(String[] args) {
		try {
			createTraceEvent("1849.xml");
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}
	
	public static void createTraceEvent(String fileName) throws AxisFault {

		byte[] encoded; 
		FileInputStream fileInputStream = null;
		
		try {
			EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
			
			File file = new File(fileName);
			encoded = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
		    fileInputStream.read(encoded);
		    fileInputStream.close();

			JAXBContext jaxbContext = JAXBContext.newInstance(Traces.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Traces traces = (Traces)jaxbUnmarshaller.unmarshal(file);
			
			for(Trace trace : traces.getTrace()) {
				VehiclePosition vehicleLocation = new VehiclePosition();
				
				vehicleLocation.setOperatorId(BigInteger.valueOf(trace.getSource()));				
				String latitude = "0.00";
				String longitude = "0.00";
				if(trace.getCoordinate() != null) {
					if(trace.getCoordinate().getLatitude() != null) 
						latitude = trace.getCoordinate().getLatitude().toString();
					if(trace.getCoordinate().getLongitude() != null) 
						longitude = trace.getCoordinate().getLongitude().toString();	
				}
				vehicleLocation.setLatitude(Double.parseDouble(latitude));
				vehicleLocation.setLongitude(Double.parseDouble(longitude));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				vehicleLocation.setTimestamp(sdf.format(trace.getTime()));
				vehicleLocation.setMode("road");
				
				/*
				for(Property prt: trace.getProperty()) {
					if(prt.getKey().contentEquals("ATY"))
						if(prt.getValue().contentEquals("1_LAD")) {
							
							JAXBContext jaxbContext3 = JAXBContext.newInstance(LoadContainer.class);
							Marshaller jaxbMarshaller = jaxbContext3.createMarshaller();
							jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
							jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "LoadContainerType.xsd");
							
							LoadContainer loadContainer = new LoadContainer();
							GregorianCalendar c = new GregorianCalendar();
							c.setTime(new Date());
							XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
							loadContainer.setTimestamp(date);
							loadContainer.setTruckId(trace.getSource() + "");
							StringWriter str = new StringWriter();
							
							jaxbMarshaller.marshal(loadContainer, str);
							ImportEvents loadEvent = new ImportEvents();
							loadEvent.setXml(str.toString());
							ws.importEvents(loadEvent);
							
						}
				}
				*/
				
				JAXBContext jaxbContext2 = JAXBContext.newInstance(VehiclePosition.class);
				Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "VehiclePosition.xsd");
				
				StringWriter stringWriter = new StringWriter();
				jaxbMarshaller.marshal(vehicleLocation, stringWriter);
				
				ImportEvents event = new ImportEvents();
				event.setXml(stringWriter.toString());
				
				ws.importEvents(event);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

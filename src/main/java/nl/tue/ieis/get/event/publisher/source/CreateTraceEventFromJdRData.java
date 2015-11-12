package main.java.nl.tue.ieis.get.event.publisher.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.axis2.AxisFault;

import main.java.de.hpi.EventProcessingPlatformWebserviceStub;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub.ImportEvents;
import main.java.nl.jdr.traces.Traces;
import main.java.nl.jdr.traces.Traces.Trace;
public class CreateTraceEventFromJdRData {

	public static void createTraceEvent(String fileName) throws AxisFault {
		//EventProcessingPlatformWebservicePortType ws = new EventProcessingPlatformWebservicePortTypeProxy();
		EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
		byte[] encoded; 
		FileInputStream fileInputStream = null;
		try {
			
			//File file = new File("2012-02-01T15_52_02_521.xml");
			File file = new File(fileName);
			encoded = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
		    fileInputStream.read(encoded);
		    fileInputStream.close();

			JAXBContext jaxbContext = JAXBContext.newInstance(Traces.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Traces traces = (Traces)jaxbUnmarshaller.unmarshal(file);
			
			for(Trace trace : traces.getTrace()) {
				main.java.nl.tue.ieis.get.event.type.traceNew.Trace newTrace = new main.java.nl.tue.ieis.get.event.type.traceNew.Trace();
				
				main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Addressmatch addressMatch = new main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Addressmatch();
				main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Addressmatch.Address newAddress = new main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Addressmatch.Address();
				newAddress.setArea("");
				newAddress.setCity("");
				newAddress.setCountry("");
				newAddress.setStreet("");
				newAddress.setZipcode(BigInteger.valueOf(0));
				addressMatch.setAddress(newAddress);
				main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Addressmatch.Coordinate newCoord = new main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Addressmatch.Coordinate();
				String latitude = "0.00";
				String longitude = "0.00";
				if(trace.getCoordinate() != null) {
					if(trace.getCoordinate().getLatitude() != null) 
						latitude = trace.getCoordinate().getLatitude().toString();
					if(trace.getCoordinate().getLongitude() != null) 
						longitude = trace.getCoordinate().getLongitude().toString();	
				}
				newCoord.setLatitude(latitude);
				newCoord.setLongitude(longitude);
				
				addressMatch.setCoordinate(newCoord);
				addressMatch.setQuality(BigDecimal.ONE);
				newTrace.setAddressmatch(addressMatch);
				
				newTrace.setHeading(BigInteger.valueOf(trace.getHeading()));
				newTrace.setMileage(BigInteger.valueOf(trace.getMileage()));
				
				main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Property newProperty = new main.java.nl.tue.ieis.get.event.type.traceNew.Trace.Property();
				if(trace.getProperty().size() > 0 ) {
					newProperty.setKey(trace.getProperty().get(0).getKey());
					newProperty.setValue(trace.getProperty().get(0).getValue());
				} else {
					newProperty.setKey("");
					newProperty.setValue("");
				}
				newTrace.setProperty(newProperty);
				
				newTrace.setSource(BigInteger.valueOf(trace.getSource()));
				newTrace.setSpeed(BigInteger.valueOf(trace.getSpeed()));
				
				Date dateNew = trace.getTime();
				GregorianCalendar gCalendat = new GregorianCalendar();
				gCalendat.setTime(dateNew);
				XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendat);
				newTrace.setTime(date2.toXMLFormat());
				
				newTrace.setType(BigInteger.valueOf(trace.getType()));
				
				JAXBContext jaxbContext2 = JAXBContext.newInstance(main.java.nl.tue.ieis.get.event.type.traceNew.Trace.class);
				Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				// jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
				// "http://www.w3.org/2001/XMLSchema-instance");
				jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "traceNEW.xsd");
				//jaxbMarshaller.setProperty("xmlns", "");
				
				StringWriter stringWriter = new StringWriter();
				jaxbMarshaller.marshal(newTrace, stringWriter);
				
				//ws.importEvents(stringWriter.toString());

				
				ImportEvents event = new ImportEvents();
				event.setXml(stringWriter.toString());
				ws.importEvents(event);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}

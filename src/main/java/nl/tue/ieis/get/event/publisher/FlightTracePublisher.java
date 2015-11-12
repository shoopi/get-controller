package main.java.nl.tue.ieis.get.event.publisher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.java.de.hpi.EventProcessingPlatformWebserviceStub;
import main.java.de.hpi.EventProcessingPlatformWebserviceUnparsableExceptionException;
import main.java.de.hpi.EventProcessingPlatformWebserviceXMLParsingExceptionException;
import main.java.de.hpi.EventProcessingPlatformWebserviceStub.ImportEvents;

import org.apache.axis2.AxisFault;

public class FlightTracePublisher {

	public static void main(String[] args) throws AxisFault {
		EventProcessingPlatformWebserviceStub ws = new EventProcessingPlatformWebserviceStub();
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get("FlighttraceEvent.xml"));
			String xml = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
			ImportEvents event = new ImportEvents();
			event.setXml(xml);
			try {
				ws.importEvents(event);
			} catch (EventProcessingPlatformWebserviceXMLParsingExceptionException e) {
				e.printStackTrace();
			} catch (EventProcessingPlatformWebserviceUnparsableExceptionException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

package main.java.nl.tue.ieis.get.xml.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

class SchemaGenerator{
	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		Class[] classes = new Class[1]; 
		classes[0] = main.java.de.ptv.intermodal.IMRoute.class;
		//classes[1] = ptv.IntermodalPlanning.IMPoint.class;
		//classes[2] = ptv.IntermodalPlanning.IMStation.class;
		//classes[3] = ptv.IntermodalPlanning.IMEmission.class;
		//classes[4] = ptv.IntermodalPlanning.IMStationTimes.class;
		//classes[5] = ptv.IntermodalPlanning.IMStationTimeEvent.class;
		
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(classes);
			SchemaOutputResolver sor = new MySchemaOutputResolver();
			jaxbContext.generateSchema(sor);
			System.out.println("Schema is generated!");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		/*
		IMRoute imRoute = new IMRoute();
        imRoute.setName("testName");
 
        Map<String, Object> properties = new HashMap<String, Object>(1);
        properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
        JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(new Class[] {IMRoute.class}, properties);
			Marshaller marshaller = jc.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
	        // Output XML
	        marshaller.marshal(imRoute, System.out);
	 
	        // Output JSON
	        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.marshal(imRoute, System.out);
	        
		} catch (JAXBException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		*/
	}
}
class MySchemaOutputResolver extends SchemaOutputResolver {
    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
        File file = new File(suggestedFileName);
        StreamResult result = new StreamResult(file);
        result.setSystemId(file.toURI().toURL().toString());
        return result;
    }
}

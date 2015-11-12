package main.java.nl.jdr.traces;

import javax.xml.bind.annotation.XmlRegistry;

import main.java.nl.jdr.traces.Traces.Trace;
import main.java.nl.jdr.traces.Traces.Trace.Coordinate;
import main.java.nl.jdr.traces.Traces.Trace.Property;

@XmlRegistry
public class ObjectFactory {

	public ObjectFactory() {
    }

    public Trace createTracesTrace() {
        return new Trace();
    }


    public Coordinate createTracesTraceCoordinate() {
        return new Coordinate();
    }


    public Property createTracesTraceProperty() {
        return new Property();
    }


    public Traces createTraces() {
        return new Traces();
    }

}

package main.java.nl.tue.ieis.get.xml.utils;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;

import java.util.Iterator;
import java.io.InputStream;
import java.util.HashMap;


public class SchemaMapper {
	private HashMap mappings;

	  public SchemaMapper(){
	    mappings = new HashMap();
	  }
	
	  public void generate(InputStream xsdInputStream ) throws Exception {
		  XSOMParser parser = new XSOMParser();
		  parser.parse(xsdInputStream);
		  XSSchemaSet sset = parser.getResult();
		  
		  
		  Iterator itr = sset.iterateSchema();
		  while( itr.hasNext() ) {
		    XSSchema s = (XSSchema)itr.next();
		    
		    System.out.println("Target namespace: "+s.getTargetNamespace());
		    
		    Iterator jtr = s.iterateElementDecls();
		    while( jtr.hasNext() ) {
		      XSElementDecl e = (XSElementDecl)jtr.next();
		      System.out.println(e.getDefaultValue());
		      System.out.println(e.getFixedValue());
		      
		      System.out.print( e.getName() );
		      if( e.isAbstract() )
		        System.out.print(" (abstract)");
		      System.out.println();
		    }
		  }
		  
		  /* If any other namespace ...
		  XSSchema gtypesSchema = sset.getSchema("namescpace");
		  Iterator<XSComplexType> complexTypeIter = gtypesSchema.iterateComplexTypes();
	    
		  while (complexTypeIter.hasNext()) {
		      XSComplexType ct = (XSComplexType) complexTypeIter.next();
		      String typeName = ct.getName();
		      String baseTypeName = ct.getBaseType().getName();
		      System.out.println(typeName + " is a " + baseTypeName);
		  }
		  */
	
		  //Global Namescpace
		  XSSchema globalSchema = sset.getSchema("");

		  Iterator<XSType> typeIter = globalSchema.iterateTypes();
		  typeIter = globalSchema.iterateTypes();
		  while (typeIter.hasNext()) {
		      XSType ct = (XSType) typeIter.next();
		      String typeName = ct.getName();
		      if(!ct.isSimpleType()){
		    	  XSComplexType complex = ct.asComplexType();
		    	  
		      } else {
		    	  XSSimpleType simple = ct.asSimpleType();
		      }
		      String baseTypeName = ct.getBaseType().getName();
		      System.out.println("--- " + typeName + " is a " + baseTypeName);
		      System.out.println();
		  }
		  
		  /*
		  // the main entity of this file is in the Elements --> there should only be one!
		  if (globalSchema.getElementDecls().size() != 1) {
			  throw new Exception("Should be only one element type per file.");
		  }
	
		  XSElementDecl elementDcl = globalSchema.getElementDecls().values().toArray(new XSElementDecl[0])[0];
		  String entityType = elementDcl.getName();
		  XSContentType xsContentType = elementDcl.getType().asComplexType().getContentType();
		  XSParticle particle = xsContentType.asParticle();
		  if (particle != null) {
			  XSTerm term = particle.getTerm();
	      if (term.isModelGroup()) {
	        XSModelGroup xsModelGroup = term.asModelGroup();
	        term.asElementDecl();
	        XSParticle[] particles = xsModelGroup.getChildren();
	        String propertyName = null;
	        String propertyType = null;
	        XSParticle pp =particles[0];
	        for (XSParticle p : particles) {
	          XSTerm pterm = p.getTerm();
	          if (pterm.isElementDecl()) {            
	            propertyName = pterm.asElementDecl().getName();
	            if (pterm.asElementDecl().getType().getName() == null) {
	              propertyType = pterm.asElementDecl().getType().getBaseType().getName();
	            } else {
	              propertyType = pterm.asElementDecl().getType().getName();              
	            }
	            System.out.println(propertyName + " is a " + propertyType);
	          }
	        }
	      }
	    }*/
  }
} 
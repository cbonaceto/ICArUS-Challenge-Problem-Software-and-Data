/* 
 * NOTICE
 * This software was produced for the office of the Director of National Intelligence (ODNI)
 * Intelligence Advanced Research Projects Activity (IARPA) ICArUS program, 
 * BAA number IARPA-BAA-10-04, under contract 2009-0917826-016, and is subject 
 * to the Rights in Data-General Clause 52.227-14, Alt. IV (DEC 2007).
 * 
 * This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 * and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
 * in operational systems or environments.
 * 
 * Copyright (C) 2015 The MITRE Corporation. All Rights Reserved.
 * 
 */
package org.mitre.icarus.cps.feature_vector.phase_1.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import org.mitre.icarus.cps.feature_vector.phase_1.FeatureType;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.LocationIntelReport;
import org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature;

import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * Abstract base class for classes that parse KML feature vector files for tasks, roads, and regions.
 * 
 * @author Lily Wong
 *
 */
public abstract class FeatureVectorKmlParser {

	/** The URL to the kml file. */
	protected URL kmlFile;
	
	/** The grid size. */
	protected GridSize gridSize;
	
	/** The filename. */
	protected String filename;
	
	/** The JaK KML object. */
	protected Kml kml;
	
	/** The JaK KML document object. */
	protected Document doc;
	
	/**
	 * Constructor for a new KML object.  Used by CSV toKML() methods.
	 */
	protected FeatureVectorKmlParser() {
		this.kml = new Kml();
		this.doc = kml.createAndSetDocument();
	}
	
	/**
	 * Instantiates a new feature vector kml from URL.
	 * Instantiates KML and document elements from the file.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException 
	 */
	protected FeatureVectorKmlParser(URL url) throws IOException, ParseException {
		this.kmlFile = url;
		if(kmlFile != null && kmlFile.getFile() != null) {
			this.filename = kmlFile.getFile().substring(0, kmlFile.getFile().indexOf('.'));
		}
		this.gridSize = new GridSize();
		
		kml = Kml.unmarshal(kmlFile.openStream());
		if(kml != null && kml.getFeature() != null) {
			doc = (Document) kml.getFeature();
		}
		else {
			throw new ParseException("Error, invalid KML file: " + kmlFile.getFile(), 0);
		}
	}
	
	/**
	 * Instantiates a new feature vector kml from URL.
	 * Instantiates KML and document elements from the file.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException 
	 */
	protected FeatureVectorKmlParser(URL url, GridSize gridSize) throws IOException, ParseException {
		this.kmlFile = url;
		if(kmlFile != null && kmlFile.getFile() != null) {
			this.filename = kmlFile.getFile().substring(0, kmlFile.getFile().indexOf('.'));
		}
		this.gridSize = gridSize;
		
		kml = Kml.unmarshal(kmlFile.openStream());
		
		if(kml != null && kml.getFeature() != null) {
			doc = (Document) kml.getFeature();
		}
		else {
			throw new ParseException("Error, invalid KML file: " + kmlFile.getFile(), 0);
		}
	}
	
	/**
	 * Instantiates a new feature vector kml.
	 * Instantiates KML and document elements from the file.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	protected FeatureVectorKmlParser(String filepath) throws IOException, ParseException {
		this.kmlFile = new File( filepath ).toURI().toURL();
		if(kmlFile != null && kmlFile.getFile() != null) {
			this.filename = kmlFile.getFile().substring(0, kmlFile.getFile().indexOf('.'));
		}
		this.gridSize = new GridSize();
		
		kml = Kml.unmarshal(kmlFile.openStream());		
		
		if(kml != null && kml.getFeature() != null) {
			doc = (Document) kml.getFeature();
		}
		else {
			throw new ParseException("Error, invalid KML file: " + kmlFile.getFile(), 0);
		}
	}
	
	/**
	 * Instantiates a new feature vector kml.
	 * Instantiates KML and document elements from the file.
	 *
	 * @param filepath the filepath
	 * @param gridSize the grid size
	 * @throws IOException
	 * @throws ParseException
	 */
	protected FeatureVectorKmlParser(String filepath, GridSize gridSize) throws IOException, ParseException {
		this.kmlFile = new File( filepath ).toURI().toURL();
		this.gridSize = gridSize;
		if(kmlFile != null && kmlFile.getFile() != null) {
			this.filename = kmlFile.getFile().substring(0, kmlFile.getFile().indexOf('.'));
		}
		
		kml = Kml.unmarshal(kmlFile.openStream());
		
		if(kml != null && kml.getFeature() != null) {
			doc = (Document) kml.getFeature();
		}
		else {
			throw new ParseException("Error, invalid KML file: " + kmlFile.getFile(), 0);
		}
	}
	
	/**
	 * Sets the path to the KML file to parse and parse the file.
	 * 
	 * @param filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public void setKmlFilePath(String filepath) throws IOException, ParseException {
		this.kmlFile = new File( filepath ).toURI().toURL();
		parseKml();
	}

	/**
	 * Sets the URL to the KML file to parse and parse the file.
	 * 
	 * @param url
	 * @throws IOException
	 * @throws ParseException
	 */
	public void setKmlFileUrl(URL url) throws IOException, ParseException {
		this.kmlFile = url;
		parseKml();
	}
	
	/**
	 * Gets the KML document object.
	 *
	 * @return the document
	 */
	public Document getDocument() { return doc; }
	
	/**
	 * Parses the KML into objects.
	 * 
	 * @throws ParseException
	 */
	protected abstract void parseKml() throws ParseException;
	
	/**
	 * Converts the KML object to a CSV file.  
	 * Must be used with constructed KML objects, not ones from toKml() methods.
	 *
	 * @param filepath the file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract void writeCsvToFile( String filepath ) throws IOException;
	
	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Sets the filename.
	 *
	 * @param filename the new filename
	 */
	public void setFilename( String filename ) {
		this.filename = filename;
	}
	
	/**
	 * Get the string representation of the KML.
	 * 
	 * @return - the KML string
	 * @throws IOException
	 */
	public String getKmlString() throws IOException {
		if(kml != null) {
			ByteArrayOutputStream output = new ByteArrayOutputStream(); 
			kml.marshal(output);
			return output.toString();
		}
		return null;
	}
	
	/**
	 * Creates the tag. Wrapper.
	 *
	 * @param name the name
	 * @param ft the ft
	 * @return the data
	 */
	protected Data createTag( String name,  FeatureType ft ) {
		return createTag( name, ft.toString() );
	}
	
	/**
	 * Creates the tag.
	 *
	 * @param name the name
	 * @param val the val
	 * @return the data
	 */
	protected Data createTag( String name, String val ) {
		Data data = new Data(val);
		data.setName( name );
		return data;
	}

	/**
	 * Write to the KML to a file.
	 *
	 * @param filepath the filepath
	 * @throws FileNotFoundException the file not found exception
	 */
	public void writeToFile( String filepath ) throws FileNotFoundException {
		if(kml != null) {
			kml.marshal(new File(filepath));
		}
	}
	
	/**
	 * Builds the csv string for a road or region (non-INT) feature.
	 *
	 * @param location the location
	 * @return the string
	 */
	protected String buildCsvString ( GridLocation2D location ) {
		 String str = "";
		 str += location.getLocationId() + ",";
		 str += location.getType() + ",";
		 str += Math.round(location.getX()) + ",";
		 str += Math.round(location.getY()) + "\n";
		 return str;
	}
	
	/**
	 * Builds the csv string for a task, including INT fields.
	 *
	 * @param feature the feature
	 * @return the string
	 */
	protected String buildCsvString( Phase1Feature feature ) {
		 GridLocation2D location = feature.getLocation();
		 String str = "";
		 str += location.getLocationId() + ",";
		 str += location.getType() + ",";
		 str += Math.round(location.getX()) + ",";
		 str += Math.round(location.getY()) + ",";
		 if ( feature.hasIntelReport() ) {
			 LocationIntelReport lir = feature.getIntelReport();
			 if ( lir.hasSocintInfo() )
				 str += lir.getSocintInfo().toString() + ",";
			 else
				 str += ",";
			 if ( lir.hasImintInfo() )
				 str += lir.getImintInfo().toString() + ",";
			 else 
				 str += ",";
			 if ( lir.hasMovintInfo() )
				 str += lir.getMovintInfo().toString() + ",";
			 else
				 str += ",";
			 if ( lir.hasSigintInfo() )
				 str += lir.getSigintInfo().toString() + ",";
			 else
				 str += "\n";
		 } else
			 str += ",,,\n";
		 return str;
	}	
}
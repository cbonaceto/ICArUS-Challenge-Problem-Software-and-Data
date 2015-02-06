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
package org.mitre.icarus.cps.examples.phase_2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.mitre.icarus.cps.feature_vector.phase_2.AreaOfInterest;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;

import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * Provides examples for converting feature XML files to KML files.
 * 
 * @author LWONG
 */
public class KmlExample {
	
	/**
	 * Converts the specified Area Of Interest XML file to a KML file in the same directory.
	 * 
	 * @param xmlFilepath full or relative path to the XML
	 */
	public static void createAreaOfInterestKml(String xmlFilepath) {
		try {
			// open an output file
			FileOutputStream kmlFile = new FileOutputStream(xmlFilepath.replace(".xml", ".kml"));
			// load the XML into the feature object
			JAXBContext context = JAXBContext.newInstance(AreaOfInterest.class);
			Unmarshaller um = context.createUnmarshaller();
			AreaOfInterest areaOfInterest = 
					(AreaOfInterest) um.unmarshal(new FileReader(xmlFilepath));
			// convert to KML
			final Kml kml = areaOfInterest.getKMLRepresentation();
			// write to the output file
			kml.marshal(kmlFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts the specified Blue Locations XML file to a KML file in the same directory.
	 * 
	 * @param xmlFilepath full or relative path to the XML
	 */
	public static void createBlueLocationsKml(String xmlFilepath) {
		try {
			// open an output file
			FileOutputStream kmlFile = new FileOutputStream(xmlFilepath.replace(".xml", ".kml"));
			// load the XML into the feature object
			JAXBContext context = JAXBContext.newInstance(FeatureContainer.class);
			Unmarshaller um = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			FeatureContainer<BlueLocation> blueLocations = 
					(FeatureContainer<BlueLocation>)um.unmarshal(new FileReader(xmlFilepath));
			// convert to KML
			final Kml kml = blueLocations.getKMLRepresentation();
			// write to the output file
			kml.marshal(kmlFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Example main creates KML files for Mission 1 Area of Interest and Blue Locations.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String areaOfInterestXml = "data/Phase_2_CPD/exams/Sample-Exam-2/Mission1_Area_Of_Interest.xml";
		createAreaOfInterestKml(areaOfInterestXml);
		
		String blueLocationsXml = "data/Phase_2_CPD/exams/Sample-Exam-2/Mission1_Blue_Locations.xml";
		createBlueLocationsKml(blueLocationsXml);
	}

}

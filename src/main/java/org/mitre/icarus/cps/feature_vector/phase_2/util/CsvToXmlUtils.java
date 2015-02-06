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
package org.mitre.icarus.cps.feature_vector.phase_2.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;
import org.mitre.icarus.cps.feature_vector.phase_2.GeoArea;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;
import org.mitre.icarus.cps.feature_vector.phase_2.loader.FeatureVectorLoader;

/**
 * @author CBONACETO
 *
 */
public class CsvToXmlUtils {
	
	/**
	 * 
	 * @param missionId
	 * @param blueLocationsCsvFile
	 * @param outputFolder
	 * @throws IOException
	 * @throws JAXBException 
	 */
	public static void createBlueLocationsXmlFile(String missionId, int locationsPerTrial,
			File blueLocationsCsvFile, File outputFolder) throws IOException, JAXBException {
		FileReader in = null;
		FileWriter out = null;
		try {
			in = new FileReader(blueLocationsCsvFile);		
			Iterable<CSVRecord> parser = CSVFormat.EXCEL.withHeader().parse(in);
			List<BlueLocation> locations = new LinkedList<BlueLocation>();
			int trialNum = 1;
			int locationNum = 1;
			for(CSVRecord record : parser) {
				BlueLocation location = new BlueLocation();
				location.setTrialNumber(trialNum);
				location.setId(trialNum + "-" + locationNum);
				location.setLocation(new GeoCoordinate(
						Double.parseDouble(record.get("x")), 
						Double.parseDouble(record.get("y"))));
				locations.add(location);
				trialNum++;
				if(locationNum == locationsPerTrial) {
					locationNum = 1;
				} else {
					locationNum++;
				}
			}
			String xml = FeatureVectorLoader.getInstance().marshalBlueLocations(
					new FeatureContainer<BlueLocation>(locations));
			out = new FileWriter(new File(outputFolder, missionId + "_Blue_Locations.xml"));
			out.write(xml);
		} catch(IOException e) {
			throw e;
		} finally {
			if(in != null) {
				in.close();
			}
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * 
	 * @param missionId
	 * @param imageName
	 * @param aoiBounds
	 * @param blueRegionCsvFile
	 * @param outputFolder
	 * @throws IOException
	 */
	public static void createAreaOfInterestXmlFile(String missionId, String imageName, 
			GeoArea aoiBounds, File blueRegionCsvFile,	File outputFolder) throws IOException {

	}
	
	public static void main(String[] args) {
		try {
			createBlueLocationsXmlFile("Mission4", 1,
					new File("data/Phase_2_CPD/exams/Sample-Exam-2/new_4_5/Mission4_LatLon.csv"), 
					new File("data/Phase_2_CPD/exams/Sample-Exam-2/new_4_5"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
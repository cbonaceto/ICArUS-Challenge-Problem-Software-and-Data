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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;

import org.mitre.icarus.cps.feature_vector.phase_1.FeatureType;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;

/**
 * Abstract base class for implementations that parse CSV feature vector files for tasks, roads, regions,
 * and distances.
 * 
 * @author Lily Wong
 *
 */
public abstract class FeatureVectorCsvParser {
	
	/** The URL to the csv file. */
	protected URL csvFile;
	
	/** The number of columsn in the CSV file */
	protected int numCols;
	
	/** The feature type. */
	protected FeatureType featureType;	// road, region, task, socint
	
	/** The grid size. */
	protected GridSize gridSize;
	
	/**
	 * Constructor that takes the gridSize.
	 *
	 * @param gridSize the grid size
	 */
	protected FeatureVectorCsvParser( GridSize gridSize ) {
		this.gridSize = gridSize;
	}
	
	/**
	 * Instantiates a new feature vector CSV from a URL with default gridsize.
	 * 
	 * @param url
	 * @throws IOException
	 */
	protected FeatureVectorCsvParser( URL url ) throws IOException {
		this.csvFile = url;
		this.numCols = determineNumCols();
		this.gridSize = new GridSize();
	}
	
	/**
	 * Instantiates a new feature vector CSV from a URL with a GridSize.
	 * 
	 * @param url
	 * @param gridSize
	 * @throws IOException
	 */
	protected FeatureVectorCsvParser ( URL url, GridSize gridSize ) throws IOException {
		this.csvFile = url;
		this.numCols = determineNumCols();
		this.gridSize = gridSize;
	}
	
	/**
	 * Instantiates a new feature vector CSV from a String with default gridsize.
	 *
	 * @param filepath the filepath
	 * @throws IOException
	 */
	protected FeatureVectorCsvParser ( String filepath ) throws IOException {
		this.csvFile = new File( filepath ).toURI().toURL();
		this.numCols = determineNumCols();
		this.gridSize = new GridSize();
	}
	
	/**
	 * Instantiates a new feature vector CSV from a String with GridSize.
	 *
	 * @param filepath the filepath
	 * @param gridSize the grid size
	 * @throws IOException
	 */
	protected FeatureVectorCsvParser ( String filepath, GridSize gridSize ) throws IOException, ParseException {
		this.csvFile = new File ( filepath ).toURI().toURL();
		this.numCols = determineNumCols();
		this.gridSize = gridSize;
	}	

	/**
	 * Checks if the CSV has INT info.
	 *
	 * @return true, if successful
	 */
	public boolean hasInts() {
		if ( numCols > 4 )
			return true;
		return false;
	}

	/**
	 * Sets the path to the CSV file to parse and parse the file.
	 * 
	 * @param filepath
	 * @throws IOException
	 * @throws ParseException
	 */
	public void setCsvFilePath(String filepath) throws IOException, ParseException {
		this.csvFile = new File( filepath ).toURI().toURL();
		parseCsv();
	}

	/**
	 * Sets the URL to the CSV file to parse and parse the file.
	 * 
	 * @param url
	 * @throws IOException
	 * @throws ParseException
	 */
	public void setCsvFileUrl(URL url) throws IOException, ParseException {
		this.csvFile = url;
		parseCsv();
	}
	
	/**
	 * Gets the filename: last portion of the filepath, omitting the extension.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		if(csvFile != null && csvFile.getFile() != null) {
			return csvFile.getFile().substring(0, csvFile.getFile().indexOf('.'));
		}	
		return null;
	}
	
	/**
	 * Gets the feature type.
	 *
	 * @return the feature type
	 */
	public FeatureType getFeatureType() { return featureType; }

	public GridSize getGridSize() {
		return gridSize;
	}

	/**
	 * Determine number of columns in the CSV file.
	 *
	 * @return the int
	 * @throws IOException
	 */
	private int determineNumCols() throws IOException {
		Scanner scan;		
		scan = new Scanner( csvFile.openStream() );
		try {
			if ( scan.hasNextLine() ) {
				String[] tokens = scan.nextLine().split(",");
				return tokens.length;
			}
		} catch(Exception ex) {} 
		finally {
			if(scan != null) {scan.close();}
		}
		return -1;
	}

	/**
	 * Determine the feature type.
	 *
	 * @param type String representation of the type
	 */
	protected void determineFeatureType(String type) {
		try {
			featureType = FeatureType.valueOf(type);
		} catch (IllegalArgumentException e ) {
			featureType = FeatureType.Task;
		}
	}
	
	/**
	 * Parses the CSV file.
	 *
	 * @throws IOException
	 * @throws ParseException
	 */
	public abstract void parseCsv() throws IOException, ParseException;
	
	/**
	 * Converts the CSV representation to KML .
	 *
	 * @return the feature vector kml representation
	 */
	public abstract FeatureVectorKmlParser toKml();	
}
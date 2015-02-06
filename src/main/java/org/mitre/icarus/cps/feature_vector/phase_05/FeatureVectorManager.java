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
package org.mitre.icarus.cps.feature_vector.phase_05;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;

import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVector;
import org.mitre.icarus.cps.feature_vector.HashMapCache;

/**
 * Manages the caching and loading of feature vectors and object palettes. 
 * 
 * @author CBONACETO
 *
 */
public class FeatureVectorManager {

	/** Cached object palettes.   Key for outer map is the URL to the object palette, 
	 * key for inner map(s) is the object ID for an object in that palette. */
	protected HashMapCache<String, Map<String, ParsedPaletteObject>> objectPalettes;
	
	/** Cached feature vectors.  Key is URL to the feature vector file */
	protected HashMapCache<String, FeatureVector> featureVectors;
	
	/** Cached single sector feature vectors.  Used for annotation grid training.
	 * Key is URL_SectorId */
	protected HashMapCache<String, FeatureVector> singleSectorFeatureVectors;
	
	/** Singleton instance of the feature vector manager */
	private static final FeatureVectorManager instance = new FeatureVectorManager();
	
	/** Feature vector parser */
	private static final FeatureVectorParser parser = new FeatureVectorParser();
	
	private FeatureVectorManager() {
		objectPalettes = new HashMapCache<String, Map<String, ParsedPaletteObject>>(2, 20);
		featureVectors = new HashMapCache<String, FeatureVector>(20, 100);		
		singleSectorFeatureVectors = new HashMapCache<String, FeatureVector>(20, 100);
	}

	public static FeatureVectorManager getInstance() {
		return instance;
	}

	
	/**
	 * Get a feature vector from the given relative URL to the feature vector file and object palette
	 * file, and the base URL to resolve the relative URLs.
	 * 
	 * @param featureVectorUrl
	 * @param objectPaletteUrl
	 * @param baseUrl
	 * @return the feature vector
	 * @throws IOException
	 * @throws ParseException
	 */
	public FeatureVector getFeatureVector(String featureVectorUrl, String objectPaletteUrl, URL baseUrl) throws IOException, ParseException {

		URL featureFile = new URL(baseUrl, featureVectorUrl);
		URL objectFile = new URL(baseUrl, objectPaletteUrl);		

		return getFeatureVector(featureFile, objectFile);
	}
	
	
	/**
	 * Get a feature vector from the given absolute URL to the feature vector file and object palette
	 * file.
	 * 
	 * @param featureVectorUrl
	 * @param objectPaletteUrl
	 * @return the feature vector
	 * @throws IOException
	 * @throws ParseException
	 */
	public FeatureVector getFeatureVector(URL featureVectorUrl, URL objectPaletteUrl) throws IOException, ParseException {
		
		FeatureVector featureVector = featureVectors.get(featureVectorUrl.toString());		
		if(featureVector == null || !featureVector.getObjectPalette().equals(objectPaletteUrl)) {
			//Load and cache the feature vector
			featureVector = parser.parseFeatureVector(featureVectorUrl, objectPaletteUrl.toString(), 
					getObjectPalette(objectPaletteUrl));
			featureVectors.put(featureVectorUrl.toString(), featureVector);
		}
		return featureVector;	
	}
	
	/**
	 * Get a feature vector containing a single sector with the given sectorId
	 * from the given relative URL to the feature vector file and object palette
	 * file, and the base URL to resolve the relative URLs.
	 * 
	 * @param featureVectorUrl
	 * @param objectPaletteUrl
	 * @param baseUrl
	 * @param sectorId
	 * @return the feature vector
	 * @throws IOException
	 * @throws ParseException
	 */
	public FeatureVector getSingleSectorFeatureVector(String featureVectorUrl, String objectPaletteUrl, URL baseUrl,
			Integer sectorId) throws IOException, ParseException {

		URL featureFile = new URL(baseUrl, featureVectorUrl);
		URL objectFile = new URL(baseUrl, objectPaletteUrl);		

		return getSingleSectorFeatureVector(featureFile, objectFile, sectorId);
	}
	
	
	/**
	 * Get a feature vector containing a single sector with the given sectorId
	 * from the given absolute URL to the feature vector file and object palette
	 * file.
	 * 
	 * @param featureVectorUrl
	 * @param objectPaletteUrl
	 * @param sectorId
	 * @return the feature vector
	 * @throws IOException
	 * @throws ParseException
	 */
	public FeatureVector getSingleSectorFeatureVector(URL featureVectorUrl, URL objectPaletteUrl, 
			Integer sectorId) throws IOException, ParseException {
		
		FeatureVector featureVector = singleSectorFeatureVectors.get(featureVectorUrl.toString() + "_" + sectorId.toString());		
		if(featureVector == null || !featureVector.getObjectPalette().equals(objectPaletteUrl)) {
			//Load and cache the single sector feature vector
			featureVector = parser.parseSingleSectorFeatureVector(featureVectorUrl, objectPaletteUrl.toString(), 
					getObjectPalette(objectPaletteUrl), sectorId);
			singleSectorFeatureVectors.put(featureVectorUrl.toString() + "_" + sectorId.toString(), featureVector);
		}
		return featureVector;
	}
	
	/** 
	 * Return whether the given feature vector contains a single sector with the given sectorId. 
	 */
	protected boolean containsSingleSector(FeatureVector world, Integer sectorId) {
		return (world != null && world.getSectorLayer() != null && 
				world.getSectorLayer().getNumSectors() == 1 &&
				world.getSectorLayer().containsSector(sectorId));
	}
	
	
	/**
	 * Get a map of ID's to objects from the given relative URL to an object palette file
	 * and the base URL used to resolve the relative URL.
	 * 
	 * @param objectPaletteUrl
	 * @param baseUrl
	 * @return 
	 * @throws IOException
	 * @throws ParseException
	 */
	public Map<String, ParsedPaletteObject> getObjectPalette(String objectPaletteUrl, URL baseUrl) throws IOException, ParseException {
		return getObjectPalette(new URL(baseUrl, objectPaletteUrl));
	}
	
	
	/**
	 * Get a map of ID's to objects from the given absolute URL to an object palette file.
	 * 
	 * @param objectPaletteUrl
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public Map<String, ParsedPaletteObject> getObjectPalette(URL objectPaletteUrl) throws IOException, ParseException {
		
		Map<String, ParsedPaletteObject> objects = objectPalettes.get(objectPaletteUrl.toString());		
		if(objects == null) {
			//Load and cache the object palette
			objects = parser.parseObjectPalette(objectPaletteUrl);
			objectPalettes.put(objectPaletteUrl.toString(), objects);
		}
		return objects;
	}	
	
	
	/**
	 * Get a single object from an object palette file specified by the given relative URL 
	 * to an object palette file and the base URL used to resolve the relative URL.
	 * 
	 * @param objectPaletteUrl
	 * @param baseUrl
	 * @param objectId
	 * @param objectOrientation
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public ParsedPaletteObject getObject(String objectPaletteUrl, URL baseUrl,
			int objectId, 
			int objectOrientation) throws IOException, ParseException {
				
		return getObject(new URL(baseUrl, objectPaletteUrl), objectId, objectOrientation);
	}
	
	
	/**
	 * Get a single object from an object palette specified from the given absolute URL
	 * to an object palette file.
	 * 
	 * @param objectPaletteUrl
	 * @param objectId
	 * @param objectOrientation
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public ParsedPaletteObject getObject(URL objectPaletteUrl, 
			int objectId,
			int objectOrientation) throws IOException, ParseException {
		
		Map<String, ParsedPaletteObject> objects = getObjectPalette(objectPaletteUrl);
		if(objects != null) {
			return objects.get(parser.createObjectKey(objectId, objectOrientation));
		}		
		return null;
	}
}

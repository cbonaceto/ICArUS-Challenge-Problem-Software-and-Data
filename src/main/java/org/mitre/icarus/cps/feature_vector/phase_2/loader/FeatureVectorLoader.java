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
package org.mitre.icarus.cps.feature_vector.phase_2.loader;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.feature_vector.HashMapCache;
import org.mitre.icarus.cps.feature_vector.phase_2.AreaOfInterest;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.Region;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureVectorFileDescriptor;
import org.mitre.icarus.cps.feature_vector.phase_2.IFeature;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

import de.micromata.opengis.kml.v_2_2_0.AbstractObject;

/**
 * Manages the loading and caching of Phase 2 feature vector files.
 * 
 * @author CBONACETO
 *
 */
public class FeatureVectorLoader {
	
	/** Singleton instance of the feature vector loader */
	private static final FeatureVectorLoader instance = new FeatureVectorLoader();	
	
	/** Whether caching is enabled */
	private boolean cachingEnabled = false;
	
	/** Area of interest cache */
	private HashMapCache<String, AreaOfInterest> aoiCache;
	
	/** Blue locations cache */
	private HashMapCache<String, FeatureContainer<BlueLocation>> locationsCache;
	
	/**
	 * Construct a FeatureVectorLoader. Private constructor as this class is a singleton.
	 */
	private FeatureVectorLoader() {
		aoiCache = new HashMapCache<String, AreaOfInterest>(6, 12);
		locationsCache = new HashMapCache<String, FeatureContainer<BlueLocation>>(6, 12);
	}

	/**
	 * Get the singleton instance of the feature vector loader.
	 * 
	 * @return the feature vector loader
	 */
	public static FeatureVectorLoader getInstance() {
		return instance;
	}

	/**
	 * Get whether caching is enabled. When enabled, feature vector objects will be cached by the URL of the file
	 * containing the objects. The objects will only be loaded again from files if they aren't in the cache.
	 * 
	 * @return whether caching is enabled
	 */
	public boolean isCachingEnabled() {
		return cachingEnabled;
	}

	/**
	 * Set whether caching is enabled. When enabled, feature vector objects will be cached by the URL of the file
	 * containing the objects. The objects will only be loaded again from files if they aren't in the cache.
	 * 
	 * @param cachingEnabled whether caching is enabled
	 */
	public void setCachingEnabled(boolean cachingEnabled) {
		if(cachingEnabled != this.cachingEnabled) {
			this.cachingEnabled = cachingEnabled;
			if(!cachingEnabled) {
				aoiCache.clear();
			}
		}
	}
	
	/**
	 * Marshals an AreaOfInterest to XML.
	 * 
	 * @param aoi an area of interest
	 * @return XML string representation of area of interest
	 * @throws JAXBException
	 */
	public String marshalAreaOfInterest(AreaOfInterest aoi) throws JAXBException {
		Marshaller marshaller = IcarusExamLoader.createMarshallerForClass(aoi.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(aoi, output);
		
		return output.toString();
	}
	
	/** Unmarshals an AreaOfInterest from an XML file. */
	/**
	 * @param aoiFile URL of the file containing the area of interest
	 * @param validate
	 * @return
	 * @throws JAXBException
	 */
	public AreaOfInterest unmarshalAreaOfInterest(URL aoiFile, boolean validate) throws JAXBException {
		AreaOfInterest aoi = null;
		if(cachingEnabled) {
			aoi = aoiCache.get(aoiFile.toString());
		}		
		
		if(aoi == null) {
			Unmarshaller unmarshaller = IcarusExamLoader.createUnmarshallerForClass(
					AreaOfInterest.class, IcarusExamLoader_Phase2.ExamSchema, validate);
			aoi = (AreaOfInterest)(unmarshaller.unmarshal(aoiFile));
			if(cachingEnabled && aoi != null) {
				aoiCache.put(aoiFile.toString(), aoi);
			}
			if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
				throw new JAXBException(unmarshaller.getEventHandler().toString());
			}
		}	
	
		return aoi;
	}	
	
	/**
	 * Marshals a set of Blue locations to XML.
	 * 
	 * @param locations a set of Blue locations
	 * @return XML string representation of the locations
	 * @throws JAXBException
	 */
	public String marshalBlueLocations(FeatureContainer<BlueLocation> locations) throws JAXBException {
		Marshaller marshaller = IcarusExamLoader.createMarshallerForClass(locations.getClass());
		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(locations, output);
		
		return output.toString();
	}
	
	/**
	 * Unmarshals a set of Blue locations from an XML file.
	 * 
	 * @param locationsFile URL of the file containing the Blue locations
	 * @param validate whether to validate the file against the schema
	 * @return a FeatureContainer contining the locations
	 * @throws JAXBException
	 */	
	public FeatureContainer<BlueLocation> unmarshalBlueLocations(URL locationsFile, boolean validate) throws JAXBException {
		return this.unmarshalBlueLocations(locationsFile, validate, null, null);
	}
	
	/**
	 * Unmarshals a set of Blue locations from an XML file. Also sets the IMINT radius and SIGINT radius
	 * for locations that contain IMINT and/or SIGINT data.
	 * 
	 * @param locationsFile URL of the file containing the Blue locations
	 * @param validate whether to validate the file against the schema
	 * @param imintRadius_miles the default IMINT radius (miles)
	 * @param sigintRadius_miles the default SIGINT radius (miles)
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public FeatureContainer<BlueLocation> unmarshalBlueLocations(URL locationsFile, boolean validate, 
			Double imintRadius_miles, Double sigintRadius_miles) throws JAXBException {
		FeatureContainer<BlueLocation> locations = null;
		if(cachingEnabled) {
			locations = locationsCache.get(locationsFile.toString());
		}		
		
		if(locations == null) {
			Unmarshaller unmarshaller = IcarusExamLoader.createUnmarshallerForClass(
					FeatureContainer.class, IcarusExamLoader_Phase2.ExamSchema, validate);			
			locations = (FeatureContainer<BlueLocation>)(unmarshaller.unmarshal(locationsFile));
			if(cachingEnabled && locations != null) {
				locationsCache.put(locationsFile.toString(), locations);
			}
			if(locations != null) {
				initializeIntRadiiForLocations(locations, imintRadius_miles, sigintRadius_miles);
			}
			if(validate && unmarshaller.getEventHandler() != null && !unmarshaller.getEventHandler().toString().isEmpty()) {
				throw new JAXBException(unmarshaller.getEventHandler().toString());
			}
		}	
		
		return locations;
	}
	
	/**
	 * Initializes the default IMINT and SIGINT radiuses for a set of Blue locations.
	 * 
	 * @param locations a set of Blue locations
	 * @param imintRadius_miles the default IMINT radius (miles)
	 * @param sigintRadius_miles the default SIGINT radius (miles)
	 */
	protected void initializeIntRadiiForLocations(FeatureContainer<BlueLocation> locations, Double imintRadius_miles, 
			Double sigintRadius_miles) {
		if(locations != null && !locations.isEmpty() && (imintRadius_miles != null || sigintRadius_miles != null)) {
			for(BlueLocation location : locations) {
				
				if(imintRadius_miles != null && location.getImint() != null && 
						location.getImint().getRadius_miles() == null) {
					location.getImint().setRadius_miles(imintRadius_miles);
				}
				if(sigintRadius_miles != null && location.getSigint() != null && 
						location.getSigint().getRadius_miles() == null) {
					location.getSigint().setRadius_miles(sigintRadius_miles);
				}
			}
		}
	}
	
	/**
	 * Marshals a feature object to its KML representation.
	 * 
	 * @param feature a feature object
	 * @return the KML representationof the feature object
	 * @throws JAXBException
	 */
	public String marshalFeatureToKml(IFeature<?> feature) throws JAXBException {
		AbstractObject kmlGeometry = feature.getKMLGeometry();
		if(kmlGeometry != null) {
			//System.out.println("kmlGeo class "+ kmlGeometry.getClass());
			Marshaller marshaller = IcarusExamLoader.createMarshallerForClass(AbstractObject.class);

			ByteArrayOutputStream output = new ByteArrayOutputStream (); 
			marshaller.marshal(kmlGeometry, output);
			return output.toString();
		}		
		return null;
	}
	
	/** Test main */
	public static void main(String[] args) {
		try {
			//Create a sample area of interest
			AreaOfInterest aoi = new AreaOfInterest(null, -71.0717455140345,
					42.4088540713865, -71.0630111562008, 42.4024047168992);
			aoi.setSceneImageFile(new FeatureVectorFileDescriptor("Mission_1.png"));
			Region region = new Region();
			aoi.setBlueRegion(region);
			ArrayList<GeoCoordinate> vertices = new ArrayList<GeoCoordinate>();
			region.setVertices(vertices);
			vertices.add(new GeoCoordinate(-71.06565812028882, 42.40872637045506)); 
			vertices.add(new GeoCoordinate(-71.0689793202888, 42.40872637045506));
			vertices.add(new GeoCoordinate(-71.07051218182727, 42.40661167015056));
			vertices.add(new GeoCoordinate(-71.068432, 42.405047));
			vertices.add(new GeoCoordinate(-71.068434, 42.403994));
			vertices.add(new GeoCoordinate(-71.064247, 42.403769));
			vertices.add(new GeoCoordinate(-71.0642893, 42.4051317));			
			vertices.add(new GeoCoordinate(-71.0646541, 42.4051297));
			vertices.add(new GeoCoordinate(-71.064824, 42.405823));
			vertices.add(new GeoCoordinate(-71.065689, 42.405804));
			vertices.add(new GeoCoordinate(-71.06565812028882, 42.40872637045506));			
			System.out.println(FeatureVectorLoader.getInstance().marshalAreaOfInterest(aoi));

			/*//Load an area of interest file
			AreaOfInterest aoi = getInstance().unmarshalAreaOfInterest(
					new File("data/Phase_2_CPD/exams/Sample_Exam_1/Mission1_Area_Of_Interest.xml").toURI().toURL(), false);
			System.out.println(getInstance().marshalAreaOfInterest(aoi));
			System.out.println(getInstance().marshalFeatureToKml(aoi.getBlueRegion()));

			//Load a Blue locations file
			FeatureContainer<BlueLocation> locations = getInstance().unmarshalBlueLocations(
					new File("data/Phase_2_CPD/exams/Sample_Exam_1/Mission1_Blue_Locations.xml").toURI().toURL(), false);
			System.out.println(getInstance().marshalBlueLocations(locations));*/
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
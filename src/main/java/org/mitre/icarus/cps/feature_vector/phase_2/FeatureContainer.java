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
package org.mitre.icarus.cps.feature_vector.phase_2;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;

/**
 * Container for lists of Phase 2 features, including: 
 * Road, Building, Terrain, Blue Region and Locations.
 * 
 * @author LWONG
 *
 * @param <T> The feature type
 */
@XmlRootElement(name="FeatureContainer", namespace="IcarusCPD_2")
@XmlType(name="FeatureContainer", namespace="IcarusCPD_2")
public class FeatureContainer<T extends AbstractFeature<? extends Geometry>> implements Iterable<T> {

	/** The list of features */
	private List<T> featureList;

	/**
	 * Construct an empty FeatureContainer.
	 */
	public FeatureContainer() {}
	
	/**
	 * Construct a FeatureContainer with the given list of features.
	 * 
	 * @param featureList the list of features
	 */
	public FeatureContainer(List<T> featureList) {
		this.featureList = featureList;
	}

	/**
	 * Get the list of features in the container.
	 * 
	 * @return the list of features
	 */
	@XmlElements({
		@XmlElement(name="Road", type=Road.class, namespace="IcarusCPD_2"),
		@XmlElement(name="Building", type=Building.class, namespace="IcarusCPD_2"),
		@XmlElement(name="Terrain", type=Terrain.class, namespace="IcarusCPD_2"),
		@XmlElement(name="Region", type=Region.class, namespace="IcarusCPD_2"),
		@XmlElement(name="BlueLocation", type=BlueLocation.class, namespace="IcarusCPD_2")
	})
	public List<T> getFeatureList() {
		return featureList;
	}

	/**
	 * Set the list of features in the container.
	 * 
	 * @param featureList the list of features
	 */
	public void setFeatureList(List<T> featureList) {
		this.featureList = featureList;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return featureList != null ? featureList.iterator() : null;
	}
	
	/**
	 * Get the number of features in the feature container.
	 * 
	 * @return the number of features in the feature container
	 */
	public Integer size() {
		return featureList != null ? featureList.size() : 0;
	}
	
	/**
	 * Get whether the feature container is empty.
	 * 
	 * @return whether the feature container is empty
	 */
	public boolean isEmpty() {
		return featureList != null ? featureList.isEmpty() : true;
	}
	
	/**
	 * Get a KML doucment containing the objects in this feature container.
	 * 
	 * @return a KML document
	 */
	public Kml getKMLRepresentation() {
		final Kml kml = new Kml();
		//TODO: Shouldn't assume this is a BlueLocation feature container
		Document document = kml.createAndSetDocument();
		for(T feature : featureList) {
			document.addToFeature(((BlueLocation) feature).getKMLRepresentation());
		}
		return kml;
	}
}
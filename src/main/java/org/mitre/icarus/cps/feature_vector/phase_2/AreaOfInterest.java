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

import java.awt.Image;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.GroundOverlay;
import de.micromata.opengis.kml.v_2_2_0.Kml;


/**
 * 
 * The area of interest for a trial or a mission. Contains geographic region bounding coordinates, the geo-referenced scene image,
 * the Blue region, the road network, the buildings, and the terrain in the area. Currently, only the region coordinates,
 * scene image, and Blue region are used. 
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="AreaOfInterest", namespace="IcarusCPD_2")
@XmlType(name="AreaOfInterest",  namespace="IcarusCPD_2", propOrder={"sceneImageFile", "blueRegion", 
		"roads", "buildings", "terrain", "imintRadius_miles", "sigintRadius_miles"})
public class AreaOfInterest extends GeoArea {
	
	/** The scene image file */
	protected FeatureVectorFileDescriptor sceneImageFile;
	
	/** The scene image */
	protected Image sceneImage;
	
	/** The blue region */
	protected Region blueRegion;
	
	/** The road network in the area (loaded from the road network file) */
	protected FeatureContainer<Road> roads; 
	
	/** The buildings in the area (loaded from the buildings file)*/
	protected FeatureContainer<Building> buildings;
	
	/** The terrain in the area (loaded from the terrain file) */
	protected FeatureContainer<Terrain> terrain;	
	
	/** The IMINT radius used to compute density in the area of interest (miles) */
	protected Double imintRadius_miles;
	
	/** The SIGINT radius in the area of interest (miles) */
	protected Double sigintRadius_miles;

	/**
	 * Construct an empty area of interest.
	 */
	public AreaOfInterest() {}
	
	/**
	 * Construct an AreaOfInterest with the given scene image and bounding coordinates.
	 * 
	 * @param sceneImage The scene image file. The scene image is a geo-referenced image aligned to the area bounds.
	 * @param topLeftLon West coordinate (the top left longitude of the area)
	 * @param topLeftLat North coordinate (the top left latitude of the area)
	 * @param bottomRightLon East coordinate (the bottom right longitude of the area)
	 * @param bottomRightLat South coordinate (the bottom right latitude of the area)
	 */
	public AreaOfInterest(Image sceneImage, Double topLeftLon, Double topLeftLat, Double bottomRightLon, Double bottomRightLat) {
		super(topLeftLon, topLeftLat, bottomRightLon, bottomRightLat);
		this.sceneImage = sceneImage;
	}

	/**
	 * Get the scene image file. The scene image is a geo-referenced image aligned to the area bounds.
	 * 
	 * @return the scene image file
	 */
	@XmlElement(name="SceneImageFile")
	public FeatureVectorFileDescriptor getSceneImageFile() {
		return sceneImageFile;
	}

	/**
	 * Set the scene image file. The scene image is a geo-referenced image aligned to the area bounds.
	 * 
	 * @param sceneImageFile the scene image file
	 */
	public void setSceneImageFile(FeatureVectorFileDescriptor sceneImageFile) {
		this.sceneImageFile = sceneImageFile;
	}

	/**
	 * Get the scene image. The scene image is a geo-referenced image aligned to the area bounds.
	 * 
	 * @return the scene image
	 */
	@XmlTransient
	public Image getSceneImage() {
		return sceneImage;
	}

	/**
	 * Set the scene image. The scene image is a geo-referenced image aligned to the area bounds.
	 * 
	 * @param sceneImage the scene image
	 */
	public void setSceneImage(Image sceneImage) {
		this.sceneImage = sceneImage;
	}

	/**
	 * Get the Blue region.
	 * 
	 * @return the Blue region
	 */
	@XmlElement(name="BlueRegion")
	public Region getBlueRegion() {
		return blueRegion;
	}

	/**
	 * Set the Blue region.
	 * 
	 * @param blueRegion the Blue region
	 */
	public void setBlueRegion(Region blueRegion) {
		this.blueRegion = blueRegion;
	}

	/**
	 * Get the roads. Not currently used.
	 * 
	 * @return the roads
	 */
	@XmlElement(name="Roads")
	public FeatureContainer<Road> getRoads() {
		return roads;
	}

	/**
	 * Set the roads. Not currently used.
	 * 
	 * @param roads the roads
	 */
	public void setRoads(FeatureContainer<Road> roads) {
		this.roads = roads;
	}

	/**
	 * Get the buildings. Not currently used.
	 * 
	 * @return the buildings
	 */
	@XmlElement(name="Buildings")
	public FeatureContainer<Building> getBuildings() {
		return buildings;
	}

	/**
	 * Set the buildings. Not currently used.
	 * 
	 * @param buildings the buildings
	 */
	public void setBuildings(FeatureContainer<Building> buildings) {
		this.buildings = buildings;
	}

	/**
	 * Get the terrain features. Not currently used.
	 * 
	 * @return the terrain features
	 */
	@XmlElement(name="Terrain")
	public FeatureContainer<Terrain> getTerrain() {
		return terrain;
	}

	/**
	 * Set the terrain features. Not currently used.
	 * 
	 * @param terrain the terrain features
	 */
	public void setTerrain(FeatureContainer<Terrain> terrain) {
		this.terrain = terrain;
	}	

	/**
	 * Get the default IMINT radius for the area (miles). This is the radius around a location
	 * over which building density is calculation.
	 * 
	 * @return the default IMINT radius for the area (miles)
	 */
	@XmlAttribute(name="imintRadius_miles")
	public Double getImintRadius_miles() {
		return imintRadius_miles;
	}

	/**
	 * Set the default IMINT radius for the area (miles). This is the radius around a location
	 * over which building density is calculation.
	 * 
	 * @param imintRadius_miles the default IMINT radius for the area (miles)
	 */
	public void setImintRadius_miles(Double imintRadius_miles) {
		this.imintRadius_miles = imintRadius_miles;
	}

	/**
	 * Get the default SIGINT radius for the area (miles). This is the radius around a location
	 * over which SIGINT is detected.
	 * 
	 * @return the default SIGINT radius for the area (miles)
	 */
	@XmlAttribute(name="sigintRadius_miles")
	public Double getSigintRadius_miles() {
		return sigintRadius_miles;
	}

	/**
	 * Set the default SIGINT radius for the area (miles). This is the radius around a location
	 * over which SIGINT is detected.
	 * 
	 * @param sigintRadius_miles the default SIGINT radius for the area (miles)
	 */
	public void setSigintRadius_miles(Double sigintRadius_miles) {
		this.sigintRadius_miles = sigintRadius_miles;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_2.GeoArea#getKMLGeometry()
	 */
	@Override
	public GroundOverlay getKMLGeometry() {
		GroundOverlay groundOverlay = super.getKMLGeometry();
		groundOverlay.setIcon(
				new de.micromata.opengis.kml.v_2_2_0.Icon()
				.withHref(sceneImageFile.getFileUrl()));
		return groundOverlay;
	}
	
	/**
	 * Get a KML document containing this area of interest.
	 *  
	 * @return a KML document
	 */
	public Kml getKMLRepresentation() {
		final Kml kml = new Kml();
		Document document = kml.createAndSetDocument();
		document.addToFeature(getKMLGeometry());
		if (blueRegion != null)
			document.addToFeature(blueRegion.getKMLRepresentation());
		return kml;
	}
}
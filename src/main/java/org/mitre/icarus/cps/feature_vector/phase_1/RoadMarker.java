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
package org.mitre.icarus.cps.feature_vector.phase_1;

import java.util.HashMap;


/**
 * Contains information about a road: label and INTs.
 * 
 * @author Lily Wong
 *
 */
public class RoadMarker extends Phase1Feature {
	
	/** The road id. */
	private String roadId;	
	
	/** The Constant icons. */
	@SuppressWarnings({ "serial" })
	private static final HashMap<String,String> icons = new HashMap<String,String>() {
		{
			put( "1", "#1" );
			put( "2", "#2" );
			put( "3", "#3" );
			put( "4", "#4" );
			
			put( ImintType.Government.toString(), 		"#Government" );
			put( ImintType.Military.toString(), 		"#Military" );
			put( MovintType.SparseTraffic.toString(), 	"#SparseTraffic" );
			put( MovintType.DenseTraffic.toString(), 	"#DenseTraffic" );
		}
	};
	
	/**
	 * Instantiates a new road marker.
	 *
	 * @param roadId the road id
	 * @param markerLocation the marker location
	 * @param locationIntel the location intel
	 */
	public RoadMarker(String roadId, GridLocation2D markerLocation,
			LocationIntelReport locationIntel) {
		super( roadId, markerLocation, locationIntel );
		this.roadId = roadId;
	}

	/**
	 * Gets the road id.
	 *
	 * @return the road id
	 */
	public String getRoadId() {
		return roadId;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoadMarker [roadId=" + roadId + ", location=" + location
				+ ", intelReport=" + intelReport + "]";
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.feature_vector.phase_1.Phase1Feature#getIcons()
	 */
	@Override
	public HashMap<String, String> getIcons() {
		return icons;
	}
}

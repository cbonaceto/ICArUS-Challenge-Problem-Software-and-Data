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
package org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation;

import javax.xml.bind.annotation.XmlType;

/**
 * An facility scene item.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="FacilitySceneItem", namespace="IcarusCPD_05")
public class FacilitySceneItem extends SceneItem {	
	
	public FacilitySceneItem() {}
	
	public FacilitySceneItem(String facilityName, int facilityId) {
		super(facilityName, facilityId);
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.testing.scene_presentation.SceneItem#getItemType()
	 */
	@Override
	public SceneItemType getItemType() {
		return SceneItemType.Facility;
	}	
}

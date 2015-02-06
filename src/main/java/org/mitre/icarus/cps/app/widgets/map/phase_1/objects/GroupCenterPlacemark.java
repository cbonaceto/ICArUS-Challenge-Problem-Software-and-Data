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
package org.mitre.icarus.cps.app.widgets.map.phase_1.objects;

import java.awt.Color;
import java.awt.Image;

import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;

/**
 * @author CBONACETO
 *
 */
public class GroupCenterPlacemark extends Phase1FeaturePlacemark<GroupCenter> {	
	
	public GroupCenterPlacemark(GroupCenter groupCenter) {
		this(groupCenter, null);
	}
	
	public GroupCenterPlacemark(GroupCenter groupCenter, Image groupIcon) {
		super(groupCenter);
		name = groupCenter.getGroup().toString();
		id = name;
		showName = (groupIcon == null);
		markerIcon = groupIcon;
		markerShape = PlacemarkShape.Circle;
		foregroundColor = Color.WHITE;
		backgroundColor = ColorManager_Phase1.getGroupCenterColor(groupCenter.getGroup());
	}

	public long getEditTime() {
		return editTime;
	}

	public void setEditTime(long editTime) {
		this.editTime = editTime;
	}
}
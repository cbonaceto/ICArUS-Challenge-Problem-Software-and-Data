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

import javax.xml.bind.annotation.XmlType;

/**
 * The Enum GroupType.
 */
@XmlType(name="GroupType", namespace="IcarusCPD_1")
public enum GroupType {	
	A,
	B,
	C,
	D,
	X,
	O,
	Unknown;
	
	public static enum GroupName {Aqua, Bromine, Citrine, Diamond, X, O, Unknown};
	
	public String getGroupNameFull() {
		return GroupName.values()[this.ordinal()].toString();
	}
	
	public String getGroupNameAbbreviated() {
		return this.toString();
	}	
	
	@Override
	public String toString() {
		if(this == Unknown) {
			return "?";
		}
		return super.toString();
	}

	public static String createGroupListString() {
		StringBuilder sb = new StringBuilder("[");
		int numGroups = GroupType.values().length;
		int i = 0;
		for(GroupType group : GroupType.values()) {
			sb.append(group.toString());
			if(i < numGroups - 1) {
				sb.append(", ");
			}
			i++;
		}
		sb.append("]");
		return sb.toString();
	}
}
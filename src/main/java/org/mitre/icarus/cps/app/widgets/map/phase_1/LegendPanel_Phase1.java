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
package org.mitre.icarus.cps.app.widgets.map.phase_1;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JFrame;

import org.mitre.icarus.cps.app.widgets.map.LegendPanel;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.ImintType;
import org.mitre.icarus.cps.feature_vector.phase_1.MovintType;
import org.mitre.icarus.cps.feature_vector.phase_1.SigintType;

/**
 * A specialized legend panel for the Phase 1 Challenge Problem format.
 * 
 * @author CBONACETO
 *
 */
public class LegendPanel_Phase1 extends LegendPanel {

	private static final long serialVersionUID = 8659901057219150494L;
	
	/** The SIGACTs legend item */
	protected ParentLegendItem sigactsItem;
	
	/** The Roads legend item */
	protected ParentLegendItem roadsItem;
	
	/** The Group Centers legend item */
	protected ParentLegendItem groupCentersItem;
	
	/** The IMINT legend item */
	protected ParentLegendItem imintItem;
	
	/** The MOVINT legend item */
	protected ParentLegendItem movintItem;
	
	/** The SIGINT legend item */
	protected ParentLegendItem sigintItem;
	
	/** The SOCINT legend item */
	protected ParentLegendItem socintItem;

	public LegendPanel_Phase1() {
		sigactsItem = new ParentLegendItem("SIGACT Locations", null);
		sigactsItem.setVisible(false);
		
		roadsItem = new ParentLegendItem("Roads", ImageManager_Phase1.getImageIcon(ImageManager_Phase1.ROADS_LAYER_ICON));
		roadsItem.setVisible(false);
		
		//groupCentersItem = new ParentLegendItem("Group Centers", null);
		groupCentersItem = new ParentLegendItem("Centers", null);
		groupCentersItem.setVisible(false);
		
		imintItem = new ParentLegendItem("IMINT Types", null);		
		imintItem.setChildren(new ArrayList<LegendItem>(2));
		imintItem.getChildren().add(new LegendItem("Government Facility", 
				ImageManager_Phase1.getImintImageIcon(ImintType.Government)));
		imintItem.getChildren().add(new LegendItem("Military Facility", 
				ImageManager_Phase1.getImintImageIcon(ImintType.Military)));		
		imintItem.setVisible(false);
		
		movintItem = new ParentLegendItem("MOVINT Types", null);
		movintItem.setChildren(new ArrayList<LegendItem>(2));
		movintItem.getChildren().add(new LegendItem("Sparse Traffic", 
				ImageManager_Phase1.getMovintImageIcon(MovintType.SparseTraffic)));
		movintItem.getChildren().add(new LegendItem("Dense Traffic", 
				ImageManager_Phase1.getMovintImageIcon(MovintType.DenseTraffic)));
		movintItem.setVisible(false);
		
		sigintItem = new ParentLegendItem("SIGINT Types", null);
		sigintItem.setChildren(new ArrayList<LegendItem>(2));
		sigintItem.getChildren().add(new LegendItem("Silence", 
				ImageManager_Phase1.getSigintImageIcon(SigintType.Silent)));
		sigintItem.getChildren().add(new LegendItem("Chatter", 
				ImageManager_Phase1.getSigintImageIcon(SigintType.Chatter)));
		sigintItem.setVisible(false);
		
		socintItem = new ParentLegendItem("SOCINT Regions", null);
		socintItem.setVisible(false);		
	}
	
	public void setSigactsItemVisible(boolean visible) {		
		if(visible != sigactsItem.isVisible()) {		
			if(visible) {
				addLegendItem(sigactsItem);
			}
			else {
				removeLegendItem(sigactsItem);
			}
		}
	}
	
	public void setSigactsItemName(String name) {
		sigactsItem.setName(name);
		legendTree.repaint();
	}
	
	public void setSigactLocations(Collection<GroupAttack> locations) {
		if(locations != null && !locations.isEmpty()) {
			sigactsItem.setChildren(new ArrayList<LegendItem>(locations.size()));
			for(GroupAttack location : locations) {
				if(location.getGroup() == GroupType.Unknown) {
					sigactsItem.getChildren().add(new LegendItem("Unknown Attack",
							ImageManager_Phase1.getSigactLocationImageIcon(location.getGroup())));
				}
				else if(location.getGroup() != null) {
					sigactsItem.getChildren().add(new LegendItem(
							"<html><u>" + location.getGroup().getGroupNameAbbreviated() + "</u>" + 
							location.getGroup().getGroupNameFull().substring(1) + " Attack</html>",
							ImageManager_Phase1.getSigactLocationImageIcon(location.getGroup())));
				}
				else if(location.getLocation() != null && location.getLocation().getLocationId() != null) {
					String locationName = null;
					if(location.getLocation().getLocationId().equals("?")) {
						locationName = "Unknown Attack";
					}
					else {
						locationName = "Location " + location.getLocation().getLocationId();
					}
					sigactsItem.getChildren().add(new LegendItem(locationName,
							ImageManager_Phase1.getSigactLocationImageIcon(location.getLocation().getLocationId())));
				}
				else {
					sigactsItem.getChildren().add(new LegendItem("Unknown Group Attack",
							ImageManager_Phase1.getSigactLocationImageIcon(GroupType.Unknown)));
				}
			}
		}
		else {
			sigactsItem.setChildren(null);
		}
		if(sigactsItem.isVisible()) {
			updateLegendItem(sigactsItem);
		}
	}
	
	public void setRoadsItemVisible(boolean visible) {
		if(visible != roadsItem.isVisible()) {			
			if(visible) {
				addLegendItem(roadsItem);
			}
			else {
				removeLegendItem(roadsItem);
			}
		}
	}
	
	public void setGroupCenterGroups(Collection<GroupType> groups) {
		if(groups != null && !groups.isEmpty()) {
			groupCentersItem.setChildren(new ArrayList<LegendItem>(groups.size()));
			for(GroupType group : groups) {
				groupCentersItem.getChildren().add(new LegendItem(
						"<html><u>" + group.getGroupNameAbbreviated() + "</u>" + 
						group.getGroupNameFull().substring(1) + " Center</html>",
						//"Group " + group.toString() + " Center",
						ImageManager_Phase1.getGroupCenterImageIcon(group)));				
			}
		}
		else {
			groupCentersItem.setChildren(null);			
		}		
		if(groupCentersItem.isVisible()) {
			updateLegendItem(groupCentersItem);
		}		
	}	
	
	public void setGroupCentersItemVisible(boolean visible) {
		if(visible != groupCentersItem.isVisible()) {			
			if(visible) {
				addLegendItem(groupCentersItem);
			}
			else {
				removeLegendItem(groupCentersItem);
			}
		}
	}
	
	public void setGroupCentersItemName(String name) {
		groupCentersItem.setName(name);
		legendTree.repaint();
	}
	
	public void setImintItemVisible(boolean visible) {
		if(visible != imintItem.isVisible()) {			
			if(visible) {
				addLegendItem(imintItem);
			}
			else {
				removeLegendItem(imintItem);
			}
		}
	}
	
	public void setMovintItemVisible(boolean visible) {
		if(visible != movintItem.isVisible()) {		
			if(visible) {
				addLegendItem(movintItem);
			}
			else {
				removeLegendItem(movintItem);
			}
		}
	}
	
	public void setSigintItemVisible(boolean visible) {
		if(visible != sigintItem.isVisible()) {			
			if(visible) {
				addLegendItem(sigintItem);
			}
			else {
				removeLegendItem(sigintItem);
			}
		}
	}
	
	public void setSocintItemVisible(boolean visible) {
		if(visible != socintItem.isVisible()) {			
			if(visible) {
				addLegendItem(socintItem);
			}
			else {
				removeLegendItem(socintItem);
			}
		}
	}	
	
	public void setSocintGroups(Collection<GroupType> groups) {
		if(groups != null && !groups.isEmpty()) {		
			socintItem.setChildren(new ArrayList<LegendItem>(groups.size()));
			for(GroupType group : groups) {				
				socintItem.getChildren().add(new LegendItem(
						"<html><u>" + group.getGroupNameAbbreviated() + "</u>" + 
						group.getGroupNameFull().substring(1) + " Region</html>",
						//"Group " + group.toString() + " Region",
						ImageManager_Phase1.getSocintRegionImageIcon(group)));
			}
		}
		else {			
			socintItem.setChildren(null);
		}			
		if(socintItem.isVisible()) {
			updateLegendItem(socintItem);
		}
	}	
	
	public static class SigactLocation {
		
		public static enum LocationType {Group, LocationId, Unknown};
		
		protected GroupAttack attackLocation;
		
		protected LocationType locationType;
		
		public SigactLocation(GroupAttack attackLocation, LocationType locationType) {
			this.attackLocation = attackLocation;
			this.locationType = locationType;
		}

		public GroupAttack getAttackLocation() {
			return attackLocation;
		}

		public void setAttackLocation(GroupAttack attackLocation) {
			this.attackLocation = attackLocation;
		}

		public LocationType getLocationType() {
			return locationType;
		}

		public void setLocationType(LocationType locationType) {
			this.locationType = locationType;
		}
	}
	
	/** Test main */
	public static void main(String[] args) {		
		JFrame frame = new JFrame("Legend Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		LegendPanel_Phase1 legend = new LegendPanel_Phase1();
		legend.setPreferredSize(new Dimension(250, 400));
		
		Collection<GroupType> groups = Arrays.asList(GroupType.A, GroupType.B, GroupType.C, GroupType.D); 
		legend.setGroupCenterGroups(groups);		
		legend.setSocintGroups(groups);		
		legend.setSigactLocations(Arrays.asList(
				new GroupAttack(null, new GridLocation2D("1")), 
				new GroupAttack(null, new GridLocation2D("2")),
				new GroupAttack(null, new GridLocation2D("3")), 
				new GroupAttack(null, new GridLocation2D("4")),
				new GroupAttack(GroupType.X, null),
				new GroupAttack(GroupType.O, null),
				new GroupAttack(GroupType.A, null),
				new GroupAttack(GroupType.B, null),
				new GroupAttack(GroupType.C, null),
				new GroupAttack(GroupType.D, null),
				new GroupAttack(null, new GridLocation2D("?"))));		
		
		legend.setSigactsItemVisible(true);		
		legend.setRoadsItemVisible(true);		
		legend.setGroupCentersItemVisible(true);
		legend.setImintItemVisible(true);
		legend.setMovintItemVisible(true);
		legend.setSigintItemVisible(true);
		legend.setSocintItemVisible(true);
		
		frame.getContentPane().add(legend);
		frame.pack();
		frame.setVisible(true);		
	}
}
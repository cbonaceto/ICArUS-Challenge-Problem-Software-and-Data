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
package org.mitre.icarus.cps.app.widgets.phase_1.experiment;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Panel for selecting a group.
 * 
 * @author CBONACETO
 *
 */
public class GroupSelectionPanel extends JPanelConditionComponent {	
	
	private static final long serialVersionUID = 1L;
	
	/** The radio buttons for each group */	
	protected List<GroupRadioButton> groupButtons;
	
	/** The groups */
	protected List<GroupType> groups;
	
	protected ButtonGroup bg;
	
	protected GridBagLayout gbl;
	
	protected boolean editable = true;

	public GroupSelectionPanel(String componentId) {
		super(componentId);
		gbl = new GridBagLayout();
		setLayout(gbl);
		groupButtons = new LinkedList<GroupRadioButton>();
	}
	
	public void setGroups(List<GroupType> groups) {
		//Add a radio button for each group		
		removeAll();
		groupButtons.clear();		
		bg = new ButtonGroup();
		this.groups = groups;
		if(groups != null) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			//gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			Color background = getBackground();
			//gbc.fill = GridBagConstraints.HORIZONTAL;
			for(GroupType group : groups) {
				//String groupStr = "Group " + group.getGroupNameFull();
				String groupStr = group.getGroupNameAbbreviated();
				GroupRadioButton groupButton = new GroupRadioButton(group, groupStr);
				groupButton.setFont(WidgetConstants.FONT_DEFAULT);
				groupButton.setForeground(ColorManager_Phase1.getGroupCenterColor(group));
				groupButton.setBackground(background);
				groupButton.setActionCommand(groupStr);
				groupButton.setEnabled(editable);
				gbc.weightx = 0;
				if(gbc.gridy == groups.size() - 1) {
					gbc.weighty = 1;
				}
				add(groupButton, gbc);
				bg.add(groupButton);
				groupButtons.add(groupButton);				
				
				//Add label with group icon
				if(WidgetConstants.USE_GROUP_SYMBOLS) {
					JLabel groupLabel = new JLabel();
					groupLabel.setIcon(ImageManager_Phase1.getGroupSymbolImageIcon(group, IconSize.Large));
					gbc.gridx = 1;
					gbc.insets.left = 3;
					gbc.weightx = 1;
					gbc.anchor = GridBagConstraints.WEST;
					add(groupLabel, gbc);
					gbc.gridx = 0;
					gbc.insets.left = 0;
					gbc.anchor = GridBagConstraints.NORTHWEST;
				}
				
				gbc.gridy++;				
			}			
		}	
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		if(editable != this.editable) {
			this.editable = editable;
			if(groupButtons != null && !groupButtons.isEmpty()) {
				for(GroupRadioButton button : groupButtons) {
					button.setEnabled(editable);
				}
			}
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(groupButtons != null) {		
			for(GroupRadioButton button : groupButtons) {
				button.setBackground(bg);
			}
		}
	}

	public List<GroupType> getGroups() {
		return this.groups;
	}
	
	public int getNumGroups() {
		if(groupButtons != null) {
			return groupButtons.size();
		}
		return 0;
	}	
	
	public GroupType getSelectedGroup() {
		if(groupButtons != null && !groupButtons.isEmpty()) {
			for(GroupRadioButton button : groupButtons) {
				if(button.isSelected()) {
					return button.group;
				}
			}
		}
		return null;
	}
	
	public boolean isButtonActionListenerPresent(ActionListener l) {
		if(groupButtons != null && !groupButtons.isEmpty()) {
			ActionListener[] listeners = groupButtons.get(0).getActionListeners();
			if(listeners != null) {
				for(ActionListener listener : listeners) {
					if(l == listener) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void addButtonActionListener(ActionListener l) {
		if(groupButtons != null) {
			for(JRadioButton button : groupButtons) {
				button.addActionListener(l);
			}
		}
	}
	
	public void removeButtonActionListener(ActionListener l) {
		if(groupButtons != null) {
			for(JRadioButton button : groupButtons) {
				button.removeActionListener(l);
			}
		}
	}
	
	protected class GroupRadioButton extends JRadioButton {
		
		private static final long serialVersionUID = 1L;
		
		GroupType group;
		
		public GroupRadioButton(GroupType group) {
			super();
			this.group = group;
		}
		
		public GroupRadioButton(GroupType group, String buttonName) {
			super(buttonName);
			this.group = group;
		}
	}
}
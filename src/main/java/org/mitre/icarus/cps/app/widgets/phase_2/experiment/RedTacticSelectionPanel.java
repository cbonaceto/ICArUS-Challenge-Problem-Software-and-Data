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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.phase_2.WidgetConstants_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * Panel with radio buttons to select the most likely Red tactic.
 * 
 * @author CBONACETO
 *
 */
public class RedTacticSelectionPanel extends JPanelConditionComponent {	
	private static final long serialVersionUID = -1486036658208367419L;

	/** The radio buttons for each Red tactic */
	protected List<RedTacticSelectionButton> redTacticButtons;
	
	/** The button group containing the buttons */
	protected ButtonGroup bg;
	
	/** Whether the buttons are editable (enabled) */
	protected boolean editable = true;
	
	public RedTacticSelectionPanel(String componentId) {
		super(componentId);
		setLayout(new GridBagLayout());
		setFont(WidgetConstants.FONT_DEFAULT);
	}
	
	public void setRedTactics(Collection<RedTacticType> redTactics) {		
		//Add a radio button for each Red tactic
		removeAll();
		if(redTacticButtons == null) {
			redTacticButtons = new ArrayList<RedTacticSelectionButton>();
		} else {
			redTacticButtons.clear();
		}
		bg = new ButtonGroup();		
		if(redTactics != null && !redTactics.isEmpty()) {			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.insets.bottom = WidgetConstants_Phase2.DATUM_LIST_VERTICAL_SPACER;
			Color background = getBackground();
			Font font = getFont();
			for(RedTacticType tactic : redTactics) {
				RedTacticSelectionButton button = new RedTacticSelectionButton(tactic);
				button.setBackground(background);
				button.setFont(font);
				button.setEnabled(editable);
				gbc.weightx = 0;
				if(gbc.gridy == redTactics.size() - 1) {
					gbc.weighty = 1;
					gbc.insets.bottom = 0;
				}
				add(button, gbc);
				bg.add(button);
				redTacticButtons.add(button);
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
			if(redTacticButtons != null && !redTacticButtons.isEmpty()) {
				for(RedTacticSelectionButton button : redTacticButtons) {
					button.setEnabled(editable);
				}
			}
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(redTacticButtons != null && !redTacticButtons.isEmpty()) {
			for(RedTacticSelectionButton button : redTacticButtons) {
				button.setBackground(bg);
			}
		}
	}	

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(redTacticButtons != null && !redTacticButtons.isEmpty()) {
			for(RedTacticSelectionButton button : redTacticButtons) {
				button.setFont(font);
			}
		}
	}
	
	public void clearRedTacticSelection() {
		bg.clearSelection();
		if(redTacticButtons != null && !redTacticButtons.isEmpty()) {
			for(RedTacticSelectionButton button : redTacticButtons) {
				button.setSelected(false);
			}
		}
	}

	public RedTacticType getSelectedRedTactic() {
		if(redTacticButtons != null && !redTacticButtons.isEmpty()) {
			for(RedTacticSelectionButton button : redTacticButtons) {
				if(button.isSelected()) {
					return button.getTactic();
				}
			}
		}
		return null;
	}
	
	public boolean isButtonActionListenerPresent(ActionListener l) {
		if(redTacticButtons != null && !redTacticButtons.isEmpty()) {
			ActionListener[] listeners = redTacticButtons.get(0).getActionListeners();
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
		if(redTacticButtons != null) {
			for(RedTacticSelectionButton button : redTacticButtons) {
				button.addActionListener(l);
			}
		}
	}
	
	public void removeButtonActionListener(ActionListener l) {
		if(redTacticButtons != null) {
			for(RedTacticSelectionButton button : redTacticButtons) {
				button.removeActionListener(l);
			}
		}
	}
	
	protected static class RedTacticSelectionButton extends JRadioButton {			
		private static final long serialVersionUID = 1L;
		
		/** The Red tactic this button is for */
		protected RedTacticType tactic;		
		
		public RedTacticSelectionButton(RedTacticType tactic) {
			this.tactic = tactic;
			setText(tactic.getName());
			setActionCommand(tactic.toString());
		}

		public RedTacticType getTactic() {
			return tactic;
		}

		public void setTactic(RedTacticType tactic) {
			this.tactic = tactic;
		}			
	}
}
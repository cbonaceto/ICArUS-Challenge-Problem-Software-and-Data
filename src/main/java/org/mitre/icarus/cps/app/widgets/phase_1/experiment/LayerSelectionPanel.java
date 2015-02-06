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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.ImageManager.IconSize;
import org.mitre.icarus.cps.app.widgets.phase_1.ImageManager_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.INTLayerPresentationProbeBase;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.IntLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.SigintLayer;
import org.mitre.icarus.cps.exam.phase_1.testing.int_layer_presentation.Task_7_INTLayerPresentationProbe;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_1.IntType;

/**
 * Panel for selecting a layer to show.
 * 
 * @author CBONACETO
 *
 */
public class LayerSelectionPanel<T extends INTLayerPresentationProbeBase> extends JPanelConditionComponent {	
	
	private static final long serialVersionUID = 1L;
	
	/** Whether the layers should be shown in reverse order */
	protected boolean reverseLayerOrder; 
	
	/** The radio buttons for each layer */	
	protected Map<T, IntLayerRadioButton> layerButtons;
	
	/** Whether to display the cost of each layer (Task 7 only) */
	protected boolean showLayerCost;
	
	protected ButtonGroup bg;
	
	protected GridBagLayout gbl;
	
	protected ActionListener buttonListener;

	public LayerSelectionPanel(String componentId) {
		super(componentId);
		gbl = new GridBagLayout();
		setLayout(gbl);
		layerButtons = new HashMap<T, IntLayerRadioButton>(12);
	}

	public void setLayers(Collection<T> layers, boolean showLayerCost, boolean reverseLayerOrder) {
		//Add a radio button for each layer
		setPreferredSize(null);
		removeAll();
		this.showLayerCost = showLayerCost;
		this.reverseLayerOrder = reverseLayerOrder;
		layerButtons.clear();		
		bg = new ButtonGroup();
		boolean sigintBlockFound = false;
		if(layers != null) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			//gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			//gbc.fill = GridBagConstraints.HORIZONTAL;
			int index = 0;
			if(reverseLayerOrder) {
				layers = new LinkedList<T>(layers);
				Collections.reverse((List<T>)layers);
			}
			for(T layer : layers) {
				String layerStr = createLayerString(layer.getLayerType());
				IntLayerRadioButton layerButton = null;
				if(showLayerCost && layer instanceof Task_7_INTLayerPresentationProbe) {
					Task_7_INTLayerPresentationProbe task7Layer = (Task_7_INTLayerPresentationProbe)layer;
					layerButton = new IntLayerRadioButton(layer,
							layerStr +  " (" + task7Layer.getCostCredits() +
							((task7Layer.getCostCredits() == 1) ? " credit)" : " credits)"));
				}
				else {
					layerButton = new IntLayerRadioButton(layer, layerStr);
				}
				layerButton.setActionCommand(layerStr);
				if(buttonListener != null) {
					layerButton.addActionListener(buttonListener);
				}
				
				if(layer.getLayerType() instanceof SigintLayer) {
					if(!sigintBlockFound) {
						//Add a "SIGINT On" label and start indenting the SIGINT layer block
						sigintBlockFound = true;
						gbc.insets.left = layerButton.getInsets().left;
						add(new JLabel("SIGINT on:"), gbc);
						gbc.gridy++;
						gbc.insets.left = 20;
					}					
				}
				else {
					//Stop indenting the SIGINT layer block
					sigintBlockFound = false;
					gbc.insets.left = 0;
				}
				
				gbc.weightx = 0;
				if(index == layers.size() - 1) {
					gbc.weighty = 1;
				}				
				add(layerButton, gbc);				
				bg.add(layerButton);
				layerButtons.put(layer, layerButton);
				
				//Add label with group icon for SIGINT layers
				if(layer.getLayerType() instanceof SigintLayer && WidgetConstants.USE_GROUP_SYMBOLS) {
					SigintLayer sigint = (SigintLayer)layer.getLayerType();
					if(sigint.getGroup() != null) {
						JLabel groupLabel = new JLabel();
						groupLabel.setIcon(ImageManager_Phase1.getGroupSymbolImageIcon(sigint.getGroup(), IconSize.Large));
						int leftInsets_orig = gbc.insets.left;
						gbc.gridx = 1;
						gbc.insets.left = 3;
						gbc.weightx = 1;
						gbc.anchor = GridBagConstraints.WEST;
						add(groupLabel, gbc);
						gbc.gridx = 0;
						gbc.insets.left = leftInsets_orig;
						gbc.anchor = GridBagConstraints.NORTHWEST;
					}
				}
				
				gbc.gridy++;
				index++;
			}			
		}	
		setPreferredSize(getPreferredSize());
	}
	
	protected String createLayerString(IntLayer layer) {
		if(layer instanceof SigintLayer) {
			SigintLayer sigint = (SigintLayer)layer;
			if(sigint.getGroup() != null) {
				return "Group " + sigint.getGroup().getGroupNameFull();
			}
			else {
				return "Group Unknown";
			}
			/*StringBuilder sb = new StringBuilder(layer.getLayerType().toString());
			if(sigint.getGroup() != null) {
				sb.append(" on Group " + sigint.getGroup().getGroupNameFull());				
				return sb.toString();
			}*/
		}
		return layer.getLayerType().toString();
	}
	
	public int getNumLayers() {
		return layerButtons.size();
	}
	
	public int getNumEnabledLayers() {
		int numLayers = 0;	
		for(IntLayerRadioButton button : layerButtons.values()) {
			if(button.isEnabled()) {
				numLayers++;
			}
		}
		return numLayers;
	}
	
	public Collection<T> getEnabledLayers() {
		LinkedList<T> layers = new LinkedList<T>();
		for(IntLayerRadioButton button : layerButtons.values()) {
			if(button.isEnabled()) {
				layers.add(button.layer);
			}
		}
		return layers;
	}
	
	public Collection<T> getLayers() {
		return layerButtons.keySet();
	}	
	
	public T getSelectedLayer() {
		if(!layerButtons.isEmpty()) {
			for(IntLayerRadioButton button : layerButtons.values()) {
				if(button.isEnabled() && button.isSelected()) {
					return button.layer;
				}
			}
		}
		return null;
	}
	
	public void setLayerEnabled(T layer, boolean enabled) {
		IntLayerRadioButton button = layerButtons.get(layer);
		//System.out.println(button.getText() + ": " + button.isEnabled() + ", " + enabled);
		if(button != null && button.isEnabled() != enabled) {
			setLayerButtonEnabled(button, enabled);
		}
	}
	
	protected void setLayerButtonEnabled(IntLayerRadioButton button, boolean enabled) {
		button.setEnabled(enabled);
		if(!enabled) {
			bg.remove(button);
		}
		else {
			bg.add(button);
		}
	}
	
	/*public void removeLayer(T layer) {		
		IntLayerRadioButton button = layerButtons.remove(layer);
		if(button != null) {
			bg.remove(button);
			remove(button);
			adjustLastLayer();
			revalidate();
			repaint();
		}
	}*/
	
	/** 
	 * Enable or disable all SIGINT layers.
	 */
	public void setSigintLayersEnabled(boolean enabled) {
		if(!layerButtons.isEmpty()) {
			for(IntLayerRadioButton button : layerButtons.values()) {
				if(button.layer.getLayerType().getLayerType() == IntType.SIGINT &&
						button.isEnabled() != enabled) {
					setLayerButtonEnabled(button, enabled);
				}
			}
		}		
	}
	
	
	/**
	 * Remove all SIGINT layers.
	 */
	/*public void removeSigintLayers() {
		if(!layerButtons.isEmpty()) {
			List<IntLayerRadioButton> sigintButtons = new LinkedList<IntLayerRadioButton>();
			for(IntLayerRadioButton button : layerButtons.values()) {
				if(button.layer.getLayerType().getLayerType() == IntType.SIGINT) {
					sigintButtons.add(button);
				}
			}
			if(!sigintButtons.isEmpty()) {
				for(IntLayerRadioButton sigintButton : sigintButtons) {
					layerButtons.remove(sigintButton.layer);
					remove(sigintButton);					
				}
				adjustLastLayer();
				revalidate();
				repaint();
			}
		}		
	}*/
	
	protected void adjustLastLayer() {
		Component[] components = getComponents();
		if(components != null && components.length > 0) {
			Component lastComponent = components[components.length-1];
			GridBagConstraints gbc = gbl.getConstraints(lastComponent);
			if(gbc != null) {
				gbc.weighty = 1;
				gbl.setConstraints(lastComponent, gbc);
			}
		}
	}
	
	public boolean isButtonActionListenerPresent(ActionListener l) {
		return (buttonListener == l);
	}
	
	public void setButtonActionListener(ActionListener l) {
		if(layerButtons != null) {
			for(IntLayerRadioButton button : layerButtons.values()) {
				if(buttonListener != null) {
					button.removeActionListener(buttonListener);
				}
				button.addActionListener(l);
			}
		}
		buttonListener = l;
	}
	
	public void removeButtonActionListener() {
		if(layerButtons != null && buttonListener != null) {
			for(IntLayerRadioButton button : layerButtons.values()) {
				button.removeActionListener(buttonListener);
			}
		}
		buttonListener = null;
	}
	
	protected class IntLayerRadioButton extends JRadioButton {
		
		private static final long serialVersionUID = 1L;
		
		T layer;
		
		public IntLayerRadioButton(T layer) {
			super();
			this.layer = layer;
		}
		
		public IntLayerRadioButton(T layer, String buttonName) {
			super(buttonName);
			this.layer = layer;
		}
	}
}
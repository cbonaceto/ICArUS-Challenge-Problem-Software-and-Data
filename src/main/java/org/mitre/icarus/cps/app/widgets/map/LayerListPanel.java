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
package org.mitre.icarus.cps.app.widgets.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.gui.InstructionNavigationPanel;

/**
 * Layer panel implementation that allows each layer to have an optional link that 
 * links to the rules page for that layer.
 * 
 * @author CBONACETO
 *
 */
public class LayerListPanel extends JPanel implements ILayerPanel {	
	
	private static final long serialVersionUID = 3333630378726556365L;	
	
	/** Reference to the map containing the layers */
	protected IMapPanel map;
	
	/** All the layers (mapped by layer ID)  */
	protected Map<String, LayerAndLink> layers;	
	
	/** The current visible layers (in order) */
	protected List<LayerAndLink> visibleLayers;
	
	protected LayerAndLink lastLayer;
	
	/** Panel containing the layer checkboxes */
	protected JPanel contentPanel;
	
	protected GridBagLayout gbl;
	
	protected Component parentComponent;
	
	/** The external instructions window where layer instruction pages are shown */
	protected JFrame externalInstructionsWindow;
	protected InstructionNavigationPanel externalInstructionsPanel; 
	
	/** The instructions pages associated with layers */
	//protected ArrayList<InstructionsPage> instructionsPages;
	
	/** Additional Instruction pages not associated with a layer (mapped by page name) */
	protected Map<String, InstructionsPage> otherInstructionsPages;	
	
	protected boolean layerInstructionsEnabled = true;
	
	public LayerListPanel(IMapPanel map, Component parentComponent) {
		this(map, parentComponent, null);
	}
	
	public LayerListPanel(IMapPanel map, Component parentComponent, Collection<ILayer<? extends IMapObject>> layers) {
		super(new BorderLayout());
		this.parentComponent = parentComponent;
		this.map = map;
		this.layers = new HashMap<String, LayerAndLink>(10);
		this.visibleLayers = new LinkedList<LayerAndLink>();
		
		Dimension size =  new Dimension(665, 670);
		externalInstructionsPanel = new InstructionNavigationPanel();
		externalInstructionsPanel.setPreferredSize(size);
		/*instructionsPages = new ArrayList<InstructionsPage>();
		externalInstructionsPanel.setInstructionsPages("", instructionsPages);*/
		otherInstructionsPages = new HashMap<String, InstructionsPage>();
		
		gbl = new GridBagLayout();
		contentPanel = new JPanel(gbl);
		JScrollPane sp = new JScrollPane(contentPanel);
		sp.setBorder(null);
		add(sp);
		
		setLayers(layers);
	}	
	
	public IMapPanel getMap() {
		return map;
	}

	@Override
	public void setMap(IMapPanel map) {
		this.map = map;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(contentPanel != null) {
			contentPanel.setBackground(bg);
			if(layers != null) {
				for(LayerAndLink layer : layers.values()) {
					layer.layerCheckBox.setBackground(bg);
				}
			}
		}
	}	

	@Override
	public void setLayers(Collection<ILayer<? extends IMapObject>> layers) {
		this.layers.clear();
		visibleLayers.clear();
		lastLayer = null;
		contentPanel.removeAll();		
		if(layers != null && !layers.isEmpty()) {
			for(ILayer<? extends IMapObject> layer : layers) {
				addLayer(layer, true, null, null);
			}
		}
	}

	@Override
	public void addLayer(ILayer<? extends IMapObject> layer) {
		addLayer(layer, true, null, null);		
	}
	
	public void addLayer(ILayer<? extends IMapObject> layer, boolean enabled) {
		addLayer(layer, enabled, null, null);		
	}
	
	public void addLayer(ILayer<? extends IMapObject> layer, boolean enabled,
			InstructionsPage instructions, String instructionsLinkText) {
		if(getLayer(layer) != null) {
			return;
		}		
		if(lastLayer != null) {
			//Adjust constraints of previous last layer
			GridBagConstraints lastConstraints = gbl.getConstraints(lastLayer.layerCheckBox);
			lastConstraints.weighty = 0;
			gbl.setConstraints(lastLayer.layerCheckBox, lastConstraints);
		}		
		final LayerAndLink layerAndLink = new LayerAndLink(layer);
		layers.put(layer.getId(), layerAndLink);
		layerAndLink.layerCheckBox = new JLayerAndLinkCheckBox();
		layerAndLink.layerCheckBox.getCheckBox().setSelected(layer.isVisible());
		layerAndLink.layerCheckBox.setInstructionsLinkEnabled(layerInstructionsEnabled);
		layerAndLink.layerCheckBox.setVisible(enabled);
		layerAndLink.layerCheckBox.setBackground(getBackground());
		layerAndLink.layerCheckBox.setLayerName(layer.toString());
		if(layer.getIcon() != null) {
			layerAndLink.layerCheckBox.setLayerIcon(new ImageIcon(layer.getIcon()));
		}
		if(instructions != null && instructionsLinkText != null) {
			layerAndLink.instructions = instructions;
			layerAndLink.layerCheckBox.setInstructionsLinkText(instructionsLinkText);
			if(!externalInstructionsPanel.containsInstructionsPage(instructions)) {
				externalInstructionsPanel.addInstructionsPage(instructions);
			}
			/*if(!instructionsPages.contains(instructions)) {
				instructionsPages.add(instructions);
			}*/
		}		
		if(enabled) {
			addLayerToPanel(layerAndLink);
		}
		
		//Add action listener to checkbox to update layer visibility
		layerAndLink.layerCheckBox.getCheckBox().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Adjust layer visibility and redraw map
				layerAndLink.layer.setVisible(layerAndLink.layerCheckBox.getCheckBox().isSelected());
				map.redraw(); 
			}
		});
		
		//Add hyperlink listener to instructions link label
		layerAndLink.layerCheckBox.linkTextPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					//Show rules for the layer
					showInstructionsForLayer(layerAndLink);
					setExternalInstructionsVisible(true);
					externalInstructionsWindow.toFront();
					//System.out.println("rules linked clicked: " + layerAndLink.layer.toString());
				}
			}
		});		
	}
	
	protected void addLayerToPanel(LayerAndLink layer) {
		if(lastLayer != null) {
			//Adjust constraints of previous last layer
			GridBagConstraints lastConstraints = gbl.getConstraints(lastLayer.layerCheckBox);
			lastConstraints.weighty = 0;
			gbl.setConstraints(lastLayer.layerCheckBox, lastConstraints);
		}
		
		//Add layer to the layer panel
		visibleLayers.add(layer);
		GridBagConstraints gbc = createConstraints();
		gbc.gridy = contentPanel.getComponentCount();
		layer.row = gbc.gridy;
		gbc.weighty = 1;
		contentPanel.add(layer.layerCheckBox, gbc);
		lastLayer = layer;
		revalidate();
		repaint();	
	}
	
	protected GridBagConstraints createConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;	
		return gbc;
	}
	
	/**
	 * Add an instructions page not associated with a layer.
	 */
	public void addInstructionsPage(String pageName, InstructionsPage instructions) {
		otherInstructionsPages.put(pageName, instructions);
		if(!externalInstructionsPanel.containsInstructionsPage(instructions)) {
			externalInstructionsPanel.addInstructionsPage(instructions);
		}
		/*if(!instructionsPages.contains(instructions)) {
			instructionsPages.add(instructions);
		}*/
	}
	
	/**
	 * Remove an instructions page not associated with a layer.
	 */
	public void removeInstructionsPage(String pageName) {
		InstructionsPage page = otherInstructionsPages.get(pageName);
		if(page != null) {
			otherInstructionsPages.remove(pageName);
			externalInstructionsPanel.removeInstructionsPage(page);
		}
	}
	
	public void showInstructionsPage(String pageName) {
		InstructionsPage page = otherInstructionsPages.get(pageName);
		externalInstructionsPanel.showPage(page);
		/*int pageIndex = instructionsPages.indexOf(page);
		if(pageIndex > -1) {
			externalInstructionsPanel.showPage(pageIndex);
			setExternalInstructionsVisible(true);
		}*/
	}
	
	public void setLayerName(ILayer<? extends IMapObject> layer, String name) {
		LayerAndLink layerAndLink = getLayer(layer);
		if(layerAndLink != null) {
			layerAndLink.layerCheckBox.setLayerName(name);
		}
	}

	public void setLayerInstructions(ILayer<? extends IMapObject> layer, InstructionsPage instructions, 
			String instructionsLinkText) {	
		LayerAndLink layerAndLink = getLayer(layer);
		if(layerAndLink != null) {
			layerAndLink.layerCheckBox.setInstructionsLinkText(instructionsLinkText);
			if(instructions == null) {
				if(layerAndLink.instructions != null) {
					externalInstructionsPanel.removeInstructionsPage(layerAndLink.instructions);
					//instructionsPages.remove(layerAndLink.instructions);
				}
			} else {
				if(!externalInstructionsPanel.containsInstructionsPage(instructions)) {
					externalInstructionsPanel.addInstructionsPage(instructions);
				}
				/*if(!instructionsPages.contains(instructions)) {
					instructionsPages.add(instructions);
				}*/
			}
			layerAndLink.instructions = instructions;
		}
	}
	
	public void setLayerInstructionsEnabled(boolean enabled) {
		if(enabled != this.layerInstructionsEnabled) {
			if(layers != null && !layers.isEmpty()) {
				for(LayerAndLink layer : layers.values()) {
					layer.layerCheckBox.setInstructionsLinkEnabled(enabled);
				}
			}
			this.layerInstructionsEnabled = enabled;
			if(!enabled && externalInstructionsWindow != null) {
				externalInstructionsWindow.setVisible(false);
			}
		}
	}
	
	public void showInstructionsForLayer(ILayer<? extends IMapObject> layer) {
		showInstructionsForLayer(getLayer(layer));
	}
	
	protected void showInstructionsForLayer(LayerAndLink layer) {
		if(layer != null && layer.instructions != null) {
			if(externalInstructionsPanel.showPage(layer.instructions)) {
				setExternalInstructionsVisible(true);
			}
			/*int pageIndex = instructionsPages.indexOf(layer.instructions);			
			if(pageIndex > -1) {
				externalInstructionsPanel.showPage(pageIndex);
				setExternalInstructionsVisible(true);
			}*/					
		}
	}
	
	public void setLayerInstructionsWindowVisible(boolean visible) {
		setExternalInstructionsVisible(visible);
	}
	
	public boolean isLayerEnabled(ILayer<? extends IMapObject> layer) {
		LayerAndLink layerAndLink = getLayer(layer);
		if(layerAndLink != null) {
			return layerAndLink.layerCheckBox.isVisible();
		}
		return false;
	}
	
	public void setLayerEnabled(ILayer<? extends IMapObject> layer, boolean enabled) {
		LayerAndLink layerAndLink = getLayer(layer);
		if(layerAndLink != null) {
			layerAndLink.layerCheckBox.getCheckBox().setSelected(layer.isVisible());
			if(layerAndLink.layerCheckBox.isVisible() != enabled) {		
				layerAndLink.layerCheckBox.setVisible(enabled);
				if(enabled) {
					addLayerToPanel(layerAndLink);
				}
				else {
					removeLayerFromPanel(layerAndLink);
				}			
			}
		}
	}

	@Override
	public void removeLayer(ILayer<? extends IMapObject> layer) {
		LayerAndLink layerAndLink = getLayer(layer);
		if(layerAndLink != null) {
			layers.remove(layer.getId());
			removeLayerFromPanel(layerAndLink);
			if(layerAndLink.instructions != null) {
				externalInstructionsPanel.removeInstructionsPage(layerAndLink.instructions);
				//instructionsPages.remove(layerAndLink.instructions);
			}
		}
	}
	
	protected void removeLayerFromPanel(LayerAndLink layer) {
		contentPanel.remove(layer.layerCheckBox);
		visibleLayers.remove(layer);
		lastLayer = null;
		if(!visibleLayers.isEmpty()) {
			//Adjust constraints of remaining visible layers
			int row = 0;
			for(LayerAndLink layerAndLink : visibleLayers) {
				GridBagConstraints gbc = gbl.getConstraints(layerAndLink.layerCheckBox);				
				gbc.gridy = row;
				if(row == visibleLayers.size() - 1) {
					lastLayer = layerAndLink;
					gbc.weighty = 1;	
				}
				else {
					gbc.weighty = 0;
				}
				gbl.setConstraints(layerAndLink.layerCheckBox, gbc);
				row++;
			}
		}	
		revalidate();
		repaint();		
	}
	
	protected LayerAndLink getLayer(ILayer<? extends IMapObject> layer) {
		if(!layers.isEmpty()) {
			return layers.get(layer.getId());
		}
		return null;
	}	

	@Override
	public void removeAllLayers() {
		layers.clear();
		visibleLayers.clear();
		lastLayer = null;
		contentPanel.removeAll();
		revalidate();
		repaint();
	}

	@Override
	public JComponent getLayerPanelComponent() {
		return this;
	}	

	protected void setExternalInstructionsVisible(boolean visible) {
		if(externalInstructionsWindow == null && visible) {
			externalInstructionsWindow = new JFrame("PROBS");
			externalInstructionsWindow.setResizable(true);
			externalInstructionsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			externalInstructionsWindow.getContentPane().add(externalInstructionsPanel);			
			externalInstructionsWindow.pack();	
			externalInstructionsWindow.setMinimumSize(externalInstructionsWindow.getSize());
			if(parentComponent != null) {
				externalInstructionsWindow.setLocationRelativeTo(parentComponent);
				int xLocation = externalInstructionsWindow.getLocation().x + parentComponent.getWidth()/2 + 
				externalInstructionsWindow.getWidth()/2;
				int screenWidth = externalInstructionsWindow.getToolkit().getScreenSize().width;
				if(xLocation + externalInstructionsWindow.getWidth() > screenWidth) {
					xLocation -= xLocation + externalInstructionsWindow.getWidth() - screenWidth;
				}
				externalInstructionsWindow.setLocation(xLocation, externalInstructionsWindow.getLocation().y);
			}			
		}
		if(externalInstructionsWindow != null && visible != externalInstructionsWindow.isVisible()) {
			externalInstructionsWindow.setVisible(visible);
			//if(visible) {
				//externalInstructionsWindow.toFront();
			//}
		}
	}
	
	protected static class LayerAndLink {		
		/** The layer */
		protected ILayer<? extends IMapObject> layer;
		
		/** Layer check box. Also contains optional layer icon and instructions page link. */
		protected JLayerAndLinkCheckBox layerCheckBox;		
		
		/** The instructions page for the layer (if any) */
		protected InstructionsPage instructions;
		
		protected int row;
				
		public LayerAndLink(ILayer<? extends IMapObject> layer) {
			this.layer = layer;
		}
	}
	
	protected static class JLayerAndLinkCheckBox extends JLayerPanelCheckBox {
		
		private static final long serialVersionUID = 1L;
		
		/** Text pane where the link is shown */
		protected JEditorPane linkTextPane;
		
		protected boolean linkEnabled = true;
		
		public JLayerAndLinkCheckBox() {
			super();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 2;
			gbc.insets.left = 2;
			linkTextPane = new JEditorPane();
			linkTextPane.setEditable(false);			
			linkTextPane.setContentType("text/html");
			linkTextPane.setFont(checkBox.getFont());
			add(linkTextPane, gbc);
			linkTextPane.setVisible(false);
		}
		
		public JCheckBox getCheckBox() {
			return checkBox;
		}
		
		public void setInstructionsLinkEnabled(boolean enabled) {
			if(enabled != this.linkEnabled) {
				linkTextPane.setVisible(enabled);
				this.linkEnabled = enabled;
			}
		}
		
		public void setInstructionsLinkText(String text) {	
			if(text == null || text.isEmpty()) {
				removeInstructionsLinkText();
			}
			else {
				linkTextPane.setText("<html><font face=\"" + icon.getFont().getName() 
				+ "\">" + "(<a href=\"rules\">" + text + "</a>)</html>");
				//linkTextPane.setText("<html><font face=\"" + icon.getFont().getName() 
				//		+ "\" size=\"3\">" + "<b>(<a href=\"rules\">" + text + "</a>)</b></html>");
				linkTextPane.setVisible(linkEnabled);
			}
		}
		
		public void removeInstructionsLinkText() {
			linkTextPane.setText(null);
			linkTextPane.setVisible(false);
		}
		
		public static String formatTextAsHTML(String text, String orientation, String fontName, int fontSize) {
			StringBuilder html = new StringBuilder("<html>");
			
			html.append("<font face=\"");
			html.append(fontName + "\" size=\"");
			html.append(Integer.toString(fontSize) + "\">");
			
			html.append("<" + orientation + ">");		
			html.append(text);
			html.append("</" + orientation + ">");
			
			html.append("</font></html>");
			//System.out.println(html.toString());
			return html.toString();
		}
	}
}
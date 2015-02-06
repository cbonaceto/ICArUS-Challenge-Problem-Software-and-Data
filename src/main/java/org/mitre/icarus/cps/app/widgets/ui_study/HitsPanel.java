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
package org.mitre.icarus.cps.app.widgets.ui_study;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.experiment.ui_study.UIStudyConstants;

/**
 * @author CBONACETO
 *
 */
public class HitsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<HitPanelAndTitle> hitPanels;
	
	protected int numColumns;

	public HitsPanel(int numHitPanels, int numColumns) {
		this(createDefaultHitPanelProperties(numHitPanels), numColumns);
	}
	
	public HitsPanel(List<HitPanelProperties> hitPanelProperties, int numColumns) {
		super(new GridBagLayout());
		this.numColumns = numColumns;
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//Create the hit panels
		createHitPanels(hitPanelProperties);		
	}	
	
	protected void createHitPanels(List<HitPanelProperties> hitPanelProperties) {
		//Create the hit panels
		hitPanels = new ArrayList<HitPanelAndTitle>(hitPanelProperties.size());		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;
		//gbc.weightx = 1; gbc.weighty = 1;
		//gbc.fill = GridBagConstraints.BOTH;
		int columnCount = 1;
		for(HitPanelProperties panelProperties : hitPanelProperties) {
			if(columnCount > numColumns) {
				columnCount = 1;
				gbc.gridy++;
				gbc.gridx = 0;
				gbc.insets.top = 10;
			} 
			if(columnCount > 1) {
				gbc.anchor = GridBagConstraints.WEST;
				gbc.insets.left = 10;
			} else {
				gbc.anchor = GridBagConstraints.EAST;
				gbc.insets.left = 0;
			}
			
			//Create the current hits panel and add it to the layout
			HitPanelAndTitle hitPanel = new HitPanelAndTitle(panelProperties);
			hitPanels.add(hitPanel);
			add(hitPanel, gbc);
			
			gbc.gridx++;
			columnCount++;
		}
	}	
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(hitPanels != null) {
			for(HitPanelAndTitle hitPanel : hitPanels) {
				hitPanel.hitPanel.setFont(font);
			}
		}
	}

	public void addHit(Point hit, int panelIndex) {
		if(panelIndex < 0 || panelIndex >= hitPanels.size()) {
			throw new IllegalArgumentException("Panel index out of bounds");
		}
		hitPanels.get(panelIndex).hitPanel.addHit(hit);
	}
	
	public void clearAllHits() {
		for(HitPanelAndTitle hitPanel : hitPanels) {
			hitPanel.hitPanel.clearHits();
		}
	}
	
	public void clearHits(int panelIndex) {
		if(panelIndex < 0 || panelIndex >= hitPanels.size()) {
			throw new IllegalArgumentException("Panel index out of bounds");
		}
		hitPanels.get(panelIndex).hitPanel.clearHits();
	}
	
	public void setHits(List<Point> hits, int panelIndex) {
		if(panelIndex < 0 || panelIndex >= hitPanels.size()) {
			throw new IllegalArgumentException("Panel index out of bounds");
		}
		HitPanel hitPanel = hitPanels.get(panelIndex).hitPanel;
		hitPanel.clearHits();
		hitPanel.addHits(hits);
	}
	
	public void setNumHitPanels(int numHitPanels) {
		if(numHitPanels != hitPanels.size()) {
			setHitPanelProperties(createDefaultHitPanelProperties(numHitPanels));
		}
	}
	
	protected static List<HitPanelProperties> createDefaultHitPanelProperties(int numHitPanels) {
		ArrayList<HitPanelProperties> hitPanelProperties = new ArrayList<HitPanelProperties>(numHitPanels);
		Dimension gridSize = new Dimension(UIStudyConstants.GRID_WIDTH, UIStudyConstants.GRID_HEIGHT);
		for(int i=0; i<numHitPanels; i++) {
			hitPanelProperties.add(new HitPanelProperties(Integer.toString(i+1),
					"+", Color.BLACK, gridSize));
		}
		return hitPanelProperties;
	}
	
	public void setTitles(List<String> titles) {
		if(titles.size() != hitPanels.size()) {
			setHitPanelProperties(createDefaultHitPanelProperties(titles.size()));
		}
		int i = 0;
		for(String title : titles) {
			hitPanels.get(i).setTitle(title);
			i++;
		}
	}
	
	public void setHitPanelProperties(List<HitPanelProperties> hitPanelProperties) {
		if(hitPanelProperties.size() != hitPanels.size()) {
			removeAll();
			createHitPanels(hitPanelProperties);
			revalidate();
		} else {
			int i = 0;
			for(HitPanelProperties panelProperties : hitPanelProperties) {
				hitPanels.get(i).setHitPanelProperties(panelProperties);
				i++;
			}
		}
	}
	
	/**
	 * @author CBONACETO
	 *
	 */
	public static class HitPanelProperties {
		protected final String title;
	
		protected final Image hitSymbol;
		
		protected final String hitSymbolString;
		
		protected final Color hitSymbolColor;
		
		protected final Dimension gridSize;
		
		public HitPanelProperties(String title, Image hitSymbol, Dimension gridSize) {
			this.title = title;
			this.hitSymbol = hitSymbol;
			this.hitSymbolString = null;
			this.hitSymbolColor = null;
			this.gridSize = gridSize;
		}
		
		public HitPanelProperties(String title, String hitSymbolString, Color hitSymbolColor, Dimension gridSize) {
			this.title = title;
			this.hitSymbol = null;
			this.hitSymbolString = hitSymbolString;
			this.hitSymbolColor = hitSymbolColor;
			this.gridSize = gridSize;
		}

		public String getTitle() {
			return title;
		}

		public Image getHitSymbol() {
			return hitSymbol;
		}

		public String getHitSymbolString() {
			return hitSymbolString;
		}

		public Color getHitSymbolColor() {
			return hitSymbolColor;
		}

		public Dimension getGridSize() {
			return gridSize;
		}
	}
	
	protected static class HitPanelAndTitle extends JPanel {
		private static final long serialVersionUID = 1L;
		
		protected HitPanel hitPanel;
		
		protected JLabel titleLabel;
		
		public HitPanelAndTitle(HitPanelProperties panelProperties) {
			super(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0; gbc.gridy = 0;
			hitPanel = new HitPanel(panelProperties);
			hitPanel.setPreferredSize(UIStudyConstants.HIT_PANEL_SIZE);
			hitPanel.setFont(UIStudyConstants.FONT_HITS);
			add(hitPanel, gbc);
			
			titleLabel = new JLabel(panelProperties.title);
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			titleLabel.setFont(UIStudyConstants.FONT_PROBABILITY_TITLE);
			gbc.gridy++;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets.top = 3;
			add(titleLabel, gbc);
			
			//Add resize listener to keep hit panel square
			/*addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent ev) {
					Dimension size = getSize();
					int width = size.width;
					int height = size.height - 3 - titleLabel.getHeight();
					if(width != height) {
						Dimension newSize = null;
						if(width > height) {
							newSize = new Dimension(height, height);
						} else {
							newSize = new Dimension(width, width);	
						}
						//System.out.println("Sizing hit panel " + titleLabel.getText() + " : " + newSize);
						hitPanel.setPreferredSize(newSize);
						revalidate();
					}
				}				
			});*/
		}
		
		public void setHitPanelProperties(HitPanelProperties hitPanelProperties) {
			hitPanel.setHitPanelProperties(hitPanelProperties);
		}
		
		public void setTitle(String title) {
			titleLabel.setText(title);
		}
	}
	
	/**
	 * @author CBONACETO
	 *
	 */
	protected static class HitPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		protected Image hitSymbol;
		
		protected String hitSymbolString;
		
		protected Color hitSymbolColor;
		
		protected int gridWidth;
		
		protected int gridHeight;
		
		/** The locations of each hit */
		protected LinkedList<Point> hits;		
		
		public HitPanel(HitPanelProperties hitPanelProperties) {
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setBackground(Color.WHITE);
			hits = new LinkedList<Point>();
			setHitPanelProperties(hitPanelProperties);			
		}
		
		public void setHitPanelProperties(HitPanelProperties hitPanelProperties) {
			if(hitPanelProperties != null) {
				this.gridWidth = hitPanelProperties.gridSize.width;
				this.gridHeight = hitPanelProperties.gridSize.height;
				if(hitPanelProperties.hitSymbol != null) {
					this.hitSymbol = hitPanelProperties.hitSymbol;
					this.hitSymbolString = null;
					this.hitSymbolColor = null;
				} else {
					this.hitSymbol = null;
					this.hitSymbolString = hitPanelProperties.hitSymbolString;
					this.hitSymbolColor = hitPanelProperties.hitSymbolColor;
				}
			}
			repaint();
		}
		
		public void addHit(int x, int y) {
			hits.add(new Point(x, y));
			repaint();
		}
		
		public void addHit(Point hit) {
			hits.add(hit);
			repaint();
		}
		
		public void addHits(Collection<Point> hits) {
			this.hits.addAll(hits);
			repaint();
		}
		
		public void clearHits() {
			hits.clear();
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {			
			//Paint the background
			super.paintComponent(g);			
			
			if(!hits.isEmpty()) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				Insets insets = getInsets();
				double pixelsPerGridX = (getWidth()-insets.left-insets.right)/(double)gridWidth;
				double pixelsPerGridY = (getHeight()-insets.top-insets.bottom)/(double)gridHeight;
				
				double symbolWidth = 0;
				double symbolHeight = 0;
				if(hitSymbol == null) {
					g2d.setFont(getFont());
					g2d.setColor(hitSymbolColor);
					Rectangle2D strBounds = g2d.getFontMetrics().getStringBounds(hitSymbolString, g2d);
					symbolWidth = strBounds.getWidth();
					symbolHeight = strBounds.getHeight();
				} else {
					symbolWidth = hitSymbol.getWidth(null);
					symbolHeight = hitSymbol.getHeight(null);
				}
				//System.out.println(symbolWidth + ", " + symbolHeight);
				
				for(Point hit : hits) {
					//Translate point to panel coordinates
					double renderX = insets.left + (int)(hit.x * pixelsPerGridX);
					double renderY = insets.top + (int)((gridHeight - hit.y) * pixelsPerGridY);
					//System.out.println(renderX + ", " + renderY);
					//pixelsPerGridX = width_pixels/width_gridUnits 
					//return new Point2D.Double((gridLocation.x) * pixelsPerGridX, 
					//		(height_gridUnits - gridLocation.y) * pixelsPerGridY);
					
					if(hitSymbol == null) {
						g2d.drawString(hitSymbolString, (int)(renderX-symbolWidth/2), (int)(renderY+symbolHeight/2));
					} else {
						g2d.drawImage(hitSymbol, (int)(renderX-symbolWidth/2), (int)(renderY+symbolHeight/2), null);
					}
				}
			}
		}
	}
}
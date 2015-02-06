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
package org.mitre.icarus.cps.app.widgets.probability_entry.stacked_bars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilitySumChangeListener;
import org.mitre.icarus.cps.app.widgets.probability_entry.TimeData;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

import java.awt.Shape;

/**
 * @author Eric Kappotis
 *
 */
public class StackedBarPanel extends JPanelConditionComponent implements IProbabilityEntryContainer {
	private static final long serialVersionUID = -2447167510400737998L;
	
	private boolean editable = true;
	
	private int regions;
	private Insets insets;
	private Dimension preferredSize;
	private Dimension barSize;
	private Dimension dividerSize;
	boolean overDivider = false;
	private Divider selectedDivider = null;
	private Font labelFont;
	
	private int dividerExtension = 20;
	
	private double totalRegionHeight = 0;
	
	private List<String> entryTitles;
	private List<Integer> previousSettings;

	private List<Color> regionColors;
	
	private List<Region> regionList;
	private List<Divider> dividerList;
	private Color dividerBackgroundColor;
	private Color dividerForegroundColor;
	
	private Rectangle2D stackBarBounds;
	private Point2D legendKeyPoint = new Point2D.Double(20.0, 20.0);
	
	private boolean topTitleVisible;
	private String topTitle = "";
	private Font topTitleFont;
	
	private long dividerPressedStartTime = -1;
	
	private boolean settingCurrentSettings = false;	

	public StackedBarPanel(String componentId, int regions, Dimension barSize, Dimension dividerSize, List<Color> regionColors) {
		super(componentId);
		if(regionColors == null || regions != regionColors.size()) {
			throw new IllegalArgumentException("No region colors were assigned.");
		}
		if(regions < 2) {
			throw new IllegalArgumentException("There must be at least two regions");
		}
		
		topTitleFont = new JLabel().getFont();
		labelFont =  topTitleFont;
		this.regions = regions;
		this.insets = new Insets(10, 10, 10, 10);
		this.barSize = barSize;
		this.regionColors = regionColors;
		this.dividerSize = dividerSize;
		
		// Comment these out for now to see what happens
		
		//initializeLayout();
		//positionRegions();
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			
			/*
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if(!settingCurrentSettings && mouseEvent.getClickCount() == 2 && !mouseEvent.isConsumed()) {
					mouseEvent.consume();
					 //handle double click.
					
					// Iterate through the regions to see if one was double clicked.
					for(Region currRegion : regionList) {
						// if the x, y coordinate of the double click intersects with the
						// region bounds then the region was double clicked
						if(currRegion.bounds.contains(mouseEvent.getX(), mouseEvent.getY())) {
							//System.out.println("Region " + currRegion.toString() + " double clicked");
							currRegion.locked = !currRegion.locked;
						}
					}
				}
			}
			*/
			
			@Override
			public void mouseDragged(MouseEvent event) {
				if(!settingCurrentSettings && selectedDivider != null) {
					Rectangle2D updatedBounds = new Rectangle2D.Double(selectedDivider.bounds.getX(), 
							event.getY() - selectedDivider.bounds.getHeight() / 2.0, 
							selectedDivider.bounds.getWidth(), selectedDivider.bounds.getHeight());
					
					// find out which divider bar is being dragged
					int dividerBarIndex = dividerList.indexOf(selectedDivider);
					
					// if the top region is locked then the first divider should not
					// be able to move
					if(dividerBarIndex == 0 && selectedDivider.top.locked) {
						return;
					}					
					// if the bottom region is locked, then the last divider should not
					// be able to move
					else if(dividerBarIndex == dividerList.size() - 1 && selectedDivider.bottom.locked) {
						return;
					}					
					// right now do nothing
					//else {
						// if the region below the bottom divider is locked
						// then we want to move that divider as well
					//	if(selectedDivider.bottom.locked) {
							// get the divider below
					//		Divider dividerBelow = dividerList.get(dividerBarIndex + 1);
							
					//		Rectangle2D updatedBelowDividerBounds = 
					//				new Rectangle2D.Double(updatedBounds.getX(), );
					//	}
					//}							
					
					Shape topBorder = null;
					Shape bottomBorder = null;
					
					// The top border should take into account any locks that are
					// in place for a particular region
					
					double topBorderY = stackBarBounds.getY() + dividerBarIndex * StackedBarPanel.this.dividerSize.height;
					
					// iterate from this divider up adding in the height of any locked regions
					for(int index = dividerBarIndex; index >= 0; index--) {
						Divider divider = dividerList.get(index);
						
						if(divider.top.locked) {
							topBorderY += divider.top.bounds.getHeight();
						}
					}
					
					topBorder = new Line2D.Double(stackBarBounds.getX(), 
							topBorderY, 
							stackBarBounds.getX() + stackBarBounds.getWidth(), 
							topBorderY);
					
					int remainingDividers = dividerList.size() - dividerBarIndex - 1;
					
					double bottomBorderY = stackBarBounds.getY() + stackBarBounds.getHeight() - remainingDividers * StackedBarPanel.this.dividerSize.height;
					
					// subtract all the regions that are below the divider index from the bottom border y
					for(int index = dividerBarIndex; index < dividerList.size(); index++) {
						Divider divider = dividerList.get(index);
						
						if(divider.bottom.locked) {
							bottomBorderY = bottomBorderY - divider.bottom.bounds.getHeight();
						}
					}
					
					bottomBorder = new Line2D.Double(stackBarBounds.getX(),
							bottomBorderY,
							stackBarBounds.getX() + stackBarBounds.getWidth(),
							bottomBorderY);
					
					if(isAbove(updatedBounds, bottomBorder) && 
							isBelow(updatedBounds, topBorder)) {						
						selectedDivider.bounds = updatedBounds;
						
						// check to see if the region below is locked,
						// if it is, it needs to be adjusted accordingly
						Region bottomRegion = selectedDivider.bottom;
						
						if(bottomRegion.locked) {
							Divider belowDivider = dividerList.get(dividerBarIndex + 1);
							
							belowDivider.bounds = new Rectangle2D.Double(belowDivider.bounds.getX(), 
									updatedBounds.getY() + bottomRegion.bounds.getHeight() + belowDivider.bounds.getHeight(), 
									belowDivider.bounds.getWidth(), belowDivider.bounds.getHeight());
							positionBarsBelow(dividerBarIndex + 1);
						}
					}
					
					// the updated bounds is beyond the bottom border
					// so just place it at the bottom border
					if(isBelow(updatedBounds, bottomBorder)) {						
						selectedDivider.bounds = new Rectangle2D.Double(selectedDivider.bounds.getX(),
								bottomBorder.getBounds2D().getY() - selectedDivider.bounds.getHeight(), 
								selectedDivider.bounds.getWidth(), selectedDivider.bounds.getHeight());
					}
					
					// the updated bounds is beyond the top border
					// so just place it at the top border
					if(isAbove(updatedBounds, topBorder)) {
						selectedDivider.bounds = new Rectangle2D.Double(selectedDivider.bounds.getX(),
								topBorder.getBounds2D().getY(), selectedDivider.bounds.getWidth(),
								selectedDivider.bounds.getHeight());
					}					
					positionBarsBelow(dividerBarIndex);
					positionBarsAbove(dividerBarIndex);
					
					positionRegions();
				}
			}
		
			@Override
			public void mouseMoved(MouseEvent event) {
				if(!settingCurrentSettings) {
					for(Divider currDivider : dividerList) {
						if(currDivider.bounds.contains(event.getPoint())) {
							overDivider = true;
							selectedDivider = currDivider;
							break;
						}
						else {
							selectedDivider = null;
							overDivider = false;
						}
					}
					if(overDivider) {
						setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
					} else {
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				if(editable && overDivider && !settingCurrentSettings) {
					dividerPressedStartTime = System.currentTimeMillis();
				} else {
					dividerPressedStartTime = -1;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				if(editable && selectedDivider != null && dividerPressedStartTime > 0) {
					selectedDivider.dividerInteractionTime += System.currentTimeMillis() - dividerPressedStartTime;
					dividerPressedStartTime = -1;
					//System.out.println("updated divider " + selectedDivider + " time: " + selectedDivider.dividerInteractionTime);
				}
			}
			
			/*@Override
			public void mouseExited(MouseEvent mouseEvent) {				
				if(overDivider) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					overDivider = false;
					selectedDivider = null;
				}
			}*/	
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {			
				//if(!layoutInitialized) {
				initializeLayout();
				//}				
				positionRegions();
			}						
		});
	}
	
	private void initializeLayout() {
		//Dimension dividerSize = StackedBarPanel.this.dividerSize;
		Dimension stackBarSize = StackedBarPanel.this.barSize;

		double stackBarYLocation = insets.top;

		if(topTitleVisible && topTitle != null) {
			BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Rectangle2D titleBounds = dummyImage.getGraphics().getFontMetrics
					(getLabelFont()).getStringBounds(topTitle, dummyImage.getGraphics());
			stackBarYLocation += titleBounds.getHeight() + insets.bottom;
		}		
		
		stackBarBounds = new Rectangle2D.Double((getSize().width - stackBarSize.width) / 2.0, stackBarYLocation, stackBarSize.width, stackBarSize.height);
		
		//stackBarBounds = new Rectangle2D.Double(insets.left + dividerSize.width / 2 - stackBarSize.width / 2, 
		//		stackBarYLocation, stackBarSize.width, stackBarSize.height);	
	}
	
	private void positionBarsAbove(int barIndex) {
		
		//System.out.println("Position Bars Above Called");
		
		int updatedIndex = barIndex - 1;
		
		if(updatedIndex >= 0) {
			Divider bottomBar = dividerList.get(barIndex);
			Divider topBar = dividerList.get(updatedIndex);
			
			if(!isAbove(topBar.bounds, bottomBar.bounds)) {
				topBar.bounds = new Rectangle2D.Double(topBar.bounds.getX(),
						bottomBar.bounds.getY() - topBar.bounds.getHeight(),
						topBar.bounds.getWidth(), topBar.bounds.getHeight());
				
				positionBarsAbove(updatedIndex);
			}
		}		
	}
	
	private void positionBarsBelow(int barIndex) {
		int updatedIndex = barIndex + 1;
		
		if(updatedIndex <= dividerList.size() - 1) {
			Divider topBar = dividerList.get(barIndex);
			Divider bottomBar = dividerList.get(updatedIndex);
			
			if(!isBelow(bottomBar.bounds, topBar.bounds)) {	
				bottomBar.bounds = new Rectangle2D.Double(bottomBar.bounds.getX(), topBar.bounds.getY() + topBar.bounds.getHeight(),
						bottomBar.bounds.getWidth(), bottomBar.bounds.getHeight());
				
				positionBarsBelow(updatedIndex);
			}			
		} 
	}
	
	private boolean isAbove(Rectangle2D bounds, Shape border) {
		if((bounds.getY() + bounds.getHeight()) < border.getBounds2D().getY()) {
			return true;
		}
		return false;
	}
	
	private boolean isBelow(Rectangle2D bounds, Shape border) {
		
		Rectangle2D borderBounds = border.getBounds2D();
		
		double bottomBorderBounds = borderBounds.getY() + borderBounds.getHeight();
		
		if(bounds.getY() > bottomBorderBounds) {
			return true;
		}
		return false; 
	}
	
	@Override
	public void setTopTitleAndSumFont(Font font) {
		this.topTitleFont = font;
		initializeLayout();
		repaint();
	}

	@Override
	public void setProbabilityEntryFont(Font font) {
		this.labelFont = font;
		initializeLayout();
		repaint();
	}
	
	@Override
	public void setProbabilityEntryTitleFont(Font font) {
		this.labelFont = font;
		initializeLayout();
		repaint();
	}
	
	/**
	 * @return the labelFont
	 */
	public Font getLabelFont() {
		if(labelFont == null) {
			labelFont = new JLabel().getFont();
		}
		return labelFont;
	}	
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		if(preferredSize == null) {
			// create a fake buffered image to get its graphics context
			BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			
			int maxFontSize = dummyImage.getGraphics().getFontMetrics(getLabelFont()).stringWidth("100%");
			int dimensionWidth = this.dividerSize.width + insets.left + insets.right + (int)legendKeyPoint.getX() + insets.right + maxFontSize + insets.right;
			int dimensionHeight = this.barSize.height + insets.top + insets.bottom;
			
			if(entryTitles != null && !entryTitles.isEmpty()) {				
				int maxTitleWidth = 0;
				FontMetrics titleFontMetrics = dummyImage.getGraphics().getFontMetrics(getLabelFont());
				// calculate the max width of the entry titles
				for(String currTitle : entryTitles) {
					if(titleFontMetrics.stringWidth(currTitle) > maxTitleWidth) {
						maxTitleWidth = titleFontMetrics.stringWidth(currTitle);
					}
				}
				
				dimensionWidth += maxTitleWidth + insets.right;
			}			
			if(topTitle != null && isTopTitleVisible()) {
				Rectangle2D titleBounds = dummyImage.getGraphics().getFontMetrics(getLabelFont()).getStringBounds(topTitle, dummyImage.getGraphics());
				dimensionHeight += titleBounds.getBounds().height + insets.bottom;
			}		
			return new Dimension(dimensionWidth + 100, dimensionHeight);
		} else {
			return this.preferredSize;
		}
	}
	
	private void positionRegions() {		
		// if the region list is null, that means this is the first time
		// so set all the region heights to be the same
		if(regionList == null) {
			regionList = new ArrayList<Region>();
			
			// calculate the initial value of height of each region
			// calculate the total size the dividers will take up
			double totalDividerHeight = (regions - 1) * dividerSize.height;
			
			totalRegionHeight = (double)barSize.height - totalDividerHeight;
			
			double regionHeight = ((double)barSize.height - totalDividerHeight) / (double)regions;
			
			// set up the iterator to set the color for each region
			Iterator<Color> regionColorIter = regionColors.iterator();
			
			double regionYLocation = stackBarBounds.getY();
			
			Divider currDivider = null;
			
			for(int x = 0; x < regions; x++) {
				Rectangle2D regionRect = new Rectangle2D.Double(stackBarBounds.getX(), regionYLocation, 
						barSize.width, regionHeight);
				Region region = new Region();
				region.bounds = regionRect;
				region.regionColor = regionColorIter.next();
				regionList.add(region);
				
				// case where we have the first divider
				if(x == 0) {
					dividerList = new ArrayList<Divider>();
					currDivider = new Divider();
					currDivider.size = new Dimension(dividerSize.width, dividerSize.height);

					dividerList.add(currDivider);
					currDivider.top = region;
				}
				// the last region
				else if(x == (regions - 1)) {
					currDivider.bottom = region;
					
					//System.out.println(getBounds());
					
					currDivider.bounds = new Rectangle2D.Double((getSize().width - currDivider.size.width) / 2, 
							region.bounds.getY() - dividerSize.height, currDivider.size.getWidth(), dividerSize.height);
					
				}
				// any other region
				else {
					currDivider.bottom = region;
					
					//currDivider.bounds = new Rectangle2D.Double(insets.left, 
					//		region.bounds.getY() - dividerSize.height, dividerSize.width, dividerSize.height);
					currDivider.bounds = new Rectangle2D.Double((getSize().width - currDivider.size.width) / 2, 
							region.bounds.getY() - dividerSize.height, currDivider.size.getWidth(), dividerSize.height);
					
					currDivider = new Divider();
					currDivider.size = new Dimension(dividerSize.width + dividerExtension * x, dividerSize.height);

					dividerList.add(currDivider);
					currDivider.top = region;
				}				
				regionYLocation += regionHeight + dividerSize.height;
				
				// calculate what the percentage is for the region
				region.percent = Math.round(region.bounds.getHeight() / totalRegionHeight * 100);
			}			
		}		
		// case where everything has already been laid out at least once
		else {					
			Iterator<Divider> dividerIter = dividerList.iterator();
			Divider currDivider = null;
			
			for(Region region : regionList) {
				region.bounds = new Rectangle2D.Double();
				
				if(dividerIter.hasNext()) {
					// if currDivider is null we are at the first
					// item so it starts from the top
					if(currDivider == null) {
						currDivider = dividerIter.next();
						
						region.bounds = new Rectangle2D.Double(stackBarBounds.getX(), stackBarBounds.getY(),
								stackBarBounds.getWidth(), currDivider.bounds.getY() - stackBarBounds.getY());
						
					} else {
						Point2D previousBarEndLocation = new Point2D.Double(stackBarBounds.getX(), 
								currDivider.bounds.getY() + currDivider.bounds.getHeight());
						
						// update the divider bar
						currDivider = dividerIter.next();
						
						region.bounds = new Rectangle2D.Double(previousBarEndLocation.getX(), previousBarEndLocation.getY(),
								stackBarBounds.getWidth(), currDivider.bounds.getY() - previousBarEndLocation.getY());
					}
				}
				else {
					Point2D previousBarEndLocation = new Point2D.Double(stackBarBounds.getX(), 
							currDivider.bounds.getY() + currDivider.bounds.getHeight());
					
					region.bounds = new Rectangle2D.Double(previousBarEndLocation.getX(), previousBarEndLocation.getY(),
							stackBarBounds.getWidth(), stackBarBounds.getY() + stackBarBounds.getHeight() - previousBarEndLocation.getY());
				}
				
				// calculate what the percentage is for the region
				if(selectedDivider == null) {
					region.percent = Math.round(region.bounds.getHeight() / totalRegionHeight * 100);
				}
				//region.percent = Math.round(region.bounds.getHeight() / totalRegionHeight * 100);				
			}			
			
			//Region topManipRegion = null;
			//Region bottomManipRegion = null;
			
			// find the top manipulated region
			
			if(selectedDivider != null) {
			
				Region topRegion = selectedDivider.top;
				int topRegionIndex = regionList.indexOf(topRegion);
				
				while(Math.round(topRegion.bounds.getHeight() / totalRegionHeight * 100) == 0) {
					//System.out.println("top region was 0, going to next");
					topRegion.percent = 0;
					topRegionIndex = topRegionIndex - 1;
					
					if(topRegionIndex < 0) {
						break;
					}
					topRegion = regionList.get(topRegionIndex);			
				}
				
				// find the bottom manipulated region
				Region bottomRegion = selectedDivider.bottom;
				int bottomRegionIndex = regionList.indexOf(bottomRegion);
				
				while(Math.round(bottomRegion.bounds.getHeight() / totalRegionHeight * 100) == 0) {
					bottomRegion.percent = 0;
					bottomRegionIndex = bottomRegionIndex + 1;
					
					if(bottomRegionIndex > regionList.size() - 1) {
						break;
					}
					bottomRegion = regionList.get(bottomRegionIndex);
				}
				
				//regionList.indexOf(bottomRegion);
				
				//while(Math.round(bottomRegion.percent) == 0) {
				//	
				//}
				
				long percentSum = 0;
				for(Region region: regionList) {
					if(region != topRegion && region != bottomRegion) {
						percentSum += region.percent;
					}
				}
				
				topRegion.percent = Math.round(topRegion.bounds.getHeight() / totalRegionHeight * 100);			
				bottomRegion.percent = 100 - percentSum - topRegion.percent;
			}	
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(regionList == null) {
			return;
		}
		
		Rectangle bounds = this.getBounds();
		
		Graphics2D g2d = (Graphics2D)g;
		Color origColor = g2d.getColor();
		Stroke origStroke = g2d.getStroke();
		
		g2d.setColor(this.getBackground());
		g2d.fill(bounds);
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw the title if one exists
		if(topTitleVisible && topTitle != null) {
			// draw the string in the middle of the list bounds
			g2d.setFont(topTitleFont);
			int titleWidth = g2d.getFontMetrics().stringWidth(topTitle);
			g2d.setColor(Color.BLACK);
			//g2d.drawString(topTitle, (int)(stackBarBounds.getX() + stackBarBounds.getWidth() / 2.0 - titleWidth / 2.0), 
			//		bounds.y + insets.top + g2d.getFontMetrics().getAscent());
			g2d.drawString(topTitle, (int)(stackBarBounds.getX() + stackBarBounds.getWidth() / 2.0 - titleWidth / 2.0), 
					bounds.y + insets.top + g2d.getFontMetrics().getAscent()/2);//+ g2d.getFontMetrics().getAscent());
		}
		
		//int renderBarLocationX = bounds.width / 2 - barSize.width / 2;
		
		// render the regions		
		for(Region currRegion : regionList) {
			g2d.setColor(currRegion.regionColor);
			g2d.fill(currRegion.bounds);
		}
		
		g2d.setColor(Color.BLACK);
		//Rectangle2D renderBar = new Rectangle2D.Double(renderBarLocationX, insets.top, barSize.width, barSize.height);
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(stackBarBounds);
		//g2d.draw(renderBar);
		
		g2d.setStroke(new BasicStroke(1));		
		
		for(Divider currDivider : dividerList) {
			if(dividerBackgroundColor != null) {
				g2d.setColor(dividerBackgroundColor);
			}
			else {
				g2d.setColor(Color.GRAY);
			}
			g2d.fill(currDivider.bounds);
			if(dividerForegroundColor != null) {
				g2d.setColor(dividerForegroundColor);
			}
			else {
				g2d.setColor(Color.BLACK);
			}
			g2d.draw(currDivider.bounds);
		}
		
		// draw the legend displaying percentages
		g2d.setFont(labelFont);
		
		// start the legend next to the longest divider bar
		
		Divider lastDivider = dividerList.get(dividerList.size() - 1);
		
		//double legendXLocation = insets.left + dividerSize.width + insets.right;
		double legendXLocation = lastDivider.bounds.getX() + lastDivider.bounds.getWidth() + insets.right - 5;
		double legendYLocation = stackBarBounds.getY();
		
		Iterator<Region> regionsIter = regionList.iterator();
		Iterator<String> entryTitleIter = null;
		
		if(entryTitles != null && !entryTitles.isEmpty()) {
			entryTitleIter = entryTitles.iterator();
		}
		
		for(Color currColor : regionColors) {			
			g2d.setColor(currColor);
			
			Rectangle2D legendKeyBounds = new Rectangle2D.Double(legendXLocation, legendYLocation, legendKeyPoint.getX(), legendKeyPoint.getY());
			
			int fontAscentHeight = g2d.getFontMetrics().getAscent();
			int fontYLocation = legendKeyBounds.getBounds().y + (int)(legendKeyPoint.getY() / 2 - fontAscentHeight / 2) + fontAscentHeight;
			
			g2d.fill(legendKeyBounds);
			g2d.setColor(Color.BLACK);
			g2d.draw(legendKeyBounds);
			
			// get the next region
			Region region = regionsIter.next();
			
			int currXRenderLocation = (int)(legendXLocation + legendKeyPoint.getX() + insets.left);
			
			if(entryTitleIter != null) {
				String entryTitle = entryTitleIter.next();
				g2d.drawString(entryTitle, currXRenderLocation, fontYLocation);				
				currXRenderLocation += g2d.getFontMetrics().stringWidth(entryTitle) + insets.right;
			}
			
			g2d.drawString(region.percent + "%", currXRenderLocation, fontYLocation);
			
			legendYLocation += legendKeyPoint.getY() + insets.bottom;			
		}
		
		// set the graphics params back to what they were originally
		g2d.setColor(origColor);
		g2d.setStroke(origStroke);
	}

	/**
	 * @return the regions
	 */
	public int getRegions() {
		return regions;
	}

	/**
	 * @return the insets
	 */
	public Insets getInsets() {
		return insets;
	}

	/**
	 * @param insets the insets to set
	 */
	public void setInsets(Insets insets) {
		this.insets = insets;
	}
	
	protected class Region {
		protected Rectangle2D bounds;
		protected Color regionColor;
		protected long percent;
		
		/* The flag denoting whether or not a region is locked */
		protected boolean locked = false;
	}
	
	protected class Divider {
		protected Region top;
		protected Region bottom;
		protected Rectangle2D bounds;
		
		/** Total time spent interacting with the divider (e.g., dragging it) */
		protected long dividerInteractionTime = 0;
		protected Dimension size;
	}	

	@Override
	public String getComponentId() {
		return this.getComponentId();
	}

	@Override
	public void setComponentId(String id) {
		setComponentId(id);
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public List<String> getProbabilityEntryTitles() {
		return this.entryTitles;
	}

	@Override
	public void setProbabilityEntryTitles(List<String> titles) {
		this.entryTitles = titles;
		repaint();
	}	

	@Override
	public String getProbabilityEntryTitle(int index) {
		return entryTitles.get(index);
	}

	@Override
	public void setProbabilityEntryTitle(int index, String title) {
		entryTitles.set(index, title);
		repaint();
	}

	@Override
	public Icon getProbabilityEntryTitleIcon(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProbabilityEntryTitleIcon(int index, Icon icon) {
		// TODO Auto-generated method stub		
	}

	@Override
	public Color getProbabilityEntryTitleColor(int index) {
		return regionColors.get(index);
	}

	@Override
	public void setProbabilityEntryTitleColor(int index, Color color) {
		regionColors.set(index,  color);
		repaint();
	}
	

	@Override
	public Color getProbabilityEntryColor(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProbabilityEntryColor(int index, Color color) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void restoreDefaultProbabilityEntryColors() {
		// TODO Auto-generated method stub 
	}

	@Override
	public boolean isTopTitleVisible() {
		return topTitleVisible;
	}

	@Override
	public void setTopTitleVisible(boolean visible) {
		if(visible != this.topTitleVisible) {
			this.topTitleVisible = visible;
			initializeLayout();
		}
	}

	@Override
	public void setTopTitle(String title) {
		this.topTitle = title;
		if(topTitleVisible) {
			initializeLayout();
		}
	}

	@Override
	public void showConfirmedProbabilities() {		
		editable = false;
		if(overDivider) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			overDivider = false;
			selectedDivider = null;
		}
	}

	@Override
	public void showEditableProbabilities() {	
		editable = true;
	}

	@Override
	public boolean isSumVisible() {
		// the sum is always 100%
		return true;
	}

	@Override
	public void setSumVisible(boolean sumVisible) {
		// the sum is always 100%
	}
	
	@Override
	public Integer getSumErrorThreshold() {
		// the sum is always 100%
		return null;
	}

	@Override
	public void setSumErrorThreshold(Integer threshold) {
		// the sum is always 100%
	}

	@Override
	public void sumChanged() {
		// the sum is always 100%
	}

	@Override
	public boolean isAutoNormalize() {
		// in a stacked bar this always returns true
		return true;
	}

	@Override
	public void setAutoNormalize(boolean autoNormalize) {
		// just ignore because this should always be true for this
		// type of input method
	}

	@Override
	public void resetInteractionTimes() {
		if(dividerList != null ) {
			for(Divider divider : dividerList) {
				divider.dividerInteractionTime = 0;
			}
		}
	}

	@Override
	public List<TimeData> getDetailedTimeData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> getInteractionTimes() {
		if(dividerList != null ) {
			ArrayList<Long> interactionTimes = new ArrayList<Long>(dividerList.size());
			for(Divider divider : dividerList) {
				interactionTimes.add(divider.dividerInteractionTime);
			}
			return interactionTimes;
		}
		return null;
	}

	@Override
	public void getInteractionTimes(List<Long> interactionTimes) {
		if(dividerList != null ) {
			int i = 0;
			for(Divider divider : dividerList) {
				interactionTimes.set(i, divider.dividerInteractionTime);
				i++;
			}
		}
	}

	@Override
	public List<Integer> getCurrentSettings() {
		ArrayList<Integer> currentSettings = new ArrayList<Integer>();		
		for(Region currRegion : regionList) {
			currentSettings.add((int)currRegion.percent);
		}		
		return currentSettings;
	}

	@Override
	public void getCurrentSettings(List<Integer> currentSettings) {
		if(currentSettings == null) {
			currentSettings = new ArrayList<Integer>();
		} else {
			currentSettings.clear();
		}
		for(Region currRegion : regionList) {
			currentSettings.add((int)currRegion.percent);
		}
	}

	@Override
	public void setCurrentSettings(List<Integer> currentSettings) {
		settingCurrentSettings = true;
		selectedDivider = null;
		overDivider = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		// check to make sure that the settings add up to 100
		int sum = 0;
		for(Integer i : currentSettings) {
			sum += i;
		}
		if(sum != 100) {
			settingCurrentSettings = false; 
			throw new IllegalArgumentException("The sum of the settings does not equal 100");
		}		

		try {
			double currentYPosition = 0.0;		
			int currentSettingsCounter = 0;		
			if(dividerList != null) {
				for(Divider currDivider : dividerList) {			
					// calculate what the height of the preceding region should be
					double aboveRegionHeight = ((double)currentSettings.get(currentSettingsCounter) / 100.0) * totalRegionHeight;
					currentYPosition += aboveRegionHeight;

					Rectangle2D origBounds = currDivider.bounds;
					currDivider.bounds = new Rectangle2D.Double(origBounds.getX(), 
							stackBarBounds.getY() + currentYPosition, origBounds.getWidth(),
							origBounds.getHeight());

					currentYPosition += origBounds.getHeight();			
					currentSettingsCounter++;
				}
				positionRegions();
				repaint();
			}	
		} catch(Exception ex) {
			settingCurrentSettings = false;
		}
		settingCurrentSettings = false; 
	}

	@Override
	public List<Integer> getPreviousSettings() {
		return this.previousSettings;
	}

	@Override
	public void getPreviousSettings(List<Integer> previousSettings) {
		previousSettings = this.previousSettings;
	}

	@Override
	public void setPreviousSettings(List<Integer> previousSettings) {
		this.previousSettings = previousSettings;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().setLayout(new FlowLayout());
		
		List<Color> regionColors = new ArrayList<Color>();
		regionColors.add(new Color(255, 102, 51));
		regionColors.add(new Color(0, 184, 245));
		regionColors.add(new Color(61, 245, 0));
		regionColors.add(new Color(219, 112, 255));
		
		final StackedBarPanel panel = new StackedBarPanel("TestId", 4, new Dimension(70, 200), new Dimension(100, 5), regionColors);
		panel.setTopTitle("Test this thing out");
		panel.setTopTitleVisible(true);
		
		ArrayList<String> entryTitles = new ArrayList<String>();
		entryTitles.add("A");
		entryTitles.add("B");
		entryTitles.add("C");
		entryTitles.add("D");
		
		panel.setProbabilityEntryTitles(entryTitles);
		
		frame.getContentPane().add(panel);		
		
		//JButton button = new JButton("Test");
		//button.addActionListener(new ActionListener() {
		//	@Override
		//	public void actionPerformed(ActionEvent arg0) {
		//		ArrayList<Integer> settings = new ArrayList<Integer>();
		//		settings.add(15);
		//		settings.add(15);
		//		settings.add(30);
		//		settings.add(40);
		//		
		//		panel.setCurrentSettings(settings);
		//	}			
		//});
		//frame.getContentPane().add(button);
		
		frame.pack();
		frame.setVisible(true);
	}

	//@Override
	//public void setAutoLockAfterAdjust(boolean autoLock) {
		// FEATURE NOT SUPPORTED IN THIS COMPONENT
	//}

	//@Override
	//public boolean isAutoLockAfterAdjust() {
		// FEATURE NOT SUPPORTED IN THIS COMPONENT
	//	return false;
	//}

	@Override
	public void setEnableLocking(boolean enableLocking) {
		// FEATURE NOT SUPPORTED IN THIS COMPONENT		
	}

	@Override
	public boolean isEnableLocking() {
		// FEATURE NOT SUPPORTED IN THIS COMPONENT
		return false;
	}	
	
	@Override
	public ArrayList<Boolean> getLockSettings() {
		// FEATURE NOT SUPPORTED IN THIS COMPONENT
		return null;
	}

	@Override
	public void getLockSettings(List<Boolean> lockSettings) {
		// FEATURE NOT SUPPORTED IN THIS COMPONENT
	}

	@Override
	public void unlockAllProbabilities() {
		// FEATURE NOT SUPPORTED IN THIS COMPONENT
	}
	
	@Override
	public boolean isSumChangeListenerPresent(ProbabilitySumChangeListener listener) {
		//Always returns false
		return false;
	}

	@Override
	public void addSumChangeListener(ProbabilitySumChangeListener listener) {
		//Does nothing, sum is always 100%
	}
	
	@Override
	public void removeSumChangeListener(ProbabilitySumChangeListener listener) {
		//Does nothing, sum is always 100%
	}
	

	/**
	 * @return the dividerExtension
	 */
	public int getDividerExtension() {
		return dividerExtension;
	}

	/**
	 * @param dividerExtension the dividerExtension to set
	 */
	public void setDividerExtension(int dividerExtension) {
		this.dividerExtension = dividerExtension;
	}

	@Override
	public boolean isAllDisplayValuesValid() {
		//Always return true
		return true;
	}
}
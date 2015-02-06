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
package org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * Contains 1 or more boxes.
 * 
 * @author cbonaceto
 *
 */
public class BoxContainer extends JPanel implements IConditionComponent {
	
	private static final long serialVersionUID = 1L;

	/** Orientation constants for how the boxes will be laid out */
	public static enum BoxOrientation {CROSS, HORIZONTAL_LINE, GRID};
	
	/** The sub-panel containing the boxes */
	protected JPanel boxPanel;
	
	/** The boxes */
	protected ArrayList<? extends BoxWithDecorators> boxes;
	
	/** The orientation of the boxes */
	protected final BoxOrientation orientation;
	
	/** The top title label */
	protected JLabel topTitleLabel;
	
	/** The bottom title label */
	protected JLabel bottomTitleLabel;	
	
	protected BoxContainer(BoxOrientation orientation, String componentId) {
		this(orientation, null, componentId);
	}
	
	public BoxContainer(BoxOrientation orientation, ArrayList<? extends BoxWithDecorators> boxes) {
		this(orientation, boxes, "boxContainer");
	}
	
	public BoxContainer(BoxOrientation orientation, ArrayList<? extends BoxWithDecorators> boxes, String componentId) {
		//setBackground(Color.black);
		this.orientation = orientation;
		setName(componentId);
		setLayout(new GridBagLayout());		
		boxPanel = new JPanel();
		boxPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;		
		add(boxPanel, gbc);		
		if(boxes != null) {			
			setBoxes(boxes);
		}		
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(boxPanel != null) {
			boxPanel.setBackground(bg);
		}
	}	
	
	/**
	 * Get whether the top title is visible;
	 * 
	 * @return whether the top title is visible
	 */
	public boolean isTopTitleVisible() {
		return (topTitleLabel != null && topTitleLabel.isVisible());
	}
	
	/**
	 * Set whether the top title is visible;
	 * 
	 * @param visible
	 */
	public void setTopTitleVisible(boolean visible) {
		if(visible != isTopTitleVisible()) {
			if(visible && topTitleLabel == null) {
				topTitleLabel = new JLabel();
				topTitleLabel.setHorizontalAlignment(JLabel.CENTER);
				topTitleLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;	
				gbc.gridy = 0;
				gbc.insets.top = 3;
				gbc.insets.bottom = 7;
				gbc.anchor = GridBagConstraints.CENTER;			
				add(topTitleLabel, gbc);
			}
			topTitleLabel.setVisible(visible);
			revalidate();
			repaint();
		}
	}

	/** Set a top title for the box container */
	public void setTopTitle(String title) {
		if(topTitleLabel == null) {
			topTitleLabel = new JLabel();
			topTitleLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);
			topTitleLabel.setVisible(false);
		}		
		topTitleLabel.setText(title);
	}
	
	/** Set a bottom title for the box container */
	public void setBottomTitle(String title) {
		this.setBottomTitle(title, null);
	}
	
	public void setBottomTitle(String title, Boolean titleBorderVisible) {
		if(bottomTitleLabel == null) {
			bottomTitleLabel = new JLabel();
			bottomTitleLabel.setFont(ProbabilityEntryConstants.FONT_TITLE);			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;	
			gbc.gridy = 2;
			gbc.insets.top = 7;	
			gbc.insets.bottom = 3;
			gbc.insets.left = 5;
			gbc.anchor = GridBagConstraints.CENTER;
			add(bottomTitleLabel, gbc);
			revalidate();
			repaint();
		}		
		bottomTitleLabel.setText(title);
		if(titleBorderVisible != null) {
			if(titleBorderVisible) {
				if(bottomTitleLabel.getBorder() == null) {			
					bottomTitleLabel.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createLineBorder(getForeground()), 
							BorderFactory.createEmptyBorder(2, 3, 2, 3)));
				}
			} else {
				bottomTitleLabel.setBorder(null);
			}
		}
	}
					
	protected void setBoxes(ArrayList<? extends BoxWithDecorators> boxes) {		
		if(orientation == BoxOrientation.CROSS) {
			if(boxes.size() != 4) {
				throw new IllegalArgumentException("A BoxContainer with CROSS orientation must contain exactly 4 boxes.");
			}
		}
		else if(orientation == BoxOrientation.GRID) {
			if(boxes.size() < 1 || boxes.size() > 4) {
				throw new IllegalArgumentException("A BoxContainer with GRID orientation must contain 1-4 boxes.");
			}
		}
		else {
			if(boxes.size() < 1) {
				throw new IllegalArgumentException("A BoxContainer with HORIZONTATL orientation must contain at least 1 box.");
			}
		}
		
		this.boxes = boxes;
		GridBagConstraints gbc = new GridBagConstraints();
		if(boxPanel == null) {
			boxPanel = new JPanel();
			boxPanel.setLayout(new GridBagLayout());			
			gbc.gridx = 0;
			gbc.gridy = 1;
			add(boxPanel, gbc);
		}		
		boxPanel.removeAll();
		
		//Lay out the boxes
		gbc.gridx = 0;
		gbc.gridy = 0;
		if(orientation != BoxOrientation.CROSS) {
			gbc.anchor = GridBagConstraints.NORTH;
			//gbc.insets.left = 5;
		}		
		
		for(int i=0; i<boxes.size(); i++) {
			if(orientation == BoxOrientation.CROSS) {
				switch(i) {
				case ProbabilityContainerLayout.NORTH:
					gbc.gridx = 1;
					gbc.gridy = 0;	
					gbc.anchor = GridBagConstraints.SOUTH;
					break;
				case ProbabilityContainerLayout.SOUTH:
					gbc.gridx = 1;
					gbc.gridy = 2;	
					gbc.anchor = GridBagConstraints.NORTH;
					break;
				case ProbabilityContainerLayout.EAST:
					gbc.gridx = 2;
					gbc.gridy = 1;
					gbc.anchor = GridBagConstraints.WEST;
					break;
				case ProbabilityContainerLayout.WEST:
					gbc.gridx = 0;
					gbc.gridy = 1;
					gbc.anchor = GridBagConstraints.EAST;
					break;
				}
			}
			else {
				gbc.gridx++;				
				if (orientation == BoxOrientation.GRID && i == 2) {
					gbc.gridx = 1;
					gbc.gridy = 1;
				}
			}
			boxPanel.add(boxes.get(i), gbc);
			if(orientation != BoxOrientation.CROSS) {
				gbc.insets.left = 5;
			}
		}
		
		boxPanel.revalidate();
		revalidate();
		repaint();
	}	
	

	public ArrayList<? extends BoxWithDecorators> getBoxes() {
		return boxes;
	}
	
	public void setBoxFillColor(Color color) {
		if(boxes != null) {
			for(BoxWithDecorators box : boxes) {
				box.getBox().setBoxFillColor(color);
			}
		}
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public String getComponentId() {
		return this.getName();
	}	
	
	public void setComponentId(String componentId) {
		this.setName(componentId);
	}
}
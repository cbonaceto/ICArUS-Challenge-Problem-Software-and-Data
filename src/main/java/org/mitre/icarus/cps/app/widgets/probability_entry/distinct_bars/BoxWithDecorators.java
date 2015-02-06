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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.util.WidgetUtils;

/**
 * A box that may have 0-4 decorator components oriented to the east, west, north, and south of the box.
 * 
 * @author cbonaceto
 *
 */
public class BoxWithDecorators extends JPanel {
	private static final long serialVersionUID = 1L;	
	
	/** Array containing components positioned at each orientation */
	protected ArrayList<JComponent> decoratorComponents;
	
	/** The box component */
	protected Box box;
		
	/** The title of the box (if any) */
	protected String title;
	
	/** The title label */
	protected JLabel titleLabel;	
	
	/** The orientation of the tile */
	protected int titleOrientation = ProbabilityContainerLayout.SOUTH;	
	
	/** Whether to show the title */
	protected boolean titleVisible;	
	
	GridBagConstraints gbc;
	
	public BoxWithDecorators(Box box) {
		super();
		this.box = box;
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 1;
		gbc.gridy = 1;
		//box = new Box(boxRenderer);		
		//box.setBounds(new Rectangle(0, 0, 50, 50));
		//box.setPreferredSize(new Dimension(50, 50));
		add(box, gbc);		
		
		decoratorComponents = new ArrayList<JComponent>(4);
		for(int i=0; i<4; i++) {
			decoratorComponents.add(null);
		}
	}
	
	public Box getBox() {
		return box;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if(titleLabel != null) {
			titleLabel.setText(title);
			titleLabel.setPreferredSize(new Dimension(box.getPreferredSize().width + 6,
					titleLabel.getPreferredSize().height));
			revalidate();
			repaint();
		}
	}
	
	public Icon getTitleIcon() {
		if(titleLabel != null) {
			return titleLabel.getIcon();
		}
		return null;
	}
	
	public void setTitleIcon(Icon icon) {
		if(titleLabel == null) {
			createTitleLabel();
		}
		titleLabel.setIcon(icon);
		titleLabel.setPreferredSize(new Dimension(box.getPreferredSize().width + 6,
				titleLabel.getPreferredSize().height));
		revalidate();
		repaint();
	}
	
	public Color getTitleColor() {
		if(titleLabel != null) {
			return titleLabel.getForeground();
		}
		return null;
	}
	
	public void setTitleColor(Color color) {
		if(titleLabel == null) {
			createTitleLabel();
		}
		titleLabel.setForeground(color);
	}

	public int getTitleOrientation() {
		return titleOrientation;
	}

	public void setTitleOrientation(int titleOrientation) {
		if(this.titleOrientation != titleOrientation) {
			moveDecoratorComponent(this.titleOrientation, titleOrientation);
			this.titleOrientation = titleOrientation;
		}
	}

	public boolean isTitleVisible() {
		return titleVisible;
	}

	public void setTitleVisible(boolean titleVisible) {
		if(titleVisible != this.titleVisible) {
			this.titleVisible = titleVisible;
			
			JComponent titleComponent = decoratorComponents.get(titleOrientation);
			if(titleVisible) {
				if(titleComponent == null || !(titleComponent instanceof JLabel)) {					
					if(titleLabel == null) {
						createTitleLabel();
					}					
					titleComponent = titleLabel;
					decoratorComponents.set(titleOrientation, titleComponent);
					titleComponent.setPreferredSize(new Dimension(box.getPreferredSize().width + 6,
							titleLabel.getPreferredSize().height));
				}
				setGridCoordinates(titleOrientation);
				add(titleComponent, gbc);				
			}
			else {
				if(titleComponent != null) {
					remove(titleComponent);
				}
			}
			revalidate();
			repaint();
		}
	}
	
	public void setTitleFont(Font font) {
		if(titleLabel != null) {
			titleLabel.setFont(font);
		}
	}
	
	/** Set the size of the box part of the component */
	public void setBoxSize(int width, int height) {
		box.setPreferredSize(new Dimension(width, height));
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return box.isEnabled();
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		box.setEnabled(enabled);
	}	
	
	protected void createTitleLabel() {
		//TODO: Compute characters per line instead of hard-coding at 10
		titleLabel = new JLabel(
				WidgetUtils.formatMultilineString(title, 10, WidgetUtils.CENTER));
		titleLabel.setHorizontalTextPosition(SwingConstants.LEADING);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setFont(ProbabilityEntryConstants.FONT_PROBABILITY_TITLE);
		titleLabel.setText(title);
		titleLabel.setPreferredSize(new Dimension(box.getPreferredSize().width + 6,
				titleLabel.getPreferredSize().height));
	}
	
	protected void addDecoratorComponent(JComponent component, int orientation) {
		JComponent existingComponent = decoratorComponents.get(orientation);
		if(existingComponent != component) {
			if(existingComponent != null) {
				remove(existingComponent);
			}
			setGridCoordinates(orientation);
			decoratorComponents.set(orientation, component);
			add(component, gbc);
			revalidate();
			repaint();
		}
	}
	
	protected void removeDecoratorComponent(int orientation) {
		JComponent component = decoratorComponents.get(orientation);
		if(component != null) {
			decoratorComponents.set(orientation, null);
			remove(component);
			revalidate();
			repaint();
		}
	}
	
	protected void moveDecoratorComponent(int oldOrientation, int newOrientation) {
		JComponent component = decoratorComponents.get(oldOrientation);
		if(component != null) {
			remove(component);
			setGridCoordinates(newOrientation);
			add(component, gbc);
			revalidate();
			repaint();
		}
	}
	
	protected void setGridCoordinates(int orientation) {
		switch(orientation) {
		case ProbabilityContainerLayout.NORTH:
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.insets.left = 0;
			gbc.insets.right = 0;
			gbc.insets.top = 0;
			gbc.insets.bottom = 2;
			gbc.anchor = GridBagConstraints.CENTER;
			break;
		case ProbabilityContainerLayout.SOUTH:
			gbc.gridx = 1;
			gbc.gridy = 2;
			gbc.insets.left = 0;
			gbc.insets.right = 0;
			gbc.insets.top = 2;
			gbc.insets.bottom = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			break;
		case ProbabilityContainerLayout.EAST:
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.insets.left = 4;
			gbc.insets.right = 0;
			gbc.insets.top = 0;
			gbc.insets.bottom = 0;
			gbc.anchor = GridBagConstraints.WEST;
			break;
		case ProbabilityContainerLayout.WEST:
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.insets.left = 0;
			gbc.insets.right = 4;
			gbc.insets.top = 0;
			gbc.insets.bottom = 0;
			gbc.anchor = GridBagConstraints.EAST;
			break;
		case ProbabilityContainerLayout.CENTER:
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.insets.left = 0;
			gbc.insets.right = 0;
			gbc.insets.top = 0;
			gbc.insets.bottom = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			break;
		}
	}
}
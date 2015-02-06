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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;

/**
 * @author CBONACETO
 *
 */
public class ReportComponentContainer extends JComponent {	
	private static final long serialVersionUID = 7258076874782570212L;
	
	/** Horizontal Alignment constants */
	public static enum Alignment {Center, East, West, Stretch};
	
	/** Expand/collapse button images */
	protected static final Image expandImage = ImageManager.getImage(ImageManager.PLUS_ICON);
	protected static final Image collapseImage = ImageManager.getImage(ImageManager.MINUS_ICON);
	
	/** The title label */
	protected JLabel titleLabel;	
	
	/** The expand/collapse button label */
	protected JLabel buttonLabel;
	
	/** Separator between the title label and component */
	protected JSeparator separator;
	
	/** Whether to show the separator */
	protected boolean separatorVisible = true;
	
	/** The component */
	protected IConditionComponent component;
	
	//Needed: Expand/collapse buttons, separators (top/bottom), way to highlight component as active component
	
	public ReportComponentContainer(IConditionComponent component, Alignment horizontalAlignment) {
		this(component, horizontalAlignment, null, false);
	}
	
	public ReportComponentContainer(IConditionComponent component, Alignment horizontalAlignment,
			String title, boolean titleVisible) {
		this.component = component;
		setOpaque(true);
		setLayout(new GridBagLayout());
		titleLabel = new JLabel(title == null ? "" : title);
		titleLabel.setOpaque(true);
		titleLabel.setFont(WidgetConstants.FONT_DEFAULT);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setVisible(titleVisible);
		add(titleLabel, createTitleLabelConstraints());
		separator = new JSeparator(); 
		separator.setVisible(titleVisible);
		//separator.setForeground(Color.black);
		add(separator, createTitleSeparatorConstraints());
		add(component.getComponent(), createComponentConstraints(horizontalAlignment));
	}
	
	protected GridBagConstraints createTitleLabelConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.insets.left = 3;
		gbc.insets.right = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 1;
		return gbc;
	}
	
	protected GridBagConstraints createTitleSeparatorConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 1;
		return gbc;
	}
	
	protected GridBagConstraints createComponentConstraints(Alignment horizontalAlignment) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets.top = 14;
		switch(horizontalAlignment) {
		case Center:
			gbc.anchor = GridBagConstraints.CENTER;
			break;
		case East:
			gbc.anchor = GridBagConstraints.EAST;
			break;
		case West:
			gbc.anchor = GridBagConstraints.WEST;
			break;
		case Stretch:
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1;
			break;
		default: 
			gbc.anchor = GridBagConstraints.CENTER;
		}
		return gbc;
	}
	
	public IConditionComponent getComponent() {
		return component;
	}

	public void setComponent(IConditionComponent component, Alignment horizontalAlignment) {
		if(component != this.component) {
			remove(this.component.getComponent());
			this.component = component;
			add(component.getComponent(), createComponentConstraints(horizontalAlignment));			
			revalidate();
		}
	}
	
	/**
	 * @param horizontalAlignment
	 */
	public void setHorizontalAlignment(Alignment horizontalAlignment) {
		((GridBagLayout)getLayout()).setConstraints(component.getComponent(), createComponentConstraints(horizontalAlignment));
		revalidate();
	}	

	public boolean isTitleVisible() {
		return titleLabel.isVisible();
	}
	
	public void setTitleVisible(boolean visible) {
		if(visible != titleLabel.isVisible()) {
			titleLabel.setVisible(visible);
			separator.setVisible(visible && separatorVisible);			
			revalidate();
		}
	}
	
	public String getTitle() {
		return titleLabel.getText();
	}
	
	public void setTitle(String title) {
		titleLabel.setText(title);
		revalidate();
	}
	
	public boolean isTitleOpaque() {
		return titleLabel.isOpaque();
	}
	
	public void setTitleOpaque(boolean isOpaque) {
		titleLabel.setOpaque(isOpaque);
	}
	
	public int getTitleAlignment() {
		return titleLabel.getHorizontalAlignment();
	}
	
	public void setTitleAlignment(int alignment) {
		titleLabel.setHorizontalAlignment(alignment);
	}
	
	public Font getTitleFont() {
		return titleLabel.getFont(); 
	}
	
	public void setTitleFont(Font font) {
		titleLabel.setFont(font);
		revalidate();
	}
	
	public Color getTitleForeground() {
		return titleLabel.getForeground();
	}
	
	public void setTitleForeground(Color foreground) {
		titleLabel.setForeground(foreground);
	}
	
	public Color getTitleBackground() {
		return titleLabel.getBackground();
	}	
	
	public void setTitleBackground(Color background) {
		titleLabel.setBackground(background);
	}
	
	public void configureTitleSeparator(boolean separatorVisible, Color separatorColor, int separatorHeight) {
		this.separatorVisible = separatorVisible;
		if(separatorColor != null) {
			separator.setForeground(separatorColor);
			separator.setBackground(separatorColor);
		}
		if(separatorHeight > 0) {
			separator.setPreferredSize(new Dimension(1, separatorHeight));			
		}
		separator.setVisible(titleLabel.isVisible() && separatorVisible);		
		revalidate();
	}
	
	public boolean isActive() {
		//TODO: Implement this
		return false;
	}
	
	public void setActive(boolean active) {
		//TODO: Implement this
	}
	
	public boolean isExpanded() {
		//TODO: Implement this
		return false;
	}
	
	public void setExpanded(boolean expanded) {
		//TODO: Implement this
	}	
}
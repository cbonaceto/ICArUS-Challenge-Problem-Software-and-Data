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
package org.mitre.icarus.cps.experiment_core.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;

/**
 * Panel for an experiment condition (e.g., the task 1 condition).  Contains a content area and
 * a banner at the top or bottom of the page for instructions text.
 * 
 * @author CBONACETO
 *
 */
public abstract class ConditionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static enum BannerOrientation {Top, Bottom};
	
	/** The instruction text banner (oriented along the top or bottom of the panel) */
	protected JLabel instructionBanner;
	protected JSeparator instructionBannerSeparator;
	protected JPanel bannerPanel;
	protected BannerOrientation bannerOrientation;
	
	/** The number of lines of text to support in the instruction banner (used to size the banner height, default is 4) */
	protected int instructionBannerTextHeight;
	
	/** The instructions panel */
	protected InstructionsPanel instructionPanel;
	
	/** The blank page */
	protected JPanel blankPage;
	
	/** The content pane area (where the current condition component is displayed) */
	protected JPanel contentPane;
	
	/** The current condition Component */
	protected IConditionComponent currentComponent;
	
	/** The condition components (mapped by id) */
	protected HashMap<String, IConditionComponent> conditionComponents;
	
	/** Listeners that have registered to receive subject action events fired
	 * by this condition panel  */
	//TODO: This needs to be made thread-safe
	protected transient List<SubjectActionListener> subjectActionListeners;
	
	protected ConditionPanel(boolean showInstructionBanner, BannerOrientation bannerOrientation) {
		this(showInstructionBanner, bannerOrientation, 4, null);
	}
	
	protected ConditionPanel(boolean showInstructionBanner, BannerOrientation bannerOrientation, 
			int instructionBannerTextHeight) {
		this(showInstructionBanner, bannerOrientation, instructionBannerTextHeight, null);
	}
	
	protected ConditionPanel(boolean showInstructionBanner, BannerOrientation bannerOrientation,
			int instructionBannerTextHeight, Collection<IConditionComponent> conditionComponents) {
		super();
		this.bannerOrientation = bannerOrientation;
		this.instructionBannerTextHeight = instructionBannerTextHeight;
		//setBackground(Color.black);
		
		setLayout(new BorderLayout());
		
		if(showInstructionBanner) {
			bannerPanel = new JPanel();
			bannerPanel.setLayout(new GridBagLayout());
			bannerPanel.setBackground(Color.white);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0; 
			gbc.gridy = 0;
			gbc.weightx = 1;			
			//bannerPanel.setLayout(new BorderLayout());
			if(bannerOrientation == BannerOrientation.Bottom) {
				gbc.weighty = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				instructionBannerSeparator = WidgetConstants.createDefaultSeparator();
				bannerPanel.add(instructionBannerSeparator, gbc);
				gbc.gridy++;
			}
			gbc.weighty = 1;
			gbc.fill = GridBagConstraints.BOTH;
			instructionBanner = new JLabel();
			instructionBanner.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
			instructionBanner.setFont(WidgetConstants.FONT_INSTRUCTION_BANNER);
			instructionBanner.setBackground(Color.white);
			instructionBanner.setOpaque(true);
			instructionBanner.setHorizontalAlignment(SwingConstants.CENTER);
			//bannerPanel.add(instructionBanner, BorderLayout.CENTER);
			bannerPanel.add(instructionBanner, gbc);
			if(bannerOrientation == BannerOrientation.Top) {
				instructionBannerSeparator = WidgetConstants.createDefaultSeparator();
				gbc.weighty = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridy++;				
				bannerPanel.add(instructionBannerSeparator, gbc);
			}
		}
		
		if(showInstructionBanner && bannerOrientation == BannerOrientation.Top) {
			add(bannerPanel, BorderLayout.NORTH);
		}
				
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());	
		//contentPane.setBackground(Color.blue);		
		add(contentPane, BorderLayout.CENTER);
		
		if(showInstructionBanner && bannerOrientation == BannerOrientation.Bottom) {			
			add(bannerPanel, BorderLayout.SOUTH);
		}
		
		instructionPanel = new InstructionsPanel("instructions");
		instructionPanel.setBackground(WidgetConstants.COLOR_COMPONENT_BACKGROUND);
		//instructionPanel.setBackground(Color.red);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				resized();
			}
		});
		
		if(conditionComponents != null) {
			setConditionComponents(conditionComponents);
		}	
	}	
	
	protected void setConditionComponents(Collection<IConditionComponent> conditionComponents) {		
		if(conditionComponents != null) {
			this.conditionComponents = new HashMap<String, IConditionComponent>();			
			this.conditionComponents.put(instructionPanel.getComponentId(), instructionPanel);
			Dimension maxSize = new Dimension(0, 0);
			for(IConditionComponent component : conditionComponents) {
				this.conditionComponents.put(component.getComponentId(), component);
				Dimension size = component.getComponent().getPreferredSize();
				if(size.width > maxSize.width) {
					maxSize.width = size.width;
				}
				if(size.height > maxSize.height) {
					maxSize.height = size.height;
				}
			}
			//Set the preferred size to fit the largest condition component 
			contentPane.setPreferredSize(maxSize);	
			instructionPanel.setPreferredSize(maxSize);
			
			if(instructionBanner != null) {
				//Set the preferred size to fit the largest condition component and the instruction banner
				instructionBanner.setPreferredSize(new Dimension(maxSize.width, 
						getFontMetrics(instructionBanner.getFont()).getHeight() * instructionBannerTextHeight + 4)); //Was +6
				bannerPanel.revalidate();
			}			
			revalidate();
			repaint();
		}
	}
	
	protected void resized() {
		if(instructionBanner != null) {
			instructionBanner.setPreferredSize(new Dimension(getSize().width, 
					instructionBanner.getPreferredSize().height));		
		}		
		revalidate();
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(contentPane != null) {
			contentPane.setBackground(bg);
		}
	}	
	
	public void setInstructionBannerText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(instructionBanner != null) {
					instructionBanner.setText("<html><center>" + text + "</center></html>");
				}				
			}			
		});
	}
	
	public void setInstructionBannerVisible(Boolean visible) {
		if(instructionBanner != null && instructionBanner.isVisible() != visible) {
			instructionBanner.setVisible(visible);
			instructionBannerSeparator.setVisible(visible);
			revalidate();
		}
	}
	
	public void setInstructionsURL(URL url) {
		instructionPanel.setInstructionsURL(url);
	}
	
	public void setInstructionsText(String text) {
		instructionPanel.setInstructionText(text);
	}
	
	public void setInstructionsPage(final InstructionsPage page) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				instructionPanel.setInstructionsPage(page);
			}
		});
	}
	
	public void setInstructionsImage(final Image image) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				instructionPanel.setInstructionsImage(image);
			}
		});
	}

	public void showInstructionsPage() {		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setConditionComponent(instructionPanel);
			}
		});
	}
	
	/** Show a blank page filled with the current background color */
	public void showBlankPage() {
		currentComponent = null;
		contentPane.removeAll();
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	protected void setConditionComponent(String componentId) {
		setConditionComponent(conditionComponents.get(componentId));
	}
	
	protected void setConditionComponent(IConditionComponent component) {
		if(component == null) {
			throw new IllegalArgumentException("Error setting condition component: component null");
		}
		
		if(component != this.currentComponent) {			
			this.currentComponent = component;						
			contentPane.removeAll();
			if(instructionBanner != null) {
				if(bannerOrientation == BannerOrientation.Top) {
					contentPane.add(component.getComponent(), BorderLayout.CENTER);
				}
				else {
					contentPane.add(component.getComponent(), BorderLayout.CENTER);
				}
			}
			else {
				contentPane.add(component.getComponent(), BorderLayout.CENTER);
			}
			//component.getComponent().setBackground(Color.black);
			contentPane.revalidate();
			contentPane.repaint();
		}
	}
	
	public void addSubjectActionListener(SubjectActionListener listener) {
		if(subjectActionListeners == null) {
			subjectActionListeners = new LinkedList<SubjectActionListener>();
		}
		if(!subjectActionListeners.contains(listener)) {
			subjectActionListeners.add(listener);
		}
	}
	
	public void removeSubjectActionListener(SubjectActionListener listener) {
		if(subjectActionListeners != null) {
			subjectActionListeners.remove(listener);
		}
	}
	
	/** Fire a subject action event to all registered listeners */
	protected void fireSubjectActionEvent(SubjectActionEvent event) {
		if(subjectActionListeners != null) {
			for(SubjectActionListener listener : subjectActionListeners) {
				listener.subjectActionPerformed(event);
			}
		}
	}
}
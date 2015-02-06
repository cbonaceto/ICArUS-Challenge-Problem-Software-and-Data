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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.mitre.icarus.cps.app.widgets.phase_2.experiment.report_panel.ReportComponentContainer.Alignment;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryContainer;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryComponentFactory;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;

/**
 * 
 * @author CBONACETO
 *
 */
public class AssessmentReportPanel extends JScrollPane {
	private static final long serialVersionUID = -6308283507971242027L;
	
	public static enum ScrollPosition {Top, Center, Bottom};
	
	/** Panel containing the components (this is the view in the scroll pane) */
	protected JPanel contentPanel;
	
	/** Ordered list of each component in the panel (from top to bottom) */
	protected LinkedList<ReportComponentContainer> components;
	
	/** Components mapped by Id */
	protected HashMap<String, ReportComponentContainer> componentsMap;
	
	/** Current active component */
	protected ReportComponentContainer activeComponent;

	/** The layout */
	protected GridBagLayout gbl;
	protected GridBagConstraints gbc;
	
	/** Vertical spacing between components */
	protected int verticalSpacing = 4;
	
	/** Vertical spacing above the first component */
	int topSpacing = 3;
	
	/** Vertical spacing below the last component */
	int bottomSpacing = 3;
	
	/** Factory used to create ReportComponentContainer instances */
	protected AbstractReportComponentContainerFactory componentFactory = new DefaultReportComponentContainerFactory(); 
	
	public AssessmentReportPanel() {
		this(4, Color.WHITE);
	}
		
	public AssessmentReportPanel(int verticalSpacing, Color background) {	
		super(new JPanel());		
		this.verticalSpacing = verticalSpacing;
		//setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPanel = (JPanel)getViewport().getView();
		if(background != null) {
			contentPanel.setBackground(background);
		}
		contentPanel.setLayout(gbl = new GridBagLayout());		
		components = new LinkedList<ReportComponentContainer>();
		componentsMap = new HashMap<String, ReportComponentContainer>();
		
		gbc = new GridBagConstraints();
		//gbc.insets = new Insets(verticalSpacing/2, 3, verticalSpacing/2, 3);
		gbc.insets = new Insets(verticalSpacing/2, 0, verticalSpacing/2, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
	}	
	
	public void addComponentAtTop(IConditionComponent component, Alignment horizontalAlignment, String title, boolean titleVisible, 
			boolean makeActiveComponent, boolean scrollToComponent, ScrollPosition scrollPosition) {
		int numVisibleComponents = 0;
		if(!components.isEmpty()) {
			//Move existing components down
			int i = 1;			
			ReportComponentContainer firstVisibleComponent = null;
			for(ReportComponentContainer rc : components) {
				GridBagConstraints constraints = gbl.getConstraints(rc);
				//if(i == 1) {
				//	constraints.insets.top = verticalSpacing/2;
				//}
				constraints.gridy = i;
				//constraints.weighty = (i == components.size()) ? 1 : 0;
				//constraints.weighty = 0;
				gbl.setConstraints(rc, constraints);
				if(rc.isVisible() && firstVisibleComponent == null) {
					numVisibleComponents++;
					firstVisibleComponent = rc;
				}
				i++;				
			}
			//Adjust constraints of the current first visible component
			if(firstVisibleComponent != null) {
				GridBagConstraints constraints = gbl.getConstraints(firstVisibleComponent);
				constraints.insets.top = verticalSpacing / 2;
				//constraints.weighty = numVisibleComponents == 1 ? 1 : 0;
				//constraints.insets.bottom = 0;
				gbl.setConstraints(firstVisibleComponent, constraints);
			}
		}
		//Add new component at top
		gbc.gridy = 0;
		gbc.weighty = numVisibleComponents == 0 ? 1 : 0;
		gbc.insets.top = topSpacing;
		gbc.insets.bottom = numVisibleComponents == 0 ? bottomSpacing : verticalSpacing/2;	
		//gbc.insets.bottom = components.isEmpty() ? verticalSpacing : verticalSpacing/2;
		ReportComponentContainer rc = componentFactory.createReportComponentContainer(component, horizontalAlignment, title, titleVisible);;
		components.addFirst(rc);
		componentsMap.put(component.getComponentId(), rc);
		contentPanel.add(rc, gbc);
		revalidate();
		if(makeActiveComponent) {
			setActiveComponent(rc, scrollToComponent, scrollPosition);
		} else if(scrollToComponent) {
			scrollToComponent(rc, scrollPosition, false);
		}
	}	
	
	public void addComponentAtBottom(IConditionComponent component, Alignment horizontalAlignment, String title, boolean titleVisible,
			boolean makeActiveComponent, boolean scrollToComponent, ScrollPosition scrollPosition) {
		VisibleReportComponentStatus visibleStatus = getVisibleReportComponentStatus();
		if(!components.isEmpty() && visibleStatus.lastVisibleComponent != null) {
			//Adjust constraints of the current last visible component
			GridBagConstraints constraints = gbl.getConstraints(visibleStatus.lastVisibleComponent);
			constraints.insets.bottom = verticalSpacing/2;
			constraints.weighty = 0;
			gbl.setConstraints(visibleStatus.lastVisibleComponent, constraints);
		}
		//Add new component at bottom
		gbc.gridy = components.size();
		gbc.weighty = 1;
		gbc.insets.top = visibleStatus.numVisibleComponents == 0 ? topSpacing : verticalSpacing/2; 
		gbc.insets.bottom = bottomSpacing;
		//gbc.insets.top = components.isEmpty() ? verticalSpacing : verticalSpacing/2;
		//gbc.insets.bottom = verticalSpacing;
		ReportComponentContainer rc = componentFactory.createReportComponentContainer(component, horizontalAlignment, title, titleVisible);
		components.add(rc);
		componentsMap.put(component.getComponentId(), rc);
		contentPanel.add(rc, gbc);
		revalidate();
		if(makeActiveComponent) {
			setActiveComponent(rc, scrollToComponent, scrollPosition);
		} else if(scrollToComponent) {
			scrollToComponent(rc, scrollPosition, false);
		}
	}
	
	/**
	 * Get the first and last visible component, and the number of visible components.
	 */
	protected VisibleReportComponentStatus getVisibleReportComponentStatus() {
		if(!components.isEmpty()) {
			int numVisible = 0;
			ReportComponentContainer firstVisibleComponent = null;
			ReportComponentContainer lastVisibleComponent = null;
			for(ReportComponentContainer rc : components) {
				if(rc.isVisible()) {
					if(firstVisibleComponent == null) {
						firstVisibleComponent = rc;
					}
					if(lastVisibleComponent == null) {
						lastVisibleComponent = rc;
					}
					numVisible++;
				}
			}
			return new VisibleReportComponentStatus(numVisible, firstVisibleComponent, lastVisibleComponent);
		}
		return new VisibleReportComponentStatus(0, null, null);
	}
	
	public AbstractReportComponentContainerFactory getComponentFactory() {
		return componentFactory;
	}

	public void setComponentFactory(AbstractReportComponentContainerFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	public IConditionComponent getComponentById(String componentId) {
		ReportComponentContainer rc = getComponentContainerById(componentId);
		return rc != null ? rc.component : null;
	}	
	
	public IConditionComponent getComponentByRow(int row) {
		ReportComponentContainer rc = getComponentContainerByRow(row);
		return rc != null ? rc.component : null;
	}
	
	public void revalidateComponentContainerAtRow(int row) {
		ReportComponentContainer rc = getComponentContainerByRow(row);
		if(rc != null) {
			rc.revalidate();			
			rc.repaint();
		}
	}
	
	public void revalidateContentPanel() {
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	protected ReportComponentContainer getComponentContainerById(String componentId) {
		return componentsMap.get(componentId);
	}
	
	protected ReportComponentContainer getComponentContainerByRow(int row) {
		if(row >=0 && row < components.size()) {
			return components.get(row);
		}
		return null;
	}
	
	public void setComponentAtRow(IConditionComponent component, Alignment horizontalAlignment, int row, String title, boolean titleVisible,
			boolean makeActiveComponent, boolean scrollToComponent, ScrollPosition scrollPosition) {
		ReportComponentContainer rc = getComponentContainerByRow(row);
		if(rc != null) {
			rc.setComponent(component, horizontalAlignment);
			rc.setTitle(title);
			rc.setTitleVisible(titleVisible);
			revalidate();
			if(makeActiveComponent) {
				setActiveComponent(rc, scrollToComponent, scrollPosition);
			} else if(scrollToComponent) {
				scrollToComponent(rc, scrollPosition, false);
			}	
		}
	}
	
	public void setComponentPropertiesAtRow(Alignment horizontalAlignment, int row, String title, boolean titleVisible,
			boolean makeActiveComponent, boolean scrollToComponent, ScrollPosition scrollPosition) {
		ReportComponentContainer rc = getComponentContainerByRow(row);
		if(rc != null) {
			rc.setHorizontalAlignment(horizontalAlignment);
			rc.setTitle(title);
			rc.setTitleVisible(titleVisible);		
			revalidate();
			if(makeActiveComponent) {
				setActiveComponent(rc, scrollToComponent, scrollPosition);
			} else if(scrollToComponent) {
				scrollToComponent(rc, scrollPosition, false);
			}	
		}
	}
	
	public IConditionComponent removeComponent(String componentId) {
		return removeComponent(getComponentContainerById(componentId));
	}
	
	public IConditionComponent removeComponentAtRow(int row) {
		return removeComponent(getComponentContainerByRow(row));
	}
	
	protected IConditionComponent removeComponent(ReportComponentContainer component) {
		if(component != null) {
			int row = -1;
			if(components.size() > 1) {
				ReportComponentContainer firstVisibleComponent = null;
				ReportComponentContainer lastVisibleComponent = null;
				int i =0;
				for(ReportComponentContainer rc : components) {
					GridBagConstraints constraints = gbl.getConstraints(rc);
					if(row == -1) {
						if(rc == component) {
							row = i;
						} 
					} else {					
						//Move components below removed component up						
						constraints.gridy = i - 1;
						gbl.setConstraints(rc, constraints);
						i++;
					}
					if(rc.isVisible()) {
						if(firstVisibleComponent == null) {
							firstVisibleComponent = rc;
						}
						lastVisibleComponent = rc;
					}
					
				}
				if(component == firstVisibleComponent) {
					//Adjust constraints of the new first visible component
					VisibleReportComponentStatus visibleStatus = getVisibleReportComponentStatus();
					if(visibleStatus.firstVisibleComponent != null) {
						GridBagConstraints constraints = gbl.getConstraints(visibleStatus.firstVisibleComponent);
						constraints.insets.top = topSpacing;
						constraints.weighty = visibleStatus.numVisibleComponents == 1 ? 1 : 0;
						gbl.setConstraints(visibleStatus.firstVisibleComponent, constraints);
					}
				} else if(component == lastVisibleComponent) {
					//Adjust constraints of the new last visible component
					VisibleReportComponentStatus visibleStatus = getVisibleReportComponentStatus();
					if(visibleStatus.lastVisibleComponent != null) {
						GridBagConstraints constraints = gbl.getConstraints(visibleStatus.lastVisibleComponent);
						constraints.insets.bottom = bottomSpacing;
						constraints.weighty = 1;
						gbl.setConstraints(visibleStatus.lastVisibleComponent, constraints);
					}
				}
				/*if(row == (components.size() - 1)) {
					//Adjust constraints of last visible component?					
					ReportComponentContainer rc = components.get(row - 1);
					GridBagConstraints constraints = gbl.getConstraints(rc);
					constraints.weighty = 1;
					constraints.insets.bottom = verticalSpacing;
					gbl.setConstraints(rc, constraints);
				}*/
			} else {
				row = 0;
			}
			contentPanel.remove(component);
			components.remove(row);
			componentsMap.remove(component.component.getComponentId());
			revalidate();
			return component.component;
		}
		return null;
	}
	
	public void removeAllComponents() {
		contentPanel.removeAll();
		components.clear();
		componentsMap.clear();
		activeComponent = null;
		revalidate();
	}
	
	public String getComponentTitle(String componentId) {
		ReportComponentContainer component = getComponentContainerById(componentId);
		return component != null ? component.getTitle() : null;
	}
	
	public void setComponentTitle(String componentId, String title) {
		ReportComponentContainer component = getComponentContainerById(componentId);
		if(component != null) {
			component.setTitle(title);
		}
	}
	
	public void setComponentTitle(int row, String title) {
		ReportComponentContainer component = getComponentContainerByRow(row);
		if(component != null) {
			component.setTitle(title);
		}
	}
	
	public boolean isComponentTitleVisible(String componentId) {
		ReportComponentContainer component = getComponentContainerById(componentId);
		return component != null ? component.isTitleVisible() : false;
	}
	
	public void setComponentTitleVisible(String componentId, boolean titleVisible) {
		ReportComponentContainer component = getComponentContainerById(componentId);
		if(component != null) {
			component.setTitleVisible(titleVisible);
		}
	}
	
	public void setComponentTitleVisible(int row, boolean titleVisible) {
		ReportComponentContainer component = getComponentContainerByRow(row);
		if(component != null) {
			component.setTitleVisible(titleVisible);
		}
	}
	
	public boolean isComponentVisible(String componentId) {
		ReportComponentContainer component = getComponentContainerById(componentId);
		return component != null ? component.isVisible() : false;
	}
	
	public boolean isComponentVisible(int row) {		
		ReportComponentContainer component = getComponentContainerByRow(row);
		return component != null ? component.isVisible() : false;
	}	
	
	public void setComponentVisible(String componentId, boolean visible) {
		setComponentVisible(getComponentContainerById(componentId), visible);		
	}
	
	public void setComponentVisible(int row, boolean visible) {		
		setComponentVisible(getComponentContainerByRow(row), visible);
	}	
	
	protected void setComponentVisible(ReportComponentContainer component, boolean visible) {
		if(component != null && visible != component.isVisible()) {
			VisibleReportComponentStatus visibleStatusPre = getVisibleReportComponentStatus();
			component.setVisible(visible);
			VisibleReportComponentStatus visibleStatusPost = getVisibleReportComponentStatus();
			//Adjust constraints of first and last visible component
			if(visible) {				
				GridBagConstraints constraints = gbl.getConstraints(component);
				if(component == visibleStatusPost.firstVisibleComponent) {
					//This component is the new first visible component
					constraints.insets.top = topSpacing;
					constraints.insets.bottom = visibleStatusPost.numVisibleComponents == 1 ? bottomSpacing : verticalSpacing/2;
					constraints.weighty = visibleStatusPost.numVisibleComponents == 1 ? 1 : 0;
					//System.out.println(constraints.weighty + ", " + visibleStatusPost.numVisibleComponents + ", " + visibleStatusPre.numVisibleComponents);
					gbl.setConstraints(component, constraints);
					if(visibleStatusPre.firstVisibleComponent != null) {
						//Set the constraints on the old first visible component
						GridBagConstraints oldFirstConstraints = gbl.getConstraints(visibleStatusPre.firstVisibleComponent);
						oldFirstConstraints.insets.top = verticalSpacing/2;
						oldFirstConstraints.weighty = 0;
						gbl.setConstraints(visibleStatusPre.firstVisibleComponent, oldFirstConstraints);
					}
				} else if(component == visibleStatusPost.lastVisibleComponent) {
					//This component is the new last visible component
					constraints.insets.top = visibleStatusPost.numVisibleComponents == 1 ? topSpacing : verticalSpacing/2;
					constraints.insets.bottom = bottomSpacing;
					constraints.weighty = 1;
					gbl.setConstraints(component, constraints);
					if(visibleStatusPre.lastVisibleComponent != null) {
						//Set the constraints on the old last visible component
						GridBagConstraints oldLastConstraints = gbl.getConstraints(visibleStatusPre.lastVisibleComponent);
						oldLastConstraints.insets.top = verticalSpacing/2;
						oldLastConstraints.weighty = 0;
						gbl.setConstraints(visibleStatusPre.lastVisibleComponent, oldLastConstraints);
					}
				}
			} else {
				if(component == visibleStatusPre.firstVisibleComponent) {
					//This component was previously the first visible component
					if(visibleStatusPost.firstVisibleComponent != null) {
						//Set the constraints on the new first visible component
						GridBagConstraints constraints = gbl.getConstraints(visibleStatusPost.firstVisibleComponent);
						constraints.insets.top = topSpacing;
						constraints.insets.bottom = visibleStatusPost.numVisibleComponents == 1 ? bottomSpacing : verticalSpacing/2;
						constraints.weighty = visibleStatusPost.numVisibleComponents == 1 ? 1 : 0;
						gbl.setConstraints(visibleStatusPost.firstVisibleComponent, constraints);
					} 
				} else if(component == visibleStatusPre.lastVisibleComponent) {
					//This component was previously the last visible component
					if(visibleStatusPost.lastVisibleComponent != null) {
						//Set the constraints on the new last visible component
						GridBagConstraints constraints = gbl.getConstraints(visibleStatusPost.lastVisibleComponent);
						constraints.insets.top = visibleStatusPost.numVisibleComponents == 1 ? topSpacing : verticalSpacing/2;
						constraints.insets.bottom = bottomSpacing;
						constraints.weighty = 1;
						gbl.setConstraints(visibleStatusPost.lastVisibleComponent, constraints);
					}
				}
			}
			revalidate();
			repaint();
		}		
	}
	
	public void setAllComponentsVisible(boolean visible) {
		if(!components.isEmpty()) {
			ReportComponentContainer firstVisibleComponent = null;
			ReportComponentContainer lastVisibleComponent = null;
			for(ReportComponentContainer component : components) {
				component.setVisible(visible);
				if(visible) {
					GridBagConstraints constraints = gbl.getConstraints(component);
					if(firstVisibleComponent == null) {
						//Adjust constraints of first visible component
						firstVisibleComponent = component;
						constraints.insets.top = topSpacing;
					} else {						
						lastVisibleComponent = component;
						constraints.insets.bottom = verticalSpacing / 2;
					}
					gbl.setConstraints(component, constraints);
				}
				
				//Adjust constraints of the last visible component
				if(visible) {
					GridBagConstraints constraints = gbl.getConstraints(lastVisibleComponent);
					constraints.insets.bottom = bottomSpacing;
					constraints.weighty = 1;
					gbl.setConstraints(lastVisibleComponent, constraints);
				}
			}
		}
		revalidate();
	}
	
	public boolean isComponentExpanded(String componentId) {
		ReportComponentContainer component = getComponentContainerById(componentId);
		return component != null ? component.isExpanded() : false;
	}
	
	public boolean isComponentExpanded(int row) {	
		ReportComponentContainer component = getComponentContainerByRow(row);
		return component != null ? component.isExpanded() : false;
	}	
	
	public void setComponentExpanded(String componentId, boolean expanded) {
		setComponentExpanded(getComponentContainerById(componentId), expanded);	
	}
	
	public void setComponentExpanded(int row, boolean expanded) {
		setComponentExpanded(getComponentContainerByRow(row), expanded);
	}	
	
	protected void setComponentExpanded(ReportComponentContainer component, boolean expanded) {
		if(component != null && expanded != component.isVisible()) {
			component.setExpanded(expanded);			
			revalidate();
			repaint();
		}		
	}
	
	public void setAllComponentsExpanded(boolean expanded) {
		for(ReportComponentContainer component : components) {
			component.setExpanded(expanded);			
		}
		revalidate();
	}
	
	public void setActiveComponent(String componentId, boolean scrollToComponent, ScrollPosition scrollPosition) {
		setActiveComponent(getComponentContainerById(componentId), scrollToComponent, scrollPosition);
	}
	
	public void setActiveComponent(int row, boolean scrollToComponent, ScrollPosition scrollPosition) {
		setActiveComponent(getComponentContainerByRow(row), scrollToComponent, scrollPosition);
	}
	
	protected void setActiveComponent(ReportComponentContainer component, boolean scrollToComponent, ScrollPosition scrollPosition) {
		if(component != null) {
			if(component != activeComponent) {
				if(activeComponent != null) {
					activeComponent.setActive(false);
				}
				activeComponent = component;
				activeComponent.setActive(true);
			}
			if(scrollToComponent) {
				scrollToComponent(component, scrollPosition, false);
			}
		}
	}
	
	public void scrollToComponent(String componentId, ScrollPosition scrollPosition, boolean makeActiveComponent) {
		scrollToComponent(getComponentContainerById(componentId), scrollPosition, makeActiveComponent);
	}
	
	public void scrollToComponent(int row, ScrollPosition scrollPosition, boolean makeActiveComponent) {
		scrollToComponent(getComponentContainerByRow(row), scrollPosition, makeActiveComponent);
	}
	
	protected void scrollToComponent(ReportComponentContainer component, ScrollPosition scrollPosition, boolean makeActiveComponent) {
		if(component != null) {
			//this.scrollRectToVisible(aRect);
			//TODO: Implement this
			if(makeActiveComponent) {
				setActiveComponent(component, false, null);
			}
		}
	}
	
	public int getNumComponents() {
		return components.size();
	}
	
	/** Test main */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		AssessmentReportPanel p = new AssessmentReportPanel(10, null);
		frame.getContentPane().add(p);
		p.setPreferredSize(new Dimension(400, 800));				
		
		//JPanelConditionComponent c1 = new JPanelConditionComponent("1");
		IProbabilityEntryContainer c1 = ProbabilityEntryComponentFactory.createDefaultProbabilityEntryComponent(Arrays.asList("A", "B", "C", "D"), false);
		//c1.getComponent().setBackground(Color.red);
		//c1.getComponent().setBorder(BorderFactory.createLineBorder(Color.blue));
		//c1.getComponent().setPreferredSize(new Dimension(100, 200));		
		p.addComponentAtTop(c1, Alignment.Stretch, "Proability Panel", true, false, false, null);
		
		int numPanels = 1;
		for(int i=0; i<numPanels; i++) {
			JPanelConditionComponent c = new JPanelConditionComponent(Integer.toString(i));
			//c.getComponent().setBackground(Color.red);
			//c.getComponent().setBorder(BorderFactory.createLineBorder(Color.blue));
			c.getComponent().setPreferredSize(new Dimension(100, 200));			
			p.addComponentAtBottom(c, Alignment.Stretch, "Component " + Integer.toString(i+1), true, false, false, null);			
		}
		
		frame.pack();
		frame.setVisible(true);
	}	
	
	protected static class VisibleReportComponentStatus {
		protected final int numVisibleComponents;
		protected final ReportComponentContainer firstVisibleComponent;
		protected final ReportComponentContainer lastVisibleComponent;
		
		public VisibleReportComponentStatus(int numVisibleComponents, 
				ReportComponentContainer firstVisibleComponent,ReportComponentContainer lastVisibleComponent) {
			this.numVisibleComponents = numVisibleComponents;
			this.firstVisibleComponent = firstVisibleComponent;
			this.lastVisibleComponent = lastVisibleComponent;
		}
	}
}
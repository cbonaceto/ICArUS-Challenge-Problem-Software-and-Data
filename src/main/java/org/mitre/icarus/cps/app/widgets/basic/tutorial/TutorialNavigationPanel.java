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
package org.mitre.icarus.cps.app.widgets.basic.tutorial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.exam.base.training.TutorialNavigationTree;
import org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;
import org.mitre.icarus.cps.experiment_core.gui.IInstructionsPanel;

/**
 * A panel with a tree-like menu for navigating the pages in a tutorial.
 * 
 * @author CBONACETO
 *
 */
public class TutorialNavigationPanel extends JPanel implements TreeWillExpandListener {

	private static final long serialVersionUID = 8607222780114233241L;
	
	/** Reference to the component where the tutorial is displayed */
	protected IInstructionsPanel instructionsPanel;
	
	/** The tree containing the tutorial sections, subsections, etc. */
	protected JTree tutorialTree;
	
	/** The tree model */
	protected DefaultTreeModel treeModel;
	
	/** The tutorials (mapped by tutorial name) */
	protected Map<String, Tutorial> tutorials;	
	
	/** The root node */
	protected DefaultMutableTreeNode rootNode;
	
	/** Listeners registered to receive tutorial node selection events */
	private List<TutorialTreeSelectionListener> tutorialNodeSelectionListeners;
	
	private JScrollPane sp;
	
	public TutorialNavigationPanel(IInstructionsPanel instructionsPanel) {
		super(new BorderLayout());
		this.instructionsPanel = instructionsPanel;
		tutorials = new HashMap<String, Tutorial>();
		tutorialNodeSelectionListeners = Collections.synchronizedList(
				new LinkedList<TutorialTreeSelectionListener>());
		
		this.tutorialTree = new JTree();
		setFont(this.tutorialTree.getFont());
		rootNode = new DefaultMutableTreeNode();
		treeModel = new DefaultTreeModel(rootNode);
		this.tutorialTree.setModel(treeModel);		
		this.tutorialTree.setRootVisible(false);
		this.tutorialTree.setShowsRootHandles(true);		
		this.tutorialTree.setCellRenderer(new TutorialTreeNodeRenderer());
		
		sp = new JScrollPane(this.tutorialTree);
		sp.setBorder(null);
		add(sp);
				
		//Create a mouse listener to change the cursor to a hand when the mouse is over a selectable node
		MouseAdapter mouseListener = new MouseAdapter() {
			private boolean showingHandCursor = false;
			@Override
			public void mouseClicked(MouseEvent e) {
				TutorialTreeNode node = getSelectedNode();
				/*if(node != null) {
					if(node.getNode() != null) {
						System.out.println("node clicked: " + node.getNode().getPageOrSectionName() + ", row: " + node.getRow());
					} else {
						System.out.println("node clicked: null, row: " + node.getRow());
					}
				}*/
				if(node != null && node.isSelectable()) {
					//Show the tutorial page for the node that was clicked
					if(TutorialNavigationPanel.this.instructionsPanel != null) {
						Tutorial tutorial =	tutorials.get(node.getTutorialName());
						if(tutorial != null && tutorial.tutorialPages != null 
								&& node.getNode().getPageIndex() < tutorial.tutorialPages.size()) {
							//System.out.println("Navigating to page: " + (node.getNode().getPageIndex()+1) + 
							//		" of " + tutorial.tutorialPages.size());
							TutorialNavigationPanel.this.instructionsPanel.setInstructionsPage(
									tutorial.tutorialPages.get(node.getNode().getPageIndex()));
						}
					}
					//Notify listeners that a node was clicked
					fireTutorialTreeSelectionEvent(node);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(showingHandCursor) {
					TutorialNavigationPanel.this.tutorialTree.setCursor(Cursor.getDefaultCursor());
					showingHandCursor = false;
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				TutorialTreeNode node = getNodeAtLocation(e.getX(), e.getY());
				if(node == null || !node.isSelectable()) {
					if(showingHandCursor) {
						TutorialNavigationPanel.this.tutorialTree.setCursor(Cursor.getDefaultCursor());
						showingHandCursor = false;
					}
				} else {
					if(!showingHandCursor) {
						TutorialNavigationPanel.this.tutorialTree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						showingHandCursor = true;
					}
				}
			}
			protected TutorialTreeNode getNodeAtLocation(int x, int y) {
				TreePath path = TutorialNavigationPanel.this.tutorialTree.getPathForLocation(x, y);
				if(path != null) {
					Object object = path.getLastPathComponent();
					if(object instanceof TutorialTreeNode) {
						return (TutorialTreeNode)object;
					}
				}
				return null;
			}
			protected TutorialTreeNode getSelectedNode() {
				Object object = TutorialNavigationPanel.this.tutorialTree.getLastSelectedPathComponent();
				if(object instanceof TutorialTreeNode) {
					return (TutorialTreeNode)object;
				}
				return null;
			}
		};
		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		this.tutorialTree.setSelectionModel(selectionModel);
		this.tutorialTree.addTreeWillExpandListener(this);
		this.tutorialTree.addMouseListener(mouseListener);
		this.tutorialTree.addMouseMotionListener(mouseListener);		
	}
	
	/*@Override
	public Dimension getPreferredSize() {
		if(tutorialTree != null) {
			return tutorialTree.getPreferredSize();
		} else {
			return super.getPreferredSize();
		}
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {		
		super.setPreferredSize(preferredSize);
		if(tutorialTree != null) {
			tutorialTree.setPreferredSize(preferredSize);
			//System.out.println(tutorialTree.getPreferredSize());
		}
	}*/

	public IInstructionsPanel getInstructionsPanel() {
		return instructionsPanel;
	}

	public void setInstructionsPanel(IInstructionsPanel instructionsPanel) {
		this.instructionsPanel = instructionsPanel;
	}
	
	/**
	 * @param tutorialName
	 * @param tutorialPages
	 * @param tutorialNavigationTree
	 * @param showTutorialParentNode
	 */
	public void addTutorial(String tutorialName, List<? extends InstructionsPage> tutorialPages,
			TutorialNavigationTree tutorialNavigationTree, boolean showTutorialParentNode) {
		Tutorial tutorial = new Tutorial(tutorialPages);		
		TutorialTreeParentNode tutorialParentNode = null;
		int row = tutorialTree.getRowCount();
		if(showTutorialParentNode) {
			//Create a parent node for the entire tutorial linked to the first page in the tutorial
			tutorialParentNode = new TutorialTreeParentNode();
			tutorialParentNode.setPageOrSectionName(tutorialName);
			tutorialParentNode.setPageIndex(0);
			tutorial.tutorialParentNode = new TutorialTreeNode(tutorialName, tutorialParentNode);
			rootNode.add(tutorial.tutorialParentNode);
			tutorial.tutorialParentNode.setRow(row);
			//System.out.println("added node at row " + row);
		}		
		//Create the tutorial section nodes
		if(tutorialNavigationTree != null && 
			tutorialNavigationTree.getTutorialSections() != null && 
			!tutorialNavigationTree.getTutorialSections().isEmpty()) {
			tutorial.tutorialSectionNodes = new HashMap<String, TutorialTreeNode>();
			for(TutorialTreeParentNode tutorialSection : tutorialNavigationTree.getTutorialSections()) {
				TutorialTreeNode node = new TutorialTreeNode(tutorialName, tutorialSection);
				//node.setRow(row++);
				tutorial.tutorialSectionNodes.put(tutorialSection.getPageOrSectionName(), node);
				//Add the child nodes if present
				if(tutorialSection.getChildren() != null && 
						!tutorialSection.getChildren().isEmpty()) {
					for(org.mitre.icarus.cps.exam.base.training.TutorialTreeNode child: 
						tutorialSection.getChildren()) {
						TutorialTreeNode childNode = new TutorialTreeNode(tutorialName, child);
						childNode.setRow(row++);
						node.add(childNode);
					}
				}
				//Add the tutorial section node either directly to the tree or to the tutorial parent node if present
				if(tutorial.tutorialParentNode != null) {
					tutorial.tutorialParentNode.add(node);
					//treeModel.nodeStructureChanged(tutorial.tutorialParentNode);
				} else {
					rootNode.add(node);
					node.setRow(row++);
					tutorialTree.expandRow(node.getRow());
					//treeModel.nodeStructureChanged(rootNode);
				}				
				//System.out.println("Row count: " + tutorialTree.getRowCount());
				//node.setRow(row++);
			}
		}
		tutorials.put(tutorialName, tutorial);
		if(tutorial.tutorialParentNode != null) {
			treeModel.nodeStructureChanged(tutorial.tutorialParentNode);
			tutorialTree.expandRow(tutorial.tutorialParentNode.getRow());
		}
		treeModel.nodeStructureChanged(rootNode);
		tutorialTree.repaint();
	}	

	/**
	 * Remove all tutorials from the tree.
	 */
	public void removeAllTutorials() {
		tutorials.clear();
		rootNode.removeAllChildren();
		treeModel.nodeStructureChanged(rootNode);
		tutorialTree.repaint();
	}
	
	/**
	 * Remove a tutorial from the tree.
	 * 
	 * @param tutorialName
	 */
	public void removeTutorial(String tutorialName) {
		Tutorial tutorial = tutorials.get(tutorialName);
		if(tutorial != null) {
			tutorials.remove(tutorialName);
			if(tutorial.tutorialParentNode != null) {
				treeModel.removeNodeFromParent(tutorial.tutorialParentNode);
			} else if(tutorial.tutorialSectionNodes != null) {
				for(TutorialTreeNode node : tutorial.tutorialSectionNodes.values()) {
					treeModel.removeNodeFromParent(node);
				}
			}
			updateTutorialNodeRowNumbers();
			tutorialTree.repaint();
		}
	}	
	
	public void expandAllTutorials() {
		if(tutorialTree != null) {
			for(int row = 0; row < tutorialTree.getRowCount(); row++) {
				tutorialTree.expandRow(row);
			}
		}
		tutorialTree.repaint();
	}
	
	public void expandTutorial(String tutorialName) {
		Tutorial tutorial = tutorials.get(tutorialName);
		if(tutorial != null && tutorial.enabled) {
			if(tutorial.tutorialParentNode != null) {
				tutorialTree.expandRow(tutorial.tutorialParentNode.getRow());
			}
			if(tutorial.tutorialSectionNodes != null) {
				for(TutorialTreeNode tutorialSectionNode : tutorial.tutorialSectionNodes.values()) {
					tutorialTree.expandRow(tutorialSectionNode.getRow());
				}
			}
			tutorialTree.repaint();
		}
	}
	
	public void collapseAllTutorials() {
		if(tutorialTree != null) {
			for(int row = 0; row < tutorialTree.getRowCount(); row++) {
				tutorialTree.collapseRow(row);
			}
		}
		tutorialTree.repaint();
	}
	
	public void collapseTutorial(String tutorialName) {
		Tutorial tutorial = tutorials.get(tutorialName);
		if(tutorial != null) {
			if(tutorial.tutorialParentNode != null) {
				tutorialTree.collapseRow(tutorial.tutorialParentNode.getRow());
			}
			if(tutorial.tutorialSectionNodes != null) {
				for(TutorialTreeNode tutorialSectionNode : tutorial.tutorialSectionNodes.values()) {
					tutorialTree.collapseRow(tutorialSectionNode.getRow());
				}
			}
			tutorialTree.repaint();
		}
	}
	
	/**
	 * Remove a tutorial section from the tutorial tree.
	 */
	/*public void removeTutorialSection(TutorialTreeParentNode tutorialSection) {
		TutorialTreeNode node = tutorialSections.get(tutorialSection.getPageOrSectionName());
		if(node != null) {
			tutorialSections.remove(tutorialSection.getPageOrSectionName());
			treeModel.removeNodeFromParent(node);
			updateTutorialSectionRowNumbers();
			tutorialTree.repaint();
		}
	}*/
	
	protected void updateTutorialNodeRowNumbers() {
		if(rootNode != null && rootNode.getChildCount() > 0) {
			for(int row = 0; row < rootNode.getChildCount(); row++) {
				TreeNode node = rootNode.getChildAt(row);
				if(node != null && node instanceof TutorialTreeNode) {
					((TutorialTreeNode)node).setRow(row);
				}
			}
		}
	}
	
	/**
	 * Remove all tutorial sections.
	 */
	/*public void removeAllTutorialSections() {
		tutorialSections.clear();
		rootNode.removeAllChildren();
		treeModel.nodeStructureChanged(rootNode);
		tutorialTree.repaint();
	}*/
	
	/** Get whether the tutorial tree is empty */
	public boolean isTutorialTreeEmpty() {
		return tutorialTree.getRowCount() == 0;
	}
	
	/** Get whether a tutorial is enabled */
	public boolean isTutorialEnabled(String tutorialName) {
		Tutorial tutorial = tutorials.get(tutorialName);
		return tutorial != null ? tutorial.enabled : false;
		/*TutorialTreeNode node = tutorialSections.get(tutorialSection.getPageOrSectionName());
		return node != null ? node.isEnabled() : false;*/
	}
	
	/** Set whether a tutorial is enabled */
	public void setTutorialEnabled(String tutorialName, boolean enabled) {
		Tutorial tutorial = tutorials.get(tutorialName);
		if(tutorial != null && enabled != tutorial.enabled) {
			tutorial.enabled = enabled;
			//Enable or disable all nodes
			if(tutorial.tutorialParentNode != null) {
				tutorial.tutorialParentNode.setEnabled(enabled);
			}
			if(tutorial.tutorialSectionNodes != null) {
				for(TutorialTreeNode node : tutorial.tutorialSectionNodes.values()) {
					node.setEnabled(enabled);
					if(node.getChildCount() > 0) {
						for(int i = 0; i < node.getChildCount(); i++) {
							((TutorialTreeNode)node.getChildAt(i)).setEnabled(enabled);
						}
					}
				}
			}
			if(!enabled) {
				//Collapse the tutorial
				collapseTutorial(tutorialName);
			}
			tutorialTree.repaint();
		}
		/*TutorialTreeNode node = tutorialSections.get(tutorialSection.getPageOrSectionName());
		if(node != null && enabled != node.isEnabled()) {			
			node.setEnabled(enabled);
			tutorialTree.collapseRow(node.getRow());			
			tutorialTree.repaint();
		}*/
	}
	
	/** Required by TreeWillExpandListener interface. */
	@Override
    public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
        if(e.getPath() != null) {
            //Cancel expansion if node is disabled
        	Object object = e.getPath().getLastPathComponent();
        	if(object instanceof TutorialTreeNode) {
        		TutorialTreeNode node = (TutorialTreeNode)object;
        		if(!node.isEnabled()) {
        			throw new ExpandVetoException(e);
        		}
        	}
        }
    }

    /** Required by TreeWillExpandListener interface. */
	@Override
    public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException {
		//Does nothing
    }
	
	/**
	 * @param listener
	 * @return
	 */
	public boolean isTutorialNodeSelectionListenerPresent(TutorialTreeSelectionListener listener) {
		synchronized(tutorialNodeSelectionListeners) {
			return tutorialNodeSelectionListeners.contains(listener);
		}
	}
	
	/**
	 * @param listener
	 */
	public void addTutorialTreeSelectionListener(TutorialTreeSelectionListener listener) {
		synchronized(tutorialNodeSelectionListeners) {
			tutorialNodeSelectionListeners.add(listener);
		}
	}
	
	/**
	 * @param listener
	 */
	public void removeTutorialTreeSelectionListener(TutorialTreeSelectionListener listener) {
		synchronized(tutorialNodeSelectionListeners) {
			tutorialNodeSelectionListeners.remove(listener);
		}
	}
	
	/** Notify all registered listeners that a node in the tree was clicked */
	private void fireTutorialTreeSelectionEvent(TutorialTreeNode selectedNode) {
		synchronized(tutorialNodeSelectionListeners) {
			TutorialTreeSelectionEvent event = new TutorialTreeSelectionEvent(this, 
					selectedNode.getTutorialName(), selectedNode.getNode().getPageOrSectionName(),
					selectedNode.getNode().getPageIndex());
			if(!tutorialNodeSelectionListeners.isEmpty()) {
				for(TutorialTreeSelectionListener listener :tutorialNodeSelectionListeners) {
					listener.tutorialTreeItemSelected(event);
				}
			}
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(tutorialTree != null) {
			tutorialTree.setBackground(bg);
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(tutorialTree != null) {
			tutorialTree.setFont(font);
			JLabel sizingLabel = new JLabel(" ");
			sizingLabel.setFont(font);
			tutorialTree.setRowHeight(sizingLabel.getPreferredSize().height + 20);
			tutorialTree.repaint();
		}
	}
	
	/** Test main */
	public static void main(String[] args) {
		IcarusLookAndFeel.initializeICArUSLookAndFeel();
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TutorialNavigationPanel navPanel = new TutorialNavigationPanel(null);
		navPanel.setFont(WidgetConstants.FONT_INSTRUCTION_PANEL);		
		
		//Create the exam tutorial navigation tree
		TutorialNavigationTree examTutorial = new TutorialNavigationTree();
		//TutorialTreeParentNode examTutorial = new TutorialTreeParentNode("Exam Tutorial", "Exam Tutorial", 0);
		List<TutorialTreeParentNode> sections = new LinkedList<TutorialTreeParentNode>();
		examTutorial.setTutorialSections(sections);		
		sections.add(new org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode("Exam Tutorial", "Task Overview", 1));
		sections.add(new org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode("Exam Tutorial", "Scoring", 10));
		sections.add(new org.mitre.icarus.cps.exam.base.training.TutorialTreeParentNode("Exam Tutorial", "Intelligence", 20));
		navPanel.addTutorial("Exam Tutorial", null, examTutorial, true);		
		//Create the mission tutorial navigation trees
		for(int mission = 1; mission <= 3; mission++) {
			//TutorialNavigationTree missionTutorial = new TutorialNavigationTree();
			//sections = new LinkedList<TutorialTreeParentNode>();
			//sections.add(new TutorialTreeParentNode("Mission 1 Tutorial", "Mission 1 Tutorial", 0));
			//missionTutorial.setTutorialSections(sections);
			navPanel.addTutorial("Mission " + mission + " Instructions", null, null, true);
		}
		
		//navPanel.expandAllTutorials();
		//navPanel.setPreferredSize(new Dimension(100, 100));
		System.out.println(navPanel.getPreferredSize() + ", " + navPanel.sp.getPreferredSize());
		//navPanel.setPreferredSize(navPanel.getPreferredSize());
		//navPanel.setTutorialEnabled("Exam Tutorial", false);
		frame.getContentPane().add(navPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	/** Contains the pages, parent node, and section nodes for a tutorial. */
	protected static class Tutorial {
		/** The tutorial pages */
		public List<? extends InstructionsPage> tutorialPages;
		
		/** The tutorial parent node (optional) */
		public TutorialTreeNode tutorialParentNode;
		
		/** The tutorial section nodes (mapped by section name) */
		public Map<String, TutorialTreeNode> tutorialSectionNodes;
		
		/** Whether the tutorial is enabled */
		public boolean enabled = true;
		
		public Tutorial() {}
		
		public Tutorial(List<? extends InstructionsPage> tutorialPages) {
			this(tutorialPages, null, null);
		}
		
		public Tutorial(List<? extends InstructionsPage> tutorialPages,
				TutorialTreeNode tutorialParentNode,
				Map<String, TutorialTreeNode> tutorialSectionNodes) {
			this.tutorialPages = tutorialPages;
			this.tutorialParentNode = tutorialParentNode;
			this.tutorialSectionNodes = tutorialSectionNodes;
		}
	}
	
	/** A tutorial tree parent node or section node. */
	protected static class TutorialTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;
		
		/** The name of the tutorial the node is for */
		private String tutorialName;
		
		/** The row for the node in the tree */
		private int row;
		
		/** The tutorial tree node */		
		private org.mitre.icarus.cps.exam.base.training.TutorialTreeNode node;
		
		/** Whether the node is enabled */
		private boolean enabled = true;
		
		public TutorialTreeNode() {}
		
		public TutorialTreeNode(String tutorialName,
				org.mitre.icarus.cps.exam.base.training.TutorialTreeNode node) {
			this.tutorialName = tutorialName;
			this.node = node;
		}

		public String getTutorialName() {
			return tutorialName;
		}

		public void setTutorialName(String tutorialName) {
			this.tutorialName = tutorialName;
		}

		public org.mitre.icarus.cps.exam.base.training.TutorialTreeNode getNode() {
			return node;
		}

		public void setNode(org.mitre.icarus.cps.exam.base.training.TutorialTreeNode node) {
			this.node = node;
		}
		
		public boolean isSelectable() {
			return enabled && (node != null ? node.getPageIndex() != null : false);
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		@Override
		public String toString() {
			return node != null ? node.getPageOrSectionName() : "";
		}		
	}	
	
	protected static class TutorialTreeNodeRenderer implements TreeCellRenderer {

		private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();		
		
		//protected static Font parentFont;		
		//protected static Font childFont;
		/*private static Color textForeground, textBackground;		
		static {
			textForeground = UIManager.getColor("Tree.textForeground");
			textBackground = UIManager.getColor("Tree.textBackground");
		}*/

		protected JLabel getRenderer() {
			return renderer;
		}

		public TutorialTreeNodeRenderer() {
			//renderer.setFont(WidgetConstants.FONT_INSTRUCTION_PANEL);			
			/*Font fontValue;
			fontValue = UIManager.getFont("Tree.font");
			if (fontValue != null) {
				renderer.setFont(fontValue);
			}*/
			//renderer.setForeground(fg);
			renderer.setBackground(Color.WHITE);
			//Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			if(value instanceof TutorialTreeNode) {
				TutorialTreeNode node = (TutorialTreeNode)value;				
				//renderer.setNode(node);
				renderer.setFont(tree.getFont());
				renderer.setEnabled(node.isEnabled());
				if(node.isSelectable()) {
					renderer.setText("<html><a href=''>" + node.toString() + "</a></html>");
				} else {
					renderer.setText(node.toString());
				}
				/*if(value instanceof TutorialTreeParentNode) {
					renderer.setPreferredSize(new Dimension(renderer.getPreferredSize().width,
							renderer.getPreferredSize().height + 50));
				}*/
			}				
			return renderer;
		}
	}
}
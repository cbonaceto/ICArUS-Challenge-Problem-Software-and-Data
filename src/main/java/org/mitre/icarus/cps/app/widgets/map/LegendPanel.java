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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.mitre.icarus.cps.app.widgets.ImageManager;

/**
 * Map legend panel.
 * 
 * @author CBONACETO
 *
 */
public class LegendPanel extends JPanel {
	
	private static final long serialVersionUID = 959005309754333232L;
	
	/** The legend tree */
	protected JTree legendTree;
	
	/** The root node */
	protected DefaultMutableTreeNode rootNode;
	
	/** The tree model */
	protected DefaultTreeModel treeModel;
	
	/** Contains the top level nodes in the layer tree mapped by the legend item associated with the node */
	protected Map<ParentLegendItem, LegendTreeNode> legendNodes;	
	
	public LegendPanel() {
		this(null);
	}
	
	public LegendPanel(Collection<ParentLegendItem> legendItems) {
		super(new BorderLayout());
		
		//Change the default tree expansion images
		UIManager.put("Tree.expandedIcon", ImageManager.getImageIcon(ImageManager.MINUS_ICON));
		UIManager.put("Tree.collapsedIcon", ImageManager.getImageIcon(ImageManager.PLUS_ICON));
		updateUI();	
		
		legendNodes = new HashMap<ParentLegendItem, LegendTreeNode>(10);
		
		legendTree = new JTree();
		legendTree.setRowHeight(0);
		legendTree.setRootVisible(true);
		legendTree.setCellRenderer(new LegendTreeCellRenderer());

		JScrollPane sp = new JScrollPane(legendTree);
		sp.setBorder(null);
		add(sp);
		
		createLegendTreeModel(legendItems);
	}
	
	protected void createLegendTreeModel(Collection<ParentLegendItem> legendItems) {		
		rootNode = new DefaultMutableTreeNode();
		legendNodes.clear();
		
		if(legendItems != null) {
			for(ParentLegendItem legendItem : legendItems) {
				LegendTreeNode parentNode = new LegendTreeNode(legendItem);	
				parentNode.row = rootNode.getChildCount() + 1;
				legendNodes.put(legendItem, parentNode);				
				rootNode.add(parentNode);
				if(legendItem.getChildren() != null) {
					for(LegendItem childLegendItem : legendItem.getChildren()) {
						LegendTreeNode childNode = new LegendTreeNode(childLegendItem);
						parentNode.add(childNode);
					}
				}
			}
		}
		
		treeModel = new DefaultTreeModel(rootNode);
		legendTree.setModel(treeModel);
		for(LegendTreeNode node : legendNodes.values()) {
			legendTree.expandRow(node.row);
		}
		legendTree.repaint();
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(legendTree != null) {
			legendTree.setBackground(bg);
		}
	}
	
	/**
	 * Set the legend items.
	 * 
	 * @param legendItems the legend items
	 */
	public void setLegendItems(Collection<ParentLegendItem> legendItems) {
		createLegendTreeModel(legendItems);
	}
	
	/**
	 * Add a legend item.
	 */
	public void addLegendItem(ParentLegendItem legendItem) {
		LegendTreeNode parentNode = new LegendTreeNode(legendItem);
		parentNode.legendItem.visible = true;
		parentNode.row = legendTree.getRowCount();
		legendNodes.put(legendItem, parentNode);
		if(legendItem.getChildren() != null) {
			for(LegendItem childLegendItem : legendItem.getChildren()) {
				LegendTreeNode childNode = new LegendTreeNode(childLegendItem);
				parentNode.add(childNode);
			}
		}
		rootNode.add(parentNode);
		boolean[] expandedRows = new boolean[legendTree.getRowCount()];
		for(int row = 0; row < expandedRows.length; row++) {
			expandedRows[row] = legendTree.isExpanded(row);
		}
		treeModel.nodeStructureChanged(rootNode);
		for(int row = 0; row < expandedRows.length; row++) {
			if(expandedRows[row]) {
				legendTree.expandRow(row);
			}
		}		
		legendTree.expandRow(parentNode.row);		
		legendTree.repaint();
	}
	
	/**
	 * Update the given legend item.
	 * 
	 * @param legendItem the legend item to update
	 */
	public void updateLegendItem(ParentLegendItem legendItem) {
		LegendTreeNode parentNode = legendNodes.get(legendItem);
		if(parentNode != null) {
			parentNode.removeAllChildren();
			if(legendItem.getChildren() != null) {
				for(LegendItem childLegendItem : legendItem.getChildren()) {
					LegendTreeNode childNode = new LegendTreeNode(childLegendItem);
					parentNode.add(childNode);
				}
			}			
			treeModel.nodeStructureChanged(parentNode);
			for(int row=0; row<legendTree.getRowCount(); row++) {
				Object[] items = legendTree.getPathForRow(row).getPath();
				if(items != null && items.length > 1) {
					if(items[1] == parentNode) {
						parentNode.row = row;
						break;
					}						
				}
			}
			legendTree.expandRow(parentNode.row);
			legendTree.repaint();
		}
	}
	
	/**
	 * Remove a legend item.
	 */
	public void removeLegendItem(ParentLegendItem legendItem) {
		LegendTreeNode parentNode = legendNodes.get(legendItem);
		if(parentNode != null) {
			if(parentNode.legendItem != null) {
				/*if(parentNode.legendItem instanceof ParentLegendItem) {
					((ParentLegendItem)parentNode.legendItem).children = null;
				}*/
				parentNode.legendItem.visible = false;
			}
			legendNodes.remove(parentNode);
			treeModel.removeNodeFromParent(parentNode);
			legendTree.repaint();
		}
	}
	
	/**
	 * Remove all legend items.
	 */
	public void removeAllLegendItems() {
		if(legendNodes != null && !legendNodes.isEmpty()) {
			for(LegendTreeNode node : legendNodes.values()) {
				if(node.legendItem != null) {
					/*if(node.legendItem instanceof ParentLegendItem) {
						((ParentLegendItem)node.legendItem).children = null;
					}*/
					node.legendItem.visible = false;
				}
			}
		}
		legendNodes.clear();
		rootNode.removeAllChildren();
		treeModel.nodeStructureChanged(rootNode);
		legendTree.repaint();
	}		
	
	public static class LegendItem {
		
		protected String name;

		protected Icon icon;	
		
		protected boolean visible = true;

		public LegendItem() {}		
		
		public LegendItem(String name) {
			this.name = name;
		}

		public LegendItem(String name, Icon icon) {
			this.name = name;
			this.icon = icon;
		}		

		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		public Icon getIcon() {
			return icon;
		}
		
		public void setIcon(Icon icon) {
			this.icon = icon;
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}
	}
	
	public static class ParentLegendItem extends LegendItem {
		
		protected Collection<LegendItem> children;

		public ParentLegendItem() {}		

		public ParentLegendItem(String name, Icon icon) {
			super(name, icon);
		}		
		
		public Collection<LegendItem> getChildren() {
			return children;
		}
		
		public void setChildren(Collection<LegendItem> children) {
			this.children = children;
		}
	}

	protected static class LegendTreeNode extends DefaultMutableTreeNode {	

		private static final long serialVersionUID = -7669713736827742968L;
		
		protected LegendItem legendItem;
		
		protected int row;
		
		public LegendTreeNode(LegendItem legendItem) {
			super(legendItem);
			this.legendItem = legendItem;
		}

		public LegendItem getLegendItem() {
			return legendItem;
		}

		@Override
		public LegendItem getUserObject() {
			return legendItem;
		}		
	}
	
	protected class LegendTreeCellRenderer extends DefaultTreeCellRenderer {
		
		private static final long serialVersionUID = 8156350192800010593L;		
		
		public LegendTreeCellRenderer() {
			setIcon(null);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
				boolean expanded, boolean leaf,	int row, boolean hasFocus) {
			
			super.getTreeCellRendererComponent(
	                tree, value, sel,
	                expanded, leaf, row,
	                hasFocus);		
			
			if(value instanceof LegendTreeNode) {
				LegendTreeNode node = (LegendTreeNode)value;
				//if(!node.legendItem.visible) {
				//	return null;
				//}
				//else {
				setText(node.legendItem.name);
				setIcon(node.legendItem.icon);
				//}
			}
			else if(value instanceof DefaultMutableTreeNode) {				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
				if(node.isRoot()) {
					setIcon(null);
				}
			}	
			return this;
		}
	}
}
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.IMapPanel_Phase1;

/**
 * ILayerPanel implementations that uses a JTree to display and select the layers.
 * 
 * @author CBONACETO
 *
 */
public class LayerTreePanel extends JPanel implements ILayerPanel, TreeSelectionListener {
	
	private static final long serialVersionUID = 6885584458997208025L;	
	
	/** Reference to the map containing the layers */
	protected IMapPanel map;
	
	/** The tree containing the layers */
	protected JTree layerTree;
	
	/** The tree model */
	protected DefaultTreeModel treeModel;
	
	/** The tree nodes for each layer (mapped by layer ID) */
	protected Map<String, LayerTreeNode> layerNodes;
	
	/** The root node */
	protected DefaultMutableTreeNode rootNode;
	
	public LayerTreePanel(IMapPanel_Phase1 map) {
		this(map, new LinkedList<ILayer<? extends IMapObject>>());
	}
	
	public LayerTreePanel(IMapPanel_Phase1 map, Collection<ILayer<? extends IMapObject>> layers) {
		super(new BorderLayout());
		this.map = map;
		layerNodes = new HashMap<String, LayerTreeNode>(10);
		
		layerTree = new JTree();
		layerTree.setRowHeight(0);
		setLayers(layers);		
		layerTree.setRootVisible(false);
		layerTree.setCellRenderer(new LayerTreeNodeRenderer());
		layerTree.setCellEditor(new LayerTreeNodeEditor(layerTree, map));
		layerTree.setEditable(true);

		JScrollPane sp = new JScrollPane(layerTree);
		sp.setBorder(null);
		add(sp);
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
		if(layerTree != null) {
			layerTree.setBackground(bg);
		}
	}
	
	/**
	 * Set the layers in the layer panel.
	 * 
	 * @param layers the layers
	 */
	@Override
	public void setLayers(Collection<ILayer<? extends IMapObject>> layers) {
		createLayerTreeModel(layers);
	}
	
	/**
	 * Add a layer to the layer panel
	 */
	@Override
	public void addLayer(ILayer<? extends IMapObject> layer) {
		LayerTreeNode node = new LayerTreeNode(layer);		
		layerNodes.put(layer.getId(), node);
		rootNode.add(node);
		treeModel.nodeStructureChanged(rootNode);
		layerTree.repaint();
	}
	
	/**
	 * Remove a layer from the layer panel
	 */
	@Override
	public void removeLayer(ILayer<? extends IMapObject> layer) {
		LayerTreeNode node = layerNodes.get(layer.getId());
		if(node != null) {
			layerNodes.remove(layer.getId());
			treeModel.removeNodeFromParent(node);
			layerTree.repaint();
		}
	}
	
	/**
	 * Remove all layers from the layer panel
	 */
	@Override
	public void removeAllLayers() {
		layerNodes.clear();
		rootNode.removeAllChildren();
		treeModel.nodeStructureChanged(rootNode);
		layerTree.repaint();
	}	
	
	@Override
	public JComponent getLayerPanelComponent() {
		return this;
	}

	protected void createLayerTreeModel(Collection<ILayer<? extends IMapObject>> layers) {		
		rootNode = new DefaultMutableTreeNode();
		layerNodes.clear();
		
		if(layers != null) {
			for(ILayer<? extends IMapObject> layer : layers) {
				LayerTreeNode node = new LayerTreeNode(layer);
				layerNodes.put(layer.getId(), node);
				rootNode.add(node);
			}
		}
		
		treeModel = new DefaultTreeModel(rootNode);
		layerTree.setModel(treeModel);
		layerTree.repaint();
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {}
	
	public static class LayerTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;
		
		ILayer<? extends IMapObject> layer;
		
		public LayerTreeNode(ILayer<? extends IMapObject> layer) {
			this.layer = layer;
		}	

		public ILayer<? extends IMapObject> getLayer() {
			return layer;
		}
		
		

		@Override
		public String toString() {
			if(layer != null) {
				return layer.toString();
			}
			return null;
		}		
	}	
	
	public static class JLayerTreeCheckBox extends JLayerPanelCheckBox {
		
		private static final long serialVersionUID = 1L;
		
		protected LayerTreeNode node;		

		public LayerTreeNode getNode() {
			return node;
		}

		public void setNode(LayerTreeNode node) {
			this.node = node;
		}
	}
	
	public static class LayerTreeNodeRenderer implements TreeCellRenderer {
		
		private JLayerTreeCheckBox leafRenderer = new JLayerTreeCheckBox();

		private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

		protected Color selectionBorderColor, selectionForeground, selectionBackground,
		textForeground, textBackground;

		protected JLayerTreeCheckBox getLeafRenderer() {
			return leafRenderer;
		}

		public LayerTreeNodeRenderer() {
			Font fontValue;
			fontValue = UIManager.getFont("Tree.font");
			if (fontValue != null) {
				leafRenderer.setFont(fontValue);
			}
			leafRenderer.setBackground(Color.WHITE);
			Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
			leafRenderer.checkBox.setFocusPainted((booleanValue != null)
					&& (booleanValue.booleanValue()));

			selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
			selectionForeground = UIManager.getColor("Tree.selectionForeground");
			selectionBackground = UIManager.getColor("Tree.selectionBackground");
			textForeground = UIManager.getColor("Tree.textForeground");
			textBackground = UIManager.getColor("Tree.textBackground");
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			Component returnValue;
			
			if(leaf) {
				String stringValue = tree.convertValueToText(value, selected,
						expanded, leaf, row, false);
				leafRenderer.checkBox.setText(stringValue);
				leafRenderer.checkBox.setSelected(false);

				leafRenderer.setEnabled(tree.isEnabled());				
				
				if(value != null && value instanceof LayerTreeNode) {
					LayerTreeNode node = (LayerTreeNode)value;
					leafRenderer.setNode(node);
					leafRenderer.checkBox.setText(node.toString());
					ILayer<? extends IMapObject> layer = node.getLayer();
					if(layer != null) {						
						leafRenderer.checkBox.setSelected(layer.isVisible());
						//leafRenderer.setEnabled(layer.isVisibilityUserControllable());
						if(layer.getIcon() != null) {
							leafRenderer.setLayerIcon(new ImageIcon(layer.getIcon()));
						}
						else {
							leafRenderer.setLayerIcon(null);
						}
					}
				}				
				returnValue = leafRenderer;
			} else {
				returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,
						value, selected, expanded, leaf, row, hasFocus);
			}
			return returnValue;
		}
	}

	public static class LayerTreeNodeEditor extends AbstractCellEditor implements TreeCellEditor {
		private static final long serialVersionUID = 1L;

		protected LayerTreeNodeRenderer renderer = new LayerTreeNodeRenderer();

		//private ChangeEvent changeEvent = null;

		protected JTree tree;
		
		protected IMapPanel_Phase1 map;

		public LayerTreeNodeEditor(JTree tree, IMapPanel_Phase1 map) {
			this.tree = tree;
			this.map = map;
		}

		public Object getCellEditorValue() {
			JLayerTreeCheckBox checkbox = renderer.getLeafRenderer();
			return checkbox.getNode();
		}

		public boolean isCellEditable(EventObject event) {
			boolean returnValue = false;
			if (event instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent) event;
				TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
				if (path != null) {
					Object node = path.getLastPathComponent();
					if ((node != null) && (node instanceof LayerTreeNode)) {						
						returnValue = ((LayerTreeNode)node).isLeaf();						
					}
				}
			}
			return returnValue;
		}

		public Component getTreeCellEditorComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row) {			
			
			Component editor = renderer.getTreeCellRendererComponent(tree, value,
					true, expanded, leaf, row, true);
			
			if(editor instanceof JLayerTreeCheckBox) {
				final JLayerTreeCheckBox checkbox = (JLayerTreeCheckBox)editor;
				if(checkbox.checkBox.getActionListeners() == null || 
						checkbox.checkBox.getActionListeners().length == 0) {
					checkbox.checkBox.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//Redraw map				
							stopCellEditing();
							checkbox.getNode().getLayer().setVisible(checkbox.checkBox.isSelected());
							map.redraw(); 
						}
					});
				}
			}			

			return editor;
		}
	}
}
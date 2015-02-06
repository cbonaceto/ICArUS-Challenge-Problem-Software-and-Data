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
package org.mitre.icarus.cps.app.widgets.map.phase_05;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.mitre.icarus.cps.app.widgets.phase_05.ImageManager;

/**
 * A class for creating the JTree model for displaying a list of layers.
 * 
 * @author Jing Hu
 *
 */
public class LayerTree implements TreeSelectionListener {
	
	public LayerTreeNode createLayerTree(FeatureVector world) {
		LayerTreeNode node = new LayerTreeNode(world);		
		
		for (Layer l : world.getLayers()) {
			if (l.isEnabled() && l.isShowInLayerTree()) {
				node.add(new LayerTreeNode(l));
			}
		}
		
		return node;
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {		
	}
	
	public static class LayerTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;
		
		Feature feature;
		
		public LayerTreeNode(Feature feature) {
			this.feature = feature;
		}	

		public Feature getFeature() {
			return feature;
		}

		@Override
		public String toString() {
			return feature.getName();
		}		
	}	
	
	public static class JLayerTreeCheckBox extends JPanel {
		private static final long serialVersionUID = 1L;
		
		protected JCheckBox checkBox;
		
		private JLabel icon;
		
		protected LayerTreeNode node;
	
		public JLayerTreeCheckBox() {
			super(new GridBagLayout());
			this.checkBox = new JCheckBox();
			this.icon = new JLabel();
			icon.setPreferredSize(ImageManager.IconSize);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets.left = 5;
			add(icon, gbc);
			gbc.gridx = 1;
			add(checkBox, gbc);			
		}
		
		@Override
		public void setEnabled(boolean enabled) {
			checkBox.setEnabled(enabled);
			icon.setEnabled(enabled);
		}

		@Override
		public void setBackground(Color bg) {
			super.setBackground(bg);
			if(checkBox != null) {
				checkBox.setBackground(bg);
			}
			if(icon != null) {
				icon.setBackground(bg);
			}
		}

		public void setLayerIcon(Icon icon) {
			this.icon.setIcon(icon);
		}
		
		public Icon getLayerIcon() {
			return icon.getIcon();
		}

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

		Color selectionBorderColor, selectionForeground, selectionBackground,
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
			
			if (leaf) {

				String stringValue = tree.convertValueToText(value, selected,
						expanded, leaf, row, false);
				leafRenderer.checkBox.setText(stringValue);
				leafRenderer.checkBox.setSelected(false);

				leafRenderer.setEnabled(tree.isEnabled());

				/*if (selected) {
					leafRenderer.setForeground(selectionForeground);
					leafRenderer.setBackground(selectionBackground);
				} else {
					leafRenderer.setForeground(textForeground);
					leafRenderer.setBackground(textBackground);
				}*/
				
				if(value != null && value instanceof LayerTreeNode) {
					LayerTreeNode node = (LayerTreeNode)value;
					leafRenderer.setNode(node);
					leafRenderer.checkBox.setText(node.toString());						
					leafRenderer.checkBox.setSelected(node.getFeature().isVisible());
					if(node.getFeature() instanceof Layer) {
						Layer layer = (Layer)node.getFeature();
						leafRenderer.setEnabled(layer.isVisibilityUserControllable());
						if(layer.getIcon() != null) {
							leafRenderer.setLayerIcon(layer.getIcon());
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

		LayerTreeNodeRenderer renderer = new LayerTreeNodeRenderer();

		ChangeEvent changeEvent = null;

		JTree tree;
		
		FeatureVectorPanel scene;

		public LayerTreeNodeEditor(JTree tree, FeatureVectorPanel scene) {
			this.tree = tree;
			this.scene = scene;
		}

		public Object getCellEditorValue() {
			JLayerTreeCheckBox checkbox = renderer.getLeafRenderer();
			//checkbox.getNode().feature.setVisible(checkbox.isSelected());
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
							//Redraw scene				
							stopCellEditing();
							checkbox.getNode().feature.setVisible(checkbox.checkBox.isSelected());
							scene.repaint(); 
						}
					});
				}
			}
			// editor always selected / focused		
			/*if(editor instanceof JCheckBox) {
				final JCheckBox checkbox = (JCheckBox)editor;				
				if(checkbox.getItemListeners() == null || checkbox.getItemListeners().length == 0) {
					ItemListener itemListener = new ItemListener() {
						public void itemStateChanged(ItemEvent itemEvent) {
							System.out.println("stopped editing, value: " + checkbox.isSelected());
							if (stopCellEditing()) {
								fireEditingStopped();
							}
						}
					};
					checkbox.addItemListener(itemListener);
				}
			}*/

			return editor;
		}
	}
}

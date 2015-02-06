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
package org.mitre.icarus.cps.app.widgets.phase_1.experiment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.mitre.icarus.cps.app.widgets.phase_1.ColorManager_Phase1;
import org.mitre.icarus.cps.experiment_core.gui.JPanelConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * A panel to show the attack wave history for Task 7.
 * 
 * @author CBONACETO
 *
 */
public class AttackHistoryPanel extends JPanelConditionComponent {
	
	private static final long serialVersionUID = 1L;
	
	/** The group attack history */
	protected LinkedList<Attack> attackHistory;
	
	protected int maxAttacksToShow = 5;
	
	protected JTable attackTable;
	
	protected DefaultTableModel tableModel;
	
	int currColumn = 1;

	public AttackHistoryPanel(int maxAttacksToShow, String title) {
		super("attack_history");		
		this.maxAttacksToShow = maxAttacksToShow;
		attackHistory = new LinkedList<Attack>();
		
		setBackground(Color.white);
		//setBorder(BorderFactory.createLineBorder(Color.darkGray));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.left = 5;
		gbc.insets.right = 5;
		gbc.weightx = 0;
		
		if(title != null) {
			JLabel titleLabel = new JLabel("<html>" + title + "</html>");			
			add(titleLabel, gbc);
		}
		
		Object[] columnNames = new Object[6];
		columnNames[0] = "Trial";
		for(int i=1; i<= maxAttacksToShow; i++) {
			columnNames[i] = Integer.toString(i);
		}
		tableModel = new DefaultTableModel(columnNames, 2);
		tableModel.setValueAt("Trial:", 0, 0);
		tableModel.setValueAt("Group:", 1, 0);
		attackTable = new JTable(tableModel);
		attackTable.setDefaultRenderer(Object.class, new CellRenderer());
		int columnWidth = new JLabel("9999").getPreferredSize().width;
		//attackTable.getColumn("Trial").setWidth(new JLabel("Group").getPreferredSize().width);
		attackTable.getColumn("Trial").setMaxWidth(new JLabel("Group").getPreferredSize().width + 6);
		for(int i=1; i<= maxAttacksToShow; i++) {
			TableColumn column = attackTable.getColumn(Integer.toString(i));
			column.setWidth(columnWidth);
			column.setMinWidth(columnWidth);
			column.setMaxWidth(columnWidth);
		}
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets.left = 0;
		gbc.insets.right = 0;
		gbc.weightx = 1;	
		attackTable.setShowGrid(true);
		attackTable.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.gridColor")));
		add(attackTable, gbc);		
	}	
	
	public void addAttack(GroupType attackingGroup, int trialNum) {
		if(currColumn > maxAttacksToShow) {
			//TODO: Remove the first column and shift all columns to the left			
			attackHistory.poll();
			int column = 1;
			for(Attack attack : attackHistory) {
				attackTable.setValueAt(Integer.toString(attack.trialNum), 0, column);
				attackTable.setValueAt(attack.group, 1, column);
				column++;
			}
			currColumn = maxAttacksToShow;
		}		
		attackHistory.add(new Attack(attackingGroup, trialNum));
		attackTable.setValueAt(Integer.toString(trialNum), 0, currColumn);
		attackTable.setValueAt(attackingGroup, 1, currColumn);
		currColumn++;
	}
	
	public void clearAttackHistory() {
		for(int row=0; row<2; row++) {
			for(int column = 1; column <= maxAttacksToShow; column++) {
				attackTable.setValueAt("", row, column);
			}
		}
		currColumn = 1;
		attackHistory.clear();
	}
	
	protected static class Attack {
		
		public GroupType group;
		
		public int trialNum;
		
		public Attack(GroupType group, int trialNum) {
			this.group = group;
			this.trialNum = trialNum;
		}
	}
	
	protected static class CellRenderer extends DefaultTableCellRenderer {
		
		private static final long serialVersionUID = 1L;
		
		protected static Font plainFont = new JLabel().getFont().deriveFont(Font.PLAIN);
		
		protected static Font boldFont = plainFont.deriveFont(Font.BOLD);

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setHorizontalAlignment(JLabel.CENTER);
			if(value != null) {
				setText(value.toString());
			}
			else {
				setText("");
			}
			if(value != null && value instanceof GroupType) {
				setFont(boldFont);
				setForeground(ColorManager_Phase1.getGroupCenterColor((GroupType)value));
			}		
			else {
				setFont(plainFont);
				setForeground(Color.black);
			}
			return this;
		}				
	}
}
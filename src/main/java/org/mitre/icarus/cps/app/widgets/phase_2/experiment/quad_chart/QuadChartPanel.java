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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.quad_chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.PayoffMatrix;
import org.mitre.icarus.cps.experiment_core.gui.IConditionComponent;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * @author CBONACETO
 *
 */
@SuppressWarnings("unused")
public class QuadChartPanel<T extends IQuadChartComponent> extends JPanel implements IConditionComponent {

	private static final long serialVersionUID = -407362356227150728L;
	
	/** The layout */
	private GridBagLayout gbl;
	
	/** Content panel contains everything other than the title label */
	protected JPanel contentPanel;
	
	/** Whether the outer content panel border is visible */
	protected boolean outerBorderVisible;
	
	/** The title label */
	protected JLabel titleLabel;

	/** The quad chart cells */
	protected ArrayList<T> cells;
	
	/** The quad chart cell panels and cell panel container */
	protected ArrayList<JPanel> cellPanels;
	protected JPanel cellContainer;
	
	/** Whether the cell outer border is visible */
	protected boolean cellOuterBorderVisible;
	
	/** Whether the cell inner borders are visible */
	protected boolean cellInnerBordersVisible;	
	
	/** The column heading labels */
	protected ArrayList<JLabel> columnLabels;
	protected JPanel columnLabelContainer;	
	
	/** The row heading labels */
	protected ArrayList<JLabel> rowLabels;
	protected JPanel rowLabelContainer;
	
	/** Filler component at the intersection of the row and column headings */
	protected JComponent cornerComponent;
	
	/** Whether the row and column heading borders are visible */
	protected boolean headingBordersVisible;	
	
	/** The border color */
	protected Color borderColor = Color.BLACK;
	
	/** The component ID */
	protected String componentId;
	
	public QuadChartPanel(Collection<T> cells) {
		this(cells, SwingConstants.BOTTOM, SwingConstants.LEFT);
	}
	
	public QuadChartPanel(Collection<T> cells, final int columnHeadingOrientation, final int rowHeadingOrientation) {
		setLayout(gbl = new GridBagLayout());
		if(cells == null || cells.size() != 4) {
			throw new IllegalArgumentException("Cells empty or not initialized with 4 values");
		}
		if(columnHeadingOrientation != SwingConstants.TOP && 
				columnHeadingOrientation != SwingConstants.BOTTOM) {
			throw new IllegalArgumentException(
					"colmnHeadingOrienation must be one of SwingConstants.TOP or SwingConstants.BOTTOM");
		}
		if(rowHeadingOrientation != SwingConstants.LEFT && 
				rowHeadingOrientation != SwingConstants.RIGHT) {
			throw new IllegalArgumentException("rowHeadingOrienation must be one of SwingConstants.LEFT or SwingConstants.RIGHT");
		}
		int horizontalSpacing = 4;
		int verticalSpacing = 4;
		GridBagConstraints gbc = new GridBagConstraints();		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		//Create the title label		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets.bottom = verticalSpacing;
		add(titleLabel = new JLabel(), gbc);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.insets.bottom = 0;
		
		//Create the content panel
		contentPanel = new JPanel(new GridBagLayout());
		gbc.gridy = 1;		
		add(contentPanel, gbc);
		
		//Create the cell panels
		cellOuterBorderVisible = true;
		cellInnerBordersVisible = true;
		this.cells = new ArrayList<T>(4);
		cellPanels = new ArrayList<JPanel>(4);
		cellContainer = new JPanel(new GridLayout(2, 2));
		cellContainer.setBorder(BorderFactory.createLineBorder(borderColor));		
		for(T cell : cells) {
			JPanel cellPanel = new JPanel(new BorderLayout());
			cellPanel.setBorder(BorderFactory.createLineBorder(borderColor));
			cellPanel.add(cell.getComponent());
			cellPanels.add(cellPanel);
			//cell.getComponent().setOpaque(true);
			//cell.getComponent().setBackground(Color.blue);
			this.cells.add(cell);
			cellContainer.add(cellPanel);
		}
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		contentPanel.add(cellContainer, gbc);
		//gbc.fill = GridBagConstraints.NONE;
		
		//Create the column headings
		columnLabels = new ArrayList<JLabel>(2);
		gbc.gridx = 1;
		gbc.ipady = verticalSpacing * 2;
		gbc.gridy = columnHeadingOrientation == SwingConstants.TOP ? 0 : 2;		
		columnLabelContainer = new JPanel(new GridLayout(1, 2));
		contentPanel.add(columnLabelContainer, gbc);
		gbc.ipady = 0;
		for(int i=0; i<2; i++) {
			JLabel columnLabel = new JLabel();
			columnLabel.setHorizontalAlignment(SwingConstants.CENTER);
			columnLabels.add(columnLabel);
			columnLabelContainer.add(columnLabel);
		}
		gbc.insets.top = 0;
		gbc.insets.bottom = 0;
		
		//Create the row headings
		rowLabels = new ArrayList<JLabel>(2);		
		gbc.gridy = 1;
		gbc.ipadx = horizontalSpacing * 2;
		gbc.gridx = rowHeadingOrientation == SwingConstants.LEFT ? 0 : 2;		
		rowLabelContainer = new JPanel(new GridLayout(2, 1));
		contentPanel.add(rowLabelContainer, gbc);
		gbc.ipadx = 0;
		for(int i=0; i<2; i++) {
			JLabel rowLabel = new JLabel();
			rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
			rowLabels.add(rowLabel);
			rowLabelContainer.add(rowLabel);
		}
		
		cornerComponent = new JPanel();
		cornerComponent.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				GridBagConstraints gbc = gbl.getConstraints(titleLabel);
				if(rowHeadingOrientation == SwingConstants.LEFT) {
					 gbc.insets.left = cornerComponent.getWidth();
				} else {
					gbc.insets.right = cornerComponent.getWidth();
				}
				gbl.setConstraints(titleLabel, gbc);
				revalidate();
			}
		});
		gbc.gridx = rowHeadingOrientation == SwingConstants.LEFT ? 0 : 2;
		gbc.gridy = columnHeadingOrientation == SwingConstants.TOP ? 0 : 2;
		contentPanel.add(cornerComponent, gbc);		
	}	
	
	@Override
	public void setBackground(Color background) {		
		super.setBackground(background);
		if(cellContainer != null) {
			cellContainer.setBackground(background);
		}		
		if(cornerComponent != null) {
			cornerComponent.setBackground(background);
		}
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
	
	public void setTitleVisible(boolean visible) {
		if(visible != titleLabel.isVisible()) {
			titleLabel.setVisible(visible);
			revalidate();
			repaint();
		}
	}
	
	public void setTitleFont(Font font) {
		titleLabel.setFont(font);
		revalidate();
		repaint();
	}
	
	public void setTitleForeground(Color color) {
		titleLabel.setForeground(color);
	}
	
	public T getCell(int cellRow, int cellColumn) {
		return getCell(2 * cellRow + cellColumn);
	}
	
	public T getCell(int cellIndex) {
		if(cellIndex >= 0 && cellIndex < 4) {
			return cells.get(cellIndex);
		}
		return null;
	}	
	
	public Dimension getCellPreferredSize() {
		return cellPanels.get(0).getPreferredSize();
	}
	
	/**
	 * Set the preferred size for all cells.
	 * 
	 * @param size
	 */
	public void setCellPreferredSize(Dimension size) {
		for(JPanel cellPanel : cellPanels) {
			cellPanel.setPreferredSize(size);
		}
		revalidate();
		repaint();
	}
	
	/**
	 * Set the font for all cells.
	 * 
	 * @param font
	 */
	public void setCellFont(Font font) {
		for(T cell : cells) {
			cell.getComponent().setFont(font);
		}
	}
	
	/**
	 * Set the foreground color for all cells.
	 * 
	 * @param foreground
	 */
	public void setCellForeground(Color foreground) {
		for(T cell : cells) {
			cell.getComponent().setForeground(foreground);
		}
	}
	
	/**
	 * Set the background color for all cells
	 * 
	 * @param background
	 */
	public void setCellBackground(Color background) {
		cellContainer.setBackground(background);
		for(int i=0; i<4; i++) {
			cellPanels.get(i).setBackground(background);
			cells.get(i).getComponent().setBackground(background);
		}
	}
	
	/**
	 * Set the background color for the given cell.
	 * 
	 * @param cellRow
	 * @param cellColumn
	 * @param background
	 */
	public void setCellBackground(int cellRow, int cellColumn, Color background) {
		setCellBackground(2 * cellRow + cellColumn, background);
	}

	/**
	 * Set the background color for the given cell.
	 * 
	 * @param cellIndex
	 * @param background
	 */
	public void setCellBackground(int cellIndex, Color background) {
		if(cellIndex >= 0 && cellIndex < 4) {
			cellPanels.get(cellIndex).setBackground(background);
			cells.get(cellIndex).getComponent().setBackground(background);
		}
	}
	
	/**
	 * Set the color of all borders.
	 * 
	 * @param color
	 */
	public void setBorderColor(Color color) {
		if(color != borderColor) {
			this.borderColor = color;
			updateOuterBorder();
			updateCellOuterBorder();
			updateCellInnerBorders();
			updateHeadingBorders();
		}
	}	
	
	public void setOuterBorderVisible(boolean visible) {
		if(visible != outerBorderVisible) {
			outerBorderVisible = visible;
			updateOuterBorder();
		}
	}
	
	protected void updateOuterBorder() {
		if(outerBorderVisible) {
			contentPanel.setBorder(BorderFactory.createLineBorder(borderColor));
		} else {
			contentPanel.setBorder(null);
		}
	}

	public void setCellOuterBorderVisible(boolean visible) {
		if(visible != cellOuterBorderVisible) {
			cellOuterBorderVisible = visible;
			updateCellOuterBorder();
		}
	}
	
	protected void updateCellOuterBorder() {
		if(cellOuterBorderVisible) {
			cellContainer.setBorder(BorderFactory.createLineBorder(borderColor));				
		} else {
			cellContainer.setBorder(null);
		}
	}

	public void setCellInnerBordersVisible(boolean visible) {
		if(visible != cellInnerBordersVisible) {
			cellInnerBordersVisible = visible;
			updateCellInnerBorders();
		}
	}	
	
	protected void updateCellInnerBorders() {
		for(JPanel cellPanel : cellPanels) {
			if(cellInnerBordersVisible) {
				cellPanel.setBorder(BorderFactory.createLineBorder(borderColor));
			} else {
				cellPanel.setBorder(null);
			}
		}
	}
	
	public void setHeadingBordersVisible(boolean visible) {
		if(visible != headingBordersVisible) {
			headingBordersVisible = visible;
			updateHeadingBorders();
		}
	}
	
	protected void updateHeadingBorders() {
		if(headingBordersVisible) {
			cornerComponent.setBorder(BorderFactory.createLineBorder(borderColor));
		} else {
			cornerComponent.setBorder(null);
		}
		for(JLabel columnLabel : columnLabels) {
			if(headingBordersVisible) {
				columnLabel.setBorder(BorderFactory.createLineBorder(borderColor));
			} else {
				columnLabel.setBorder(null);
			}
		}		
		for(JLabel rowLabel : rowLabels) {
			if(headingBordersVisible) {
				rowLabel.setBorder(BorderFactory.createLineBorder(borderColor));
			} else {
				rowLabel.setBorder(null);
			}
		} 
	}	
	
	public void setColumnHeadings(List<String> columnHeadings) {
		if(columnHeadings != null && columnHeadings.size() == 2) {
			int i = 0;
			for(String columnHeading : columnHeadings) {
				columnLabels.get(i).setText(columnHeading);
				i++;
			}
		}
	}
	
	public void setColumnHeadingsVisible(boolean visible) {
		if(visible != columnLabelContainer.isVisible()) {
			columnLabelContainer.setVisible(visible);
			revalidate();
			repaint();
		}
	}
	
	public void setColumnHeadingsForeground(Color foreground) {
		for(JLabel columnLabel : columnLabels) {
			columnLabel.setForeground(foreground);
		}
	}
	
	public void setColumnHeadingsBackground(Color background) {
		columnLabelContainer.setBackground(background);
	}
	
	public void setRowHeadings(List<String> rowHeadings) {
		if(rowHeadings != null && rowHeadings.size() == 2) {
			int i = 0;
			for(String rowHeading : rowHeadings) {
				rowLabels.get(i).setText(rowHeading);
				i++;
			}
		}
	}	
	
	public void setRowHeadingsVisible(boolean visible) {
		if(visible != rowLabelContainer.isVisible()) {
			rowLabelContainer.setVisible(visible);
			revalidate();
			repaint();
		}
	}
	
	public void setRowHeadingsForeground(Color foreground) {
		for(JLabel rowLabel : rowLabels) {
			rowLabel.setForeground(foreground);
		}
	}
	
	public void setRowHeadingsBackground(Color background) {
		rowLabelContainer.setBackground(background);
	}
	
	public void setHeadingsFont(Font font) {
		for(int i=0; i<2; i++) {
			columnLabels.get(i).setFont(font);
			rowLabels.get(i).setFont(font);
		}
	}	 
	
	/** Test main */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		/*RedTacticParametersPanel rtp = QuadChartPanelFactory.createRedTacticParametersPanel(
				"Probability of Red Attack", 
				BlueBook.createDefaultBlueBook().getMission_1_Tactics().get(0).getTacticParameters(), 
				true, false);*/
		/*RedTacticParametersPanel rtp = QuadChartPanelFactory.createRedTacticParametersPanel(
				"Probability of Red Attack", 0.25d, 3, 2, 5, true);*/
		//frame.getContentPane().add(rtp);
		/*frame.getContentPane().add(QuadChartPanelFactory.createSigintReliabilityPanel(
				SigintReliability.createDefaultSigintReliability()));*/
		frame.getContentPane().add(QuadChartPanelFactory.createPayoffMatrixPanel(
				PayoffMatrix.createDefaultPayoffMatrix()));		
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public String getComponentId() {
		return componentId;
	}

	@Override
	public void setComponentId(String id) {
		this.componentId = id;
	}

	@Override
	public JComponent getComponent() {
		return this;
	}
}
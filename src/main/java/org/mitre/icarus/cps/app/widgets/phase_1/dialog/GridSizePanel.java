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
package org.mitre.icarus.cps.app.widgets.phase_1.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;

/**
 * Panel with fields to configure a grid size.
 * 
 * @author CBONACETO
 *
 */
public class GridSizePanel extends JPanel {

	private static final long serialVersionUID = 143507862716947957L;

	/** The grid size */
	protected GridSize gridSize;
	
	protected JTextField gridWidthField;
	protected JLabel gridWidthErrors;
	
	protected JTextField gridHeightField;
	protected JLabel gridHeightErrors;
	
	protected JTextField milesPerGridUnitField;
	protected JLabel milesPerGridUnitErrors;
	
	protected JTextField latField;
	protected JLabel latErrors;
	
	protected JTextField lonField;
	protected JLabel lonErrors;
	
	public GridSizePanel() {
		this(null);
	}
	
	public GridSizePanel(GridSize gridSize) {
		super(new GridBagLayout());
		createPanel();
		if(gridSize != null) {
			setGridSize(gridSize);
		}
		else {
			this.gridSize = new GridSize();
		}
	}
	
	protected void createPanel() {	
		Dimension textFieldSize = new Dimension(150,
				new JTextField().getPreferredSize().height);
		Dimension errorFieldSize = new JLabel("Miles per grid unit must be a number between 0 and 1").getPreferredSize();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel("Grid Width: "), gbc);
		gridWidthField = new JTextField();
		gridWidthField.setPreferredSize(textFieldSize);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(gridWidthField, gbc);		
		gridWidthErrors = new JLabel();
		gridWidthErrors.setHorizontalAlignment(JLabel.CENTER);
		gridWidthErrors.setPreferredSize(errorFieldSize);
		gridWidthErrors.setForeground(Color.red);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		add(gridWidthErrors, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.top = 4;
		add(new JLabel("Grid Height: "), gbc);
		gridHeightField = new JTextField();
		gridHeightField.setPreferredSize(textFieldSize);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(gridHeightField, gbc);		
		gridHeightErrors = new JLabel();
		gridHeightErrors.setHorizontalAlignment(JLabel.CENTER);
		gridHeightErrors.setPreferredSize(errorFieldSize);
		gridHeightErrors.setForeground(Color.red);
		gbc.gridx = 0;
		gbc.gridy++;	
		gbc.gridwidth = 2;
		gbc.insets.top = 0;		
		add(gridHeightErrors, gbc);		
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.top = 4;
		add(new JLabel("Miles per grid unit: "), gbc);
		milesPerGridUnitField = new JTextField();
		milesPerGridUnitField.setPreferredSize(textFieldSize);
		gbc.gridx = 1;		
		gbc.anchor = GridBagConstraints.WEST;
		add(milesPerGridUnitField, gbc);		
		milesPerGridUnitErrors = new JLabel();
		milesPerGridUnitErrors.setHorizontalAlignment(JLabel.CENTER);
		milesPerGridUnitErrors.setPreferredSize(errorFieldSize);
		milesPerGridUnitErrors.setForeground(Color.red);
		gbc.gridx = 0;
		gbc.gridy++;	
		gbc.gridwidth = 2;
		gbc.insets.top = 0;
		add(milesPerGridUnitErrors, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.top = 4;
		add(new JLabel("Bottom Left Latitude: "), gbc);
		latField = new JTextField();
		latField.setPreferredSize(textFieldSize);
		gbc.gridx = 1;		
		gbc.anchor = GridBagConstraints.WEST;
		add(latField, gbc);		
		latErrors = new JLabel();
		latErrors.setHorizontalAlignment(JLabel.CENTER);
		latErrors.setPreferredSize(errorFieldSize);
		latErrors.setForeground(Color.red);
		gbc.gridx = 0;
		gbc.gridy++;	
		gbc.gridwidth = 2;
		gbc.insets.top = 0;
		add(latErrors, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.top = 4;
		add(new JLabel("Bottom Left Longitude: "), gbc);
		lonField = new JTextField();
		lonField.setPreferredSize(textFieldSize);
		gbc.gridx = 1;		
		gbc.anchor = GridBagConstraints.WEST;
		add(lonField, gbc);		
		lonErrors = new JLabel();
		lonErrors.setHorizontalAlignment(JLabel.CENTER);
		lonErrors.setPreferredSize(errorFieldSize);
		lonErrors.setForeground(Color.red);
		gbc.gridx = 0;
		gbc.gridy++;	
		gbc.gridwidth = 2;
		gbc.insets.top = 0;
		add(lonErrors, gbc);
	}

	public GridSize getGridSize() {
		GridSize gridSize = new GridSize();
		try {
			gridSize.setGridWidth(Integer.parseInt(gridWidthField.getText()));
		} catch(Exception ex) {}
		try {
			gridSize.setGridHeight(Integer.parseInt(gridHeightField.getText()));
		} catch(Exception ex) {}
		try {
			gridSize.setMilesPerGridUnit(Double.parseDouble(milesPerGridUnitField.getText()));
		} catch(Exception ex) {}		
		try {
			gridSize.setBottomLeftLat(Double.parseDouble(latField.getText()));
		} catch(Exception ex) {}
		try {
			gridSize.setBottomLeftLon(Double.parseDouble(lonField.getText()));
		} catch(Exception ex) {}
		return gridSize;
	}

	public void setGridSize(GridSize gridSize) {
		if(gridSize != null) {
			this.gridSize = new GridSize(gridSize);
			updateFields();
		}
	}
	
	protected void updateFields() {
		if(gridSize != null) {
			gridWidthField.setText(gridSize.getGridWidth() == null ? "" : gridSize.getGridWidth().toString());
			gridHeightField.setText(gridSize.getGridHeight() == null ? "" : gridSize.getGridHeight().toString());
			milesPerGridUnitField.setText(gridSize.getMilesPerGridUnit() == null ? "" : gridSize.getMilesPerGridUnit().toString());
			latField.setText(gridSize.getBottomLeftLat() == null ? "" : gridSize.getBottomLeftLat().toString());
			lonField.setText(gridSize.getBottomLeftLon() == null ? "" : gridSize.getBottomLeftLon().toString());
		}		
	}
	
	public void setGridSize(Integer gridWidth, Integer gridHeight,
			Double milesPerGridUnit, Double bottomLeftLat, Double bottomLeftLon) {
		setGridSize(new GridSize(gridWidth, gridHeight, milesPerGridUnit,
				bottomLeftLat, bottomLeftLon));
	}
	
	public boolean validateFields() {
		String errorString = null;
		boolean fieldsValid = true;
		
		try {
			Integer width = Integer.parseInt(gridWidthField.getText());
			if(width <= 0) {
				fieldsValid = false;
				errorString = "Grid width must be greater than 0";
			}
		} catch(Exception ex) {
			fieldsValid = false;
			if(gridWidthField.getText() == null || gridWidthField.getText().isEmpty()) {
				errorString = "Enter grid width";
			} else {
				errorString = "Grid width must be an integer greater than 0";
			}
		}
		gridWidthErrors.setText(errorString);
		
		errorString = null;
		try {
			Integer height = Integer.parseInt(gridHeightField.getText());
			if(height <= 0) {
				fieldsValid = false;
				errorString = "Grid height must be greater than 0";
			}
		} catch(Exception ex) {
			fieldsValid = false;
			if(gridHeightField.getText() == null || gridHeightField.getText().isEmpty()) {
				errorString = "Enter grid height";
			} else {
				errorString = "Grid height must be an integer greater than 0";
			}
		}
		gridHeightErrors.setText(errorString);
		
		errorString = null;
		try {
			Double mpg = Double.parseDouble(milesPerGridUnitField.getText());
			if(mpg <= 0 || mpg > 1) {
				fieldsValid = false;
				errorString = "Miles per grid unit must be between 0 and 1";
			}
		} catch(Exception ex) {
			fieldsValid = false;
			if(milesPerGridUnitField.getText() == null || milesPerGridUnitField.getText().isEmpty()) {
				errorString = "Enter miles per grid unit";
			} else {
				errorString = "Miles per grid unit must be a number between 0 and 1";
			}
		}
		milesPerGridUnitErrors.setText(errorString);
		
		errorString = null;
		try {
			Double lat = Double.parseDouble(latField.getText());
			if(lat < 0 || lat > 90) {
				fieldsValid = false;
				errorString = "Bottom left latitude must be between 0 and 90";
			}
		} catch(Exception ex) {
			fieldsValid = false;
			if(latField.getText() == null || latField.getText().isEmpty()) {
				errorString = "Enter bottom left latitude";
			} else {
				errorString = "Bottom left latitude must be a number between 0 and 90";
			}
		}
		latErrors.setText(errorString);
		
		errorString = null;
		try {
			Double lon = Double.parseDouble(lonField.getText());
			if(lon < 0 || lon > 180) {
				fieldsValid = false;
				errorString = "Bottom left longitude must be between 0 and 180";
			}
		} catch(Exception ex) {
			fieldsValid = false;
			if(lonField.getText() == null || lonField.getText().isEmpty()) {
				errorString = "Enter bottom left longitude";
			} else {
				errorString = "Bottom left longitude must be a number between 0 and 180";
			}
		}
		lonErrors.setText(errorString);
		
		return fieldsValid;
	}
}
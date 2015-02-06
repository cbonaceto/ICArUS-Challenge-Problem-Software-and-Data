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
package org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.mitre.icarus.cps.exam.phase_2.testing.IDatum;

/**
 * 
 * @author CBONACETO
 *
 */
public class DatumLabel extends JPanel {
	
	private static final long serialVersionUID = -3828718578656539691L;	
	
	/** The layout */
	protected GridBagLayout gbl;
	
	/** The datum */
	protected IDatum datum;
	
	/** The label ID */
	protected String labelId;
	
	/** The datum name */
	protected String datumName;
	
	/** The datum value */
	protected String datumValue;
	
	/** The check box */
	protected JCheckBox checkBox;
	
	/** The datum name label */
	protected JLabel nameLabel;
	
	/** The datum value label */
	protected JLabel valueLabel;
	
	/** The datum value text alignment (SwingConstants.LEFT or SwingConstants.RIGHT) */
	protected int datumValueAlignment;	
	
	/** The indent level */
	protected int indentLevel;
	
	/** Whether this is a title label */
	protected boolean titleLabel;
	
	protected boolean checked = false;
	
	protected boolean highlighted = false;	

	//protected boolean enabled;	
	//protected boolean clickable;
	
	public DatumLabel(DatumListItem datum, boolean showCheckBox, boolean titleLabel, int datumValueAlignment) {
		this(datum.getName(), null, datum.getColor(), showCheckBox, titleLabel, datumValueAlignment);
	}
	
	public DatumLabel(String datumName, String datumValue, Color foreground, 
			boolean showCheckBox, boolean titleLabel, int datumValueAlignment) {
		super(new GridBagLayout());
		gbl = (GridBagLayout)getLayout();
		this.datumName = datumName;
		this.datumValue = datumValue;
		this.datumValueAlignment = datumValueAlignment;
		if(showCheckBox) {
			checkBox = new JCheckBox();
			checkBox.setEnabled(false);
		}
		this.titleLabel = titleLabel;
		nameLabel = new JLabel(formatDatumNameText());
		valueLabel = new JLabel(datumValue != null ? datumValue : "");
		/*label = new JLabel();
		label.setText(formatLabelText());*/
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		if(showCheckBox) {
			add(checkBox, gbc);
			gbc.gridx++;
			gbc.insets.left = 2;
		}		
		//add(label, gbc);		
		add(nameLabel, gbc);
		gbc.gridx++;
		gbc.insets.left = 0;
		gbc.weightx = 1;
		if(datumValueAlignment == SwingConstants.RIGHT) {			
			gbc.anchor = GridBagConstraints.EAST;
		}
		add(valueLabel, gbc);
		
		if(foreground != null) {
			setForeground(foreground);
		}
	}
	
	public IDatum getDatum() {
		return datum;
	}
	
	public void setDatum(IDatum datum) {
		this.datum = datum;
	}

	public String getDatumName() {
		return datumName;
	}

	public void setDatumName(String datumName) {
		this.datumName = datumName;
		nameLabel.setText(formatDatumNameText());
		//label.setText(formatLabelText());
	}

	public String getDatumValue() {
		return datumValue;
	}

	public void setDatumValue(String datumValue) {
		this.datumValue = datumValue;
		nameLabel.setText(formatDatumNameText());
		valueLabel.setText(datumValue != null ? datumValue : "");
		//label.setText(formatLabelText());
	}
	
	protected String formatDatumNameText() {
		StringBuilder sb = new StringBuilder();
		if(datumName != null) {
			sb.append(datumName);
			if(datumValue != null) {
				sb.append(": ");
			}
		}		
		return sb.toString();
	}

	/*protected String formatLabelText() {
		StringBuilder sb = new StringBuilder("<html>");
		if(datumName != null && !datumName.isEmpty()) {
			sb.append(datumName);
		}
		if(datumValue != null && !datumValue.isEmpty()) {
			sb.append(": ");
			sb.append(datumValue);
		}
		sb.append("</html>");
		return sb.toString();
	}*/
	
	public int getDatumValueHorizontalAlignment() {
		return datumValueAlignment;
	}

	public void setDatumValueHorizontalAlignment(int datumValueAlignment) {
		if(datumValueAlignment != this.datumValueAlignment) {
			this.datumValueAlignment = datumValueAlignment;
			if(valueLabel != null) {
				GridBagConstraints gbc = gbl.getConstraints(valueLabel);
				gbc.weightx = 1;
				if(datumValueAlignment == SwingConstants.RIGHT) {
					gbc.anchor = GridBagConstraints.EAST;
				} else {
					gbc.anchor = GridBagConstraints.WEST;
				}
				gbl.setConstraints(valueLabel, gbc);
			}
		}
	}
	
	public int getIndentLevel() {
		return indentLevel;
	}	

	public void setIndentLevel(int indentLevel) {
		this.indentLevel = indentLevel;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		if(checked != this.checked) {
			this.checked = checked;
			if(checkBox != null) {
				checkBox.setSelected(checked);
			}
		}
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		if(highlighted != this.highlighted) {
			this.highlighted = highlighted;
			if(highlighted) {
				setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			} else {
				setBorder(null);
			}
		}
	}

	public boolean isTitleLabel() {
		return titleLabel;
	}

	public void setTitleLabel(boolean titleLabel) {
		this.titleLabel = titleLabel;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(checkBox != null) {
			checkBox.setBackground(bg);
		}
		if(nameLabel != null) {
			nameLabel.setBackground(bg);
		}
		if(valueLabel != null) {
			valueLabel.setBackground(bg);
		}
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(nameLabel != null) {
			nameLabel.setFont(font);
		}
		if(valueLabel != null) {
			valueLabel.setFont(font);
		}
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if(nameLabel != null) {
			nameLabel.setForeground(fg);
		}
		if(valueLabel != null) {
			valueLabel.setForeground(fg);
		}
	}

	@Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(isOpaque);
		if(checkBox != null) {
			checkBox.setOpaque(isOpaque);
		}
		if(nameLabel != null) {
			nameLabel.setOpaque(isOpaque);
		}
		if(valueLabel != null) {
			valueLabel.setOpaque(isOpaque);
		}
	}	
}
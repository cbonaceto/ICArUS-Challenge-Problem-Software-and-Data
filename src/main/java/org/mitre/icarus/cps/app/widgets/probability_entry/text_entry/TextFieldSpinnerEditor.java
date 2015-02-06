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
package org.mitre.icarus.cps.app.widgets.probability_entry.text_entry;

import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;

public class TextFieldSpinnerEditor extends NumberEditor {
	private static final long serialVersionUID = 5493345259302973757L;
	
	boolean valueNull = false;
	
	protected JSpinner spinner;

	public TextFieldSpinnerEditor(JSpinner spinner) {
		super(spinner);
		this.spinner = spinner;		
	}
	/*@Override
	public void stateChanged(ChangeEvent e) {		
		super.stateChanged(e);
	}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);
		if(spinner != null && e.getPropertyName().equals("editValid")) {
			//System.out.println("Property " + e.getPropertyName() + " Changed: " + "Spinner value: " + spinner.getValue() 
			//		+ ", Old Value: " + e.getOldValue() + ", New Value: " + e.getNewValue());
			if(this.getTextField().getText() == null && this.getTextField().getText().isEmpty() || this.getTextField().getText().equals("")) {				
				System.out.println("text field empty");
				//this.getTextField().setT
			} else {
				System.out.println(this.getTextField().getText());
			}
		} 
	}*/
}
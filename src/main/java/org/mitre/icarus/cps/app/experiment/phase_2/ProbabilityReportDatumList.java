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
package org.mitre.icarus.cps.app.experiment.phase_2;

import java.util.List;

import org.mitre.icarus.cps.app.widgets.phase_2.experiment.datum_list.DatumListItem;

/**
 * @author CBONACETO
 *
 */
public class ProbabilityReportDatumList {
	
	protected List<DatumListItem> datumItems;
	
	protected List<String> datumValues;
	
	protected boolean considerBlueBook;
	
	protected boolean considerSigint;
	
	protected boolean considerSigintReliability;
	
	protected boolean considerBatchPlots;
	
	public ProbabilityReportDatumList() {}
	
	public ProbabilityReportDatumList(List<DatumListItem> datumItems, List<String> datumValues, 
			boolean considerBlueBook, boolean considerSigintReliability, 
			boolean considerSigint, boolean considerBatchPlots) {
		this.datumItems = datumItems;
		this.datumValues = datumValues;
		this.considerBlueBook = considerBlueBook;
		this.considerSigint = considerSigint;
		this.considerSigintReliability = considerSigintReliability;
		this.considerBatchPlots = considerBatchPlots;
	}

	public List<DatumListItem> getDatumItems() {
		return datumItems;
	}

	public void setDatumItems(List<DatumListItem> datumItems) {
		this.datumItems = datumItems;
	}

	public List<String> getDatumValues() {
		return datumValues;
	}

	public void setDatumValues(List<String> datumValues) {
		this.datumValues = datumValues;
	}

	public boolean isConsiderBlueBook() {
		return considerBlueBook;
	}

	public void setConsiderBlueBook(boolean considerBlueBook) {
		this.considerBlueBook = considerBlueBook;
	}

	public boolean isConsiderSigint() {
		return considerSigint;
	}

	public void setConsiderSigint(boolean considerSigint) {
		this.considerSigint = considerSigint;
	}

	public boolean isConsiderSigintReliability() {
		return considerSigintReliability;
	}

	public void setConsiderSigintReliability(boolean considerSigintReliability) {
		this.considerSigintReliability = considerSigintReliability;
	}

	public boolean isConsiderBatchPlots() {
		return considerBatchPlots;
	}

	public void setConsiderBatchPlots(boolean considerBatchPlots) {
		this.considerBatchPlots = considerBatchPlots;
	}
}
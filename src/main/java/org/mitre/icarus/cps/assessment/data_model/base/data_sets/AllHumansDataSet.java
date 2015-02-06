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
package org.mitre.icarus.cps.assessment.data_model.base.data_sets;

import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;
import org.mitre.icarus.cps.assessment.data_model.base.data_sets.DataSet;

/**
 * @author CBONACETO
 *
 */
public abstract class AllHumansDataSet<D extends AbstractTrialData<?>, S extends AbstractSubjectMetrics> extends DataSet {
	
	/** Trial data for each subject on each trial of each task (Task->Trials->Subjects) **/
	protected List<List<List<D>>> trialDataByTask;
	
	/** Overall subject metrics for each subject (Computed using only Task 6 data) **/
	protected List<S> subjectMetrics = new LinkedList<S>();

	public List<List<List<D>>> getTrialDataByTask() {
		return trialDataByTask;
	}

	public void setTrialDataByTask(List<List<List<D>>> trialDataByTask) {
		this.trialDataByTask = trialDataByTask;
	}

	public List<S> getSubjectMetrics() {
		return subjectMetrics;
	}

	public void setSubjectMetrics(List<S> subjectMetrics) {
		this.subjectMetrics = subjectMetrics;
	}

	@Override
	public DataType getData_type() {
		return DataType.Human_Multiple;
	}
}
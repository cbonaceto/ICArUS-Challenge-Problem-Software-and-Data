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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.mitre.icarus.cps.assessment.data_model.base.DataType;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractSubjectMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.AbstractTrialData;

/**
 * @author CBONACETO
 *
 */
@Entity
public abstract class SingleModelDataSet<D extends AbstractTrialData<?>,
	T extends AbstractTaskMetrics<D>, M extends AbstractExamMetrics<T>, 
	S extends AbstractSubjectMetrics> extends SingleParticipantDataSet<D, T, M, S> {
	
	@Id @GeneratedValue
	@Column( name="singleModelDataSetId" )
	@Override
	public int getId() {
		return id;
	}	

	/*//@ManyToOne(cascade=CascadeType.ALL)
	public M getExamMetrics() {
		return examMetrics;
	}

	public void setExamMetrics(M examMetrics) {
		this.examMetrics = examMetrics;
	}*/

	@Override
	public DataType getData_type() {
		return DataType.Model_Single;
	}	
}
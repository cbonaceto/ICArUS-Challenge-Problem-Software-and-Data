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
package org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.mitre.icarus.cps.assessment.data_model.base.DataType;

/**
 * 
 * @author cbonaceto
 *
 */
public abstract class AbstractSubjectMetrics implements Serializable {	
	private static final long serialVersionUID = 935017420960136106L;
	
	private int id;
	
	protected String site_id;	
	protected String response_generator_id; 
	protected DataType data_type; 
	protected Boolean human;	
	protected String exam_id;	
	protected Integer num_subjects;		
	
	protected Boolean metrics_stale;	 

	@Id
	@GeneratedValue
	@Column( name="subjectMetricsId" )
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getResponse_generator_id() {
		return response_generator_id;
	}

	public void setResponse_generator_id(String response_generator_id) {
		this.response_generator_id = response_generator_id;
	}

	@Enumerated
	@Column( name="subjectMetricsDataType" )
	public DataType getData_type() {
		return data_type;
	}

	public void setData_type(DataType data_type) {
		this.data_type = data_type;
	}

	public Boolean getHuman() {
		return human;
	}

	public void setHuman(Boolean human) {
		this.human = human;
	}

	public String getExam_id() {
		return exam_id;
	}

	public void setExam_id(String exam_id) {
		this.exam_id = exam_id;
	}	

	public Integer getNum_subjects() {
		return num_subjects;
	}

	public void setNum_subjects(Integer num_subjects) {
		this.num_subjects = num_subjects;
	}

	public Boolean getMetrics_stale() {
		return metrics_stale;
	}

	public void setMetrics_stale(Boolean metrics_stale) {
		this.metrics_stale = metrics_stale;
	}	
}
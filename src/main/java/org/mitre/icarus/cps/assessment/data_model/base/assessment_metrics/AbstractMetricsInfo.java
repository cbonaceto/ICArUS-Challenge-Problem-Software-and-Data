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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Base class for classes containing information about assessment metrics
 * that will be computed.
 * 
 * @author CBONACETO
 *
 */
@Entity
public class AbstractMetricsInfo implements Serializable {

    private static final long serialVersionUID = -5230008230535855532L;

    private int id;

    @Id
    @GeneratedValue
    @Column(name = "metricsInfoId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

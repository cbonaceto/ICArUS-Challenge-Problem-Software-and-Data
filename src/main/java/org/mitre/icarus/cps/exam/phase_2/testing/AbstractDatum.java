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
package org.mitre.icarus.cps.exam.phase_2.testing;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.IntDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintReliability;

/**
 * Abstract implementation of the IDatum interface that provides methods for setting and getting
 * the datum ID and datum name. Datum are information items (e.g., IMINT report of "U", OSINT report of "P") used
 * to make inferences and/or decisions.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="AbstractDatum", namespace="IcarusCPD_2")
@XmlSeeAlso({IntDatum.class, SigintReliability.class})
public abstract class AbstractDatum implements IDatum {	
	/** The datum ID */
	protected String id;
	
	/** The datum name */
	protected String name;
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getId()
	 */
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#getName()
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IDatum#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
}
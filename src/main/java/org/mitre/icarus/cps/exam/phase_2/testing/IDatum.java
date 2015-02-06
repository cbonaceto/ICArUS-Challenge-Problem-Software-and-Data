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

/**
 * Interface for "datum" items in Phase 2. Datum are information items (e.g., IMINT report of "U", OSINT report of "P") used
 * to make inferences and/or decisions. Datum items can be refered to by a unique ID and DatumType identifier. Datum
 * items may also have a name.
 * 
 * @author CBONACETO
 *
 */
public interface IDatum {
	
	/**
	 * Get the datum ID. 
	 * 
	 * @return the datum ID
	 */
	public String getId();	
	
	/**
	 * Set the datum ID
	 * 
	 * @param id the datum id
	 */
	public void setId(String id);
	
	/**
	 * Get the datum name
	 * 
	 * @return the datum name
	 */
	public String getName();
	
	/**
	 * Set the datum name
	 * 
	 * @param name the datum name
	 */
	public void setName(String name);
	
	/**
	 * Get the datum type.
	 * 
	 * @return the datum type
	 */
	public DatumType getDatumType();
}
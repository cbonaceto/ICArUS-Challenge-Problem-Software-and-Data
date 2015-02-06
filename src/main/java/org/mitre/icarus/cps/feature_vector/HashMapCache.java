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
package org.mitre.icarus.cps.feature_vector;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A hash map implementation that maintains a steady state of maxEntries entries.
 * This is used to cache feature vectors (Phase 1 and 2) and object palettes (Phase 05).
 *
 * 
 * @author CBONACETO
 *
 * @param <K>
 * @param <V>
 */
public class HashMapCache<K, V> extends LinkedHashMap<K, V>  {

	private static final long serialVersionUID = 1L;

	final int maxEntries;

	/**
	 * Constructor takes the initialize size of the hash map and the maximum number of entries to keep cached.
	 * 
	 * @param initialSize the initial map size
	 * @param maxEntries the maximum number of entries to cache 
	 */
	public HashMapCache(int initialSize, int maxEntries) {
		super(initialSize);
		this.maxEntries = maxEntries;
	}		

	/* (non-Javadoc)
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 */
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxEntries;
	}
}

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
package org.mitre.icarus.cps.app.window.configuration;


/**
 * Contains information about a file type.
 * 
 * @author CBONACETO
 *
 */
public class FileDescriptor {
	
	protected Integer fileTypeId;
	
	protected String fileTypeDescription;	
	
	protected String fileTypeTitle;
	
	//protected boolean fileRequired;
	
	protected String[] extenions;	
	
	public FileDescriptor() {}
	
	public FileDescriptor(Integer fileTypeId, String fileTypeDescription, String fileTypeTitle, String... extensions) {
		this.fileTypeId = fileTypeId;
		this.fileTypeDescription = fileTypeDescription;
		this.fileTypeTitle = fileTypeTitle;
		this.extenions = extensions;
	}

	public Integer getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(Integer fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public String getFileTypeDescription() {
		return fileTypeDescription;
	}

	public void setFileTypeDescription(String fileTypeDescription) {
		this.fileTypeDescription = fileTypeDescription;
	}
	
	public String getFileTypeTitle() {
		return fileTypeTitle;
	}

	public void setFileTypeTitle(String fileTypeTitle) {
		this.fileTypeTitle = fileTypeTitle;
	}	

	public String[] getExtenions() {
		return extenions;
	}

	public void setExtenions(String[] extenions) {
		this.extenions = extenions;
	}
}
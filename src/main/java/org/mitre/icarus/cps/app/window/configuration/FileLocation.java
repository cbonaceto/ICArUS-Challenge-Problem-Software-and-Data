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

import java.io.File;
import java.net.URL;

/**
 * 
 * Contains information on where a file is located.
 * 
 * @author CBONACETO
 *
 */
public class FileLocation {
	
	protected String fileName;
	
	protected String fileLocation;
	
	protected URL fileUrl;
	
	protected File file;
	
	protected FileDescriptor fileType;	
	
	public FileLocation(String fileName) {
		this.fileName = fileName;
	}
	
	public FileLocation(String fileName, String fileLocation, FileDescriptor fileType) {
		this.fileName = fileName;
		this.fileLocation = fileLocation;						
		this.fileType = fileType;		
	}	
	
	public FileLocation(String fileName, URL fileUrl, FileDescriptor fileType) {
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.fileType = fileType;
	}	
	
	public FileLocation(File file, FileDescriptor fileType) {
		this.fileName = file != null ? file.getName() : null;
		this.file = file;
		this.fileType = fileType;
	}
	
	public FileLocation(URL fileUrl, File file, FileDescriptor fileType) {
		this.fileName = file != null ? file.getName() : null;
		this.fileUrl = fileUrl;
		this.file = file;
		this.fileType = fileType;		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public URL getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(URL fileUrl) {
		this.fileUrl = fileUrl;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public FileDescriptor getFileType() {
		return fileType;
	}

	public void setFileType(FileDescriptor fileType) {
		this.fileType = fileType;
	}	
}
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
package org.mitre.icarus.cps.app.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * @author CBONACETO
 *
 */
public class CPSFileUtils {
	
	public static final String PATH_SEPARATOR = System.getProperty("file.separator"); 
	
	private CPSFileUtils() {}	
	
	public static String separatorsToSystem(String path) {
		return FilenameUtils.separatorsToSystem(path);
	}
	
	public static File createFile(String path) {
		return new File(separatorsToSystem(path));
	}
	
	public static File createFile(File folder, String fileName) {
		return new File(folder, fileName);
		//return new File(separatorsToSystem(path));
	}
	
	/*public static void main(String args[]) {
		System.out.println(createFile("data/response_data"));
	}*/
	
	public static void deleteFile(File file) {
		FileUtils.deleteQuietly(file);
	}
	
	public static String getDefaultPathSeparator() {
		return PATH_SEPARATOR;
	}
	
	public static File getTempDirectory() {
		return FileUtils.getTempDirectory();
	}
	
	public static File getUserHomeDirectory() {
		return FileUtils.getUserDirectory();
	}
	
	public static void makeDirectory(File directory) throws IOException {
		//if(makeParentDirectories) {
		FileUtils.forceMkdir(directory);
		//responseDataFolder.mkdirs();
		//}
	}	
	
	public static String readFileToString(File file) throws IOException {
		return FileUtils.readFileToString(file, Charset.defaultCharset());
	}
	
	public static void writeStringToFile(File file, String data) throws IOException {
		FileUtils.writeStringToFile(file, data, Charset.defaultCharset(), false);
	}	
}
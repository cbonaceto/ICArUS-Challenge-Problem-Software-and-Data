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
package org.mitre.icarus.cps.assessment.persistence.phase_2.spreadsheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.AverageHumanDataSet_Phase2;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.SingleModelDataSet_Phase2;

/**
 * Saves data sets as Excel workbooks.
 */
public class SpreadsheetCPADataPersister {

	/** URL to the data folder to store XML files in */
	protected URL dataFolder;

	/** Human dataset */
	protected AverageHumanDataSet_Phase2 humanData;

	public SpreadsheetCPADataPersister() {}

	public SpreadsheetCPADataPersister(URL dataFolder, AverageHumanDataSet_Phase2 humanData) {
		this.dataFolder = dataFolder;
		this.humanData = humanData;
	}

	public URL getDataFolder() {
		return dataFolder;
	}

	public void setDataFolder(URL dataFolder) {
		this.dataFolder = dataFolder;
	}

	public AverageHumanDataSet_Phase2 getHumanData() {
		return humanData;
	}

	public void setHumanData(AverageHumanDataSet_Phase2 humanData) {
		this.humanData = humanData;
	}

	/**
	 * Writes an Excel workbook combining the given model dataset with the human data 
	 * provided upon initialization.
	 */
	public void persistSingleModelDataSet(SingleModelDataSet_Phase2 dataset) {
		Workbook wb = SpreadsheetMarshaller.marshall(dataset, humanData, new XSSFWorkbook());
		writeFile(wb, generateFilename(dataset));
	}

	/**
	 * Writes a workbook to the specified filename in the data folder.
	 */
	private void writeFile(Workbook wb, String filename) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(new File(dataFolder.getFile(), filename));
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a generated string filename for the spreadsheet. Appends "generated".
	 */
	public static String generateFilename(SingleModelDataSet_Phase2 dataset) {
		return dataset.getSite_id() +"_"+ 
				dataset.getResponse_generator_id() +"_"+
				dataset.getExam_id() 
				+"_generated.xlsx";

	}
}

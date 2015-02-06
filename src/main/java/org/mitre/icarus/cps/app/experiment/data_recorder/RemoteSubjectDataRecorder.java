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
package org.mitre.icarus.cps.app.experiment.data_recorder;

import java.io.File;
import java.io.IOException;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;
import org.mitre.icarus.cps.web.experiments.client.IDataSource;

/**
 * Data recorder implementation that records subject data to both the local file system and to a remote server.
 * 
 * @author CBONACETO
 *
 */
public class RemoteSubjectDataRecorder<E extends IcarusExam<?>, P extends IcarusExamPhase, T extends IcarusTutorialPhase<?>> 
	extends AbstractSubjectDataRecorder<E, P, T> {
	
	/** Local subject data recorder instance to write to the local file system */
	protected final LocalSubjectDataRecorder<E, P, T> localDataRecorder;
	
	/** Remote server connection */
	protected IDataSource dataSourceConnection;
	
	/** Whether to save data to the local file system (default is true) */
	protected boolean saveLocalData = true;
	
	/** Whether to save data to the remote server (default is true) */
	protected boolean saveRemoteData = true;
	
	/** Folder where temporary files and/or subject data files are written */
	protected File outputFolder;
	
	public RemoteSubjectDataRecorder(IDataSource dataSourceConnection, File outputFolder) {
		this.dataSourceConnection = dataSourceConnection;
		this.outputFolder = outputFolder;
		localDataRecorder = new LocalSubjectDataRecorder<E, P, T>(outputFolder);
	}

	public IDataSource getDataSourceConnection() {
		return dataSourceConnection;
	}

	public void setDataSourceConnection(IDataSource dataSourceConnection) {
		this.dataSourceConnection = dataSourceConnection;
	}

	public boolean isSaveLocalData() {
		return saveLocalData;
	}

	public void setSaveLocalData(boolean saveLocalData) {
		this.saveLocalData = saveLocalData;
	}

	public boolean isSaveRemoteData() {
		return saveRemoteData;
	}

	public void setSaveRemoteData(boolean saveRemoteData) {
		this.saveRemoteData = saveRemoteData;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public File recordExamPhaseData(IcarusSubjectData subjectData, E exam,
			P examPhase, IcarusExamLoader<E, P, T> examLoader) throws Exception {
		File dataFile = null;
		boolean deleteTempFile = false;
		try {
			if(saveLocalData) {
				//Record data file locally
				dataFile = localDataRecorder.recordExamPhaseData(subjectData, exam, examPhase, examLoader);
			} else {
				String xml = examLoader.marshalExamPhase(examPhase);	
				if(saveRemoteData && dataSourceConnection != null) {
					dataFile = this.createTempDataFile(subjectData, exam.getId(), examPhase.getId(), xml);
					deleteTempFile = true;
				}
			}		
			if(saveRemoteData && dataSourceConnection != null) {
				//Record data file on server				
				dataSourceConnection.saveTaskData(subjectData.getSubjectId(), exam.getName(), examPhase.getName(), dataFile);				
			} 
		} catch(Exception e) {
			throw(e);
		} finally {
			if(deleteTempFile) {
				try {CPSFileUtils.deleteFile(dataFile);} catch(Exception ex) {}				
			}
		}
		return dataFile;	
	}

	@Override
	public File recordTutorialPhaseData(IcarusSubjectData subjectData, E exam,
			T tutorialPhase, IcarusExamLoader<E, P, T> examLoader)
			throws Exception {
		File dataFile = null;
		boolean deleteTempFile = false;
		try {
			if(saveLocalData) {
				//Record data file locally
				dataFile = localDataRecorder.recordTutorialPhaseData(subjectData, exam, tutorialPhase, examLoader);
			} else {
				String xml = examLoader.marshalTutorialPhase(tutorialPhase);	
				if(saveRemoteData && dataSourceConnection != null) {
					dataFile = createTempDataFile(subjectData, exam.getId(), 
							tutorialPhase.getId() != null ? tutorialPhase.getId() : tutorialPhase.getName(), xml);
					deleteTempFile = true;
				}
			}		
			if(saveRemoteData && dataSourceConnection != null) {
				//Record data file on server				
				dataSourceConnection.saveTaskData(subjectData.getSubjectId(), exam.getName(), 
						tutorialPhase.getName(), dataFile);				
			} 
		} catch(Exception e) {
			throw(e);
		} finally {
			if(deleteTempFile) {
				try {CPSFileUtils.deleteFile(dataFile);} catch(Exception ex) {}				
			}
		}
		return dataFile;
	}
	
	protected File createTempDataFile(IcarusSubjectData subjectData, String examId, 
			String phaseId, String xmlData) throws IOException {
		String subjectId = formatSubjectId(subjectData.getSubjectId());
		String siteId = subjectData.getSite() != null ? subjectData.getSite().getTag() : null;
		File dataFile = new File(outputFolder, formatDataFileName(siteId, subjectId, examId, phaseId));
		//deleteTempFile = true;
		CPSFileUtils.writeStringToFile(dataFile, xmlData);
		return dataFile;
	}
}
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

import javax.xml.bind.JAXBException;

import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.IcarusExamPhase;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.base.training.IcarusTutorialPhase;

/**
 * Default subject data recorder implementation. Records subject data to the local file system.
 * 
 * @author CBONACETO
 *
 */
public class LocalSubjectDataRecorder<E extends IcarusExam<?>, P extends IcarusExamPhase, T extends IcarusTutorialPhase<?>> 
	extends AbstractSubjectDataRecorder<E, P, T> {
	
	/** Folder where subject data files are written */
	protected File outputFolder;
	
	public LocalSubjectDataRecorder(File outputFolder) {
		this.outputFolder = outputFolder;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	/*public void test() {
		FileSaveService fss = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");
	}*/	
	
	@Override
	public File recordExamPhaseData(IcarusSubjectData subjectData, E exam,
			P examPhase, IcarusExamLoader<E, P, T> examLoader) throws Exception {		
		File saveFile = createExamPhaseFile(subjectData, exam.getId(), 
				examPhase.getId() != null ? examPhase.getId() : examPhase.getName());
		recordExamPhaseData(saveFile, exam, examPhase, examLoader);
		return saveFile;
	}

	@Override
	public File recordTutorialPhaseData(IcarusSubjectData subjectData, E exam,
			T tutorialPhase, IcarusExamLoader<E, P, T> examLoader)
			throws Exception {
		File saveFile = createExamPhaseFile(subjectData, exam.getId(), 
				tutorialPhase.getId() != null ? tutorialPhase.getId() :	tutorialPhase.getName());
		recordTutorialPhaseData(saveFile, exam, tutorialPhase, examLoader);
		return saveFile;
	}
	
	/**
	 * @param subjectData
	 * @param examId
	 * @param phaseId
	 * @return
	 */
	protected File createExamPhaseFile(IcarusSubjectData subjectData, String examId, String phaseId) 
			throws IOException {
		String subjectId = formatSubjectId(subjectData.getSubjectId());
		String siteId = subjectData.getSite() != null ? subjectData.getSite().getTag() : null;
		if(outputFolder == null) {
			outputFolder = CPSFileUtils.createFile("data/response_data");
		}
		File responseDataFolder = createResponseDataFolder(outputFolder, siteId, subjectId);			
		return new File(responseDataFolder, formatDataFileName(siteId, subjectId, examId, phaseId));
	}

	/**
	 * @param saveFile
	 * @param exam
	 * @param examPhase
	 * @param examLoader
	 * @throws IOException
	 * @throws JAXBException
	 */
	protected void recordExamPhaseData(File saveFile, E exam, P examPhase, 
			IcarusExamLoader<E, P, T> examLoader) throws IOException, JAXBException {
		String xml = examLoader.marshalExamPhase(examPhase);
		CPSFileUtils.writeStringToFile(saveFile, xml);
	}
	
	/**
	 * @param saveFile
	 * @param exam
	 * @param tutorialPhase
	 * @param examLoader
	 * @throws IOException
	 * @throws JAXBException
	 */
	protected void recordTutorialPhaseData(File saveFile, E exam, T tutorialPhase, 
			IcarusExamLoader<E, P, T> examLoader) throws IOException, JAXBException {
		String xml = examLoader.marshalTutorialPhase(tutorialPhase);
		CPSFileUtils.writeStringToFile(saveFile, xml);
	}
}
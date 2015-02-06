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
package org.mitre.icarus.cps.exam.phase_05.response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.mitre.icarus.cps.exam.base.response.IcarusTrialResponse;
import org.mitre.icarus.cps.exam.phase_05.loader.IcarusExamLoader_Phase05;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.SceneItemProbabilityData;
import org.mitre.icarus.cps.exam.phase_05.testing.scene_presentation.layer_presentation_types.LayerPresentation.PresentationType;

/**
 * Converts one or more test response files to CSV for import into
 * MATLAB.
 * 
 * @author CBONACETO
 *
 */
public class ResponseDataCSVConverter {
	
	/** Main */
	public static void main(String[] args) {
		System.out.println();
		
		if(args.length == 0) {
			System.out.println("You must specify an exam name.  Usage: ExportCSV examName directory subjectID");
			System.out.println("Press ENTER to close this window");
			try {
				System.in.read();
			} catch(Exception err) {}
			return;
		}
		
		String exam = args[0];		
		
		String sid = null;
		if(args.length > 1) {
			sid = args[1];
		}
		
		String dir = "data/response_data";
		if(args.length > 2) {
			dir = args[2];
		}
		
		try {
			System.out.print("Exporting CSV files in " + dir  + " for: ");
			if(sid == null) {
				System.out.print("Participants: All");
			}
			else {
				System.out.print("Participant: " + sid);
			}			
			System.out.print(", Exam: " + exam);
			System.out.println();
			
			int numFiles = convertResponsesToCSV(new File(dir), exam, sid);
			System.out.println("Processed " + numFiles + " response files.");
			System.out.println("Press ENTER to close this window");
			try {
				System.in.read();
			} catch(Exception err) {}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int convertResponsesToCSV(File directory, String examName) throws Exception {
		return convertResponsesToCSV(directory, examName, null);
	}
	
	public static int convertResponsesToCSV(File directory, String examName, String participant) throws Exception {
		int numResponses = 0;
		
		if(!directory.isDirectory() ||!directory.exists()) {
			throw new IllegalArgumentException(directory.getPath() + " is not a directory or cannot be found.");
		}
		
		String sid = null;
		String outFile = null;
		if(participant != null) {
			sid = "S" + participant;
			outFile = sid + "_" + examName + ".csv";
		}
		else {
			outFile = "All_" + examName + ".csv";
		}
		
		//BufferedWriter out = new BufferedWriter(new FileWriter(directory.getAbsolutePath() + "/" + outFile));
		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
		out.write("SID,Phase #,Trial #,Question Type,User Choice (0/1),Num Sets,Num Facilities,Num Probs,1,2,3,4,5,6,7,8,9,10,11,12");
		out.write("\n");
		
		File[] files = directory.listFiles();
		if(files != null) {
			for(File file : files) {
				
				String fileName = file.getName();
				boolean responseFound = false;
				
				if(sid == null) {
					//Get responses for all participants
					responseFound = (fileName.contains(examName) && !fileName.contains("training") && fileName.endsWith("xml"));
				}
				else {
					//Get responses for a single participant
					responseFound = (fileName.contains(examName) && !fileName.contains("training") &&
							fileName.contains(sid) && fileName.endsWith("xml"));
				}
				
				if(responseFound) {
					IcarusExamPhaseResponse response = IcarusExamLoader_Phase05.unmarshalExamPhaseResponse(file.toURI().toURL());
					if(response != null && response instanceof IcarusTestPhaseResponse_Phase05) {
						out.write(responseToCSV((IcarusTestPhaseResponse_Phase05)response, 
								 parseSubjectID(file.getName()),
								 parsePhase(file.getName()), 3, 4));
						out.write("\n");						
						numResponses++;
					}
				}
			}
		}
		
		try {
			out.flush();
			out.close();
		} catch(IOException e) {}
		
		return numResponses;
	}
	
	/** Get the subject ID given a test phase response file name */
	protected static String parseSubjectID(String responseFileName) {
		int firstUnderscorePos = responseFileName.indexOf('_');
		return responseFileName.substring(1, firstUnderscorePos);	
	}
	
	/** Get the phase number given a test phase response file name */
	protected static int parsePhase(String responseFileName) {
		int underscorePos = responseFileName.lastIndexOf('_');
		return Integer.parseInt(responseFileName.substring(underscorePos+6, 
				responseFileName.length() - 4));
	}
	
	/** Serialize response to CSV format 
	 * Column Headers: Subject ID, Phase #,Trial #,Question Type,User Choice (0/1),Num Layer Presentations,Num Facilities,
	 *                 Num Probs Per Layer Presentation,1,2,3,4,5,6,7,8,9,10,11,12
	 */        
	public static String responseToCSV(IcarusTestPhaseResponse_Phase05 response, String sid, int phase, 
			int maxLayerPresentations, int maxProbsPerLayerPresentation) {
		//Headers: Subject ID, Phase #,Trial #,Question Type,User Choice (0/1),Num Layer Presentations,Num Facilities,
		//         Num Probs Per Layer Presentation,1,2,3,4,5,6,7,8,9,10,11,12
		
		StringBuilder sb = new StringBuilder();	
		
		String phaseStr = sid + "," + Integer.toString(phase);
		if(response.getTrialResponses() != null) {
			int trialNum = 1;
			int numTrials = response.getTrialResponses().size();
			for(IcarusTrialResponse trial : response.getTrialResponses()) {
				sb.append(phaseStr + "," + trialNum + ","); //Append subject ID, phase number and trial number				
				if(trial instanceof IdentifyItemTrialResponse) {
					appendIdentifyItemTrial(sb, (IdentifyItemTrialResponse)trial,
							maxLayerPresentations, maxProbsPerLayerPresentation);					
				}
				else if(trial instanceof LocateItemTrialResponse) {
					appendLocateItemTrial(sb, (LocateItemTrialResponse)trial,
							maxLayerPresentations, maxProbsPerLayerPresentation);
				}
				if(trialNum < numTrials) {
					sb.append("\n"); //Append newline
				}
				trialNum++;
			}
		}		
		
		return sb.toString();
	}

	/** Append data from an identify item trial */
	protected static void appendIdentifyItemTrial(StringBuilder sb, IdentifyItemTrialResponse identify,
			int maxLayerPresentations, int maxProbsPerLayerPresentation) {
		
		sb.append("1,"); //Append 1 to indicate identify item trial
		if(identify.getSceneItemProbabilityData() != null && !identify.getSceneItemProbabilityData().isEmpty()) {
			//Determine if a user choice presentation is present
			Integer userChoiceLayer = 0;
			for(SceneItemProbabilityResponseData data : identify.getSceneItemProbabilityData()) {
				if(data.getLayersShown() != null) {
					for(LayerData layer : data.getLayersShown()) {
						if(layer.getPresentationType() == PresentationType.UserChoice) {
							userChoiceLayer = layer.getLayerID();
							break;
						}
					}
				}
			}
			sb.append(userChoiceLayer + ","); //Append the first user choice layer selected, or 0 if none
			sb.append(identify.getSceneItemProbabilityData().size() + ","); // Append the number of layer presentations
			int presentationNum = 0;
			for(SceneItemProbabilityResponseData data : identify.getSceneItemProbabilityData()) {
				if(data.getLayersShown() != null && !data.getLayersShown().isEmpty()) {
					if(presentationNum == 0) {
						sb.append(data.getSceneItemProbabilities().size() + ","); //Append the number of scene items probed
						sb.append(maxProbsPerLayerPresentation + ","); //Append  max number of probabilities assessed per layer presentation
					}
					
					int numProbs = 0;
					if(data.getSceneItemProbabilities() != null && !data.getSceneItemProbabilities().isEmpty()) {
						for(SceneItemProbabilityData itemData : data.getSceneItemProbabilities()) {
							sb.append(itemData.getProbability()); //Append probability
							if(presentationNum < maxLayerPresentations-1 ||
									numProbs < maxProbsPerLayerPresentation-1) {
								sb.append(",");
							}
							numProbs++;							
						}								
					}
				
					//Pad with 0s if num probs < max probs per layer presentation
					for(; numProbs < maxProbsPerLayerPresentation; numProbs++) {
						sb.append("0");
						if(presentationNum < maxLayerPresentations-1 ||
								numProbs < maxProbsPerLayerPresentation-1) {
							sb.append(",");
						}
					}
					
					presentationNum++;
				}			
			}
			
			//Pad with 0s if presentationNum < max layer presentations
			for(; presentationNum < maxLayerPresentations; presentationNum++) {
				for(int i=0; i<maxProbsPerLayerPresentation; i++) {
					sb.append("0");
					if(presentationNum < maxLayerPresentations-1 ||
							i < maxProbsPerLayerPresentation-1) {
						sb.append(",");
					}
				}
			}
		}
	}
	
	/** Append data from a locate item trial */
	protected static void appendLocateItemTrial(StringBuilder sb, LocateItemTrialResponse locate,
			int maxLayerPresentations, int maxProbsPerLayerPresentation) {
		
		sb.append("2,"); //Append 2 to indicate identify item trial
		if(locate.getSectorProbabilityData() != null && !locate.getSectorProbabilityData().isEmpty()) {
			//Determine if a user choice presentation is present
			Integer userChoiceLayer = 0;
			for(SectorProbabilityResponseData data : locate.getSectorProbabilityData()) {
				if(data.getLayersShown() != null) {
					for(LayerData layer : data.getLayersShown()) {
						if(layer.getPresentationType() == PresentationType.UserChoice) {
							userChoiceLayer = layer.getLayerID();
							break;
						}
					}
				}
			}
			sb.append(userChoiceLayer + ","); //Append the first user choice layer selected, or 0 if none
			sb.append(locate.getSectorProbabilityData().size() + ","); // Append the number of layer presentations
			int presentationNum = 0;
			for(SectorProbabilityResponseData data : locate.getSectorProbabilityData()) {
				if(data.getLayersShown() != null && !data.getLayersShown().isEmpty()) {
					if(presentationNum == 0) {
						sb.append(data.getSectorProbabilities().size() + ","); //Append the number of sectors probed
						sb.append(maxProbsPerLayerPresentation + ","); //Append  max number of probabilities assessed per layer presentation
					}
					
					int numProbs = 0;
					if(data.getSectorProbabilities() != null && !data.getSectorProbabilities().isEmpty()) {
						for(SectorProbabilityData itemData : data.getSectorProbabilities()) {
							sb.append(itemData.getProbability()); //Append probability
							if(presentationNum < maxLayerPresentations-1 ||
									numProbs < maxProbsPerLayerPresentation-1) {
								sb.append(",");
							}
							numProbs++;
							
						}								
					}
					
					//Pad with 0s if num probs < max probs per layer presentation
					for(; numProbs < maxProbsPerLayerPresentation; numProbs++) {
						sb.append("0");
						if(presentationNum < maxLayerPresentations-1 ||
								numProbs < maxProbsPerLayerPresentation-1) {
							sb.append(",");
						}
					}
					
					presentationNum++;
				}
			}

			//Pad with 0s if presentationNum < max layer presentations
			for(; presentationNum < maxLayerPresentations; presentationNum++) {
				for(int i=0; i<maxProbsPerLayerPresentation; i++) {
					sb.append("0");
					if(presentationNum < maxLayerPresentations-1 ||
							i < maxProbsPerLayerPresentation-1) {
						sb.append(",");
					}
				}
			}
		}
	}
		
}

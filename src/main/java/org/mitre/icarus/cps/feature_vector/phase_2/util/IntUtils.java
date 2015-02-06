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
package org.mitre.icarus.cps.feature_vector.phase_2.util;

import java.io.File;
import java.net.URL;
//import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;
import org.mitre.icarus.cps.feature_vector.phase_2.loader.FeatureVectorLoader;

/**
 * Computes values for Imint, Osint, Sigint.
 * 
 * @author LWONG
 *
 */
public class IntUtils {
	
	/**
	 * Probability that blue will defeat red if red chooses to attack.
	 * P = 1-e^(-vr)
	 * Initially assume v to be 2 as per document.
	 * @param r normalized distance to closest blue border pt
	 * @return
	 */
	public static Double computeOsintP(Double r) {
		double v = 1.5d; //2.d
		return 1-Math.pow(Math.E, -1*v*r);
	}
	
	/**
	 * Computes opportunity value from the normalized density.
	 * 2-5 for Missions 1-3, 2-10 otherwise, achieved by halving parameter if M1-3
	 * Fixes values < 2 to be 2.
	 * @param u
	 * @return
	 */
	public static Integer computeOpportunity(Double density, int minU, int maxU) {
		int opp = (int) Math.round(density * (maxU - minU)) + minU;
		if (opp < minU) opp = minU;
		return opp;
	}
	
	/**
	 * @param examFolder
	 * @param missions
	 * @param multiplier
	 */
	public static void rescalePValues(File examFolder, Collection<Integer> missions,
			Double multiplier) {
		URL examFolderUrl;
		try {
			examFolderUrl = examFolder.toURI().toURL();		
			for(int i : missions) {			
				String blueLocationsFileName = "Mission" + Integer.toString(i) + "_Blue_Locations.xml";
				FeatureContainer<BlueLocation> blueLocations = FeatureVectorLoader.getInstance().unmarshalBlueLocations(
						IcarusExamLoader_Phase2.createUrl(examFolderUrl, blueLocationsFileName), false,
						null, null);
				if(blueLocations != null && !blueLocations.isEmpty()) {					
					for(BlueLocation blueLocation : blueLocations) {
						System.out.println("Old P: " + blueLocation.getOsint().getRedVulnerability_P());
						blueLocation.getOsint().setRedVulnerability_P(
								blueLocation.getOsint().getRedVulnerability_P() * multiplier);
						System.out.println("New P: " + blueLocation.getOsint().getRedVulnerability_P());
					}
					String xml = FeatureVectorLoader.getInstance().marshalBlueLocations(blueLocations);
					IcarusExamLoader.writeFile(xml, new File(examFolder, blueLocationsFileName));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param examFolder
	 * @param missions
	 * @param minU
	 * @param maxU
	 * @param scaleToFitDistribution
	 * @param uDistribution
	 */
	public static void rescaleUValues(File examFolder, Collection<Integer> missions, 
			int minU, int maxU, Boolean scaleToFitDistribution, double[] uDistribution) {
		//Scale "U" values for Missions 4-6 to range 2-5
		//String examFolderName = "Sample-Exam-2";
		//File examFolder = new File("data/Phase_2_CPD/exams/" + examFolderName);
		if(scaleToFitDistribution && (uDistribution == null || 
				uDistribution.length != maxU - minU + 1)) {
			throw new IllegalArgumentException("Error scaling densities to fit a distribution. The uDistribution array must" +
					"have maxU - minU + 1 values.");
		}
		List<BlueLocation> allBlueLocations = null;
		List<FeatureContainer<BlueLocation>> blueLocationContainers = null;
		if(scaleToFitDistribution) {			
			allBlueLocations = new LinkedList<BlueLocation>();
			blueLocationContainers = new LinkedList<FeatureContainer<BlueLocation>>();
		}
		URL examFolderUrl;
		try {
			examFolderUrl = examFolder.toURI().toURL();		
			for(int i : missions) {			
				String blueLocationsFileName = "Mission" + Integer.toString(i) + "_Blue_Locations.xml";
				FeatureContainer<BlueLocation> blueLocations = FeatureVectorLoader.getInstance().unmarshalBlueLocations(
						IcarusExamLoader_Phase2.createUrl(examFolderUrl, blueLocationsFileName), false,
						null, null);
				if(blueLocations != null && !blueLocations.isEmpty()) {
					if(scaleToFitDistribution) {
						allBlueLocations.addAll(blueLocations.getFeatureList());
						blueLocationContainers.add(blueLocations);
					} else {
						for(BlueLocation blueLocation : blueLocations) {
							//System.out.println("Density: " + blueLocation.getImint().getBuildingDensity());
							//System.out.println("Old U: " + blueLocation.getImint().getRedOpportunity_U());
							blueLocation.getImint().setRedOpportunity_U(
									IntUtils.computeOpportunity(blueLocation.getImint().getBuildingDensity(), minU, maxU));
							//System.out.println("New U: " + blueLocation.getImint().getRedOpportunity_U());
						}
						String xml = FeatureVectorLoader.getInstance().marshalBlueLocations(blueLocations);
						IcarusExamLoader.writeFile(xml, new File(examFolder, blueLocationsFileName));
					}
				}
			}
			if(scaleToFitDistribution && !allBlueLocations.isEmpty()) {
				//Order all locations by their density value, then assign the percentages specified
				//in uDistribution to each U value (e.g., the first uDistribution[0] percent will
				//be assigned minU, the next uDistribution[1] percent will be assigned minU + 1, etc...
				Collections.sort(allBlueLocations, new BlueLocationDensityComparator());				
				int numLocations = allBlueLocations.size();
				int currentU = minU;
				int currentUIndex = 0;
				double currLocationNum = 1;
				for(BlueLocation blueLocation : allBlueLocations) {
					double percent = currLocationNum / numLocations;
					if(percent > uDistribution[currentUIndex]) {						
						currentU++;
						currentUIndex++;
						currLocationNum = 1;						
					}					
					blueLocation.getImint().setRedOpportunity_U(currentU);
					System.out.println("Location Density: " + 
							blueLocation.getImint().getBuildingDensity() + 
							", U: " + currentU);
					currLocationNum++;
				}
				
				//Write the blue location files
				Iterator<Integer> missionIter = missions.iterator();
				for(FeatureContainer<BlueLocation> blueLocations : blueLocationContainers) {
					String blueLocationsFileName = "Mission" + Integer.toString(missionIter.next()) +
							"_Blue_Locations.xml";
					String xml = FeatureVectorLoader.getInstance().marshalBlueLocations(blueLocations);
					IcarusExamLoader.writeFile(xml, new File(examFolder, blueLocationsFileName));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compares Blue locations based on the IMINT building density value.
	 * 
	 * @author CBONACETO
	 *
	 */
	public static class BlueLocationDensityComparator implements Comparator<BlueLocation> {		
		@Override
		public int compare(BlueLocation loc0, BlueLocation loc1) {
			if(loc0 == null || loc0.getImint() == null || 
					loc0.getImint().getBuildingDensity() == null) {
				if(loc1 == null || loc1.getImint() == null ||
						loc1.getImint().getBuildingDensity() == null) {
					return 0;
				} else {
					return -1;
				}
			} else if(loc1 == null || loc1.getImint() == null ||
					loc1.getImint().getBuildingDensity() == null) {
				return 1;
			} else {
				return loc0.getImint().getBuildingDensity().compareTo(
						loc1.getImint().getBuildingDensity());
			}
		}		
	}
	
	public static void main(String[] args) {
		System.out.println(IntUtils.computeOsintP(1.d));
		/*String examFolderName = "Sample-Exam-2";
		List<Integer> missions = Arrays.asList(1, 2, 3, 4, 5);
		//rescalePValues(new File("data/Phase_2_CPD/exams/" + examFolderName), missions, 1.2d);		
		//rescaleUValues(new File("data/Phase_2_CPD/exams/" + examFolderName), 
		//		missions, 2, 5, false, null);
		rescaleUValues(new File("data/Phase_2_CPD/exams/" + examFolderName), 
				missions, 2, 5, true, new double[] {.25, .25, .25, .25});*/		
	}
}
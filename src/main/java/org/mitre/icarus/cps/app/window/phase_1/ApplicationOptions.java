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
package org.mitre.icarus.cps.app.window.phase_1;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.mitre.icarus.cps.web.model.Site;

/**
 * Contains set of enabled options in the Phase 1 ICArUS Test & Evaluation application.
 * 
 * @author CBONACETO
 *
 */
public class ApplicationOptions {
	
	public static enum Menu {File, ExamOptions, FeatureVectorOptions, Help};
	
	public static enum FileOption {Open_Pilot_Exam, Open_Other_Exam, Validate_Exam, Open_Task, Open_Roads, Open_Regions, Return_Home, Logout, Exit}	

	public static enum FeatureVectorOption {Set_Grid_Size, Clear_Task, Clear_Roads, Clear_Regions, Clear_All}
	
	public static enum ExamOption {Skip_To_Task, Change_Particpant, Restart_Exam}
	
	/** The menu names */
	private EnumMap<Menu, String> menuNames;
	
	/** The file options enabled */
	private EnumSet<FileOption> fileOptions;
	
	/** The feature vector options enabled */
	private EnumSet<FeatureVectorOption> featureVectorOptions;	
	
	/** The exam options enabled */
	private EnumSet<ExamOption> examOptions;	
	
	/** Whether data collection is enabled */
	private boolean enableDataCollection;
	
	/** Whether to show the security banner */
	private boolean showSecurityBanner;
	
	/** Whether the application is FOUO */
	private boolean fouo;
	
	/** Whether the score computer is enabled */
	private boolean enableScoreComputer;
	
	/** Whether to warn the user if they take an action that will result in data not being saved (e.g., logout while in a mission) */
	private boolean warnIfDataLost;
	
	/** Whether the appliation is running as an experiment client */
	private boolean clientMode;
	
	/** The sites to choose from (previously the only member variable) */
	private List<Site> sites;
	
	protected ApplicationOptions() {
		sites = initializeSites();
		menuNames = new EnumMap<Menu, String>(Menu.class);
		menuNames.put(Menu.File, "File");
		menuNames.put(Menu.ExamOptions, "Exam Options");
		menuNames.put(Menu.FeatureVectorOptions, "Feature Vector Options");
		menuNames.put(Menu.Help, "Help");
		fileOptions = EnumSet.noneOf(FileOption.class);
		featureVectorOptions = EnumSet.noneOf(FeatureVectorOption.class);
		examOptions = EnumSet.noneOf(ExamOption.class);		
	}
	
	protected List<Site> initializeSites() {
		List<Site> sitesList = null;
		try {
			//Initialize list of sites from sites xml file
			Sites sites = new Sites(getClass().getClassLoader().getResourceAsStream("sites.xml"));
			if(sites != null) {
				sitesList = sites.sites;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			//Create default sites
			sitesList = Arrays.asList(new Site("MITRE Bedford", "BED"), new Site("MITRE McLean", "MCL"));
		}
		return sitesList;
	}

	public static ApplicationOptions createInstanceNoOptions() {
		return new ApplicationOptions();
	}
	
	public static ApplicationOptions createInstanceAllOptions() {
		ApplicationOptions options = new ApplicationOptions();
		options.enableAllOptions();
		return options;
	}
	
	public static ApplicationOptions createInstanceDeveloperToolsOptions() {
		ApplicationOptions options = new ApplicationOptions();
		options.enableDeveloperToolsOptions();		
		return options;
	}
	
	public static ApplicationOptions createInstanceExperimenterOptions() {
		ApplicationOptions options = new ApplicationOptions();
		options.enableExperimenterOptions();	
		return options;
	}
	
	public static ApplicationOptions createInstanceExperimentClientOptions() {
		ApplicationOptions options = new ApplicationOptions();
		options.enableExperimentClientOptions();
		return options;
	}
	
	public void enableAllOptions() {		
		for(FileOption option : FileOption.values()) {
			if(option != FileOption.Logout && option != FileOption.Return_Home) {
				fileOptions.add(option);
			}
		}		
		for(FeatureVectorOption option : FeatureVectorOption.values()) {
			featureVectorOptions.add(option);
		}		
		for(ExamOption option : ExamOption.values()) {
			examOptions.add(option);
		}
		enableScoreComputer = true;
		enableDataCollection = true;
		showSecurityBanner = true;
		warnIfDataLost = false;
		clientMode = false;
		fouo = true;
	}
	
	public void enableDeveloperToolsOptions() {
		fileOptions.clear();
		fileOptions.addAll(Arrays.asList(
				FileOption.Open_Other_Exam,
				FileOption.Validate_Exam, 
				FileOption.Open_Task,
				FileOption.Open_Roads,
				FileOption.Open_Regions,
				FileOption.Exit));
		featureVectorOptions.clear();
		for(FeatureVectorOption option : FeatureVectorOption.values()) {
			featureVectorOptions.add(option);
		}
		examOptions.clear();
		examOptions.addAll(Arrays.asList(
				ExamOption.Skip_To_Task, 
				ExamOption.Restart_Exam));
		enableScoreComputer = false;
		enableDataCollection = false;
		showSecurityBanner = true;
		warnIfDataLost = false;
		clientMode = false;
		fouo = true;
	}	
	
	public void enableExperimenterOptions() {
		fileOptions.clear();
		for(FileOption option : FileOption.values()) {
			fileOptions.add(option);
		}
		featureVectorOptions.clear();
		for(FeatureVectorOption option : FeatureVectorOption.values()) {
			featureVectorOptions.add(option);
		}
		examOptions.clear();
		for(ExamOption option : ExamOption.values()) {
			examOptions.add(option);
		}
		enableScoreComputer = true;
		enableDataCollection = true;
		showSecurityBanner = true;
		warnIfDataLost = true;
		clientMode = true;
		fouo = false;
	}
	
	public void enableExperimentClientOptions() {
		fileOptions.clear();
		fileOptions.addAll(Arrays.asList(
				FileOption.Return_Home,
				FileOption.Logout,
				FileOption.Exit));
		featureVectorOptions.clear();
		examOptions.clear();
		examOptions.addAll(Arrays.asList(
				ExamOption.Skip_To_Task, 
				ExamOption.Restart_Exam));
		enableScoreComputer = true;
		enableDataCollection = true;
		showSecurityBanner = true;
		warnIfDataLost = true;
		clientMode = true;
		fouo = false;
	}
	
	public void disableAllOptions() {
		fileOptions.clear();
		featureVectorOptions.clear();
		examOptions.clear();
		enableScoreComputer = false;
		enableDataCollection = false;
		showSecurityBanner = true;
	}
	
	public String getMenuName(Menu menu) {
		return menuNames.get(menu);
	}
	
	public boolean isFileOptionEnabled(FileOption option) {
		return fileOptions.contains(option);
	}
	
	public void setFileOptionEnabled(FileOption option) {
		fileOptions.add(option);
	}
	
	public Collection<FileOption> getFileOptions() {
		return fileOptions;
	}
	
	public boolean isFeatureVectorOptionEnabled(FeatureVectorOption option) {
		return featureVectorOptions.contains(option);
	}
	
	public void setFeatureVectorOptionEnabled(FeatureVectorOption option) {
		featureVectorOptions.add(option);
	}
	
	public Collection<FeatureVectorOption> getFeatureVectorOptions() {
		return featureVectorOptions;
	}
	
	public boolean isExamOptionEnabled(ExamOption option) {
		return examOptions.contains(option);
	}
	
	public void setExamOptionEnabled(ExamOption option) {
		examOptions.add(option);
	}
	
	public Collection<ExamOption> getExamOptions() {
		return examOptions;
	}
	
	public boolean isEnableDataCollection() {
		return enableDataCollection;
	}

	public boolean isShowSecurityBanner() {
		return showSecurityBanner;
	}

	public void setShowSecurityBanner(boolean showSecurityBanner) {
		this.showSecurityBanner = showSecurityBanner;
	}

	public boolean isFouo() {
		return fouo;
	}	

	public boolean isEnableScoreComputer() {
		return enableScoreComputer;
	}	

	public boolean isWarnIfDataLost() {
		return warnIfDataLost;
	}

	public boolean isClientMode() {
		return clientMode;
	}	
	
	public List<Site> getSites() {
		return sites;
	}
	
	@XmlRootElement(name="Sites")
	protected static class Sites {		
		@XmlElement(name ="Site")
		public LinkedList<Site> sites;
		
		public Sites() {}
		
		public Sites(InputStream is) throws Exception {
			Sites sites = unmarshalSites(is);
			if(sites != null) {
				this.sites = sites.sites;
			}
		}
		
		public static Sites unmarshalSites(InputStream is) throws Exception {			
			JAXBContext jaxbContext = JAXBContext.newInstance(Sites.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (Sites)unmarshaller.unmarshal(is);			
		}
	}
}
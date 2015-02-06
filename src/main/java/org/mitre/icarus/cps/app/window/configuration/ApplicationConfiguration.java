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
 * Contains the application configuration settings.
 *
 * @author CBONACETO
 *
 */
public class ApplicationConfiguration {

    /**
     * Application types
     */
    public static enum ApplicationType {

        FullApplication, DeveloperToolsApplication,
        ExperimentClientApplication, ExperimentClientWebStart,
        PlayerOnlyApplication, PlayerOnlyWebStart,
        TrainingClientWebStart, TrainingClientApplet //These are new modes for use as a training/teaching tool
    }; 

    /**
     * Menu types
     */
    public static enum Menu {

        File, ExamControlOptions, FeatureVectorControlOptions, Help
    };

    /**
     * Application-level file options
     */
    public static enum FileOption {

        Open_Current_Exam_For_Data_Collection, 
        Open_Current_Exam_For_Demonstration,
        Open_Current_Exam_For_Training,
        Phase_Menu, Return_Home, Logout, Exit
    }

    /**
     * Application-level exam open options
     */
    public static enum ExamOpenOption {

        Open_For_Data_Collection, Open_For_Demonstration,
        Open_For_Training, Open_For_Playback, Open_For_Validation
    }

    /**
     * Application-level feature vector open options
     */
    public static enum FeatureVectorOpenOption {

        Open_For_Display, Open_For_Validation
    }

    /**
     * Application-level exam control options for exam demonstration, data
     * collection, and playback
     */
    public static enum ExamControlOption {

        Change_Particpant, Advance_To_Phase, Advance_To_Trial, Restart_Exam
    }

    /**
     * Application level feature vector control options
     */
    public static enum FeatureVectorControlOption {

        Clear_File_Data, Clear_All_Data
    };

    /**
     * The application type
     */
    private ApplicationType applicationType;

    /**
     * The application name
     */
    private String applicationName;

    /**
     * The application version information
     */
    private String applicationVersion;

    /**
     * The menu names
     */
    private EnumMap<Menu, String> menuNames;

    /**
     * The menus that are enabled
     */
    private EnumMap<Menu, Boolean> menusEnabled;

    /**
     * The application-level file options enabled
     */
    private EnumSet<FileOption> fileOptions;

    /**
     * The application-level exam open options enabled. These options may not be
     * enabled for all phases
     */
    private EnumSet<ExamOpenOption> examOpenOptions;

    /**
     * The application-level exam control options enabled. These options may not
     * be enabled for all phases
     */
    private EnumSet<ExamControlOption> examControlOptions;

    /**
     * The application-level feature vector open options enabled. These options
     * may not be enabled for all phases.
     */
    private EnumSet<FeatureVectorOpenOption> featureVectorOpenOptions;

    /**
     * The application-level feature vector control options enabled. These
     * options may not be enabled for all phases
     */
    private EnumSet<FeatureVectorControlOption> featureVectorControlOptions;

    /**
     * The CPD phases enabled
     */
    private String[] phases;

    /**
     * Whether to load the default exam when the application launches
     */
    private boolean loadDefaultExamOnLaunch;

    /**
     * Whether data collection is enabled
     */
    private boolean enableDataCollection;

    /**
     * Whether to show the security banner
     */
    private boolean showSecurityBanner;

    /**
     * Whether the application is FOUO
     */
    private boolean fouo;

    /**
     * Whether the application has been approved for public release
     */
    private boolean publicReleased;

    /**
     * Whether to warn the user if they take an action that will result in data
     * not being saved (e.g., logout while an experiment condition is in
     * progress)
     */
    private boolean warnIfDataLost;

    /**
     * Whether to warn the user before closing the application using the close
     * button
     */
    private boolean warnBeforeExiting;

    /**
     * Whether the application is running as an experiment client
     */
    private boolean clientMode;
    
    /** Whether access to the file system is permitted. If not, all
     items will be loaded from the class path (including exams). */
    private boolean fileSystemAccessible;
    
    /** Whether the application is running as an applet */
    private boolean applet;

    /**
     * The sites to choose from
     */
    private List<Site> sites;

    protected ApplicationConfiguration() {
        sites = initializeSites();

        menuNames = new EnumMap<Menu, String>(Menu.class);
        menuNames.put(Menu.File, "File");
        menuNames.put(Menu.ExamControlOptions, "Exam Options");
        menuNames.put(Menu.FeatureVectorControlOptions, "Feature Vector Options");
        menuNames.put(Menu.Help, "Help");

        menusEnabled = new EnumMap<Menu, Boolean>(Menu.class);
        menusEnabled.put(Menu.File, false);
        menusEnabled.put(Menu.ExamControlOptions, false);
        menusEnabled.put(Menu.FeatureVectorControlOptions, false);
        menusEnabled.put(Menu.Help, false);

        fileOptions = EnumSet.noneOf(FileOption.class);
        examOpenOptions = EnumSet.noneOf(ExamOpenOption.class);
        examControlOptions = EnumSet.noneOf(ExamControlOption.class);
        featureVectorOpenOptions = EnumSet.noneOf(FeatureVectorOpenOption.class);
        featureVectorControlOptions = EnumSet.noneOf(FeatureVectorControlOption.class);
    }

    protected final List<Site> initializeSites() {
        List<Site> sitesList = null;
        try {
            //Initialize list of sites from sites xml file
            Sites sites = new Sites(getClass().getClassLoader().getResourceAsStream("sites.xml"));
            if (sites != null) {
                sitesList = sites.sites;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //Create default sites
            sitesList = Arrays.asList(new Site("MITRE Bedford", "BED"), new Site("MITRE McLean", "MCL"));
        }
        return sitesList;
    }

    /**
     * @param applicationType
     * @param applicationVersion
     * @param phases
     * @param loadDefaultExamOnLaunch
     * @param publicReleased
     * @return
     */
    public static ApplicationConfiguration createInstance(ApplicationType applicationType,
            String applicationVersion, String[] phases,
            boolean loadDefaultExamOnLaunch, boolean publicReleased) {
        ApplicationConfiguration config = new ApplicationConfiguration();
        switch (applicationType) {
            case FullApplication:
                config.enableAllOptions();
                config.applicationName = "ICArUS Test & Evaluation Suite";
                break;
            case DeveloperToolsApplication:
                config.enableDeveloperToolsOptions();
                config.applicationName = "ICArUS Developer Tools Suite";
                break;
            case ExperimentClientApplication:
                config.enableExperimentClientOptions();
                config.applicationName = "ICArUS Experiment Software";
                break;
            case ExperimentClientWebStart:
                config.enableExperimentClientOptions();
                config.applicationName = "ICArUS Experiment Software";
                break;
            case PlayerOnlyApplication:
                config.enablePlayerOnlyOptions(false);
                config.applicationName = "ICArUS Test & Evaluation Player";
                break;
            case PlayerOnlyWebStart:
                config.enablePlayerOnlyOptions(true);
                config.clientMode = true;
                config.applicationName = "ICArUS Test & Evaluation Player";
                break;
            case TrainingClientWebStart:
            case TrainingClientApplet:
                config.enableTrainingOptions(false, 
                        applicationType == ApplicationType.TrainingClientApplet);
                config.applicationName = "ICArUS Training Suite";
                break;
        }
        config.applicationType = applicationType;
        config.applicationVersion = applicationVersion;
        config.phases = phases;
        config.loadDefaultExamOnLaunch = loadDefaultExamOnLaunch;
        config.publicReleased = publicReleased;
        if (publicReleased) {
            config.fouo = false;
        }
        return config;
    }

    protected void enableAllOptions() {
        menusEnabled.put(Menu.File, true);
        menusEnabled.put(Menu.ExamControlOptions, true);
        menusEnabled.put(Menu.FeatureVectorControlOptions, true);
        menusEnabled.put(Menu.Help, true);
        fileOptions.clear();
        for (FileOption option : FileOption.values()) {
            if (option != FileOption.Logout && option != FileOption.Return_Home
                    && option != FileOption.Open_Current_Exam_For_Training) {
                fileOptions.add(option);
            }
        }
        examOpenOptions.clear();
        for (ExamOpenOption option : ExamOpenOption.values()) {
            if(option != ExamOpenOption.Open_For_Training) {
                examOpenOptions.add(option);
            }
        }
        examControlOptions.clear();
        for (ExamControlOption option : ExamControlOption.values()) {
            examControlOptions.add(option);
        }
        featureVectorOpenOptions.clear();
        for (FeatureVectorOpenOption option : FeatureVectorOpenOption.values()) {
            featureVectorOpenOptions.add(option);
        }
        featureVectorControlOptions.clear();
        for (FeatureVectorControlOption option : FeatureVectorControlOption.values()) {
            featureVectorControlOptions.add(option);
        }
        loadDefaultExamOnLaunch = true;
        enableDataCollection = true;
        showSecurityBanner = true;
        warnIfDataLost = false;
        warnBeforeExiting = true;
        clientMode = false;
        fileSystemAccessible = true;
        fouo = true;
    }

    protected void enableDeveloperToolsOptions() {
        menusEnabled.put(Menu.File, true);
        menusEnabled.put(Menu.ExamControlOptions, true);
        menusEnabled.put(Menu.FeatureVectorControlOptions, true);
        menusEnabled.put(Menu.Help, true);
        fileOptions.clear();
        fileOptions.addAll(Arrays.asList(
                FileOption.Phase_Menu,
                FileOption.Exit));
        examOpenOptions.clear();
        examOpenOptions.addAll(Arrays.asList(
                ExamOpenOption.Open_For_Demonstration,
                ExamOpenOption.Open_For_Playback,
                ExamOpenOption.Open_For_Validation));
        examControlOptions.clear();
        examControlOptions.addAll(Arrays.asList(
                ExamControlOption.Restart_Exam,
                ExamControlOption.Advance_To_Phase,
                ExamControlOption.Advance_To_Trial));
        featureVectorOpenOptions.clear();
        for (FeatureVectorOpenOption option : FeatureVectorOpenOption.values()) {
            featureVectorOpenOptions.add(option);
        }
        featureVectorControlOptions.clear();
        for (FeatureVectorControlOption option : FeatureVectorControlOption.values()) {
            featureVectorControlOptions.add(option);
        }
        loadDefaultExamOnLaunch = false;
        enableDataCollection = false;
        showSecurityBanner = true;
        warnIfDataLost = false;
        warnBeforeExiting = false;
        clientMode = false;
        fileSystemAccessible = true;
        fouo = true;
    }

    protected void enableExperimenterOptions() {
        menusEnabled.put(Menu.File, true);
        menusEnabled.put(Menu.ExamControlOptions, true);
        menusEnabled.put(Menu.FeatureVectorControlOptions, false);
        menusEnabled.put(Menu.Help, true);
        fileOptions.clear();
        fileOptions.addAll(Arrays.asList(
                FileOption.Open_Current_Exam_For_Data_Collection,
                FileOption.Phase_Menu,
                FileOption.Exit));
        examOpenOptions.clear();
        examOpenOptions.addAll(Arrays.asList(
                ExamOpenOption.Open_For_Data_Collection));
        //ExamOpenOption.Open_Any_For_Data_Collection));
        examControlOptions.clear();
        for (ExamControlOption option : ExamControlOption.values()) {
            examControlOptions.add(option);
        }
        featureVectorOpenOptions.clear();
        featureVectorControlOptions.clear();
        loadDefaultExamOnLaunch = true;
        enableDataCollection = true;
        showSecurityBanner = true;
        warnIfDataLost = true;
        warnBeforeExiting = true;
        clientMode = true;
        fileSystemAccessible = false;
        fouo = false;
    }

    protected void enableExperimentClientOptions() {
        menusEnabled.put(Menu.File, true);
        menusEnabled.put(Menu.ExamControlOptions, true);
        menusEnabled.put(Menu.FeatureVectorControlOptions, false);
        menusEnabled.put(Menu.Help, true);
        fileOptions.clear();
        fileOptions.addAll(Arrays.asList(
                FileOption.Return_Home,
                FileOption.Logout,
                FileOption.Exit));
        examOpenOptions.clear();
        examControlOptions.clear();
        examControlOptions.addAll(Arrays.asList(
                ExamControlOption.Restart_Exam,
                ExamControlOption.Advance_To_Phase));
        featureVectorOpenOptions.clear();
        featureVectorControlOptions.clear();
        loadDefaultExamOnLaunch = true;
        enableDataCollection = true;
        showSecurityBanner = true;
        warnIfDataLost = true;
        warnBeforeExiting = true;
        clientMode = true;
        fileSystemAccessible = false;
        fouo = false;
    }

    protected void enablePlayerOnlyOptions(boolean clientMode) {
        menusEnabled.put(Menu.File, true);
        menusEnabled.put(Menu.ExamControlOptions, true);
        menusEnabled.put(Menu.FeatureVectorControlOptions, false);
        menusEnabled.put(Menu.Help, true);
        fileOptions.clear();
        fileOptions.addAll(Arrays.asList(
                FileOption.Phase_Menu,
                FileOption.Exit));
        examOpenOptions.clear();
        examOpenOptions.addAll(Arrays.asList(
                ExamOpenOption.Open_For_Playback));
        examControlOptions.clear();
        examControlOptions.addAll(Arrays.asList(
                ExamControlOption.Restart_Exam,
                ExamControlOption.Advance_To_Phase,
                ExamControlOption.Advance_To_Trial));
        featureVectorOpenOptions.clear();
        featureVectorControlOptions.clear();
        loadDefaultExamOnLaunch = false;
        enableDataCollection = false;
        showSecurityBanner = true;
        this.clientMode = clientMode;
        fileSystemAccessible = true;
        warnIfDataLost = false;
        warnBeforeExiting = false;
        fouo = true;
    }
    
    protected void enableTrainingOptions(boolean clientMode, boolean applet) {        
        menusEnabled.put(Menu.File, true);
        menusEnabled.put(Menu.ExamControlOptions, true);
        menusEnabled.put(Menu.FeatureVectorControlOptions, false);
        menusEnabled.put(Menu.Help, true);
        fileOptions.clear();
        fileOptions.clear();
        fileOptions.addAll(Arrays.asList(
                FileOption.Open_Current_Exam_For_Training,
                FileOption.Phase_Menu,
                FileOption.Exit));        
        examOpenOptions.clear();
        examOpenOptions.addAll(Arrays.asList(
                ExamOpenOption.Open_For_Training));        
        examControlOptions.clear();
        examControlOptions.addAll(Arrays.asList(
                ExamControlOption.Restart_Exam,
                ExamControlOption.Advance_To_Phase,
                ExamControlOption.Advance_To_Trial));        
        featureVectorOpenOptions.clear();               
        featureVectorControlOptions.clear();
        loadDefaultExamOnLaunch = false;
        enableDataCollection = false;
        showSecurityBanner = true;
        warnIfDataLost = false;
        warnBeforeExiting = false;
        this.clientMode = clientMode;
        fileSystemAccessible = false;
        this.applet = applet;
        fouo = true;
    }

    protected void disableAllOptions() {
        menusEnabled.put(Menu.File, false);
        menusEnabled.put(Menu.ExamControlOptions, false);
        menusEnabled.put(Menu.FeatureVectorControlOptions, false);
        menusEnabled.put(Menu.Help, false);
        fileOptions.clear();
        examOpenOptions.clear();
        examControlOptions.clear();
        featureVectorOpenOptions.clear();
        featureVectorControlOptions.clear();
        loadDefaultExamOnLaunch = false;
        enableDataCollection = false;
        showSecurityBanner = true;
        warnIfDataLost = false;
        warnBeforeExiting = false;
        fouo = true;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getMenuName(Menu menu) {
        return menuNames.get(menu);
    }

    public boolean isMenuEnabled(Menu menu) {
        return menusEnabled.get(menu);
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

    public boolean isExamOpenOptionEnabled(ExamOpenOption option) {
        return examOpenOptions.contains(option);
    }

    public void setExamOpenOptionEnabled(ExamOpenOption option) {
        examOpenOptions.add(option);
    }

    public Collection<ExamOpenOption> getExamOpenOptions() {
        return examOpenOptions;
    }

    public boolean isExamControlOptionEnabled(ExamControlOption option) {
        return examControlOptions.contains(option);
    }

    public void setExamControlOptionEnabled(ExamControlOption option) {
        examControlOptions.add(option);
    }

    public Collection<ExamControlOption> getExamControlOptions() {
        return examControlOptions;
    }

    public boolean isFeatureVectorOpenOptionEnabled(FeatureVectorOpenOption option) {
        return featureVectorOpenOptions.contains(option);
    }

    public void setFeatureVectorOpenOptionEnabled(FeatureVectorOpenOption option) {
        featureVectorOpenOptions.add(option);
    }

    public Collection<FeatureVectorOpenOption> getFeatureVectorOpenOptions() {
        return featureVectorOpenOptions;
    }

    public boolean isFeatureVectorControlOptionEnabled(FeatureVectorControlOption option) {
        return featureVectorControlOptions.contains(option);
    }

    public void setFeatureVectorControlOptionEnabled(FeatureVectorControlOption option) {
        featureVectorControlOptions.add(option);
    }

    public Collection<FeatureVectorControlOption> getFeatureVectorControlOptions() {
        return featureVectorControlOptions;
    }

    public String[] getPhases() {
        return phases;
    }

    public boolean isLoadDefaultExamOnLaunch() {
        return loadDefaultExamOnLaunch;
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

    public boolean isPublicReleased() {
        return publicReleased;
    }

    public boolean isWarnIfDataLost() {
        return warnIfDataLost;
    }

    public boolean isWarnBeforeExiting() {
        return warnBeforeExiting;
    }

    public boolean isClientMode() {
        return clientMode;
    }

    public boolean isFileSystemAccessible() {
        return fileSystemAccessible;
    }

    public boolean isApplet() {
        return applet;
    }

    public List<Site> getSites() {
        return sites;
    }

    @XmlRootElement(name = "Sites")
    protected static class Sites {

        @XmlElement(name = "Site")
        public LinkedList<Site> sites;

        public Sites() {
        }

        public Sites(InputStream is) throws Exception {
            Sites sites = unmarshalSites(is);
            if (sites != null) {
                this.sites = sites.sites;
            }
        }

        public static Sites unmarshalSites(InputStream is) throws Exception {
            JAXBContext jaxbContext = JAXBContext.newInstance(Sites.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Sites) unmarshaller.unmarshal(is);
        }
    }
}

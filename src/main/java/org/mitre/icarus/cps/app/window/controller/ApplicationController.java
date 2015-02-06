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
package org.mitre.icarus.cps.app.window.controller;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mitre.icarus.cps.app.experiment.IcarusExperimentController;
import org.mitre.icarus.cps.app.experiment.IcarusSubjectData;
import org.mitre.icarus.cps.app.util.CPSFileUtils;
import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.widgets.client.HomeDialog;
import org.mitre.icarus.cps.app.widgets.client.LoginDialog;
import org.mitre.icarus.cps.app.widgets.client.RegistrationDialog;
import org.mitre.icarus.cps.app.widgets.dialog.ErrorDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ExamListSelectionDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ExamPlaybackFileSelectionDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ParticipantIdDlg;
import org.mitre.icarus.cps.app.widgets.dialog.SkipToPhaseDlg;
import org.mitre.icarus.cps.app.widgets.dialog.TrialSelectionDlg;
import org.mitre.icarus.cps.app.widgets.dialog.ValidationErrorsDlg;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressEvent;
import org.mitre.icarus.cps.app.widgets.events.ButtonPressListener;
import org.mitre.icarus.cps.app.widgets.progress_monitor.SwingProgressMonitor;
import org.mitre.icarus.cps.app.window.IApplicationWindow;
import org.mitre.icarus.cps.app.window.ApplicationWindow;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ApplicationType;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FileOption;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.configuration.FileLocation;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfiguration;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfigurationFactory;
import org.mitre.icarus.cps.app.window.menu.ApplicationMenuListener;
import org.mitre.icarus.cps.exam.base.IcarusExam;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.experiment_core.Experiment;
import org.mitre.icarus.cps.experiment_core.condition.Condition;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;
import org.mitre.icarus.cps.web.experiments.client.DataSourceFactory;
import org.mitre.icarus.cps.web.experiments.client.IDataSource;
import org.mitre.icarus.cps.web.experiments.client.exceptions.ClientException;
import org.mitre.icarus.cps.web.model.ExamList;
import org.mitre.icarus.cps.web.model.ExamList.ExamListEntry;
import org.mitre.icarus.cps.web.model.Role;
import org.mitre.icarus.cps.web.model.Site;
import org.mitre.icarus.cps.web.model.User;

/**
 * The main application class. Creates the application window. TODO: Add
 * additional exception handling, return exceptions encountered, also need an
 * applet version (for the player), show blank page if errors encountered
 *
 * @author CBONACETO
 *
 */
@SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
public final class ApplicationController implements ApplicationMenuListener, ButtonPressListener, HyperlinkListener, SubjectActionListener {

    /**
     * Enumeration defining states the application can be in
     */
    public static enum ApplicationState {

        Showing_Exam_For_Demonstration, Showing_Exam_For_Training, Showing_Exam_For_Data_Collection,
        Playing_Back_Exam_Response, Showing_Feature_Vector_Data, Blank, Exited
    };

    /**
     * The application version
     */
    public static final String VERSION = "2.2b";

    /**
     * The application type
     */
    //This should be set to ApplicationType.DeveloperToolsApplication when building the developer tools version
    //private static final ApplicationType APPLICATION_TYPE = ApplicationType.FullApplication;
	//private static final ApplicationType APPLICATION_TYPE = ApplicationType.DeveloperToolsApplication;
    //private static final ApplicationType APPLICATION_TYPE = ApplicationType.ExperimentClientApplication;
    //private static final ApplicationType APPLICATION_TYPE = ApplicationType.ExperimentClientWebStart;
    //private static final ApplicationType APPLICATION_TYPE = ApplicationType.PlayerOnlyApplication;

    /**
     * Whether to load the current default exam (if any) on launch
     */
    //This should be set to false when building the developer tools version
    //private static final boolean LOAD_DEFAULT_EXAM_ON_LAUNCH = false;

    /**
     * The phases enabled
     */
    //private static final String[] PHASES = {"1", "2"};

    /**
     * The current application state
     */
    protected ApplicationState applicationState;

    /**
     * The application configuration
     */
    protected ApplicationConfiguration configuration;

    /**
     * The phase controllers for each challenge problem phase (mapped by phase
     * ID)
     */
    protected Map<String, PhaseController<?, ?, ?, ?, ?, ?, ?, ?>> phaseControllers;

    /**
     * The current challenge problem phase controller
     */
    protected PhaseController<?, ?, ?, ?, ?, ?, ?, ?> currentPhaseController;

    /**
     * The default exam file (if any)
     */
    protected FileLocation defaultExamFile;
    protected String defaultExamFilePhaseId;
    
    /**
     * The Phase 1 and Phase 2 exams packaged for opening from the classpath.
     * Used when the file system is not accessible.
     */
    protected ExamList examList;

    /**
     * The current site
     */
    protected Site site = new Site("DEMO", "DEMO");

    /**
     * The current subject ID
     */
    protected String subjectId;

    /**
     * The current subject user name
     */
    protected String subjectName;

    /**
     * Roles for the current subject when running as a client
     */
    protected List<Role> subjectRoles;

    protected List<String> completedTasks;

    /**
     * Remote server connection when running as a client
     */
    protected IDataSource dataSourceConnection;

    /**
     * File chooser for opening exam files for data collection, validation,
     * playback, etc.
     */
    protected static JFileChooser examFileChooser;

    /**
     * Dialog for opening exam playback files
     */
    protected ExamPlaybackFileSelectionDlg examPlaybackFileChooser;

    /**
     * File chooser for opening feature vector files to display
     */
    protected static JFileChooser featureVectorFileChooser;

    /**
     * The application window
     */
    protected IApplicationWindow applicationWindow;

    /**
     * The login screen
     */
    protected LoginDialog loginDlg;

    /**
     * Dialog to create a new subject/user account
     */
    protected RegistrationDialog registrationDlg;

    /**
     * The home screen
     */
    protected HomeDialog homeDlg;

    protected boolean contentShown = false;

    /**
     *
     */
    public ApplicationController() {
        this(ApplicationConfiguration.createInstance(ApplicationType.FullApplication, 
                VERSION, new String[] {"1", "2"}, false, true));
    }
    
    /**
     *
     * @param config
     */
    public ApplicationController(ApplicationConfiguration config) {
        this.configuration = config;
        if (configuration.isClientMode()) {
            dataSourceConnection = DataSourceFactory.createDefaultDataSource();
        }
        File outputFolder = createOutputFolder();

        //Create the phase configurations
        List<PhaseConfiguration<?, ?, ?, ?, ?>> phaseConfigurations = new LinkedList<PhaseConfiguration<?, ?, ?, ?, ?>>();        
        for (String phaseId : configuration.getPhases()) {
            try {                
                PhaseConfiguration<?, ?, ?, ?, ?> phaseConfiguration
                        = PhaseConfigurationFactory.createPhaseConfiguration(phaseId);
                if (phaseConfiguration != null) {
                    phaseConfigurations.add(phaseConfiguration);
                    if (phaseConfiguration.getDefaultExamFile() != null) {
                        defaultExamFile = phaseConfiguration.getDefaultExamFile();
                        defaultExamFilePhaseId = phaseId;
                    }
                    if (phaseConfiguration.getDefaultExamFileLocation() != null) {
                        phaseConfiguration.setExamFile(phaseConfiguration.getDefaultExamFileLocation());
                        phaseConfiguration.setFeatureVectorFile(phaseConfiguration.getDefaultExamFileLocation());
                    }
                    if (phaseConfiguration.getDefaultPlaybackFileLocations() != null) {
                        phaseConfiguration.setExamPlaybackFiles(phaseConfiguration.getDefaultPlaybackFileLocations());
                    }
                }
            } catch (Exception ex) {
                handleException(new Exception("Error initializing the application", ex), true); 
            }
        }

        //Create the application window and register to receive menu events
        applicationWindow = new ApplicationWindow(configuration, phaseConfigurations, defaultExamFile != null);
        applicationWindow.getMenu().addMenuListener(this);

        //Create the phase controllers
        try {
            phaseControllers = new HashMap<String, PhaseController<?, ?, ?, ?, ?, ?, ?, ?>>();
            for (PhaseConfiguration<?, ?, ?, ?, ?> phaseConfiguration : phaseConfigurations) {
                PhaseController<?, ?, ?, ?, ?, ?, ?, ?> pc = PhaseControllerFactory.createPhaseController(
                        phaseConfiguration, applicationWindow,
                        configuration.isFileOptionEnabled(FileOption.Return_Home),
                        configuration.isEnableDataCollection(),
                        configuration.isClientMode(), dataSourceConnection, outputFolder);
                pc.setSubjectActionListener(this);
                phaseControllers.put(phaseConfiguration.getPhaseId(), pc);
            }
        } catch (IllegalArgumentException ex) {
            handleException(new Exception("Error initializing the application", ex), true);                
        }

        if (applicationWindow.getWindowComponent() instanceof Frame) {
            Frame frame = (Frame) applicationWindow.getWindowComponent();
            frame.setSize(new Dimension(800, 600));
            CPSUtils.centerFrameOnScreen(frame);
            //Add listener to show warning dialog before closing the app
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    if (configuration.isWarnBeforeExiting()) {
                        confirmExit();
                    } else {
                        exit();
                    }
                }
            });
        }
        applicationWindow.setVisible(true);
        
        //Load the exam list if the file system is not accesible
        if(!configuration.isFileSystemAccessible()) {
            try {
              examList = new ExamList(getClass().getClassLoader().getResourceAsStream("exams.xml"));;  
            } catch(Exception e) {
                e.printStackTrace();
                examList = new ExamList(new ArrayList<ExamListEntry>());
            }
        }

        if (configuration.isLoadDefaultExamOnLaunch() && defaultExamFile != null) {
            //Load the current default exam
            try {
                if (defaultExamFile.getFileUrl() == null) {
                    resolveFileLocation(defaultExamFile);
                }
                IcarusSubjectData subjectData = ParticipantIdDlg.showDialog(applicationWindow.getWindowComponent(),
                        "Begin", configuration.getSites(), null,
                        "Begin Exam", false);
                if (subjectData != null) {
                    subjectId = subjectData.getSubjectId();
                    site = subjectData.getSite();
                    openExamForDataCollection(defaultExamFile, defaultExamFilePhaseId);
                    if (currentPhaseController != null
                            && currentPhaseController.getPhaseConfiguration().getDefaultExamFileLocation() != null) {
                        currentPhaseController.getPhaseConfiguration().setExamFile(
                                currentPhaseController.getPhaseConfiguration().getDefaultExamFileLocation());
                    }
                    applicationState = ApplicationState.Showing_Exam_For_Data_Collection;
                } else {
                    applicationState = ApplicationState.Blank;
                }
            } catch (Exception ex) {
                applicationState = ApplicationState.Blank;
                handleException(new Exception("Error loading the current exam", ex), true);                
            }
        } else {
            applicationState = ApplicationState.Blank;
        }
    }

    protected final File createOutputFolder() {
        File outputFolder = null;
        if (configuration.isEnableDataCollection() && configuration.isFileSystemAccessible()) {
            if (configuration.getApplicationType() == ApplicationType.ExperimentClientWebStart
                    || configuration.getApplicationType() == ApplicationType.PlayerOnlyWebStart
                    || configuration.getApplicationType() == ApplicationType.TrainingClientWebStart) {
                File homeDir = CPSFileUtils.getUserHomeDirectory();
                //String homeDir= System.getProperty("user.home");		
                if (homeDir == null) {
                    homeDir = new File("");
                    //homeDir = new File("").getAbsolutePath();
                }
                //Create local data folder if it doesn't exist
                outputFolder = CPSFileUtils.createFile(homeDir.getPath() + "/ICArUS_Data");
                if (!outputFolder.exists()) {
                    outputFolder.mkdir();
                }
            } else if (!configuration.isApplet()) {
                outputFolder = CPSFileUtils.createFile("data/response_data");
            }
        }
        return outputFolder;
    }

    @Override
    public void fileOptionSelected(FileOption fileOption) {
        checkExited();
        switch (fileOption) {
            case Exit:
                final boolean confirmExit = configuration.isFileOptionEnabled(FileOption.Logout)
                        && configuration.isWarnBeforeExiting();
                if (confirmExit) {
                    confirmExit();
                } else {
                    exit();
                }
                break;
            case Logout:
                logout();
                break;
            case Open_Current_Exam_For_Data_Collection:
            case Open_Current_Exam_For_Demonstration:
            case Open_Current_Exam_For_Training:
                if (defaultExamFile != null) {
                    if (defaultExamFile.getFileUrl() == null) {
                        try {
                            resolveFileLocation(defaultExamFile);
                        } catch (MalformedURLException ex) {
                            handleException(new Exception("Error loading the current exam", ex), true);                
                        }
                    }
                    if (fileOption == FileOption.Open_Current_Exam_For_Data_Collection) {
                        if (subjectId == null) {
                            //Show a subject ID dialog to get the subject ID
                            IcarusSubjectData subjectData = ParticipantIdDlg.showDialog(applicationWindow.getWindowComponent(),
                                    "Enter Participant ID",
                                    configuration.getSites(), site, "OK", true);
                            if (subjectData != null) {
                                subjectId = subjectData.getSubjectId();
                                site = subjectData.getSite();
                            }
                        }
                        if (subjectId != null) {
                            openExamForDataCollection(defaultExamFile, defaultExamFilePhaseId);
                        }
                    } else {
                        openExamForDemonstrationOrTraining(defaultExamFile, defaultExamFilePhaseId, 
                                fileOption == FileOption.Open_Current_Exam_For_Training);
                    }
                }
                break;
            case Return_Home:
                returnHome();
                break;
            default:
                break;
        }
    }

    @Override
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void examOpenOptionSelected(ExamOpenOption openOption, String phaseId) {
        checkExited();
        PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
        if (phaseController != null) {
            switch (openOption) {
                case Open_For_Data_Collection:
                case Open_For_Demonstration:
                case Open_For_Training:
                    try {
                        //Show the exam file chooser dialog or the exam selection dialog
                        FileLocation examFileLocation = null;
                        FileDescriptor examFileType = phaseController.getPhaseConfiguration().getExamFileType();
                        if (configuration.isFileSystemAccessible()) {
                            File examFile = chooseExamFile(phaseController.getPhaseConfiguration().getExamFile(), examFileType,
                                    openOption == ExamOpenOption.Open_For_Data_Collection
                                    ? "Open Phase " + phaseId + " Exam for Data Collection"
                                    : openOption == ExamOpenOption.Open_For_Demonstration
                                    ? "Open Phase " + phaseId + " Exam for Demonstration"
                                    : "Open Phase " + phaseId + " Exam for Training");
                            if (examFile != null) {
                                examFileLocation = new FileLocation(examFile.toURI().toURL(),
                                        examFile, examFileType);
                            }
                        } else {                            
                            ExamListEntry exam = ExamListSelectionDlg.showDialog(
                                    applicationWindow.getWindowComponent(), 
                                    "Select a Phase " + phaseId + " Exam", phaseId,
                                    examList);
                            if(exam != null) {
                                examFileLocation = new FileLocation(exam.examName,
                                        exam.examFileLocation, examFileType);
                                resolveFileLocation(examFileLocation);
                            }
                        }
                        if (examFileLocation != null) {                            
                            if (openOption == ExamOpenOption.Open_For_Data_Collection) {
                                if (subjectId == null) {
                                    //Show a subject ID dialog to get the subject ID
                                    IcarusSubjectData subjectData = ParticipantIdDlg.showDialog(applicationWindow.getWindowComponent(),
                                            "Enter Participant ID",
                                            configuration.getSites(), site, "OK", true);
                                    if (subjectData != null) {
                                        subjectId = subjectData.getSubjectId();
                                        site = subjectData.getSite();
                                    }
                                }
                                if (subjectId != null) {
                                    openExamForDataCollection(examFileLocation, phaseId);
                                }
                            } else {
                                openExamForDemonstrationOrTraining(examFileLocation, phaseId,
                                        openOption == ExamOpenOption.Open_For_Training);
                            }
                        }
                    } catch (Exception ex) {
                        handleException(new Exception("Error loading exam file", ex), true);
                    }
                    break;
                case Open_For_Playback:
                    if (!configuration.isFileSystemAccessible()) {
                        handleException(new Exception("Error, access to the file system to open "
                                + "files for playback not supported.", null), false);
                    } else {
                        try {
                            List<FileDescriptor> examPlaybackFileTypes = phaseController.getPhaseConfiguration().getExamPlaybackFileTypes();
                            List<File> examPlaybackFiles = chooseExamPlaybackFiles(
                                    phaseController.getPhaseConfiguration().getExamPlaybackFiles(),
                                    phaseController.getPhaseConfiguration().getDefaultPlaybackFileLocations(),
                                    phaseController.getPhaseConfiguration().getExamPlaybackFileTypes(),
                                    "Choose Phase " + phaseId + " Exam Playback Files");
                            if (examPlaybackFiles != null) {
                                List<FileLocation> examPlaybackFileLocations = new ArrayList<FileLocation>();
                                int i = 0;
                                for (File file : examPlaybackFiles) {
                                    examPlaybackFileLocations.add(new FileLocation(file.toURI().toURL(), file,
                                            examPlaybackFileTypes.get(i)));
                                    i++;
                                }
                                openExamForPlayback(examPlaybackFileLocations, phaseId);
                            }
                        } catch (Exception ex) {
                            handleException(new Exception("Error loading exam playback files", ex), true);
                        }
                    }
                    break;
                case Open_For_Validation:                  
                    try {
                        FileDescriptor examFileType = phaseController.getPhaseConfiguration().getExamFileType();
                        File examFile = chooseExamFile(phaseController.getPhaseConfiguration().getExamFile(), examFileType,
                                "Validate Phase " + phaseId + " Exam File");
                        if (examFile != null) {
                            FileLocation examFileLocation = new FileLocation(examFile.toURI().toURL(),
                                    examFile, examFileType);
                            openExamForValidation(examFileLocation, phaseId);
                        }
                    } catch (Exception ex) {
                        handleException(new Exception("Error loading exam file", ex), true);
                    }
                    break;
                default:
                    break;
            }
        } else {
            handleException(new Exception("Phase " + phaseId + " is not supported by this version of the software."), false);
        }
    }

    @Override
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void featureVectorOpenOptionSelected(FeatureVectorOpenOption openOption,
            FileDescriptor featureVectorFileType, String phaseId) {
        checkExited();
        if (!configuration.isFileSystemAccessible()) {
             handleException(new Exception("Error, access to the file system to open feature "
                     + "vector files is not supported.", null), false);
        } else {
            PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
            if (phaseController != null) {
                switch (openOption) {
                    case Open_For_Display:
                        File featureVectorFile = chooseFeatureVectorFile(
                                phaseController.getPhaseConfiguration().getFeatureVectorFile(),
                                featureVectorFileType,
                                "Open " + featureVectorFileType.getFileTypeTitle() + " File for Display");
                        if (featureVectorFile != null) {
                            try {
                                FileLocation featureVectorFileLocation = new FileLocation(featureVectorFile.toURI().toURL(),
                                        featureVectorFile, featureVectorFileType);
                                openFeatureVectorForDisplay(featureVectorFileLocation, phaseId);
                            } catch (Exception ex) {
                                handleException(new Exception("Error opening feature vector file", ex), true);
                            }
                        }
                        break;
                    case Open_For_Validation:
                        featureVectorFile = chooseFeatureVectorFile(phaseController.getPhaseConfiguration().getFeatureVectorFile(),
                                featureVectorFileType,
                                "Validate " + featureVectorFileType.getFileTypeTitle() + " File");
                        //"Open Feature Vector for Validation");
                        if (featureVectorFile != null) {
                            try {
                                FileLocation featureVectorFileLocation = new FileLocation(featureVectorFile.toURI().toURL(),
                                        featureVectorFile, featureVectorFileType);
                                openFeatureVectorForValidation(featureVectorFileLocation, phaseId);
                            } catch (Exception e) {
                                handleException(new Exception("Error opening feature vector file", e), true);
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {
                handleException(new Exception("Phase " + phaseId + " is not supported by this version of the software."), false);
            }
        }
    }

    @Override
    public void examControlOptionSelected(ExamControlOption controlOption) {
        checkExited();
        if (applicationState == ApplicationState.Playing_Back_Exam_Response
                || applicationState == ApplicationState.Showing_Exam_For_Data_Collection
                || applicationState == ApplicationState.Showing_Exam_For_Demonstration
                || applicationState == ApplicationState.Showing_Exam_For_Training) {
            final IcarusExperimentController<?, ?, ?, ?> examController = currentPhaseController != null
                    ? currentPhaseController.getActiveExamController() : null;
            switch (controlOption) {
                case Advance_To_Phase:
                    //Go to a different phase in the exam
                    if (examController != null && examController.getExam() != null) {
                        Integer phase = SkipToPhaseDlg.showDialog(applicationWindow.getWindowComponent(),
                                examController.getExam().getConditions(),
                                examController.getCurrentConditionIndex(),
                                currentPhaseController != null ? currentPhaseController.getPhaseConfiguration().getTaskTypeName() : "Mission");
                        if (phase != null && phase >= 0) {
                            advanceToExamPhase(phase);
                        }
                    }
                    break;
                case Advance_To_Trial:
                    //Go to a different trial in the current test phase in the exam (if the current phase is a test phase and this action is supported)
                    //TODO: Add checks to see if this action is supported
                    if (examController != null && examController.getCurrentExamPhase() != null
                            && examController.getCurrentExamPhase() instanceof IcarusTestPhase<?>) {
                        List<? extends IcarusTestTrial> trials = examController.getCurrentTrials();
                        if (trials != null && !trials.isEmpty()) {
                            Integer trial = TrialSelectionDlg.showDialog(applicationWindow.getWindowComponent(),
                                    trials, examController.getCurrentTrialNumber());
                            if (trial != null) {
                                advanceToExamTrialInCurrentPhase(trial);
                            }
                        }
                    }
                    break;
                case Change_Particpant:
                    //Change the subject ID and site of the participant
                    IcarusSubjectData subjectData = ParticipantIdDlg.showDialog(
                            applicationWindow.getWindowComponent(), "Enter Participant ID",
                            configuration.getSites(), site, "OK", true);
                    if (subjectData != null) {
                        changeExamParticipant(subjectData.getSubjectId(), subjectData.getSite());
                    }
                    break;
                case Restart_Exam:
                    //Restart the exam from the beginning
                    if (JOptionPane.showConfirmDialog(applicationWindow.getWindowComponent(),
                            "Would you like to restart the current exam?", "",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        restartExam();
                    }
                    break;
            }
        }
    }

    @Override
    public void featureVectorControlOptionSelected(FeatureVectorControlOption controlOption,
            FileDescriptor featureVectorFileType) {
        checkExited();
        if (applicationState == ApplicationState.Showing_Feature_Vector_Data
                && currentPhaseController != null) {
            switch (controlOption) {
                case Clear_All_Data:
                    currentPhaseController.clearAllFeatureVectorData();
                    break;
                case Clear_File_Data:
                    currentPhaseController.clearFeatureVectorData(featureVectorFileType);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void buttonPressed(ButtonPressEvent event) {
        checkExited();
        switch (event.getButtonId()) {
            case ButtonPressEvent.BUTTON_OK:
                //Try to create a new user
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        createUser();
                    }
                });
                break;
            case ButtonPressEvent.BUTTON_CANCEL:
                //Cancel create new user
                registrationDlg.setVisible(false);
                break;
            case ButtonPressEvent.BUTTON_CREATE_USER:
                registrationDlg.setLocationRelativeTo(applicationWindow.getWindowComponent());
                registrationDlg.resetFields();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        registrationDlg.setVisible(true);
                    }
                });
                break;
            case ButtonPressEvent.BUTTON_EXIT:
                //Exit
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (configuration.isWarnBeforeExiting()) {
                            confirmExit();
                        } else {
                            exit();
                        }
                    }
                });
                break;
            case ButtonPressEvent.BUTTON_LOGIN:
                //Login
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                });
                break;
            case ButtonPressEvent.BUTTON_LOGOUT:
                //Logout
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logout();
                    }
                });
                break;
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        checkExited();
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            //Process a selection on the home screen
            if (e.getDescription().equalsIgnoreCase("start_at_beginning")) {
                //Start the current experiment at the beginning
                boolean startExam = true;
                if (completedTasks != null && !completedTasks.isEmpty()) {
                    //Warn that previous data will be lost if the exam is restarted
                    StringBuilder taskTypeName = new StringBuilder(
                            currentPhaseController != null ? currentPhaseController.getPhaseConfiguration().getTaskTypeName() : "Mission");
                    taskTypeName.append("s");
                    startExam = JOptionPane.showConfirmDialog(applicationWindow.getWindowComponent(),
                            "<html>You must redo all " + taskTypeName.toString() + " if you restart the experiment."
                            + "<br>Would you still like to restart the experiment?</html>", "",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
                    if (startExam) {
                        //TODO: Delete data from previously completed tasks
                    }
                }
                if (startExam) {
                    homeDlg.setVisible(false);
                    /*boolean pilotOpened = openDefaultExam();
                     if(pilotOpened) {
                     showExam(defaultExam, subjectId, site);					
                     } else {*/
                    homeDlg.setLocationRelativeTo(applicationWindow.getWindowComponent());
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            homeDlg.setVisible(true);
                        }
                    });
                    //}
                }
            } else if (e.getDescription().equalsIgnoreCase("start_where_leftoff")) {
                //Start the current experiment where the subject left off
                homeDlg.setVisible(false);
                /*boolean pilotOpened = false;
                 //System.out.println("Completed Tasks: " + completedTasks.size());
                 Integer currentTaskNumber = getCurrenTaskInCurrentExam(completedTasks);
                 pilotOpened = openDefaultExam();
                 if(pilotOpened) {					
                 showExam(defaultExam, subjectId, site);
                 if(currentTaskNumber != null && 
                 currentTaskNumber > 0 && currentTaskNumber < defaultExam.getConditions().size()) {
                 examController.skipToCondition(currentTaskNumber);
                 }
                 } else {*/
                homeDlg.setLocationRelativeTo(applicationWindow.getWindowComponent());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        homeDlg.setVisible(true);
                    }
                });
                //}
            }
        }
    }

    @Override
    public void subjectActionPerformed(SubjectActionEvent event) {
        checkExited();
        if (event.eventType == SubjectActionEvent.EXIT_BUTTON_PRESSED) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    returnHome();
                }
            });
        }
    }

    /**
     * Open an exam for demonstration or training.
     * 
     * @param examFile
     * @param phaseId
     * @param openForTraining
     */
    public void openExamForDemonstrationOrTraining(FileLocation examFile, String phaseId,
            boolean openForTraining) {        
        checkExited();
        PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
        if (phaseController != null) {
            try {
                SwingProgressMonitor progressMonitor = new SwingProgressMonitor(applicationWindow.getWindowComponent(),
                        "Opening Exam", "Opening Exam", 0, 100);
                applicationWindow.clearContentPane();
                if (currentPhaseController != null) {
                    currentPhaseController.stopExam();
                }
                String name = openForTraining ? "TRAIN" : "DEMO";
                JComponent experimentPanel = phaseController.showExamForDataCollectionOrDemonstration(examFile, false,
                        new IcarusSubjectData(name, new Site(name, name), 0), this, progressMonitor);
                applicationWindow.setContentPaneComponent(experimentPanel, true,
                        phaseController.getPhaseConfiguration().getExamDataCollectionWindowAlignment());//!contentShown);
                contentShown = true;
                updateWindowTitleFileNames(Collections.singleton(examFile.getFileName()));
                applicationState = openForTraining ? ApplicationState.Showing_Exam_For_Training 
                        : ApplicationState.Showing_Exam_For_Demonstration;
                currentPhaseController = phaseController;
                currentPhaseController.getPhaseConfiguration().setExamFile(examFile);
                applicationWindow.getMenu().configureMenus(currentPhaseController.getPhaseConfiguration(), applicationState);
            } catch (Exception ex) {
                handleException(new Exception("An error occured opening the exam.", ex), true);
            }
        } else {
            handleException(new Exception("Phase " + phaseId + " is not supported by this version of the software."), false);
        }
    }

    /**
     * @param examFile
     * @param phaseId
     */
    public void openExamForDataCollection(FileLocation examFile, String phaseId) {
        checkExited();
        PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
        if (phaseController != null) {
            try {
                SwingProgressMonitor progressMonitor = new SwingProgressMonitor(applicationWindow.getWindowComponent(),
                        "Opening Exam", "Opening Exam", 0, 100);
                applicationWindow.clearContentPane();
                if (currentPhaseController != null) {
                    currentPhaseController.stopExam();
                }
                JComponent experimentPanel = phaseController.showExamForDataCollectionOrDemonstration(examFile, true,
                        new IcarusSubjectData(subjectId, site, 0), this, progressMonitor);
                applicationWindow.setContentPaneComponent(experimentPanel, true,
                        phaseController.getPhaseConfiguration().getExamDataCollectionWindowAlignment());//!contentShown);	
                contentShown = true;
                updateWindowTitleFileNames(Collections.singleton(examFile.getFileName()));
                applicationState = ApplicationState.Showing_Exam_For_Data_Collection;
                currentPhaseController = phaseController;
                currentPhaseController.getPhaseConfiguration().setExamFile(examFile);
                applicationWindow.getMenu().configureMenus(currentPhaseController.getPhaseConfiguration(), applicationState);
            } catch (Exception ex) {
                handleException(new Exception("An error occured opening the exam.", ex), true);
            }
        } else {
            handleException(new Exception("Phase " + phaseId + " is not supported by this version of the software."), false);
        }
    }

    /**
     * Validate an exam file.
     * 
     * @param examFile
     * @param phaseId
     */
    public void openExamForValidation(FileLocation examFile, String phaseId) {
        checkExited();
        PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
        if (phaseController != null) {
            try {
                //Validate the exam file
                Exception e = null;
                try {
                    phaseController.loadExamForDataCollectionOrDemonstration(examFile, true, null);
                } catch (Exception e1) {
                    e = e1;
                }
                if (e == null) {
                    JOptionPane.showMessageDialog(applicationWindow.getWindowComponent(),
                            "The exam file was valid.",
                            "Exam Valid",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    ValidationErrorsDlg.showErrorDialog(applicationWindow.getWindowComponent(),
                            "Exam Not Valid",
                            "The exam file contained errors: ",
                            e.getMessage());
                }
            } catch (Exception e) {
                handleException(new Exception("Error loading exam file", e), true);
            }
        } else {
            handleException(new Exception("Phase " + phaseId + " is not supported by this version of the software."), false);
        }
    }

    public void openExamForPlayback(List<FileLocation> examPlaybackFiles, String phaseId) {
        checkExited();
        PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
        if (phaseController != null) {
            try {
                SwingProgressMonitor progressMonitor = new SwingProgressMonitor(applicationWindow.getWindowComponent(),
                        "Opening Exam Playback Files", "Opening Exam Playback Files", 0, 100);
                applicationWindow.clearContentPane();
                if (currentPhaseController != null) {
                    currentPhaseController.stopExam();
                }
                JComponent experimentPanel = phaseController.showExamForPlayback(examPlaybackFiles, this, progressMonitor);
                applicationWindow.setContentPaneComponent(experimentPanel, true,
                        phaseController.getPhaseConfiguration().getExamPlaybackWindowAlignment());//!contentShown);
                contentShown = true;
                updateWindowTitleFiles(examPlaybackFiles);
                applicationState = ApplicationState.Playing_Back_Exam_Response;
                currentPhaseController = phaseController;
                currentPhaseController.getPhaseConfiguration().setExamPlaybackFiles(examPlaybackFiles);
                applicationWindow.getMenu().configureMenus(currentPhaseController.getPhaseConfiguration(), applicationState);
            } catch (Exception ex) {
                handleException(new Exception("An error occured opening the exam playback files.", ex), true);
            }
        } else {
            handleException(new Exception("Phase " + phaseId + " is not supported by this version of the software."), false);
        }
    }

    /**
     * @param featureVectorFile
     * @param phaseId
     */
    public void openFeatureVectorForDisplay(FileLocation featureVectorFile, String phaseId) {
        checkExited();
        PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
        if (phaseController != null) {
            try {
                SwingProgressMonitor progressMonitor = new SwingProgressMonitor(applicationWindow.getWindowComponent(),
                        "Opening Feature Vector Files", "Opening Feature Vector Files", 0, 100);
                applicationWindow.clearContentPane();
                if (currentPhaseController != null && currentPhaseController.isExamRunning()) {
                    currentPhaseController.stopExam();
                }
                if (currentPhaseController != phaseController
                        || applicationState != ApplicationState.Showing_Feature_Vector_Data) {
                    applicationState = ApplicationState.Showing_Feature_Vector_Data;
                    if (currentPhaseController != phaseController) {
                        currentPhaseController = phaseController;
                        currentPhaseController.clearAllFeatureVectorData();
                    }
                    applicationWindow.getMenu().configureMenus(currentPhaseController.getPhaseConfiguration(), applicationState);
                }
                JComponent mapPanel = phaseController.getMapPanel();
                if (applicationWindow.getContentPaneComponent() != mapPanel) {
                    applicationWindow.setContentPaneComponent(mapPanel, true,
                            phaseController.getPhaseConfiguration().getFeatureVectorWindowAlignment());//!contentShown);
                    contentShown = true;
                }
                updateWindowTitleFileNames(phaseController.showFeatureVectorData(featureVectorFile, progressMonitor));
                currentPhaseController.getPhaseConfiguration().setFeatureVectorFile(featureVectorFile);
            } catch (Exception ex) {
                handleException(new Exception("An error occured opening the feature vector file.", ex), true);
            }
        }
    }

    /**
     * @param featureVectorFile
     * @param phaseId
     */
    public void openFeatureVectorForValidation(FileLocation featureVectorFile, String phaseId) {
        checkExited();
        PhaseController<?, ?, ?, ?, ?, ?, ?, ?> phaseController = phaseControllers.get(phaseId);
        if (phaseController != null) {
            try {
                //Validate the feature vector file
                Exception e = null;
                try {
                    phaseController.openFeatureVectorFile(featureVectorFile, true, null);
                } catch (Exception e1) {
                    e = e1;
                }

                if (e == null) {
                    JOptionPane.showMessageDialog(applicationWindow.getWindowComponent(),
                            "The feature vector file was valid.",
                            "Feature Vector Valid",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    ValidationErrorsDlg.showErrorDialog(applicationWindow.getWindowComponent(),
                            "Feature Vector Not Valid",
                            "The feature vector file contained errors: ",
                            e.getMessage());
                }
            } catch (Exception e) {
                handleException(new Exception("Error loading feature vector file", e), true);
            }
        } else {
            handleException(new Exception("Phase " + phaseId + " is not supported by this version of the software."), false);
        }
    }

    /**
     * @param featureVectorType
     */
    public void clearFeatureVectorData(FileDescriptor featureVectorType) {
        checkExited();
        if (applicationState == ApplicationState.Showing_Feature_Vector_Data && currentPhaseController != null) {
            try {
                updateWindowTitleFileNames(currentPhaseController.clearFeatureVectorData(featureVectorType));
            } catch (Exception ex) {
                handleException(new Exception("An error occured clearing the feature vector data.", ex), true);
            }
        }
    }

    /**
     *
     */
    public void clearAllFeatureVectorData() {
        checkExited();
        if (applicationState == ApplicationState.Showing_Feature_Vector_Data && currentPhaseController != null) {
            try {
                currentPhaseController.clearAllFeatureVectorData();
                updateWindowTitleFileNames(null);
            } catch (Exception ex) {
                handleException(new Exception("An error occured clearing the feature vector data.", ex), true);
            }
        }
    }

    /**
     * @param files
     */
    protected void updateWindowTitleFiles(Collection<FileLocation> files) {
        StringBuilder sb = new StringBuilder(configuration.getApplicationName());
        if (files != null && !files.isEmpty()) {
            sb.append(" - ");
            int i = 0;
            for (FileLocation file : files) {
                sb.append(file.getFileName() != null ? file.getFileName()
                        : file.getFile() != null ? file.getFile().getName() : "");
                if (i < files.size() - 1) {
                    sb.append(", ");
                }
                i++;
            }
        }
        applicationWindow.setWindowTitle(sb.toString());
    }

    /**
     * @param fileNames
     */
    protected void updateWindowTitleFileNames(Collection<String> fileNames) {
        StringBuilder sb = new StringBuilder(configuration.getApplicationName());
        if (fileNames != null && !fileNames.isEmpty()) {
            sb.append(" - ");
            int i = 0;
            for (String fileName : fileNames) {
                sb.append(fileName);
                if (i < fileNames.size() - 1) {
                    sb.append(", ");
                }
                i++;
            }
        }
        applicationWindow.setWindowTitle(sb.toString());
    }

    public void changeExamParticipant(String subjectId, Site site) {
        checkExited();
        this.subjectId = subjectId;
        this.site = site;
        if (currentPhaseController != null) {
            currentPhaseController.changeExamParticipant(subjectId, site);
        }
    }

    public void restartExam() {
        checkExited();
        if (currentPhaseController != null) {
            currentPhaseController.restartExam();
        }
    }

    public void advanceToExamPhase(int phaseIndex) {
        checkExited();
        if (currentPhaseController != null) {
            currentPhaseController.advanceExamToPhase(phaseIndex);
        }
    }

    public void advanceToExamTrialInCurrentPhase(int trialNumber) {
        checkExited();
        if (currentPhaseController != null) {
            currentPhaseController.advanceExamToTrialInCurrentPhase(trialNumber);
        }
    }

    /**
     * Log into the client application.
     */
    private void login() {
        try {
            boolean credentialsEntered = true;
            if (loginDlg.getUserName() == null || loginDlg.getUserName().isEmpty()) {
                handleException(new Exception("Please enter a user name."), false);
                credentialsEntered = false;
            } else if (loginDlg.getPassword() == null || loginDlg.getPassword().length == 0) {
                handleException(new Exception("Please enter a password."), false);
                credentialsEntered = false;
            }
            if (credentialsEntered) {
                subjectName = loginDlg.getUserName();
                subjectRoles = dataSourceConnection.validateLogin(
                        subjectName, new String(loginDlg.getPassword()));
                if (subjectRoles == null || subjectRoles.isEmpty()) {
                    //The login was not valid
                    handleException(new Exception("Your login was not valid. Please re-type your user name and password"), false);
                } else {
					//System.out.println(subjectRoles + ", " + subjectName);
                    //The login was valid, go to the home screen
                    loginDlg.setVisible(false);
                    registrationDlg.setVisible(false);
                    //subjectId = Long.toString(dataSourceConnection.getSubject(subjectName).getId());
                    User subject = dataSourceConnection.getUser(subjectName);
                    if (subject != null) {
                        site = subject.getSite();
                        if (subject.getId() != null) {
                            subjectId = subject.getId().toString();
                        }
                    }
					//System.out.println("site ID: " + siteId);
                    //TODO: Display site in home dialog
                    homeDlg.setUser(subjectName, subjectId, false);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            returnHome();
                        }
                    });
                }
            }
        } catch (ClientException exception) {
            //An exception occured			
            handleException(exception, false);
            loginDlg.setVisible(true);
        }
    }

    /**
     * Log out of the client application.
     */
    private void logout() {
        boolean logout = true;
        if (configuration.isWarnIfDataLost() && isUnsavedData()) {
            //Warn if unsaved data
            String taskTypeName
                    = currentPhaseController != null ? currentPhaseController.getPhaseConfiguration().getTaskTypeName() : "Mission";
            logout = JOptionPane.showConfirmDialog(applicationWindow.getWindowComponent(),
                    "<html>The current " + taskTypeName + " is not complete, and you will have to restart it at the "
                    + "beginning if you log out.<br>Would you like to log out?</html>", "",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
        }
        if (logout) {
            //Logout
            if (currentPhaseController != null) {
                currentPhaseController.stopExam();
            }
            applicationWindow.clearContentPane();
            subjectId = null;
            subjectName = null;
            homeDlg.setVisible(false);
            registrationDlg.setVisible(false);
            loginDlg.resetFields();
            loginDlg.setLocationRelativeTo(applicationWindow.getWindowComponent());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    loginDlg.setVisible(true);
                }
            });
        }
    }

    /**
     * Create a new user account and login as that user.
     */
    private void createUser() {
        try {
            boolean credentialsEntered = true;
            if (registrationDlg.getSite() == null || registrationDlg.getSite().length() == 0) {
                handleException(new Exception("Please enter a valid site."), false);
                credentialsEntered = false;
            } else if (registrationDlg.getAuthenticationCode() == null || registrationDlg.getAuthenticationCode().length() == 0) {
                handleException(new Exception("Please enter an authentication code."), false);
                credentialsEntered = false;
            } else if (registrationDlg.getUserName() == null || registrationDlg.getUserName().isEmpty()) {
                handleException(new Exception("Please enter a user name."), false);
                credentialsEntered = false;
            } else if (registrationDlg.getPassword() == null || registrationDlg.getPassword().length == 0) {
                handleException(new Exception("Please enter a password."), false);
                credentialsEntered = false;
            } else if (!(new String(registrationDlg.getPassword()).equals(new String(registrationDlg.getRetypePassword())))) {
                handleException(new Exception("The passwords you entered do not match. Please re-type the passwords."), false);
                registrationDlg.resetPasswordFields();
                credentialsEntered = false;
            }
            if (credentialsEntered) {
                subjectName = registrationDlg.getUserName();
                subjectRoles = dataSourceConnection.createNewUser(subjectName, new String(registrationDlg.getPassword()), registrationDlg.getSite(),
                        new String(registrationDlg.getAuthenticationCode()));
                if (subjectRoles == null || subjectRoles.isEmpty()) {
                    //The user was not created
                    handleException(new Exception("Your user account could not be created, please try again."), false);
                } else {
                    //The user was created, go to the home screen
                    loginDlg.setVisible(false);
                    registrationDlg.setVisible(false);
                    User subject = dataSourceConnection.getUser(subjectName);
                    //subjectId = Long.toString(dataSourceConnection.getSubject(subjectName).getId());
                    if (subject != null) {
                        site = subject.getSite();
                        if (subject.getId() != null) {
                            subjectId = subject.getId().toString();
                        }
                    }
                    homeDlg.setUser(subjectName, subjectId, false);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            returnHome();
                        }
                    });
                }
            }
        } catch (ClientException exception) {
            //An exception occured
            handleException(exception, false);
        }
    }

    /**
     * Return to the home screen.
     */
    private void returnHome() {
        boolean returnHome = true;
        if (configuration.isWarnIfDataLost() && isUnsavedData()) {
            //Warn if unsaved data
            String taskTypeName
                    = currentPhaseController != null ? currentPhaseController.getPhaseConfiguration().getTaskTypeName() : "Mission";
            returnHome = JOptionPane.showConfirmDialog(applicationWindow.getWindowComponent(),
                    "<html>The current " + taskTypeName + " is not complete, and you will have to restart it at the "
                    + "beginning if you return home.<br>Would you like to return home?</html>", "",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
        }

        if (returnHome) {
            if (currentPhaseController != null) {
                currentPhaseController.stopExam();
            }
            applicationWindow.clearContentPane();

            //Update home screen options
            homeDlg.setOptionsText(createHomeOptions());

            //Show home screen
            homeDlg.setLocationRelativeTo(applicationWindow.getWindowComponent());
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    homeDlg.setVisible(true);;
                }
            });
        }
    }

    /**
     * Create the list of options to choose from on the home screen.
     *
     * @return
     */
    private String createHomeOptions() {
        StringBuilder options = new StringBuilder();
        //openDefaultExam(); //TODO: Implement method
        completedTasks = null;
        try {
            Experiment<?> exam = getCurrentExam();
            //System.out.println(subjectId + ", " + exam.getName());
            if (exam != null && exam instanceof IcarusExam<?>) {
                completedTasks = dataSourceConnection.getCompletedExamTasks(subjectId, ((IcarusExam<?>) exam).getName());
            }
        } catch (ClientException ex) {
            handleException(new Exception("Error, could not retrieve your experiment history", ex), false);
        }
        //System.out.println("Completed Task: " + completedTasks);
        if (completedTasks != null && !completedTasks.isEmpty()) {
            if (!isCurrentExamComplete(completedTasks)) {
                options.append("<a href=\"start_where_leftoff\">Start Current Experiment Where I Left Off</a><br>");
            }
            options.append("<a href=\"start_at_beginning\">Restart Current Experiment At Beginning</a><br>");
        } else {
            options.append("<a href=\"start_at_beginning\">Start Current Experiment</a><br>");
        }
        return options.toString();
    }

    /**
     * @return
     */
    private boolean isUnsavedData() {
        if (currentPhaseController != null && currentPhaseController.getActiveExamController() != null) {
            return currentPhaseController.getActiveExamController().isConditionRunning();
        }
        return false;
    }

    /**
     * @param completedTasks
     * @return
     */
    /*private Integer getCurrenTaskInCurrentExam(List<String> completedTasks) {
     Experiment<?> exam = getCurrentExam();
     if(exam != null && completedTasks != null && !completedTasks.isEmpty()) {			
     int taskIndex = -1;
     //System.out.println("Completed tasks: " + completedTasks);
     if(exam.getConditions() != null && !exam.getConditions().isEmpty() && !completedTasks.isEmpty()) {				
     for(String task : completedTasks) {
     int index = 0;
     int testIndex = -1;					
     for(Condition condition : exam.getConditions()) {						
     if(condition.getName().equalsIgnoreCase(task)) {
     testIndex = index;
     break;
     }
     index++;
     }
     if(testIndex > taskIndex) {
     taskIndex = testIndex;
     }
     }
     }
     if(taskIndex > -1) {
     return taskIndex+1;
     } else {
     return 0;
     }
     }
     return null;
     }*/
    /**
     * @param completedTasks
     * @return
     */
    private boolean isCurrentExamComplete(List<String> completedTasks) {
        Experiment<?> exam = getCurrentExam();
        if (exam != null && completedTasks != null) {
            if (exam.getConditions() != null) {
                for (Condition condition : exam.getConditions()) {
                    if (!completedTasks.contains(condition.getName())) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Shows a file chooser to open an exam. Returns null if no file was chosen.
     * 
     * @param lastExamFileChosen
     * @param examFileType
     * @param dialogTitle
     * @return
     */
    protected File chooseExamFile(FileLocation lastExamFileChosen,
            FileDescriptor examFileType, String dialogTitle) {
        if (examFileChooser == null) {
            examFileChooser = new JFileChooser(new File("."));
        }
        //examFileChooser.setDialogTitle("Open Exam");
        examFileChooser.setDialogTitle(dialogTitle);
        examFileChooser.setFileFilter(new FileNameExtensionFilter(examFileType.getFileTypeDescription(), examFileType.getExtenions()));
        if (lastExamFileChosen != null) {
            if (lastExamFileChosen.getFile() != null) {
                examFileChooser.setCurrentDirectory(lastExamFileChosen.getFile());
            } else if (lastExamFileChosen.getFileUrl() != null) {
                examFileChooser.setCurrentDirectory(new File(lastExamFileChosen.getFileUrl().getFile()));
            } else if (lastExamFileChosen.getFileLocation() != null) {
                examFileChooser.setCurrentDirectory(new File(lastExamFileChosen.getFileLocation()));
            }
        }
        //Show file open dialog		
        if (examFileChooser.showOpenDialog(applicationWindow.getWindowComponent()) == JFileChooser.APPROVE_OPTION) {
            return examFileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Shows a file chooser to open the exam playback files. Returns null if no
     * files were chosen.
     * 
     * @param lastExamPlaybackFilesChosen
     * @param defaultExamPlaybackFiles
     * @param examPlaybackFileTypes
     * @param dialogTitle
     * @return
     */
    protected List<File> chooseExamPlaybackFiles(List<FileLocation> lastExamPlaybackFilesChosen,
            List<FileLocation> defaultExamPlaybackFiles, List<FileDescriptor> examPlaybackFileTypes,
            String dialogTitle) {
        List<File> files = null;
        if (lastExamPlaybackFilesChosen != null && !lastExamPlaybackFilesChosen.isEmpty()) {
            files = new ArrayList<File>();
            for (FileLocation fileLocation : lastExamPlaybackFilesChosen) {
                files.add(fileLocation.getFile());
            }
        } else if (defaultExamPlaybackFiles != null && !defaultExamPlaybackFiles.isEmpty()) {
            files = new ArrayList<File>();
            for (FileLocation fileLocation : defaultExamPlaybackFiles) {
                files.add(fileLocation.getFile());
            }
        }
        return ExamPlaybackFileSelectionDlg.showDialog(applicationWindow.getWindowComponent(),
                dialogTitle, examPlaybackFileTypes, files);
    }

    /**
     * Shows a file chooser to open a feature vector file. Returns null if no
     * file was chosen.
     * 
     * @param lastFeatureVectorFileChosen
     * @param featureVectorFileType
     * @param dialogTitle
     * @return
     */
    protected File chooseFeatureVectorFile(FileLocation lastFeatureVectorFileChosen,
            FileDescriptor featureVectorFileType, String dialogTitle) {
        if (featureVectorFileChooser == null) {
            featureVectorFileChooser = new JFileChooser(new File("."));
        }
        featureVectorFileChooser.setDialogTitle(dialogTitle);
        featureVectorFileChooser.setFileFilter(new FileNameExtensionFilter(featureVectorFileType.getFileTypeDescription(),
                featureVectorFileType.getExtenions()));
        if (lastFeatureVectorFileChosen != null) {
            if (lastFeatureVectorFileChosen.getFile() != null) {
                featureVectorFileChooser.setCurrentDirectory(lastFeatureVectorFileChosen.getFile());
            } else if (lastFeatureVectorFileChosen.getFileUrl() != null) {
                featureVectorFileChooser.setCurrentDirectory(new File(lastFeatureVectorFileChosen.getFileUrl().getFile()));
            } else if (lastFeatureVectorFileChosen.getFileLocation() != null) {
                featureVectorFileChooser.setCurrentDirectory(new File(lastFeatureVectorFileChosen.getFileLocation()));
            }
        }
        //Show file open dialog		
        if (featureVectorFileChooser.showOpenDialog(applicationWindow.getWindowComponent()) == JFileChooser.APPROVE_OPTION) {
            return featureVectorFileChooser.getSelectedFile();
        }
        return null;
    }
    
    public void windowToFront() {
        checkExited();
        if(applicationWindow.getWindowComponent() instanceof Frame) {
            ((Frame)applicationWindow.getWindowComponent()).toFront();
        }
    }

    /**
     * Confirm whether to exit the application
     */
    protected void confirmExit() {
        if (JOptionPane.showConfirmDialog(applicationWindow.getWindowComponent(), "Would you like to exit the application?", "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            exit();
        }
    }
    
    protected void exit() {
        if (configuration.isApplet()) {
            try {
                if (currentPhaseController != null) {
                    currentPhaseController.stopExam();
                }                
                if (applicationWindow != null) {
                    applicationWindow.disposeWindow();
                    applicationWindow = null;
                }
            } catch (Exception e) {
            } finally {
                currentPhaseController = null;
                applicationState = ApplicationState.Exited;
            }
        } else {
            System.exit(0);
        }
    }
    
    private void checkExited() {
        if (applicationState == ApplicationState.Exited) {
            throw new IllegalArgumentException("The application has been exited");
        }
    }
    
    public boolean isExited() {
        return applicationState == ApplicationState.Exited;
    }

    /**
     * @param ex
     * @param showStackTrace
     */
    protected void handleException(Exception ex, boolean showStackTrace) {
        ErrorDlg.showErrorDialog(applicationWindow.getWindowComponent(), ex, showStackTrace);
    }

    /**
     * @param fileLocation
     * @return
     * @throws MalformedURLException
     */
    protected URL resolveFileLocation(FileLocation fileLocation) throws MalformedURLException {
        if (fileLocation != null) {
            if (fileLocation.getFileUrl() == null) {
                if (fileLocation.getFileLocation() != null) {
                    if (configuration.getApplicationType() == ApplicationType.ExperimentClientWebStart
                            || configuration.getApplicationType() == ApplicationType.PlayerOnlyWebStart
                            || configuration.getApplicationType() == ApplicationType.TrainingClientWebStart
                            || configuration.getApplicationType() == ApplicationType.TrainingClientApplet) {                        
                        fileLocation.setFileUrl(getClass().getClassLoader().getResource(fileLocation.getFileLocation()));
                    } else {
                        fileLocation.setFileUrl(new File(fileLocation.getFileLocation()).toURI().toURL());
                    }
                } else if (fileLocation.getFile() != null) {
                    fileLocation.setFileUrl(fileLocation.getFile().toURI().toURL());
                }
            }
            return fileLocation.getFileUrl();
        }
        return null;
    }

    protected Experiment<?> getCurrentExam() {
        IcarusExperimentController<?, ?, ?, ?> examController = currentPhaseController != null
                ? currentPhaseController.getActiveExamController() : null;
        return examController != null ? examController.getExam() : null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IcarusLookAndFeel.initializeICArUSLookAndFeel();
                new ApplicationController();
            }
        });
    }
}

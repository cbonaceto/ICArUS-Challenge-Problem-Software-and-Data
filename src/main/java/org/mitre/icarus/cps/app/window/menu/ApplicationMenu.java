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
package org.mitre.icarus.cps.app.window.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.dialog.AboutDlg;
import org.mitre.icarus.cps.app.window.IApplicationWindow;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.ExamOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorControlOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FeatureVectorOpenOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.FileOption;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration.Menu;
import org.mitre.icarus.cps.app.window.configuration.FileDescriptor;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfiguration;
import org.mitre.icarus.cps.app.window.controller.ApplicationController.ApplicationState;

/**
 * Manages the application menu (a JMenuBar).
 * 
 * @author CBONACETO
 *
 */
public class ApplicationMenu {

    /**
     * The menu bar containing the menus
     */
    protected JMenuBar menuBar;

    /**
     * The file menu
     */
    protected JMenu fileMenu;

    /**
     * The feature vector menu
     */
    protected JMenu featureVectorMenu;

    /**
     * The exam menu
     */
    protected JMenu examMenu;

    /**
     * The help menu
     */
    protected JMenu helpMenu;

    /**
     * Exam control menu items
     */
    protected EnumMap<ExamControlOption, JMenuItem> examControlItems;

    /**
     * Clear feature vector data menu items for each phase (maps phase ID to
     * menu items list)
     */
    protected Map<String, List<JMenuItem>> featureVectorControlItems;

    /**
     * Help "About" menu item
     */
    protected JMenuItem aboutItem;

    /**
     * The current phase
     */
    protected String currentPhaseId;

    /**
     * Listeners registered to receive menu events
     */
    protected final List<ApplicationMenuListener> menuListeners;

    public ApplicationMenu(final ApplicationConfiguration configuration,
            final Collection<PhaseConfiguration<?, ?, ?, ?, ?>> phaseConfigurations,
            final IApplicationWindow window, boolean currentExamSpecified) {
        menuBar = new JMenuBar();
        menuListeners = Collections.synchronizedList(new LinkedList<ApplicationMenuListener>());

        //Create the file menu if enabled
        if (configuration.isMenuEnabled(Menu.File)) {
            fileMenu = new JMenu(configuration.getMenuName(Menu.File));
            fileMenu.setMnemonic(KeyEvent.VK_F);
            menuBar.add(fileMenu);
            boolean separatorNeeded = false;

            //Create file menu item to open current exam for data collection
            //TODO: Fix this
            if (currentExamSpecified) {
                if (configuration.isFileOptionEnabled(FileOption.Open_Current_Exam_For_Data_Collection)) {
                    JMenuItem openCurrentExamItem = new JMenuItem("Open Current Exam for Data Collection");
                    fileMenu.add(openCurrentExamItem);
                    openCurrentExamItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fireFileOptionSelected(FileOption.Open_Current_Exam_For_Data_Collection);
                        }
                    });
                    separatorNeeded = true;
                }
                if (configuration.isFileOptionEnabled(FileOption.Open_Current_Exam_For_Demonstration)) {
                    JMenuItem openCurrentExamItem = new JMenuItem("Open Current Exam for Demonstration");
                    fileMenu.add(openCurrentExamItem);
                    openCurrentExamItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fireFileOptionSelected(FileOption.Open_Current_Exam_For_Demonstration);
                        }
                    });
                    separatorNeeded = true;
                }
                if (configuration.isFileOptionEnabled(FileOption.Open_Current_Exam_For_Training)) {
                    JMenuItem openCurrentExamItem = new JMenuItem("Open Current Exam for Training");
                    fileMenu.add(openCurrentExamItem);
                    openCurrentExamItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fireFileOptionSelected(FileOption.Open_Current_Exam_For_Training);
                        }
                    });
                    separatorNeeded = true;
                }
            }

            //Create file open items for each phase (the phase sub-menus)
            if (configuration.isFileOptionEnabled(FileOption.Phase_Menu) && phaseConfigurations != null && !phaseConfigurations.isEmpty()) {
                if (separatorNeeded) {
                    fileMenu.addSeparator();
                }
                for (PhaseConfiguration<?, ?, ?, ?, ?> phaseConfiguration : phaseConfigurations) {                    
                    if ((phaseConfiguration.getExamOpenOptions() != null && !phaseConfiguration.getExamOpenOptions().isEmpty())
                            || (phaseConfiguration.getFeatureVectorOpenOptions() != null && !phaseConfiguration.getFeatureVectorOpenOptions().isEmpty())) {
                        final String phaseId = phaseConfiguration.getPhaseId();
                        JMenu phaseMenu = new JMenu("Phase " + phaseConfiguration.getPhaseName());
                        boolean phaseMenuSeparatorNeeded = false;

                        //Create exam open items for the phase
                        if (phaseConfiguration.getExamOpenOptions() != null && !phaseConfiguration.getExamOpenOptions().isEmpty()) {
                            for (ExamOpenOption examOpenOption : phaseConfiguration.getExamOpenOptions()) {
                                if (configuration.isExamOpenOptionEnabled(examOpenOption)) {
                                    phaseMenuSeparatorNeeded = true;
                                    JMenuItem examOpenItem = new JMenuItem();
                                    phaseMenu.add(examOpenItem);
                                    switch (examOpenOption) {
                                        case Open_For_Data_Collection:
                                            examOpenItem.setText("Open Exam for Data Collection");
                                            break;
                                        case Open_For_Demonstration:
                                            examOpenItem.setText("Open Exam for Demonstration");
                                            break;
                                        case Open_For_Training:
                                            examOpenItem.setText("Open Exam for Training");
                                            break;
                                        case Open_For_Playback:
                                            examOpenItem.setText("Open Exam Response for Playback");
                                            break;
                                        case Open_For_Validation:
                                            examOpenItem.setText("Validate Exam File");
                                            break;
                                        default:
                                            break;
                                    }
                                    final ExamOpenOption examOpenOptionFinal = examOpenOption;
                                    examOpenItem.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            fireExamOpenOptionSelected(examOpenOptionFinal, phaseId);
                                        }
                                    });
                                }
                            }
                        }

                        //Create feature vector open items for the phase
                        if (phaseConfiguration.getFeatureVectorOpenOptions() != null && !phaseConfiguration.getFeatureVectorOpenOptions().isEmpty()
                                && phaseConfiguration.getFeatureVectorFileTypes() != null && !phaseConfiguration.getFeatureVectorFileTypes().isEmpty()) {
                            for (final FeatureVectorOpenOption featureVectorOpenOption : phaseConfiguration.getFeatureVectorOpenOptions()) {
                                for (final FileDescriptor featureVectorFileType : phaseConfiguration.getFeatureVectorFileTypes()) {
                                    if (configuration.isFeatureVectorOpenOptionEnabled(featureVectorOpenOption)) {
                                        if (phaseMenuSeparatorNeeded) {
                                            phaseMenu.addSeparator();
                                            phaseMenuSeparatorNeeded = false;
                                        }
                                        JMenuItem featureVectorOpenItem = new JMenuItem();
                                        phaseMenu.add(featureVectorOpenItem);
                                        switch (featureVectorOpenOption) {
                                            case Open_For_Display:
                                                featureVectorOpenItem.setText("Open " + featureVectorFileType.getFileTypeTitle() + " File");
                                                break;
                                            case Open_For_Validation:
                                                featureVectorOpenItem.setText("Validate " + featureVectorFileType.getFileTypeTitle() + " File");
                                                break;
                                            default:
                                                break;
                                        }
                                        featureVectorOpenItem.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                fireFeatureVectorOpenOptionSelected(featureVectorOpenOption, featureVectorFileType, phaseId);
                                            }
                                        });
                                    }
                                }
                            }
                        }

                        if (phaseMenu.getItemCount() > 0) {
                            separatorNeeded = true;
                            fileMenu.add(phaseMenu);
                        }
                    }
                }
            }

            //Create logout menu item
            if (configuration.isFileOptionEnabled(FileOption.Logout)) {
                if (separatorNeeded) {
                    fileMenu.addSeparator();
                    separatorNeeded = false;
                }
                JMenuItem logoutItem = new JMenuItem("Log Out");
                fileMenu.add(logoutItem);
                logoutItem.setMnemonic(KeyEvent.VK_L);
                logoutItem.setIcon(ImageManager.getImageIcon(ImageManager.LOGOUT_ICON));
                logoutItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fireFileOptionSelected(FileOption.Logout);
                    }
                });
            }

            //Create exit menu item
            if (configuration.isFileOptionEnabled(FileOption.Exit)) {
                if (separatorNeeded) {
                    fileMenu.addSeparator();
                    separatorNeeded = false;
                }
                JMenuItem exitItem = new JMenuItem("Exit");
                fileMenu.add(exitItem);
                exitItem.setMnemonic(KeyEvent.VK_X);
                exitItem.setIcon(ImageManager.getImageIcon(ImageManager.EXIT_ICON));
                exitItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fireFileOptionSelected(FileOption.Exit);
                    }
                });
            }
        }

        //Create the exam control menu if enabled		
        if (configuration.isMenuEnabled(Menu.ExamControlOptions)) {
            examMenu = new JMenu(configuration.getMenuName(Menu.ExamControlOptions));
            examMenu.setEnabled(false);
            examMenu.setMnemonic(KeyEvent.VK_E);
            menuBar.add(examMenu);
            examControlItems = new EnumMap<ExamControlOption, JMenuItem>(ExamControlOption.class);
            //Create the exam control menu items
            if (!configuration.getExamControlOptions().isEmpty()) {
                for (ExamControlOption examOption : configuration.getExamControlOptions()) {
                    JMenuItem examControlItem = new JMenuItem();
                    examControlItems.put(examOption, examControlItem);
                    switch (examOption) {
                        case Advance_To_Phase:
                            examControlItem.setText("Advance to Mission");
                            break;
                        case Advance_To_Trial:
                            examControlItem.setText("Advance to Trial");
                            break;
                        case Change_Particpant:
                            examControlItem.setText("Change Participant ID");
                            break;
                        case Restart_Exam:
                            examControlItem.setText("Restart Exam");
                            break;
                    }
                    final ExamControlOption examOptionFinal = examOption;
                    examControlItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fireExamControlOptionSelected(examOptionFinal);
                        }
                    });
                }
            }
        }

        //Create the feature vector control menu if enabled
        if (configuration.isMenuEnabled(Menu.FeatureVectorControlOptions)) {
            featureVectorMenu = new JMenu(configuration.getMenuName(Menu.FeatureVectorControlOptions));
            featureVectorMenu.setEnabled(false);
            featureVectorMenu.setMnemonic(KeyEvent.VK_V);
            menuBar.add(featureVectorMenu);
            featureVectorControlItems = new HashMap<String, List<JMenuItem>>();
            //Create the feature vector control menu items for each phase
            if (!configuration.getFeatureVectorControlOptions().isEmpty() && phaseConfigurations != null && !phaseConfigurations.isEmpty()) {
                for (PhaseConfiguration<?, ?, ?, ?, ?> phaseConfiguration : phaseConfigurations) {
                    List<JMenuItem> featureVectorItems = new LinkedList<JMenuItem>();
                    featureVectorControlItems.put(phaseConfiguration.getPhaseId(), featureVectorItems);
                    if (configuration.isFeatureVectorControlOptionEnabled(FeatureVectorControlOption.Clear_File_Data)
                            && phaseConfiguration.getFeatureVectorFileTypes() != null && !phaseConfiguration.getFeatureVectorFileTypes().isEmpty()) {
                        //Create clear feature vector menu items
                        for (final FileDescriptor featureVectorFileType : phaseConfiguration.getFeatureVectorFileTypes()) {
                            JMenuItem featureVectorControlItem = new JMenuItem("Clear " + featureVectorFileType.getFileTypeTitle() + " Data");
                            featureVectorItems.add(featureVectorControlItem);
                            featureVectorControlItem.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    fireFeatureVectorControlOptionSelected(FeatureVectorControlOption.Clear_File_Data,
                                            featureVectorFileType);
                                }
                            });
                        }
                    }
                    if (configuration.isFeatureVectorControlOptionEnabled(FeatureVectorControlOption.Clear_All_Data)) {
                        //Create clear all feature vector data menu item	
                        JMenuItem featureVectorControlItem = new JMenuItem("Clear All Feature Vector Data");
                        featureVectorItems.add(featureVectorControlItem);
                        featureVectorControlItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                fireFeatureVectorControlOptionSelected(FeatureVectorControlOption.Clear_All_Data, null);
                            }
                        });
                    }
                }
            }
        }

        //Create the help menu with the about item if enabled
        if (configuration.isMenuEnabled(Menu.Help)) {
            helpMenu = new JMenu(configuration.getMenuName(Menu.Help));
            menuBar.add(helpMenu);
            helpMenu.setMnemonic(KeyEvent.VK_H);
            aboutItem = new JMenuItem("About " + configuration.getApplicationName());
            aboutItem.setMnemonic(KeyEvent.VK_A);
            //aboutMenuItem.setIcon(ImageManager.getImageIcon(ImageManager.ICARUS_LOGO));
            helpMenu.add(aboutItem);
            aboutItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Show the About dialog
                    AboutDlg.showDefaultAboutDlg(window.getWindowComponent(), configuration.isFouo(),
                            configuration.isPublicReleased(), configuration.getApplicationName(),
                            configuration.getApplicationVersion());
                }
            });
        }
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * @param listener
     */
    public void addMenuListener(ApplicationMenuListener listener) {
        if (!menuListeners.contains(listener)) {
            menuListeners.add(listener);
        }
    }

    /**
     * @param listener
     */
    public void removeMenuListener(ApplicationMenuListener listener) {
        menuListeners.remove(listener);
    }

    protected void fireFileOptionSelected(FileOption fileOption) {
        synchronized (menuListeners) {
            if (!menuListeners.isEmpty()) {
                for (ApplicationMenuListener listener : menuListeners) {
                    listener.fileOptionSelected(fileOption);
                }
            }
        }
    }

    protected void fireExamOpenOptionSelected(ExamOpenOption openOption, String phaseId) {
        synchronized (menuListeners) {
            if (!menuListeners.isEmpty()) {
                for (ApplicationMenuListener listener : menuListeners) {
                    listener.examOpenOptionSelected(openOption, phaseId);
                }
            }
        }
    }

    protected void fireFeatureVectorOpenOptionSelected(FeatureVectorOpenOption openOption, FileDescriptor featureVectorFileType,
            String phaseId) {
        synchronized (menuListeners) {
            if (!menuListeners.isEmpty()) {
                for (ApplicationMenuListener listener : menuListeners) {
                    listener.featureVectorOpenOptionSelected(openOption, featureVectorFileType, phaseId);
                }
            }
        }
    }

    protected void fireExamControlOptionSelected(ExamControlOption controlOption) {
        synchronized (menuListeners) {
            if (!menuListeners.isEmpty()) {
                for (ApplicationMenuListener listener : menuListeners) {
                    listener.examControlOptionSelected(controlOption);
                }
            }
        }
    }

    protected void fireFeatureVectorControlOptionSelected(FeatureVectorControlOption controlOption,
            FileDescriptor featureVectorFileType) {
        synchronized (menuListeners) {
            if (!menuListeners.isEmpty()) {
                for (ApplicationMenuListener listener : menuListeners) {
                    listener.featureVectorControlOptionSelected(controlOption, featureVectorFileType);
                }
            }
        }
    }

    /**
     * Set which menus and menu items are enabled based on the application
     * state.
     *
     * @param currentPhase
     * @param applicationState
     */
    public void configureMenus(PhaseConfiguration<?, ?, ?, ?, ?> currentPhase, ApplicationState applicationState) {
        if (currentPhaseId == null || !currentPhaseId.equals(currentPhase.getPhaseId())) {
            currentPhaseId = currentPhase.getPhaseId();

            //Configure the feature vector control menu if enabled the phase
            if (featureVectorMenu != null) {
                featureVectorMenu.removeAll();
                boolean separatorNeeded = false;
                //Add the custom feature vector menu items
                if (currentPhase.getCustomFeatureVectorMenuItems() != null
                        && currentPhase.getCustomFeatureVectorMenuItems().getItemCount() > 0) {
                    separatorNeeded = true;
                    for (int i = 0; i < currentPhase.getCustomFeatureVectorMenuItems().getItemCount(); i++) {
                        featureVectorMenu.add(currentPhase.getCustomFeatureVectorMenuItems().getItem(i));
                    }
                    //featureVectorMenu.add(currentPhase.getCustomFeatureVectorMenuItems());
                }
                //Add the standard feature vector control items
                List<JMenuItem> items = featureVectorControlItems.get(currentPhaseId);
                if (items != null && !items.isEmpty()) {
                    if (separatorNeeded) {
                        featureVectorMenu.addSeparator();
                    }
                    for (JMenuItem item : items) {
                        featureVectorMenu.add(item);
                    }
                }
            }

            //Configure the help menu if enabled for the application
            if (helpMenu != null) {
                helpMenu.removeAll();
                //Add the custom help menu items
                if (currentPhase.getCustomHelpMenuItems() != null
                        && currentPhase.getCustomHelpMenuItems().getItemCount() > 0) {
                    for (int i = 0; i < currentPhase.getCustomHelpMenuItems().getItemCount(); i++) {
                        helpMenu.add(currentPhase.getCustomHelpMenuItems().getItem(i));
                    }
                    //helpMenu.add(currentPhase.getCustomHelpMenuItems());
                    helpMenu.addSeparator();
                }
                //Add the about menu item
                helpMenu.add(aboutItem);
            }
        }

        //Configure the exam menu if enabled for the phase and set which menus are enabled/shown based on the application state
        switch (applicationState) {
            case Playing_Back_Exam_Response:
                if (examMenu != null) {
                    examMenu.removeAll();
                    if (currentPhase.getExamPlaybackControlOptions() != null && !currentPhase.getExamPlaybackControlOptions().isEmpty()) {
                        for (ExamControlOption examControlOption : currentPhase.getExamPlaybackControlOptions()) {
                            JMenuItem item = examControlItems.get(examControlOption);
                            if (item != null) {
                                examMenu.add(item);
                            }
                        }
                    }
                    examMenu.setEnabled(examMenu.getItemCount() > 0);
                }
                if (featureVectorMenu != null) {
                    featureVectorMenu.setEnabled(false);
                }
                break;
            case Showing_Exam_For_Data_Collection:
            case Showing_Exam_For_Demonstration:
            case Showing_Exam_For_Training:    
                if (examMenu != null) {
                    examMenu.removeAll();
                    EnumSet<ExamControlOption> examControlOptions = applicationState == ApplicationState.Showing_Exam_For_Data_Collection
                            ? currentPhase.getExamDataCollectionControlOptions() : currentPhase.getExamDemonstrationControlOptions();
                    if (examControlOptions != null && !examControlOptions.isEmpty()) {
                        for (ExamControlOption examControlOption : examControlOptions) {
                            JMenuItem item = examControlItems.get(examControlOption);
                            if (item != null) {
                                examMenu.add(item);
                            }
                        }
                    }
                    examMenu.setEnabled(examMenu.getItemCount() > 0);
                }
                if (featureVectorMenu != null) {
                    featureVectorMenu.setEnabled(false);
                }
                break;
            case Showing_Feature_Vector_Data:
                if (examMenu != null) {
                    examMenu.setEnabled(false);
                }
                if (featureVectorMenu != null) {
                    featureVectorMenu.setEnabled(featureVectorMenu.getItemCount() > 0);
                }
            case Blank:
                break;
        }
    }
}

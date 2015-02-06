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
package org.mitre.icarus.cps.app.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.Calendar;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.util.CPSUtils;
import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner;
import org.mitre.icarus.cps.app.widgets.security_banner.JSecurityBanner.Classification;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration;
import org.mitre.icarus.cps.app.window.configuration.PhaseConfiguration;
import org.mitre.icarus.cps.app.window.menu.ApplicationMenu;

/**
 * The main application window.
 *
 * @author CBONACETO
 *
 */
public class ApplicationWindow implements IApplicationWindow {

    /**
     * The application configuration
     */
    private final ApplicationConfiguration configuration;

    /**
     * The application frame
     */
    protected JFrame frame;

    /**
     * The application menu
     */
    protected ApplicationMenu menu;

    /**
     * Contains all panels (aside from the security banner panel)
     */
    protected JPanel contentPanel;

    /**
     * The current content panel component
     */
    protected JComponent contentPanelComponent;

    //The login screen
    //protected LoginDialog loginDlg;
    
    //Dialog to create a new subject/user account
    //protected RegistrationDialog registrationDlg;
    
    // The home screen
    //protected HomeDialog homeDlg;
    
    private boolean disposed = false;
    
    /**
     * Constructor that takes an application configuration.
     *
     * @param configuration
     * @param phaseConfigurations
     * @param currentExamSpecified
     */
    public ApplicationWindow(final ApplicationConfiguration configuration,
            final Collection<PhaseConfiguration<?, ?, ?, ?, ?>> phaseConfigurations,
            final boolean currentExamSpecified) {
        this.configuration = configuration;       

	//Create the GUI
        //if (configuration.isApplet()) {
        //    initializeApplet(phaseConfigurations, currentExamSpecified);
        //} else {
        initializeFrame(phaseConfigurations, currentExamSpecified);
        //}
    }

    @Override
    public boolean isVisible() {
        return frame != null && frame.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        if (frame != null) {
            frame.setVisible(visible);
        }
    }

    @Override
    public void setWindowTitle(String title) {
        if (frame != null) {
            frame.setTitle(title);
        }
    }

    @Override
    public void clearContentPane() {        
        contentPanel.removeAll();
        contentPanel.validate();
        contentPanel.repaint();
    }

    @Override
    public JComponent getContentPaneComponent() {
        return contentPanelComponent;
    }

    @Override
    public void setContentPaneComponent(JComponent contentPanelComponent, boolean sizeWindowToFitContent,
            WindowAlignment windowAlignment) { //boolean centerFrameOnScreen) {        
        if (this.contentPanelComponent != contentPanelComponent) {
            //this.contentPanelComponent = contentPanelComponent;
            contentPanel.removeAll();
            contentPanel.add(contentPanelComponent);
            contentPanel.validate();
            contentPanel.repaint();

            if (frame != null) {
                if (sizeWindowToFitContent) {
                    //Size the frame to fit the new content panel
                    frame.setMinimumSize(null);
                    int oldWidth = frame.getSize().width;
                    int oldHeight = frame.getSize().height;
                    frame.pack();
                    //Ensure the frame still fits on the screen
                    if (resizeFrameIfLargerThanScreen() || frame.getSize().width != oldWidth
                            || frame.getSize().height != oldHeight) {
						//Re-center the frame if it wasn't resized to fit the screen but
                        //was resized to fit the contents
                        if (windowAlignment != WindowAlignment.NONE) {
                            CPSUtils.alignFrameOnScreen(frame, windowAlignment);
                        }
                        //CPSUtils.centerFrameOnScreen(frame);
                    }
                    frame.setMinimumSize(frame.getSize());
                } else {
                    //Just be sure the frame is smaller than the screen
                    if (resizeFrameIfLargerThanScreen()) {
                        if (windowAlignment != WindowAlignment.NONE) {
                            CPSUtils.alignFrameOnScreen(frame, windowAlignment);
                        }
                        //CPSUtils.centerFrameOnScreen(frame);
                    }
                }
            }
			//frame.setSize(new Dimension(1234, 850)); //TODO: comment this line
            //System.out.println("Window size: " + frame.getSize());
        }
    }

    protected boolean resizeFrameIfLargerThanScreen() {
        if (frame != null) {
            int width = frame.getSize().width;
            int height = frame.getSize().height;
            Dimension screenSize = frame.getToolkit().getScreenSize();
            if (width > screenSize.width) {
                width = screenSize.width;
            }
            if (height > screenSize.height - 60) {
                height = screenSize.height - 60;
            }
            if (frame.getSize().width > width || frame.getSize().height > height) {
                frame.setSize(new Dimension(width, height));
                frame.setMinimumSize(frame.getSize());
                frame.validate();
                /*if(centerFrameOnScreen) {
                 CPSUtils.centerFrameOnScreen(frame);
                 }*/
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Component getWindowComponent() {
        checkDisposed();
        //return applicationType == ApplicationType.Application ? frame : getContentPane();
        return frame;
    }

    @Override
    public ApplicationMenu getMenu() {
        return menu;
    }
    
    @Override
    public void disposeWindow() {
        if(!disposed) {
            frame.dispose();
            frame = null;
            disposed = true;
        }
    }
    
    protected void checkDisposed() {
        if (disposed) {
            throw new IllegalArgumentException("The window has been disposed.");
        }
    }

    private void initializeFrame(
            Collection<PhaseConfiguration<?, ?, ?, ?, ?>> phaseConfigurations,
            boolean currentExamSpecified) {
        frame = new JFrame(configuration.getApplicationName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = ImageManager.getImage(ImageManager.ICARUS_LOGO);
        if (icon != null) {
            frame.setIconImage(icon);
        }

        frame.setResizable(true);
        frame.setVisible(false);
        frame.getContentPane().removeAll();

        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;

        //Add the content panel
        contentPanel = new JPanel(new BorderLayout());
        constraints.fill = GridBagConstraints.BOTH;
        frame.getContentPane().add(contentPanel, constraints);

        if (configuration.isShowSecurityBanner()) {
            //Add the security banner panel
            constraints.gridy++;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weighty = 0;
            frame.getContentPane().add(WidgetConstants.createDefaultSeparator(), constraints);
            constraints.gridy++;
            String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            frame.getContentPane().add(new JSecurityBanner(JSecurityBanner.COPYRIGHT_CHAR
                    + " " + year + " The MITRE Corporation. All Rights Reserved. ",
                    configuration.isFouo() ? Classification.FOUO
                    : configuration.isPublicReleased() ? Classification.None : Classification.Internal), constraints);
        }

        //Create the application menu
        menu = new ApplicationMenu(configuration, phaseConfigurations, this, currentExamSpecified);
        frame.setJMenuBar(menu.getMenuBar());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);        
    }
    
    /*private void initializeApplet(Container contentPane,
            Collection<PhaseConfiguration<?, ?, ?, ?, ?>> phaseConfigurations,
            boolean currentExamSpecified) {        
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;

        //Add the content panel
        contentPanel = new JPanel(new BorderLayout());
        constraints.fill = GridBagConstraints.BOTH;
        getContentPane().add(contentPanel, constraints);

        if (configuration.isShowSecurityBanner()) {
            //Add the security banner panel
            constraints.gridy++;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weighty = 0;
            getContentPane().add(WidgetConstants.createDefaultSeparator(), constraints);
            constraints.gridy++;
            String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            getContentPane().add(new JSecurityBanner(JSecurityBanner.COPYRIGHT_CHAR
                    + " " + year + " The MITRE Corporation. All Rights Reserved. ",
                    configuration.isFouo() ? Classification.FOUO
                    : configuration.isPublicReleased() ? Classification.None : Classification.Internal), constraints);
        }

        //Create the application menu
        menu = new ApplicationMenu(configuration, phaseConfigurations, this, currentExamSpecified);
        
        frame.setJMenuBar(menu.getMenuBar());
        
        setSize(contentPanel.getPreferredSize());
        setPreferredSize(contentPanel.getPreferredSize());
        validate();
        repaint();
    }

    @Override
    public void init() {
        initializeApplet(phaseConfigurations, currentExamSpecified);
    }*/
}

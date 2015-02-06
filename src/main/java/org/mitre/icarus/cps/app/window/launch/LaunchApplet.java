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
package org.mitre.icarus.cps.app.window.launch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.mitre.icarus.cps.app.window.IcarusLookAndFeel;
import org.mitre.icarus.cps.app.window.configuration.ApplicationConfiguration;
import org.mitre.icarus.cps.app.window.controller.ApplicationController;

/**
 *
 * @author CBONACETO
 */
public class LaunchApplet extends JApplet {
    
    /** The application controller instance */
    protected ApplicationController appController;
    
    /** Button to launch the application */
    protected JButton launchButton;

    @Override
    public void init() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IcarusLookAndFeel.initializeICArUSLookAndFeel();
                JPanel appletPanel = new JPanel(new GridBagLayout());
                appletPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                launchButton = new JButton("Open Training Suite");
                launchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (appController == null || appController.isExited()) {
                            appController = new ApplicationController(ApplicationConfiguration.createInstance(
                                    ApplicationConfiguration.ApplicationType.TrainingClientApplet, ApplicationController.VERSION,
                                    new String[]{"1", "2"}, false, false));
                            
                        } else {
                            appController.windowToFront();
                        }
                    }                    
                });
                appletPanel.add(launchButton);
                Dimension size = appletPanel.getPreferredSize();                
                appletPanel.setPreferredSize(
                        new Dimension(size.width + 20, size.height + 20));
                getContentPane().add(appletPanel);
                setSize(appletPanel.getPreferredSize());
		setPreferredSize(appletPanel.getPreferredSize());		
		validate();
		repaint();
            }
        });
    }
}

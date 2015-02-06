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

import java.awt.Component;

import javax.swing.JComponent;

import org.mitre.icarus.cps.app.window.menu.ApplicationMenu;

public interface IApplicationWindow {
	
	/** Window alignment constants (when aligning the window in the monitor) */
	public static enum WindowAlignment {LEFT, CENTER, RIGHT, NONE}
	
	public boolean isVisible();
	
	public void setVisible(boolean visible);
	
	public void setWindowTitle(String title);
	
	public void clearContentPane();
	
	public JComponent getContentPaneComponent();
	public void setContentPaneComponent(JComponent contentPane, boolean sizeWindowToFitContent, 
			WindowAlignment windowAlignment); //boolean centerFrameOnScreen);

	public Component getWindowComponent();
	
	public ApplicationMenu getMenu();
        
        public void disposeWindow();
}
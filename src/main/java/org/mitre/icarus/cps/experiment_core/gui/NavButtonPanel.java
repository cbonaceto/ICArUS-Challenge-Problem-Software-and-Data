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
package org.mitre.icarus.cps.experiment_core.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionListener;

/**
 * Default nav button panel implementation.
 * 
 * @author CBONACETO
 *
 */
public class NavButtonPanel extends JPanel implements INavButtonPanel, ActionListener {
	private static final long serialVersionUID = 1L;

	/** Default orientation for the Help button area */
	public static final int HELP_BUTTON_AREA_ORIENTATION = SwingConstants.RIGHT; //previously SwingConstants.RIGHT
	
	/** Default orientation for the Back/Next button area */
	public static final int BACK_NEXT_BUTTON_AREA_ORIENTATION = SwingConstants.CENTER; //previously SwingConstants.CENTER
	
	/** The exit button*/
	protected JButton exitButton;
	
	/** Panel containing exit button */
	protected JPanel exitButtonPanel;

	/** The next button */
	protected JButton nextButton;
	
	/** The back button */
	protected JButton backButton;

	/** Panel containing back and next buttons */
	protected JPanel backNextPanel;
	
	/** The help button */
	protected JButton helpButton;
	
	/** Panel containing help button */
	protected JPanel helpButtonPanel;
	
	/** A label displayed in the navigation panel */
	protected JLabel messageLabel;
	protected String messageLabelSizingString = " ";
	protected JPanel messagePanel;
	
	/** Default next button icon */
	protected static final ImageIcon nextIcon = ImageManager.getImageIcon(ImageManager.NEXT_ICON);
	protected static final ImageIcon nextIconDisabled = ImageManager.getImageIcon(ImageManager.NEXT_DISABLED_ICON);
	
	/** Default back button icon */	
	protected static final ImageIcon backIcon = ImageManager.getImageIcon(ImageManager.BACK_ICON);
	protected static final ImageIcon backIconDisabled = ImageManager.getImageIcon(ImageManager.BACK_DISABLED_ICON);
	
	/** Default help button icon */
	protected static final ImageIcon helpIcon = ImageManager.getImageIcon(ImageManager.HELP_ICON);	
	
	/** Listeners registered to be notified when a button is pressed */
	private List<SubjectActionListener> buttonPressListeners = 
		Collections.synchronizedList(new LinkedList<SubjectActionListener>());
	
	/**
	 * 
	 */
	public NavButtonPanel() {
		this(false, true, true, false);
	}
	
	/**
	 * @param exitButtonVisible
	 * @param backButtonVisible
	 * @param nextButtonVisible
	 * @param helpButtonVisible
	 */
	public NavButtonPanel(boolean exitButtonVisible, boolean backButtonVisible, 
			boolean nextButtonVisible, boolean helpButtonVisible) {
		this(exitButtonVisible, backButtonVisible, nextButtonVisible, helpButtonVisible, 
				HELP_BUTTON_AREA_ORIENTATION, BACK_NEXT_BUTTON_AREA_ORIENTATION, 
				false, SwingConstants.LEFT);
	}
	
	/**
	 * @param exitButtonVisible
	 * @param backButtonVisible
	 * @param nextButtonVisible
	 * @param helpButtonVisible
	 * @param helpButtonAreaOrientation
	 * @param backNextButtonAreaOrientation
	 * @param messageLabelVisible
	 * @param messageLabelOrientation
	 */
	public NavButtonPanel(boolean exitButtonVisible, boolean backButtonVisible, 
			boolean nextButtonVisible, boolean helpButtonVisible, 
			int helpButtonAreaOrientation, int backNextButtonAreaOrientation,
			boolean messageLabelVisible, int messageLabelOrientation) {
		//Possibly use a GridBag layout instead of a Grid Layout
		super(new GridLayout(1, 3));
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));		
		
		if(helpButtonAreaOrientation != SwingConstants.LEFT && 
				helpButtonAreaOrientation != SwingConstants.RIGHT && 
				helpButtonAreaOrientation != SwingConstants.CENTER) {
			helpButtonAreaOrientation = SwingConstants.RIGHT;
		}
		if(backNextButtonAreaOrientation == helpButtonAreaOrientation ||
				(backNextButtonAreaOrientation != SwingConstants.LEFT &&
				backNextButtonAreaOrientation != SwingConstants.RIGHT &&
				backNextButtonAreaOrientation != SwingConstants.CENTER)) {
			backNextButtonAreaOrientation = SwingConstants.CENTER;
		}
		if(messageLabelOrientation != SwingConstants.LEFT && 
				messageLabelOrientation != SwingConstants.RIGHT) {
			messageLabelOrientation = SwingConstants.RIGHT;
		}
		HashSet<Integer> orientations = new HashSet<Integer>(Arrays.asList(
				SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.RIGHT));
		orientations.remove(helpButtonAreaOrientation);
		orientations.remove(backNextButtonAreaOrientation);
		int exitButtonAreaOrientation = orientations.iterator().next();
		
		messagePanel = new JPanel();
		messagePanel.add(messageLabel = new JLabel());
		messageLabel.setText(" ");
		
		exitButtonPanel = new JPanel();
		if(messageLabelOrientation == exitButtonAreaOrientation &&
				messageLabelOrientation == SwingConstants.RIGHT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			exitButtonPanel.add(messagePanel);
		}
		exitButton = new JButton("Help");
		messageLabel.setFont(exitButton.getFont());
		exitButton.setMargin(WidgetConstants.INSETS_CONTROL);
		exitButton.addActionListener(this);
		exitButtonPanel.add(exitButton);
		exitButton.setVisible(exitButtonVisible);
		if(messageLabelOrientation == exitButtonAreaOrientation &&
				messageLabelOrientation == SwingConstants.LEFT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			exitButtonPanel.add(messagePanel);
		}
		
		backNextPanel = new JPanel();
		if(messageLabelOrientation == backNextButtonAreaOrientation &&
				messageLabelOrientation == SwingConstants.RIGHT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			backNextPanel.add(messagePanel);
		}
		backButton = new JButton("Back");
		backButton.setMargin(WidgetConstants.INSETS_CONTROL);
		backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		backButton.setIcon(backIcon);
		backButton.setDisabledIcon(backIconDisabled);
		backButton.addActionListener(this);
		backButton.setVisible(backButtonVisible);	
		backNextPanel.add(backButton);
		nextButton = new JButton("Next");
		nextButton.setMargin(WidgetConstants.INSETS_CONTROL);
		nextButton.setHorizontalTextPosition(SwingConstants.LEFT);
		nextButton.setIcon(nextIcon);
		nextButton.setDisabledIcon(nextIconDisabled);
		nextButton.addActionListener(this);
		nextButton.setVisible(nextButtonVisible);
		if(messageLabelOrientation == backNextButtonAreaOrientation &&
				messageLabelOrientation == SwingConstants.LEFT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			backNextPanel.add(messagePanel);
		}
		backNextPanel.add(nextButton);		
		
		helpButtonPanel = new JPanel();		
		if(messageLabelOrientation == helpButtonAreaOrientation &&
				messageLabelOrientation == SwingConstants.RIGHT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			helpButtonPanel.add(messagePanel);
		}
		helpButton = new JButton("Help");
		helpButton.setMargin(WidgetConstants.INSETS_CONTROL);
		helpButton.setIcon(helpIcon);
		helpButton.addActionListener(this);
		helpButtonPanel.add(helpButton);
		if(messageLabelOrientation == helpButtonAreaOrientation &&
				messageLabelOrientation == SwingConstants.LEFT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			helpButtonPanel.add(messagePanel);
		}
		helpButton.setVisible(helpButtonVisible);
		
		if(exitButtonAreaOrientation == SwingConstants.LEFT) {
			add(exitButtonPanel);
		} else if(backNextButtonAreaOrientation == SwingConstants.LEFT) {
			add(backNextPanel);
		} else {
			add(helpButtonPanel);
		}
		if(exitButtonAreaOrientation == SwingConstants.CENTER) {
			add(exitButtonPanel);
		} else if(backNextButtonAreaOrientation == SwingConstants.CENTER) {
			add(backNextPanel);
		} else {
			add(helpButtonPanel);
		}
		if(exitButtonAreaOrientation == SwingConstants.RIGHT) {
			add(exitButtonPanel);
		} else if(backNextButtonAreaOrientation == SwingConstants.RIGHT) {
			add(backNextPanel);
		} else {
			add(helpButtonPanel);
		}		
		/*add(exitButtonPanel);
		add(backNextPanel);
		add(helpButtonPanel);*/				
		messagePanel.setVisible(messageLabelVisible);		
	}
	
	@Override
	public JComponent getNavButtonPanelComponent() {
		return this;
	}
	
	public void setMessageLabelVisible(boolean visible) {
		if(messagePanel.isVisible() != visible) {
			messagePanel.setVisible(visible);
			revalidate();
		}
		/*if(messageLabel.isVisible() != visible) {
			messageLabel.setVisible(visible);
			revalidate();
		}*/
	}
	
	public void setMessageLabelFont(Font font) {
		messageLabel.setFont(font);
		if(messageLabelSizingString != null) {
			sizeMessageLabelToMessage(messageLabelSizingString);
		}
	}
	
	public void sizeMessageLabelToMessage(String message) {
		messageLabelSizingString = message;
		if(message != null) {
			String currText = messageLabel.getText();
			messageLabel.setPreferredSize(null);
			messageLabel.setText(message);
			messageLabel.setPreferredSize(messageLabel.getPreferredSize());
			messageLabel.setSize(messageLabel.getPreferredSize());
			messageLabel.setText(currText);
		} else {
			messageLabel.setPreferredSize(null);
		}
		revalidate();
	}
	
	public void setMessage(String message) {
		if(message == null || message.equals("")) {
			messageLabel.setText(" ");
		} else { 
			messageLabel.setText(message);
		}
		revalidate();
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(800, 100));
		NavButtonPanel navPanel = new NavButtonPanel(false, true, true, false, 
				SwingConstants.RIGHT, SwingConstants.CENTER, true, SwingConstants.RIGHT);
		navPanel.sizeMessageLabelToMessage("This could be a very long message");
		//navPanel.setButtonVisible(ButtonType.Exit, true);
		navPanel.setButtonVisible(ButtonType.Back, true);
		navPanel.setButtonVisible(ButtonType.Next, true);
		//navPanel.setButtonVisible(ButtonType.Help, true);
		frame.getContentPane().add(navPanel);
		frame.pack();
		frame.setVisible(true);
		navPanel.setMessageLabelVisible(true);
		navPanel.setMessage("A test message");
	}
	
	@Override
	public void setFocusedButton(ButtonType button) {
		JButton jButton = getButton(button);
		if(jButton != null) {
			jButton.requestFocusInWindow();
		}
	}	

	@Override
	public void setButtonVisible(ButtonType button, boolean visible) {
		JButton jButton = getButton(button);
		if(jButton != null) {
			if(jButton.isVisible() != visible) {
				jButton.setVisible(visible);
				revalidate();
			}
		}
	}
	
	@Override
	public boolean isButtonEnabled(ButtonType button) {
		JButton jButton = getButton(button);
		if(jButton != null) {
			return jButton.isEnabled();
		} else {
			return false;
		}
	}

	@Override
	public void setButtonEnabled(ButtonType button, boolean enabled) {
		JButton jButton = getButton(button);
		if(jButton != null) {
			jButton.setEnabled(enabled);
		}
	}
	
	@Override
	public void setButtonText(ButtonType button, String text) {
		JButton jButton = getButton(button);
		if(jButton != null) {
			jButton.setText(text);
			revalidate();
		}
	}	
	
	@Override
	public void setButtonIcon(ButtonType button, Icon icon) {
		JButton jButton = getButton(button);
		if(jButton != null) {
			jButton.setIcon(icon);
			revalidate();
		}
	}
	
	protected JButton getButton(ButtonType button) {		
		switch(button) {
		case Exit:
			return exitButton;
		case Next:
			return nextButton;
		case Back:
			return backButton;
		case Help:
			return helpButton;
		default:
			return null;
		}
	}

	@Override
	public boolean isSubjectActionListenerPresent(SubjectActionListener listener) {
		synchronized(buttonPressListeners) {
			return buttonPressListeners != null && buttonPressListeners.contains(listener);
		}
	}

	@Override
	public void addSubjectActionListener(SubjectActionListener listener) {
		synchronized(buttonPressListeners) {
			buttonPressListeners.add(listener);
		}
	}	
	
	@Override
	public void removeSubjectActionListener(SubjectActionListener listener) {
		synchronized(buttonPressListeners) {
			buttonPressListeners.remove(listener);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == exitButton) {
			//Fire an exit button pressed event
			fireButtonPressedEvent(SubjectActionEvent.EXIT_BUTTON_PRESSED);
		}
		if(event.getSource() == backButton) {
			//Fire a back button pressed event
			fireButtonPressedEvent(SubjectActionEvent.BACK_BUTTON_PRESSED);
		}
		else if(event.getSource() == nextButton) {
			//Fire a next button pressed event
			fireButtonPressedEvent(SubjectActionEvent.NEXT_BUTTON_PRESSED);
		}
		else if(event.getSource() == helpButton) {
			//Fire a help button pressed event
			fireButtonPressedEvent(SubjectActionEvent.HELP_BUTTON_PRESSED);
		}
	}
	
	/** Fire a button pressed event to all registered listeners */
	protected void fireButtonPressedEvent(int buttonTypeEvent) {
		synchronized(buttonPressListeners) {
			if(buttonPressListeners != null && !buttonPressListeners.isEmpty()) {
				final SubjectActionEvent event = new SubjectActionEvent(buttonTypeEvent, this); 
				for(final SubjectActionListener listener : buttonPressListeners) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							listener.subjectActionPerformed(event);
						}
					});					
				}
			}
		}
	}
}
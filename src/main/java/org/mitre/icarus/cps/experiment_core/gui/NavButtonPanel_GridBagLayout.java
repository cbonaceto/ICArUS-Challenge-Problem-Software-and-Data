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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
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
public class NavButtonPanel_GridBagLayout extends JPanel implements INavButtonPanel, ActionListener {
private static final long serialVersionUID = 1L;
	
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
	public NavButtonPanel_GridBagLayout() {
		this(false, true, true, false);
	}
	
	/**
	 * @param exitButtonVisible
	 * @param backButtonVisible
	 * @param nextButtonVisible
	 * @param helpButtonVisible
	 */
	public NavButtonPanel_GridBagLayout(boolean exitButtonVisible, boolean backButtonVisible, 
			boolean nextButtonVisible, boolean helpButtonVisible) {
		this(exitButtonVisible, backButtonVisible, nextButtonVisible, helpButtonVisible,
				false, SwingConstants.LEFT);
	}
	
	/**
	 * @param exitButtonVisible
	 * @param backButtonVisible
	 * @param nextButtonVisible
	 * @param helpButtonVisible
	 * @param messageLabelVisible
	 * @param messageLabelOrientation
	 */
	public NavButtonPanel_GridBagLayout(boolean exitButtonVisible, boolean backButtonVisible, 
			boolean nextButtonVisible, boolean helpButtonVisible, 
			boolean messageLabelVisible, int messageLabelOrientation) {
		//super(new GridLayout(1, 3));
		super(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints panelGbc = new GridBagConstraints();
		panelGbc.gridx = 0;
		panelGbc.gridy = 0;
		
		messagePanel = new JPanel();
		messagePanel.add(messageLabel = new JLabel());
		//messageLabel = new JLabel();
		messageLabel.setText(" ");
		
		exitButtonPanel = new JPanel(new GridBagLayout());
		exitButtonPanel.setBackground(Color.magenta);
		exitButton = new JButton("Help");
		messageLabel.setFont(exitButton.getFont());
		exitButton.setMargin(WidgetConstants.INSETS_CONTROL);
		exitButton.addActionListener(this);
		panelGbc.gridx = 0;
		panelGbc.anchor = GridBagConstraints.WEST;
		exitButtonPanel.add(exitButton, panelGbc);
		exitButton.setVisible(exitButtonVisible);
		if(messageLabelOrientation != SwingConstants.RIGHT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			panelGbc.gridx = 1;
			exitButtonPanel.add(messagePanel, panelGbc);
			//messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			//exitButtonPanel.add(messageLabel);
		}
		gbc.gridx = 0;
		//gbc.anchor = GridBagConstraints.WEST;
		//gbc.insets.left = 4;
		add(exitButtonPanel, gbc);
		gbc.insets.left = 0;
		
		backNextPanel = new JPanel(new GridBagLayout());
		backNextPanel.setBackground(Color.red);
		backButton = new JButton("Back");
		backButton.setMargin(WidgetConstants.INSETS_CONTROL);
		backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
		backButton.setIcon(backIcon);
		backButton.setDisabledIcon(backIconDisabled);
		backButton.addActionListener(this);
		backButton.setVisible(backButtonVisible);
		panelGbc.gridx = 0;
		panelGbc.anchor = GridBagConstraints.CENTER;
		backNextPanel.add(backButton, panelGbc);
		nextButton = new JButton("Next");
		nextButton.setMargin(WidgetConstants.INSETS_CONTROL);
		nextButton.setHorizontalTextPosition(SwingConstants.LEFT);
		nextButton.setIcon(nextIcon);
		nextButton.setDisabledIcon(nextIconDisabled);
		nextButton.addActionListener(this);
		nextButton.setVisible(nextButtonVisible);
		panelGbc.gridx = 1;
		backNextPanel.add(nextButton, panelGbc);
		gbc.gridx = 1;
		//gbc.anchor = GridBagConstraints.CENTER;
		add(backNextPanel, gbc);
		
		helpButtonPanel = new JPanel(new GridBagLayout());
		panelGbc.gridx = 0;
		panelGbc.anchor = GridBagConstraints.EAST;
		helpButtonPanel.setBackground(Color.blue);
		if(messageLabelOrientation == SwingConstants.RIGHT) {
			messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			helpButtonPanel.add(messagePanel, panelGbc);
			panelGbc.gridx++;
		}
		helpButton = new JButton("Help");
		helpButton.setMargin(WidgetConstants.INSETS_CONTROL);
		helpButton.setIcon(helpIcon);
		helpButton.addActionListener(this);
		helpButtonPanel.add(helpButton, panelGbc);
		helpButton.setVisible(helpButtonVisible);
		gbc.gridx = 2;
		//gbc.anchor = GridBagConstraints.EAST;
		//gbc.insets.right = 4;
		add(helpButtonPanel, gbc);
		
		messagePanel.setVisible(messageLabelVisible);
	}
	
	@Override
	public JComponent getNavButtonPanelComponent() {
		return this;
	}
	
	public void setMessageLabelVisible(boolean visible) {
		if(messagePanel.isVisible() != visible) {
			messagePanel.setVisible(visible);
			updateLayout();
		}
		/*if(messageLabel.isVisible() != visible) {
			messageLabel.setVisible(visible);
			updateLayout();
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
		updateLayout();
	}
	
	public void setMessage(String message) {
		if(message == null || message.equals("")) {
			messageLabel.setText(" ");
		} else { 
			messageLabel.setText(message);
		}
		updateLayout();
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(800, 100));
		NavButtonPanel_GridBagLayout navPanel = 
				new NavButtonPanel_GridBagLayout(false, true, true, false, true, SwingConstants.RIGHT);
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
				updateLayout();
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
			updateLayout();
		}
	}	
	
	@Override
	public void setButtonIcon(ButtonType button, Icon icon) {
		JButton jButton = getButton(button);
		if(jButton != null) {
			jButton.setIcon(icon);
			updateLayout();
		}
	}
	
	protected void updateLayout() {
		exitButtonPanel.setPreferredSize(null);
		helpButtonPanel.setPreferredSize(null);
		revalidate();
		//System.out.println(helpButtonPanel.getPreferredSize().width);
		if(exitButtonPanel.getPreferredSize().width != helpButtonPanel.getPreferredSize().width) {
			int maxWidth = Math.max(exitButtonPanel.getPreferredSize().width, helpButtonPanel.getPreferredSize().width);
			exitButtonPanel.setPreferredSize(
					new Dimension(maxWidth, exitButtonPanel.getPreferredSize().height));
			helpButtonPanel.setPreferredSize(
					new Dimension(maxWidth, helpButtonPanel.getPreferredSize().height));
			//System.out.println(maxWidth);
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
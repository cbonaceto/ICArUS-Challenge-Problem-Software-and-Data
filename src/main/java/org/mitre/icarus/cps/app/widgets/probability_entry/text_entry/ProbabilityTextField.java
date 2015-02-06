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
package org.mitre.icarus.cps.app.widgets.probability_entry.text_entry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryComponent;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.Setting;
import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.IProbabilityController;

/**
 * @author Eric Kappotis
 *
 */
public class ProbabilityTextField extends JPanel implements IProbabilityEntryComponent {
	private static final long serialVersionUID = 4607500329924678649L;
	
	/** The setting object **/
	private final Setting setting;
	
	/** Reference to the probability controller */
	protected IProbabilityController controller;
	
	private JPanel textFieldPanel;
	
	private JSpinner textFieldSpinner;
	private JSpinner.DefaultEditor spinnerEditor;
	private Color spinnerForeground;
	
	private JTextField textField;	
	
	private JLabel titleLabel;
	
	private JLabel lockLabel;
	
	/** Label for the percent sign */
	protected JLabel percentLabel;

	private boolean editable = true;
	
	private boolean enableLocking;
	
	private int currKeyCode;
	
	private boolean focused;
	
	private int id;
	
	/** Total time spent editing the probability text */
	protected long textEditTime = 0;
	
	protected long textEditStartTime = -1;
	
	/** Whether to format the text field as a percent by showing a percent sign after it. Default is true */
	protected boolean formatAsPercent = true;
	
	//TODO: Use these
	protected boolean showingMinSetting = false;
	protected boolean showingMaxSetting = false;
	
	private boolean updatingSetting = false;
	
	/** The previous setting (not used or displayed) */
	protected Integer previousSetting;
	
	public ProbabilityTextField(String title, boolean showTitle, boolean showSpinner, int id, IProbabilityController controller) {
		this(new Setting(), title, showTitle, showSpinner, id, controller);
	}
	
	public ProbabilityTextField(Setting setting, String title, boolean showTitle, boolean showSpinner, int id, IProbabilityController controller) {		
		setLayout(new GridBagLayout());
		if(setting != null) {
			this.setting = setting;
		} else {
			this.setting = new Setting();
		}
		this.id = id;
		this.controller = controller;
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;		
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.NONE;	
		
		titleLabel = new JLabel(title);
		titleLabel.setHorizontalTextPosition(JLabel.LEFT);		
		titleLabel.setFont(ProbabilityEntryConstants.FONT_PROBABILITY_TITLE);
		this.add(titleLabel, constraints);		
		
		textFieldPanel = new JPanel(new GridBagLayout());
		constraints.gridx = 0; 
		constraints.gridy = 0;
		if(showSpinner) {
			textFieldSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
			textFieldSpinner.setEditor(new TextFieldSpinnerEditor(textFieldSpinner));
			textFieldSpinner.setFont(ProbabilityEntryConstants.FONT_PROBABILITY);
			textFieldSpinner.setValue(0);
			textFieldSpinner.setPreferredSize(new Dimension(textFieldSpinner.getPreferredSize().width, 
					textFieldSpinner.getPreferredSize().height + 10));
			textFieldPanel.add(textFieldSpinner, constraints);
		} else {
			textField = new JTextField();
			textField.setFont(ProbabilityEntryConstants.FONT_PROBABILITY);
			JTextField tempField = new JTextField("99.99");
			tempField.setFont(ProbabilityEntryConstants.FONT_PROBABILITY);
			textField.setPreferredSize(tempField.getPreferredSize());
			textFieldPanel.add(textField, constraints);	
		}
		
		percentLabel = new JLabel(editable ? "%" : "99.99%");
		percentLabel.setFont(ProbabilityEntryConstants.FONT_PROBABILITY);
		percentLabel.setPreferredSize(percentLabel.getPreferredSize());		
		percentLabel.setText(editable ? "%" : "");
		percentLabel.setHorizontalAlignment(editable ? JLabel.LEFT : JLabel.CENTER);
		percentLabel.setVisible(formatAsPercent);		
		constraints.gridx = 1; 
		constraints.gridy = 0;
		constraints.insets.left = 3;
		textFieldPanel.add(percentLabel, constraints);
		constraints.insets.left = 0;
		
		constraints.gridx = 0;
		constraints.gridy = 0;		
		add(textFieldPanel, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;		
		lockLabel = new JLabel(ImageManager.getImageIcon(ImageManager.UNLOCKED_ICON));
		lockLabel.setVisible(false);
		add(lockLabel, constraints);
		
		lockLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(editable) {
					toggleLock();
				}
			}			
		});
		
		FocusAdapter focusListener = new FocusAdapter() {			
			@Override
			public void focusGained(FocusEvent event) {				
				if(editable) {
					textEditStartTime = System.currentTimeMillis();
				} else {
					textEditStartTime = -1;
				}
				if(textField != null) {				
					if(textField.getText() != null) {
						textField.setSelectionStart(0);
						textField.setSelectionEnd(textField.getText().length());
					}
				}
			}
			@Override
			public void focusLost(FocusEvent event) {			
				if(editable && textEditStartTime > 0) {
					textEditTime += System.currentTimeMillis() - textEditStartTime;
				}
				if(textField != null) {
					//updateSetting();
				}
			}			
		};		
		KeyAdapter keyListener = new KeyAdapter() {			
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				currKeyCode = keyEvent.getKeyCode();
			}
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				if(!isNumberKeyPressed(currKeyCode)) {
					if(currKeyCode == KeyEvent.VK_ENTER) {
						//Enter pressed
						if(editable && textEditStartTime > 0) {
							textEditTime += System.currentTimeMillis() - textEditStartTime;
						}
						if(textField != null) {
							updateSetting();
						}
					} else {
						keyEvent.consume();
					}
				} else {					
					if(textField != null) {
						updateSetting();
					} else if(textFieldSpinner != null) {
						String text = spinnerEditor.getTextField().getText();
						if(text != null && text.length() > 3) {
							keyEvent.consume();
						}
					}
				}
			}
		};
		
		if(textField != null) {
			textField.addFocusListener(focusListener);
			textField.addKeyListener(keyListener);			
			/*textField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void changedUpdate(DocumentEvent event) {}

				@Override
				public void insertUpdate(DocumentEvent event) {
					//ProbabilityEntryField.this.controller.updateSettingRequest(getId(), getIntValue());
				}

				@Override
				public void removeUpdate(DocumentEvent event) {
					//ProbabilityEntryField.this.controller.updateSettingRequest(getId(), getIntValue());
				}			
			});*/			
		} else if(textFieldSpinner != null) {
			spinnerEditor = (JSpinner.DefaultEditor)textFieldSpinner.getEditor();
			spinnerEditor.getTextField().addFocusListener(focusListener);
			spinnerEditor.getTextField().addKeyListener(keyListener);
			spinnerEditor.getTextField().getDocument().addDocumentListener(new DocumentListener() {				
				@Override
				public void insertUpdate(DocumentEvent e) {
					//System.out.println("spinner changed: insert");
					updateSetting();
				}	
				@Override
				public void removeUpdate(DocumentEvent e) {
					//System.out.println("spinner changed: remove");
					//if(spinnerForeground == null) {updateSetting();}
					updateSetting();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					//System.out.println("spinner changed: changed");
					updateSetting();					
				}				
			});
		}		
	}
	
	protected void updateSetting() {
		if(!updatingSetting) {
			//System.out.println("updating setting");
			try {
				int currentParsedSetting = 0;
				boolean spinnerEditedForFirstTime = false;
				updatingSetting = true;				
				if(textField != null) {
					if(textField.getText().isEmpty()) {
						//setting.setCurrentSetting(0);
						currentParsedSetting = 0;
					} else {
						//setting.setCurrentSetting(Integer.parseInt(textField.getText()));
						currentParsedSetting = Integer.parseInt(textField.getText());
					}
				} else {
					//Attempt to update the current setting to the value in the spinner's text field
					String text = spinnerEditor.getTextField().getText();					
					currentParsedSetting = textFieldSpinner.getValue() != null ? (Integer)textFieldSpinner.getValue() : 0;
					//System.out.println("Text: " + text + ", current parsed setting: " + currentParsedSetting);
					if(spinnerForeground != null) {
						spinnerEditedForFirstTime = true;
						spinnerEditor.getTextField().setForeground(spinnerForeground);
						spinnerForeground = null;
					}
					if(text != null && !text.isEmpty() && !text.equals("")) {
						try {							
							currentParsedSetting = Integer.parseInt(text);
							if(spinnerEditedForFirstTime && currentParsedSetting == 1) {
								currentParsedSetting = 0;
							}
						} catch(Exception ex) {}
					}
				}				
				if(currentParsedSetting != setting.getIntValue() || spinnerEditedForFirstTime) {
					//if(currentParsedSetting < 0) {currentParsedSetting = 0;}
					//System.out.println("Updating setting to: " + currentParsedSetting);
					setting.setCurrentSetting(currentParsedSetting);
					/*if(controller != null && controller.isAutoNormalize()) {
						if(getIntValue() > getMaxValue()) {			
							setCurrentSetting(getMaxValue());
						}
					} else {
						if(getIntValue() > 100) {			
							setCurrentSetting(100);
						}		
					}*/
					if(controller != null) {
						controller.updateSettingRequest(id, setting.getDisplaySetting());
					}
					if(textField != null) {
						textField.setText(Integer.toString(setting.getDisplaySetting()));
					} else {
						int maxValue = controller != null && controller.isAutoNormalize() ? getMaxValue() : 100;
						if(getIntValue() > maxValue) {
							spinnerEditor.getTextField().setForeground(ProbabilityEntryConstants.COLOR_TEXT_ERROR);
						} else {
							spinnerEditor.getTextField().setForeground(spinnerForeground);
						}
					}					
					//SwingUtilities.invokeLater(new Runnable() {public void run() {
					//		try {spinnerEditor.commitEdit();} catch(Exception ex) {};}});					
					//System.out.println("Current setting: "  + setting.getIntValue() + ", Display setting: " + setting.getDisplaySetting());
				} else {
					//System.out.println("setting didn't change");
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}  finally {
				updatingSetting = false;
			}
		}
	}
	
	protected boolean isNumberKeyPressed(int keyCode) {
		if(keyCode == KeyEvent.VK_0 || keyCode == KeyEvent.VK_1 || keyCode == KeyEvent.VK_2 ||
			keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_4 || keyCode == KeyEvent.VK_5 ||
			keyCode == KeyEvent.VK_6 || keyCode == KeyEvent.VK_7 || keyCode == KeyEvent.VK_8 ||
			keyCode == KeyEvent.VK_9 || keyCode == KeyEvent.VK_NUMPAD0 || keyCode == KeyEvent.VK_NUMPAD1 ||
			keyCode == KeyEvent.VK_NUMPAD2 || keyCode == KeyEvent.VK_NUMPAD3 || keyCode == KeyEvent.VK_NUMPAD4 ||
			keyCode == KeyEvent.VK_NUMPAD5 || keyCode == KeyEvent.VK_NUMPAD6 || keyCode == KeyEvent.VK_NUMPAD7 ||
			keyCode == KeyEvent.VK_NUMPAD8 || keyCode == KeyEvent.VK_NUMPAD9) {			
			return true;			
		}
		return false;
	}
	
	public boolean isFormatAsPercent() {
		return formatAsPercent;
	}

	public void setFormatAsPercent(boolean formatAsPercent) {
		if(formatAsPercent != percentLabel.isVisible()) {
			this.formatAsPercent = formatAsPercent;
			percentLabel.setVisible(formatAsPercent);
			revalidate();
		}
	}

	@Override
	public boolean requestFocus(boolean temporary) {
		return textField != null ? textField.requestFocus(temporary) : textFieldSpinner.requestFocus(temporary);
	}

	@Override
	public boolean requestFocusInWindow() {
		return textField != null ? textField.requestFocusInWindow() : textFieldSpinner.requestFocusInWindow();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		JTextField sizingField = new JTextField("99.999");
		sizingField.setFont(font);
		if(percentLabel != null) {
			String text = percentLabel.getText();
			if(!editable) {
				percentLabel.setText("99.99%");
			}			
			percentLabel.setPreferredSize(null);
			percentLabel.setFont(font);
			percentLabel.setPreferredSize(percentLabel.getPreferredSize());
			if(!editable) {
				percentLabel.setText(text);
			}
		}
		if(textField != null) {
			textField.setFont(font);
			textField.setPreferredSize(sizingField.getPreferredSize());
		}
		if(textFieldSpinner != null) {
			textFieldSpinner.setFont(font);	
			textFieldSpinner.setPreferredSize(new Dimension(textFieldSpinner.getPreferredSize().width, 
					textFieldSpinner.getPreferredSize().height + 10));
		}
		revalidate();
	}
	
	@Override
	public Color getProbabilityEntryColor() {
		if(percentLabel != null) {
			return percentLabel.getForeground();
		} else if(textField != null) {
			return textField.getForeground();	
		}
		return null;		
	}

	@Override
	public void setProbabilityEntryColor(Color color) {
		/*if(percentLabel != null) {
			percentLabel.setForeground(color);	
		}*/
		if(textField != null) {
			textField.setForeground(color);	
		} else if(textFieldSpinner != null) {
			textFieldSpinner.setForeground(color);
		}
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(textFieldPanel != null) {
			textFieldPanel.setBackground(bg);
			if(textField != null) {
				textField.setBackground(bg);	
			} else if(textFieldSpinner != null) {
				textFieldSpinner.setBackground(bg);
			}
		}
	}

	public long getInteractionTime() {
		return textEditTime;
	}
	
	public void resetInteractionTime() {
		textEditTime = 0;
		textEditStartTime = -1;
	}

	/**
	 * @return the locked
	 */
	@Override
	public boolean isLocked() {
		return setting.isLocked();
	}

	/**
	 * @param locked the locked to set
	 */
	@Override
	public void setLocked(boolean locked) {
		if(setting.isLocked() != locked) {
			toggleLock();
		}
	}
	
	/** If locked, unlock. If unlocked, lock. */
	protected void toggleLock() {
		if(!setting.isLocked()) {
			//Attempt to lock setting if possible
			updateSetting();
			boolean lockable = true;
			if(controller != null) {
				lockable = controller.isLockable(ProbabilityTextField.this.id);
			}
			if(lockable) {
				//Lock setting
				setting.setLocked(true);
				lockLabel.setIcon(ImageManager.getImageIcon(ImageManager.LOCKED_ICON));
				if(textField != null) {
					textField.setEnabled(false);
				} else {
					textFieldSpinner.setEnabled(false);
				}
			}
		} else {
			//Unlock setting
			setting.setLocked(false);
			lockLabel.setIcon(ImageManager.getImageIcon(ImageManager.UNLOCKED_ICON));
			if(textField != null) {
				textField.setEnabled(true);
			} else {
				textFieldSpinner.setEnabled(true);
			}
		}
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void setEditable(boolean editable) {
		if(this.editable != editable) {
			this.editable = editable;
			if(!(editable && setting.isLocked())) {
				//Don't enable the text field if the setting is locked
				if(!editable) {
					if(textField != null) {
						textField.setVisible(false);
					} else {
						textFieldSpinner.setVisible(false);
					}
					percentLabel.setHorizontalAlignment(JLabel.CENTER);
					percentLabel.setPreferredSize(null);
					percentLabel.setText("99.99%");
					percentLabel.setPreferredSize(percentLabel.getPreferredSize());
					percentLabel.setText(textField != null ? textField.getText() : textFieldSpinner.getValue().toString() + "%");					
					if(!percentLabel.isVisible()) {
						percentLabel.setVisible(true);
					}
				} else {
					if(textField != null) {
						textField.setVisible(true);
					} else {
						textFieldSpinner.setVisible(true);
					}
					percentLabel.setHorizontalAlignment(JLabel.LEFT);
					if(formatAsPercent) {
						percentLabel.setPreferredSize(null);
						percentLabel.setText("99.99%");						
						percentLabel.setPreferredSize(percentLabel.getPreferredSize());
						percentLabel.setText("%");
					} else {
						percentLabel.setVisible(false);
					}
				}
				revalidate();
			}
		}
	}	
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return titleLabel.getText();
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
		revalidate();
	}
	
	public Color getTitleColor() {
		return titleLabel.getForeground();
	}
	
	public void setTitleColor(Color color) {
		titleLabel.setForeground(color);
		repaint();
	}
	
	public Font getTitleFont() {
		return titleLabel.getFont();
	}
	
	public void setTitleFont(Font font) {
		titleLabel.setFont(font);
		revalidate();
		repaint();
	}
	
	public Icon getTitleIcon() {
		if(titleLabel != null) {
			return titleLabel.getIcon();
		}
		return null;
	}
	
	public void setTitleIcon(Icon icon) {		
		titleLabel.setIcon(icon);
		revalidate();
		repaint();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ProbabilityTextField entryField = new ProbabilityTextField("A", true, true, 1, null);
		frame.getContentPane().add(entryField);
		
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void setProbabilityController(IProbabilityController controller) {
		this.controller = controller;
	}

	@Override
	public Integer getMaxValue() {
		// calculates the max value that can be entered into a particular field
		// based on which fields are locked
		if(controller != null) {
			int calcMax = 100;
			for(IProbabilityEntryComponent entryField : controller.getProbabilityComponents()) {
				if(entryField.isLocked() && entryField != this) {
					calcMax = calcMax - entryField.getIntValue();
				}
			}			
			return calcMax;
		}		
		return setting.getMaxValue();
	}

	@Override
	public void setMaxValue(Integer value) {
		setting.setMaxValue(value);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void setEnableLocking(boolean enableLocking) {
		if(enableLocking != this.enableLocking) {
			this.enableLocking = enableLocking;
			lockLabel.setVisible(enableLocking);
			revalidate();
		}
	}

	@Override
	public boolean isEnableLocking() {
		return this.enableLocking;
	}

	@Override
	public boolean isFocused() {
		return this.focused;
	}

	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	@Override
	public Integer getIntValue() {
		return setting.getIntValue();
	}

	@Override
	public Double getDoubleValue() {
		return setting.getValue();		
	}

	@Override
	public void setDoubleValue(Double value) {
		setting.setValue(value);
	}

	@Override
	public void setCurrentSetting(Integer value) {
		setting.setCurrentSetting(value);
		setTextFieldOrSpinnerValue(value);
	}
	
	@Override
	public Integer getPreviousSetting() {
		return previousSetting;
	}

	@Override
	public void setPreviousSetting(Integer setting) {
		this.previousSetting = setting;
	}

	@Override
	public Integer getDisplaySetting() {
		return setting.getDisplaySetting();
	}

	@Override
	public void setDisplaySetting(Integer value) {
		setting.setDisplaySetting(value);
		setTextFieldOrSpinnerValue(value);
	}	
	
	protected void setTextFieldOrSpinnerValue(Integer value) {
		if(textField != null) {
			textField.setText(Integer.toString(value));
			if(!textField.isVisible()) {
				percentLabel.setText(textField.getText() + "%");
			}
		} else {
			if(!updatingSetting) {
				try {
					updatingSetting = true;
					textFieldSpinner.setValue(value != null ? value : 0);
					int maxValue = controller != null && controller.isAutoNormalize() ? getMaxValue() : 100;
					int val = getIntValue();
					if(val < 0 || val > maxValue) {
						spinnerEditor.getTextField().setForeground(ProbabilityEntryConstants.COLOR_TEXT_ERROR);
					} else {
						spinnerEditor.getTextField().setForeground(spinnerForeground);
					}
					/*if(value == null) {
						//Set the value to 0, but hide the value in the text field editor			
						//spinnerEditor.getTextField().setText("");
						textFieldSpinner.setValue(0);
						if(spinnerForeground == null) {
							spinnerForeground = spinnerEditor.getTextField().getForeground();
							spinnerEditor.getTextField().setForeground(spinnerEditor.getTextField().getBackground());
						}
					} else {
						textFieldSpinner.setValue(value);
						if(spinnerForeground != null) {
							spinnerEditor.getTextField().setForeground(spinnerForeground);
							spinnerForeground = null;
						}
					}*/
				} catch(Exception ex) {					
				} finally {
					updatingSetting = false;
				}
			}
			if(!textFieldSpinner.isVisible()) {
				percentLabel.setText(textFieldSpinner.getValue().toString() + "%");
			}
		}
	}	

	@Override
	public boolean isDisplayedValueValid() {
		Integer maxValue = getMaxValue();
		maxValue = maxValue == null ? 100 : maxValue;
		Integer currentParsedSetting = null;
		try {
			String text = textField != null ? textField.getText() : spinnerEditor.getTextField().getText();			
			//System.out.println(text.isEmpty() + ", " + spinnerEditor.getTextField().hasFocus() + ", " + textFieldSpinner.getValue());
			currentParsedSetting = text != null && !text.isEmpty() && !text.equals("") ? 
					Integer.parseInt(text) : textFieldSpinner != null && textFieldSpinner.getValue() != null ? 
							(Integer)textFieldSpinner.getValue() : null;					
		} catch(Exception ex) {}
		//System.out.println("Current parsed setting: " + currentParsedSetting + ", valid: " + 
		//		(currentParsedSetting != null && currentParsedSetting >= 0 && currentParsedSetting <= maxValue));
		return currentParsedSetting != null && currentParsedSetting >= 0 && currentParsedSetting <= maxValue;
	}

	@Override
	public JComponent getComponent() {
		return this;
	}	
}
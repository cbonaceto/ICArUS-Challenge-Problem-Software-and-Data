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
package org.mitre.icarus.cps.app.widgets.probability_entry.distinct_bars;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.mitre.icarus.cps.app.widgets.ImageManager;
import org.mitre.icarus.cps.app.widgets.events.IcarusGUIEvent;
import org.mitre.icarus.cps.app.widgets.events.IcarusGUIEventListener;
import org.mitre.icarus.cps.app.widgets.events.InteractionComponentType;
import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryComponent;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityContainerLayout;
import org.mitre.icarus.cps.app.widgets.probability_entry.Setting;
import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.IProbabilityController;
import org.mitre.icarus.cps.app.widgets.probability_entry.spinners.JSettingSpinner;
import org.mitre.icarus.cps.app.widgets.probability_entry.text_entry.ProbabilityTextField;

/**
 * A settings box that may have 0-4 decorator components oriented to the east, west, north, and south of the box.
 * 
 * @author CBONACETO
 *
 */
public class SettingsBoxWithDecorators extends BoxWithDecorators implements IProbabilityEntryComponent {
	
	private static final long serialVersionUID = 1L;
	
	/** The edit control types */
	public static enum EditControlType{Spinner, TextField, PercentDisplay, None};

	/** The settings box */
	protected SettingsBox settingsBox;
	
	/** The probability controller */
	protected IProbabilityController controller;

	protected int id;
	
	/** The current edit control type (spinner, text box, percent label, or none) */
	private EditControlType editControlType;
	
	/** When an edit control type is Spinner or TextField, whether the spinner or text box can be edited */
	protected boolean editControlEditable;
	
	/** The orientation of the edit control. Default is North. */
	protected int editControlOrientation = ProbabilityContainerLayout.NORTH;
	
	/** A spinner control for settings box when the edit control type is Spinner */
	protected JSettingSpinner spinner;
	
	/** A text entry field for the settings box when the edit control type is TextField or PercentDisplay */
	protected ProbabilityTextField textBox;
	
	/** The current edit control (spinner, text box, or null) */
	protected IProbabilityEntryComponent editControl;
	
	/** Whether or not to format the edit control setting as a percent */
	protected boolean formatAsPercent = true;
	
	/** Whether the box is editable (e.g., by clicking or dragging the mouse or using the scroll wheel) */
	protected boolean boxEditable;
	
	/** Whether the setting can be changed by dragging the current setting bar */
	protected boolean boxDraggable = true;
	
	/** Whether the settings can be changed by clicking in the box */
	protected boolean boxClickable = false;
	
	/** Whether the setting at the mouse's location should be displayed as the mouse is moved in the bar */
	protected boolean displaySettingAtMouseLocation = false;	
	
	/** JLabel containing the lock image */
	protected JLabel lockLabel;
	
	/** The orientation of the lock image */
	protected int lockOrientation = ProbabilityContainerLayout.WEST;
	
	/** Whether locking is enabled */
	protected boolean enableLocking = false;	
	
	/** Listeners that have registered to be notified when the user changes the setting (by using the spinner
	 * or dragging the bar) */
	protected transient List<IcarusGUIEventListener> listeners;
	
	protected boolean mouseCurrentlyPressed = false;
	protected boolean mouseOverBar = false;
	protected int yOffset = 0;	
	
	/** Hit target width for the current setting line */
	protected int lineHitTargetHeight = 14; //Previously 10
	
	/** The total time spent interacting with the slider control */
	protected long sliderInteractionTime = 0;	
	
	protected long sliderPressedStartTime = -1;
	
	/** Total time spent clicking */
	protected long positionClickTime = 0;
	
	protected long positionClickedStartTime = -1;	
	
	public SettingsBoxWithDecorators(Dimension boxSize, Setting setting, int id) {
		super(new SettingsBox(boxSize, setting));
		this.settingsBox = (SettingsBox)box;
		this.id = id;
	}
	
	public SettingsBoxWithDecorators(Dimension boxSize, String title, int titleOrientation,
			Setting setting, int id) {
		super(new SettingsBox(boxSize, setting));
		this.settingsBox = (SettingsBox)box;
		this.id = id;
		super.titleOrientation = titleOrientation;
		super.title = title;
		setTitleVisible(true);
	}
	
	public SettingsBoxWithDecorators(Dimension boxSize, EditControlType editControlType, 
			boolean editControlEditable, int editControlOrientation, boolean bEditable, 
			Setting setting, boolean formatAsPercent, int id) {		
		super(new SettingsBox(boxSize, setting));
		this.settingsBox = (SettingsBox)box;
		this.editControlEditable = editControlEditable;
		this.boxEditable = bEditable;
		this.editControlOrientation = editControlOrientation;
		this.formatAsPercent = formatAsPercent;
		this.id = id;
		
		setEditControlType(editControlType);
		
		if(setting.isLocked()) {
			lockLabel = new JLabel(ImageManager.getImageIcon(ImageManager.LOCKED_ICON));
		} else {
			lockLabel = new JLabel(ImageManager.getImageIcon(ImageManager.UNLOCKED_ICON));
		}		
		lockLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(isEditable()) {
					toggleLock();
				}
			}			
		});		
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent mouseEvent) {
				if(boxEditable && settingsBox.isEnabled() && boxDraggable) {
					if(mouseCurrentlyPressed && mouseOverBar) {						
						int yPos = mouseEvent.getY() + yOffset;
						int myCurrentSetting = getSettingAtYLocation(yPos, settingsBox.getBounds().height);
						//Update the percent display at the mouse location
						if(displaySettingAtMouseLocation && settingsBox.settingAtMouseLocation != null) { 
							settingsBox.settingAtMouseLocation = null;
						}
						controller.updateSettingRequest(SettingsBoxWithDecorators.this.id, myCurrentSetting);						
					}
				}
			}

			@Override
			public void mouseMoved(MouseEvent mouseEvent) {
				//Update whether the mouse is over the slider bar
				if(boxEditable && settingsBox.isEnabled() && boxDraggable) {					
					checkMouseOverBar(mouseEvent);
				}	
				
				//Update the percent display at the mouse location
				if(displaySettingAtMouseLocation && boxEditable && settingsBox.isEnabled()) {
					int yPos = mouseEvent.getY() + yOffset;
					settingsBox.settingAtMouseLocation = getSettingAtYLocation(yPos, settingsBox.getBounds().height);
					if(editControl != null) {
						editControl.setDisplaySetting(settingsBox.settingAtMouseLocation);
					} else {
						settingsBox.mouseLocation = mouseEvent.getPoint();					
						settingsBox.repaint();
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent mouseEvent) {				
				if(boxEditable && settingsBox.isEnabled() && mouseOverBar && !mouseCurrentlyPressed) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					mouseOverBar = false;
				}
				if(displaySettingAtMouseLocation && settingsBox.settingAtMouseLocation != null) {
					if(editControl != null) {
						editControl.setDisplaySetting(settingsBox.setting.getDisplaySetting());
					} else {
						settingsBox.settingAtMouseLocation = null;
						settingsBox.repaint();
					}
				}
			}		
		
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if(boxClickable && positionClickedStartTime > 0) {
					positionClickTime += System.currentTimeMillis() - positionClickedStartTime;
					positionClickedStartTime = -1;
					//System.out.println("updated click time: " + positionClickTime);
				}
			}
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				if(boxEditable && settingsBox.isEnabled() && boxDraggable) {					
					int wheelRotation = event.getWheelRotation();					
					int newSetting = settingsBox.setting.getDisplaySetting();
					if(wheelRotation > 0) {
						newSetting--;
					}
					if(wheelRotation < 0) {
						newSetting++;
					}
					if(newSetting >= settingsBox.setting.getMinValue() && 
							newSetting <= settingsBox.setting.getMaxValue()) {
						controller.updateSettingRequest(SettingsBoxWithDecorators.this.id, newSetting);
						mouseMoved(event);
					}
				}
			}			

			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				if(boxEditable && settingsBox.isEnabled()) {
					if(boxClickable) {
						positionClickedStartTime = System.currentTimeMillis();
						int yPos = mouseEvent.getY() + yOffset;						
						int myCurrentSetting = getSettingAtYLocation(yPos, settingsBox.getBounds().height);
						controller.updateSettingRequest(SettingsBoxWithDecorators.this.id, myCurrentSetting);
						//mouseOverBar = true;
						checkMouseOverBar(mouseEvent);
					} else {
						positionClickedStartTime = -1;
					}
					
					if(boxDraggable && mouseOverBar) {
						sliderPressedStartTime = System.currentTimeMillis();
					} else {
						sliderPressedStartTime = -1;
					}
				}				
				mouseCurrentlyPressed = true;
			}
			
			protected void checkMouseOverBar(MouseEvent mouseEvent) {
				Rectangle bounds = settingsBox.getBounds();
				float pixelsPerPercent = ((float)bounds.height)/(settingsBox.setting.getMaxValue()-settingsBox.setting.getMinValue());
				int height = (int)(pixelsPerPercent * settingsBox.setting.getDisplaySetting());				
				int yPos = bounds.height - height;
				Rectangle lineGrabRect = new Rectangle(0, yPos - lineHitTargetHeight/2, bounds.width, lineHitTargetHeight);
				boolean overBar = lineGrabRect.contains(mouseEvent.getX(), mouseEvent.getY());					
				if(overBar != mouseOverBar) {
					if(overBar) {
						setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
						//yOffset = mouseEvent.getY() - yPos;
					} else {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}	
					mouseOverBar = overBar;
				}
			}

			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				mouseCurrentlyPressed = false;		
				if(boxEditable && settingsBox.isEnabled() && boxDraggable) {
					if(sliderPressedStartTime > 0) {
						sliderInteractionTime += System.currentTimeMillis() - sliderPressedStartTime;
						sliderPressedStartTime = -1;
						//System.out.println("updated slider time: " + sliderInteractionTime);
					}					
					Rectangle bounds = settingsBox.getBounds();
					float pixelsPerSetting = ((float)bounds.height)/(settingsBox.setting.getMaxValue()-settingsBox.setting.getMinValue());
					int height = (int)(pixelsPerSetting * settingsBox.setting.getDisplaySetting());				
					int yPos = bounds.height - height;				
					Rectangle lineGrabRect = new Rectangle(0, yPos - lineHitTargetHeight/2, bounds.width, lineHitTargetHeight);
					if(!lineGrabRect.contains(mouseEvent.getX(), mouseEvent.getY())) {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						mouseOverBar = false;
					}
				}
			}			
		};
		
		//Add mouse motion listener for dragging the slider bar
		settingsBox.addMouseMotionListener(mouseAdapter);
		
		//Add mouse listener to move the slider bar to a point the user clicked and change the cursor when over a bar
		settingsBox.addMouseListener(mouseAdapter);
		
		//Add mouse wheel listener to update the slider bar when the mouse wheel is moved
		settingsBox.addMouseWheelListener(mouseAdapter);
	}
	
	protected int getSettingAtYLocation(int yPos, int barHeight) {
		float pixelsPerPercent = (settingsBox.setting.getMaxValue()-settingsBox.setting.getMinValue())/((float)barHeight);
		int setting = (int)((barHeight - yPos - settingsBox.boxBorderThickness) * pixelsPerPercent);
		if(setting > settingsBox.setting.getMaxValue()) {
			setting = settingsBox.setting.getMaxValue();
		} else if(setting < settingsBox.setting.getMinValue()) {
			setting = settingsBox.setting.getMinValue();
		}
		return setting;
	}
	
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(editControl != null) {
			editControl.getComponent().setBackground(bg);
		}
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if(editControl != null) {
			editControl.setFont(font);
		}
	}

	public long getInteractionTime() {
		if(spinner != null) {
			return sliderInteractionTime + positionClickTime + spinner.getInteractionTime();
		} else if(textBox != null) {
			return sliderInteractionTime + positionClickTime + textBox.getInteractionTime();
		}
		return sliderInteractionTime + positionClickTime;
	}	
	
	public Long getDragTime_ms() {
		if(boxDraggable) {
			return sliderInteractionTime;
		}
		return null;
	}
	
	public Long getClickTime_ms() {
		if(boxClickable) {
			return positionClickTime;
		}
		return null;
	}
	
	public Long getSpinnerTime_ms() {
		if(spinner != null) {
			return spinner.getInteractionTime();
		}
		return null;
	}
	
	public Long getTextEntryTime_ms() {
		if(textBox != null) {
			return textBox.getInteractionTime();
		}
		return null;
	}
	
	public void resetInteractionTime() {
		sliderInteractionTime = 0;
		sliderPressedStartTime = -1;
		positionClickTime = 0;
		positionClickedStartTime = -1;
		if(spinner != null) {
			spinner.resetInteractionTime();
		}
		if(textBox != null) {
			textBox.resetInteractionTime();
		}
	}
	
	public synchronized void addIcarusGUIEventListener(IcarusGUIEventListener listener) {
		if(listeners == null) {
			listeners = new LinkedList<IcarusGUIEventListener>();
		}
		listeners.add(listener);
	}
	
	public synchronized void removeIcarusGUIEventListener(IcarusGUIEventListener listener) {
		if(listeners != null) {			
			listeners.remove(listener);
		}
	}
	
	protected synchronized void fireChangedEvent(InteractionComponentType interactionComponent) {
		if(listeners != null) {			
			IcarusGUIEvent event = new IcarusGUIEvent(this, IcarusGUIEvent.SETTINGS_BOX_VALUE_CHANGED, 
					interactionComponent);
			for(IcarusGUIEventListener listener : listeners) {
				listener.icarusGUIActionPerformed(event);
			}
		}
	}
	
	public EditControlType getEditControlType() {
		return editControlType;
	}

	public void setEditControlType(EditControlType editControlType) {
		if(this.editControlType != editControlType) {
			this.editControlType = editControlType;
			switch(editControlType) {
			case Spinner: //case PercentDisplay:
				//Show the spinner (editable if Spinner, not editable if PercentDisplay)
				if(spinner == null) {
					spinner = new JSettingSpinner(new Setting(settingsBox.setting), 25, editControlEditable, getId());
					spinner.setProbabilityController(controller);					
				}
				spinner.setEditable(editControlEditable && editControlType == EditControlType.Spinner);
				spinner.setFormatAsPercent(formatAsPercent);
				editControl = spinner;
				addDecoratorComponent(spinner, editControlOrientation);
				break;
			case TextField: case PercentDisplay:
				//Show the text box
				if(textBox == null) {
					textBox = new ProbabilityTextField(new Setting(settingsBox.setting), null, false, false, getId(), controller);
				}
				textBox.setEditable(editControlEditable && editControlType == EditControlType.TextField);
				textBox.setFormatAsPercent(formatAsPercent);
				editControl = textBox;
				addDecoratorComponent(textBox, editControlOrientation);
				break;
			default:
				//Don't show anything
				removeDecoratorComponent(editControlOrientation);
				editControl = null;
				break;
			}
		}
	}

	public int getEditControlOrientation() {
		return editControlOrientation;
	}

	public void setEditControlOrientation(int editControlOrientation) {
		if(this.editControlOrientation != editControlOrientation) {
			if(editControlType != EditControlType.None) {
				moveDecoratorComponent(this.editControlOrientation, editControlOrientation);
			}
			this.editControlOrientation = editControlOrientation;
		}
	}		
		
	@Override
	public boolean isEditable() {
		return editControlEditable || boxEditable;
	}

	@Override
	public void setEditable(boolean editable) {
		//Update box
		setBoxEditable(editable);
		
		boolean editControlEditable = editable && this.editControlEditable;
		if(editControlType == EditControlType.Spinner) {
			//Update spinner
			if(spinner != null) {
				//Don't enable the spinner if the setting is locked
				if(!(editControlEditable && isLocked())) {
					spinner.setEditable(editControlEditable);
				}
			}	
		} else if(editControlType == EditControlType.TextField) {
			//Update text box
			if(textBox != null) {
				//Don't enable the text box if the setting is locked
				if(!(editControlEditable && isLocked())) {
					textBox.setEditable(editControlEditable);
				}
			}
		}	
	}

	@Override
	public boolean isEnabled() {
		return isEditable();
	}

	@Override
	public void setEnabled(boolean enabled) {
		setEditable(enabled);
	}

	public boolean isEditControlEditable() {
		return editControlEditable;
	}

	public void setEditControlEditable(boolean editControlEditable) {
		this.editControlEditable = editControlEditable;
		
		//Update spinner
		if(spinner != null) {
			if(!(editControlEditable && isLocked())) {
				//Don't enable the spinner if the setting is locked
				spinner.setEditable(editControlEditable);
			}
		}
		
		//Update text box
		if(textBox != null) {
			if(!(editControlEditable && isLocked())) {
				//Don't enable the text box if the setting is locked
				textBox.setEditable(editControlEditable);
			}
		}
	}	
	
	public boolean isBoxEditable() {
		return boxEditable;
	}

	public void setBoxEditable(boolean boxEditable) {
		this.boxEditable = boxEditable;
		if(!boxEditable) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			mouseOverBar = false;
		}
		//Don't enable the box if the setting is locked
		if(!(boxEditable && isLocked())) {
			settingsBox.setEnabled(boxEditable);
		}
	}

	@Override
	public Integer getPreviousSetting() {
		return settingsBox.getPreviousSetting();
	}

	@Override
	public void setPreviousSetting(Integer lastSetting) {
		settingsBox.setPreviousSetting(lastSetting);
	}

	public boolean isShowPreviousSetting() {
		return settingsBox.isShowPreviousSetting();
	}

	public void setShowPreviousSetting(boolean showPreviousSetting) {
		settingsBox.setShowPreviousSetting(showPreviousSetting);
	}	

	public boolean isShowCurrentSetting() {
		return settingsBox.isShowCurrentSetting();
	}

	public void setShowCurrentSetting(boolean showCurrentSetting) {
		settingsBox.setShowCurrentSetting(showCurrentSetting);
	}
	
	public Color getCurrentSettingLineColor() {
		return settingsBox.currentSettingLineColor;
	}

	public void setCurrentSettingLineColor(Color currentSettingLineColor) {
		settingsBox.setCurrentSettingLineColor(currentSettingLineColor);
	}
	
	public Color getCurrentSettingLineColorDisabled() {
		return settingsBox.getCurrentSettingLineColorDisabled();
	}

	public void setCurrentSettingLineColorDisabled(Color currentSettingLineColorDisabled) {
		settingsBox.setCurrentSettingLineColorDisabled(currentSettingLineColorDisabled);
	}

	public Color getCurrentSettingFillColor() {
		return settingsBox.currentSettingFillColor;
	}

	public void setCurrentSettingFillColor(Color currentSettingFillColor) {
		settingsBox.setCurrentSettingFillColor(currentSettingFillColor);
	}
	
	@Override
	public Color getProbabilityEntryColor() {
		return getCurrentSettingFillColor();
	}

	@Override
	public void setProbabilityEntryColor(Color color) {
		setCurrentSettingFillColor(color);
	}

	public Color getCurrentSettingFillColorDisabled() {
		return settingsBox.currentSettingFillColorDisabled;
	}

	public void setCurrentSettingFillColorDisabled(Color currentSettingFillColorDisabled) {
		settingsBox.setCurrentSettingFillColorDisabled(currentSettingFillColorDisabled);
	}
	
	public boolean isFillCurrentSetting() {
		return settingsBox.isFillCurrentSetting();
	}

	public void setFillCurrentSetting(boolean fillCurrentSetting) {
		settingsBox.setFillCurrentSetting(fillCurrentSetting);
	}

	public Color getLastSettingFillColor() {
		return settingsBox.getLastSettingDashColor();
	}

	public void setLastSettingFillColor(Color lastSettingFillColor) {
		settingsBox.setLastSettingFillColor(lastSettingFillColor);
	}
	
	public boolean isBoxClickable() {
		return boxClickable;
	}
	
	public void setBoxClickable(boolean boxClickable) {
		this.boxClickable = boxClickable;
	}
	
	public boolean isBoxDraggable() {
		return boxDraggable;
	}

	public void setBoxDraggable(boolean boxDraggable) {
		this.boxDraggable = boxDraggable;
		settingsBox.setCurrentSettingLineDraggable(boxDraggable);
	}
	
	public void setCurrentSettingLineDraggable(boolean draggable) {
		settingsBox.setCurrentSettingLineDraggable(draggable);
	}

	public boolean isDisplaySettingAtMouseLocation() {
		return displaySettingAtMouseLocation;
	}

	public void setDisplaySettingAtMouseLocation(boolean displaySettingAtMouseLocation) {
		this.displaySettingAtMouseLocation = displaySettingAtMouseLocation;
		if(!displaySettingAtMouseLocation) {
			settingsBox.settingAtMouseLocation = null;
			settingsBox.repaint();
		}
	}

	@Override
	public void setCurrentSetting(Integer value) {		
		settingsBox.setCurrentSetting(value);
		if(editControl != null) {
			editControl.setCurrentSetting(value);
		}		
		fireChangedEvent(InteractionComponentType.Slider);
	}	
	
	@Override
	public Integer getMaxValue() {
		return settingsBox.setting.getMaxValue();
	}

	@Override
	public void setMaxValue(Integer value) {
		//this.maxValue = value;
		settingsBox.setting.setMaxValue(value);
		if(editControl != null) {
			editControl.setMaxValue(value);
		}
	}
	
	@Override
	public Integer getDisplaySetting() {
		return settingsBox.setting.getDisplaySetting();
	}

	@Override
	public void setDisplaySetting(Integer value) {
		settingsBox.setting.setDisplaySetting(value);
		if(editControl != null) {
			editControl.setDisplaySetting(value);
		}
		fireChangedEvent(InteractionComponentType.Slider);
	}
	
	/**
	 * @return the allowLocking
	 */
	@Override
	public boolean isEnableLocking() {
		return enableLocking;
	}

	/**
	 * @param allowLocking the allowLocking to set
	 */
	@Override
	public void setEnableLocking(boolean enableLocking) {
		if(this.enableLocking != enableLocking) {
			this.enableLocking = enableLocking;		
			if(enableLocking) {
				addDecoratorComponent(lockLabel, lockOrientation);
			} else {
				removeDecoratorComponent(lockOrientation);
			}
		}
	}

	@Override
	public boolean isLocked() {
		return settingsBox.setting.isLocked();
	}

	@Override
	public void setLocked(boolean locked) {
		if(isLocked() != locked) {
			toggleLock();
		}
	}
	
	/** If locked, unlock. If unlocked, lock. */
	protected void toggleLock() {
		if(!isLocked()) {
			//Attempt to lock setting if possible
			boolean lockable = true;
			if(controller != null) {
				lockable = controller.isLockable(id);
			}
			if(lockable) {
				//Lock setting
				settingsBox.setting.setLocked(true);
				lockLabel.setIcon(ImageManager.getImageIcon(ImageManager.LOCKED_ICON));
				settingsBox.setEnabled(false);
				if(editControl != null && editControlEditable) {
					editControl.setEditable(false);
				}
				repaint();
			}
		} else {
			//Unlock setting
			settingsBox.setting.setLocked(false);
			lockLabel.setIcon(ImageManager.getImageIcon(ImageManager.UNLOCKED_ICON));
			settingsBox.setEnabled(true);
			if(editControl != null && editControlEditable) {
				spinner.setEditable(true);
			}
			repaint();
		}
	}

	@Override
	public void setFocused(boolean focused) {
		settingsBox.setting.setCurrent(focused);
		if(editControl != null) {
			editControl.setFocused(focused);
		}
	}

	@Override
	public boolean isFocused() {		
		return settingsBox.setting.isCurrent();
	}

	@Override
	public void setProbabilityController(IProbabilityController controller) {
		this.controller = controller;
		if(editControl != null) {
			editControl.setProbabilityController(controller);
		}
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
	public JComponent getComponent() {
		return this;
	}	

	@Override
	public Integer getIntValue() {
		return settingsBox.setting.getIntValue();
	}

	@Override
	public Double getDoubleValue() {
		return settingsBox.setting.getValue();
	}

	@Override
	public void setDoubleValue(Double value) {
		settingsBox.setting.setValue(value);
	}

	@Override
	public boolean isDisplayedValueValid() {
		switch(editControlType) {
		case Spinner:
			return spinner.isDisplayedValueValid();			
		case TextField: case PercentDisplay:
			return textBox.isDisplayedValueValid();			
		default:
			return true;
		}
	}
}
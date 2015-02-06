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
package org.mitre.icarus.cps.app.widgets.probability_entry.spinners;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.mitre.icarus.cps.app.widgets.probability_entry.IProbabilityEntryComponent;
import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.Setting;
import org.mitre.icarus.cps.app.widgets.probability_entry.controllers.IProbabilityController;

/**
 *  
 *  
 * @author CBONACETO
 *
 */
public class JSettingSpinner extends JComponent implements IProbabilityEntryComponent  {	
	private static final long serialVersionUID = 3557507164987753667L;
	
	/** The setting object **/
	private final Setting setting;
	
	private int id;
	
	/** Reference to the probability controller */
	private IProbabilityController controller;
	
	private boolean highlight;	
	
	/** Whether the current setting can be adjusted */
	private boolean editable;	
	
	/** Whether to underline the current setting */
	private boolean underlineSetting = false;
	
	/** Whether to format the current setting as a percent */
	private boolean formatAsPercent = true;
	
	private MouseAdapter mouseAdapter;
	private Polygon upButton;
	private Polygon downButton;
	
	/** The action listeners **/
	private List<ActionListener> listeners;
	
	/** Mouse states */
	public final static int NONE = 0;
	public final static int UP_ARROW = 1;
	public final static int DOWN_ARROW = 2;
	
	private int mousePressedOverState;
	
	private boolean overDownArrow = false;
	private boolean overUpArrow = false;
	
	// timer related things
	private Timer mouseDownTimer;
	private ActionListener updateSpinnerTask;
	private int delayTime;
	
	/** Triangle button width */
	private int triangleWidth = 18; //Was originally 15
	
	/** Triangle button height */
	private int triangleHeight = (int)(0.8 * triangleWidth); //(int)(.7 * triangleWidth);
	
	/** The buffer between the percent number and the up and down arrows */
	private int xBuffer = 3;
	
	/** The spacing between the triangle buttons */
	private int triangleYBuffer = 3;
	
	private String minLabel;
	
	private String maxLabel;
	
	private boolean hasFocus = false;
	private Color overColor = ProbabilityEntryConstants.COLOR_SPINNER_HAS_FOCUS;
	private Color origColor;
	
	/** The total amount of time spent interacting with the spinner */
	protected long spinnerInteractionTime = 0;
	
	protected long spinnerPressedStartTime = -1;
	
	/** The previous setting (not used or displayed) */
	protected Integer previousSetting;
	
	public JSettingSpinner(int minPercent, int maxPercent, int id) {
		this(new Setting(minPercent, maxPercent, 0), 45, true, id);
	}
	
	public JSettingSpinner(Setting setting, int id) {
		this(setting, 45, true, id);
	}
	
	public JSettingSpinner(Setting setting, int delayTime, int id) {
		this(setting, delayTime, true, id);
	}
	
	public JSettingSpinner(Setting setting, int delayTime, boolean editable, int id) {		
		// instantiate the setting object on this spinner
		this.setting = setting;		
		this.id = id;
		this.delayTime = delayTime;
		this.editable = editable;
		mousePressedOverState = JSettingSpinner.NONE;
		minLabel = Integer.toString(setting.getMinValue());
		maxLabel = Integer.toString(setting.getMaxValue());		
		listeners = new ArrayList<ActionListener>();
		
		setDoubleBuffered(true);
		setFont(ProbabilityEntryConstants.FONT_PROBABILITY);
		
		//Set the preferred size
		pack();
		
		updateSpinnerTask = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				if(getMousePressedOverState() == JSettingSpinner.DOWN_ARROW){
					decrementCurrentSetting();
				}
				else if(getMousePressedOverState() == JSettingSpinner.UP_ARROW){
					incrementCurrSetting();
				}
			}
		};		
		mouseDownTimer = new Timer(delayTime, updateSpinnerTask);
		mouseDownTimer.setInitialDelay(500);		
		
		mouseAdapter = new MouseAdapter() {			
			@Override
			public void mouseDragged(MouseEvent event) {				
				Point mouseLocation = new Point(event.getX(), event.getY());				
				if(!downButton.getBounds().contains(mouseLocation) &&
						!upButton.getBounds().contains(mouseLocation)) {
					if(spinnerPressedStartTime > 0) {
						spinnerInteractionTime += System.currentTimeMillis() - spinnerPressedStartTime;
						spinnerPressedStartTime = -1;
						//System.out.println("updated spinner time: " + spinnerInteractionTime);
					}
					mouseDownTimer.stop();
					setMousePressedOverState(JSettingSpinner.NONE);
					overUpArrow = false;
					overDownArrow = false;
					repaint();
				}				
			}			
			
			@Override
			public void mouseMoved(MouseEvent event) {				
				if(!hasFocus) {
					hasFocus = true;
					origColor = getBackground();
					setBackground(overColor);
				}
				if(downButton.getBounds().contains(new Point(event.getX(), event.getY()))){					
					overDownArrow = true;
					overUpArrow = false;
				}
				else if(upButton.getBounds().contains(new Point(event.getX(), event.getY()))){					
					overUpArrow = true;
					overDownArrow = false;
				}
				else {
					overUpArrow = false;
					overDownArrow = false;
				}
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {					
				if(hasFocus) {
					hasFocus = false;
					setBackground(origColor);
				}
				if(spinnerPressedStartTime > 0) {
					spinnerInteractionTime += System.currentTimeMillis() - spinnerPressedStartTime;
					spinnerPressedStartTime = -1;
					//System.out.println("updated spinner time: " + spinnerInteractionTime);
				}
				overUpArrow = false;
				overDownArrow = false;
				setMousePressedOverState(JSettingSpinner.NONE);
				repaint();
			}
			
			@Override
			public void mouseEntered(MouseEvent event) {				
				/*JSettingSpinner currSpinner = JSettingSpinner.this;
				currSpinner.origColor = JSettingSpinner.this.getBackground();
				currSpinner.setBackground(overColor);
				currSpinner.repaint();*/
			}

			@Override
			public void mousePressed(MouseEvent event) {					
				// find out if up arrow or down arrow is being clicked
				if(downButton.getBounds().contains(new Point(event.getX(), event.getY()))){					
					decrementCurrentSetting();
					setMousePressedOverState(JSettingSpinner.DOWN_ARROW);
					spinnerPressedStartTime = System.currentTimeMillis();
				} else if(upButton.getBounds().contains(new Point(event.getX(), event.getY()))){					
					incrementCurrSetting();
					setMousePressedOverState(JSettingSpinner.UP_ARROW);
					spinnerPressedStartTime = System.currentTimeMillis();
				} else {
					spinnerPressedStartTime = -1;
				}
				mouseDownTimer.restart();
			}

			@Override
			public void mouseReleased(MouseEvent event) {				
				// set the state to nothing being clicked
				setMousePressedOverState(JSettingSpinner.NONE);
				mouseDownTimer.stop();
				if(spinnerPressedStartTime > 0) {
					spinnerInteractionTime += System.currentTimeMillis() - spinnerPressedStartTime;
					spinnerPressedStartTime = -1;
					//System.out.println("updated spinner time: " + spinnerInteractionTime);
				}
				repaint();
			}
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				if(hasFocus) {					
					int wheelRotation = event.getWheelRotation();
					if(wheelRotation > 0) {
						decrementCurrentSetting();
					}
					if(wheelRotation < 0) {
						incrementCurrSetting();
					}
				}
			}			
		};
		
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		pack();
	}	

	@Override
	public Color getProbabilityEntryColor() {
		return getForeground();
	}

	@Override
	public void setProbabilityEntryColor(Color color) {
		setForeground(color);
	}

	protected void pack() {
		Insets insets = getInsets();
		FontMetrics fontMetrics = getFontMetrics(getFont());
		int height = insets.top + insets.bottom + fontMetrics.getAscent() + 4;
		
		if(editable) {
			int trianglesHeight = triangleHeight * 2 + triangleYBuffer;
			if(trianglesHeight > height) {
				height = trianglesHeight;
			}		
		}
		
		int width = insets.left + insets.right + 2 + SwingUtilities.computeStringWidth(fontMetrics, maxLabel);
		if(editable) {
			width += xBuffer + triangleWidth + 2;
		}
		setPreferredSize(new Dimension(width, 2 + height + 2));
	}
	
	public long getInteractionTime() {
		return spinnerInteractionTime;
	}
	
	public void resetInteractionTime() {
		spinnerInteractionTime = 0;
		spinnerPressedStartTime = -1;
	}
	
	public synchronized void addActionListener(ActionListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	public synchronized void removeActionListener(ActionListener listener){
		if(listeners.contains(listener)){
			listeners.remove(listener);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(getBackground());
		
		if(isHighlight()) {
			g2d.fill(this.getBounds());
		}
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
		Rectangle currBounds = getBounds();	
		Insets insets = getInsets();
		currBounds.width = currBounds.width - insets.left - insets.right;
		currBounds.height = currBounds.height - insets.top - insets.bottom;
		//g2d.setColor(this.getBackground());
		//g2d.fillRect(currBounds.x, currBounds.y, currBounds.width, currBounds.height);
		
		// set font here before calculating the bounds of the string
		g2d.setColor(Color.BLACK);
		g2d.setFont(getFont());
		Rectangle2D bnds = g2d.getFontMetrics().getStringBounds(maxLabel, g2d);
		Rectangle2D stringBounds = new Rectangle2D.Double(bnds.getX(), bnds.getY(),
				bnds.getWidth(), bnds.getHeight() - g2d.getFontMetrics().getDescent());
		
		int xLocation = 0;		
		if(editable){
			xLocation = insets.left + currBounds.width / 2 - (int)((stringBounds.getWidth() + this.triangleWidth + this.xBuffer) / 2);
		} else {
			xLocation = insets.left + currBounds.width / 2 - (int)(stringBounds.getWidth() / 2);
		}
		//System.out.println("X Location: " + xLocation);
		int yLocation = insets.top + currBounds.height / 2 + (int)((stringBounds.getHeight()) / 2);
		//System.out.println("Y Location: " + yLocation);
		//System.out.println("Descent: " + g2d.getFontMetrics().getDescent());
		
		String currentSettingStr = null;		
		if(setting.getDisplaySetting() <= setting.getMinValue()) {
			currentSettingStr = minLabel;
		} else if(setting.getDisplaySetting() >= setting.getMaxValue()) {
			currentSettingStr = maxLabel;
		} else {
			if(formatAsPercent) {
				//currentSettingStr = String.valueOf(setting.getIntValue()) + "%";
				currentSettingStr = String.valueOf(setting.getDisplaySetting()) + "%";
			} else {
				//currentSettingStr = String.valueOf(setting.getIntValue());
				currentSettingStr = String.valueOf(setting.getDisplaySetting());
			}
		}		
		
		int stringWidth = (int)(g2d.getFontMetrics().getStringBounds(currentSettingStr, g2d).getWidth());
		g2d.setColor(getForeground());
		
		g2d.drawString(currentSettingStr, xLocation, yLocation);
		if(underlineSetting) {
			g2d.drawLine(xLocation, yLocation + 2, xLocation + stringWidth, yLocation + 2);
			g2d.drawLine(xLocation, yLocation + 3, xLocation + stringWidth, yLocation + 3);
		}
		//}
		
		int triangleLocationX = xLocation + xBuffer + (int)stringBounds.getWidth();
		int triangleLocationY = yLocation - (int)(stringBounds.getHeight() / 2) + g2d.getFontMetrics().getDescent();
		
		int[] x = {triangleLocationX, triangleLocationX + triangleWidth, triangleLocationX + (triangleWidth - 1) / 2};
		int[] y = {triangleLocationY, triangleLocationY, triangleLocationY + triangleHeight};
		
		int points = 3;		
		downButton = new Polygon(x, y, points);		
		
		triangleLocationY = triangleLocationY - triangleYBuffer;
		int[] topY = {triangleLocationY, triangleLocationY, triangleLocationY - triangleHeight};		
		upButton = new Polygon(x, topY, points);
		
		if(editable){		
			if(this.getMousePressedOverState() != JSettingSpinner.DOWN_ARROW){
				g2d.setColor(ProbabilityEntryConstants.COLOR_SPINNER_UNSELECTED);
				g2d.fillPolygon(downButton);
			}
			
			if(this.getMousePressedOverState() != JSettingSpinner.UP_ARROW) {
				g2d.setColor(ProbabilityEntryConstants.COLOR_SPINNER_UNSELECTED);
				g2d.fillPolygon(upButton);
			}
			else {
				g2d.setColor(ProbabilityEntryConstants.COLOR_SPINNER_SELECTED);
				g2d.fillPolygon(upButton);
				g2d.setColor(Color.BLACK);
				g2d.drawPolygon(upButton);
			}
			
			if(overDownArrow){			
				g2d.setColor(ProbabilityEntryConstants.COLOR_SPINNER_MOUSEOVER);
				g2d.fillPolygon(downButton);
				g2d.setColor(Color.BLACK);
				g2d.drawPolygon(downButton);
				
				if(this.getMousePressedOverState() == JSettingSpinner.DOWN_ARROW){
					g2d.setColor(Color.BLACK);
					g2d.drawPolygon(downButton);
				}
			}
			else if(overUpArrow){
				g2d.setColor(ProbabilityEntryConstants.COLOR_SPINNER_MOUSEOVER);
				g2d.fillPolygon(upButton);
				g2d.setColor(Color.BLACK);
				g2d.drawPolygon(upButton);
				
				if(this.getMousePressedOverState() == JSettingSpinner.UP_ARROW){
					g2d.setColor(Color.BLACK);
					g2d.drawPolygon(upButton);
				}
			}
		}		
	}
	
	protected void incrementCurrSetting() {
		//currSetting++;
		//setting.setIntValue(setting.getIntValue() + 1);
		int requestedValue = setting.getIntValue() + 1;
		
		// the controller should exist and the requested change should only happen if
		// the value being requested fits into the appropriate range
		if(controller != null && requestedValue >= setting.getMinValue() && requestedValue <= setting.getMaxValue()) {
			controller.updateSettingRequest(this.getId(), requestedValue);
		}		
		if(setting.getIntValue() > setting.getMaxValue()){
			setting.setIntValue(setting.getMaxValue());
		}
		else {
			repaint();
			ActionEvent event = new ActionEvent(this, 0, "");
			fireEvent(event);
		}		
	}
	
	protected void decrementCurrentSetting() {		
		int requestedValue = setting.getIntValue() - 1;
		
		// the controller should exist and the requested change should only happen if
		// the value being requested fits into the appropriate range
		if(controller != null && requestedValue >= setting.getMinValue() && requestedValue <= setting.getMaxValue()) {
			controller.updateSettingRequest(this.getId(), requestedValue);
		}
		else {
			setting.setIntValue(setting.getIntValue() - 1);
		}
		
		//setting.setIntValue(setting.getIntValue() - 1);
		if(setting.getIntValue() < setting.getMinValue()){
			setting.setIntValue(setting.getMinValue());
		}
		else {
			repaint();
			ActionEvent event = new ActionEvent(this, 0, "");
			fireEvent(event);
		}		
	}
	
	protected synchronized void fireEvent(ActionEvent event){
		for(ActionListener currListener : listeners){
			currListener.actionPerformed(event);
		}
	}	

	public String getMinLabel() {
		return minLabel;
	}

	public void setMinLabel(String minLabel) {
		this.minLabel = minLabel;
	}

	public String getMaxLabel() {
		return maxLabel;
	}

	public void setMaxLabel(String maxLabel) {
		this.maxLabel = maxLabel;
		pack();
		repaint();
	}	

	public Polygon getUpButton() {
		return upButton;
	}

	public void setUpButton(Polygon upButton) {
		this.upButton = upButton;
	}

	public Polygon getDownButton() {
		return downButton;
	}

	public void setDownButton(Polygon downButton) {
		this.downButton = downButton;
	}

	public int getMousePressedOverState() {
		return mousePressedOverState;
	}

	public void setMousePressedOverState(int mousePressedOverState) {
		this.mousePressedOverState = mousePressedOverState;
	}
	
	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		if(editable != this.editable) {
			this.removeMouseListener(mouseAdapter);
			this.removeMouseMotionListener(mouseAdapter);
			if(editable) {
				this.addMouseListener(mouseAdapter);
				this.addMouseMotionListener(mouseAdapter);
			}
			this.editable = editable;
			//pack();
			this.repaint();
		}
	}

	public boolean isFormatAsPercent() {
		return formatAsPercent;
	}

	public void setFormatAsPercent(boolean formatAsPercent) {
		this.formatAsPercent = formatAsPercent;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new JSettingSpinner(new Setting(0, 100, 50), 50, false, 0));
		f.pack();
		f.setVisible(true);		
	}

	@Override
	public void setProbabilityController(IProbabilityController controller) {
		this.controller = controller;
	}

	/*@Override
	public Setting getSetting() {
		return this.setting;
	}*/
	
	@Override
	public Integer getMaxValue() {
		return setting.getMaxValue();
	}

	@Override
	public void setMaxValue(Integer value) {
		setting.setMaxValue(value);
	}

	@Override
	public boolean isLocked() {
		return setting.isLocked();
	}

	@Override
	public void setLocked(boolean locked) {
		setting.setLocked(locked);
	}

	@Override
	public void setFocused(boolean focused) {
		setting.setCurrent(focused);
	}

	@Override
	public boolean isFocused() {
		return setting.isCurrent();
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	public int getMinSetting() {
		return setting.getMinValue();
	}

	public void setMinSetting(int minSetting) {
		this.setting.setMinValue(minSetting);
	}

	public int getMaxSetting() {
		return this.setting.getMaxValue();
	}

	public void setMaxSetting(int maxSetting) {
		this.setting.setMaxValue(maxSetting);
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
		repaint();
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
		repaint();
	}
	
	/**
	 * @return the highlight
	 */
	public boolean isHighlight() {
		return highlight;
	}

	/**
	 * @param highlight the highlight to set
	 */
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.gui.probability_entry.IProbabilityEntryContainer#getComponent()
	 */
	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public void setEnableLocking(boolean enableLocking) {
		//Does nothing, locking not supported		
	}

	@Override
	public boolean isEnableLocking() {
		return false;
	}

	@Override
	public boolean isDisplayedValueValid() {
		return setting.getDisplaySetting() >= setting.getMinValue() && 
				setting.getDisplaySetting() <= setting.getMaxValue();
	}	
}
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.mitre.icarus.cps.app.widgets.probability_entry.ProbabilityEntryConstants;
import org.mitre.icarus.cps.app.widgets.probability_entry.Setting;

/**
 * A box that shows a current setting and previous setting.
 * 
 * @author CBONACETO
 * 
 */
public class SettingsBox extends Box {
	private static final long serialVersionUID = 1L;
	
	/** The setting data */
	protected Setting setting;		
	
	/** Whether to show the current setting */
	protected boolean showCurrentSetting;
	
	/** The previous setting */
	protected Integer previousSetting = 0;
	
	/** Whether to show the previous setting (now shown as a dashed line, previously shown as a shaded rectangle) */
	protected boolean showPreviousSetting;
	
	/** Whether to fill the current setting as a box in addition to drawing
	 *  a line at the current setting */
	protected boolean fillCurrentSetting = true;
	
	/** Current setting line color */
	protected Color currentSettingLineColor = ProbabilityEntryConstants.COLOR_CURRENT_SETTING_LINE;
	
	/** Current setting line color when disabled */
	protected Color currentSettingLineColorDisabled = ProbabilityEntryConstants.COLOR_CURRENT_SETTING_LINE_DISABLED;
	
	/** Whether to show the "draggable" version of the current setting line */
	protected boolean currentSettingLineDraggable = false;
	
	/** Current setting fill color */
	protected Color currentSettingFillColor = ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL;
	
	/** Current setting fill color when disabled */
	protected Color currentSettingFillColorDisabled = ProbabilityEntryConstants.COLOR_CURRENT_SETTING_FILL_DISABLED;
	
	/** The previous setting line color */	
	protected Color lastSettingDashColor = ProbabilityEntryConstants.COLOR_PREVIOUS_SETTING;
	
	/** The previous setting line stroke */
	protected Stroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, 
			BasicStroke.JOIN_BEVEL, 0, new float[]{7}, 0);
	
	/** The setting at the mouse's current location. If not null, displayed at point mouseLocation. */
	protected Integer settingAtMouseLocation = null;
	
	/** The mouse's current location */
	protected Point mouseLocation;
	
	public SettingsBox(Dimension boxSize, Setting setting) {
		this(boxSize, setting, true, true);
	}
	
	public SettingsBox(Dimension boxSize, Setting setting,
			boolean showPreviousSetting, boolean showCurrentSetting) {
		super(boxSize);
		setDoubleBuffered(true);
		
		this.showPreviousSetting = showPreviousSetting;
		this.showCurrentSetting = showCurrentSetting;
		this.setting = setting;
		//this.minSetting = minSetting;
		//this.maxSetting = maxSetting;
	}	

	public Integer getPreviousSetting() {
		return previousSetting;
	}
	
	public void setPreviousSetting(Integer previousSetting) {
		this.previousSetting = previousSetting;
		if(showPreviousSetting)
			repaint();
	}

	public boolean isShowPreviousSetting() {
		return showPreviousSetting;
	}

	public void setShowPreviousSetting(boolean showPreviousSetting) {
		this.showPreviousSetting = showPreviousSetting;
		repaint();
	}

	/*public Integer getCurrentSetting() {
		//return currentSetting;
		return (int)Math.round(this.setting.getIntValue());
	}*/

	public void setCurrentSetting(Integer currentSetting) {
		setting.setCurrentSetting(currentSetting);
		if(showCurrentSetting) { 
			repaint();
		}
	}

	public boolean isShowCurrentSetting() {
		return showCurrentSetting;
	}

	public void setShowCurrentSetting(boolean showCurrentSetting) {
		this.showCurrentSetting = showCurrentSetting;
		repaint();
	}

	public Color getCurrentSettingLineColor() {
		return currentSettingLineColor;
	}

	public void setCurrentSettingLineColor(Color currentSettingLineColor) {
		this.currentSettingLineColor = currentSettingLineColor;
		repaint();
	}

	public Color getCurrentSettingLineColorDisabled() {
		return currentSettingLineColorDisabled;
	}

	public void setCurrentSettingLineColorDisabled(Color currentSettingLineColorDisabled) {
		this.currentSettingLineColorDisabled = currentSettingLineColorDisabled;
		if(!enabled) {
			repaint();
		}
	}

	public boolean isCurrentSettingLineDraggable() {
		return currentSettingLineDraggable;
	}

	public void setCurrentSettingLineDraggable(boolean currentSettingLineDraggable) {
		this.currentSettingLineDraggable = currentSettingLineDraggable;
		repaint();
	}

	public Color getCurrentSettingFillColor() {
		return currentSettingFillColor;
	}

	public void setCurrentSettingFillColor(Color currentSettingFillColor) {
		this.currentSettingFillColor = currentSettingFillColor;
		repaint();
	}		
	
	public Color getCurrentSettingFillColorDisabled() {
		return currentSettingFillColorDisabled;
	}

	public void setCurrentSettingFillColorDisabled(Color currentSettingFillColorDisabled) {
		this.currentSettingFillColorDisabled = currentSettingFillColorDisabled;
		if(!enabled) {
			repaint();
		}
	}

	public boolean isFillCurrentSetting() {
		return fillCurrentSetting;
	}

	public void setFillCurrentSetting(boolean fillCurrentSetting) {
		this.fillCurrentSetting = fillCurrentSetting;
		repaint();
	}

	public Color getLastSettingDashColor() {
		return lastSettingDashColor;
	}

	public void setLastSettingFillColor(Color lastSettingDashColor) {
		this.lastSettingDashColor = lastSettingDashColor;
		repaint();
	}

	/**
	 * @param lastSettingDashColor the lastSettingDashColor to set
	 */
	public void setLastSettingDashColor(Color lastSettingDashColor) {
		this.lastSettingDashColor = lastSettingDashColor;
		repaint();
	}	

	@Override
	protected void paintComponent(Graphics g) {		
		Graphics2D g2d = (Graphics2D)g;
		Rectangle bounds = getBounds();
		float pixelsPerPercent = ((float)bounds.height)/(setting.getMaxValue() - setting.getMinValue());		
		
		Stroke normalStroke = g2d.getStroke();		
		if(boxBorderThickness > 0) {
			g2d.setStroke(new BasicStroke(boxBorderThickness));
		}
		
		//Draw the box background
		if(enabled) {
			g2d.setColor(boxFillColor);
		}
		else {
			g2d.setColor(boxFillColorDisabled);
		}
		g2d.fillRect(0, 0, bounds.width, bounds.height);		
		
		//if(showPreviousSetting && previousSetting != null && previousSetting > 0) {
			//Draw shaded box indicating last setting
		//	g2d.setColor(lastSettingFillColor);
		//	int height = (int)(pixelsPerPercent * previousSetting);
		//	g2d.fillRect(0, bounds.height - height, bounds.width, height);
		//}
		
		int yPos = 0;
		if(showCurrentSetting && setting.getDisplaySetting() != null /* && setting.getDisplaySetting() > 0 */) {			
			int height = (int)(pixelsPerPercent * setting.getDisplaySetting()) + boxBorderThickness;		
			yPos = bounds.height - height;
			if(yPos >= bounds.height) {
				yPos = bounds.height - 1;
			}
			//System.out.println(pixelsPerPercent + ", " + yPos + ", " + height);

			//Draw box indicating current setting
			if(this.fillCurrentSetting) {
				if(enabled) {
					g2d.setColor(currentSettingFillColor);
				}
				else {
					g2d.setColor(currentSettingFillColorDisabled);
				}
				g2d.fillRect(0, yPos, bounds.width, height);
			}
			//else {			
			//Draw line indicating current setting	
			//g2d.drawLine(0, yPos, bounds.width, yPos);
			//}
			
			//fill the bar a certain color
			if(fillCurrentSetting) {
				Rectangle rect = new Rectangle(0, yPos, bounds.width, bounds.height - yPos);
				if(enabled) {
					g2d.setColor(currentSettingFillColor);
				}
				else {
					g2d.setColor(currentSettingFillColorDisabled);
				}
				g2d.fill(rect);
			}
		}

		//Draw line indicating current setting
		if(showCurrentSetting && setting.getDisplaySetting() != null) {			
			if(enabled) {
				g2d.setColor(currentSettingLineColor);
			}
			else {
				g2d.setColor(currentSettingLineColorDisabled);
			}					
			
			if(currentSettingLineDraggable && enabled) {
				//Draw the "draggable" version of the current setting line
				//Stroke origStroke = g2d.getStroke();
				//g2d.setStroke(normalStroke);
				int lineWidth = 3; //8 //4
				Rectangle2D currRect = new Rectangle2D.Double(0, yPos - lineWidth/2, 
						bounds. width, lineWidth);
				g2d.setColor(currentSettingLineColor);
				//g2d.setColor(Color.GRAY);
				g2d.fill(currRect);
				//g2d.setColor(currentSettingLineColor);
				//g2d.drawLine(0, yPos, bounds.width, yPos);
				//g2d.draw(currRect);
				//g2d.setStroke(origStroke);
			}
			else {
				//Draw the standard current setting line
				g2d.drawLine(0, yPos, bounds.width, yPos);
			}
		}
		
		//Draw dashed line indicating previous setting
		if(enabled && showPreviousSetting && previousSetting != null && previousSetting > 0) {
			g2d.setColor(lastSettingDashColor);
			int height = (int)(pixelsPerPercent * previousSetting) + boxBorderThickness;
			//g2d.fillRect(0, bounds.height - height, bounds.width, height); //Previously drew a shaded box			
			Stroke origStroke = g2d.getStroke();
			g2d.setStroke(dashedStroke);			
			g2d.drawLine(0, bounds.height - height, bounds.width, bounds.height - height);
			g2d.setStroke(origStroke);
		}		
		
		//Draw the border
		if(boxBorderThickness > 0) {
			if(enabled) {
				g2d.setColor(boxBorderColor);
			}
			else {
				g2d.setColor(boxBorderColorDisabled);
			}
			int halfBorderThickness = boxBorderThickness/2;
			g2d.drawRect(halfBorderThickness, halfBorderThickness, 
					bounds.width - boxBorderThickness, bounds.height - boxBorderThickness);
		}
		
		//Display settingAtMouseLocation
		if(settingAtMouseLocation != null && mouseLocation != null) {
			String percentStr = settingAtMouseLocation.toString() + "%";
			Rectangle2D strBounds = g2d.getFontMetrics().getStringBounds(percentStr, g2d); 
			g2d.setColor(Color.black);
			g2d.setStroke(normalStroke);
			int padding = 2;
			g2d.setColor(Color.WHITE);
			int rectY = mouseLocation.y-(int)strBounds.getHeight() - padding;
			if(rectY < 0) {rectY = 0;}
			g2d.fillRect(mouseLocation.x - padding, rectY, 
					(int)strBounds.getWidth() + padding * 2, (int)strBounds.getHeight() + padding);
			g2d.setColor(Color.GRAY);
			g2d.drawRect(mouseLocation.x - padding, rectY, 
					(int)strBounds.getWidth() + padding * 2, (int)strBounds.getHeight() + padding);
			g2d.setColor(Color.BLACK);
			g2d.drawString(settingAtMouseLocation.toString() + "%", 
					mouseLocation.x + padding, rectY + (float)strBounds.getHeight());
		}
	}
}
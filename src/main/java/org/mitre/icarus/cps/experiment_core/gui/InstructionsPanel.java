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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.mitre.icarus.cps.app.widgets.WidgetConstants;
import org.mitre.icarus.cps.experiment_core.condition.InstructionsPage;

/**
 * Component for displaying instructions as images and as html/text.
 * 
 * @author CBONACETO
 *
 */
public class InstructionsPanel extends JPanelConditionComponent implements IInstructionsPanel {
	
	private static final long serialVersionUID = 1L;	
	
	/** The layout */
	protected GridBagLayout gbl;

	/** Editor pane containing html/text to render */
	protected JEditorPane editorPane; 
	
	/** JLabel contains the instruction image to render if using a single image instead of text */
	protected JLabel imagePane;
	protected Image instructionsImage;
	protected ImageIcon instructionsImageIcon;
	protected boolean instructionsImageScaled = false;
	
	/** The instructions widget */
	protected JComponent widget;
	
	/** Panel containing the instructions widget */
	protected JPanel widgetPanel;
	
	/** The instructions page currently being displayed */
	private InstructionsPage page;
	
	/** Listeners registered to receive action commands */
	protected List<HyperlinkListener> hyperLinkListeners;
	
	protected int topInset = 8;
	
	public InstructionsPanel(String componentId) {
		super(componentId);		
		setLayout(gbl = new GridBagLayout());
		hyperLinkListeners = Collections.synchronizedList(new LinkedList<HyperlinkListener>());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;		
		editorPane = new JEditorPane();
		editorPane.setContentType("text/html");
		editorPane.setFont(WidgetConstants.FONT_INSTRUCTION_BANNER);
		editorPane.setEditable(false);
		gbc.insets.top = topInset;
		add(editorPane, gbc);
		
		imagePane = new JLabel();
		imagePane.setIconTextGap(0);
		imagePane.setHorizontalAlignment(JLabel.CENTER);
		imagePane.setVerticalAlignment(JLabel.NORTH);
		//imagePane.setBackground(getBackground());	
		imagePane.setOpaque(true);
		gbc.gridy++;
		add(imagePane, gbc);
		imagePane.setVisible(false);
		
		widgetPanel = new JPanel(new BorderLayout());		
		widgetPanel.setVisible(false);
		gbc.gridy++;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(widgetPanel, gbc);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(page != null && page.getPageClickHyperlink() != null && hyperLinkListeners != null) {
					synchronized(hyperLinkListeners) {
						for(HyperlinkListener listener : hyperLinkListeners) {
							listener.hyperlinkUpdate(new HyperlinkEvent(InstructionsPanel.this, 
									HyperlinkEvent.EventType.ACTIVATED, null, page.getPageClickHyperlink()));
						}
					}
				}
			}			
		});
		
		//Add resize listener to scale instruction image (if any) to fit the panel if the image is too large
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				scaleInstructionsImageToFit();
			}
		});
	}

	/** Add a hyperlink listener */
	@Override
	public void addHyperlinkListener(HyperlinkListener listener) {
		synchronized(hyperLinkListeners) {
			hyperLinkListeners.add(listener);
		}
		editorPane.addHyperlinkListener(listener);
	}
	
	/** Remove a hyperlink listener */
	@Override
	public void removeHyperlinkListener(HyperlinkListener listener) {
		synchronized(hyperLinkListeners) {
			hyperLinkListeners.remove(listener);
		}
		editorPane.removeHyperlinkListener(listener);	
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(editorPane != null && imagePane != null) {
			editorPane.setBackground(bg);
			imagePane.setBackground(bg);
		}
	}
	
	@Override
	public void setInstructionsPage(InstructionsPage page) {
		this.page = page;
		if(page == null) {
			setInstructionText("");
		} else {
			GridBagConstraints widgetPanelConstraints = gbl.getConstraints(widgetPanel);
			if(page.getInstructionsWidget() != null) {
				setInstructionsWidget(page.getInstructionsWidget());
				widgetPanelConstraints.weighty = 1;
				widgetPanelConstraints.anchor = GridBagConstraints.NORTH;
			}
			if(page.getPageImage() != null) {
				setInstructionsImage(page.getPageImage());
				widgetPanelConstraints.weighty = 0;
				widgetPanelConstraints.anchor = GridBagConstraints.SOUTH;
			} else if(page.getPageText() != null){
				setInstructionText(page.getPageText());
				widgetPanelConstraints.weighty = 0;
				widgetPanelConstraints.anchor = GridBagConstraints.SOUTH;
			} else if(page.getPageURL() != null) {
				setInstructionsURL(page.getPageURL());
				widgetPanelConstraints.weighty = 0;
				widgetPanelConstraints.anchor = GridBagConstraints.SOUTH;
			} else {
				editorPane.setVisible(false);
				imagePane.setVisible(false);
			}
			gbl.setConstraints(widgetPanel, widgetPanelConstraints);
			if(widgetPanel.isVisible()) {
				revalidate();
				repaint();
			}
			/*if(page.getPageClickHyperlink() != null) {
				//TODO: Change cursor
			} else {
				
			}*/
		}
	}	
	
	public static String formatTextAsHTML(String text) {
		return formatTextAsHTML(text, "center", WidgetConstants.FONT_INSTRUCTION_PANEL.getName(), 
				WidgetConstants.FONT_SIZE_HTML);
	}
	
	public static String formatTextAsHTML(String text, String orientation, String fontName, int fontSize) {
		StringBuilder html = new StringBuilder("<html><br><br>");		
		html.append("<font face=\"");
		html.append(fontName + "\" size=\"");
		html.append(Integer.toString(fontSize) + "\">");		
		html.append("<" + orientation + ">");		
		html.append(text);
		html.append("</" + orientation + ">");		
		html.append("</font></html>");
		return html.toString();
	}	

	@Override
	public void setInstructionText(String instructions) {
		editorPane.setText(instructions);
		if(!editorPane.isVisible()) {
			editorPane.setVisible(true);
			imagePane.setVisible(false);
			revalidate();
			repaint();
		}
	}
	
	@Override
	public void setInstructionsImage(Image instructionsImage) {
		if(instructionsImageScaled && instructionsImageIcon != null && instructionsImageIcon.getImage() != null) {
			instructionsImageIcon.getImage().flush();
		}
		this.instructionsImage = instructionsImage;		
		if(!imagePane.isVisible()) {
			imagePane.setVisible(true);			
			editorPane.setVisible(false);
			revalidate();
			repaint();
		}	
		instructionsImageScaled = false;
		if(instructionsImageIcon == null) {instructionsImageIcon = new ImageIcon();}
		instructionsImageIcon.setImage(instructionsImage);		
		imagePane.setIcon(instructionsImageIcon);
		revalidate();
		repaint();
		//Scale the image to fit the image panel if necessary
		scaleInstructionsImageToFit();		
	}	
	
	protected void scaleInstructionsImageToFit() {
		if(instructionsImage != null && imagePane.isVisible()) {
			//System.out.println("Panel size: " + getSize());
			Dimension size = getSize();
			if(size.width > 0 && size.height > 0) {
				Insets insets = getInsets();
				int width = size.width - insets.left - insets.right;
				int height = size.height - insets.top - insets.bottom - topInset;
				int imageWidth = instructionsImage.getWidth(null);
				int imageHeight = instructionsImage.getHeight(null);
				if(imageWidth > width || imageHeight > height) {
					instructionsImageScaled = true;
					//System.out.println("Rescaled image");
					//System.out.println("Old size: (" + imageWidth + "," + imageHeight + ")");
					if(imageWidth > width) {
						imageHeight = (int)((float)width/imageWidth * imageHeight);
						imageWidth = width;
						if(imageHeight > height) {
							imageWidth = (int)((float)height/imageHeight * imageWidth);
							imageHeight = height;
						} 
					} else {
						imageWidth = (int)((float)height/imageHeight * imageWidth);
						imageHeight = height;
						if(imageWidth > width) {
							imageHeight = (int)((float)width/imageWidth * imageHeight);
							imageWidth = width;
						} 
					}
					//System.out.println("New size: (" + imageWidth + "," + imageHeight + ")");
					instructionsImageIcon.setImage(instructionsImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
					imagePane.setIcon(instructionsImageIcon);
					revalidate();
					repaint();
				} else if(instructionsImageScaled) {
					//Restore the image at its original size
					//System.out.println("image size restored");
					instructionsImageIcon.setImage(instructionsImage);
					imagePane.setIcon(instructionsImageIcon);
					instructionsImageScaled = false;
					revalidate();
					repaint();
				}
			}
		}
	}
	
	@Override
	public void setInstructionsURL(String instructionsUrl) {
		try {
			editorPane.setPage(instructionsUrl);
		} catch(IOException ex) {
			System.err.println("Warning: Instruction page not found: " + instructionsUrl);
		}
		if(!editorPane.isVisible()) {
			editorPane.setVisible(true);
			imagePane.setVisible(false);
			revalidate();
			repaint();
		}
	}
	
	@Override
	public void setInstructionsURL(URL instructionsUrl) {
		try {
			editorPane.setPage(instructionsUrl);
		} catch(IOException ex) {
			System.err.println("Warning: Instruction page not found: " + instructionsUrl);
		}
	}
	
	public JComponent getInstructionsWidget() {
		return widget;
	}

	@Override
	public void setInstructionsWidget(JComponent widget) {
		if(this.widget != widget) {
			widgetPanel.removeAll();
			this.widget = widget;
			if(widget != null) {
				widgetPanel.add(widget);
				if(!widgetPanel.isVisible()) {
					widgetPanel.setVisible(true);
				}
			} else {
				widgetPanel.setVisible(false);
			}
			revalidate();
			repaint();
		}
	}
}
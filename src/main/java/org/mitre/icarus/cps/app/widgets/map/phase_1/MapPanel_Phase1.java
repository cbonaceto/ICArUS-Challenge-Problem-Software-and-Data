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
package org.mitre.icarus.cps.app.widgets.map.phase_1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mitre.icarus.cps.app.widgets.map.LayerTreePanel;
import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.objects.IMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.editors.IMapObjectEditor;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.AnnotationLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.IEditableLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ILayer_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.PlacemarkLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.RegionLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.RoadLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.layers.ShapeLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.AbstractMapObject_Phase1;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.CircleShape;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.GroupCenterPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IAnnotatableMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.PlacemarkMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.RegionMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.RoadMapObject;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.ScaleBar;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.ToolTip;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.IAnnotationMapObject.AnnotationOrientation;
import org.mitre.icarus.cps.app.widgets.map.phase_1.objects.PlacemarkMapObject.PlacemarkShape;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.SocintPolygonCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.parser.RoadCsvParser;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridSize;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupCenter;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;
import org.mitre.icarus.cps.feature_vector.phase_1.Road;
import org.mitre.icarus.cps.feature_vector.phase_1.SocintPolygon;

/**
 * The GUI for the CPD Phase 1 map.
 * 
 * @author CBONACETO
 *
 */
public class MapPanel_Phase1 extends JPanel implements IMapPanel_Phase1 {
	
	private static final long serialVersionUID = 1L;	
	
	/** The map model */
	protected MapModel mapModel;
	
	/** The grid size */
	protected GridSize gridSize;
	
	/** The render properties */
	protected RenderProperties renderData;
	
	/** Whether the render properties changed since the last time the map was rendered */
	private boolean renderPropertiesChanged = true;
	
	/** The timer for tooltip events */
	//private Timer toolTipTimer = null;
	
	/** The currently displaying tooltip */
	private ToolTip currentToolTip = null;
	
	/** The map object the current tool tip is for */
	protected IAnnotatableMapObject currentToolTipObject;
	
	/** says whether or not to disable tool tips */
	//private boolean toolTipsVisible = true;
	
	/** The map object the mouse is currently over */
	private AbstractMapObject_Phase1 highlightedObject = null;
	
	/** Whether the the custom mouse pointer is set */
	private boolean customCursorSet = false;
	
	/** Whether the cursor is locked (cannot be change until restoreCursor is called) */
	private boolean cursorLocked = false;
	
	/** The current map object editor for the map object being edited */
	private IMapObjectEditor<? extends AbstractMapObject_Phase1> editor;
	
	/** The map border color */
	protected Color mapBorderColor = Color.LIGHT_GRAY;
	
	/** The map border line thickness */
	protected int mapBorderLineWidth = 0;
	
	/**	Whether to always show the map border */
	protected boolean alwaysShowMapBorder = false;
	
	/** Whether to show the map border. If alwaysShowMapBorder is false,
	 * this flag will be true when the width and height of the panel are different 
	 * (the map will be a square centered in the panel) */
	protected boolean showMapBorder = false;
	
	/** Whether to show the map scale bar */
	protected boolean showScale = false;
	
	/** The map scale bar */
	protected ScaleBar scaleBar;
	
	public MapPanel_Phase1(GridSize gridSize) {
		this(null, gridSize);
	}
	
	public MapPanel_Phase1(MapModel mapModel, GridSize gridSize) {
		this.mapModel = mapModel;
		this.gridSize = gridSize;
		this.setLayout(null);
		
		renderData = new RenderProperties(gridSize.getGridWidth(), gridSize.getGridHeight(), gridSize.getMilesPerGridUnit());
		setBackground(Color.white);
		
		//Add resize listener to update render properties
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				updateRenderBounds();
				repaint();
			}
		});
		
		//Add mouse listener to show tool tips
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Point2D mousePoint = renderData.translateMouseToPixel(event.getPoint());
				
				// don't let the tool tip disappear if the user clicks on it.
				if(currentToolTip != null) {
					if(currentToolTip.contains(mousePoint)) {
						return;
					}
				}
				
				if(getMapModel() != null && getMapModel().getLayers() != null) {					
					IAnnotatableMapObject mouseOverObject = null;					
					for(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer : getMapModel().getLayers()) {
						for(AbstractMapObject_Phase1 mapObject : layer.getMapObjects()) {
							if(mapObject.contains(mousePoint) && mapObject instanceof IAnnotatableMapObject) {
								if(layer.isVisible()) {
									mouseOverObject = (IAnnotatableMapObject)mapObject;
									break;
								}
							}
						}
					}					
					if(mouseOverObject != null) {
						ToolTip toolTip = mouseOverObject.createToolTip();						
						currentToolTip = toolTip;						
						if(currentToolTipObject == mouseOverObject) {
							currentToolTip = null;
							currentToolTipObject = null;
							redraw();
						} else if(currentToolTip != null) {
							currentToolTipObject = mouseOverObject;
							redraw();
						}
					} else {
						currentToolTip = null;
						currentToolTipObject = null;
						redraw();
					}
				}
			}			
		});
		
		//Add mouse motion listener to determine which object the mouse is over and possibly update the cursor
		addMouseMotionListener(new MouseMotionAdapter() {			
			@SuppressWarnings("unchecked")
			@Override
			public void mouseMoved(MouseEvent event) {				
				if(getMapModel() != null && getMapModel().getLayers() != null) {
					Point2D mousePoint = renderData.translateMouseToPixel(event.getX(), event.getY());
					
					// iterate over the layers backwards to find the first item a mouse is over
					ArrayList<ILayer<? extends AbstractMapObject_Phase1>> layersList = 
						new ArrayList<ILayer<? extends AbstractMapObject_Phase1>>(getMapModel().getLayers());
					
					ListIterator<ILayer<? extends AbstractMapObject_Phase1>> iter = 
						layersList.listIterator(getMapModel().getLayers().size());
					
					AbstractMapObject_Phase1 mouseOverObject = null;
					
					while(iter.hasPrevious()) {
						ILayer<? extends AbstractMapObject_Phase1> currLayer = iter.previous();
						if(currLayer.isSelectable() && currLayer.isVisible()) {
							if(currLayer.getFirstObjectAtLocation(mousePoint) != null) {
								// set the previous mouse object to not have the mouse over it
								if(highlightedObject != null) {
									highlightedObject.setMouseOverState(false, event);
								}
								mouseOverObject = currLayer.getFirstObjectAtLocation(mousePoint); 
								mouseOverObject.setMouseOverState(true, event);
								highlightedObject = mouseOverObject;
								
								if(currLayer instanceof IEditableLayer<?>) {
									IEditableLayer<AbstractMapObject_Phase1> editableLayer = 
											(IEditableLayer<AbstractMapObject_Phase1>)currLayer;									
									if(editableLayer.isEditable() && mouseOverObject.isEditable()) {
										IMapObjectEditor<? extends AbstractMapObject_Phase1> currEditor = 
												editableLayer.editObject(mouseOverObject);									
										if(currEditor != null) {
											if(editor != null) {
												editor.doneEditingMapObject();
											}
											editor = currEditor;
										}
									}
								}								
								break;			
							}
						}
					}
					if(mouseOverObject != null) {
						if(mouseOverObject.isSelectable() &&
								(editor == null || !editor.isEditorArmed())) {
								//(editor == null || mouseOverObject != editor.getEditingMapObject())) {
							//should only show hand if object has a tool tip
							setCursor(CursorType.HAND, false);
						}
					}
					else {
						if(editor != null) {
							editor.doneEditingMapObject();
							editor = null;
						}
						if(highlightedObject != null) {
							highlightedObject.setMouseOverState(false, event);
							highlightedObject = null;
						}
						restoreCursor();
					}
					redraw();
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent event) {}
		});
	}	
	
	protected void updateRenderBounds() {
		Insets insets = getInsets();
		int width = getWidth() - insets.left - insets.right - 2;
		int height = getHeight() - insets.top - insets.bottom - 2;
		
		renderData.setRenderBounds(insets.left+1, insets.top+1, width, height);
		showMapBorder = (width != height);		
		//System.out.println("render bounds: " + renderData.getRenderBounds());
		//System.out.println("grid: (0, 99), pixel: " + renderData.translateToPixel(new GridLocation2D(0, 99)) + ", pgu: " + renderData.getPixelsPerGridUnit());
		//System.out.println("grid: (0, 0), pixel: " + renderData.translateToPixel(new GridLocation2D(0, 0)) + ", pgu: " + renderData.getPixelsPerGridUnit());
		
		renderPropertiesChanged = true;
	}
	
	@Override
	public MapModel getMapModel() {
		return mapModel;
	}

	@Override
	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}	

	@Override
	public GridSize getGridSize() {
		return gridSize;
	}

	@Override
	public void setGridSize(GridSize gridSize) {
		this.gridSize = gridSize;
		Rectangle2D.Double renderBounds = renderData.renderBounds;		
		renderData = new RenderProperties(gridSize.getGridWidth(), gridSize.getGridHeight(), 
				gridSize.getMilesPerGridUnit(), renderBounds);
		updateRenderBounds();
		repaint();
	}
	
	public GridLocation2D getCenterGridLocation() {
		return renderData.getCenterGridLocation();
	}

	@Override
	public double translateToPixels(double gridUnits) {
		return renderData.translateToPixels(gridUnits);
	}
	
	@Override
	public double translateToGridUnits(double pixels) {
		return renderData.translateToGridUnits(pixels);
	}
	
	@Override
	public Point2D translateMouseToPixel(int mouseX, int mouseY) {
		return renderData.translateMouseToPixel(mouseX, mouseY);
	}
	
	@Override
	public void translateMouseToPixel(Point mousePoint) {
		renderData.translateMouseToPixel(mousePoint);
	}

	@Override
	public Point2D getPixelLocation(GridLocation2D gridLocation) {
		return renderData.translateToPixel(gridLocation);
	}

	@Override
	public Point2D getPixelLocation(double gridX, double gridY) {
		return renderData.translateToPixel(gridX, gridY);
	}

	@Override
	public GridLocation2D getGridLocation(Point2D pixelLocation) {
		return renderData.translateToGrid(pixelLocation.getX(), pixelLocation.getY());
	}
	
	public GridLocation2D getGridLocation(Point mouseLocation) {
		return renderData.translateToGrid(mouseLocation);
	}

	@Override
	public GridLocation2D getGridLocation(double pixelX, double pixelY) {
		return renderData.translateToGrid(pixelX, pixelY);
	}

	@Override
	public void setCursor(CursorType cursorType) {
		setCursor(cursorType, true);
	}
	
	protected void setCursor(CursorType cursorType, boolean lockCursor) {
		if(!cursorLocked) {
			cursorLocked = lockCursor;
			customCursorSet = true;
			if(cursorType == CursorType.EXPAND_EAST_WEST) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			}
			else if(cursorType == CursorType.EXPAND_NORTH_SOUTH) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			}
			else if(cursorType == CursorType.HAND) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			else if(cursorType == CursorType.MOVE) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
			else {
				super.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	@Override
	public void restoreCursor() {
		if(customCursorSet) {
			super.setCursor(Cursor.getDefaultCursor());
			customCursorSet = false;
		}
		cursorLocked = false;
	}
	
	public Color getMapBorderColor() {
		return mapBorderColor;
	}

	public void setMapBorderColor(Color mapBorderColor) {
		this.mapBorderColor = mapBorderColor;
	}

	public int getMapBorderLineWidth() {
		return mapBorderLineWidth;
	}

	public void setMapBorderLineWidth(int mapBorderLineWidth) {
		this.mapBorderLineWidth = mapBorderLineWidth;
	}	
	public boolean isShowScale() {
		return showScale;
	}

	public void setShowScale(boolean showScale) {
		if(this.showScale != showScale) {
			this.showScale = showScale;
			if(showScale && scaleBar == null) {
				scaleBar = new ScaleBar();
				scaleBar.setCenterLocation(new GridLocation2D(3, 3));
			}
			repaint();
		}
	}
	
	public boolean isAlwaysShowMapBorder() {
		return alwaysShowMapBorder;
	}

	public void setAlwaysShowMapBorder(boolean alwaysShowMapBorder) {
		this.alwaysShowMapBorder = alwaysShowMapBorder;
	}

	@Override
	public void redraw() {
		repaint();
	}
	
	@Override
	public void redraw(ILayer<? extends IMapObject> modifiedLayer) {
		//TODO: Possibly optimize to consider the layer that changed
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics gfx) {
		Graphics2D g = (Graphics2D) gfx;		
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Paint the background
		super.paintComponent(g);
		
		if(renderData.getPixelsPerGridUnit() == 0) {
			return;
		}		
		
		//Draw a border around the map
		if(mapBorderLineWidth > 0 && (alwaysShowMapBorder || showMapBorder)) {
			Stroke origStroke = g.getStroke();
			if(mapBorderColor == null) {
				g.setColor(Color.GRAY);
			}
			else {
				g.setColor(mapBorderColor);
			}
			g.setStroke(new BasicStroke(mapBorderLineWidth));
			//Adjust the render bounds so that the top and right border edges are drawn at the last map grid coordinate
			/*Rectangle2D.Double adjustedRenderBounds = new Rectangle2D.Double(renderData.renderBounds.x,
					renderData.renderBounds.y + renderData.getPixelsPerGridUnit(), 
					(renderData.getWidth_gridUnits()-1) * renderData.getPixelsPerGridUnit(), 
					(renderData.getHeight_gridUnits()-1) * renderData.getPixelsPerGridUnit());
			g.draw(adjustedRenderBounds);*/
			g.draw(renderData.renderBounds);			
			g.setStroke(origStroke);
		}
		
		//g.setClip(renderData.renderBounds);
		g.translate(renderData.renderBounds.x, renderData.renderBounds.y);
		
		//Render the layers		
		if(mapModel != null && mapModel.layers != null && !mapModel.layers.isEmpty()) {
			for(ILayer_Phase1<? extends AbstractMapObject_Phase1> layer : mapModel.getLayers()) {
				if(layer.isVisible()) {
					layer.render(g, renderData, renderPropertiesChanged);
				}
			}
		}
		
		//Render the current tool tip if there is one
		if(currentToolTip != null && currentToolTipObject != null && 
				currentToolTipObject.getLayer() != null && currentToolTipObject.getLayer().isVisible()) {
			currentToolTip.render(g, renderData, renderPropertiesChanged);
		}
		
		// Draw axis
		/*
		g.setColor(Color.BLACK);
		g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
		g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
		*/
		
		//Draw the scale bar
		if(showScale && scaleBar != null) {
			scaleBar.setStartLocation_pixels(new Point2D.Double(10, 
					renderData.renderBounds.height - 30));
			scaleBar.render(g, renderData, renderPropertiesChanged);
		}
		
		g.translate(-renderData.renderBounds.x, -renderData.renderBounds.y);
		renderPropertiesChanged = false;
	}		
	
	/** Test main */
	public static void main(String[] args) {
		MapModel mapModel = new MapModel();
		MapPanel_Phase1 mapPanel = new MapPanel_Phase1(mapModel, new GridSize(100, 100));
		mapPanel.setMapBorderLineWidth(1);
		
		//Load a test region
		RegionLayer regionLayer = new RegionLayer("1");

		//mapModel.addLayer(regionLayer);
		SocintPolygonCsvParser regionParser;
		try {
			regionParser = new SocintPolygonCsvParser("data/Phase_1_CPD/Sample Data/regions.csv");
			if(regionParser.getRegions() != null) {
				for(SocintPolygon region : regionParser.getRegions()) {
					RegionMapObject regionGUI = new RegionMapObject(region);
					regionGUI.setBackgroundColor(Color.lightGray);
					regionGUI.setBorderColor(Color.darkGray);
					//regionGUI.setBorderThickness(2);
					regionLayer.addMapObject(regionGUI);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		//Load a test road
		RoadLayer roadLayer = new RoadLayer("2");
		//mapModel.addLayer(roadLayer);
		RoadCsvParser roadParser;
		try {
			roadParser = new RoadCsvParser("data/Phase_1_CPD/Sample Data/roadTest.csv");
			if(roadParser.getRoads() != null) {
				for(Road road : roadParser.getRoads()) {
					roadLayer.addMapObject(new RoadMapObject(road));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}			
		
		//Create test placemarks
		PlacemarkLayer placemarkLayer = new PlacemarkLayer("3", mapPanel);
		placemarkLayer.setEditable(true);
		mapModel.addLayer(placemarkLayer);
		PlacemarkMapObject a = new GroupCenterPlacemark(
				new GroupCenter(GroupType.A, new GridLocation2D(GroupType.A.toString(), 0, 50)));
		a.setMarkerShape(PlacemarkShape.Square);
		placemarkLayer.addMapObject(a);
		
		PlacemarkMapObject b = new GroupCenterPlacemark(
				new GroupCenter(GroupType.B, new GridLocation2D(GroupType.B.toString(), 75, 20)));
		b.setEditable(true);
		b.setToolTipText("<html>Test<br/>Label</html>");
		placemarkLayer.addMapObject(b);		
		
		//Create an annotation for the placemark
		AnnotationLayer annotationLayer = new AnnotationLayer("4");
		mapModel.addLayer(annotationLayer);
		int i=1;
		for(AnnotationOrientation orientation : AnnotationOrientation.values()) {
			if(orientation != AnnotationOrientation.Center && orientation != AnnotationOrientation.Center_1) {
				PlacemarkMapObject annotationObject = new PlacemarkMapObject();
				annotationObject.setMarkerShape(PlacemarkShape.Square);
				annotationObject.setBorderColor(Color.black);
				annotationObject.setBorderLineWidth(1);
				annotationObject.setName("Test Annotation " + i);
				annotationObject.setId("Test Annotation " + i);
				annotationObject.setShowName(true);				
				a.addAnnotationAtOrientation(annotationObject, orientation, annotationLayer);
				i++;
			}
		}
		
		//Test placemark editing
		//placemarkLayer.editObject(placemark);
		
		//Create placemark with an icon
		PlacemarkMapObject iconPlacemark = new PlacemarkMapObject();
		placemarkLayer.addMapObject(iconPlacemark);
		iconPlacemark.setCenterLocation(new GridLocation2D(66, 70));
		iconPlacemark.setMarkerShape(PlacemarkShape.Square);
		iconPlacemark.setBorderColor(Color.black);
		iconPlacemark.setBorderLineWidth(1);
		iconPlacemark.setId("Icon Placemark");
		
		try {
			iconPlacemark.setMarkerIcon(ImageIO.read(
					iconPlacemark.getClass().getClassLoader().getResourceAsStream("images/military_icon_small.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		//Create a test circle and edit it
		ShapeLayer shapeLayer = new ShapeLayer("5", mapPanel);
		shapeLayer.setEditable(true);
		mapModel.addLayer(shapeLayer);
		CircleShape circle = new CircleShape(25.0, CircleShape.RadiusType.Radius_Pixels);
		circle.setEditable(true); //Make the circle editable
		shapeLayer.addMapObject(circle);
		circle.setId("circle");
		circle.setCenterLocation(new GridLocation2D(null, 0, 99));
		circle.setBorderColor(Color.red);
		//circle.setControlPointColor(Color.red);
		circle.setBorderLineWidth(1);		
		//shapeLayer.editObject(circle, new CircleEditor());
		
		JFrame frame = new JFrame("Map Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);	
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		frame.setContentPane(contentPane);
		
		//Add a layer panel to the frame
		LayerTreePanel layerPanel = new LayerTreePanel(mapPanel);
		layerPanel.setPreferredSize(new Dimension(200, 300));
		layerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(layerPanel, gbc);	
		
		//Add the map panel to the frame
		mapPanel.setPreferredSize(new Dimension(300, 300));
		mapPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		gbc.gridx++;
		gbc.weightx = 1;
		contentPane.add(mapPanel, gbc);		
		
		frame.pack();
		frame.setVisible(true);
	}	
}
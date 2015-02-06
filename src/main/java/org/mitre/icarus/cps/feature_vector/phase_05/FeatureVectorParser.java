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
package org.mitre.icarus.cps.feature_vector.phase_05;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JFrame;

import org.mitre.icarus.cps.app.widgets.map.phase_05.Building;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Feature;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVector;
import org.mitre.icarus.cps.app.widgets.map.phase_05.FeatureVectorPanel;
import org.mitre.icarus.cps.app.widgets.map.phase_05.IMINTLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_05.LayoutMode;
import org.mitre.icarus.cps.app.widgets.map.phase_05.MASINTFeature;
import org.mitre.icarus.cps.app.widgets.map.phase_05.MASINTLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_05.SIGINTFeature;
import org.mitre.icarus.cps.app.widgets.map.phase_05.SIGINTLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Sector;
import org.mitre.icarus.cps.app.widgets.map.phase_05.SectorLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_05.Layer.LayerType;
import org.mitre.icarus.cps.app.widgets.phase_05.WidgetConstants_Phase05;
import org.mitre.icarus.cps.app.widgets.phase_05.ImageManager;

/**
 * Parser for feature vector and object palette CSV files.  Protected methods
 * are used by the FeatureVectorManager.
 * 
 * @author Jing Hu
 *
 */
public class FeatureVectorParser {
	
	/** Whitespace padding added to the top, bottom, left, and right of the scene */
	private static final int IMAGE_PADDING = 14;
	
	/** Whitespace added to the top, bottom, left, and right of a sector */
	private static final int SECTOR_PADDING = WidgetConstants_Phase05.INT_HIT_RENDER_SIZE/2;	
	
	protected Map<String, ParsedPaletteObject> parseObjectPalette(URL objectPaletteUrl) throws IOException, ParseException {
		Map<String, List<int[]>> objectLists = new HashMap<String, List<int[]>>();
		
		parseObjects(objectLists, objectPaletteUrl);
		/*if (waterFile != null) {
			parseObjects(objectLists, waterFile);
		}*/
		
		Map<String, ParsedPaletteObject> objects = new HashMap<String, ParsedPaletteObject>();
		if(!objectLists.isEmpty()) {
			for (Entry<String, List<int[]>> entry : objectLists.entrySet()) {
				objects.put(entry.getKey(), new ParsedPaletteObject(entry.getValue()));
			}
		}
		
		return objects;
	}
	
	/**
	 * Parses the layers from a feature vector. This function expects a folder
	 * and looks for the files "object_palette_vector.csv",
	 * "water_palette_vector.csv" and "feature_vector.csv" in the given
	 * folder.
	 */
	protected FeatureVector parseFeatureVector(URL directoryUrl) throws IOException, ParseException {
		
		URL objectFile = new URL(directoryUrl, "object_palette_vector.csv");
		//URL waterFile = new URL(file, "water_palette_vector.csv");
		URL featureFile = new URL(directoryUrl, "feature_vector.csv");

		return parseFeatureVector(objectFile, featureFile);
	}

	protected FeatureVector parseFeatureVector(String featureVectorUrl, String objectPaletteUrl, URL baseUrl) throws IOException, ParseException {
		
		URL featureFile = new URL(baseUrl, featureVectorUrl);
		URL objectFile = new URL(baseUrl, objectPaletteUrl);		

		return parseFeatureVector(featureFile, objectFile);
	}
	
	protected FeatureVector parseFeatureVector(URL featureVectorUrl, URL objectPaletteUrl) throws IOException, ParseException {
		return parseFeatureVector(featureVectorUrl, objectPaletteUrl.toString(), 
				parseObjectPalette(objectPaletteUrl), false, 0);
	}
	
	protected FeatureVector parseFeatureVector(URL featureVectorUrl, String objectPaletteName, 
			Map<String, ParsedPaletteObject> objects) throws IOException, ParseException {
		return parseFeatureVector(featureVectorUrl, objectPaletteName, objects, false, 0);
	}
	
	protected FeatureVector parseSingleSectorFeatureVector(URL featureVectorUrl, String objectPaletteName, 
			Map<String, ParsedPaletteObject> objects, int sectorId) throws IOException, ParseException {
		return parseFeatureVector(featureVectorUrl, objectPaletteName, objects, true, sectorId);
	}
	
	protected FeatureVector parseFeatureVector(URL featureVectorUrl, String objectPaletteName, 
			Map<String, ParsedPaletteObject> objects, boolean singleSector, int singleSectorId) throws IOException, ParseException {	
		
		FeatureVector world = new FeatureVector(objectPaletteName);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(featureVectorUrl.openStream()));
		
		//Discard first line (column headers)
		String line = in.readLine();
		
		//Create sector layer
		SectorLayer sectorLayer = new SectorLayer();
		sectorLayer.setIcon(ImageManager.getImageIcon(ImageManager.SECTOR_LAYER_ICON));
		sectorLayer.setAlwaysEnabled(true);
		
		//Parse the feature vector file
		//Expected column headers are: LayerID, LayerType, ObjectID, ObjectOrientation, Metadata, X, Y, Sector
		int lineNum = 2;
		while ((line = in.readLine()) != null) {			
			String[] split = line.split(",");
			
			if(split == null || split.length < 8) {
				throw new ParseException(
						"Error at line " + lineNum + " in feature vector file. Expected LayerID, LayerType, ObjectID, ObjectOrientation, Metadata, X, Y, Sector.", lineNum);
			}	
			
			//Get the sector
			int sectorId = 0;
			try {
				sectorId = Integer.parseInt(split[7]);
				if(sectorId < 0) {
					throw new NumberFormatException();
				}
			} catch(NumberFormatException ex) {
				throw new ParseException(
						"Error at line " + lineNum + " in feature vector file. Sector must be a positive integer", lineNum);
			}
			
			if(singleSector && singleSectorId != sectorId) {
				continue;
			}			
			
			//Get the layer ID and layer type
			int layerId = 0;
			try {
				layerId = Integer.parseInt(split[0]);
			} catch(NumberFormatException ex) {
				throw new ParseException(
						"Error at line " + lineNum + " in feature vector file. LayerID must be an integer.", lineNum);
			}			
			LayerType layerType = LayerType.valueOf(split[1]);
			if(layerType == null) {
				throw new ParseException(
						"Error at line " + lineNum + " in feature vector file. LayerType must be one of IMINT, MASINT, or SIGINT.", lineNum);
			}
			
			//Get the object key (objectId_orientation)	
			int objectId = 0;
			try {
				objectId = Integer.parseInt(split[2]); 
			} catch(NumberFormatException ex) {
				throw new ParseException(
						"Error at line " + lineNum + " in feature vector file. ObjectID must be an integer.", lineNum);
			}
			String objectKey = createObjectKey(split[2], split[3]);			
			
			//Get the data field
			String meta = split[4];

			//Get the x,y coordinates
			int x = 0;
			int y = 0;
			try {
				x = Integer.parseInt(split[5]);
				y = Integer.parseInt(split[6]);
			} catch(NumberFormatException ex) {
				throw new ParseException(
						"Error at line " + lineNum + " in feature vector file. X and Y must be integers.", lineNum);
			}
			
			Feature feature = null;			
			
			switch (layerType) {
			case IMINT:
				Building building = new Building();
				
				ParsedPaletteObject obj = objects.get(objectKey);
				if(obj == null) {
					throw new ParseException(
							"Error at line " + lineNum + " in feature vector file. An object with the ID " + 
							split[2] + " and rotation " + split[3] + 
							" was not found in the object palette.", lineNum);
					//System.err.println("Warning, object " + objectKey +  " not in object pallete!");
				}
				int[][] layout = obj.getLayout();
				
				building.setLayout(layout);
				
				if (meta != null && meta.equals("1")) {
					//Add rooftop hardware
					int[][] acc = {layout[(int) (Math.random()*layout.length)]};
					building.setAccessories(acc);
				}
				
				building.setLayoutMode(LayoutMode.LIST);				
				building.setType(objectId);
				building.setBounds(new Rectangle(x, y, obj.getWidth(), obj.getHeight()));
				feature = building;
				
				IMINTLayer imIntLayer = (IMINTLayer)world.getLayer(layerId);
				if(imIntLayer == null) {
					//Create new IMIINT layer
					imIntLayer = new IMINTLayer(layerId, 0, 0);
					imIntLayer.setIcon(ImageManager.getImageIcon(ImageManager.IMINT_LAYER_ICON));
					world.addLayer(imIntLayer);
				}
				building.setParent(imIntLayer);
				imIntLayer.getChildren().add(building);
				
				break;
				
			case SIGINT:
				SIGINTFeature sigint = new SIGINTFeature();
				sigint.setBounds(new Rectangle(x, y, 1, 1));
				feature = sigint;
				
				SIGINTLayer sigIntLayer = (SIGINTLayer)world.getLayer(layerId);
				if(sigIntLayer == null) {
					//Create new SIGINT layer
					sigIntLayer = new SIGINTLayer(layerId);
					sigIntLayer.setIcon(ImageManager.getImageIcon(ImageManager.SIGINT_LAYER_ICON));
					world.addLayer(sigIntLayer);
				}
				sigint.setParent(sigIntLayer);
				sigIntLayer.getChildren().add(sigint);
				
				break;
				
			case MASINT:
				MASINTFeature masint = new MASINTFeature();
				//masint.setMasCount(Integer.parseInt(split[2]));
				masint.setBounds(new Rectangle(x, y, 1, 1));
				masint.setType(objectId);
				feature = masint;
				
				MASINTLayer masIntLayer = (MASINTLayer)world.getLayer(layerId);
				if(masIntLayer == null) {
					//Create new MASINT layer
					masIntLayer = new MASINTLayer(layerId);
					masIntLayer.setIcon(ImageManager.getImageIcon(ImageManager.MASINT_LAYER_ICON));
					world.addLayer(masIntLayer);
				}
				masint.setParent(masIntLayer);
				masIntLayer.getChildren().add(masint);
				
				break;
				
			default:
				break;				
			}
			
			if(feature != null) {
				feature.setLayerId(layerId);
				//Assign feature object to a sector
				Sector sector = sectorLayer.getSector(sectorId);
				if (sector == null) {
					sector = new Sector(sectorId);
					sector.setName("Sector " + sectorId);
					sectorLayer.addSector(sector);
				}
				sector.getSectorObjects().add(feature);
			}
			
			lineNum++;
		}
		
		reBounds(world);
		fixBounds(world);
		
		setSectorBounds(sectorLayer);
		world.addLayer(sectorLayer);

		world.getBounds().x = IMAGE_PADDING;
		world.getBounds().y = IMAGE_PADDING;
		world.getBounds().width += IMAGE_PADDING;
		world.getBounds().height += IMAGE_PADDING;
		
		//System.out.println(world.getBounds());
		return world;
	}

	/**
	 * Recursively generate bounds for objects in the tree.
	 */
	private void reBounds(Feature feature) {
		//TODO: SIGINT and MASINT features have a minimum width x height of 16x16.
		//They should reset their bounds if the minimum width or height is not met.
		
		if (feature.getChildren() == null || feature.getChildren().isEmpty()) {
			return;
		}
		
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for (Feature child : feature.getChildren()) {
			Rectangle bounds = child.getBounds();
			reBounds(child);
			
			if (bounds.x < minX) {
				minX = bounds.x;
			}
			
			if (bounds.y < minY) {
				minY = bounds.y;
			}
			
			if (bounds.x + bounds.width > maxX) {
				maxX = bounds.x + bounds.width;
			}
			
			if (bounds.y + bounds.height > maxY) {
				maxY = bounds.y + bounds.height;
			}
		}
		
		for (Feature child : feature.getChildren()) {
			
			Rectangle bounds = child.getBounds();
			
			bounds.x -= minX;
			bounds.y -= minY;
		}
		
		feature.getBounds().setBounds(minX, minY, maxX-minX, maxY-minY);
	}
	
	/** Generate sector bounds */
	private void setSectorBounds(SectorLayer sectorLayer) {
		
		if(sectorLayer.getChildren() == null || sectorLayer.getChildren().isEmpty()) {
			return;
		}
		
		for(Sector sector : sectorLayer.getChildren()) {

			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;

			for(Feature child : sector.getSectorObjects()) {				
				Rectangle bounds = child.getBounds();

				//Translate position to world coordinates
				Point pos = getPositionInWorld(child);

				if (pos.x < minX) {
					minX = pos.x;
				}

				if (pos.y < minY) {
					minY = pos.y;
				}

				if (pos.x + bounds.width > maxX) {
					maxX = pos.x + bounds.width;
				}

				if (pos.y + bounds.height > maxY) {
					maxY = pos.y + bounds.height;
				}
			}

			sector.getBounds().setBounds(minX-1 - SECTOR_PADDING/2, minY-1 - SECTOR_PADDING/2, 
					maxX-minX+2 + SECTOR_PADDING, maxY-minY+2 + SECTOR_PADDING);
		}
	}
	
	/** Given a feature, get the position of the feature in world coordinates by 
	 * recursively getting its position relative to its parent, its parent's parent, etc.*/
	private Point getPositionInWorld(Feature feature) {
		Point p = new Point(feature.getBounds().x, feature.getBounds().y);
		Feature currFeature = feature.getParent();
		while(currFeature != null) {
			p.x += currFeature.getBounds().x;
			p.y += currFeature.getBounds().y;
			currFeature = currFeature.getParent();
		}
		return p;
	}
	
	/**
	 * Flips the y-axis in the feature children
	 * @param parent
	 */
	private void fixBounds(Feature parent) {
		Rectangle parentBounds = parent.getBounds();
		for (Feature feature : parent.getChildren()) {
			
			if (feature.getChildren() != null) {
				fixBounds(feature);
			}
			
			Rectangle bounds = feature.getBounds();
			bounds.y = parentBounds.height - bounds.y - bounds.height;
		}
	}
	
	/**
	 * Parses objects from an object palette or water palette vector.  Object maps
	 * key is objectId_orientation
	 */
	private void parseObjects(Map<String, List<int[]>> objects, URL file) throws IOException, ParseException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(file.openStream()));
		
		// discard 1st line
		String line = in.readLine();
		
		//Expected column headers are: ObjectID, ObjectOrientation, X, Y
		int lineNum = 2;
		while ((line = in.readLine()) != null) {
			String[] split = line.split(",");
			
			if(split == null || split.length < 4) {
				throw new ParseException(
						"Error at line " + lineNum + " in object palette file. Expected ObjectID, ObjectOrientation, X, Y.", lineNum);
			}			
			
			//Get the object key (objectId_orientation)
			String objectKey = createObjectKey(split[0], split[1]);
			//System.out.println("key: " + objectKey);
			//Integer objectId = Integer.valueOf(split[0]);
			//Integer orientation = Integer.valueOf(split[1]);
			
			List<int[]> list = objects.get(objectKey);
			if (list == null) {
				list = new ArrayList<int[]>(5);
				objects.put(objectKey, list);
			}
			
			int[] pair = new int[2];
			try {
				pair[0] = Integer.parseInt(split[2]);
				pair[1] = Integer.parseInt(split[3]);
			} catch(NumberFormatException ex) {
				throw new ParseException(
						"Error at line " + lineNum + " in object palette file. X and Y must be integers.", lineNum);
			}
			
			list.add(pair);
			lineNum++;
		}		
	}	
	
	
	/**
	 *  Get the object key (objectId_orientation)
	 *  
	 */
	protected String createObjectKey(int objectId, int orientation) {
		return createObjectKey(Integer.toString(objectId),
				Integer.toString(orientation));
	}
	
	private String createObjectKey(String objectId, String orientation) {
		 return objectId + "_" + orientation;
	}
	
	public static void main(String[] args) {
		FeatureVectorParser parser = new FeatureVectorParser();
		
		
		FeatureVector world = null;
		try {
			world = parser.parseFeatureVector(new File("data/sample3").toURI().toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(world != null) {
			System.out.println(world.getBounds());

			FeatureVectorPanel imagePanel = new FeatureVectorPanel();
			imagePanel.setWorld(world);
			imagePanel.setPreferredSize(new Dimension(800, 600));

			JFrame frame = new JFrame("Test Frame");
			frame.add(imagePanel);
			frame.add(imagePanel.createLayerPanel(), BorderLayout.WEST);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
	}

}

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
package org.mitre.icarus.cps.app.widgets.map.phase_2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.mitre.icarus.cps.app.widgets.map.LineStyle;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.AnnotationLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.CircleLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.PlacemarkLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.PolygonLayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Circle;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Placemark;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Polygon;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplayMode;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.BlueLocationPlacemark.DisplaySize;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Circle.RadiusType;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Placemark.PlacemarkShape;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.Annotation;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.IAnnotationMapObject.AnnotationOrientation;
import org.mitre.icarus.cps.app.widgets.map.phase_2.tile_factory.IcarusTileFactoryInfo;
import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.exam.phase_2.loader.IcarusExamLoader_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.feature_vector.phase_2.AreaOfInterest;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.FeatureContainer;
import org.mitre.icarus.cps.feature_vector.phase_2.Region;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.loader.FeatureVectorLoader;

public class MapTest {
	
	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "gatekeeper.mitre.org");
		System.setProperty("http.proxyPort", "80");
		
		testMapEditing();
		//testBlueLocationMap();
		//testMapImageOverlay();
		//testMapImageTiles();
	}
	
	protected static void testMapEditing() {
		try {
			JFrame frame = new JFrame("Map Editor Test");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			//Create the map
			MapPanelContainer mapPanel = new MapPanelContainer(frame, true, false);	
			mapPanel.setAOILayerEnabled(true);
			mapPanel.setBlueRegionLayerEnabled(true);
			mapPanel.setBlueLocationsLayerEnabled(true);
			mapPanel.setBlueLocationsLayerSelectable(true);
			mapPanel.setShowScaleBar(false);
			mapPanel.setPanEnabled(false);
			mapPanel.setZoomEnabled(true);
			frame.getContentPane().add(mapPanel);		

			//Load the Mission 1 area of interest
			URL aoiFileUrl = new File("data/Phase_2_CPD/exams/Sample-Exam-2/Mission1_Area_Of_Interest.xml").toURI().toURL();
			AreaOfInterest aoi = FeatureVectorLoader.getInstance().unmarshalAreaOfInterest(
					aoiFileUrl, false);
			if(aoi != null && aoi.getSceneImageFile() != null 
					&& aoi.getSceneImageFile().getFileUrl() != null) {
				//Load the scene image
				aoi.setSceneImage(ImageIO.read(IcarusExamLoader_Phase2.createUrl(
						aoiFileUrl, aoi.getSceneImageFile().getFileUrl())));
			}
			mapPanel.setAOI(aoi);
			mapPanel.setBlueRegion(aoi != null ? aoi.getBlueRegion() : null);

			//Add blue locations
			URL locationsUrl = new File("data/Phase_2_CPD/exams/Sample-Exam-2/Mission1_Blue_Locations.xml").toURI().toURL();
			FeatureContainer<BlueLocation> blueLocations = FeatureVectorLoader.getInstance().unmarshalBlueLocations(locationsUrl, false);
			mapPanel.setIntAnnotationTextLayerEnabled(MapConstants_Phase2.SHOW_OSINT_TEXT || 
					MapConstants_Phase2.SHOW_IMINT_TEXT);
			mapPanel.setOsintLineLayerEnabled(MapConstants_Phase2.SHOW_OSINT_LINE);
			mapPanel.setImintCircleLayerEnabled(MapConstants_Phase2.SHOW_IMINT_CIRCLE);
			mapPanel.setSigintLayerEnabled(true);			
			Map<String, BlueLocationPlacemark> placemarks = mapPanel.addBlueLocations(blueLocations.getFeatureList(), 
					createBlueLocationNames(blueLocations), true, 
					DisplayMode.StandardMode, DisplaySize.StandardSize, false);
			if(placemarks != null) {
				for(BlueLocationPlacemark placemark : placemarks.values()) {
					mapPanel.addIntToBlueLocation(placemark, DatumType.OSINT, false, true);					
					mapPanel.addIntToBlueLocation(placemark, DatumType.IMINT, false, true);
					mapPanel.addIntToBlueLocation(placemark, DatumType.SIGINT, false, true);
				}
			}
			
			//Edit the Blue locations			
			mapPanel.setBlueLocationsEditable(true);
			
			//Add a circle to the map and edit it		
			CircleLayer<MapPanel_Phase2> circleLayer = new CircleLayer<MapPanel_Phase2>("SHAPE_LAYER", 
					mapPanel.getMapPanel());
			mapPanel.getMapPanel().addLayer(circleLayer);
			Circle circle = new Circle(
					new GeoPosition(mapPanel.getMapPanel().getCenterPosition().getLatitude()-.001, 
							mapPanel.getMapPanel().getCenterPosition().getLongitude()-.001), 
					0.05d, RadiusType.Miles);
			circle.setName("Circle");
			circle.setBorderLineStyle(new LineStyle(1));
			circle.setControlPointsVisible(true);
			circleLayer.addMapObject(circle);
			circleLayer.setEditable(true);
			//circleLayer.editObject(circle);	

			frame.pack();
			frame.setVisible(true);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected static List<String> createBlueLocationNames(FeatureContainer<BlueLocation> blueLocations) {
		if(blueLocations != null && !blueLocations.isEmpty()) {
			ArrayList<String> locationNames = new ArrayList<String>(blueLocations.size());
			for(BlueLocation location : blueLocations) {
				locationNames.add(location.getId());
			}
			return locationNames;
		}
		return null;
	}
	
	protected static void testBlueLocationMap() {
		try {			
			JFrame frame = new JFrame("Blue Map Demo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//Create the map using a SingleTileFactory
			MapPanelContainer mapPanel = new MapPanelContainer(frame, true, true);
			//mapPanel.getMapPanel().setTileFactory(new SingleTileFactory(0, 5, 6));			
			//mapPanel.getMapPanel().setTileFactory(new ImageScalingTileFactory(792,
			//		0.006296933792000914, 0, 5, 11));
			mapPanel.setShowScaleBar(false);
			mapPanel.setPanEnabled(true);
			mapPanel.setZoomEnabled(false);
			
			MapPanel_Phase2 map = mapPanel.getMapPanel();
			//System.out.println(map.getTileFactory().getMapSize(0));			
			//Use the "f" test map
			//map.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			byte[] bimg = cacheInputStream(new File("data/Phase_2_CPD/exams/Sample-Exam-2/Mission1_GrayScale.png").toURI().toURL());			
			GeoPosition topLeft = new GeoPosition(42.2735829415404, -71.0745339752029); //north, west
			GeoPosition bottomRight = new GeoPosition(42.268923407819, -71.0682370414109); //south, east			
			
			//Load the sample map image
			BufferedImage img = GraphicsUtilities.loadCompatibleImage(new ByteArrayInputStream(bimg));
			//map.setPreferredSize(new Dimension(800, 800));
			//map.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			//map.setSize(map.getPreferredSize());
			mapPanel.setMapPreferredSize(new Dimension(MapConstants_Phase2.PREFERRED_MAP_WIDTH, 
					MapConstants_Phase2.PREFERRED_MAP_HEIGHT), MapConstants_Phase2.MAP_BORDER_NARROW);		
			mapPanel.setAOI(new AreaOfInterest(img, topLeft.getLongitude(), topLeft.getLatitude(),
					bottomRight.getLongitude(), bottomRight.getLatitude()));
			mapPanel.setAOILayerEnabled(true);
			
			//DEBUG CODE
			/*double width_degrees =  Math.abs(topLeft.getLongitude() - bottomRight.getLongitude());
			double pixelsPerDegree = img.getWidth()/width_degrees;
			double tileSize = (pixelsPerDegree * 360) / Math.pow(2, 10);
			System.out.println("width (degrees): " + width_degrees + ", height (degrees): " +
					Math.abs(topLeft.getLatitude() - bottomRight.getLatitude()) + ", pixels/degree: " + pixelsPerDegree + ", tileSize: " + tileSize);
			System.out.println(map.getTileFactory().getTileSize(1));*/
			//END DEBUG CODE
			
			//Add the blue region to the map
			PolygonLayer<MapPanel_Phase2> polygonLayer = new PolygonLayer<MapPanel_Phase2>("POLYGON_LAYER", map);
			polygonLayer.setSelectable(false);
			map.addLayer(polygonLayer);
			JAXBContext context = JAXBContext.newInstance(AreaOfInterest.class);
			String filepath = "data/Phase_2_CPD/exams/Sample-Exam-2/Mission1_Area_Of_Interest.xml";
			Unmarshaller um = context.createUnmarshaller();
			AreaOfInterest aoi = (AreaOfInterest)um.unmarshal(new File(filepath));
			Region blueRegion = aoi.getBlueRegion();
			Polygon polygon = new Polygon(blueRegion.getVertices());
			polygon.setBorderLineWidth(1.f);
			polygon.setBorderColor(ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_PLAYER));
			polygon.setForegroundColor(polygon.getBorderColor());
			polygon.setBackgroundColor(polygon.getBorderColor());
			polygon.setTransparency(0.45f);
			polygonLayer.addMapObject(polygon);			
			
			//Add a Blue location to the map
			PlacemarkLayer<MapPanel_Phase2> placemarkLayer = new PlacemarkLayer<MapPanel_Phase2>("PLACEMARK_LAYER", map);
			placemarkLayer.setEditable(true);
			map.addLayer(placemarkLayer);
			BlueLocation location = new BlueLocation();
			location.setLocation(new GeoCoordinate(map.getCenterPosition().getLongitude(), map.getCenterPosition().getLatitude()));
			location.setImint(new ImintDatum());
			location.setName("1");
			location.setId("1");
			BlueLocationPlacemark placemark = new BlueLocationPlacemark(location, "1", true);
			placemark.setToolTipText("Blue Location 1");
			placemark.setInformationText("Blue Location 1");
			placemarkLayer.addMapObject(placemark);
			//placemark.setEditable(true);
			//placemarkLayer.editObject(placemark);			
			
			//Add an annotation to the Blue location placemark
			AnnotationLayer<MapPanel_Phase2> annotationLayer = new AnnotationLayer<MapPanel_Phase2>("ANNOTATION_LAYER", map); 
			map.addLayer(annotationLayer);
			Placemark annotation = new Placemark((GeoCoordinate)null, PlacemarkShape.None, false, null);
			annotation.setShowName(true);
			annotation.setName("100%");			
			//annotation.setBorderLineStyle(new LineStyle(1.5f));	
			Annotation a = placemark.addAnnotationAtOrientation(annotation, AnnotationOrientation.NorthWest, annotationLayer);	
			a.setShowAnnotationLine(false);
			a.setOffset_pixels(8);
			
			//Add another annotation
			annotation = new Placemark((GeoCoordinate)null, PlacemarkShape.None, false, null);
			annotation.setShowName(true);
			annotation.setName("10");			
			//annotation.setBorderLineStyle(new LineStyle(1.5f));	
			a = placemark.addAnnotationAtOrientation(annotation, AnnotationOrientation.NorthEast, annotationLayer);	
			a.setShowAnnotationLine(false);
			a.setOffset_pixels(8);
			
			frame.getContentPane().add(mapPanel);
			frame.pack();
			frame.setVisible(true);
			//System.out.println(mapPanel.getMapPanel().getSize());
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	protected static void testMapImageOverlay() {
		try {			
			JFrame frame = new JFrame("Map Demo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//Create the map using a SingleTileFactory
			MapPanelContainer mapPanel = new MapPanelContainer(frame, true, true);
			mapPanel.setShowScaleBar(true);
			mapPanel.setPanEnabled(true);
			mapPanel.setZoomEnabled(true);
			
			MapPanel_Phase2 map = mapPanel.getMapPanel();
			
			//AFG map test
			/* byte[] bimg = cacheInputStream(new File("data/Phase_2_CPD/maps/afg_map.png").toURI().toURL());
			GeoPosition topLeft = new GeoPosition(38.35029424354208, 60.35);
			GeoPosition bottomRight = new GeoPosition(29.30034304722024, 74.90253399751862);*/
			
			//Bedford map test
			/* byte[] bimg = cacheInputStream(new File("data/Phase_2_CPD/maps/bedford.png").toURI().toURL());
			GeoPosition topLeft = new GeoPosition(42.5125539720867, -71.2551260402179);
			GeoPosition bottomRight = new GeoPosition(42.4992926739791, -71.2117365584704);*/
			
			//Macon, GA map test
			byte[] bimg = cacheInputStream(new File("data/Phase_2_CPD/maps/macon-ga-preprocessed-preprocessed-google.png").toURI().toURL());			
			GeoPosition topLeft = new GeoPosition(32.8463151846328, -83.7786837502866);
			GeoPosition bottomRight = new GeoPosition(32.7934487635663, -83.7115820245294);			
			
			//Load the sample map image
			BufferedImage img = GraphicsUtilities.loadCompatibleImage(new ByteArrayInputStream(bimg));
			mapPanel.setMapPreferredSize(new Dimension(MapConstants_Phase2.PREFERRED_MAP_WIDTH, 
					MapConstants_Phase2.PREFERRED_MAP_HEIGHT), MapConstants_Phase2.MAP_BORDER);
			//map.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			//map.setSize(map.getPreferredSize());
			mapPanel.setAOI(new AreaOfInterest(img, topLeft.getLongitude(), topLeft.getLatitude(),
					bottomRight.getLongitude(), bottomRight.getLatitude()));
			mapPanel.setAOILayerEnabled(true);
			
			//Add a polygon to the map
			PolygonLayer<MapPanel_Phase2> polygonLayer = new PolygonLayer<MapPanel_Phase2>("POLYGON_LAYER", map);
			polygonLayer.setSelectable(true);
			map.addLayer(polygonLayer);
			GeoPosition center = map.getCenterPosition();
			double width_degrees = 0.01;
			Polygon polygon = new Polygon(Arrays.asList(
					new GeoCoordinate(center.getLongitude()-width_degrees, center.getLatitude()-width_degrees),
					new GeoCoordinate(center.getLongitude()+width_degrees, center.getLatitude()-width_degrees),
					new GeoCoordinate(center.getLongitude()+width_degrees, center.getLatitude()+width_degrees),
					new GeoCoordinate(center.getLongitude()-width_degrees, center.getLatitude()+width_degrees)));
			polygon.setBackgroundColor(Color.gray);
			polygon.setBorderLineWidth(1.f);
			polygon.setBorderColor(Color.black);
			polygon.setTransparency(0.5f);
			polygon.setName("Sample Building");
			polygon.setToolTipText("Sample Building");
			polygonLayer.addMapObject(polygon);
			
			//Add a placemark to the map and edit it
			PlacemarkLayer<MapPanel_Phase2> placemarkLayer = new PlacemarkLayer<MapPanel_Phase2>("PLACEMARK_LAYER", map);
			placemarkLayer.setEditable(true);
			map.addLayer(placemarkLayer);
			Placemark placemark = new Placemark(map.getCenterPosition(), PlacemarkShape.Square, true, null);
			placemark.setName("Military Facility");
			placemark.setToolTipText("Military Facility");
			placemark.setMarkerIcon(GraphicsUtilities.loadCompatibleImage(new ByteArrayInputStream(
					cacheInputStream(new File("images/military_icon_small.png").toURI().toURL()))));
			placemark.setShowName(true);
			placemark.setInformationText("A sample military facility");
			placemark.setBorderLineStyle(new LineStyle(1));
			placemarkLayer.addMapObject(placemark);
			//placemark.setEditable(true);
			//placemarkLayer.editObject(placemark);			
			
			//Add an annotation to the placemark
			/*AnnotationLayer<IcarusMapViewer> annotationLayer = new AnnotationLayer<IcarusMapViewer>("ANNOTATION_LAYER", map); 
			map.addLayer(annotationLayer);
			PlacemarkMapObject annotation = new PlacemarkMapObject(null, PlacemarkShape.Square, true, null);
			annotation.setName("Annotation");
			annotation.setBorderLineStyle(new LineStyle(1.5f));			
			placemark.addAnnotationAtOrientation(annotation, AnnotationOrientation.NorthWest, annotationLayer);*/		
			
			//Add a circle to the map and edit it		
			CircleLayer<MapPanel_Phase2> circleLayer = new CircleLayer<MapPanel_Phase2>("SHAPE_LAYER", map);
			map.addLayer(circleLayer);
			Circle circle = new Circle(
					new GeoPosition(map.getCenterPosition().getLatitude()-.01, map.getCenterPosition().getLongitude()-.01), 
					0.5d, RadiusType.Miles);
			circle.setName("Circle");
			circle.setBorderLineStyle(new LineStyle(1));
			circle.setControlPointsVisible(true);
			circleLayer.addMapObject(circle);
			//circleLayer.editObject(circle);	
			
			frame.getContentPane().add(mapPanel);
			frame.pack();
			frame.setVisible(true);		
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	protected static void testMapImageTiles() {
		try {
			IcarusTileFactoryInfo tf = new IcarusTileFactoryInfo(0, 1, 1, 792, false, false, 
					new File("data/Phase_2_CPD/maps").toURI().toURL().toString(), 
					"x", "y", "z");
			tf.setSingleTileMode(true);
			//tf.setSingleTileName("bedford.png");
			tf.setSingleTileName("bedford-square.png");
			
			JFrame frame = new JFrame("Map Test");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//Create the map using the IcarusTileFactoryInfo configured to always point to the same image tile
			JXMapViewer map = new JXMapViewer();
			map.setPanEnabled(false);
			map.setZoomEnabled(false);
			map.setZoom(0);
			map.setTileFactory(new DefaultTileFactory(tf));
			map.setDrawTileBorders(false);
			map.setPreferredSize(new Dimension(792, 792));
			map.setSize(map.getPreferredSize());
			map.setCenter(new Point2D.Double(792/2, 792/2));
			
			System.out.println(map.convertPointToGeoPosition(new Point2D.Double(0, 0)));
			
			//Test adding a way point
			
			
			frame.getContentPane().add(map);
			frame.pack();
			frame.setVisible(true);
			
			/*String baseUrl = new File("data/Phase_2_CPD/maps").toURI().toURL().toString();
			System.out.println("Base URL: " + baseUrl);
			String tileUrl = tf.getTileUrl(0, 0, 0);
			System.out.println("Tile URL: " + tileUrl);
			URI uri = new URI(tileUrl);
			byte[] bimg = cacheInputStream(uri.toURL());
			BufferedImage img = GraphicsUtilities.loadCompatibleImage(new ByteArrayInputStream(bimg));
			if(img != null) {
				System.out.println("Loaded image, size: " + img.getWidth() + " x " + img.getHeight());
			}*/
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	protected static byte[] cacheInputStream(URL url) throws Exception {
		InputStream ins = url.openStream();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buf = new byte[256];
		while(true) {
			int n = ins.read(buf);
			if(n == -1) break;
			bout.write(buf,0,n);
		}
		return bout.toByteArray();
	}
}
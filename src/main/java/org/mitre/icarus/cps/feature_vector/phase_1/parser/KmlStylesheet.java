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
package org.mitre.icarus.cps.feature_vector.phase_1.parser;

import java.util.ArrayList;

import org.mitre.icarus.cps.feature_vector.phase_1.FeatureType;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Style;

/**
 * Generates a KML style sheet for custom placemark icons and line styles.
 * 
 * @author Lily Wong
 *
 */
public class KmlStylesheet {
	
	/** The Constant ICON_SCALE. */
	public static final double ICON_SCALE = 0.4;
	
	/** The Constant LABEL_SCALE. */
	public static final double LABEL_SCALE = 1.5;	// how much larger than an attack icon should a center or marker should be 
	
	/** The Constant STYLE_FILEPATH. */
	public static final String STYLE_FILEPATH = "src/org/mitre/icarus/cps/feature_vector/cpd1/output/style.kml";
	
	/** The Constant WHITE_PLACEMARK_ICON. */
	private static final String WHITE_PLACEMARK_ICON = "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png";
	
	/** The Constant RELATIVE_ICON_DIRECTORY. */
	private static final String RELATIVE_ICON_DIRECTORY = "kml-icons/";
	
	/** The attack styles. */
	private ArrayList<AttackStyleIcon> attackStyles;
	
	/** The label styles. */
	private ArrayList<LabelStyleIcon> labelStyles;
	
	/** The int styles. */
	private ArrayList<LabelStyleIcon> intStyles;
	
	/** The task styles. */
	private ArrayList<StyleIcon> taskStyles;	// contains the above
	
	/** The kml. */
	private Kml kml;
	
	/** The doc. */
	private Document doc;
	
	/**
	 * KML stylesheet for CSV toKML() methods.
	 *
	 * @param doc the doc
	 * @param csvFeatureType the csv feature type
	 */
	public KmlStylesheet( Document doc, FeatureType csvFeatureType ) {
		this.doc = doc;
		if ( csvFeatureType.isTask() )
			generateIconStyles();
		else if ( csvFeatureType.isRoad() )
			generateLineStyles();
		else if ( csvFeatureType.isRegion() )
			generateRegionPolyStyles();
		else if ( csvFeatureType.isSocint() )
			generateSocintPolyStyles();
		else {}
	}
	
	/**
	 * Instantiates a new kml stylesheet.
	 */
	public KmlStylesheet() {
		kml = new Kml();
		doc = kml.createAndSetDocument();
		generateIconStyles();
	}
	
	/**
	 * Generate icon styles.
	 */
	@SuppressWarnings("serial")
	public void generateIconStyles() {
		/* Group Attacks */
		attackStyles = new ArrayList<AttackStyleIcon>() {
			{	// 						id, 			aabbggrr color code
				add( new AttackStyleIcon("red", 		"ff0000ff") );	// A
				add( new AttackStyleIcon("blue", 		"ffff3300") );	// B
				add( new AttackStyleIcon("green",		"ff00a400") );	// C
				add( new AttackStyleIcon("yellow",		"ff00ffff") );	// D
				add( new AttackStyleIcon("light-grey",	"ffe9e9e9") );	// X
				add( new AttackStyleIcon("dark-grey", 	"ff555555") );	// O
			}
		};
		
		/* Group Centers, Road Markers */
		labelStyles = new ArrayList<LabelStyleIcon>() {
			{	//   					id, 	filename
				// Group Centers
				add( new LabelStyleIcon("A", "letter_a.png") );	
				add( new LabelStyleIcon("B", "letter_b.png") );
				add( new LabelStyleIcon("C", "letter_c.png") );
				add( new LabelStyleIcon("D", "letter_d.png") );
				// Road Markers
				add( new LabelStyleIcon("1", "number_1.png") );
				add( new LabelStyleIcon("2", "number_2.png") );
				add( new LabelStyleIcon("3", "number_3.png") );
				add( new LabelStyleIcon("4", "number_4.png") );
			}
		};
		
		/* INT Markers */
		intStyles = new ArrayList<LabelStyleIcon>() {
			{	
				// INT markers
				add( new LabelStyleIcon( "Government", "congress.png" ) );	
				add( new LabelStyleIcon( "Military", "army.png" ) );	
				add( new LabelStyleIcon( "SparseTraffic", "traffic.png",
											"ff00a5ff" ) );	
				add( new LabelStyleIcon( "DenseTraffic", "traffic.png",
											"ffff00ff" ) );	
				add( new LabelStyleIcon( "Silent", "accessdenied-white.png" ) );	
				add( new LabelStyleIcon( "Chatter", "wifi-white.png" ) );	
			}
		};
		
		/* collection of all styles related to a Task */
		taskStyles = new ArrayList<StyleIcon>();
		taskStyles.addAll( attackStyles );
		taskStyles.addAll( labelStyles );
		taskStyles.addAll( intStyles );
		
		/* add the styles to the KML file */
		for ( StyleIcon styleIcon : taskStyles ) {
			addIconStyle( styleIcon.getId(), styleIcon.getUrl(), 
					styleIcon.getColor(), styleIcon.getScale() );
		}
	}
	
	// add a Style definition with a specified icon and aabbggrr color
	/**
	 * Adds the icon style.
	 *
	 * @param id the id
	 * @param url the url
	 * @param color the color
	 * @param iconScale the icon scale
	 */
	private void addIconStyle(String id, String url, String color, double iconScale ) {
		Style style = doc.createAndAddStyle()
							.withId( id );
		IconStyle iconstyle = style.createAndSetIconStyle()
									.withScale( iconScale * ICON_SCALE )
									.withColor( color );
		iconstyle.createAndSetIcon()
					.withHref( url );
	}
	
	/* Linestring Styles */
	/**
	 * Generate line styles.
	 */
	public void generateLineStyles() {
		addLineStyle( "default", 		"ffffffff", 2 );		
	}
	
	/**
	 * Adds the line style.
	 *
	 * @param id the id
	 * @param color the color
	 * @param width the width
	 */
	private void addLineStyle( String id, String color, int width ) {
		Style style = doc.createAndAddStyle()
				.withId( id );
		style.createAndSetLineStyle()
			.withColor(color)
			.withWidth(width);	// pixels
	}
	
	/* Polygon Styles */
	/**
	 * Generate region poly styles.
	 */
	public void generateRegionPolyStyles() {
		addPolyStyle( "Aregion", "550000ff" );
		addPolyStyle( "Bregion", "55ff0000" );
		addPolyStyle( "Cregion", "5500a400" );
		addPolyStyle( "Dregion", "5500ffff" );
	}
	/**
	 * Generate socint poly styles.
	 */
	public void generateSocintPolyStyles() {
		addPolyStyle( "1pt", "55ff0000" );
		addPolyStyle( "2pt", "550000ff" );
		addPolyStyle( "3pt", "5500a400" );
		addPolyStyle( "4pt", "5500ffff" );
	}
	
	/**
	 * Adds the poly style.
	 *
	 * @param id the id
	 * @param color the color
	 */
	private void addPolyStyle( String id, String color ) {
		Style style = doc.createAndAddStyle()
				.withId( id );
		style.createAndSetPolyStyle()
			.withColor(color)
			.withFill( true )
			.withOutline( false );
	}
	
	/* Represents an Icon */
	/**
	 * The Class StyleIcon.
	 */
	protected class StyleIcon {
		
		/** The id. */
		private String id;
		
		/** The url. */
		private String url;
		
		/** The color. */
		private String color;
		
		/** The scale. */
		private double scale;
		
		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		protected String getId() { return id; }
		
		/**
		 * Gets the url.
		 *
		 * @return the url
		 */
		protected String getUrl() { return url; }
		
		/**
		 * Gets the color.
		 *
		 * @return the color
		 */
		protected String getColor() { return color; }
		
		/**
		 * Gets the scale.
		 *
		 * @return the scale
		 */
		protected double getScale() { return scale; }

		/**
		 * Instantiates a new style icon.
		 *
		 * @param id the id
		 * @param url the url
		 * @param color the color
		 * @param scale the scale
		 */
		private StyleIcon(String id, String url, String color, double scale) {
			this.id = id;
			this.url = url;
			this.color = color;
			this.scale = scale;
		}
		
		/**
		 * Instantiates a new style icon.
		 *
		 * @param id the id
		 * @param url the url
		 * @param color the color
		 */
		private StyleIcon(String id, String url, String color) {
			this(id, url, color, 1);
		}
	}
	// Attack placemark
	/**
	 * The Class AttackStyleIcon.
	 */
	private class AttackStyleIcon extends StyleIcon {
		
		/**
		 * Instantiates a new attack style icon.
		 *
		 * @param id the id
		 * @param color the color
		 */
		private AttackStyleIcon( String id, String color ) {
			super( id, WHITE_PLACEMARK_ICON, color, 1 );
		}
	}
	// Center or marker placemark whose size >= size of attack icon.
	/**
	 * The Class LabelStyleIcon.
	 */
	private class LabelStyleIcon extends StyleIcon {
		
		/**
		 * Instantiates a new label style icon.
		 *
		 * @param id the id
		 * @param url the url
		 */
		private LabelStyleIcon( String id, String url ) {
			this( id, url, "ffffffff" );
		}
		
		/**
		 * Instantiates a new label style icon.
		 *
		 * @param id the id
		 * @param url the url
		 * @param color the color
		 */
		private LabelStyleIcon( String id, String url, String color ) {
			super( id, RELATIVE_ICON_DIRECTORY + url, color, 1.5 );
		}
	}
}



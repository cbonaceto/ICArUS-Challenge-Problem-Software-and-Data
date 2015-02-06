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
package org.mitre.icarus.cps.feature_vector.phase_2.util;

import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;

/**
 * A collection of static methods for performing Geodetic calculations, unit conversions, and coordinate transformations.
 * 
 * @author CBONACETO
 *
 */
public class GeoUtils {
	
	public static final double PI_TIMES_2 = Math.PI * 2.d;
	
	public static final double PI_OVER_2 = Math.PI/2.d;
	
	public static final double PI_OVER_180 = Math.PI/180.d;
	
	public static final double D180_OVER_PI = 180.d/Math.PI;
	
	public static final double EARTH_RADIUS_MILES = 3956.6;
	
	private GeoUtils() {}
	
	/** Test main */
	public static void main(String[] args) {
		System.out.println(GeoUtils.computeLengthOfDegreeOfLongitude_meters(32.7934487635663));
	}
	
	/**
	 * Converts feet to meters.
	 * 
	 * @param feet a distance in feet
	 * @return the distance in meters
	 */
	public static double feetToMeters(double feet) {
		return feet * (1.d/3.2808399);
	}	
	
	/**
	 * Converts statute miles to meters.
	 * 
	 * @param miles a distance in statute miles
	 * @return the distance in meters
	 */
	public static double milesToMeters(double miles) {
		return GeoUtils.feetToMeters(5280 * miles);
	}
	
	/**
	 * Converts nautical miles to meters.
	 * 
	 * @param nm
	 * @return
	 */
	public static double nmToMeters(double nm) {
		return GeoUtils.feetToMeters(6076.11549 * nm);
	}
	
	/**
	 * Converts meters to feet.
	 * 
	 * @param meters a distance in meters
	 * @return the distance in feet
	 */
	public static double metersToFeet(double meters) {
		return meters *  3.2808399; 
	}
	
	/**
	 * Converts meters to statute miles.
	 * 
	 * @param meters a distance in meters
	 * @return the distance in statute miles
	 */
	public static double metersToMiles(double meters) {
		return GeoUtils.metersToFeet(meters)/5280;
	}
	
	/**
	 * Converts meters to nautical miles.
	 * 
	 * @param meters a distance in meters
	 * @return the distance in nautical miles
	 */
	public static double metersToNM(double meters) {
		return GeoUtils.metersToFeet(meters)/6076.11549;
	}
	
	/**
	 * Converts degrees to radians.
	 * 
	 * @param degrees
	 * @return radians
	 */
	public static double degreesToRadians(double degrees) {
		return degrees * PI_OVER_180;
	}
	
	/**
	 * Converts radians to degrees.
	 * 
	 * @param radians
	 * @return degrees
	 */
	public static double radiansToDegrees(double radians) {
		return radians * D180_OVER_PI;
	}
	
	/**
	 * Computes the distance in miles between two locations.
	 * 
	 * @param p1 location 1
	 * @param p2 location 2
	 * @return the distance in miles between p1 and p2
	 */
	public static Double computeDistance_miles(GeoCoordinate p1, GeoCoordinate p2) {
		// convert lat lon to radians
		double p1Lat = degreesToRadians(p1.getLat());
		double p1Lon = degreesToRadians(p1.getLon());
		double p2Lat = degreesToRadians(p2.getLat());
		double p2Lon = degreesToRadians(p2.getLon());
		// absolute differences for computation
		double latDiff = Math.abs(p2Lat-p1Lat);
		double lonDiff = Math.abs(p2Lon-p1Lon);
		
		// haversine formula, better for small distances
		double termA = Math.pow(Math.sin(latDiff/2), 2);
		double lonTerm = Math.pow(Math.sin(lonDiff/2), 2);
		double termB = Math.cos(p1Lat) * Math.cos(p2Lat) * lonTerm;
		double centralAngle = 2 * Math.asin(Math.sqrt(termA + termB));
		
		// arccosine formula, basic
//		double term1 = Math.sin(p1Lat) * Math.sin(p2Lat);
//		double term2 = Math.cos(p1Lat) * Math.cos(p2Lat) * Math.cos(lonDiff);
//		double centralAngle = Math.acos(term1 + term2);
		
		return EARTH_RADIUS_MILES * centralAngle;
	}
	
	
	
	/**
	 * Computes the length of a degree of longitude in meters at a given coordinate.
	 *  
	 * @param p
	 * @return
	 */
	public static Double computeLengthOfDegreeOfLongitude_meters(GeoCoordinate p) {
		if(p != null) {
			return computeLengthOfDegreeOfLongitude_meters(p.getLat());
		} 
		return 0D;		
	}
	
	/**
	 * Computes the length of a degree of longitude in meters at a given latitude in degrees.
	 * . 
	 * @param latitude
	 * @return
	 */
	public static Double computeLengthOfDegreeOfLongitude_meters(double latitude) {
		double latitude_rads = GeoUtils.degreesToRadians(latitude);
		double p1 = 111412.84;	
		double p2 = -93.5;
		double p3 = 0.118;
		return (p1 * Math.cos(latitude_rads)) + (p2 * Math.cos(3 * latitude_rads)) + (p3 * Math.cos(5 * latitude_rads));		
		//int c = 111111;
		//return c * Math.cos(latitude_rads);
	}
	
	/**
	 * Computes the length of a degree of latitude in meters at a given coordinate.
	 *  
	 * @param p
	 * @return
	 */
	public static Double computeLengthOfDegreeOfLatitude_meters(GeoCoordinate p) {
		if (p != null) {
			return computeLengthOfDegreeOfLatitude_meters(p.getLat());
		} 
		return 0D;		
	}
	
	/**
	 * Computes the length of a degree of latitude in meters at a given latitude in degrees.
	 * . 
	 * @param latitude
	 * @return
	 */
	public static Double computeLengthOfDegreeOfLatitude_meters(double latitude) {
		double latitude_rads = GeoUtils.degreesToRadians(latitude);
		double m1 = 111132.92;
		double m2 = -559.82;	
		double m3 = 1.175;		
		double m4 = -0.0023;
		return m1 + (m2 * Math.cos(2 * latitude_rads)) + (m3 * Math.cos(4 * latitude_rads)) + (m4 * Math.cos(6 * latitude_rads));
	}	
	
	private static Double dotProduct(Double[] a, Double[] b) {
		return (a[0]*b[0]) + (a[1]*b[1]); 
	}

	/**
	 * Computes the relation between a point and the start point of a line segment.
	 * If relation < 0, the closest point is the line start.
	 * if 0 <= relation <= 1, the closest point is on the line.
	 * If relation > 1, the closest point is the line end.
	 * 
	 * Relation is projection of point onto line segment, scaled as unit vector.
	 * 
	 * @param point
	 * @param lineStart
	 * @param lineEnd
	 * @return
	 */
	public static Double computeRelationFromPointToLineStart(
			GeoCoordinate point, GeoCoordinate lineStart, GeoCoordinate lineEnd) {
		
		boolean printDebug = false;
		int scale = 1; // use if the computed values are too small to be accurately represented
		
		// End -> Start vector
		Double[] endToStart = new Double[2];
		endToStart[0] = (lineEnd.getLat() - lineStart.getLat()) * scale;
		endToStart[1] = (lineEnd.getLon() - lineStart.getLon()) * scale;
		if (printDebug) System.out.println("endToStart: " + endToStart[0] + ", " + endToStart[1]);
		
		// Point -> Start vector
		Double[] pointToStart = new Double[2];
		pointToStart[0] = (point.getLat() - lineStart.getLat()) * scale;
		pointToStart[1] = (point.getLon() - lineStart.getLon()) * scale;
		if (printDebug) System.out.println("pointToStart: " + pointToStart[0] + ", " + pointToStart[1]);

		// Line segment distance, squared for easy computation
		Double startToEndDistanceSq = Math.pow((computeDistance_euclidean(lineStart, lineEnd)),2);
		if (printDebug) System.out.println("pointToStartDistanceSq: " + startToEndDistanceSq);
		
		// Use dot product to project Point onto line segment
		Double dotProduct = dotProduct(pointToStart, endToStart);
		if (printDebug) System.out.println("dotProduct: " + dotProduct);
		
		// Scale projected point to line segment unit vector
		return dotProduct / startToEndDistanceSq;
	}
	
	/**
	 * Compute the shortest distance from a point to a line segment (side of polygon).
	 * Part of finding distance from a blue point to the closest blue region.
	 * 
	 * Method from the first answer:
	 * http://stackoverflow.com/questions/10983872/distance-from-a-point-to-a-polygon
	 * 
	 * @param point
	 * @param lineStart
	 * @param lineEnd
	 * @return
	 */
	public static Double computeShortestDistanceFromPointToLineSegment(
			GeoCoordinate point, GeoCoordinate lineStart, GeoCoordinate lineEnd) {
		
		// check validity of line segment - if start==end, computation would divide by dist=0 
		if ( lineStart.getLat().equals(lineEnd.getLat()) && 
				lineStart.getLon().equals(lineEnd.getLon()) ) {
			throw new IllegalArgumentException("Invalid line segment: start == end");
		}
		
		double relationToStart = computeRelationFromPointToLineStart(point,lineStart,lineEnd);
		
		if (relationToStart < 0) {
			// lineStart is closest
			return computeDistance_miles(point, lineStart);
		}
		else if ( relationToStart > 1 ) {
			// lineEnd is closest
			return computeDistance_miles(point, lineEnd);
		}
		else {
			
		}
		return null;
	}
	
	/**
	 * Calculate the shortest distance from a point to a line segment where the shortest
	 * distance is indeed on the line segment.
	 * 
	 * @param point
	 * @param lineStart
	 * @param lineEnd
	 * @param relation
	 * @return
	 */
	public static Double computeDistance_pythagorean(GeoCoordinate point, GeoCoordinate lineStart, GeoCoordinate lineEnd, double relation) {
		double segmentLegDistance = relation * computeDistance_miles(lineStart, lineEnd);
		double hypotenuse = computeDistance_miles(point, lineStart);
		return Math.sqrt( Math.pow(hypotenuse, 2) - Math.pow(segmentLegDistance, 2) );
	}
	
	/**
	 * Returns the Euclidean distance from one coordinate to another.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double computeDistance_euclidean(GeoCoordinate a, GeoCoordinate b) {
		return Math.sqrt( Math.pow(b.getLat()-a.getLat(), 2) + Math.pow(b.getLon()-a.getLon(), 2));
	}
}
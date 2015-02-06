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
package org.mitre.icarus.cps.app.widgets.map.phase_2.objects;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import org.mitre.icarus.cps.app.experiment.phase_2.MissionControllerUtils;
import org.mitre.icarus.cps.app.widgets.map.layers.ILayer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapConstants_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.MapPanel_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.layers.ILayer_Phase2;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Circle.RadiusType;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.Annotation;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.int_annotation.BasicIntRenderer;
import org.mitre.icarus.cps.app.widgets.map.phase_2.objects.annotation.int_annotation.SigintRenderer;
import org.mitre.icarus.cps.app.widgets.phase_2.ColorManager_Phase2;
import org.mitre.icarus.cps.assessment.score_computer.phase_2.ScoreComputer_Phase2.RedBluePayoff;
import org.mitre.icarus.cps.exam.phase_2.testing.DatumType;
import org.mitre.icarus.cps.exam.phase_2.testing.PlayerType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueAction.BlueActionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedAction.RedActionType;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.coordinate.GeoCoordinate;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.OsintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;

/**
 * 
 * @author CBONACETO
 *
 */
public class BlueLocationPlacemark extends Placemark {
	
	/** Blue location placemark display mode types */
	public static enum DisplayMode{StandardMode, AttackOutcomeMode};
	
	/** Blue location placemark display sizes */
	public static enum DisplaySize{StandardSize, BatchPlotSize};
	
	/** The blue location */
	protected final BlueLocation blueLocation;
	
	/** The blue action taken at the location */
	protected BlueActionType blueAction;
	
	/** The red action taken at the location */
	protected RedActionType redAction;
	
	/** The points awarded to Blue and Red at the location */
	protected RedBluePayoff redBluePayoff;
	
	/** OSINT text */
	protected Placemark osintText;
	
	/** OSINT overlay (line from blue point to blue region boundary) */
	protected Polyline osintLine;	
	
	/** IMINT text */
	protected Placemark imintText;
	
	/** HUMINT datum at the location */
	protected HumintDatum humintDatum;
	
	/** HUMINT text */
	protected Placemark humintText;
	
	/** Whether Humint has been revealed */
	protected boolean humintVisible = false;	
	
	/** IMINT overlay (circle showing IMINT region) */
	protected Circle imintCircle;
	
	/** SIGINT overlay (circle showing SIGINT state (chatter/silent)) */
	protected Circle sigintCircle;
	
	/** The current display mode */
	protected DisplayMode displayMode = DisplayMode.StandardMode;
	
	/** The current display size */
	protected DisplaySize displaySize = DisplaySize.StandardSize;
	
	/** Whether placemark is highlighted */
	protected boolean highlighted = false;
	
	/** Border line thickness to use when highlighting */
	protected float highlightedLineThickness = MapConstants_Phase2.FILL_BLUE_PLACEMARK ? 3.f : 4.f;
	
	protected float previousLineWidth;
	
	protected Color previousBorderColor;	
	
	public BlueLocationPlacemark(BlueLocation blueLocation, String mapDisplayName, boolean showMapDisplayName) {
		super(blueLocation.getLocation(), PlacemarkShape.Circle, true, null);		
		this.blueLocation = blueLocation;
		this.name = mapDisplayName;		
		this.id = blueLocation.getId();
		this.showName = showMapDisplayName;
		borderColor = ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_PLAYER);
		foregroundColor = Color.WHITE; //Color.BLACK;		
		backgroundColor = MapConstants_Phase2.FILL_BLUE_PLACEMARK ? borderColor : null;
		placemarkPreferredSize_pixels = (double)MapConstants_Phase2.BLUE_PLACEMARK_SIZE_PIXELS;
		setBorderLineWidth(2.f);
		configureToolTip(mapDisplayName, null);
	}
	
	public BlueLocation getBlueLocation() {
		return blueLocation;
	}
	
	public BlueActionType getBlueAction() {
		return blueAction;
	}

	public void setBlueAction(BlueActionType blueAction) {
		this.blueAction = blueAction;
	}

	public RedActionType getRedAction() {
		return redAction;
	}

	public void setRedAction(RedActionType redAction) {
		this.redAction = redAction;
	}	
	
	public RedBluePayoff getRedBluePayoff() {
		return redBluePayoff;
	}

	public void setRedBluePayoff(RedBluePayoff redBluePayoff) {
		this.redBluePayoff = redBluePayoff;
	}

	/**
	 * @return
	 */
	public boolean isOsintTextVisible() {
		return osintText != null && osintText.isVisible();
	}

	/**
	 * @param visible
	 * @param osintTextLayer
	 */
	public void setOsintTextVisible(boolean visible, 
			ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2> osintTextLayer) {
		OsintDatum osintDatum = blueLocation.getOsint();	
		if(visible && osintText == null && osintDatum != null && osintDatum.getRedVulnerability_P() != null) {
			osintText = new Placemark((GeoCoordinate)null, PlacemarkShape.None, false, null);
			osintText.setTextFont(MapConstants_Phase2.PLACEMARK_FONT_LARGE);
			osintText.setShowName(true);
			osintText.setName(" " + MissionControllerUtils.createIntDatumValueString(osintDatum) + " ");
			osintText.setForegroundColor(ColorManager_Phase2.getColor(ColorManager_Phase2.OSINT));
			osintText.setBackgroundColor(Color.WHITE);
			Annotation annotation = addAnnotationAtOrientation(osintText, AnnotationOrientation.NorthWest, osintTextLayer);
			annotation.setShowAnnotationLine(false);
			annotation.setOffset_pixels(MapConstants_Phase2.INT_ANNOTATION_OFFSET_PIXELS);
		}
	}
	
	/**
	 * 
	 */
	public void removeOsintText() {
		if(osintText != null) {
			removeAnnotationAtOrientation(AnnotationOrientation.NorthWest);
			osintText = null;
		}
	}

	/**
	 * @return
	 */
	public boolean isOsintLineVisible() {
		return osintLine != null && osintLine.isVisible();
	}
	
	/**
	 * @param visible
	 * @param osintLayer
	 */
	public void setOsintLineVisible(boolean visible, ILayer<? super Polyline> osintLineLayer) {		
		OsintDatum osintDatum = blueLocation.getOsint();
		if(visible && osintLine == null && osintDatum != null && osintDatum.getClosestBlueRegionPoint() != null) {
			//Create the OSINT line			
			osintLine = new Polyline(Arrays.asList(
					blueLocation.getLocation(),
					osintDatum.getClosestBlueRegionPoint()));
			osintLine.setId("osint_" + id);
			Color color = ColorManager_Phase2.getColor(ColorManager_Phase2.OSINT);
			osintLine.setBorderColor(color);
			osintLine.setBorderLineWidth(MapConstants_Phase2.OSINT_LINE_STYLE.getLineWidth());
			osintLine.setBorderLineStyle(MapConstants_Phase2.OSINT_LINE_STYLE);
			setControlPointRadius(1);
			osintLine.setControlPointsVisible(true);
			osintLine.setControlPointBorderColor(color);
			osintLine.setControlPointBackgroundColor(color);
			osintLineLayer.addMapObject(osintLine);
		}
		if(osintLine != null) {
			osintLine.setVisible(visible);
		}		
	}
	
	/**
	 * 
	 */
	public void removeOsintLine() {
		if(osintLine != null) {
			if(osintLine.getLayer() != null) {
				try {osintLine.getLayer().removeMapObject(osintLine);} catch(Exception ex) {}
			}
			osintLine = null;
		}
	}
	
	/**
	 * @return
	 */
	public boolean isImintTextVisible() {
		return imintText != null && imintText.isVisible();
	}
	
	/**
	 * @param visible
	 * @param imintTextLayer
	 */
	public void setImintTextVisible(boolean visible, 
			ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2> imintTextLayer) {
		ImintDatum imintDatum = blueLocation.getImint();	
		if(visible && imintText == null && imintDatum != null && imintDatum.getRedOpportunity_U() != null) {
			imintText = new Placemark((GeoCoordinate)null, PlacemarkShape.None, false, null);
			imintText.setTextFont(MapConstants_Phase2.PLACEMARK_FONT_LARGE);
			imintText.setShowName(true);
			imintText.setName(" " + MissionControllerUtils.createIntDatumValueString(imintDatum) + " ");
			imintText.setForegroundColor(ColorManager_Phase2.getColor(ColorManager_Phase2.IMINT));
			imintText.setBackgroundColor(Color.WHITE);
			Annotation annotation = addAnnotationAtOrientation(imintText, AnnotationOrientation.NorthEast, imintTextLayer);
			annotation.setShowAnnotationLine(false);
			annotation.setOffset_pixels(MapConstants_Phase2.INT_ANNOTATION_OFFSET_PIXELS);
		}
	}
	
	/**
	 * 
	 */
	public void removeImintText() {
		if(imintText != null) {
			removeAnnotationAtOrientation(AnnotationOrientation.NorthEast);
			imintText = null;
		}
	}
	
	/**
	 * @return
	 */
	public boolean isImintCircleVisible() {
		return imintCircle != null && imintCircle.isVisible();
	}

	/**
	 * @param visible
	 * @param imintLayer
	 */
	public void setImintCircleVisible(boolean visible, ILayer<? super Circle> imintCircleLayer) {		
		ImintDatum imintDatum = blueLocation.getImint();
		if(visible && imintCircle == null && imintDatum != null) {
			//Create the IMINT circle			
			imintCircle = new Circle(blueLocation.getLocation(), 
				MapConstants_Phase2.IMINT_RADIUS_FIXED ? MapConstants_Phase2.IMINT_RADIUS_PIXELS : 
					imintDatum.getRadius_miles() == null ? 0.025 : imintDatum.getRadius_miles(), 
				MapConstants_Phase2.IMINT_RADIUS_FIXED ? RadiusType.Pixels : RadiusType.Miles);
			imintCircle.setId("imint_" + id);
			imintCircle.setBorderColor(ColorManager_Phase2.getColor(ColorManager_Phase2.IMINT));
			imintCircle.setBorderLineWidth(MapConstants_Phase2.IMINT_LINE_STYLE.getLineWidth());
			imintCircle.setBorderLineStyle(MapConstants_Phase2.IMINT_LINE_STYLE);			
			imintCircleLayer.addMapObject(imintCircle);
		}
		if(imintCircle != null) {
			imintCircle.setVisible(visible);
		}		
	}
	
	/**
	 * 
	 */
	public void removeImintCircle() {
		if(imintCircle != null) {
			if(imintCircle.getLayer() != null) {
				try {imintCircle.getLayer().removeMapObject(imintCircle);} catch(Exception ex) {}
			}
			imintCircle = null;
		}
	}
	
	/**
	 * @return
	 */
	public HumintDatum getHumintDatum() {
		return humintDatum;
	}

	/**
	 * @param humintDatum
	 */
	public void setHumintDatum(HumintDatum humintDatum) {
		this.humintDatum = humintDatum;
	}

	/**
	 * @return
	 */
	public boolean isHumintTextVisible() {
		return humintText != null && humintText.isVisible();
	}
	
	/**
	 * @param visible
	 * @param imintTextLayer
	 */
	public void setHumintTextVisible(boolean visible, 
			ILayer_Phase2<? super Annotation, ? extends MapPanel_Phase2> humintTextLayer) {
		this.humintVisible = visible;
		if(visible && humintText == null && humintDatum != null && humintDatum.getRedCapability_Pc() != null) {
			humintText = new Placemark((GeoCoordinate)null, PlacemarkShape.None, false, null);
			humintText.setTextFont(MapConstants_Phase2.PLACEMARK_FONT_LARGE);
			humintText.setShowName(true);
			humintText.setName(" " + MissionControllerUtils.createProbabilityString(
					humintDatum.getRedCapability_Pc(), false) + " ");
			humintText.setForegroundColor(ColorManager_Phase2.getColor(ColorManager_Phase2.HUMINT));
			humintText.setBackgroundColor(Color.WHITE);
			Annotation annotation = addAnnotationAtOrientation(humintText, AnnotationOrientation.South, humintTextLayer);
			annotation.setShowAnnotationLine(false);
			annotation.setOffset_pixels(MapConstants_Phase2.INT_ANNOTATION_OFFSET_PIXELS);
		}
	}
	
	/**
	 * 
	 */
	public void removeHumintText() {
		this.humintVisible = false;
		if(humintText != null) {
			removeAnnotationAtOrientation(AnnotationOrientation.South);
			humintText = null;
		}
	}
	
	public boolean isHumintVisible() {
		return humintVisible;
	}

	public void setHumintVisible(boolean humintVisible) {
		this.humintVisible = humintVisible;
	}

	/**
	 * @return
	 */
	public boolean isSigintCircleVisible() {
		return sigintCircle != null && sigintCircle.isVisible();
	}
	
	/**
	 * @param visible
	 * @param sigintLayer
	 */
	public void setSigintCircleVisible(boolean visible, ILayer<? super Circle> sigintLayer) {
		SigintDatum sigintDatum = blueLocation.getSigint();
		if(visible && sigintCircle == null && sigintDatum != null) {
			//Create the SIGINT circle
			boolean useFixedPixelRadius = MapConstants_Phase2.SIGINT_RADIUS_FIXED || sigintDatum.getRadius_miles() == null;
			sigintCircle = new Circle(blueLocation.getLocation(), 
					useFixedPixelRadius ? MapConstants_Phase2.SIGINT_RADIUS_PIXELS : sigintDatum.getRadius_miles(), 
					useFixedPixelRadius ? RadiusType.Pixels : RadiusType.Miles);
			sigintCircle.setId("sigint_" + id);
			sigintCircle.setBorderColor(ColorManager_Phase2.getColor(ColorManager_Phase2.SIGINT));
			if(sigintDatum.isRedActivityDetected() == null || !sigintDatum.isRedActivityDetected()) {
				//Add a single dotted-line circle for silent
				sigintCircle.setBorderLineWidth(MapConstants_Phase2.SIGINT_SILENT_LINE_STYLE.getLineWidth());
				sigintCircle.setBorderLineStyle(MapConstants_Phase2.SIGINT_SILENT_LINE_STYLE);
				if(MapConstants_Phase2.SIGINT_SILENCE_TRANSPARENCY < 1.f) {
					sigintCircle.setTransparency(MapConstants_Phase2.SIGINT_SILENCE_TRANSPARENCY);
				}
			} else {
				//Add a solid circle for chatter
				sigintCircle.setBorderLineWidth(MapConstants_Phase2.SIGINT_CHATTER_LINE_STYLE.getLineWidth());
				sigintCircle.setBorderLineStyle(MapConstants_Phase2.SIGINT_CHATTER_LINE_STYLE);
				if(MapConstants_Phase2.FILL_SIGINT_CIRCLE) {
					sigintCircle.setBackgroundColor(sigintCircle.getBorderColor());
					if(MapConstants_Phase2.SIGINT_CHATTER_TRANSPARENCY < 1.f) {
						sigintCircle.setTransparency(MapConstants_Phase2.SIGINT_CHATTER_TRANSPARENCY);
					}
				}
			}
			sigintLayer.addMapObject(sigintCircle);
		}
		if(sigintCircle != null) {
			sigintCircle.setVisible(visible);
		}		
	}
	
	/**
	 * 
	 */
	public void removeSigintCircle() {
		if(sigintCircle != null) {
			if(sigintCircle.getLayer() != null) {
				try {sigintCircle.getLayer().removeMapObject(sigintCircle);} catch(Exception ex) {}
			}
			sigintCircle = null;
		}
	}	
	
	/**
	 * Removes the OSINT, IMINT, and SIGINT overlays if present. Sets humintVisible to false.
	 */
	public void removeAllIntOverlays() {
		removeOsintLine();
		removeImintCircle();
		removeSigintCircle();
		humintVisible = false;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.app.widgets.map.phase_2.objects.Placemark#removeAllAnnotations()
	 */
	@Override
	public void removeAllAnnotations() {
		super.removeAllAnnotations();
		removeAllIntOverlays();
	}	
	
	/**
	 * @return
	 */
	public DisplayMode getDisplayMode() {
		return displayMode;
	}

	/**
	 * @param displayMode
	 */
	public void setDisplayMode(DisplayMode displayMode) {
		if(displayMode != null && this.displayMode != displayMode) {
			this.displayMode = displayMode;
			switch(displayMode) {
			case AttackOutcomeMode:				
				//borderColor = redAction == RedActionType.Attack ? ColorManager_Phase2.getColor(ColorManager_Phase2.RED_ATTACK) : 
				//	ColorManager_Phase2.getColor(ColorManager_Phase2.RED_NOT_ATTACK);					
				if(redAction == RedActionType.Attack) {					
					if(redBluePayoff != null && redBluePayoff.getWinner() != null) {
						borderColor = redBluePayoff.getWinner() == PlayerType.Red ?	ColorManager_Phase2.getColor(ColorManager_Phase2.RED_PLAYER) : 
							ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_PLAYER);	
					} else {
						borderColor = null;
					}
					backgroundColor = ColorManager_Phase2.getColor(ColorManager_Phase2.RED_ATTACK);
				} else {
					borderColor = ColorManager_Phase2.getColor(ColorManager_Phase2.RED_NOT_ATTACK);
					backgroundColor = borderColor;
				}								
				//foregroundColor = Color.BLACK;				
				//placemarkPreferredSize_pixels = (double)MapConstants_Phase2.BLUE_OUTCOME_PLACEMARK_SIZE_PIXELS;
				//setBorderLineWidth(highlighted ? highlightedLineThickness : 2.f);
				break;
			case StandardMode:
				borderColor = ColorManager_Phase2.getColor(ColorManager_Phase2.BLUE_PLAYER);
				//foregroundColor = Color.BLACK;
				backgroundColor = MapConstants_Phase2.FILL_BLUE_PLACEMARK ? borderColor : null;
				//placemarkPreferredSize_pixels = (double)MapConstants_Phase2.BLUE_PLACEMARK_SIZE_PIXELS;				
				//setBorderLineWidth(highlighted ? highlightedLineThickness : 2.f);
				break;
			}
		}		
	}

	public DisplaySize getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(DisplaySize displaySize) {
		if(displaySize != null && this.displaySize != displaySize) {
			this.displaySize = displaySize;
			switch(displaySize) {
			case BatchPlotSize:
				placemarkPreferredSize_pixels = (double)MapConstants_Phase2.BLUE_OUTCOME_PLACEMARK_SIZE_PIXELS;
				break;
			case StandardSize:
				placemarkPreferredSize_pixels = (double)MapConstants_Phase2.BLUE_PLACEMARK_SIZE_PIXELS;
				break;
			}
			objectBoundsChanged = true;
		}
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		if(this.highlighted != highlighted) {
			this.highlighted = highlighted;
			if(highlighted) {	
				previousBorderColor = borderColor;
				previousLineWidth = getBorderLineWidth();
				if(MapConstants_Phase2.FILL_BLUE_PLACEMARK) {
					borderColor = MapConstants_Phase2.HIGHLIGHT_COLOR;
				}
				setBorderLineWidth(highlightedLineThickness);
				setTextFont(MapConstants_Phase2.PLACEMARK_FONT_LARGE);
			}
			else {
				borderColor = previousBorderColor;
				setBorderLineWidth(previousLineWidth);
				setTextFont(MapConstants_Phase2.PLACEMARK_FONT);
			}
		}
	}	
	
	/**
	 * Configure the tool tip.
	 * 
	 * @param intsToShow
	 */
	public void configureToolTip(String displayName, Collection<DatumType> intsToShow) {
		StringBuilder toolTip = new StringBuilder("<html>");
		toolTip.append(displayName);		
		if(intsToShow != null && !intsToShow.isEmpty()) {
			toolTip.append("<br>");
			int i = 0;
			for(DatumType intToShow : intsToShow) {				
				boolean intShown = appendIntToToolTip(intToShow, toolTip);								
				if(intShown && i < intsToShow.size() - 1) {
					toolTip.append("<br>");
				}
				i++;
			}
		}
		toolTip.append("</html>");
		toolTipText = toolTip.toString();
	}
	
	/**
	 * @param intAdded
	 */
	public void updateToolTip(DatumType intAdded) {		
		StringBuilder toolTip = new StringBuilder(toolTipText != null ? toolTipText : "");
		if(toolTipText != null && toolTipText.endsWith("</html>")) {
			toolTip.delete(toolTipText.length()-7, toolTipText.length());
		}
		if(toolTip.length() > 0) {
			toolTip.append("<br>");
		}
		appendIntToToolTip(intAdded, toolTip);
		toolTip.append("</html>");
		toolTipText = toolTip.toString();
	}
	
	/**
	 * @param intAdded
	 * @param sb
	 * @return
	 */
	protected boolean appendIntToToolTip(DatumType intAdded, StringBuilder sb) {
		switch(intAdded) {				
		case OSINT:
			if(blueLocation.getOsint() != null) {
				sb.append(BasicIntRenderer.createIntString(blueLocation.getOsint(), true));
				return true;
			}
			break;
		case IMINT:
			if(blueLocation.getImint() != null) {
				sb.append(BasicIntRenderer.createIntString(blueLocation.getImint(), true));
				return true;
			}
			break;
		case HUMINT:
			if(humintDatum != null) {
				sb.append(BasicIntRenderer.createIntString(humintDatum, true));
				return true;
			}
			break;				
		case SIGINT:
			if(blueLocation.getSigint() != null) {
				sb.append(SigintRenderer.createSigintString(blueLocation.getSigint(), false, true));
				return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}
}
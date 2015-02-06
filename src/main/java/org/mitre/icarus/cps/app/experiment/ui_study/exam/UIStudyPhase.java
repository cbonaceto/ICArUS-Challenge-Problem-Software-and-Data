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
package org.mitre.icarus.cps.app.experiment.ui_study.exam;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.pause.IcarusPausePhase;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestPhase;

/**
 * Base class for UI study task phases.
 * task 1: percentages, no normalize (not necessary)
 * task 2: spatial, no normalize
 * task 3: spatial, normalize
 * task 4: multiple spatial blocks, normalize
 * task 5: spatial (same as 3), initial probs specified
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="UIStudyPhase", namespace="IcarusUIStudy")
@XmlType(name="UIStudyPhase", namespace="IcarusUIStudy")
@XmlSeeAlso({Task_1_Phase.class, Task_2_3_5_PhaseBase.class, Task_4_Phase.class})
public abstract class UIStudyPhase<T extends UIStudyTrial> extends IcarusTestPhase<T> {
	
	/** Probability distribution display types */
	@XmlType(name="DistributionType", namespace="IcarusUIStudy")
	public static enum DistributionType {Spatial, Percent};
	
	/** Probability UI widget types */
	@XmlType(name="WidgetType", namespace="IcarusUIStudy")
	public static enum WidgetType {SliderSpinner, SliderSpinnerOld, DistinctBars, StackedBars, Spinner, Keypad};
	
	/** Modality types */
	@XmlType(name="ModalityType", namespace="IcarusUIStudy")
	public static enum ModalityType {Mouse, MouseDrag, MouseClick, TouchClick, TouchDrag, Keypad};
	
	/** Normalization modes */
	@XmlType(name="NormalizationMode", namespace="IcarusUIStudy")
	public static enum NormalizationMode {Exact, Loose, Delayed, Dynamic, DynamicLocking};
	
	/** The URL to the tutorial file for the task (if any). FOR HUMAN SUBJECT USE ONLY. */
	protected String tutorialUrl;	
	
	/** Pause phase to show before the phase (if any) */
	protected IcarusPausePhase pausePhase;
	
	/** The Probability UI widget type */
	protected WidgetType widget;
	
	/** The modality type */
	protected ModalityType modality;
	
	/** The normalization mode */
	protected NormalizationMode normalizationMode;
	
	/** The error threshold for loose normalization mode */
	protected Double looseNormalizationErrorThreshold;
	
	/**
	 * Get the task tutorial file URL (if any).
	 *
	 * @return the tutorial file URL 
	 */
	@XmlElement(name="TutorialUrl")
	public String getTutorialUrl() {
		return tutorialUrl;
	}

	/**
	 * Set the task tutorial file URL.	
	 * 
	 * @param tutorialUrl URL to the tutorial file
	 */
	public void setTutorialUrl(String tutorialUrl) {
		this.tutorialUrl = tutorialUrl;
	}

	/**
	 * Get the pause phase (if any).	
	 * 
	 * @return the pause phase
	 */
	@XmlElement(name="Pause")
	public IcarusPausePhase getPausePhase() {
		return pausePhase;
	}
	
	/**
	 * Set the pause phase. 
	 * 
	 * @param pausePhase the pause phase
	 */
	public void setPausePhase(IcarusPausePhase pausePhase) {
		this.pausePhase = pausePhase;
	}	
	
	@XmlAttribute(name="widget")
	public WidgetType getWidget() {
		return widget;
	}

	public void setWidget(WidgetType widget) {
		this.widget = widget;
	}

	@XmlAttribute(name="modality")
	public ModalityType getModality() {
		return modality;
	}

	public void setModality(ModalityType modality) {
		this.modality = modality;
	}

	@XmlAttribute(name="normalization")
	public NormalizationMode getNormalizationMode() {
		return normalizationMode;
	}

	public void setNormalizationMode(NormalizationMode normalizationMode) {
		this.normalizationMode = normalizationMode;
	}
	
	@XmlAttribute(name="normalizationthreshold")
	public Double getLooseNormalizationErrorThreshold() {
		return looseNormalizationErrorThreshold;
	}

	public void setLooseNormalizationErrorThreshold(Double looseNormalizationErrorThreshold) {
		this.looseNormalizationErrorThreshold = looseNormalizationErrorThreshold;
	}

	public abstract DistributionType getDistributionType();
}
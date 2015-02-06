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
package org.mitre.icarus.cps.exam.phase_1.testing;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.assessment.score_computer.phase_1.GaussianFunction.Gaussian2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GridLocation2D;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Class that contains attack dispersion parameters for group attacks in Tasks 1-3.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="Task_1_2_3_AttackDispersionParameters", namespace="IcarusCPD_1")
public class Task_1_2_3_AttackDispersionParameters {

	/** The group the parameters are for*/
	protected GroupType group;

	/** The base rate */
	protected Double baseRate;

	/** The group center location */
	protected GridLocation2D centerLocation;

	/** The 2D Gaussian function used to generate likelihoods given an X, Y grid location */
	protected Gaussian2D gaussian;	
	
	/** SigmaX value used in the 2D Gaussian */
	protected Double sigmaX;
	
	/** SigmaY value used in the 2D Gaussian */
	protected Double sigmaY;
	
	/** Theta value used in the 2D Gaussian */
	protected Double theta;
	
	public Task_1_2_3_AttackDispersionParameters() {
		gaussian = new Gaussian2D();
	}

	public Task_1_2_3_AttackDispersionParameters(GroupType group) {
		this.group = group;
		gaussian = new Gaussian2D();
	}

	public Task_1_2_3_AttackDispersionParameters(GroupType group, double baseRate, GridLocation2D centerLocation) {
		this.group = group;
		this.baseRate = baseRate;
		gaussian = new Gaussian2D();
		gaussian.setX0(centerLocation.getX());
		gaussian.setY0(centerLocation.getY());		
		this.centerLocation = centerLocation;
	}

	public Task_1_2_3_AttackDispersionParameters(GroupType group, double baseRate, GridLocation2D centerLocation, 
			double sigmaX, double sigmaY, double theta) {
		this.group = group;
		this.baseRate = baseRate;
		gaussian = new Gaussian2D();
		gaussian.setX0(centerLocation.getX());
		gaussian.setY0(centerLocation.getY());
		setGaussianParameters(sigmaX, sigmaY, theta);
		this.centerLocation = centerLocation;			
	}		

	@XmlAttribute(name="group")
	public GroupType getGroup() {
		return group;
	}

	public void setGroup(GroupType group) {
		this.group = group;
	}

	@XmlAttribute(name="baseRate")
	public Double getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(Double baseRate) {
		this.baseRate = baseRate;
	}

	@XmlElement(name="CenterLocation")
	public GridLocation2D getCenterLocation() {
		return centerLocation;
	}

	public void setCenterLocation(GridLocation2D centerLocation) {
		this.centerLocation = centerLocation;
		gaussian.setX0(centerLocation.getX());
		gaussian.setY0(centerLocation.getY());
	}
	
	@XmlAttribute(name="sigmaX")
	public Double getSigmaX() {
		return sigmaX;
	}
	
	public void setSigmaX(Double sigmaX) {
		this.sigmaX = sigmaX;
		gaussian.setParameters(sigmaX == null ? 0 : sigmaX, 
				sigmaY == null ? 0 : sigmaY, theta == null ? 0 : theta);
	}
	
	@XmlAttribute(name="sigmaY")
	public Double getSigmaY() {
		return sigmaY;
	}
	
	public void setSigmaY(Double sigmaY) {
		this.sigmaY = sigmaY;
		gaussian.setParameters(sigmaX == null ? 0 : sigmaX, 
				sigmaY == null ? 0 : sigmaY, theta == null ? 0 : theta);
	}
	
	@XmlAttribute(name="theta")
	public Double getTheta() {
		return theta;
	}
	
	public void setTheta(Double theta) {
		this.theta = theta;
		gaussian.setParameters(sigmaX == null ? 0 : sigmaX, 
				sigmaY == null ? 0 : sigmaY, theta == null ? 0 : theta);
	}	

	public void setGaussianParameters(double sigmaX, double sigmaY, double theta) {
		this.sigmaX = sigmaX;
		this.sigmaY = sigmaY;
		this.theta = theta;
		gaussian.setParameters(sigmaX, sigmaY, theta);
	}

	public double computePxy(GridLocation2D location) {			
		return gaussian.getGuassianValue(location.getX(), location.getY());
		//pxy = exp(-(a*(X-ccv(1)).^2 + 2*b*(X-ccv(1)).*(Y-ccv(2)) + c*(Y-ccv(2)).^2));
		/*return Math.exp(-(a * Math.pow(location.getX() - centerLocation.getX(), 2) + 
					2 * b * (location.getX() - centerLocation.getX()) * (location.getY() - centerLocation.getY()) + 
					c * Math.pow(location.getY() - centerLocation.getY(), 2)));*/
	}
}
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
package org.mitre.icarus.cps.exam.phase_1.testing.probability_rules;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.BayesianUpdateStrategy;
import org.mitre.icarus.cps.exam.phase_1.testing.probability_rules.update_strategy.ProbabilityUpdateStrategy;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Defines the Probabilistic Rules of Behavioral Style (PROBS) that govern group attack
 * likelihoods based on each INT type.
 * 
 * @author CBONACETO
 *
 */
@XmlType(name="ProbabilityRules", namespace="IcarusCPD_1")
public class ProbabilityRules {	
	
	/**The probability update strategy to use in the ScoreComputer when computing normative solutions. 
	 * If null, Bayesian updating is used. */
	protected ProbabilityUpdateStrategy probabilityUpdateStrategy;	
	
	/** Distance decay function values for HUMINT (Missions 3-7) */
	protected HumintRules humintRules;
	
	/** IMINT rules for each group (Missions 5-7) */
	protected ArrayList<ImintRule> imintRules;
	
	/** Maps group to the IMINT rule for that group */
	protected HashMap<GroupType, ImintRule> imintRulesMap;
	
	/** MOVINT rules for each group (Missions 5-7) */
	protected ArrayList<MovintRule> movintRules;	
	
	/** Maps group to the MOVINT rule for that group */
	protected HashMap<GroupType, MovintRule> movintRulesMap;
	
	/** SIGINT rules for each group ((Missions 5-7)) */
	protected ArrayList<SigintRule> sigintRules;	
	
	/** Maps group to the SIGINT rule for that group */
	protected HashMap<GroupType, SigintRule> sigintRulesMap;
	
	/** SOCINT rules for each group (Missions 4-7) */
	protected ArrayList<SocintRule> socintRules;
	
	/** Maps group to the SOCINT rule for that group */
	protected HashMap<GroupType, SocintRule> socintRulesMap;
	
	/** WAVEINT rules for each group (Mission 7 only) */
	protected ArrayList<WaveintRule> waveintRules;
	
	/** Maps group to the WAVEINT rule for that group */
	protected HashMap<GroupType, WaveintRule> waveintRulesMap;
	
	public ProbabilityRules() {
		imintRulesMap = new HashMap<GroupType, ImintRule>();
		movintRulesMap = new HashMap<GroupType, MovintRule>();
		sigintRulesMap = new HashMap<GroupType, SigintRule>();
		socintRulesMap = new HashMap<GroupType, SocintRule>();
		waveintRulesMap = new HashMap<GroupType, WaveintRule>();
	}	
	
	/**
	 * Get the probability update strategy to use in the ScoreComputer when computing normative solutions. 
	 * If null, Bayesian updating is used. 
	 * 
	 * @return the probability update strategy
	 */
	@XmlTransient
	public ProbabilityUpdateStrategy getProbabilityUpdateStrategy() {
		return probabilityUpdateStrategy;
	}

	/**
	 * Set the probability update strategy to use in the ScoreComputer when computing normative solutions. 
	 * If null, Bayesian updating is used.
	 * 
	 * @param probabilityUpdateStrategy the probability update strategys
	 */
	public void setProbabilityUpdateStrategy(ProbabilityUpdateStrategy probabilityUpdateStrategy) {
		this.probabilityUpdateStrategy = probabilityUpdateStrategy;
	}

	/**
	 * Call this after setting the IMINT, MOVINT, SIGINT, SOCINT, and WAVEINT rules to update
	 * the mappings from each group to the INT rules for the group.
	 */
	public void updateRulesMaps() {
		imintRulesMap.clear();
		if(imintRules != null && !imintRules.isEmpty()) {
			for(ImintRule rule : imintRules) {
				imintRulesMap.put(rule.getGroup(), rule);
			}
		}
		movintRulesMap.clear();
		if(movintRules != null && !movintRules.isEmpty()) {
			for(MovintRule rule : movintRules) {
				movintRulesMap.put(rule.getGroup(), rule);
			}
		}
		sigintRulesMap.clear();
		if(sigintRules != null && !sigintRules.isEmpty()) {
			for(SigintRule rule : sigintRules) {
				sigintRulesMap.put(rule.getGroup(), rule);
			}
		}
		socintRulesMap.clear();
		if(socintRules != null && !socintRules.isEmpty()) {
			for(SocintRule rule : socintRules) {
				socintRulesMap.put(rule.getGroup(), rule);
			}
		}
		waveintRulesMap.clear();
		if(waveintRules != null && !waveintRules.isEmpty()) {
			for(WaveintRule rule : waveintRules) {
				waveintRulesMap.put(rule.getGroup(), rule);
			}
		}
	}

	/**
	 * Get the HUMINT rules.
	 * 
	 * @return the HUMINT rules
	 */
	@XmlElement(name="HumintRules")
	public HumintRules getHumintRules() {
		return humintRules;
	}
	
	/**
	 * Using HUMINT rules, get the attack likelihood given the distance
	 * from the attack location to the group center.
	 * 
	 * @param distance_miles the distance in miles
	 * @return the attack likelihood
	 */
	public double getHumintLikelihood(double distance_miles) {
		return humintRules.getAttackLikelihood(distance_miles);		
		/*double a = .4;
		double b = 0;
		double c = 10;
		return a * Math.pow(Math.E, -((Math.pow(distance_miles-b, 2)/(2 * c * c))));*/
		
		/*if(distance_miles < 20) {
			return -0.0175 * distance_miles + 0.40;
		}
		else if(distance_miles < 30) {	
			return -0.004 * distance_miles + 0.13;
		}
		return 0.01d;*/
	}
	
	public static void main(String[] args) {
		ProbabilityRules pr = ProbabilityRules.createDefaultProbabilityRules();
		System.out.println("0: " + pr.getHumintLikelihood(0));
		System.out.println("5: " + pr.getHumintLikelihood(5));
		System.out.println("10: " + pr.getHumintLikelihood(10));
		System.out.println("20: " + pr.getHumintLikelihood(20));
		System.out.println("40: " + pr.getHumintLikelihood(40));
		System.out.println("5.393079404586947E307: " + pr.getHumintLikelihood(5.393079404586947E307));
	}

	/**
	 * Set the HUMINT rules.
	 * 
	 * @param humintRules the HUMINT rules
	 */
	public void setHumintRules(HumintRules humintRules) {
		this.humintRules = humintRules;
	}

	/**
	 * Get the IMINT rules.
	 * 
	 * @return the IMINT rules
	 */
	@XmlElementWrapper(name="ImintRules")
	@XmlElement(name="ImintRule")
	public ArrayList<ImintRule> getImintRules() {
		return imintRules;
	}
	
	/**
	 * Get the IMINT rule for the given group.
	 * 
	 * @param group the group
	 * @return the IMINT rule for the group
	 */
	public ImintRule getImintRule(GroupType group) {
		return imintRulesMap.get(group);
	}

	/**
	 * Set the IMINT rules.
	 * 
	 * @param imintRules the IMINT rules
	 */
	public void setImintRules(ArrayList<ImintRule> imintRules) {
		this.imintRules = imintRules;
	}	

	/**
	 * Get the MOVINT rules.
	 * 
	 * @return the MOVINT rules
	 */
	@XmlElementWrapper(name="MovintRules")
	@XmlElement(name="MovintRule")
	public ArrayList<MovintRule> getMovintRules() {
		return movintRules;
	}
	
	/**
	 * Get the MOVINT rule for the given group.
	 * 
	 * @param group the group
	 * @return the MOVINT rule for the group
	 */
	public MovintRule getMovintRule(GroupType group) {
		return movintRulesMap.get(group);
	}

	/**
	 * Set the MOVINT rules.
	 * 
	 * @param movintRules the MOVINT rules.
	 */
	public void setMovintRules(ArrayList<MovintRule> movintRules) {
		this.movintRules = movintRules;
	}

	/**
	 * Get the SIGINT rules.
	 * 
	 * @return the SIGINT rules.
	 */
	@XmlElementWrapper(name="SigintRules")
	@XmlElement(name="SigintRule")
	public ArrayList<SigintRule> getSigintRules() {
		return sigintRules;
	}
	
	/**
	 * Get the SIGINT rule for the given group.
	 * 
	 * @param group the group
	 * @return the SIGINT rule for the group
	 */
	public SigintRule getSigintRule(GroupType group) {
		return sigintRulesMap.get(group);
	}

	/**
	 * Set the SIGINT rules.
	 * 
	 * @param sigintRules the SIGINT rules.
	 */
	public void setSigintRules(ArrayList<SigintRule> sigintRules) {
		this.sigintRules = sigintRules;
	}			

	/**
	 * Get the SOCINT rules.
	 * 
	 * @return the SOCINT rules.
	 */
	@XmlElementWrapper(name="SocintRules")
	@XmlElement(name="SocintRule")
	public ArrayList<SocintRule> getSocintRules() {
		return socintRules;
	}
	
	/**
	 * Get the SOCINT rule for the given group.
	 * 
	 * @param group the group
	 * @return the SOCINT rule for the group
	 */
	public SocintRule getSocintRule(GroupType group) {
		return socintRulesMap.get(group);
	}

	/**
	 * Set the SOCINT rules.
	 * 
	 * @param socintRules the SOCINT rules.
	 */
	public void setSocintRules(ArrayList<SocintRule> socintRules) {
		this.socintRules = socintRules;
	}
	
	/**
	 * Get the WAVEINT rules.
	 * 
	 * @return the WAVEINT rules.
	 */
	@XmlElementWrapper(name="WaveintRules")
	@XmlElement(name="WaveintRule")
	public ArrayList<WaveintRule> getWaveintRules() {
		return waveintRules;
	}
	
	/**
	 * Get the WAVEINT rule for the given group.
	 * 
	 * @param group the group
	 * @return the WAVEINT rule for the group
	 */
	public WaveintRule getWaveintRule(GroupType group) {
		return waveintRulesMap.get(group);
	}

	/**
	 * Set the WAVEINT rules.
	 * 
	 * @param waveintRules the WAVEINT rules.
	 */
	public void setWaveintRules(ArrayList<WaveintRule> waveintRules) {
		this.waveintRules = waveintRules;
	}
	
	/**
	 * Create the default probability rules for each INT type.
	 * 
	 * @return the default probability rules
	 */
	public static ProbabilityRules createDefaultProbabilityRules() {
		ProbabilityRules rules = new ProbabilityRules();
		
		rules.probabilityUpdateStrategy = new BayesianUpdateStrategy();
		
		rules.humintRules = new HumintRules(0.4d, 0.d, 10.d);
		
		rules.imintRules = new ArrayList<ImintRule>(4);
		rules.imintRules.add(new ImintRule(GroupType.A, 0.4D, 0.1D));
		rules.imintRules.add(new ImintRule(GroupType.B, 0.4D, 0.1D));
		rules.imintRules.add(new ImintRule(GroupType.C, 0.1D, 0.4D));
		rules.imintRules.add(new ImintRule(GroupType.D, 0.1D, 0.4D));
		
		rules.movintRules = new ArrayList<MovintRule>(4);
		rules.movintRules.add(new MovintRule(GroupType.A, 0.4D, 0.1D));
		rules.movintRules.add(new MovintRule(GroupType.B, 0.1D, 0.4D));
		rules.movintRules.add(new MovintRule(GroupType.C, 0.4D, 0.1D));
		rules.movintRules.add(new MovintRule(GroupType.D, 0.1D, 0.4D));
		
		rules.sigintRules = new ArrayList<SigintRule>(4);
		rules.sigintRules.add(new SigintRule(GroupType.A, 0.7D, 0.3D, 0.1D, 0.9D));
		rules.sigintRules.add(new SigintRule(GroupType.B, 0.7D, 0.3D, 0.1D, 0.9D));
		rules.sigintRules.add(new SigintRule(GroupType.C, 0.7D, 0.3D, 0.1D, 0.9D));
		rules.sigintRules.add(new SigintRule(GroupType.D, 0.7D, 0.3D, 0.1D, 0.9D));
		
		rules.socintRules = new ArrayList<SocintRule>(4);
		rules.socintRules.add(new SocintRule(GroupType.A, 0.4D, 0.2D));
		rules.socintRules.add(new SocintRule(GroupType.B, 0.4D, 0.2D));
		rules.socintRules.add(new SocintRule(GroupType.C, 0.4D, 0.2D));
		rules.socintRules.add(new SocintRule(GroupType.D, 0.4D, 0.2D));
		
		rules.waveintRules = new ArrayList<WaveintRule>(4);
		rules.waveintRules.add(new WaveintRule(GroupType.A, 0.85D, 0.05D));
		rules.waveintRules.add(new WaveintRule(GroupType.B, 0.85D, 0.05D));
		rules.waveintRules.add(new WaveintRule(GroupType.C, 0.85D, 0.05D));
		rules.waveintRules.add(new WaveintRule(GroupType.D, 0.85D, 0.05D));
		
		rules.updateRulesMaps();
		return rules;
	}
}
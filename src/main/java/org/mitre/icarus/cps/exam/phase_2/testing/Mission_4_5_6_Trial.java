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
package org.mitre.icarus.cps.exam.phase_2.testing;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.phase_2.testing.bluebook.RedTacticType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.HumintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.ImintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.OsintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.AbstractRedTacticsProbe;

/**
 * A trial in Mission 4, 5, or 6. Setters and getters in the IcarusTestTrial_Phase2 base class are overridden just
 * to enable the correct ordering of elements in XML files.
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Mission_4_5_6_Trial", namespace="IcarusCPD_2")
@XmlType(name="Mission_4_5_6_Trial", namespace="IcarusCPD_2",
	propOrder={"redTacticsProbe", "osintPresentation", "imintPresentation", 
		"attackPropensityProbe_Pp",	"humintPresentation", 
		"attackProbabilityProbe_Ppc", "sigintSelectionProbe",
		"sigintPresentation", "attackProbabilityProbe_Pt", "attackProbabilityProbe_Ptpc",
		"blueActionSelection", "redActionSelection", "showdownWinner", "redTactic"})
public class Mission_4_5_6_Trial extends IcarusTestTrial_Phase2 {	
	
	/** A report on the tactic that Red may be playing with based on Red's history of attacks */
	protected AbstractRedTacticsProbe redTacticsProbe;
	
	/** The tactic Red is playing with on the trial (ground truth, not revealed to the participant) */
	protected RedTacticType redTactic;

	/**
	 * Get the probe to report the tactic Red may be playing with based on the history of Red's attacks.
	 * 
	 * @return the Red tactics probe
	 */
	@XmlElement(name="RedTacticsProbe")
	public AbstractRedTacticsProbe getRedTacticsProbe() {
		return redTacticsProbe;
	}

	/**
	 * Set the probe to report the tactic Red may be playing with based on the history of Red's attacks.
	 * 
	 * @param redTacticsProbe the Red tactics probe
	 */
	public void setRedTacticsProbe(AbstractRedTacticsProbe redTacticsProbe) {
		this.redTacticsProbe = redTacticsProbe;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getOsintPresentation()
	 */
	@Override
	@XmlElement(name="OsintPresentation")	
	public List<OsintPresentationProbe> getOsintPresentation() {
		return osintPresentation;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setOsintPresentation(java.util.List)
	 */
	@Override
	public void setOsintPresentation(List<OsintPresentationProbe> osintPresentation) {
		this.osintPresentation = osintPresentation;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getImintPresentation()
	 */
	@Override
	@XmlElement(name="ImintPresentation")
	public List<ImintPresentationProbe> getImintPresentation() {
		return imintPresentation;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setImintPresentation(java.util.List)
	 */
	@Override
	public void setImintPresentation(List<ImintPresentationProbe> imintPresentation) {		
		this.imintPresentation = imintPresentation;
	}	

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getAttackPropensityProbe_Pp()
	 */
	@Override
	@XmlElement(name="AttackPropensityProbe_Pp")
	public AttackProbabilityReportProbe getAttackPropensityProbe_Pp() {
		return attackPropensityProbe_Pp;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setAttackPropensityProbe_Pp(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe)
	 */
	@Override
	public void setAttackPropensityProbe_Pp(AttackProbabilityReportProbe attackPropensityProbe_Pp) {
		this.attackPropensityProbe_Pp = attackPropensityProbe_Pp;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getHumintPresentation()
	 */
	@Override
	@XmlElement(name="HumintPresentation")
	public List<HumintPresentationProbe> getHumintPresentation() {
		return humintPresentation;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setHumintPresentation(java.util.List)
	 */
	@Override
	public void setHumintPresentation(List<HumintPresentationProbe> humintPresentation) {
		this.humintPresentation = humintPresentation;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getAttackProbabilityProbe_Ppc()
	 */
	@Override
	@XmlElement(name="AttackProbabilityProbe_Ppc")
	public AttackProbabilityReportProbe getAttackProbabilityProbe_Ppc() {
		return attackProbabilityProbe_Ppc;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setAttackProbabilityProbe_Ppc(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe)
	 */
	@Override
	public void setAttackProbabilityProbe_Ppc(AttackProbabilityReportProbe attackProbabilityProbe_Ppc) {
		this.attackProbabilityProbe_Ppc = attackProbabilityProbe_Ppc;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getSigintSelectionProbe()
	 */
	@Override
	@XmlElement(name="SigintSelectionProbe")
	public SigintSelectionProbe getSigintSelectionProbe() {
		return sigintSelectionProbe;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setSigintSelectionProbe(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe)
	 */
	@Override
	public void setSigintSelectionProbe(SigintSelectionProbe sigintSelectionProbe) {
		this.sigintSelectionProbe = sigintSelectionProbe;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getSigintPresentation()
	 */
	@Override
	@XmlElement(name="SigintPresentation")
	public List<SigintPresentationProbe> getSigintPresentation() {
		return sigintPresentation;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setSigintPresentation(java.util.List)
	 */
	@Override
	public void setSigintPresentation(List<SigintPresentationProbe> sigintPresentation) {
		this.sigintPresentation = sigintPresentation;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getAttackProbabilityProbe_Pt()
	 */
	@Override
	@XmlElement(name="AttackProbabilityProbe_Pt")
	public AttackProbabilityReportProbe getAttackProbabilityProbe_Pt() {
		return attackProbabilityProbe_Pt;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setAttackProbabilityProbe_Pt(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe)
	 */
	@Override
	public void setAttackProbabilityProbe_Pt(AttackProbabilityReportProbe attackProbabilityProbe_Pt) {
		this.attackProbabilityProbe_Pt = attackProbabilityProbe_Pt;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getAttackProbabilityProbe_Ptpc()
	 */
	@Override
	@XmlElement(name="AttackProbabilityProbe_Ptpc")
	public AttackProbabilityReportProbe getAttackProbabilityProbe_Ptpc() {
		return attackProbabilityProbe_Ptpc;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setAttackProbabilityProbe_Ptpc(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe)
	 */
	@Override
	public void setAttackProbabilityProbe_Ptpc(AttackProbabilityReportProbe attackProbabilityProbe_Ptpc) {
		this.attackProbabilityProbe_Ptpc = attackProbabilityProbe_Ptpc;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getBlueActionSelection()
	 */
	@Override
	@XmlElement(name="BlueActionSelection")
	public BlueActionSelectionProbe getBlueActionSelection() {
		return blueActionSelection;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setBlueActionSelection(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe)
	 */
	@Override
	public void setBlueActionSelection(BlueActionSelectionProbe blueActionSelection) {
		this.blueActionSelection = blueActionSelection;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getRedActionSelection()
	 */
	@Override
	@XmlElement(name="RedActionSelection")
	public RedActionSelectionProbe getRedActionSelection() {
		return redActionSelection;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setRedActionSelection(org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedActionSelectionProbe)
	 */
	@Override
	public void setRedActionSelection(RedActionSelectionProbe redActionSelection) {
		this.redActionSelection = redActionSelection;
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#getShowdownWinner()
	 */
	@Override
	@XmlElement(name="ShowdownWinner")
	public List<ShowdownWinner> getShowdownWinner() {
		return showdownWinner;
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#setShowdownWinner(java.util.List)
	 */
	@Override
	public void setShowdownWinner(List<ShowdownWinner> showdownWinner) {
		this.showdownWinner = showdownWinner;
	}
	
	/**
	 * Get the tactic Red is playing with on the trial (ground truth, not revealed to the participant).
	 * 
	 * @return the tactic Red is playing with
	 */
	@XmlElement(name="RedTactic")
	public RedTacticType getRedTactic() {
		return redTactic;
	}	

	/**
	 * Set the tactic Red is playing with on the trial (ground truth, not revealed to the participant).
	 * 
	 * @param redTactic the tactic Red is playing with
	 */
	public void setRedTactic(RedTacticType redTactic) {
		this.redTactic = redTactic;
	}	
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#isAdditionalResponseDataPresent()
	 */
	@Override
	protected boolean isAdditionalResponseDataPresent() {
		return (redTacticsProbe != null && redTacticsProbe.isResponsePresent());
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {
		if(redTacticsProbe != null) {
			redTacticsProbe.clearResponseData();
		}
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2)
	 */
	@Override
	protected void copyAdditionalResponseData(IcarusTestTrial_Phase2 trial) {
		if(redTacticsProbe != null && trial != null && 
				trial instanceof Mission_4_5_6_Trial) {
			redTacticsProbe.copyResponseData(
					((TrialPartProbe)((Mission_4_5_6_Trial)trial).redTacticsProbe));
		}
	}
}
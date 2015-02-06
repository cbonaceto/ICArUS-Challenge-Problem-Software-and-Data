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

import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.HumintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.ImintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.OsintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.red_tactics_probes.MostLikelyRedTacticProbe;

/**
 * A trial in Mission 1, 2, or 3. Setters and getters in the IcarusTestTrial_Phase2 base class are overridden just
 * to enable the correct ordering of elements in XML files. 
 * 
 * @author CBONACETO
 *
 */
@XmlRootElement(name="Mission_1_2_3_Trial", namespace="IcarusCPD_2")
@XmlType(name="Mission_1_2_3_Trial", namespace="IcarusCPD_2",
	propOrder={"osintPresentation", "imintPresentation", 
			"mostLikelyRedTacticProbe", "attackPropensityProbe_Pp",
			"humintPresentation", "attackProbabilityProbe_Ppc", "sigintSelectionProbe",
			"sigintPresentation", "attackProbabilityProbe_Pt", "attackProbabilityProbe_Ptpc",
			"blueActionSelection", "redActionSelection", "showdownWinner"}) 
public class Mission_1_2_3_Trial extends IcarusTestTrial_Phase2 {
	
	/** Probe on whether it is more likely that Red is playing as passive or aggressive */
	protected MostLikelyRedTacticProbe mostLikelyRedTacticProbe;		
	
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

	/**
	 * Get the probe on the most likely tactic Red is playing with based on the history of Red's attacks (Mission 2 only).
	 * 
	 * @return the most likely Red tactic probe
	 */
	@XmlElement(name="MostLikelyRedTacticProbe")
	public MostLikelyRedTacticProbe getMostLikelyRedTacticProbe() {
		return mostLikelyRedTacticProbe;
	}

	/**
	 * Set the probe on the most likely tactic Red is playing with based on the history of Red's attacks (Mission 2 only).
	 * 
	 * @param mostLikelyRedTacticProbe the most likely Red tactic probe
	 */
	public void setMostLikelyRedTacticProbe(MostLikelyRedTacticProbe mostLikelyRedTacticProbe) {
		this.mostLikelyRedTacticProbe = mostLikelyRedTacticProbe;
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

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#isAdditionalResponseDataPresent()
	 */
	@Override
	protected boolean isAdditionalResponseDataPresent() {
		return (mostLikelyRedTacticProbe != null && mostLikelyRedTacticProbe.isResponsePresent());
	}

	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#clearAdditionalResponseData()
	 */
	@Override
	protected void clearAdditionalResponseData() {		
		if(mostLikelyRedTacticProbe != null) {
			mostLikelyRedTacticProbe.clearResponseData();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2#copyAdditionalResponseData(org.mitre.icarus.cps.exam.phase_2.testing.IcarusTestTrial_Phase2)
	 */
	@Override
	protected void copyAdditionalResponseData(IcarusTestTrial_Phase2 trial) {
		if(mostLikelyRedTacticProbe != null && trial != null && trial instanceof Mission_1_2_3_Trial) {
			mostLikelyRedTacticProbe.copyResponseData(
					((Mission_1_2_3_Trial)trial).mostLikelyRedTacticProbe);
		}
	}
}
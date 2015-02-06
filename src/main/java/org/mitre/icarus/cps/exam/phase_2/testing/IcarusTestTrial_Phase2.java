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

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.mitre.icarus.cps.exam.base.response.ResponseGeneratorData;
import org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial;
import org.mitre.icarus.cps.exam.phase_2.feedback.TrialFeedback_Phase2;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.BlueActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.AttackProbabilityReportProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.RedActionSelectionProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.HumintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.ImintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.OsintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintPresentationProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.int_presentation.SigintSelectionProbe;
import org.mitre.icarus.cps.feature_vector.phase_2.BlueLocation;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.HumintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.ImintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.IntDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.OsintDatum;
import org.mitre.icarus.cps.feature_vector.phase_2.int_datum.SigintDatum;

/**
 * Abstract base class for test trials in Phase 2 Missions.
 *
 * @author CBONACETO
 *
 */
@XmlRootElement(name = "IcarusTestTrial_Phase2", namespace = "IcarusCPD_2")
@XmlType(name = "IcarusTestTrial_Phase2", namespace = "IcarusCPD_2",
        propOrder = {"responseGenerator", "startTime", "endTime", "trialTime_ms",
            "responseFeedBack", "humint"})
@XmlSeeAlso({Mission_1_2_3_Trial.class, Mission_4_5_6_Trial.class})
public abstract class IcarusTestTrial_Phase2 extends IcarusTestTrial {

    /**
     * The response generator for the trial (ICAruS system or human subject)
     */
    private ResponseGeneratorData responseGenerator;

    /**
     * The trial start time
     */
    private Date startTime;

    /**
     * The trial end time
     */
    private Date endTime;

    /**
     * The total amount of time spent on the trial in ms (collected for human
     * subjects only)
     */
    private Long trialTime_ms;

    /**
     * Whether this is a "attack history" trial. Attack history trials are used
     * in Mission 6 to create a history of previous attacks when assessing Red
     * tactics on a probe trial.
     */
    protected Boolean attackHistoryTrial;

    /**
     * The Blue location(s) (from the feature vector file). Each location
     * contains the OSINT, IMINT, and SIGINT data associated with the location
     */
    protected List<BlueLocation> blueLocations;

    /**
     * The probability that Red has the capability to attack at any location on
     * this trial (from HUMINT, Pc)
     */
    protected HumintDatum humint;

    /**
     * OSINT presentation(s) at each location
     */
    protected List<OsintPresentationProbe> osintPresentation;

    /**
     * IMINT presentation(s) at each location
     */
    protected List<ImintPresentationProbe> imintPresentation;

    /**
     * Blue's report on Red's probability to attack at each location (reported
     * by participant) (Pp), based on OSINT vulnerability (P), IMINT opportunity
     * (U), and the BLUEBOOK
     */
    protected AttackProbabilityReportProbe attackPropensityProbe_Pp;

    /**
     * HUMINT presentation(s) at each location
     */
    protected List<HumintPresentationProbe> humintPresentation;

    /**
     * Blue's report on Red's probability to attack at each location (reported
     * by the participant) (Pp,c), considers the propensity to attack (Pp) and
     * the Red capability to attack (HUMINT, Pc)
     */
    protected AttackProbabilityReportProbe attackProbabilityProbe_Ppc;

    /**
     * SIGINT selection at one or more locations
     */
    protected SigintSelectionProbe sigintSelectionProbe;

    /**
     * SIGINT presentation(s) at each location
     */
    protected List<SigintPresentationProbe> sigintPresentation;

    /**
     * Blue's report on the probability of Red attack at each location based
     * only on SIGINT activity (reported by participant) (Pt)
     */
    protected AttackProbabilityReportProbe attackProbabilityProbe_Pt;

    /**
     * Blue's report on the probability of Red attack at each location based on
     * all information (reported by participant) (Pt,p,c), considers all factors
     * of activity, propensity, and capability
     */
    protected AttackProbabilityReportProbe attackProbabilityProbe_Ptpc;

    /**
     * The Blue action taken (divert, ~divert) at each location (may be decided
     * by the system [Mission 1] or the participant)
     */
    protected BlueActionSelectionProbe blueActionSelection;

    /**
     * The Red action taken (attack at one or no locations) (decided by the
     * system, not the participant)
     */
    protected RedActionSelectionProbe redActionSelection;

    /**
     * If there is a show-down at a location, contains the player (Blue or Red)
     * who will win the show-down at each location
     */
    protected List<ShowdownWinner> showdownWinner;

    /**
     * Feedback provided by the test harness on the subject/model response to
     * the trial
     */
    protected TrialFeedback_Phase2 responseFeedBack;

    /**
     * Get the response generator for the trial (ICAruS system or human
     * subject).
     *
     * @return the response generator
     */
    @XmlElement(name = "ResponseGenerator")
    public ResponseGeneratorData getResponseGenerator() {
        return responseGenerator;
    }

    /**
     * Set the response generator for the trial (ICAruS system or human
     * subject).
     *
     * @param responseGenerator the response generator
     */
    public void setResponseGenerator(ResponseGeneratorData responseGenerator) {
        this.responseGenerator = responseGenerator;
    }

    /**
     * Get the trial start time.
     *
     * @return the start time
     */
    @XmlAttribute(name = "startTime")
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Set the trial start time.
     *
     * @param startTime the trial start time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Get the trial end time.
     *
     * @return the trial end time
     */
    @XmlAttribute(name = "endTime")
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Set the trial end time.
     *
     * @param endTime the trial end time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Get the total amount of time spent on the trial in ms (collected for
     * human subjects only).
     *
     * @return the time in ms
     */
    @XmlAttribute(name = "trialTime_ms")
    public Long getTrialTime_ms() {
        return trialTime_ms;
    }

    /**
     * Get the total amount of time spent on the trial in ms (collected for
     * human subjects only).
     *
     * @param trialTime_ms the time in ms
     */
    public void setTrialTime_ms(Long trialTime_ms) {
        this.trialTime_ms = trialTime_ms;
    }

    /**
     * Get whether this is an "attack history" trial. Attack history trials are
     * used in Mission 6 to create a history of previous attacks when assessing
     * Red tactics on a probe trial.
     *
     * @return whether this is an "attack history" trial
     */
    @XmlAttribute(name = "attackHistoryTrial")
    public Boolean isAttackHistoryTrial() {
        return attackHistoryTrial;
    }

    /**
     * Set whether this is an "attack history" trial. Attack history trials are
     * used in Mission 6 to create a history of previous attacks when assessing
     * Red tactics on a probe trial.
     *
     * @param attackHistoryTrial whether this is an "attack history" trial
     */
    public void setAttackHistoryTrial(Boolean attackHistoryTrial) {
        this.attackHistoryTrial = attackHistoryTrial;
    }

    /**
     * Convenience method to get a Blue location with the given location ID.
     *
     * @param locationId the location ID
     * @return a Blue location with the given location ID
     */
    public BlueLocation getBlueLocation(String locationId) {
        if (locationId != null && blueLocations != null) {
            for (BlueLocation location : blueLocations) {
                if (locationId.equals(location.getId())) {
                    return location;
                }
            }
        }
        return null;
    }

    /**
     * Convenience method to get the number of blue locations for the trial.
     * This should be the same for all trials in the Mission.
     *
     * @return the number of Blue locations
     */
    public int getNumBlueLocations() {
        return blueLocations != null ? blueLocations.size() : 0;
    }

    /**
     * Get the Blue locations for the trial. The locations are initialized from
     * the Blue locations feature vector file.
     *
     * @return the Blue locations
     */
    @XmlTransient
    public List<BlueLocation> getBlueLocations() {
        return blueLocations;
    }

    /**
     * Set the Blue locations for the trial.
     *
     * @param blueLocations the Blue locations
     */
    public void setBlueLocations(List<BlueLocation> blueLocations) {
        this.blueLocations = blueLocations;
    }

    /**
     * Get the probability that Red has the capability to attack at any location
     * on this trial (from HUMINT, Pc).
     *
     * @return the HUMINT
     */
    @XmlElement(name = "Humint")
    public HumintDatum getHumint() {
        return humint;
    }

    /**
     * Set the probability that Red has the capability to attack at any location
     * on this trial (from HUMINT, Pc).
     *
     * @param humint the HUMINT
     */
    public void setHumint(HumintDatum humint) {
        this.humint = humint;
    }

    /**
     * Convenience method to get the INT data for the given INT type and
     * location.
     *
     * @param intType the INT type (OSINT, IMINT, HUMINT, or SIGINT)
     * @param locationId the ID of the location
     * @return the OSINT, IMINT, HUMINT, or SIGINT at the location
     */
    public IntDatum getInt(DatumType intType, String locationId) {
        switch (intType) {
            case HUMINT:
                return getHumint(locationId);
            case IMINT:
                return getImint(locationId);
            case OSINT:
                return getOsint(locationId);
            case SIGINT:
                return getSigint(locationId);
            default:
                return null;
        }
    }

    /**
     * Convenience method to get the OSINT data for the given location.
     *
     * @param locationId the ID of the location
     * @return the OSINT at the location
     */
    public OsintDatum getOsint(String locationId) {
        BlueLocation location = getBlueLocation(locationId);
        return location != null ? location.getOsint() : null;
    }

    /**
     * Convenience method to get the IMINT data for the given location.
     *
     * @param locationId the ID of the location
     * @return the IMINT at the location
     */
    public ImintDatum getImint(String locationId) {
        BlueLocation location = getBlueLocation(locationId);
        return location != null ? location.getImint() : null;
    }

    /**
     * Convenience method to get the HUMINT for the trial (the same for all
     * locations).
     *
     * @param locationId the location ID is ignored
     * @return the HUMINT for the trial
     */
    public HumintDatum getHumint(String locationId) {
        return humint;
    }

    /**
     * Convenience method to get the SIGINT data for the given location.
     *
     * @param locationId the ID of the location
     * @return the SIGINT at the location
     */
    public SigintDatum getSigint(String locationId) {
        BlueLocation location = getBlueLocation(locationId);
        return location != null ? location.getSigint() : null;
    }

    /**
     * Convenience method to get an attack probability report with the given
     * datum identification.
     *
     * @param datumIdentifier the datum identifier
     * @return the attack probability report with the given datum identification
     */
    public AttackProbabilityReportProbe getAttackProbabilityReportProbe(DatumIdentifier datumIdentifier) {
        switch (datumIdentifier.getDatumType()) {
            case AttackProbabilityReport_Activity:
                return attackProbabilityProbe_Pt;
            case AttackProbabilityReport_Activity_Capability_Propensity:
                return attackProbabilityProbe_Ptpc;
            case AttackProbabilityReport_Capability_Propensity:
                return attackProbabilityProbe_Ppc;
            case AttackProbabilityReport_Propensity:
                return attackPropensityProbe_Pp;
            default:
                return null;
        }
    }

    /**
     * Get the OSINT presentation(s) at each location.
     *
     * @return the OSINT presentation(s)
     */
    @XmlTransient
    public List<OsintPresentationProbe> getOsintPresentation() {
        return osintPresentation;
    }

    /**
     * Set the OSINT presentation(s) at each location.
     *
     * @param osintPresentation the OSINT presentation(s)
     */
    public void setOsintPresentation(List<OsintPresentationProbe> osintPresentation) {
        this.osintPresentation = osintPresentation;
    }

    /**
     * Get the IMINT presentation(s) at each location.
     *
     * @return the IMINT presentation(s)
     */
    @XmlTransient
    public List<ImintPresentationProbe> getImintPresentation() {
        return imintPresentation;
    }

    /**
     * Set the IMINT presentation(s) at each location.
     *
     * @param imintPresentation the IMINT presentation(s)
     */
    public void setImintPresentation(List<ImintPresentationProbe> imintPresentation) {
        this.imintPresentation = imintPresentation;
    }

    /**
     * Get Blue's report on Red's propensity to attack at each location (Pp),
     * which considers OSINT vulnerability (P), IMINT opportunity (U), and the
     * BLUEBOOK.
     *
     * @return the Red attack propensity probe (Pp)
     */
    @XmlTransient
    public AttackProbabilityReportProbe getAttackPropensityProbe_Pp() {
        return attackPropensityProbe_Pp;
    }

    /**
     * Set Blue's report on Red's propensity to attack at each location (Pp),
     * which considers OSINT vulnerability (P), IMINT opportunity (U), and the
     * BLUEBOOK.
     *
     * @param attackPropensityProbe_Pp the Red attack propensity probe (Pp)
     */
    public void setAttackPropensityProbe_Pp(AttackProbabilityReportProbe attackPropensityProbe_Pp) {
        this.attackPropensityProbe_Pp = attackPropensityProbe_Pp;
    }

    /**
     * Get the HUMINT presentation(s) at each location.
     *
     * @return the HUMINT presentations
     */
    @XmlTransient
    public List<HumintPresentationProbe> getHumintPresentation() {
        return humintPresentation;
    }

    /**
     * Set the HUMINT presentation(s) at each location.
     *
     * @param humintPresentation the HUMINT presentations
     */
    public void setHumintPresentation(List<HumintPresentationProbe> humintPresentation) {
        this.humintPresentation = humintPresentation;
    }

    /**
     * Get Blue's report on Red's probability to attack at each location (Pp,c),
     * which considers the Red propensity to attack (Pp) and the Red capability
     * to attack (Pc).
     *
     * @return the Red attack probability report probe (Pp,c)
     */
    @XmlTransient
    public AttackProbabilityReportProbe getAttackProbabilityProbe_Ppc() {
        return attackProbabilityProbe_Ppc;
    }

    /**
     * Set Blue's report on Red's probability to attack at each location (Pp,c),
     * which considers the Red propensity to attack (Pp) and the Red capability
     * to attack (Pc).
     *
     * @param attackProbabilityProbe_Ppc the Red attack probability report probe
     * (Pp,c)
     */
    public void setAttackProbabilityProbe_Ppc(AttackProbabilityReportProbe attackProbabilityProbe_Ppc) {
        this.attackProbabilityProbe_Ppc = attackProbabilityProbe_Ppc;
    }

    /**
     * @return
     */
    @XmlTransient
    public SigintSelectionProbe getSigintSelectionProbe() {
        return sigintSelectionProbe;
    }

    /**
     * @param sigintSelectionProbe
     */
    public void setSigintSelectionProbe(SigintSelectionProbe sigintSelectionProbe) {
        this.sigintSelectionProbe = sigintSelectionProbe;
    }

    /**
     * Get the SIGINT presentation(s) at each location.
     *
     * @return the SIGINT presentation(s)
     */
    @XmlTransient
    public List<SigintPresentationProbe> getSigintPresentation() {
        return sigintPresentation;
    }

    /**
     * Set the SIGINT presentation(s) at each location.
     *
     * @param sigintPresentation the SIGINT presentation(s)
     */
    public void setSigintPresentation(List<SigintPresentationProbe> sigintPresentation) {
        this.sigintPresentation = sigintPresentation;
    }

    /**
     * Get Blue's report on the probability of Red attack at each location based
     * only on SIGINT activity (Pt).
     *
     * @return the Red attack probability report probe (Pt)
     */
    @XmlTransient
    public AttackProbabilityReportProbe getAttackProbabilityProbe_Pt() {
        return attackProbabilityProbe_Pt;
    }

    /**
     * Set Blue's report on the probability of Red attack at each location based
     * only on SIGINT activity (Pt).
     *
     * @param attackProbabilityProbe_Pt the Red attack probability report probe
     * (Pt)
     */
    public void setAttackProbabilityProbe_Pt(AttackProbabilityReportProbe attackProbabilityProbe_Pt) {
        this.attackProbabilityProbe_Pt = attackProbabilityProbe_Pt;
    }

    /**
     * Get Blue's report on the probability of Red attack at each location based
     * on all information (Pt,p,c), which considers all factors of activity,
     * propensity, and capability.
     *
     * @return the Red attack probability report probe (Pt,p,c)
     */
    @XmlTransient
    public AttackProbabilityReportProbe getAttackProbabilityProbe_Ptpc() {
        return attackProbabilityProbe_Ptpc;
    }

    /**
     * Set Blue's report on the probability of Red attack at each location based
     * on all information (Pt,p,c), which considers all factors of activity,
     * propensity, and capability.
     *
     * @param attackProbabilityProbe_Ptpc the Red attack probability report
     * probe (Pt,p,c)
     */
    public void setAttackProbabilityProbe_Ptpc(AttackProbabilityReportProbe attackProbabilityProbe_Ptpc) {
        this.attackProbabilityProbe_Ptpc = attackProbabilityProbe_Ptpc;
    }

    /**
     * Get the Blue action taken (divert, ~divert) at each location (may be
     * decided by the system [Mission 1] or the participant).
     *
     * @return the Blue action take at each location
     */
    @XmlTransient
    public BlueActionSelectionProbe getBlueActionSelection() {
        return blueActionSelection;
    }

    /**
     * Set the Blue action taken (divert, ~divert) at each location (may be
     * decided by the system [Mission 1] or the participant).
     *
     * @param blueActionSelection the Blue action taken at each location
     */
    public void setBlueActionSelection(BlueActionSelectionProbe blueActionSelection) {
        this.blueActionSelection = blueActionSelection;
    }

    /**
     * Get the Red action taken (attack at one or no locations) (decided by the
     * system, not the participant).
     *
     * @return the Red action taken
     */
    @XmlTransient
    public RedActionSelectionProbe getRedActionSelection() {
        return redActionSelection;
    }

    /**
     * Set the Red action taken (attack at one or no locations) (decided by the
     * system, not the participant).
     *
     * @param redActionSelection the Red action taken
     */
    public void setRedActionSelection(RedActionSelectionProbe redActionSelection) {
        this.redActionSelection = redActionSelection;
    }

    /**
     * Get the player (Blue or Red) who will win the show-down at each location
     * (if there is a show-down).
     *
     * @return the player who will win the show-down at each location
     */
    @XmlTransient
    public List<ShowdownWinner> getShowdownWinner() {
        return showdownWinner;
    }

    /**
     * Set the player (Blue or Red) who will win the show-down at each location
     * (if there is a show-down).
     *
     * @param showdownWinner the player who will win the show-down at each
     * location
     */
    public void setShowdownWinner(List<ShowdownWinner> showdownWinner) {
        this.showdownWinner = showdownWinner;
    }

    /**
     * Get the feedback provided by the test harness on the subject/model
     * response to the trial.
     *
     * @return the feedback
     */
    @XmlElement(name = "ResponseFeedback")
    public TrialFeedback_Phase2 getResponseFeedBack() {
        return responseFeedBack;
    }

    /**
     * Set the feedback provided by the test harness on the subject/model
     * response to the trial.
     *
     * @param responseFeedBack the feedback
     */
    public void setResponseFeedBack(TrialFeedback_Phase2 responseFeedBack) {
        this.responseFeedBack = responseFeedBack;
    }

    /**
     * Get whether response data is present by checking to see if any trial part
     * probe that the participant responds to contains response data.
     *
     * @return whether response data is present for the trial
     */
    public boolean isResponsePresent() {
        return (attackPropensityProbe_Pp != null && attackPropensityProbe_Pp.isResponsePresent())
                || (attackProbabilityProbe_Ppc != null && attackProbabilityProbe_Ppc.isResponsePresent())
                || (sigintSelectionProbe != null && sigintSelectionProbe.isResponsePresent())
                || (attackProbabilityProbe_Pt != null && attackProbabilityProbe_Pt.isResponsePresent())
                || (attackProbabilityProbe_Ptpc != null && attackProbabilityProbe_Ptpc.isResponsePresent())
                || (blueActionSelection != null && blueActionSelection.isResponsePresent()
                || isAdditionalResponseDataPresent());
    }

    /**
     * Subclasses may override this method to return if any additional response
     * data is present.
     *
     * @return whether any additional response data is present
     */
    protected abstract boolean isAdditionalResponseDataPresent();

    /**
     * Copy the response data in the given trial to this trial.
     *
     * @param trial a trial
     */
    public void copyResponseData(IcarusTestTrial_Phase2 trial) {
        if (trial != null) {
            responseGenerator = trial.responseGenerator;
            startTime = trial.startTime;
            endTime = trial.endTime;
            trialTime_ms = trial.trialTime_ms;
            if (osintPresentation != null && !osintPresentation.isEmpty()
                    && trial.osintPresentation != null && !trial.osintPresentation.isEmpty()) {
                int numIntPresentations = Math.min(osintPresentation.size(), trial.osintPresentation.size());
                for (int i = 0; i < numIntPresentations; i++) {
                    osintPresentation.get(i).copyResponseData(trial.osintPresentation.get(i));
                }
            }
            if (imintPresentation != null && !imintPresentation.isEmpty()
                    && trial.imintPresentation != null && !trial.imintPresentation.isEmpty()) {
                int numIntPresentations = Math.min(imintPresentation.size(), trial.imintPresentation.size());
                for (int i = 0; i < numIntPresentations; i++) {
                    imintPresentation.get(i).copyResponseData(trial.imintPresentation.get(i));
                }
            }
            if (attackPropensityProbe_Pp != null) {
                attackPropensityProbe_Pp.copyResponseData(trial.attackPropensityProbe_Pp);
            }
            if (humintPresentation != null && !humintPresentation.isEmpty()
                    && trial.humintPresentation != null && !trial.humintPresentation.isEmpty()) {
                int numIntPresentations = Math.min(humintPresentation.size(), trial.humintPresentation.size());
                for (int i = 0; i < numIntPresentations; i++) {
                    humintPresentation.get(i).copyResponseData(trial.humintPresentation.get(i));
                }
            }
            if (attackProbabilityProbe_Ppc != null) {
                attackProbabilityProbe_Ppc.copyResponseData(trial.attackProbabilityProbe_Ppc);
            }
            if (sigintSelectionProbe != null) {
                sigintSelectionProbe.copyResponseData(trial.sigintSelectionProbe);
            }
            if (sigintPresentation != null && !sigintPresentation.isEmpty()
                    && trial.sigintPresentation != null && !trial.sigintPresentation.isEmpty()) {
                int numIntPresentations = Math.min(sigintPresentation.size(), trial.sigintPresentation.size());
                for (int i = 0; i < numIntPresentations; i++) {
                    sigintPresentation.get(i).copyResponseData(trial.sigintPresentation.get(i));
                }
            }
            if (attackProbabilityProbe_Pt != null) {
                attackProbabilityProbe_Pt.copyResponseData(trial.attackProbabilityProbe_Pt);
            }
            if (attackProbabilityProbe_Ptpc != null) {
                attackProbabilityProbe_Ptpc.copyResponseData(trial.attackProbabilityProbe_Ptpc);
            }
            if (blueActionSelection != null) {
                blueActionSelection.copyResponseData(trial.blueActionSelection);
            }
            if (redActionSelection != null) {
                redActionSelection.copyResponseData(trial.redActionSelection);
            }
            responseFeedBack = trial.responseFeedBack;
            copyAdditionalResponseData(trial);
        }
    }

    /**
     * Subclasses may override this method to copy any additional response data
     * in the given trial to this trial.
     *
     * @param trial a trial
     */
    protected abstract void copyAdditionalResponseData(IcarusTestTrial_Phase2 trial);

    /**
     * Clears any participant response data to the trial.
     */
    public void clearResponseData() {
        startTime = null;
        endTime = null;
        trialTime_ms = null;
        if (osintPresentation != null) {
            for (OsintPresentationProbe osint : osintPresentation) {
                osint.clearResponseData();
            }
        }
        if (imintPresentation != null) {
            for (ImintPresentationProbe imint : imintPresentation) {
                imint.clearResponseData();
            }
        }
        if (attackPropensityProbe_Pp != null) {
            attackPropensityProbe_Pp.clearResponseData();
        }
        if (humintPresentation != null) {
            for (HumintPresentationProbe humintPresentationProbe : humintPresentation) {
                humintPresentationProbe.clearResponseData();
            }
        }
        if (attackProbabilityProbe_Ppc != null) {
            attackProbabilityProbe_Ppc.clearResponseData();
        }
        if (sigintSelectionProbe != null) {
            sigintSelectionProbe.clearResponseData();
        }
        if (sigintPresentation != null) {
            for (SigintPresentationProbe sigint : sigintPresentation) {
                sigint.clearResponseData();
            }
        }
        if (attackProbabilityProbe_Pt != null) {
            attackProbabilityProbe_Pt.clearResponseData();
        }
        if (attackProbabilityProbe_Ptpc != null) {
            attackProbabilityProbe_Ptpc.clearResponseData();
        }
        if (blueActionSelection != null) {
            blueActionSelection.clearResponseData();
        }
        if (redActionSelection != null) {
            redActionSelection.clearResponseData();
        }
        responseFeedBack = null;
        clearAdditionalResponseData();
    }

    /**
     * Subclasses may override this method to clear any additional response data
     * they contain.
     */
    protected abstract void clearAdditionalResponseData();

    /* 
     * Always returns null, method not used in Phase 2.
     * (non-Javadoc)
     * @see org.mitre.icarus.cps.exam.base.testing.IcarusTestTrial#getTestTrialType()
     */
    @SuppressWarnings("deprecation")
    @Override
    public TestTrialType getTestTrialType() {
        //Always returns null
        return null;
    }
}

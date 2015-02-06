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
package org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.trial_part_data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NameValuePair;
import org.mitre.icarus.cps.exam.phase_2.testing.MissionType;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbe;
import org.mitre.icarus.cps.exam.phase_2.testing.trial_part_probes.TrialPartProbeType;

/**
 * @author CBONACETO
 * @param <T>
 *
 */
public abstract class TrialPartData<T extends TrialPartProbe> implements Serializable {

    private static final long serialVersionUID = -7773560924939509481L;

    /**
     * The type of trial part the data is for
     */
    protected TrialPartProbeType trialPartType;

    /**
     * The time spent on the trial part
     */
    protected Double trialPartTime_ms;

    /**
     * Whether the participant generated the response to the trial part
     */
    protected Boolean participantGeneratedResponse;

    /**
     * The trial part probe the data was extracted from
     */
    protected T trialPartProbe;

    public TrialPartData() {
    }

    public TrialPartData(T trialPartProbe) {
        setTrialPartProbe(trialPartProbe);
    }

    /*public String getTrialPartName() {
     return trialPartName;
     }

     public void setTrialPartName(String trialPartName) {
     this.trialPartName = trialPartName;
     }*/
    public TrialPartProbeType getTrialPartType() {
        return trialPartType;
    }

    public void setTrialPartType(TrialPartProbeType trialPartType) {
        this.trialPartType = trialPartType;
    }

    public Double getTrialPartTime_ms() {
        return trialPartTime_ms;
    }

    public void setTrialPartTime_ms(Double trialPartTime_ms) {
        this.trialPartTime_ms = trialPartTime_ms;
    }

    public Boolean getParticipantGeneratedResponse() {
        return participantGeneratedResponse;
    }

    public void setParticipantGeneratedResponse(Boolean participantGeneratedResponse) {
        this.participantGeneratedResponse = participantGeneratedResponse;
    }

    public T getTrialPartProbe() {
        return trialPartProbe;
    }

    /**
     * @param trialPartProbe
     */
    public void setTrialPartProbe(T trialPartProbe) {
        this.trialPartProbe = trialPartProbe;
        if (trialPartProbe != null) {
            //trialPartName = trialPartProbe.getName();
            trialPartType = trialPartProbe.getType();
            trialPartTime_ms = trialPartProbe.getTrialPartTime_ms() != null
                    ? trialPartProbe.getTrialPartTime_ms().doubleValue() : null;
            participantGeneratedResponse = trialPartProbe.isDataProvidedToParticipant() != null
                    ? !trialPartProbe.isDataProvidedToParticipant() : null;
            initializeTrialPartData(trialPartProbe);
        }
    }

    /**
     * @param trialPartProbe
     */
    protected abstract void initializeTrialPartData(T trialPartProbe);
    
    /**
     *
     * @param dataValues
     * @param missionType
     * @param includeTrialPartId
     * @param includeTrialPartTime
     * @param includeParticipantGeneratedResponeFlag
     * @return
     */
    public List<NameValuePair> getDataValuesAsString(List<NameValuePair> dataValues, MissionType missionType,
            boolean includeTrialPartId, boolean includeTrialPartTime,
            boolean includeParticipantGeneratedResponeFlag) {
        String id = trialPartType != null ? trialPartType.getId() : "unknown";
        if (dataValues == null) {
            dataValues = new LinkedList<NameValuePair>();
        }
        if (includeTrialPartId) {
            dataValues.add(new NameValuePair("probe_id", id));
        }
        if (includeTrialPartTime) {
            dataValues.add(new NameValuePair(id + "_time",
                    trialPartTime_ms != null ? trialPartTime_ms.toString() : null));
        }
        if (includeParticipantGeneratedResponeFlag) {
            dataValues.add(new NameValuePair(id + "_participant_responded",
                    participantGeneratedResponse != null ? participantGeneratedResponse.toString() : null));
        }
        getAdditionalDataValuesAsString(dataValues);
        return dataValues;
    }

    /**
     * @param dataValues
     * @return
     */
    protected abstract List<NameValuePair> getAdditionalDataValuesAsString(
            List<NameValuePair> dataValues);
}

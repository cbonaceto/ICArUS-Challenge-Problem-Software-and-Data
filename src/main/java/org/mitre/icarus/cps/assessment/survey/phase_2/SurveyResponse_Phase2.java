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
package org.mitre.icarus.cps.assessment.survey.phase_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.mitre.icarus.cps.assessment.survey.SurveyField;
import org.mitre.icarus.cps.assessment.survey.SurveyResponse;
import org.mitre.icarus.cps.assessment.survey.SurveyField.FieldType;

/**
 * Contains a response to the Phase 2 demographics survey.
 * 
 * @author CBONACETO
 *
 */
public class SurveyResponse_Phase2 extends SurveyResponse {
    
    /**
     * Age
     */
    protected Integer age;

    /**
     * Gender
     */
    protected String gender;

    /**
     * Associate degree discipline
     */
    protected String associatesDegreeDiscipline;

    /**
     * Bachelor degree discipline
     */
    protected String bachelorsDegreeDiscipline;

    /**
     * Certificate degree discipline
     */
    protected String certificateDegreeDiscipline;

    /**
     * Master degree discipline
     */
    protected String mastersDegreeDiscipline;

    /**
     * PhD discipline
     */
    protected String phdDegreeDiscipline;
    
    /**
     * Current degree program type
     */
    protected EducationalDegreeType currentDegree;

    /**
     * Current degree program discipline
     */
    protected String currentDegreeDiscipline;

    /**
     * Highest degree earned
     */
    protected EducationalDegreeType highestDegree;

    /**
     * Jobs
     */
    protected List<Job> jobs;

    /**
     * Total years of job experience
     */
    protected Double yearsExperience;

    /**
     * Years of experience as an intelligence or geospatial analysis
     */
    protected Double yearsGeoIntExperience;

    /**
     * Level of training in probability and statistics (1=None, 2=Elementary,
     * 3=Intermediate, 4=Advanced)
     */
    protected Integer probsAndStatsTraining;

    /**
     * Frequency with which subject works with probability and statistics (1=
     * Never, 2=Rarely, 3=Occasionally, 4=Often)
     */
    protected Integer probsAndStatsUsageFrequency;

    /**
     * Frequency with which subject works with geospatial data (1= Never,
     * 2=Rarely, 3=Occasionally, 4=Often)
     */
    protected Integer geoDataUsageFrequency;

    /**
     * Level of expertise in geospatial intel analysis (in Final Exam only) (1 =
     * None, 2=Novice, 3=Intermediate, 5=Expert)
     */
    protected Integer geoIntAnalysisLevel;

    /**
     * Level of spatial ability (in Final Exam only) (1=Poor, 2=Weak, 3=Average,
     * 4=Good, 5=Excellent)
     */
    protected Integer spatialAbility;

    /**
     * BIS score
     */
    protected Integer bis;

    /**
     * BAS Drive score
     */
    protected Integer basDrive;

    /**
     * BAS Fun Seeking score
     */
    protected Integer basFunSeeking;

    /**
     * BAS Reward Responsiveness score
     */
    protected Integer basRewardResponsiveness;

    /**
     * BAS Total score
     */
    protected Integer basTotal;

    /**
     * Fox/Hedgehog test score (in Final Exam only)
     */
    protected Integer foxHedgehogScore;

    /**
     * Santa Barbara Sense of Direction Test (SBSDT) score
     */
    protected Double sbsdtScore;
    
    /** 
     * Water Level Test (WLT) score
     */
    protected Double wltScore;

    /**
     * Cognitive Reflections Test (CRT) score (in Final Exam only)
     */
    protected Integer crtScore;

    /**
     * Video Game Experience score
     */
    protected Double vge;

    public SurveyResponse_Phase2() {
       initializeFields();
    }
    
    private void initializeFields() {
        fields = new ArrayList<SurveyField>(10);
        fields.add(new SurveyField(FieldType.NumericValue, "Age"));
        fields.add(new SurveyField(FieldType.TextValue, "Gender"));
        fields.add(new SurveyField(FieldType.TextValue, "AssociatesDegreeDiscipline"));
        fields.add(new SurveyField(FieldType.TextValue, "BachelorsDegreeDiscipline"));
        fields.add(new SurveyField(FieldType.TextValue, "CertificateDegreeDiscipline"));
        fields.add(new SurveyField(FieldType.TextValue, "MastersDegreeDiscipline"));
        fields.add(new SurveyField(FieldType.TextValue, "PhdDiscipline"));
        fields.add(new SurveyField(FieldType.TextValue, "CurrentDegree"));
        fields.add(new SurveyField(FieldType.TextValue, "CurrentDegreeDiscipline"));
        fields.add(new SurveyField(FieldType.TextValue, "HighestDegree"));        
        for (int i = 1; i <= 10; i++) {
            fields.add(new SurveyField(FieldType.TextValue, "Job_" + i + "_Type"));
            fields.add(new SurveyField(FieldType.TextValue, "Job_" + i + "_Employer"));
            fields.add(new SurveyField(FieldType.TextValue, "Job_" + i + "_Title"));
            fields.add(new SurveyField(FieldType.NumericValue, "Job_" + i + "_Years"));
        }
        fields.add(new SurveyField(FieldType.NumericValue, "YearsExperience"));
        fields.add(new SurveyField(FieldType.NumericValue, "YearsGeoIntExperience"));
        fields.add(new SurveyField(FieldType.NumericValue, "ProbabilityAndStatsTraining"));
        fields.add(new SurveyField(FieldType.NumericValue, "ProbabilityAndStatsFrequency"));
        fields.add(new SurveyField(FieldType.NumericValue, "GeospatialDataFrequency"));
        fields.add(new SurveyField(FieldType.NumericValue, "GeointAnalysisLevel"));
        fields.add(new SurveyField(FieldType.NumericValue, "SpatialAbility"));
        fields.add(new SurveyField(FieldType.NumericValue, "BIS"));
        fields.add(new SurveyField(FieldType.NumericValue, "BASDrive"));
        fields.add(new SurveyField(FieldType.NumericValue, "BASFunSeeking"));
        fields.add(new SurveyField(FieldType.NumericValue, "BASRewardResponsiveness"));
        fields.add(new SurveyField(FieldType.NumericValue, "BASTotal"));
        fields.add(new SurveyField(FieldType.NumericValue, "FoxHedgehogScore"));
        fields.add(new SurveyField(FieldType.NumericValue, "SBSDTScore"));
        fields.add(new SurveyField(FieldType.NumericValue, "WLTScore"));
        fields.add(new SurveyField(FieldType.NumericValue, "CRTScore"));
        fields.add(new SurveyField(FieldType.NumericValue, "VGE"));
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
        fields.get(SurveyFieldType_Phase2.Age.ordinal()).setValue(
                age != null ? age.toString() : null);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        fields.get(SurveyFieldType_Phase2.Gender.ordinal()).setValue(gender);
    }   

    public String getAssociatesDegreeDiscipline() {
        return associatesDegreeDiscipline;
    }

    public void setAssociatesDegreeDiscipline(String associatesDegreeDiscipline) {
        this.associatesDegreeDiscipline = associatesDegreeDiscipline;
        fields.get(SurveyFieldType_Phase2.AssociatesDegreeDiscipline.ordinal()).setValue(
                associatesDegreeDiscipline);
    }

    public String getBachelorsDegreeDiscipline() {
        return bachelorsDegreeDiscipline;
    }

    public void setBachelorsDegreeDiscipline(String bachelorsDegreeDiscipline) {
        this.bachelorsDegreeDiscipline = bachelorsDegreeDiscipline;
        fields.get(SurveyFieldType_Phase2.BachelorsDegreeDiscipline.ordinal()).setValue(
                bachelorsDegreeDiscipline);
    }    

    public String getCertificateDegreeDiscipline() {
        return certificateDegreeDiscipline;
    }

    public void setCertificateDegreeDiscipline(String certificateDegreeDiscipline) {
        this.certificateDegreeDiscipline = certificateDegreeDiscipline;
        fields.get(SurveyFieldType_Phase2.CertificateDegreeDiscipline.ordinal()).setValue(
                certificateDegreeDiscipline);
    }
    
    public String getMastersDegreeDiscipline() {
        return mastersDegreeDiscipline;
    }

    public void setMastersDegreeDiscipline(String mastersDegreeDiscipline) {
        this.mastersDegreeDiscipline = mastersDegreeDiscipline;
        fields.get(SurveyFieldType_Phase2.MastersDegreeDiscipline.ordinal()).setValue(
                mastersDegreeDiscipline);
    }    

    public String getPhdDegreeDiscipline() {
        return phdDegreeDiscipline;
    }

    public void setPhdDegreeDiscipline(String phdDegreeDiscipline) {
        this.phdDegreeDiscipline = phdDegreeDiscipline;
        fields.get(SurveyFieldType_Phase2.PhDDiscipline.ordinal()).setValue(phdDegreeDiscipline);
    }

    public EducationalDegreeType getCurrentDegree() {
        return currentDegree;
    }

    public void setCurrentDegree(EducationalDegreeType currentDegree) {
        this.currentDegree = currentDegree;
        fields.get(SurveyFieldType_Phase2.CurrentDegree.ordinal()).setValue(
                currentDegree != null ? currentDegree.toString() : null);
    }

    public String getCurrentDegreeDiscipline() {
        return currentDegreeDiscipline;
    }

    public void setCurrentDegreeDiscipline(String currentDegreeDiscipline) {
        this.currentDegreeDiscipline = currentDegreeDiscipline;
        fields.get(SurveyFieldType_Phase2.CurrentDegreeDiscipline.ordinal()).setValue(
                currentDegreeDiscipline);
    }

    public EducationalDegreeType getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(EducationalDegreeType highestDegree) {
        this.highestDegree = highestDegree;
        fields.get(SurveyFieldType_Phase2.HighestDegree.ordinal()).setValue(
                highestDegree != null ? highestDegree.toString() : null);
    }   

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        int jobNum = 1;
        if (jobs != null && !jobs.isEmpty()) {
            Iterator<Job> jobsIter = jobs.iterator();
            while(jobsIter.hasNext() && jobNum <= 10) {
                Job job = jobsIter.next();
                setJob(job, jobNum);
                jobNum++;
            }
        }
        for (; jobNum <= 10; jobNum++) {
            setJob(null, jobNum);
        }
    }
    
    private void setJob(Job job, int jobNum) {
        JobType type = null;
        String employer = null;
        String title = null;
        Double years = null;
        if (job != null) {
            type = job.getJobType();
            employer = job.getEmployer();
            title = job.getTitle();
            years = job.getYears();
        }
        int fieldIndex = SurveyFieldType_Phase2.Job_1_Type.ordinal() + (jobNum-1) * 4;
        fields.get(fieldIndex).setValue(type != null ? type.toString() : null);
        fields.get(fieldIndex+1).setValue(employer);
        fields.get(fieldIndex+2).setValue(title);
        fields.get(fieldIndex+3).setValue(years != null ? years.toString() : null);
    }
    
    public Double getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Double yearsExperience) {
        this.yearsExperience = yearsExperience;
        fields.get(SurveyFieldType_Phase2.YearsExperience.ordinal()).setValue(
                yearsExperience != null ? yearsExperience.toString() : null);
    }

    public Double getYearsGeoIntExperience() {
        return yearsGeoIntExperience;
    }

    public void setYearsGeoIntExperience(Double yearsGeoIntExperience) {
        this.yearsGeoIntExperience = yearsGeoIntExperience;
        fields.get(SurveyFieldType_Phase2.YearsGeoIntExperience.ordinal()).setValue(
                yearsGeoIntExperience != null ? yearsGeoIntExperience.toString() : null);
    }    

    public Integer getProbsAndStatsTraining() {
        return probsAndStatsTraining;
    }

    public void setProbsAndStatsTraining(Integer probsAndStatsTraining) {
        this.probsAndStatsTraining = probsAndStatsTraining;
        fields.get(SurveyFieldType_Phase2.ProbabilityAndStatsTraining.ordinal()).setValue(
                probsAndStatsTraining != null ? probsAndStatsTraining.toString() : null);
    }

    public Integer getProbsAndStatsUsageFrequency() {
        return probsAndStatsUsageFrequency;
    }

    public void setProbsAndStatsUsageFrequency(Integer probsAndStatsUsageFrequency) {
        this.probsAndStatsUsageFrequency = probsAndStatsUsageFrequency;
        fields.get(SurveyFieldType_Phase2.ProbabilityAndStatsFrequency.ordinal()).setValue(
                probsAndStatsUsageFrequency != null ? probsAndStatsUsageFrequency.toString() : null);
    }

    public Integer getGeoDataUsageFrequency() {
        return geoDataUsageFrequency;
    }

    public void setGeoDataUsageFrequency(Integer geoDataUsageFrequency) {
        this.geoDataUsageFrequency = geoDataUsageFrequency;
        fields.get(SurveyFieldType_Phase2.GeospatialDataFrequency.ordinal()).setValue(
                geoDataUsageFrequency != null ? geoDataUsageFrequency.toString() : null);
    }

    public Integer getGeoIntAnalysisLevel() {
        return geoIntAnalysisLevel;
    }

    public void setGeoIntAnalysisLevel(Integer geoIntAnalysisLevel) {
        this.geoIntAnalysisLevel = geoIntAnalysisLevel;
        fields.get(SurveyFieldType_Phase2.GeoIntAnalysisLevel.ordinal()).setValue(
                geoIntAnalysisLevel != null ? geoIntAnalysisLevel.toString() : null);
    }

    public Integer getSpatialAbility() {
        return spatialAbility;
    }

    public void setSpatialAbility(Integer spatialAbility) {
        this.spatialAbility = spatialAbility;
        fields.get(SurveyFieldType_Phase2.SpatialAbility.ordinal()).setValue(
                spatialAbility != null ? spatialAbility.toString() : null);
    }

    public Integer getBis() {
        return bis;
    }

    public void setBis(Integer bis) {
        this.bis = bis;
        fields.get(SurveyFieldType_Phase2.BIS.ordinal()).setValue(
                bis != null ? bis.toString() : null);
    }

    public Integer getBasDrive() {
        return basDrive;
    }

    public void setBasDrive(Integer basDrive) {
        this.basDrive = basDrive;
        fields.get(SurveyFieldType_Phase2.BASDrive.ordinal()).setValue(
                basDrive != null ? basDrive.toString() : null);
    }

    public Integer getBasFunSeeking() {
        return basFunSeeking;
    }

    public void setBasFunSeeking(Integer basFunSeeking) {
        this.basFunSeeking = basFunSeeking;
        fields.get(SurveyFieldType_Phase2.BASFunSeeking.ordinal()).setValue(
                basFunSeeking != null ? basFunSeeking.toString() : null);
    }

    public Integer getBasRewardResponsiveness() {
        return basRewardResponsiveness;
    }

    public void setBasRewardResponsiveness(Integer basRewardResponsiveness) {
        this.basRewardResponsiveness = basRewardResponsiveness;
        fields.get(SurveyFieldType_Phase2.BASRewardResponsiveness.ordinal()).setValue(
                basRewardResponsiveness != null ? basRewardResponsiveness.toString() : null);
    }

    public Integer getBasTotal() {
        return basTotal;
    }

    public void setBasTotal(Integer basTotal) {
        this.basTotal = basTotal;
        fields.get(SurveyFieldType_Phase2.BASTotal.ordinal()).setValue(
                basTotal != null ? basTotal.toString() : null);
    }

    public Integer getFoxHedgehogScore() {
        return foxHedgehogScore;
    }

    public void setFoxHedgehogScore(Integer foxHedgehogScore) {
        this.foxHedgehogScore = foxHedgehogScore;
        fields.get(SurveyFieldType_Phase2.FoxHedgehogScore.ordinal()).setValue(
                foxHedgehogScore != null ? foxHedgehogScore.toString() : null);
    }  

    public Double getSbsdtScore() {
        return sbsdtScore;
    }

    public void setSbsdtScore(Double sbsdtScore) {
        this.sbsdtScore = sbsdtScore;
        fields.get(SurveyFieldType_Phase2.SBSDTScore.ordinal()).setValue(
                sbsdtScore != null ? sbsdtScore.toString() : null);
    }

    public Double getWltScore() {
        return wltScore;
    }

    public void setWltScore(Double wltScore) {
        this.wltScore = wltScore;
        fields.get(SurveyFieldType_Phase2.WLTScore.ordinal()).setValue(
                wltScore != null ? wltScore.toString() : null);
    }

    public Integer getCrtScore() {
        return crtScore;
    }

    public void setCrtScore(Integer crtScore) {
        this.crtScore = crtScore;
        fields.get(SurveyFieldType_Phase2.CRTScore.ordinal()).setValue(
                crtScore != null ? crtScore.toString() : null);
    }
    
    public Double getVge() {
        return vge;
    }

    public void setVge(Double vge) {
        this.vge = vge;
        fields.get(SurveyFieldType_Phase2.VGE.ordinal()).setValue(
                vge != null ? vge.toString() : null);
    }

    @Override
    @XmlTransient
    public List<SurveyField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    /* (non-Javadoc)
     * @see org.mitre.icarus.cps.assessment.survey.SurveyResponse#setFields(java.util.List)
     */
    @Override
    public void setFields(List<SurveyField> fields) {
        if (fields != null && !fields.isEmpty()) {
            List<Job> jobsTemp = new ArrayList<Job>(10);
            for(int i=1; i<=10; i++) {
                jobsTemp.add(new Job());
            }
            for (SurveyField field : fields) {
                if (field.getName() != null) {
                    String fieldName = field.getName();
                    if (fieldName.startsWith("Job")) {
                        String[] parts = fieldName.split("_");                        
                        int jobNum = parts.length == 3 ? Integer.parseInt(parts[1]) : -1;
                        String fieldType = parts.length == 3 ? parts[2] : null;                        
                        if(jobNum >= 1 && jobNum <= 10 && fieldType != null) {
                            Job job = jobsTemp.get(jobNum - 1);
                            if (fieldType.equals("Type")) {
                                job.setJobType(JobType.valueOf(field.getValue()));
                            } else if (fieldType.equals("Employer")) {
                                job.setEmployer(field.getValue());
                            } else if (fieldType.equals("Title")) {
                                job.setTitle(field.getValue());
                            } else if (fieldType.equals("Years")) {
                                job.setYears(Double.parseDouble(field.getValue()));
                            }
                        }
                    } else {
                        boolean matchFound = false;
                        Iterator<SurveyField> fieldIter = this.fields.iterator();
                        int index = 0;
                        while (!matchFound && fieldIter.hasNext()) {
                            SurveyField matchedField = fieldIter.next();
                            if (fieldName.equals(matchedField.getName())) {
                                matchFound = true;
                                SurveyFieldType_Phase2 fieldType = SurveyFieldType_Phase2.values()[index];
                                switch (fieldType) {
                                    case Age:
                                        setAge(Integer.parseInt(field.getValue()));
                                        break;
                                    case Gender:
                                        setGender(field.getValue());
                                        break;
                                    case AssociatesDegreeDiscipline:
                                        setAssociatesDegreeDiscipline(field.getValue());
                                        break;
                                    case BachelorsDegreeDiscipline:
                                        setBachelorsDegreeDiscipline(field.getValue());
                                        break;
                                    case CertificateDegreeDiscipline:
                                        setCertificateDegreeDiscipline(field.getValue());
                                        break;
                                    case MastersDegreeDiscipline:
                                        setMastersDegreeDiscipline(field.getValue());
                                        break;
                                    case PhDDiscipline:
                                        setPhdDegreeDiscipline(field.getValue());
                                        break;
                                    case CurrentDegree:
                                        setCurrentDegree(EducationalDegreeType.valueOf(field.getValue()));
                                        break;
                                    case CurrentDegreeDiscipline:
                                        setCurrentDegreeDiscipline(field.getValue());
                                        break;
                                    case HighestDegree:
                                        setHighestDegree(EducationalDegreeType.valueOf(field.getValue()));
                                        break;
                                    case YearsExperience:
                                        setYearsExperience(Double.parseDouble(field.getValue()));
                                        break;
                                    case YearsGeoIntExperience:
                                        setYearsGeoIntExperience(Double.parseDouble(field.getValue()));
                                        break;
                                    case ProbabilityAndStatsTraining:
                                        setProbsAndStatsTraining(Integer.parseInt(field.getValue()));
                                        break;
                                    case ProbabilityAndStatsFrequency:
                                        setProbsAndStatsUsageFrequency(Integer.parseInt(field.getValue()));
                                        break;
                                    case GeospatialDataFrequency:
                                        setGeoDataUsageFrequency(Integer.parseInt(field.getValue()));
                                        break;
                                    case GeoIntAnalysisLevel:
                                        setGeoIntAnalysisLevel(Integer.parseInt(field.getValue()));
                                        break;
                                    case SpatialAbility:
                                        setSpatialAbility(Integer.parseInt(field.getValue()));
                                        break;
                                    case BASDrive:
                                        setBasDrive(Integer.parseInt(field.getValue()));
                                        break;
                                    case BASFunSeeking:
                                        setBasFunSeeking(Integer.parseInt(field.getValue()));
                                        break;
                                    case BASRewardResponsiveness:
                                        setBasRewardResponsiveness(Integer.parseInt(field.getValue()));
                                        break;
                                    case BASTotal:
                                        setBasTotal(Integer.parseInt(field.getValue()));
                                        break;
                                    case BIS:
                                        setBis(Integer.parseInt(field.getValue()));
                                        break;
                                    case FoxHedgehogScore:
                                        setFoxHedgehogScore(Integer.parseInt(field.getValue()));
                                        break;
                                    case SBSDTScore:
                                        setSbsdtScore(Double.parseDouble(field.getValue()));
                                        break;
                                    case WLTScore:
                                        setWltScore(Double.parseDouble(field.getValue()));
                                        break;
                                    case CRTScore:
                                        setCrtScore(Integer.parseInt(field.getValue()));
                                        break;
                                    case VGE:
                                        setVge(Double.parseDouble(field.getValue()));
                                        break;
                                }
                            } else {
                                index++;
                            }
                        }
                    }
                }
            }
            setJobs(jobsTemp);
        }
    }
}

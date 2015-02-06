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
package org.mitre.icarus.cps.web.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 *
 * @author Eric Kappotis
 */

@Entity
@Table(name = "experiment_table")
public class Experiment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "EXPERIMENT_ID")
    private String experimentId;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Task> tasks;
    
    @Column(name = "EXPERIMENT_NAME", unique = true)
    private String name;
    
    @Column(name = "EXPERIMENT_ORDER")
    private Integer experimentOrder;
    
    @ManyToOne
    private ProgramPhase programPhase;
    
    @Column(name = "ACTIVE")
    private boolean active;
    
    @Column(name = "OFFICIAL")
    private boolean official;
    
    @Column(name = "CURRENT_EXPERIMENT")
    private boolean currentExperiment;
    
    public Integer getId() {
        return this.id;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }    

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExperimentOrder() {
        return experimentOrder;
    }

    public void setExperimentOrder(Integer experimentOrder) {
        this.experimentOrder = experimentOrder;
    }   

    public ProgramPhase getProgramPhase() {
        return programPhase;
    }

    public void setProgramPhase(ProgramPhase programPhase) {
        this.programPhase = programPhase;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public boolean isCurrentExperiment() {
        return currentExperiment;
    }

    public void setCurrentExperiment(boolean currentExperiment) {
        this.currentExperiment = currentExperiment;
    }   
    
    public Integer calculateExperimentTime() {
        
        // don't bother trying to calculate if the tasks
        // have not been set or initialzied in any ways
        if(tasks == null) {
            return null;
        }
        
        Integer totalTime = 0;
        
        for(Task currTask : tasks) {
            
            // don't factor null time of a particular task, treat as zero
            if(currTask != null) {
                totalTime += currTask.getTaskTime();
            }
        }        
        return totalTime;
    }
}

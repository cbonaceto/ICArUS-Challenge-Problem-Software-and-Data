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

import java.util.List;

/**
 *
 * @author Eric Kappotis
 */
public class IcarusModel {
    
    private String status = "success";
    private String failureMessage;
    private List<Site> siteList;
    private List<AccessCode> accessCodeList;
    private List<Role> roleList;
    private User user;
    private List<ExperimentStatus> experimentStatus;

    public List<Site> getSiteList() {
        return siteList;
    }

    public void setSiteList(List<Site> siteList) {
        this.siteList = siteList;
    }

    public List<AccessCode> getAccessCodeList() {
        return accessCodeList;
    }

    public void setAccessCodeList(List<AccessCode> accessCodeList) {
        this.accessCodeList = accessCodeList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ExperimentStatus> getExperimentStatus() {
        return experimentStatus;
    }

    public void setExperimentStatus(List<ExperimentStatus> experimentStatus) {
        this.experimentStatus = experimentStatus;
    }    
}

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
import java.util.Date;

import javax.persistence.*;
/**
 *
 * @author Eric Kappotis
 */

@Entity
@Table(name="access_code_table")
public class AccessCode implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "CODE")
    private String code;
    
    @Column(name = "MAXIMUM_USERS")
    private Integer maximumUses;
    
    @Column(name = "EXPERATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
    
    @Column(name = "TIMES_USED")
    private Integer timesUsed;
    
    @ManyToOne
    private Site site;
    
    @ManyToOne
    private Role role;

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getMaximumUses() {
        return maximumUses;
    }

    public void setMaximumUses(Integer maximumUses) {
        this.maximumUses = maximumUses;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(Integer timesUsed) {
        this.timesUsed = timesUsed;
    }    
    
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
}

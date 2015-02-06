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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 *
 * @author Eric Kappotis
 */
@Entity
@Table(name="user_table")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "EMAIL", unique = true)
    private String email;
    
    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "ORGANIZATION")
    private String organization;
    
    @Column(name = "LAST_LOGIN_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    
    @Column(name = "CREATION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationTime;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<UserAttribute> userAttributes;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Site site;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Role> roles;
    
    @Column(name = "LOCK_ACCOUNT")
    private Boolean locked;
    
    @Column(name = "INVALID_PASSWORD_COUNT")
    private Integer invalidPasswordCount = 0;

    public Integer getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }    

    public List<UserAttribute> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(List<UserAttribute> userAttributes) {
        this.userAttributes = userAttributes;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Integer getInvalidPasswordCount() {
        return invalidPasswordCount;
    }

    public void setInvalidPasswordCount(Integer invalidPasswordCount) {
        this.invalidPasswordCount = invalidPasswordCount;
    }    
}

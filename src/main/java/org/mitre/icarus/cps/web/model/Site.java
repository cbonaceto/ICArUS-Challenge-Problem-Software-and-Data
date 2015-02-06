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

import javax.persistence.*;

/**
 *
 * @author Eric Kappotis
 */

@Entity
@Table(name = "site_table")
public class Site implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "SITE_NAME", unique = true)
    private String name;
    
    @Column(name = "SITE_TAG", unique = true)
    private String tag;
    
    @OneToMany(mappedBy = "site")
    private List<AccessCode> accessCodes;
    
    public Site() {}
    
    public Site(String name) {
        this.name = name;
    }
    
    public Site(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<AccessCode> getAccessCodes() {
        return accessCodes;
    }

    public void setAccessCodes(List<AccessCode> accessCodes) {
        this.accessCodes = accessCodes;
    }
    
    @Override
    public String toString() {
        return name;
    }	
}
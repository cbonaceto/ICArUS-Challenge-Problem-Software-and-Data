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
package org.mitre.icarus.cps.web.experiments.client;

import java.io.File;
import java.util.List;
import org.mitre.icarus.cps.web.experiments.client.exceptions.ClientException;
import org.mitre.icarus.cps.web.model.Role;
import org.mitre.icarus.cps.web.model.User;

/**
 *
 * @author Eric Kappotis
 */
public interface IDataSource {
    
    /**
     *
     * @param userName
     * @param password
     * @param siteName
     * @param siteAuthCode
     * @return
     * @throws ClientException
     */
    public List<Role> createNewUser(String userName, String password, String siteName, String siteAuthCode) throws ClientException;
    
    /**
     *
     * @param userName
     * @param password
     * @return
     * @throws ClientException
     */
    public List<Role> validateLogin(String userName, String password) throws ClientException;
    
    /**
     *
     * @param userName
     * @param oldPasswrod
     * @param newPassword
     * @throws ClientException
     */
    public void changePassword(String userName, String oldPasswrod, String newPassword) throws ClientException;
    
    /**
     *
     * @param userName
     * @return
     * @throws ClientException
     */
    public User getUser(String userName) throws ClientException;
    
    /**
     *
     * @param userName
     * @param examId
     * @return
     * @throws ClientException
     */
    public List<String> getCompletedExamTasks(String userName, String examId) throws ClientException;
    
    /**
     *
     * @param examId
     * @param site
     * @param taskIds
     * @return
     * @throws ClientException
     */
    public List<User> getUsersForExam(String examId, String site, List<String> taskIds) throws ClientException;
    
    public void saveTaskData(String userId, String examId, String taskId, File data) throws ClientException;
}

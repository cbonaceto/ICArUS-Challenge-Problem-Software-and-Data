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
package org.mitre.icarus.cps.assessment.persistence.phase_2.xml;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.AverageHumanDataSet_Phase2;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.SingleModelDataSet_Phase2;
import org.mitre.icarus.cps.exam.base.loader.IcarusExamLoader;

/**
 * @author CBONACETO
 *
 */
public class XMLCPADataPersister {
	
	/** URL to the data folder to store XML files in */
	protected URL dataFolder;
	
	public XMLCPADataPersister() {}
	
	public XMLCPADataPersister(URL dataFolder) {
		this.dataFolder = dataFolder;
	}
	
	public URL getDataFolder() {
		return dataFolder;
	}

	public void setDataFolder(URL dataFolder) {
		this.dataFolder = dataFolder;
	}
	
	/**
	 * Marshals an AverageHumanDataSet instance to XML.
	 * 
	 * @param dataSet
	 * @return
	 * @throws JAXBException
	 */
	public String marshalAverageHumanDataSet(AverageHumanDataSet_Phase2 dataSet) throws JAXBException {
		Marshaller marshaller = IcarusExamLoader.createMarshallerForClass(AverageHumanDataSet_Phase2.class);		
		ByteArrayOutputStream output = new ByteArrayOutputStream ();
		marshaller.marshal(dataSet, output);		
		return output.toString();
	}
	
	/**
	 * Marshals a SingleModelDataSet instance to XML.
	 * 
	 * @param dataSet
	 * @return
	 * @throws JAXBException
	 */
	public String marshalSingleModelDataSet(SingleModelDataSet_Phase2 dataSet) throws JAXBException {
		Marshaller marshaller = IcarusExamLoader.createMarshallerForClass(SingleModelDataSet_Phase2.class);		
		ByteArrayOutputStream output = new ByteArrayOutputStream (); 
		marshaller.marshal(dataSet, output);		
		return output.toString();
	}
	
	/**
	 * @param dataSet
	 * @throws JAXBException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public void persistAverageHumanDataSet(AverageHumanDataSet_Phase2 dataSet) throws JAXBException, IOException, URISyntaxException {
		if(dataFolder == null) {
			throw new IllegalArgumentException("Error, must set data folder before persisting an XML file");
		}
		String xml = marshalAverageHumanDataSet(dataSet);
		writeFile(xml, createAverageHumanDataSetFile(dataSet.getExam_id()));
	}
	
	/**
	 * @param dataSet
	 * @throws JAXBException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public void persistSingleModelDataSet(SingleModelDataSet_Phase2 dataSet) throws JAXBException, IOException, URISyntaxException {
		if(dataFolder == null) {
			throw new IllegalArgumentException("Error, must set data folder before persisting an XML file");
		}
		String xml = marshalSingleModelDataSet(dataSet);
		writeFile(xml, createSingleModelDataSetFileName(dataSet.getExam_id(), dataSet.getSite_id(), dataSet.getResponse_generator_id()));
	}
	
	/**
	 * @param examId
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public AverageHumanDataSet_Phase2 loadAverageHumanDataSet(String examId) throws JAXBException, IOException {
		return loadAverageHumanDataSet(createAverageHumanDataSetFile(examId));
	}
	
	/**
	 * @param dataSetFile
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static AverageHumanDataSet_Phase2 loadAverageHumanDataSet(URL dataSetFile) throws JAXBException, IOException {            
		Unmarshaller unmarshaller = IcarusExamLoader.createUnmarshallerForClass(AverageHumanDataSet_Phase2.class, null, false);
		AverageHumanDataSet_Phase2 dataSet = 
				(AverageHumanDataSet_Phase2)(unmarshaller.unmarshal(dataSetFile));		
		return dataSet;
	}
	
	/**
	 * @param examId
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public SingleModelDataSet_Phase2 loadSingleModelDataSet(String examId, String siteId, String responseGeneratorId) throws JAXBException, IOException {
		return loadSingleModelDataSet(createSingleModelDataSetFileName(examId, siteId, responseGeneratorId));
	}
	
	/**
	 * @param dataSetFile
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static SingleModelDataSet_Phase2 loadSingleModelDataSet(URL dataSetFile) throws JAXBException, IOException {
		Unmarshaller unmarshaller = IcarusExamLoader.createUnmarshallerForClass(SingleModelDataSet_Phase2.class, null, false);
		SingleModelDataSet_Phase2 dataSet =
				(SingleModelDataSet_Phase2)(unmarshaller.unmarshal(dataSetFile));		
		return dataSet;
	}
	
	/**
	 * @param examId
	 * @return
	 * @throws MalformedURLException
	 */
	protected URL createAverageHumanDataSetFile(String examId) throws MalformedURLException {            
		return new URL(dataFolder, "Avg-Human_" + examId + ".xml");		
	}
	
	/**
	 * @param examId
	 * @param siteId
	 * @param responseGeneratorId
	 * @return
	 * @throws MalformedURLException
	 */
	protected URL createSingleModelDataSetFileName(String examId, String siteId, String responseGeneratorId) throws MalformedURLException {
		return new URL(dataFolder, siteId + "_" + responseGeneratorId + "_" + examId + ".xml");
	}
	
	/**
	 * @param contents
	 * @param fileUrl
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	protected static void writeFile(String contents, URL fileUrl) throws IOException, URISyntaxException {	
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(fileUrl.toURI())));
		out.write(contents);
		out.close();
	}
	
	/**
	 * @param contents
	 * @param file
	 * @throws IOException
	 */
	protected static void writeFile(String contents, File file) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(contents);
		out.close();
	}
}
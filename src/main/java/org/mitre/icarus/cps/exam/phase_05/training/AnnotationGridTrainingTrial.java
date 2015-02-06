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
package org.mitre.icarus.cps.exam.phase_05.training;

import java.util.ArrayList;
import java.util.List;

//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * Annotation grid training trial.  An annotation grid contains a grid of annotated sectors 
 * where the sectors in each row in the grid contain the same scene item.
 *   
 * @author CBONACETO
 *
 */
@XmlType(name="AnnotationGrid", namespace="IcarusCPD_05")
public class AnnotationGridTrainingTrial extends IcarusTrainingTrial {

	/** The number of rows in the grid */
	//private Integer numRows = 0;
	
	/** The number of columns in the grid */
	//private Integer numColumns = 0;
	
	/** The IDs of each layers to show */
	private List<Integer> baseLayers;
	
	/** The rows */
	private ArrayList<AnnotationGridRow> rows;	

	/**
	 * Get the number of rows in the grid.
	 * 
	 * @return
	 */
	public Integer getNumRows() {
		if(rows != null) {
			return rows.size();
		}
		return 0;
	}
//
//	/**
//	 * Set the number of rows in the grid.
//	 * 
//	 * @param numRows
//	 */
//	public void setNumRows(Integer numRows) {
//		this.numRows = numRows;
//	}
//
	/**
	 * Get the number of columns in the grid.
	 * 
	 * @return
	 */
	public Integer getNumColumns() {
		int cols = 0;
		if(rows != null && !rows.isEmpty()) {			
			for(AnnotationGridRow row : rows) {
				if(row.columns != null && row.columns.size() > cols) {
					cols = row.columns.size();
				}
			}
		}
		return cols;
	}
//
//	/**
//	 * Set the number of columns in the grid.
//	 * 
//	 * @param numColumns
//	 */
//	public void setNumColumns(Integer numColumns) {
//		this.numColumns = numColumns;
//	}
	
	/**
	 * Get the IDs of each layer to show.
	 * 
	 * @return
	 */
	@XmlElementWrapper(name="BaseLayers")
	@XmlElement(name="LayerId")
	public List<Integer> getBaseLayers() {
		return baseLayers;
	}

	/**
	 * Set the IDs of each layer to show.
	 * 
	 * @param baseLayers
	 */
	public void setBaseLayers(List<Integer> baseLayers) {
		this.baseLayers = baseLayers;
	}

	/**
	 * Get the rows.
	 * 
	 * @return
	 */
	@XmlElement(name="AnnotationGridRow")
	public ArrayList<AnnotationGridRow> getRows() {
		return rows;
	}

	/**
	 * Set the rows.
	 * 
	 * @param rows
	 */
	public void setRows(ArrayList<AnnotationGridRow> rows) {
		this.rows = rows;
	}

	@Override
	public TrainingTrialType getTrainingTrialType() {
		return TrainingTrialType.AnnotationGrid;
	}
	
	/**
	 * Contains data for each row in the grid.	  
	 *
	 * @author CBONACETO	 
	 */
	@XmlType(name="AnnotationGridRow", namespace="IcarusCPD_05")
	public static class AnnotationGridRow {
		/** The row number */
		//private Integer rowNumber = 0;
		
		/** The scene item each sector in the scene contains */
		private Integer itemId;
		
		/** The columns */
		private ArrayList<AnnotationGridColumn> columns;

//		/**
//		 * Get the row number.
//		 * 
//		 * @return
//		 */
//		@XmlAttribute(name="Row")
//		public Integer getRowNumber() {
//			return rowNumber;
//		}
//
//		/**
//		 * Set the row number.
//		 * 
//		 * @param rowNumber
//		 */
//		public void setRowNumber(Integer rowNumber) {
//			this.rowNumber = rowNumber;
//		}

		/**
		 * Get the scene item ID that each sector in the scene contains. 
		 * 
		 * @return
		 */
		@XmlElement(name="ItemId")
		public Integer getItemId() {
			return itemId;
		}

		/**
		 * Set the scene item ID that each sector in the scene contains.
		 * 
		 * @param itemId
		 */
		public void setItemId(Integer itemId) {
			this.itemId = itemId;
		}

		/**
		 * Get the columns.
		 * 
		 * @return
		 */
		@XmlElement(name="AnnotationGridColumn")
		public ArrayList<AnnotationGridColumn> getColumns() {
			return columns;
		}

		/**
		 * Set the columns.
		 * 
		 * @param columns
		 */
		public void setColumns(ArrayList<AnnotationGridColumn> columns) {
			this.columns = columns;
		}
	}
	
	/**
	 * Contains data for each column in the grid.
	 * 
	 * @author CBONACETO
	 *
	 */
	@XmlType(name="AnnotationGridColumn", namespace="IcarusCPD_05")
	public static class AnnotationGridColumn {
		/** The column number */
		//private Integer columnNumber = 0;
		
		/** URL to the feature vector file to get the sector from */
		private String featureVectorUrl;
		
		/** URL to the object palette file to use */
		private String objectPaletteUrl;
		
		/** The sector to show in the column cell */
		private Integer sectorId;

//		/**
//		 * Get the column number.
//		 * 
//		 * @return
//		 */
//		@XmlAttribute(name="Column")
//		public Integer getColumnNumber() {
//			return columnNumber;
//		}
//
//		/**
//		 * Set the column number.
//		 * 
//		 * @param columnNumber
//		 */
//		public void setColumnNumber(Integer columnNumber) {
//			this.columnNumber = columnNumber;
//		}

		/**
		 * Get the URL to the feature vector file to get the sector from.
		 * 
		 * @return
		 */
		@XmlElement(name="FeatureVectorUrl")
		public String getFeatureVectorUrl() {
			return featureVectorUrl;
		}

		/**
		 * Set the URL to the feature vector file to get the sector from.
		 * 
		 * @param featureVectorUrl
		 */
		public void setFeatureVectorUrl(String featureVectorUrl) {
			this.featureVectorUrl = featureVectorUrl;
		}

		/**
		 * Get the URL to the object palette file to use.
		 * 
		 * @return
		 */
		@XmlElement(name="ObjectPaletteUrl")
		public String getObjectPaletteUrl() {
			return objectPaletteUrl;
		}

		/**
		 * Set the URL to the object palette file to use.
		 * 
		 * @param objectPaletteUrl
		 */
		public void setObjectPaletteUrl(String objectPaletteUrl) {
			this.objectPaletteUrl = objectPaletteUrl;
		}

		/**
		 * Get the sector to show in the column cell.
		 * 
		 * @return
		 */
		@XmlElement(name="SectorId")
		public Integer getSectorId() {
			return sectorId;
		}

		/**
		 * Set the sector to show in the column cell.
		 * 
		 * @param sectorId
		 */
		public void setSectorId(Integer sectorId) {
			this.sectorId = sectorId;
		}		
	}
}

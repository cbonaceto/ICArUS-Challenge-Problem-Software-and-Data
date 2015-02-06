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
package org.mitre.icarus.cps.assessment.persistence.phase_2.spreadsheet;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.OverallCFACPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.TrialIdentifier;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.MissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.NegentropyMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.RSRAndASRMissionMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_2.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.AverageHumanDataSet_Phase2;
import org.mitre.icarus.cps.assessment.data_model.phase_2.data_sets.SingleModelDataSet_Phase2;

/**
 * Combines a model data set and a human data set into an Excel spreadsheet.
 */
public class SpreadsheetMarshaller {

	private static Workbook wb;
	private static FormulaEvaluator formulaEvaluator;
	private static List<Double> rsrAsrWeights;
	private static boolean useTaskWeights;
	private static AverageHumanDataSet_Phase2 humanDataSet;
	private static CellStyle boldLeftStyle;
	private static CellStyle boldCenterStyle;
	private static CellStyle boldShadedCenterStyle;
	private static CellStyle redCenterStyle;
	private static CellStyle greenCenterStyle;
	private static CellStyle centerStyle;
	private static CellStyle leftStyle;

	/**
	 * Returns an Excel workbook containing the combined model and human data.
	 */
	public static Workbook marshall(SingleModelDataSet_Phase2 dataset, AverageHumanDataSet_Phase2 humanData, Workbook wb) {
		formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
		rsrAsrWeights = humanData.getMetricsInfo().getRsr_asr_task_weights();
		useTaskWeights = humanData.getMetricsInfo().isUse_rsr_asr_task_weights();
		humanDataSet = humanData;
		initializeStyles(wb);
		initializeSheets(wb, dataset);
		processSingleModelDataSet(dataset.getExamMetrics(), wb);
		return wb;
	}

	/**
	 * Defines bold and red fonts.
	 */
	private static void initializeStyles(Workbook wb) {
		final Font boldFont = wb.createFont();
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		boldLeftStyle = wb.createCellStyle();
		boldLeftStyle.setFont(boldFont);
		boldLeftStyle.setAlignment(CellStyle.ALIGN_LEFT);

		boldCenterStyle = wb.createCellStyle();
		boldCenterStyle.setFont(boldFont);
		boldCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);
		setStyleBorder(boldCenterStyle, CellStyle.BORDER_THIN);

		boldShadedCenterStyle = wb.createCellStyle();
		boldShadedCenterStyle.cloneStyleFrom(boldCenterStyle);
		boldShadedCenterStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		boldShadedCenterStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		Font redFont = wb.createFont();
		redFont.setColor(Font.COLOR_RED);
		redCenterStyle = wb.createCellStyle();
		redCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);
		redCenterStyle.setFont(redFont);
		setStyleBorder(redCenterStyle, CellStyle.BORDER_THIN);
		
        Font greenFont = wb.createFont();
        greenFont.setColor(HSSFColor.GREEN.index);
    	greenCenterStyle = wb.createCellStyle();
    	greenCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);
        greenCenterStyle.setFont(greenFont);
        setStyleBorder(greenCenterStyle, CellStyle.BORDER_THIN);

		centerStyle = wb.createCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		setStyleBorder(centerStyle, CellStyle.BORDER_THIN);
		
		leftStyle = wb.createCellStyle();
	}

	private static void setStyleBorder(CellStyle style, short borderWidth) {
		style.setBorderBottom(borderWidth);
		style.setBorderLeft(borderWidth);
		style.setBorderRight(borderWidth);
		style.setBorderTop(borderWidth);
	}

	/**
	 * Creates bold titled workbook sheets as defined by SpreadsheetConstants.
	 */
	private static void initializeSheets(Workbook wb, SingleModelDataSet_Phase2 dataset) {
		for(Entry<String,String> title : SpreadsheetConstants.SHEET_TITLE_MAP.entrySet()) {
			String sheetName = title.getKey();
			String sheetTitle = title.getValue();
			Sheet sheet = wb.createSheet(sheetName);
			Row row = sheet.createRow((short)0);
			Cell cell = row.createCell(0);
			if(sheetName.equals(SpreadsheetConstants.SHEET_OVERALL)) {
				//TODO: hardcoded date based on model data xml
                                Date runDate = dataset.getTime_stamp() != null ? new Date(dataset.getTime_stamp()) : null;                                
				sheetTitle = dataset.getSite_id() + " " + sheetTitle;// + " (4/15/14 Run)"; 
                                if(runDate != null) {
                                    String date = DateFormat.getDateInstance().format(runDate);
                                    sheetTitle = sheetTitle + "(" + date + " Run)";
                                }
			} 
			cell.setCellValue(sheetTitle);
			cell.setCellStyle(boldLeftStyle);
			// merge the title cells
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,
					SpreadsheetConstants.SHEET_NUM_COLUMNS_MAP.get(sheetName)));
		}
	}

	/**
	 *  Populate each sheet as designated by SpreadsheetConstants.
	 */
	private static void processSingleModelDataSet(ExamMetrics examMetrics, Workbook wb) {
		for(String sheetName : SpreadsheetConstants.SHEET_TITLE_MAP.keySet()) {
			processSheet(examMetrics, wb, sheetName);
		}
	}

	/**
	 *  Calls the appropriate data population function for each sheet.
	 */
	private static void processSheet(ExamMetrics examMetrics, Workbook wb, String identifier) {
		int row_index = 1; // row 0 is title
		Sheet sheet = wb.getSheet(identifier);

		if(identifier.equals(SpreadsheetConstants.SHEET_OVERALL)) {
			createOverallSheet(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_RR)) {
			createRrTable(examMetrics, examMetrics.getRR(), SpreadsheetConstants.SHEET_RR, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_PM)) {
			createPmTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_CS)) {
			createCsTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_AA)) {
			createAaTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_RSR_Spec)) {
			createRsrSpecTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_RSR_Variants)) {
			createRsrVariantsTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_ASR)) {
			createAsrTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_RMR)) {
			createRmrTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_NFA)) {
			createNfaTable(sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_PDE)) {
			createPdeTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_AV)) {
			createAvTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_SS)) {
			createSsTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_CB)) {
			createCbTable(examMetrics, sheet, row_index);
		}

		autosizeColumns(sheet, identifier);
	}

	/**
	 * Creates the overall cognitive fidelity assessment, comparative performance assessment, 
	 * and neural fidelity assessment tables.
	 */
	private static void createOverallSheet(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		final int trialsAssessedIndex = 5;

		// gather overall tasks assessed
		List<Integer> tasksAssessedList = new ArrayList<Integer>(6);
		for(MissionMetrics t : examMetrics.getTasks()) {
			if(t.isTask_complete()) tasksAssessedList.add(t.getTask_number());
		}
		String tasksAssessed = tasksAssessedList.toString();

		// Overall CFA table
		row_index = startNewTable(sheet, row_index, 
				"Cognitive Fidelity Assessment (CFA)",
				new String[] {"Bias", "Pass/Fail", "Score", "Missions Assessed",
				"Missions Missing", "Trials Assessed", "Trials Missing"});
		processOverallCFACPAMetric(examMetrics.getAA(), sheet, row_index++, trialsAssessedIndex);
		int firstTableRowIndex = row_index;
		processOverallCFACPAMetric(examMetrics.getPDE(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getRR(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getAV(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getPM(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getCS(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getSS(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getCB(), sheet, row_index++, trialsAssessedIndex);
		int lastTableRowIndex = row_index;
		String pass = (examMetrics.isCFA_pass()) ? "Pass" : "Fail";
		String score = examMetrics.getCFA_score() + 
				"% ("+examMetrics.getCFA_credits_earned()+"/"+examMetrics.getCFA_credits_possible()+" Biases)";
		Row overallRow = populateNewRow(sheet, row_index++, new String[] {"Overall", pass, score, tasksAssessed, 
				"", ""+0, ""}, 
				new int[] {trialsAssessedIndex});
		sumCfaCpaTrialsAssessed(overallRow, trialsAssessedIndex, firstTableRowIndex, lastTableRowIndex);
		boldCenterRow(overallRow);

		// Overall CPA table
		row_index = startNewTable(sheet, row_index, 
				"Comparative Performance Assessment (CPA)",
				new String[] {"Measure", "Pass/Fail", "Score", "Missions Assessed",
				"Missions Missing", "Trials Assessed", "Trials Missing"});
		processOverallCFACPAMetric(examMetrics.getASR(), sheet, row_index++, trialsAssessedIndex);
		firstTableRowIndex = row_index;
		processOverallCFACPAMetric(examMetrics.getRMR(), sheet, row_index++, trialsAssessedIndex);
		lastTableRowIndex = row_index;
		pass = (examMetrics.isCPA_pass()) ? "Pass" : "Fail";
		score = roundToStr(examMetrics.getCPA_score(), 2)+"%";
		overallRow = populateNewRow(sheet, row_index++, new String[] {"Overall", pass, score, tasksAssessed, 
				"", ""+0, ""},
				new int[] {trialsAssessedIndex});
		sumCfaCpaTrialsAssessed(overallRow, trialsAssessedIndex, firstTableRowIndex, lastTableRowIndex);
		boldCenterRow(overallRow);

		// NFA table
		row_index = startNewTable(sheet, row_index, 
				"Neural Fidelity Assessment (NFA)",
				new String[] {"Measure", "Pass/Fail", "Score"});
		overallRow = populateNewRow(sheet, row_index++, new String[] {"Overall", "", ""});
		boldCenterRow(overallRow);

	}

	/**
	 * Calculates the sum of a column by setting a formula.
	 */
	private static void sumCfaCpaTrialsAssessed(Row overallRow, int trialsAssessedIndex, int firstTableRowIndex,
			int lastTableRowIndex) {
		Cell avg = overallRow.getCell(trialsAssessedIndex);
		String colStr = CellReference.convertNumToColString(trialsAssessedIndex);
		String formula = "SUM("+colStr+firstTableRowIndex+":"+colStr+lastTableRowIndex+")";
		avg.setCellFormula(formula);
		formulaEvaluator.evaluateInCell(avg);
	}

	/**
	 * Populates a row of overall metric data.
	 */
	private static void processOverallCFACPAMetric(OverallCFACPAMetric metric, Sheet sheet, int row_index, 
			int trialsAssessedIndex) {
		String name = (metric.getName() != null) ? metric.getName() : "";
		String passFail = (metric.pass != null) ? ((metric.pass) ? "Pass" : "Fail") : "";
		String score = (metric.score != null) ? roundToStr(metric.score, 2) : "";
		String tasksPresent = (metric.tasks_present != null) ? metric.tasks_present.toString() : "";
		String tasksMissing = (metric.tasks_missing != null) ? metric.tasks_missing.toString() : ""; // none
		String trialsPresent = (metric.trials_stages_present != null) ? metric.trials_stages_present.toString() : "";
		String trialsMissing = (metric.trials_stages_missing != null && metric.trials_stages_missing > 0) 
				? metric.trials_stages_missing.toString() : ""; // none
				populateNewRow(sheet, row_index,
						new String[] {name, passFail, score+"%", tasksPresent, tasksMissing, 
						trialsPresent, trialsMissing},
						new int[] {trialsAssessedIndex});
	}

	/**
	 * Populates the Anchoring and Adjustment table with both model and human data.
	 */
	private static void createAaTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		List<Double> weights = humanDataSet.getMetricsInfo().getAA_info().getTaskWeights();
		Boolean useWeights = humanDataSet.getMetricsInfo().getAA_info().isUseTaskWeights();
		String weight = "N/A";
		// all missions
		String overallScore = roundToStr(examMetrics.getAA().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, overallTableTitle,
				new String[] {"Mission", "AA SMR Score", "Weight"});
		for(Integer taskNo : examMetrics.getAA().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			Double aiAvgScore = missionMetrics.getAA_smr_score();
			String aaSmrScore = "";
			if(aiAvgScore != null) {
				aaSmrScore = roundToStr(aiAvgScore, 2);
			}
			if(weights != null && useWeights) {
				weight = roundToStr(weights.get(taskNo-1), 2);
			}
			populateNewRow(sheet, row_index++, new String[] {""+taskNo, aaSmrScore, weight});
		}
		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", overallScore, "N/A"});
		boldCenterRow(overall);

		// model
		for(Integer taskNo : examMetrics.getAA().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			Double aiAvgScore = missionMetrics.getAA_smr_score();
			String tableTitle = "Mission "+taskNo;
			if(aiAvgScore != null) {
				tableTitle += " (Avg Score: "+ roundToStr(aiAvgScore, 2)+"%)";
			}
			row_index = startNewTable(sheet, row_index, 
					tableTitle,
					new String[] {"Trial", "Pass/Fail", "Bias Observed For Humans", "Assessed",
					"Bias Observed For Model", "Bias Magnitude For Humans", "Bias Magnitude For Model",
			});
			int lastRowIndex = populateAaTable(missionMetrics, sheet, row_index);

			//TODO: will this order correctly populate human data for > 1 task?
			// human
			for(MissionMetrics humanMissionMetrics : humanDataSet.getTaskMetrics()) {
				if(humanMissionMetrics.getTask_number().equals(taskNo) && humanMissionMetrics.isTask_complete()) {
					populateAaTable(humanMissionMetrics, sheet, row_index);
				}
			}

			row_index = lastRowIndex;
		}

	}

	/**
	 * Populates the Anchoring and Adjustment table row given a MissionMetric.
	 */
	private static int populateAaTable(MissionMetrics missionMetrics, Sheet sheet, int row_index) {
		boolean isHuman = missionMetrics.isHuman();

		for(TrialData trial : missionMetrics.getTrials()) {
			String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";
			TrialMetrics trialMetrics = trial.getMetrics();
			NegentropyMetrics negentropyMetrics = trialMetrics.getNegentropyMetrics();
			Iterator<Double> deltaNpIter = negentropyMetrics.getNp_delta().iterator();
			Iterator<Double> deltaNqIter = negentropyMetrics.getNq_delta().iterator();
			// burn through one that denotes the initial empty AA value TODO: still valid?
			deltaNpIter.next();
			deltaNqIter.next();
			int stage = 0;
			int colorOnIndex = -1;
			if( trialMetrics.getAA_metrics() != null ) {
				CFAMetric aa = trialMetrics.getAA_metrics();
				if(aa.assessed != null) {
					String assessed = (aa.assessed != null) ? ((aa.assessed) ? "Yes" : "No") : "";
					String passFail = (aa.pass != null) ? ((aa.pass) ? "Pass" : "Fail") : "";
					if((assessed.equals("No")) || (isHuman && shouldExcludeStage(missionMetrics, trial, stage))) {
						passFail = "N/A";
					}
					String exhibited = (aa.exhibited != null) ? ((aa.exhibited) ? "Yes" : "No") : "";
					String magnitude = (aa.magnitude != null) ? roundToStr(aa.magnitude, 3) : "";
					String absDeltaNpLessThanAbsDeltaNq = "";
					String signDeltaNpEqualsSignDeltaNq = "";
					Double deltaNp = deltaNpIter.next();
					Double deltaNq = deltaNqIter.next();
					if(deltaNp != null && deltaNq != null) {
						absDeltaNpLessThanAbsDeltaNq = (Math.abs(deltaNp) < Math.abs(deltaNq)) ? "Yes" : "No";
						signDeltaNpEqualsSignDeltaNq = ((deltaNp<0) == (deltaNq<0)) ? "Yes" : "No";
					}
					if(isHuman) {
						int passFailColIndex = 2; 
						populateExistingRow(sheet, row_index++, 
								new String[] {trialNo, passFail, exhibited, assessed, null,
								magnitude, null},
								passFailColIndex); // overwrite pass/fail row
					} else {
						// color bias exhibited for models if assessed
						if(assessed.equalsIgnoreCase("Yes")) {
							colorOnIndex = 4;
						}
						populateNewRow(sheet, row_index++, 
								new String[] {trialNo, passFail, null, assessed, exhibited, 
								null, magnitude}, colorOnIndex);
					}
				}
			}
		}
		return row_index;
	}

	/**
	 * Returns true if the given stage should be excluded per the human data. Used by anchoring and adjustment.
	 */
	private static boolean shouldExcludeStage(MissionMetrics missionMetrics, TrialData trial, int stage) {
		// iterate through all excluded stages to find if this stage needs to be excluded
		List<TrialIdentifier> excludedTrials = humanDataSet.getMetricsInfo().getAA_info().getExcludedTrials();
		if(excludedTrials != null) {
			for(TrialIdentifier excludedTrial : excludedTrials ) {
				if(excludedTrial.getTask_number().equals(missionMetrics.getTask_number())
						&& excludedTrial.getTrial_number().equals(trial.getTrial_number())
						&& excludedTrial.getStages().contains(Integer.valueOf(stage-1))) { 
					// stage-1 because human data stages are 1-based
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates and populates the Persistence of Discredited Evidence table.
	 */
	private static void createPdeTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		List<Double> weights = humanDataSet.getMetricsInfo().getPDE_info().getTaskWeights();
		Boolean useWeights = humanDataSet.getMetricsInfo().getPDE_info().isUseTaskWeights();
		String weight = "N/A";
		// all missions
		String overallScore = roundToStr(examMetrics.getPDE().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, overallTableTitle,
				new String[] {"Mission", "PDE SMR Score", "Weight"});
		for(Integer taskNo : examMetrics.getPDE().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			Double score = missionMetrics.getPDE_smr_score();
			String scoreStr = "";
			if(score != null) {
				scoreStr = roundToStr(score, 2);
			}
			if(weights != null && useWeights) {
				weight = roundToStr(weights.get(taskNo-1), 2);
			}
			populateNewRow(sheet, row_index++, new String[] {""+taskNo, scoreStr, weight});
		}
		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", overallScore, "N/A"});
		boldCenterRow(overall);

		// model
		for(Integer taskNo : examMetrics.getPDE().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			Double avgScore = missionMetrics.getPDE_smr_score();
			String tableTitle = "Mission "+taskNo;
			if(avgScore != null) {
				tableTitle += " (SMR Score: "+ roundToStr(avgScore, 2)+"%)";
			}
			row_index = startNewTable(sheet, row_index, 
					tableTitle,
					new String[] {"Trial", "Pass/Fail", "Bias Observed For Humans", "Assessed",
					"Bias Observed For Model", "Bias Magnitude For Humans", "Bias Magnitude For Model",
			});
			int lastRowIndex = populatePdeTable(missionMetrics, sheet, row_index);

			// human
			for(MissionMetrics humanMissionMetrics : humanDataSet.getTaskMetrics()) {
				if(humanMissionMetrics.getTask_number().equals(taskNo) && humanMissionMetrics.isTask_complete()) {
					populatePdeTable(humanMissionMetrics, sheet, row_index);
				}
			}

			row_index = lastRowIndex;
		}

	}

	/**
	 * Populates the Persistence of Discredited Evidence table row given a MissionMetric.
	 */
	private static int populatePdeTable(MissionMetrics missionMetrics, Sheet sheet, int row_index) {
		boolean isHuman = missionMetrics.isHuman();

                boolean firstTrial = true;
		for(TrialData trial : missionMetrics.getTrials()) {                        
			String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";                       
			TrialMetrics trialMetrics = trial.getMetrics();
			int stage = 0;
			int colorOnIndex = -1;
			if( !firstTrial && trialMetrics.getPDE_metrics() != null ) {
				CFAMetric pde = trialMetrics.getPDE_metrics();
				if(pde.assessed != null) {
					String assessed = (pde.assessed != null) ? ((pde.assessed) ? "Yes" : "No") : "";
					String passFail = (pde.pass != null) ? ((pde.pass) ? "Pass" : "Fail") : "";
					if((assessed.equals("No")) || (isHuman && shouldExcludeStage(missionMetrics, trial, stage))) {
						passFail = "N/A";
					}
					String exhibited = (pde.exhibited != null) ? ((pde.exhibited) ? "Yes" : "No") : "";
					String magnitude = (pde.magnitude != null) ? roundToStr(pde.magnitude, 3) : "";
					if(isHuman) {
						int passFailColIndex = 2; 
						populateExistingRow(sheet, row_index++, 
								new String[] {trialNo, passFail, exhibited, assessed, null,
								magnitude, null},
								passFailColIndex); // overwrite pass/fail row
					} else {
						if(assessed.equals("Yes")) {
							colorOnIndex = 4;
						}
						populateNewRow(sheet, row_index++, 
								new String[] {trialNo, passFail, null, assessed, exhibited, 
								null, magnitude}, colorOnIndex);
					}
				}
			}
                        firstTrial = false;
		}
		return row_index;
	}

	/**
	 * Creates and populates the Availability table.
	 */
	private static void createAvTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		List<Double> weights = humanDataSet.getMetricsInfo().getAV_info().getTaskWeights();
		Boolean useWeights = humanDataSet.getMetricsInfo().getAV_info().isUseTaskWeights();
		String weight = "N/A";
		// all missions
		String overallScore = roundToStr(examMetrics.getAV().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, overallTableTitle,
				new String[] {"Mission", "AV SMR Score", "Weight"});
		for(Integer taskNo : examMetrics.getAV().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			Double score = missionMetrics.getAV_smr_score();
			String scoreStr = "";
			if(score != null) {
				scoreStr = roundToStr(score, 2);
			}
			if(weights != null && useWeights) {
				weight = roundToStr(weights.get(taskNo-1), 2);
			}
			populateNewRow(sheet, row_index++, new String[] {""+taskNo, scoreStr, weight});
		}
		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", overallScore, "N/A"});
		boldCenterRow(overall);

		// model
		for(Integer taskNo : examMetrics.getAV().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			Double avgScore = missionMetrics.getAV_smr_score();
			String tableTitle = "Mission "+taskNo;
			if(avgScore != null) {
				tableTitle += " (SMR Score: "+ roundToStr(avgScore, 2)+"%)";
			}
			row_index = startNewTable(sheet, row_index, 
					tableTitle,
					new String[] {"Trial", "Pass/Fail", "Bias Observed For Humans", "Assessed",
					"Bias Observed For Model", "Bias Magnitude For Humans", "Bias Magnitude For Model",
			});
			int lastRowIndex = populateAvTable(missionMetrics, sheet, row_index);

			// human
			for(MissionMetrics humanMissionMetrics : humanDataSet.getTaskMetrics()) {
				if(humanMissionMetrics.getTask_number().equals(taskNo) && humanMissionMetrics.isTask_complete()) {
					populateAvTable(humanMissionMetrics, sheet, row_index);
				}
			}

			row_index = lastRowIndex;
		}

	}

	/**
	 * Populates the Availability table row given a MissionMetric.
	 */
	private static int populateAvTable(MissionMetrics missionMetrics, Sheet sheet, int row_index) {
		boolean isHuman = missionMetrics.isHuman();

		for(TrialData trial : missionMetrics.getTrials()) {
			String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";
			TrialMetrics trialMetrics = trial.getMetrics();
			int stage = 0;
			int colorOnIndex = -1;
			if( trialMetrics.getAV_metrics() != null ) {
				CFAMetric av = trialMetrics.getAV_metrics();
				if(av.assessed != null) {
					String assessed = (av.assessed != null) ? ((av.assessed) ? "Yes" : "No") : "";
					String passFail = (av.pass != null) ? ((av.pass) ? "Pass" : "Fail") : "";
					if((assessed.equals("No")) && (isHuman && shouldExcludeStage(missionMetrics, trial, stage))) {
						passFail = "N/A";
					}
					String exhibited = (av.exhibited != null) ? ((av.exhibited) ? "Yes" : "No") : "";
					String magnitude = (av.magnitude != null) ? roundToStr(av.magnitude, 3) : "";
					if(isHuman) {
						int passFailColIndex = 2; 
						populateExistingRow(sheet, row_index++, 
								new String[] {trialNo, passFail, exhibited, assessed, null,
								magnitude, null},
								passFailColIndex); // overwrite pass/fail row
					} else {
						if(assessed.equals("Yes")) {
							colorOnIndex = 4;
						}
						populateNewRow(sheet, row_index++, 
								new String[] {trialNo, passFail, null, assessed, exhibited, 
								null, magnitude}, colorOnIndex);
					}
				}
			}
		}
		return row_index;
	}


	/**
	 * Creates and populates the relative success rate table.
	 */
	private static void createRsrSpecTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// title and headers
		String overallRsr = roundToStr(examMetrics.getRSR().score, 2);
		String tableTitle = "All Missions (Weighted Avg: "+overallRsr+"%)";
		row_index = startNewTable(sheet, row_index, tableTitle,
				new String[] {"Mission", "Stage", "Stage Name", "RSR", "Weight"});

		// populate table with 1 row per stage and/or overall average
		for(Integer taskNo : examMetrics.getRSR().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			boolean hasOneStage = false;
			RSRAndASRMissionMetrics rsrAsrMetrics = missionMetrics.getRSR_ASR();
			Iterator<String> stageNameIter = rsrAsrMetrics.getStageNames().iterator();
			String rsrAvg = roundToStr(rsrAsrMetrics.getRSR_avg(), 2);

			// output average if there are > 1 stages
			if(rsrAvg != null && rsrAsrMetrics.getRSR_stage_avg().size()>1) {
				// moved below to appear after individual stages
			} else {
				hasOneStage = true;
			}

			// output stage averages
			int stage = 1;
			for(Double rsrStageAvg : rsrAsrMetrics.getRSR_stage_avg()) {
				if(rsrStageAvg == null) {
					stage++;
				} else {
					String rsrStageAvgStr = roundToStr(rsrStageAvg, 2);
					Row row = populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, ""+stage++, stageNameIter.next(), rsrStageAvgStr, 
							((hasOneStage) ? roundToStr(rsrAsrWeights.get(taskNo-1),3) : "N/A")});
					if(hasOneStage) {
						boldCenterRow(row);
					}
				}
			}

			if(!hasOneStage) {
				Row row = populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "All", "All", rsrAvg, roundToStr(rsrAsrWeights.get(taskNo-1),3)});
				boldCenterRow(row);

			}
		}
		// overall row
		Row overallRow = populateNewRow(sheet, row_index, 
				new String[] {"Overall", "All", "All", overallRsr, "N/A"});
		boldCenterRow(overallRow);
	}

	/**
	 * Creates and populates the relative success rate variants table.
	 */
	private static void createRsrVariantsTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// title and headers
		String tableTitle = "All Missions";
		row_index = startNewTable(sheet, row_index, 
				tableTitle,
				new String[] {"Mission", "Stage", "Stage Name", "RSR Test Spec", "RSR-Bayesian",
				"RSR-Spm-Spr-Avg", "RSR-Spm-Spq-Avg", "RSR(RMSE)", "RSR-Bayesian(RMSE)", "Weight"});

		// populate a row for each stage and/or overall 
		for(Integer taskNo : examMetrics.getRSR().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			RSRAndASRMissionMetrics rsrAsrMetrics = missionMetrics.getRSR_ASR();
			String rsrAvg = roundToStr(rsrAsrMetrics.getRSR_avg(), 2);
			String rsrBayAvg = roundToStr(rsrAsrMetrics.getRSR_Bayesian_avg(), 2);
			String sprAvg = roundToStr(rsrAsrMetrics.getRSR_Spm_Spr_avg(), 2);
			String spqAvg = roundToStr(rsrAsrMetrics.getRSR_Spm_Spq_avg(), 2);
			String rsrAlt1Avg = roundToStr(rsrAsrMetrics.getRSR_alt_1_avg(), 2);
			String rsrAlt2Avg = roundToStr(rsrAsrMetrics.getRSR_alt_2_avg(), 2);

			// output overall when there when there are > 1 stages
			boolean hasOneStage = false;
			if(rsrAvg != null && rsrAsrMetrics.getRSR_stage_avg().size()>1) {
				// moved below to appear after individual stages
			} else {
				hasOneStage = true;
			}

			// use iterators to go through each piece of data
			Iterator<Double> testSpecIter = rsrAsrMetrics.getRSR_stage_avg().iterator();
			Iterator<Double> bayesianIter = rsrAsrMetrics.getRSR_Bayesian_stage_avg().iterator();
			Iterator<Double> sprIter = rsrAsrMetrics.getRSR_Spm_Spr_stage_avg().iterator();
			Iterator<Double> spqIter = rsrAsrMetrics.getRSR_Spm_Spq_stage_avg().iterator();
			Iterator<Double> alt1Iter = rsrAsrMetrics.getRSR_alt_1_stage_avg().iterator();
			Iterator<Double> alt2Iter = rsrAsrMetrics.getRSR_alt_2_stage_avg().iterator();
			Iterator<String> stageNameIter = rsrAsrMetrics.getStageNames().iterator();

			// output stage average 
			int stage = 1;
			for(Double rsrStageAvg : rsrAsrMetrics.getRSR_stage_avg()) {
				if(rsrStageAvg == null) {
					stage++;
				} else {
					String rsrStageAvgStr = roundToStr(getNextDouble(testSpecIter), 2);
					String rsrBayStageAvg = roundToStr(getNextDouble(bayesianIter), 2);
					String sprStageAvg = roundToStr(getNextDouble(sprIter), 2);
					String spqStageAvg = roundToStr(getNextDouble(spqIter), 2);
					String rsrAlt1StageAvg = roundToStr(getNextDouble(alt1Iter), 2);
					String rsrAlt2StageAvg = roundToStr(getNextDouble(alt2Iter), 2);
					String stageName = stageNameIter.next();
					Row row = populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, ""+stage++, stageName, rsrStageAvgStr, 
							rsrBayStageAvg, sprStageAvg, spqStageAvg, rsrAlt1StageAvg, rsrAlt2StageAvg,
							((hasOneStage) ? roundToStr(rsrAsrWeights.get(taskNo-1),3) : "N/A")});
					if(hasOneStage) {
						boldCenterRow(row);
					}
				}
			}

			if(!hasOneStage) {
				Row row = populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "All", "All", rsrAvg, rsrBayAvg, sprAvg, spqAvg,
						rsrAlt1Avg, rsrAlt2Avg, roundToStr(rsrAsrWeights.get(taskNo-1),3)});
				boldCenterRow(row);
			}
		}

		// overall row
		String overallRsr = roundToStr(examMetrics.getRSR().score, 2);
		String overallBayesian = roundToStr(examMetrics.getRSR_Bayesian().score, 2);
		String overallSpr = roundToStr(examMetrics.getRSR_Spm_Spr_avg().score, 2);
		String overallSpq = roundToStr(examMetrics.getRSR_Spm_Spq_avg().score, 2);
		String overallAlt1 = roundToStr(examMetrics.getRSR_alt_1().score, 2);
		String overallAlt2 = roundToStr(examMetrics.getRSR_alt_2().score, 2);
		Row overallRow = populateNewRow(sheet, row_index, 
				new String[] {"Overall", "All", "All", overallRsr, overallBayesian, overallSpr, 
				overallSpq, overallAlt1, overallAlt2, "N/A"});
		boldCenterRow(overallRow);
		
		// place definitions
		insertBlankRow(sheet, row_index++);
		styleRow(populateNewRow(sheet, row_index++, new String[] {"Defintions:"}), leftStyle);
		for(String definition : SpreadsheetConstants.RSR_VARIANTS_DEFINITIONS) {
			Row row = populateNewRow(sheet, row_index, new String[] {definition});
			styleRow(row, leftStyle);
			// merge the title cells
			sheet.addMergedRegion(new CellRangeAddress(row_index,row_index,0,
				SpreadsheetConstants.SHEET_NUM_COLUMNS_MAP.get(SpreadsheetConstants.SHEET_RSR_Variants)));
			row_index++;
		}
	}

	/**
	 * Creates and populates the absolute success rate table.
	 */
	private static void createAsrTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// title and headers
		String overallAsr = roundToStr(examMetrics.getASR().score, 2);
		String tableTitle = "All Missions (Weighted Avg: "+overallAsr+"%)";
		row_index = startNewTable(sheet, row_index, 
				tableTitle,
				new String[] {"Mission", "Stage", "Stage Name", "ASR", "Weight"});

		// populate a row for each stage and/or overall 
		for(Integer taskNo : examMetrics.getRSR().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			RSRAndASRMissionMetrics rsrAsrMetrics = missionMetrics.getRSR_ASR();
			String asrAvg = roundToStr(rsrAsrMetrics.getASR_avg(), 2);
			String rsrAvg = roundToStr(rsrAsrMetrics.getRSR_avg(), 2);

			// output overall average when there are more than 1 stages
			boolean hasOneStage = false;
			if(rsrAvg != null && rsrAsrMetrics.getRSR_stage_avg().size()>1) {
				// moved all row below to have it be last
			} else {
				hasOneStage = true;
			}
			// iterate through 
			Iterator<Double> asrIter = rsrAsrMetrics.getASR_stage_avg().iterator();
			Iterator<Double> rsrIter = rsrAsrMetrics.getRSR_stage_avg().iterator();
			Iterator<String> stageNameIter = rsrAsrMetrics.getStageNames().iterator();

			// output stage average
			int stage = 1;
			for(Double rsrStageAvg : rsrAsrMetrics.getRSR_stage_avg()) {
				if(rsrStageAvg == null) {
					stage++;
				} else {
					String rsrStageAvgStr = roundToStr(getNextDouble(rsrIter), 2);
					String asrStageAvg = roundToStr(getNextDouble(asrIter), 2);
					String stageName = stageNameIter.next();
					Row row = populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, ""+stage++, stageName, asrStageAvg, 
							((hasOneStage) ? roundToStr(rsrAsrWeights.get(taskNo-1),3) : "N/A")});
					if(hasOneStage) {
						boldCenterRow(row);
					}
				}
			}

			if(!hasOneStage) {
				Row row = populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "All", "All", asrAvg, 
						roundToStr(rsrAsrWeights.get(taskNo-1),3)});
				boldCenterRow(row);
			}
		}

		// overall row
		String overallRsr = roundToStr(examMetrics.getRSR().score, 2);
		Row overallRow = populateNewRow(sheet, row_index, 
				new String[] {"Overall", "All", "All", overallAsr, "N/A"});
		boldCenterRow(overallRow);
	}

	/**
	 * Creates and populates the relative match rate table.
	 */
	private static void createRmrTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		String overallRmr = roundToStr(examMetrics.getRMR().score, 2);
		List<Double> weights = humanDataSet.getMetricsInfo().getRMR_info().getTaskWeights();
		Boolean useWeights = humanDataSet.getMetricsInfo().getRMR_info().isUseTaskWeights();
		String weight = "N/A";
		
		// title and headers
		String tableTitle = "All Missions (Weighted Avg: "+overallRmr+"%)";
		row_index = startNewTable(sheet, row_index, 
				tableTitle,
				new String[] {"Mission", "Stage Name", "RMR", "Weight"});
		int[] numericColIndices = new int[] {2};

		// create a row for each mission
		for(Integer taskNo : examMetrics.getRMR().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);

			// include separate sigint and blue actions averages for mission 3
			if(taskNo == 3) {
				List<TrialData> trialDatas = missionMetrics.getTrials();
				Double sigintSum = 0.0;
				Double blueActionsSum = 0.0;
				for(TrialData trialData : trialDatas) {
					TrialMetrics trialMetrics = trialData.getMetrics();
					CPAMetric sigintMetric = trialMetrics.getRMR_sigint_metrics();
					CPAMetric blueActionsMetric = trialMetrics.getRMR_blueAction_metrics();
					sigintSum += sigintMetric.score;
					blueActionsSum += blueActionsMetric.score;
				}
				populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "SIGINT", roundToStr(sigintSum/trialDatas.size(), 2), "N/A"},
						numericColIndices);
				populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "Blue Actions", roundToStr(blueActionsSum/trialDatas.size(), 2), "N/A"},
						numericColIndices);
			}

			// populate a row for each trial

			String rmrAvg = roundToStr(missionMetrics.getRMR_avg(), 2);
			String stageName = "Blue Actions";
			if(taskNo == 3) stageName = "All";
			if(weights != null && useWeights) {
				weight = roundToStr(weights.get(taskNo-1), 2);
			}
			boldCenterRow(populateNewRow(sheet, row_index++, 
					new String[] {""+taskNo, stageName, rmrAvg, weight},
					numericColIndices));
		}

		// overall row 
		Row overallRow = populateNewRow(sheet, row_index, 
				new String[] {"Overall", "All", overallRmr, "N/A"}, 
				numericColIndices);
		boldCenterRow(overallRow);
	}

	/**
	 *  Creates and populates the Representativeness table.
	 */
	private static void createRrTable(ExamMetrics examMetrics, OverallCFACPAMetric metric, 
			String identifier, Sheet sheet, int row_index) {
		List<Double> weights = humanDataSet.getMetricsInfo().getRR_info().getTaskWeights();
		Boolean useWeights = humanDataSet.getMetricsInfo().getRR_info().isUseTaskWeights();
		String weight = "N/A";
		// all missions
		String overallScore = roundToStr(examMetrics.getRR().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, overallTableTitle,
				new String[] {"Mission", "RR SMR Score", "Weight"});
		for(Integer taskNo : examMetrics.getRR().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			Double rrAvgScore = missionMetrics.getRR_smr_score();
			String rrSmrScore = "";
			if(rrAvgScore != null) {
				rrSmrScore = roundToStr(rrAvgScore, 2);
			}
			if(weights != null && useWeights) {
				weight = roundToStr(weights.get(taskNo-1), 2);
			}
			populateNewRow(sheet, row_index++, new String[] {""+taskNo, rrSmrScore, weight});
		}
		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", overallScore, "N/A"});
		boldCenterRow(overall);
		
		// create a table for each task
		for(Integer taskNo : metric.tasks_present) {
			// headers and title
			String taskId = "Mission"+taskNo;
			MissionMetrics missionMetrics = examMetrics.getTask(taskId);
			Double avgScore = null;
			if(identifier.equals(SpreadsheetConstants.SHEET_RR)) {
				avgScore = missionMetrics.getRR_smr_score();
			} else if (identifier.equals(SpreadsheetConstants.SHEET_PM)) {
				//				avgScore = missionMetrics.getPM_RMS_avg_score();
			} 
			String tableTitle = "Mission "+taskNo;
			if(avgScore != null) tableTitle += " (Avg Score: "+avgScore+"%)";
			row_index = startNewTable(sheet, row_index, 
					tableTitle,
					new String[] {"Trial", "Pass/Fail", "Bias Observed For Humans", "Assessed",
					"Bias Observed For Model", "Bias Magnitude For Humans", "Bias Magnitude For Model"});
			int tableRowStartIndex = row_index;

			// model
			boolean isHuman = missionMetrics.isHuman();
			for(TrialData trial : missionMetrics.getTrials()) {
				String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";
				TrialMetrics metrics = trial.getMetrics();
				CFAMetric cfa = null;
				if(identifier.equals(SpreadsheetConstants.SHEET_RR)) {
					cfa = metrics.getRR_metrics().get(0);
				} else if (identifier.equals(SpreadsheetConstants.SHEET_PM)) {
					//					cfa = metrics.getPM();
				} 
				populateRrTrialData(trialNo, cfa, isHuman, sheet, row_index++);
			}

			// human
			MissionMetrics humanMissionMetrics = humanDataSet.getTaskMetrics(taskId);
			isHuman = humanMissionMetrics.isHuman();
			for(TrialData trial : humanMissionMetrics.getTrials()) {
				String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";
				TrialMetrics metrics = trial.getMetrics();
				CFAMetric cfa = null;
				if(identifier.equals(SpreadsheetConstants.SHEET_RR)) {
					cfa = metrics.getRR_metrics().get(0);
				} else if (identifier.equals(SpreadsheetConstants.SHEET_PM)) {
					//					cfa = metrics.getPM();
				} 
				populateRrTrialData(trialNo, cfa, isHuman, sheet, tableRowStartIndex++);
			}
		}
	}

	/**
	 * Populates a row for each CFAMetric.
	 */
	private static void populateRrTrialData(String trialNo, CFAMetric metric, boolean isHuman, 
			Sheet sheet, int row_index) {
		String assessed = (metric.assessed != null) ? ((metric.assessed) ? "Yes" : "No") : "";
		String passFail = (metric.pass != null && assessed.equals("Yes")) ? ((metric.pass) ? "Pass" : "Fail") : "N/A";
		String exhibited = (metric.exhibited != null) ? ((metric.exhibited) ? "Yes" : "No") : "";
		String magnitude = (metric.magnitude != null) ? roundToStr(metric.magnitude, 3) : "";
		if(isHuman) {
			populateExistingRow(sheet, row_index++, 
					new String[] {trialNo, passFail, exhibited, assessed, null, magnitude, null});

		} else {
			int colorOnIndex = -1;
			if(assessed.equals("Yes")) colorOnIndex = 4;
			populateNewRow(sheet, row_index++, 
					new String[] {trialNo, passFail, null, assessed, exhibited, null, magnitude}, colorOnIndex);
		}
	}

	/**
	 * Creates and populates the confirmation seeking table.
	 */
	private static void createCsTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// all missions
		String overallScore = roundToStr(examMetrics.getCS().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, 
				overallTableTitle,
				new String[] {"Mission", "Bias Observed For Humans", "Assessed",
				"Bias Observed For Model", "f for Humans (fH)", "f For Model (fM)",
				"CS MSR Score", "Weight"});
		for(Integer taskNo : examMetrics.getCS().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			populateCsTable(missionMetrics, sheet, row_index, true);
		}
		// human
		for(MissionMetrics missionMetrics : humanDataSet.getTaskMetrics()) {
			populateCsTable(missionMetrics, sheet, row_index, true);
		}
		row_index++;

		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", 
				"N/A", "N/A", "N/A", "N/A", "N/A", overallScore, "N/A"});
		boldCenterRow(overall);


		// model
		for(Integer taskNo : examMetrics.getCS().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			String missionTableTitle = "Mission "+taskNo+" (nH: "+ 
					roundToStr(humanDataSet.getTaskMetrics().get(taskNo-1).getCS_sigintHighestPaSelectionFrequency(), 2)
					+", nM: "+roundToStr(missionMetrics.getCS_sigintHighestPaSelectionFrequency(),2)+")";
			row_index = startNewTable(sheet, row_index, 
					missionTableTitle,
					new String[] {"Trial", "f for Humans (fH)", "f For Model (fM)"});
			populateCsTable(missionMetrics, sheet, row_index, false);
		}

		// human
		for(MissionMetrics missionMetrics : humanDataSet.getTaskMetrics()) {
			populateCsTable(missionMetrics, sheet, row_index, false);
		}
	}

	/**
	 * Populates a row of the confirmation seeking table with MissionMetrics data.
	 */
	private static void populateCsTable(MissionMetrics missionMetrics, Sheet sheet, int row_index, boolean isAllMissions) {
		boolean isHuman = missionMetrics.isHuman();
		if(isAllMissions) {
			List<Double> weights = humanDataSet.getMetricsInfo().getCS_info().getTaskWeights();
			Boolean useWeights = humanDataSet.getMetricsInfo().getCS_info().isUseTaskWeights();
			CFAMetric metric = missionMetrics.getCS_metrics();
			if(metric != null) {
				String assessed = (metric.assessed != null) ? ((metric.assessed) ? "Yes" : "No") : "";
				String exhibited = (metric.exhibited != null) ? ((metric.exhibited) ? "Yes" : "No") : "";
				String score = (metric.score != null) ? roundToStr(metric.score, 2) : "";
				String f = roundToStr(missionMetrics.getCS_sigintHighestPaSelectionFrequency(), 2);
				Integer taskNo = missionMetrics.getTask_number();
				String weight = "N/A";
				if(weights != null && useWeights) {
					weight = roundToStr(weights.get(taskNo-1), 2);
				}
				if(isHuman) {
					populateExistingRow(sheet, row_index++, 
							new String[] {""+taskNo, exhibited, assessed, null, f, null, score, weight});

				} else {
					populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, null, assessed, exhibited, null, f, score, weight});
				}
			}	
		} else {
			for(TrialData trialData : missionMetrics.getTrials()) {
				String trialF = roundToStr(trialData.getMetrics().getCS_sigintAtHighestPaLocationSelections(), 2);
				if(isHuman) {
					populateExistingRow(sheet, row_index++, new String[] {""+trialData.getTrial_number(), trialF, null});
				} else {
					populateNewRow(sheet, row_index++, new String[] {""+trialData.getTrial_number(), null, trialF});
				}
			}
		}
	}

	/**
	 * Creates and populates the probability matching table.
	 */
	private static void createPmTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// all missions
		String overallScore = roundToStr(examMetrics.getPM().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, 
				overallTableTitle,
				new String[] {"Mission", "Bias Observed For Humans", "Assessed",
				"Bias Observed For Model", "n for Humans (nH)", "n For Model (nM)",
				"PM MSR Score", "Weight"});
		int firstRowIndex = row_index-1;
		for(Integer taskNo : examMetrics.getPM().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			populatePmTable(missionMetrics, sheet, row_index++, true);
		}
		// human
		for(MissionMetrics missionMetrics : humanDataSet.getTaskMetrics()) {
			populatePmTable(missionMetrics, sheet, firstRowIndex++, true);
		}

		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", 
				"N/A", "N/A", "N/A", "N/A", "N/A", overallScore, "N/A"});
		boldCenterRow(overall);


		// model
		firstRowIndex = row_index+3; // including overall and table title
		for(Integer taskNo : examMetrics.getPM().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			String missionTableTitle = "Mission "+taskNo+" (nH: "+ 
					roundToStr(humanDataSet.getTaskMetrics().get(taskNo-1).getPM_normativeBlueOptionSelectionFrequency(), 2)
					+", nM: "+roundToStr(missionMetrics.getPM_normativeBlueOptionSelectionFrequency(),2)+")";
			row_index = startNewTable(sheet, row_index, 
					missionTableTitle,
					new String[] {"Trial", "n for Humans (nH)", "n For Model (nM)"});

			row_index = populatePmTable(missionMetrics, sheet, row_index++, false);
		}

		// human
		for(MissionMetrics missionMetrics : humanDataSet.getTaskMetrics()) {
			if(examMetrics.getPM().tasks_present.contains(missionMetrics.getTask_number())) {
				firstRowIndex = populatePmTable(missionMetrics, sheet, firstRowIndex++, false) +3; // +3 for table title and spaces
			}
		}
	}

	/**
	 * Populates a row of the probability matching table with MissionMetrics data.
	 */
	private static int populatePmTable(MissionMetrics missionMetrics, Sheet sheet, int row_index, boolean isAllMissions) {
		boolean isHuman = missionMetrics.isHuman();
		if(isAllMissions) {
			List<Double> weights = humanDataSet.getMetricsInfo().getPM_info().getTaskWeights();
			Boolean useWeights = humanDataSet.getMetricsInfo().getPM_info().isUseTaskWeights();
			CFAMetric metric = missionMetrics.getPM_metrics();
			if(metric != null) {
				String assessed = (metric.assessed != null) ? ((metric.assessed) ? "Yes" : "No") : "";
				String exhibited = (metric.exhibited != null) ? ((metric.exhibited) ? "Yes" : "No") : "";
				String score = (metric.score != null) ? roundToStr(metric.score, 2) : "";
				String n = roundToStr(missionMetrics.getPM_normativeBlueOptionSelectionFrequency(), 2);
				Integer taskNo = missionMetrics.getTask_number();
				String weight = "N/A";
				if(weights != null && useWeights) {
					weight = roundToStr(weights.get(taskNo-1), 2);
				}
				if(isHuman) {
					populateExistingRow(sheet, row_index++, 
							new String[] {""+taskNo, exhibited, assessed, null, n, null, score, weight});

				} else {
					populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, null, assessed, exhibited, null, n, score, weight});
				}
			}	
		} else {
			for(TrialData trialData : missionMetrics.getTrials()) {
				String trialN = roundToStr(trialData.getMetrics().getPM_normativeBlueOptionSelections(), 2);

				if(isHuman) {
					populateExistingRow(sheet, row_index++, new String[] {""+trialData.getTrial_number(), trialN, null});
				} else {
					populateNewRow(sheet, row_index++, new String[] {""+trialData.getTrial_number(), null, trialN});
				}
			}
		}
		return row_index;
	}

	/**
	 * Creates and populates the Satisfaction of Search table.
	 */
	private static void createSsTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// all missions
		String overallScore = roundToStr(examMetrics.getSS().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, 
				overallTableTitle,
				new String[] {"Mission", "Bias Observed For Humans", "Assessed",
				"Bias Observed For Model", "s for Humans (sH)", "s For Model (sM)",
				"SS MSR Score", "Weight"});
		int firstRowIndex = row_index-3; // hit first line for human data
		for(Integer taskNo : examMetrics.getSS().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			populateSsTable(missionMetrics, sheet, row_index++, true);
		}
		// human
		for(MissionMetrics missionMetrics : humanDataSet.getTaskMetrics()) {
			populateSsTable(missionMetrics, sheet, firstRowIndex++, true);
		}

		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", 
				"N/A", "N/A", "N/A", "N/A", "N/A", overallScore, "N/A"});
		boldCenterRow(overall);


		// model
		firstRowIndex = row_index+3; // including overall and table title
		for(Integer taskNo : examMetrics.getSS().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			String missionTableTitle = "Mission "+taskNo+" (sH: "+ 
					roundToStr(humanDataSet.getTaskMetrics().get(taskNo-1).getSS_percentTrialsReviewedInBatchPlot_avg(), 2)
					+", sM: "+roundToStr(missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg(),2)+")";
			row_index = startNewTable(sheet, row_index, 
					missionTableTitle,
					new String[] {"Trial", "s for Humans (sH)", "s For Model (sM)"});

			row_index = populateSsTable(missionMetrics, sheet, row_index++, false);
		}

		// human
		for(MissionMetrics missionMetrics : humanDataSet.getTaskMetrics()) {
			if(examMetrics.getSS().tasks_present.contains(missionMetrics.getTask_number())) {
				firstRowIndex = populateSsTable(missionMetrics, sheet, firstRowIndex++, false) +3; // +3 for table title and spaces
			}
		}
	}

	/**
	 * Populates a row of the Satisfaction of Search table with MissionMetrics data.
	 */
	private static int populateSsTable(MissionMetrics missionMetrics, Sheet sheet, int row_index, boolean isAllMissions) {
		boolean isHuman = missionMetrics.isHuman();
		if(isAllMissions) {
			List<Double> weights = humanDataSet.getMetricsInfo().getSS_info().getTaskWeights();
			Boolean useWeights = humanDataSet.getMetricsInfo().getSS_info().isUseTaskWeights();
			CFAMetric metric = missionMetrics.getSS_metrics();
			if(metric != null) {
				String assessed = (metric.assessed != null) ? ((metric.assessed) ? "Yes" : "No") : "";
				String exhibited = (metric.exhibited != null) ? ((metric.exhibited) ? "Yes" : "No") : "";
				String score = (metric.score != null) ? roundToStr(metric.score, 2) : "";
				String n = roundToStr(missionMetrics.getSS_percentTrialsReviewedInBatchPlot_avg(), 2);
				Integer taskNo = missionMetrics.getTask_number();
				String weight = "N/A";
				if(weights != null && useWeights) {
					weight = roundToStr(weights.get(taskNo-1), 2);
				}
				if(isHuman) {
					populateExistingRow(sheet, row_index++, 
							new String[] {""+taskNo, exhibited, assessed, null, n, null, score, weight});

				} else {
					populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, null, assessed, exhibited, null, n, score, weight});
				}
			}	
		} else {
			for(TrialData trialData : missionMetrics.getTrials()) {
				String trialN = roundToStr(trialData.getMetrics().getSS_percentTrialsReviewedInBatchPlot(), 2);
				if(trialN != null) {
					if(isHuman) {
						populateExistingRow(sheet, row_index++, new String[] {""+trialData.getTrial_number(), trialN, null});
					} else {
						populateNewRow(sheet, row_index++, new String[] {""+trialData.getTrial_number(), null, trialN});
					}
				}
			}
		}
		return row_index;
	}
	
	/**
	 * Creates and populates the Change Blindness table.
	 */
	private static void createCbTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// all missions
		String overallScore = roundToStr(examMetrics.getCB().score, 2);
		String overallTableTitle = "All Missions (Weighted Avg: "+overallScore+"%)"; 
		row_index = startNewTable(sheet, row_index, 
				overallTableTitle,
				new String[] {"Mission", "Bias Observed For Humans", "Assessed",
				"Bias Observed For Model", "Change Detected by Humans", "Change Detected by Model", 
				"b for Humans (bH)", "b For Model (bM)",
				"CB MSR Score", "Weight"});
		int firstRowIndex = row_index-3; // match row hit by model
		for(Integer taskNo : examMetrics.getCB().tasks_present) {
			MissionMetrics missionMetrics = examMetrics.getTask("Mission"+taskNo);
			populateCbTable(missionMetrics, sheet, row_index++, true);
		}
		// human
		for(MissionMetrics missionMetrics : humanDataSet.getTaskMetrics()) {
			populateCbTable(missionMetrics, sheet, firstRowIndex++, true);
		}

		Row overall = populateNewRow(sheet, row_index++, new String[] {"Overall", 
				"N/A", "N/A", "N/A", "N/A", "N/A", "N/A", "N/A", overallScore, "N/A"});
		boldCenterRow(overall);
	}
	
	/**
	 * Populates a row of Change Blindness table with MissionMetrics data.
	 */
	private static int populateCbTable(MissionMetrics missionMetrics, Sheet sheet, int row_index, boolean isAllMissions) {
		boolean isHuman = missionMetrics.isHuman();
		if(isAllMissions) {
			List<Double> weights = humanDataSet.getMetricsInfo().getCB_info().getTaskWeights();
			Boolean useWeights = humanDataSet.getMetricsInfo().getCB_info().isUseTaskWeights();
			CFAMetric metric = missionMetrics.getCB_metrics();
			if(metric != null) {
				String assessed = (metric.assessed != null) ? ((metric.assessed) ? "Yes" : "No") : "";
				String exhibited = (metric.exhibited != null) ? ((metric.exhibited) ? "Yes" : "No") : "";
				String score = (metric.score != null) ? roundToStr(metric.score, 2) : "";
				String b = roundToStr(missionMetrics.getCB_trialsNeededToDetectRedTacticChanges_avg(), 2);
				Boolean isDetected = missionMetrics.getCB_redTacticsChangedDetected().get(0);
				String detected = (isDetected != null) ? ((isDetected) ? "Yes" : "No") : ""; 
				Integer taskNo = missionMetrics.getTask_number();
				String weight = "N/A";
				if(weights != null && useWeights) {
					weight = roundToStr(weights.get(taskNo-1), 2);
				}
				if(isHuman) {
					populateExistingRow(sheet, row_index++, 
							new String[] {""+taskNo, exhibited, assessed, null, detected, null, b, null, score, weight});

				} else {
					populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, null, assessed, exhibited, null, detected, null, b, score, weight});
				}
			}	
		} 
		return row_index;
	}

	/**
	 * Initializes the Neural Fidelity Assessment table for later population.
	 */
	private static void createNfaTable(Sheet sheet, int row_index) {
		row_index = startNewTable(sheet, row_index, 
				null,
				new String[] {"Model", "Implemented", "Pass NFA", "Integrated"});
		String[] models = {"PFC", "ACC", "TC", "PFC", "MTL/H", "BG", "BNS"};
		for(String model : models) {
			populateNewRow(sheet, row_index++, new String[]{ model, "", "", "" } );
		}
	}

	/**
	 * Creates bolded title and header rows for a new table.
	 * Returns the index of the next row.
	 */
	private static int startNewTable(Sheet sheet, int row_index, String title,
			String[] columnNames) {
		insertBlankRow(sheet, row_index++);
		// merge the title cells
		sheet.addMergedRegion(new CellRangeAddress(row_index, row_index, 0,
				SpreadsheetConstants.SHEET_NUM_COLUMNS_MAP.get(sheet.getSheetName())-1));
		Row row = populateNewRow(sheet, row_index++, 
				new String[] {title});
		boldLeftRow(row);
		row = populateNewRow(sheet, row_index++, columnNames);
		styleRow(row, boldShadedCenterStyle);
		return row_index;
	}

	/**
	 * Populates a row with the specified values, as numeric types if possible.
	 */
	private static Row populateNewRow(Sheet sheet, int row_num, String[] values) {
		return populateNewRow(sheet, row_num, values, null);
	}
	private static Row populateNewRow(Sheet sheet, int row_num, String[] values, int colorOnIndex) {
		return populateNewRow(sheet, row_num, values, null, colorOnIndex);
	}
	/**
	 * Populates a row with the specified values, as numeric types if possible, 
	 * turning the columns specified by numericColIndices into numeric cells for formula evaluation. 
	 */
	private static Row populateNewRow(Sheet sheet, int row_num, String[] values, int[] numericColIndices) {
		return populateNewRow(sheet, row_num, values, numericColIndices, -1);
	}
	private static Row populateNewRow(Sheet sheet, int row_num, String[] values, int[] numericColIndices, int colorOnIndex) {
		Row row = sheet.createRow(row_num);

		// create numeric cells for use with formulas
		if(numericColIndices != null) {
			for(int i = 0; i < numericColIndices.length; i++) {
				int numericColIndex = numericColIndices[i];
				String val = values[numericColIndex];
				if(val != null) {
					try {
						Cell cell = row.createCell(numericColIndex, Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.parseDouble(val));
						cell.setCellStyle(centerStyle);
					} catch (NumberFormatException e) {
						System.err.println("Cannot format value "+val+" as a double.");
					}
				}
			}
		}

		// populate cells with provided values, as numeric types if possible
		int col_index = 0;
		for(String value : values) {
			if(value != null) {
				Cell cell = row.createCell(col_index);
				try {
					// set a numeric value if possible
					double doubleVal = Double.parseDouble(value);
					cell.setCellValue(doubleVal);
				} catch (NumberFormatException nfe) {
					// otherwise set value as a String
					cell.setCellValue(value);
				}
				// a Pass/Fail failure is in red font
				if(value.equalsIgnoreCase("fail") || (col_index == colorOnIndex && value.equalsIgnoreCase("No"))) {
					cell.setCellStyle(redCenterStyle);
				} else if(value.equalsIgnoreCase("pass") || (col_index == colorOnIndex && value.equalsIgnoreCase("Yes"))) {
					cell.setCellStyle(greenCenterStyle);
				} else {
					cell.setCellStyle(centerStyle);
				}
			}
			col_index++;
		}
		return row;
	}

	/**
	 * Creates a blank row.
	 */
	private static void insertBlankRow(Sheet sheet, int row_num) {
		sheet.createRow(row_num);
	}

	/**
	 * Returns a String of the given value rounded to a number of 
	 * decimal places.
	 */
	private static String roundToStr(Double value, int places) {
		if(value == null || places < 0) return null;
		final double d = Math.pow(10, places);
		return "" + Math.round(value*d) / d;
	}

	/**
	 * Returns the next value from a Double iterator.
	 */
	private static Double getNextDouble(Iterator<Double> iter) {
		if(iter != null) {
			while(iter.hasNext()) {
				final Double d = iter.next();
				if(d != null) {
					return d;
				}
			}
		}
		return null;
	}

	/**
	 * Places values into an existing row's cells, creating or overwriting them as necessary.  
	 * Numeric values will be inserted as such.
	 */
	private static void populateExistingRow(Sheet sheet, int row_index, String[] values) {
		populateExistingRow(sheet, row_index, values, null);
	}
	private static void populateExistingRow(Sheet sheet, int row_index, String[] values, 
			Integer overwriteRowIndex) {
		Row row = sheet.getRow(row_index);
		if(row != null) {
			for(int i = 0; i < values.length; i++) {
				String value = values[i];
				if(((row.getCell(i) == null) || (Integer.valueOf(i).equals(overwriteRowIndex))) 
						&& value != null && !value.equals("")) {
					try {
						Cell cell = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.parseDouble(value));
						cell.setCellStyle(centerStyle);
					} catch (NumberFormatException e) {
						Cell cell = row.createCell(i);
						cell.setCellValue(value);
						cell.setCellStyle(centerStyle);
					}
				}
			}
		}
	}

	/**
	 * Bold and center a row of cells.  For overall rows.
	 */
	private static void boldCenterRow(Row row) {
		styleRow(row, boldCenterStyle);
	}

	/**
	 * Bold a row of cells.  For sheet and table titles.
	 */
	private static void boldLeftRow(Row row) {
		styleRow(row, boldLeftStyle);
	}

	/**
	 * Apply a style to a row of cells.
	 */
	private static void styleRow(Row row, CellStyle style) {
		final Iterator<Cell> cellIter = row.cellIterator();
		while(cellIter.hasNext()) {
			Cell cell = cellIter.next();
			cell.setCellStyle(style);
		}
	}

	/**
	 * Auto-size each column of the sheet.
	 */
	private static void autosizeColumns(Sheet sheet, String identifier) {
		for(int i = 0; i < SpreadsheetConstants.SHEET_NUM_COLUMNS_MAP.get(identifier); i++) {
			sheet.autoSizeColumn(i, false);
		}
	}
}

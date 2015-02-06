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
package org.mitre.icarus.cps.assessment.persistence.phase_1.spreadsheet;

import java.util.ArrayList;
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
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.OverallCFACPAMetric;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.TrialIdentifier;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.ExamMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TaskMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialData;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.AverageHumanDataSet_Phase1;
import org.mitre.icarus.cps.assessment.data_model.phase_1.data_sets.SingleModelDataSet_Phase1;

/**
 * Combines a model data set and a human data set into an Excel spreadsheet.
 */
public class SpreadsheetMarshaller {

	private static Workbook wb;
	private static FormulaEvaluator formulaEvaluator;
	private static List<Double> taskWeights;
	private static boolean useTaskWeights;
	private static AverageHumanDataSet_Phase1 humanDataSet;
	private static CellStyle boldLeftStyle;
	private static CellStyle boldCenterStyle;
	private static CellStyle boldShadedCenterStyle;
	private static CellStyle redCenterStyle;
	private static CellStyle centerStyle;

	/**
	 * Returns an Excel workbook containing the combined model and human data.
	 */
	public static Workbook marshall(SingleModelDataSet_Phase1 dataset, AverageHumanDataSet_Phase1 humanData, Workbook wb) {
		formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
		taskWeights = humanData.getMetricsInfo().getRsr_asr_task_weights();
		useTaskWeights = humanData.getMetricsInfo().isUse_rsr_asr_task_weights();
		humanDataSet = humanData;
		initializeStyles(wb);
		initializeSheets(wb);
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

		centerStyle = wb.createCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		setStyleBorder(centerStyle, CellStyle.BORDER_THIN);
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
	private static void initializeSheets(Workbook wb) {
		for(Entry<String,String> title : SpreadsheetConstants.SHEET_TITLE_MAP.entrySet()) {
			Sheet sheet = wb.createSheet(title.getKey());
			Row row = sheet.createRow((short)0);
			Cell cell = row.createCell(0);
			cell.setCellValue(title.getValue());
			cell.setCellStyle(boldLeftStyle);
			// merge the title cells
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,
					SpreadsheetConstants.SHEET_NUM_COLUMNS_MAP.get(title.getKey())));
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
			createCFACPABiasTable(examMetrics, examMetrics.getRR(), SpreadsheetConstants.SHEET_RR, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_PM)) {
			createCFACPABiasTable(examMetrics, examMetrics.getPM_RMS(), SpreadsheetConstants.SHEET_PM, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_CS)) {
			createCsTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_AA)) {
			createAATable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_RSR_Spec)) {
			createRsrSpecTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_RSR_Variants)) {
			createRsrVariantsTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_ASR)) {
			createAsrTable(examMetrics, sheet, row_index);
		} else if(identifier.equals(SpreadsheetConstants.SHEET_RMR)) {
			createRmrTable(examMetrics, sheet, row_index);
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
		for(TaskMetrics t : examMetrics.getTasks()) {
			if(t.isTask_complete()) tasksAssessedList.add(t.getTask_number());
		}
		String tasksAssessed = tasksAssessedList.toString();

		// Overall CFA table
		row_index = startNewTable(sheet, row_index, 
				"Cognitive Fidelity Assessment (CFA)",
				new String[] {"Bias", "Pass/Fail", "Score", "Tasks Assessed",
				"Tasks Missing", "Trials Assessed", "Trials Missing"});
		processOverallCFACPAMetric(examMetrics.getRR(), sheet, row_index++, trialsAssessedIndex);
		int firstTableRowIndex = row_index;
		processOverallCFACPAMetric(examMetrics.getAI(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getPM_RMS(), sheet, row_index++, trialsAssessedIndex);
		processOverallCFACPAMetric(examMetrics.getCS(), sheet, row_index++, trialsAssessedIndex);
		int lastTableRowIndex = row_index;
		String pass = (examMetrics.isCFA_pass()) ? "Pass" : "Fail";
		String score = examMetrics.getCFA_score() + 
				" ("+examMetrics.getCFA_credits_earned()+"/"+examMetrics.getCFA_credits_possible()+")";
		Row overallRow = populateNewRow(sheet, row_index++, new String[] {"Overall", pass, score, tasksAssessed, 
				null, ""+0, null}, 
				new int[] {trialsAssessedIndex});
		sumCfaCpaTrialsAssessed(overallRow, trialsAssessedIndex, firstTableRowIndex, lastTableRowIndex);
		boldCenterRow(overallRow);

		// Overall CPA table
		row_index = startNewTable(sheet, row_index, 
				"Comparative Performance Assessment (CPA)",
				new String[] {"Measure", "Pass/Fail", "Score", "Tasks Assessed",
				"Tasks Missing", "Trials Assessed", "Trials Missing"});
		processOverallCFACPAMetric(examMetrics.getRSR(), sheet, row_index++, trialsAssessedIndex);
		firstTableRowIndex = row_index;
		processOverallCFACPAMetric(examMetrics.getRMR(), sheet, row_index++, trialsAssessedIndex);
		lastTableRowIndex = row_index;
		pass = (examMetrics.isCPA_pass()) ? "Pass" : "Fail";
		score = roundToStr(examMetrics.getCPA_score(), 2);
		overallRow = populateNewRow(sheet, row_index++, new String[] {"Overall", pass, score, tasksAssessed, 
				null, ""+0, null},
				new int[] {trialsAssessedIndex});
		sumCfaCpaTrialsAssessed(overallRow, trialsAssessedIndex, firstTableRowIndex, lastTableRowIndex);
		boldCenterRow(overallRow);

		// NFA table
		row_index = startNewTable(sheet, row_index, 
				"Neural Fidelity Assessment (NFA)",
				new String[] {"Measure", "Pass/Fail", "Score"});
		overallRow = populateNewRow(sheet, row_index++, new String[] {"Overall"});
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
						new String[] {name, passFail, score, tasksPresent, tasksMissing, 
						trialsPresent, trialsMissing},
						new int[] {trialsAssessedIndex});
	}

	/**
	 * Populates the Anchoring and Adjustment table with both model and human data.
	 */
	private static void createAATable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// model
		for(Integer taskNo : examMetrics.getAI().tasks_present) {
			TaskMetrics taskMetrics = examMetrics.getTask("Task"+taskNo);
			Double aiAvgScore = taskMetrics.getAI_avg_score();
			String tableTitle = "Task "+taskNo;
			if(aiAvgScore != null) {
				tableTitle += " (Avg Score: "+ roundToStr(aiAvgScore, 2)+"%)";
			}
			row_index = startNewTable(sheet, row_index, 
					tableTitle,
					new String[] {"Trial", "Stage", "Pass/Fail", "Bias Observed For Humans", "Assessed",
					"Bias Observed For Model", "Bias Magnitude For Humans", "Bias Magnitude For Model",
					"|delta Np| < |delta Nq| for Model", "Sign(delta Np) = Sign(delta Nq) for Model"});
			populateAATable(taskMetrics, sheet, row_index);
		}
		//TODO: will this order correctly populate human data for > 1 task?
		// human
		for(TaskMetrics taskMetrics : humanDataSet.getTaskMetrics()) {
			if(taskMetrics.isTask_complete()) {
				populateAATable(taskMetrics, sheet, row_index);
			}
		}
	}

	/**
	 * Populates the Anchoring and Adjustment table row given a TaskMetric.
	 */
	private static void populateAATable(TaskMetrics taskMetrics, Sheet sheet, int row_index) {
		boolean isHuman = taskMetrics.isHuman();

		for(TrialData trial : taskMetrics.getTrials()) {
			String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";
			TrialMetrics metrics = trial.getMetrics();
			Iterator<Double> deltaNpIter = metrics.getNp_delta().iterator();
			Iterator<Double> deltaNqIter = metrics.getNq_delta().iterator();
			// burn through one that denotes the initial empty AI value
			deltaNpIter.next();
			deltaNqIter.next();
			int stage = 0;
			if( metrics.getAI() != null ) {
				for(CFAMetric ai : metrics.getAI()) {
					stage++;
					if(ai.assessed != null) {
						String passFail = (ai.pass != null) ? ((ai.pass) ? "Pass" : "Fail") : "";
						if(isHuman && shouldExcludeStage(taskMetrics, trial, stage)) {
							passFail = "N/A";
						}
						String assessed = (ai.assessed != null) ? ((ai.assessed) ? "Yes" : "No") : "";
						String exhibited = (ai.exhibited != null) ? ((ai.exhibited) ? "Yes" : "No") : "";
						String magnitude = (ai.magnitude != null) ? roundToStr(ai.magnitude, 3) : "";
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
									new String[] {trialNo, ""+stage, passFail, exhibited, assessed, null,
									magnitude, null, absDeltaNpLessThanAbsDeltaNq, signDeltaNpEqualsSignDeltaNq},
									passFailColIndex); // overwrite pass/fail row
						} else {
							populateNewRow(sheet, row_index++, 
									new String[] {trialNo, ""+stage, passFail, null, assessed, exhibited, 
									null, magnitude, absDeltaNpLessThanAbsDeltaNq, signDeltaNpEqualsSignDeltaNq});
						}
					}
				}
			}
		}
	}

	/**
	 * Returns true if the given stage should be excluded per the human data. Used by anchoring and adjustment.
	 */
	private static boolean shouldExcludeStage(TaskMetrics taskMetrics, TrialData trial, int stage) {
		// iterate through all excluded stages to find if this stage needs to be excluded
		for(TrialIdentifier excludedTrial : humanDataSet.getMetricsInfo().getAI_info().getExcludedTrials()) {
			if(excludedTrial.getTask_number().equals(taskMetrics.getTask_number())
					&& excludedTrial.getTrial_number().equals(trial.getTrial_number())
					&& excludedTrial.getStages().contains(Integer.valueOf(stage-1))) { 
				// stage-1 because human data stages are 1-based
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates and populates the relative success rate table.
	 */
	private static void createRsrSpecTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// title and headers
		String overallRsr = roundToStr(examMetrics.getRSR().score, 2);
		String tableTitle = "All Tasks (Weighted Avg: "+overallRsr+")";
		row_index = startNewTable(sheet, row_index, tableTitle,
				new String[] {"Task", "Stage", "RSR", "Weight"});

		// populate table with 1 row per stage and/or overall average
		for(Integer taskNo : examMetrics.getRSR().tasks_present) {
			TaskMetrics taskMetrics = examMetrics.getTask("Task"+taskNo);
			boolean hasOneStage = false;
			String rsrAvg = roundToStr(taskMetrics.getRSR_avg(), 2);

			// output average if there are > 1 stages
			if(rsrAvg != null && taskMetrics.getRSR_stage_avg().size()>1) {
				Row row = populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "All", rsrAvg, roundToStr(taskWeights.get(taskNo-1),3)});
				boldCenterRow(row);
			} else {
				hasOneStage = true;
			}

			// output stage averages
			int stage = 1;
			for(Double rsrStageAvg : taskMetrics.getRSR_stage_avg()) {
				if(rsrStageAvg == null) {
					stage++;
				} else {
					String rsrStageAvgStr = roundToStr(rsrStageAvg, 2);
					Row row = populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, ""+stage++, rsrStageAvgStr, 
							((hasOneStage) ? roundToStr(taskWeights.get(taskNo-1),3) : null)});
					if(hasOneStage) {
						boldCenterRow(row);
					}
				}
			}
		}
		// overall row
		Row overallRow = populateNewRow(sheet, row_index, 
				new String[] {"Overall", "All", overallRsr, null});
		boldCenterRow(overallRow);
	}

	/**
	 * Creates and populates the relative success rate variants table.
	 */
	private static void createRsrVariantsTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// title and headers
		String tableTitle = "All Tasks";
		row_index = startNewTable(sheet, row_index, 
				tableTitle,
				new String[] {"Task", "Stage", "RSR Test Spec", "RSR-Bayesian",
				"RSR-Spm-Spr-Avg", "RSR-Spm-Spq-Avg", "RSR(RMSE)", "RSR-Bayesian(RMSE)", "Weight"});

		// populate a row for each stage and/or overall 
		for(Integer taskNo : examMetrics.getRSR().tasks_present) {
			TaskMetrics taskMetrics = examMetrics.getTask("Task"+taskNo);
			String rsrAvg = roundToStr(taskMetrics.getRSR_avg(), 2);
			String rsrBayAvg = roundToStr(taskMetrics.getRSR_Bayesian_avg(), 2);
			String sprAvg = roundToStr(taskMetrics.getRSR_Spm_Spr_avg(), 2);
			String spqAvg = roundToStr(taskMetrics.getRSR_Spm_Spq_avg(), 2);
			String rsrAlt1Avg = roundToStr(taskMetrics.getRSR_alt_1_avg(), 2);
			String rsrAlt2Avg = roundToStr(taskMetrics.getRSR_alt_2_avg(), 2);

			// output overall when there when there are > 1 stages
			boolean hasOneStage = false;
			if(rsrAvg != null && taskMetrics.getRSR_stage_avg().size()>1) {
				Row row = populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "All", rsrAvg, rsrBayAvg, sprAvg, spqAvg,
						rsrAlt1Avg, rsrAlt2Avg, roundToStr(taskWeights.get(taskNo-1),3)});
				boldCenterRow(row);
			} else {
				hasOneStage = true;
			}

			// use iterators to go through each piece of data
			Iterator<Double> testSpecIter = taskMetrics.getRSR_stage_avg().iterator();
			Iterator<Double> bayesianIter = taskMetrics.getRSR_Bayesian_stage_avg().iterator();
			Iterator<Double> sprIter = taskMetrics.getRSR_Spm_Spr_stage_avg().iterator();
			Iterator<Double> spqIter = taskMetrics.getRSR_Spm_Spq_stage_avg().iterator();
			Iterator<Double> alt1Iter = taskMetrics.getRSR_alt_1_stage_avg().iterator();
			Iterator<Double> alt2Iter = taskMetrics.getRSR_alt_2_stage_avg().iterator();

			// output stage average 
			int stage = 1;
			for(Double rsrStageAvg : taskMetrics.getRSR_stage_avg()) {
				if(rsrStageAvg == null) {
					stage++;
				} else {
					String rsrStageAvgStr = roundToStr(getNextDouble(testSpecIter), 2);
					String rsrBayStageAvg = roundToStr(getNextDouble(bayesianIter), 2);
					String sprStageAvg = roundToStr(getNextDouble(sprIter), 2);
					String spqStageAvg = roundToStr(getNextDouble(spqIter), 2);
					String rsrAlt1StageAvg = roundToStr(getNextDouble(alt1Iter), 2);
					String rsrAlt2StageAvg = roundToStr(getNextDouble(alt2Iter), 2);
					Row row = populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, ""+stage++, rsrStageAvgStr, 
							rsrBayStageAvg, sprStageAvg, spqStageAvg, rsrAlt1StageAvg, rsrAlt2StageAvg,
							((hasOneStage) ? roundToStr(taskWeights.get(taskNo-1),3) : null)});
					if(hasOneStage) {
						boldCenterRow(row);
					}
				}
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
				new String[] {"Overall", "All", overallRsr, overallBayesian, overallSpr, 
				overallSpq, overallAlt1, overallAlt2});
		boldCenterRow(overallRow);
	}

	/**
	 * Creates and populates the absolute success rate table.
	 */
	private static void createAsrTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// title and headers
		String overallAsr = roundToStr(examMetrics.getASR().score, 2);
		String tableTitle = "All Tasks (Weighted Avg: "+overallAsr+")";
		row_index = startNewTable(sheet, row_index, 
				tableTitle,
				new String[] {"Task", "Stage", "ASR", "RSR", "Weight"});

		// populate a row for each stage and/or overall 
		for(Integer taskNo : examMetrics.getRSR().tasks_present) {
			TaskMetrics taskMetrics = examMetrics.getTask("Task"+taskNo);
			String asrAvg = roundToStr(taskMetrics.getASR_avg(), 2);
			String rsrAvg = roundToStr(taskMetrics.getRSR_avg(), 2);

			// output overall average when there are more than 1 stages
			boolean hasOneStage = false;
			if(rsrAvg != null && taskMetrics.getRSR_stage_avg().size()>1) {
				Row row = populateNewRow(sheet, row_index++, 
						new String[] {""+taskNo, "All", asrAvg, rsrAvg, 
						roundToStr(taskWeights.get(taskNo-1),3)});
				boldCenterRow(row);
			} else {
				hasOneStage = true;
			}
			// iterate through 
			Iterator<Double> asrIter = taskMetrics.getASR_stage_avg().iterator();
			Iterator<Double> rsrIter = taskMetrics.getRSR_stage_avg().iterator();

			// output stage average
			int stage = 1;
			for(Double rsrStageAvg : taskMetrics.getRSR_stage_avg()) {
				if(rsrStageAvg == null) {
					stage++;
				} else {
					String rsrStageAvgStr = roundToStr(getNextDouble(rsrIter), 2);
					String asrStageAvg = roundToStr(getNextDouble(asrIter), 2);
					Row row = populateNewRow(sheet, row_index++, 
							new String[] {""+taskNo, ""+stage++, asrStageAvg, rsrStageAvgStr,
							((hasOneStage) ? roundToStr(taskWeights.get(taskNo-1),3) : null)});
					if(hasOneStage) {
						boldCenterRow(row);
					}
				}
			}
		}

		// overall row
		String overallRsr = roundToStr(examMetrics.getRSR().score, 2);
		Row overallRow = populateNewRow(sheet, row_index, 
				new String[] {"Overall", "All", overallAsr, overallRsr});
		boldCenterRow(overallRow);
	}

	/**
	 * Creates and populates the relative match rate tables.
	 */
	private static void createRmrTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		String overallRmr = roundToStr(examMetrics.getRMR().score, 2);

		// create a table for each task
		for(Integer taskNo : examMetrics.getRMR().tasks_present) {
			// title and headers
			String tableTitle = "Task "+taskNo+" (Avg: "+overallRmr+")";
			row_index = startNewTable(sheet, row_index, 
					tableTitle,
					new String[] {"Trial", "RMR"});
			int[] numericColIndices = new int[] {1};

			// populate a row for each trial
			TaskMetrics taskMetrics = examMetrics.getTask("Task"+taskNo);
			List<TrialData> trialDatas = taskMetrics.getTrials();
			for(TrialData trialData : trialDatas) {
				int trialNo = trialData.getTrial_number();
				TrialMetrics trialMetrics = trialData.getMetrics();
				String rmrAvg = roundToStr(trialMetrics.getRMR().score, 2);
				populateNewRow(sheet, row_index++, 
						new String[] {""+trialNo, rmrAvg},
						numericColIndices);
			}

			// overall row 
			Row overallRow = populateNewRow(sheet, row_index, 
					new String[] {"Overall", overallRmr}, 
					numericColIndices);
			boldCenterRow(overallRow);
		}
	}

	private static void createCFACPABiasTable(ExamMetrics examMetrics, OverallCFACPAMetric metric, 
			String identifier, Sheet sheet, int row_index) {
		// create a table for each task
		for(Integer taskNo : metric.tasks_present) {
			// headers and title
			String taskId = "Task"+taskNo;
			TaskMetrics taskMetrics = examMetrics.getTask(taskId);
			Double avgScore = null;
			if(identifier.equals(SpreadsheetConstants.SHEET_RR)) {
				avgScore = taskMetrics.getRR_avg_score();
			} else if (identifier.equals(SpreadsheetConstants.SHEET_PM)) {
				avgScore = taskMetrics.getPM_RMS_avg_score();
			} 
			String tableTitle = "Task "+taskNo;
			if(avgScore != null) tableTitle += " (Avg Score: "+avgScore+")";
			row_index = startNewTable(sheet, row_index, 
					tableTitle,
					new String[] {"Trial", "Pass/Fail", "Bias Observed For Humans", "Assessed",
					"Bias Observed For Model", "Bias Magnitude For Humans", "Bias Magnitude For Model"});
			int tableRowStartIndex = row_index;

			// model
			boolean isHuman = taskMetrics.isHuman();
			for(TrialData trial : taskMetrics.getTrials()) {
				String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";
				TrialMetrics metrics = trial.getMetrics();
				CFAMetric cfa = null;
				if(identifier.equals(SpreadsheetConstants.SHEET_RR)) {
					cfa = metrics.getRR();
				} else if (identifier.equals(SpreadsheetConstants.SHEET_PM)) {
					cfa = metrics.getPM();
				} 
				populateCfaCpaTrialData(trialNo, cfa, isHuman, sheet, row_index++);
			}

			// human
			TaskMetrics humanTaskMetrics = humanDataSet.getTaskMetrics(taskId);
			isHuman = humanTaskMetrics.isHuman();
			for(TrialData trial : humanTaskMetrics.getTrials()) {
				String trialNo = (trial.getTrial_number() != null) ? trial.getTrial_number().toString() : "";
				TrialMetrics metrics = trial.getMetrics();
				CFAMetric cfa = null;
				if(identifier.equals(SpreadsheetConstants.SHEET_RR)) {
					cfa = metrics.getRR();
				} else if (identifier.equals(SpreadsheetConstants.SHEET_PM)) {
					cfa = metrics.getPM();
				} 
				populateCfaCpaTrialData(trialNo, cfa, isHuman, sheet, tableRowStartIndex++);
			}
		}
	}

	/**
	 * Populates a row for each CFAMetric.
	 */
	private static void populateCfaCpaTrialData(String trialNo, CFAMetric metric, boolean isHuman, 
			Sheet sheet, int row_index) {
		String passFail = (metric.pass != null) ? ((metric.pass) ? "Pass" : "Fail") : "";
		String assessed = (metric.assessed != null) ? ((metric.assessed) ? "Yes" : "No") : "";
		String exhibited = (metric.exhibited != null) ? ((metric.exhibited) ? "Yes" : "No") : "";
		String magnitude = (metric.magnitude != null) ? roundToStr(metric.magnitude, 3) : "";
		if(isHuman) {
			populateExistingRow(sheet, row_index++, 
					new String[] {trialNo, passFail, exhibited, assessed, null, magnitude, null});

		} else {
			populateNewRow(sheet, row_index++, 
					new String[] {trialNo, passFail, null, assessed, exhibited, null, magnitude});
		}
	}

	/**
	 * Creates and populates the confirmation seeking table.
	 */
	private static void createCsTable(ExamMetrics examMetrics, Sheet sheet, int row_index) {
		// model
		for(Integer taskNo : examMetrics.getCS().tasks_present) {
			row_index = startNewTable(sheet, row_index, 
					"Task "+taskNo,
					new String[] {"Pass/Fail", "Bias Observed For Humans", "Assessed",
					"Bias Observed For Model", "Bias Magnitude For Humans", "Bias Magnitude For Model",
					"C for Humans", "C for Model"});
			TaskMetrics taskMetrics = examMetrics.getTask("Task"+taskNo);
			populateCsTable(taskMetrics, sheet, row_index);
		}

		// human
		for(TaskMetrics taskMetrics : humanDataSet.getTaskMetrics()) {
			populateCsTable(taskMetrics, sheet, row_index);
		}
	}

	/**
	 * Populates a row of the confirmation seeking table with TaskMetrics data.
	 */
	private static void populateCsTable(TaskMetrics taskMetrics, Sheet sheet, int row_index) {
		boolean isHuman = taskMetrics.isHuman();
		CFAMetric metric = taskMetrics.getCS();
		if(metric != null) {
			String passFail = (metric.pass != null) ? ((metric.pass) ? "Pass" : "Fail") : "";
			String assessed = (metric.assessed != null) ? ((metric.assessed) ? "Yes" : "No") : "";
			String exhibited = (metric.exhibited != null) ? ((metric.exhibited) ? "Yes" : "No") : "";
			String magnitude = (metric.magnitude != null) ? roundToStr(metric.magnitude, 2) : "";
			String c = roundToStr(taskMetrics.getC_all_trials(), 2);

			if(isHuman) {
				populateExistingRow(sheet, row_index++, 
						new String[] {passFail, exhibited, assessed, null, magnitude, null, c, null});
			} else {
				populateNewRow(sheet, row_index++, 
						new String[] {passFail, null, assessed, exhibited, null, magnitude, null, c});
			}
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

	/**
	 * Populates a row with the specified values, as numeric types if possible, 
	 * turning the columns specified by numericColIndices into numeric cells for formula evaluation. 
	 */
	private static Row populateNewRow(Sheet sheet, int row_num, String[] values, int[] numericColIndices) {
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
				if(value.equalsIgnoreCase("fail")) {
					cell.setCellStyle(redCenterStyle);
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

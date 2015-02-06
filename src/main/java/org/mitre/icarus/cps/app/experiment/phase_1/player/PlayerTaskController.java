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
package org.mitre.icarus.cps.app.experiment.phase_1.player;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mitre.icarus.cps.app.experiment.IcarusConditionController;
import org.mitre.icarus.cps.app.experiment.TrialPartState;
import org.mitre.icarus.cps.app.experiment.phase_1.player.trial_states.IPlayerTrialState;
import org.mitre.icarus.cps.app.widgets.phase_1.player.ConditionPanel_Player;
import org.mitre.icarus.cps.app.widgets.phase_1.player.TaskResponsePanel;
import org.mitre.icarus.cps.app.window.phase_1.PlayerUtils;
import org.mitre.icarus.cps.assessment.assessment_metrics_computer.phase_1.MetricsUtils;
import org.mitre.icarus.cps.assessment.data_model.base.assessment_metrics.CFAMetric;
import org.mitre.icarus.cps.assessment.data_model.phase_1.assessment_metrics.TrialMetrics;
import org.mitre.icarus.cps.exam.phase_1.playback.ExamPlaybackDataSource_Phase1;
import org.mitre.icarus.cps.exam.phase_1.playback.TaskResponseData_Phase1;
import org.mitre.icarus.cps.exam.phase_1.testing.TaskTestPhase;
import org.mitre.icarus.cps.experiment_core.event.SubjectActionEvent;
import org.mitre.icarus.cps.experiment_core.gui.INavButtonPanel.ButtonType;
import org.mitre.icarus.cps.experiment_core.subject_data.SubjectConditionData;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupAttack;
import org.mitre.icarus.cps.feature_vector.phase_1.GroupType;

/**
 * Base class for player task controllers.
 * 
 * @author CBONACETO
 *
 * @param <T>
 */
public abstract class PlayerTaskController<T extends TaskTestPhase<?>> extends 
	IcarusConditionController<PlayerController, ExamPlaybackDataSource_Phase1, TaskResponseData_Phase1<T>, ConditionPanel_Player> {	
	
	/** The task response */
	protected TaskResponseData_Phase1<T> taskResponse;
	
	/** The task */
	protected T task;	
	
	/** The task response panel */
	protected TaskResponsePanel taskResponsePanel;	
	
	/** The label to use for the participant (e.g., "model", "subject")) */
	protected String participantLabel;
	
	/** The current phase in the current trial */
	protected int trialPhase;
	
	/** The current trial part */
	protected TrialPartState currentTrialPart;
		
	/** Whether to show the score and assessment metrics on each trial */
	protected boolean showScore = true;
	
	/** Whether to display warnings when a trial isn't complete because data is missing. */
	protected boolean showIncompleteTrialWarnings = true;
	
	/** Whether ground truth has been shown */
	protected boolean groundTruthRevealed = false;
	
	/** Whether to show the trial number and trial part number in the instruction banner text */
	protected static boolean showTrialInfoInBanner = false;
	
	/** All group center groups used in the task (used to populated legend at beginning of task) */
	protected Set<GroupType> groupCenterGroupsPresent = new TreeSet<GroupType>();
	
	/** All location used in the task (used to populate legend at beginning of task) */
	protected Set<GroupAttack> locationsPresent = new TreeSet<GroupAttack>();	
	
	/** Whether or not the current trial has been initialized */
	protected boolean trialInitialized;
	
	public PlayerTaskController() {
		pauseLength = 25;
	}
	
	@Override
	public PlayerController getExamController() {
		return examController;
	}	
	
	@Override
	public void startCondition(int firstTrial, PlayerController experimentController,
			SubjectConditionData subjectConditionData) {
		this.examController = experimentController;
		startCondition(firstTrial, false, experimentController, subjectConditionData, "participant");
	}

	/**
	 * @param firstTrial
	 * @param startAtEndOfFirstTrial
	 * @param experimentController
	 * @param subjectConditionData
	 */
	public abstract void startCondition(int firstTrial, boolean startAtEndOfFirstTrial,
			PlayerController experimentController, SubjectConditionData subjectConditionData,
			String participantLabel);	
	
	@Override
	public void subjectActionPerformed(SubjectActionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void performCleanup() {
		//Does nothing
	}

	@Override
	public void initializeConditionController(TaskResponseData_Phase1<T> condition, ConditionPanel_Player conditionPanel) {
		try {
			this.taskResponse = condition;
			this.task = condition != null ? condition.getTestPhase() : null;			
			this.condition = condition;
			this.conditionPanel = conditionPanel;
			this.taskResponsePanel = conditionPanel != null ? conditionPanel.getTaskResponsePanel() : null;
			initializeTask(condition);
		} catch(Exception ex) {
			throw new IllegalArgumentException("Error initializing player task controller: " + ex.toString());
		}		
	}
	
	protected abstract void initializeTask(TaskResponseData_Phase1<T> taskResponse);
	
	public abstract void trialResponseChanged(Integer trialNum);		
	
	public static boolean isShowTrialInfoInBanner() {
		return showTrialInfoInBanner;
	}

	public static void setShowTrialInfoInBanner(boolean showTrialInfoInBanner) {
		PlayerTaskController.showTrialInfoInBanner = showTrialInfoInBanner;
	}
	
	public boolean isShowIncompleteTrialWarnings() {
		return showIncompleteTrialWarnings;
	}

	public void setShowIncompleteTrialWarnings(boolean showIncompleteTrialWarnings) {
		this.showIncompleteTrialWarnings = showIncompleteTrialWarnings;
	}

	@Override
	public void pauseBeforeNextTrial(final long pauseLength) {
		if(getExamController() != null) {
			getExamController().setNavButtonEnabled(ButtonType.Back, true);
		}
		super.pauseBeforeNextTrial(pauseLength);
	}
	
	/**
	 * @param trialNumber
	 * @param trialPartNumber
	 * @param numTrialParts
	 * @param trialPartName
	 * @param trialComplete
	 * @return
	 */
	protected StringBuilder createTrialNumberString(int trialNumber, int trialPartNumber, int numTrialParts, String trialPartName,
			boolean trialComplete) {
		StringBuilder sb = new StringBuilder();
		if(showTrialInfoInBanner) {
			sb.append("Trial <b>");
			sb.append(Integer.toString(trialNumber) + "</b> [");
			if(numTrialParts > 1) {
				sb.append("Part <b>" + Integer.toString(trialPartNumber));
				sb.append("/" + Integer.toString(numTrialParts) + "</b>: ");
			}
			sb.append(trialPartName + "]: ");
		}
		if(showIncompleteTrialWarnings && !trialComplete) {
			sb.append("<font color=\"red\"><i>This is trial is not complete.</i></font> ");
		}
		return sb;
	}	
	
	/**
	 * @param trialState
	 * @return
	 */
	protected StringBuilder createTrialAssessmentResultsString(IPlayerTrialState trialState) {
		return createTrialAssessmentResultsString(trialState, null);
	}
	
	/**
	 * @param trialState
	 * @param sb
	 * @return
	 */
	protected StringBuilder createTrialAssessmentResultsString(IPlayerTrialState trialState, StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder();
		}
		if(trialState != null) {
			if(showScore) {
				//Show the S1 and S2 scores if available
				if(trialState.getScore_s1() != null) {
					long s1_score = Math.round(trialState.getScore_s1());
					if(trialState.getScore_s2() != null) {
						sb.append(" The " + participantLabel + " received <b>" + Math.round(s1_score) + 
								" </b>out of 100 points for estimating probabilities, and" +								
								" <b> " + (Math.round(trialState.getScore_s2())) + " </b>out of 100 points for allocating troops.");
					} else {
						sb.append(" The "  + participantLabel + " received <b>" + Math.round(s1_score) + " " +
								"</b>out of 100 points for estimating probabilities.");
						
					}
				} else if(trialState.getScore_s2() != null) {
					sb.append(" The " + participantLabel + " received <b> " + (Math.round(trialState.getScore_s2())) + 
							" </b>out of 100 points for allocating troops.");
				}				
			}
			
			TrialMetrics metrics = trialState.getParticipantTrialData() != null ? trialState.getParticipantTrialData().getMetrics() : null;
			if(metrics != null && metrics.getRMR() != null && metrics.getRMR().score != null && 
					metrics.getRMR().assessed) {
				//Show the RMR score for Task 6
				if(trialState.getParticipantTrialData().getLs_index() != null) {
					int lsIndex = trialState.getParticipantTrialData().getLs_index();
					sb.append(" The " + participantLabel + " selected the sequence " + 
							MetricsUtils.formatLayerSequenceName(lsIndex, 
									trialState.getParticipantTrialData().getLayer_type() != null ? 
											trialState.getParticipantTrialData().getLayer_type().size() : 3));
					if(trialState.getAvgHumanTrialData() != null && trialState.getAvgHumanTrialData().getMetrics() != null &&
							trialState.getAvgHumanTrialData().getMetrics().getF_LS_percent() != null) {
						List<Double> flsPercent = trialState.getAvgHumanTrialData().getMetrics().getF_LS_percent();
						if(lsIndex >= 0 && lsIndex < flsPercent.size()) {
							sb.append(", which was selected by " + PlayerUtils.formatDoubleAsString(flsPercent.get(lsIndex)*100) + "% of participants.");
						} else {
							sb.append(".");
						}
					} else {
						sb.append(".");
					}
				}
				sb.append(" The RMR score is <b>" + PlayerUtils.formatDoubleAsString(metrics.getRMR().score) + "%</b>.");
			}
		}
		return sb;
	}
	
	/**
	 * @param sb
	 * @param participantMetrics
	 * @param avgHumanMetrics
	 * @return
	 */
	protected StringBuilder create_RR_AssessmentString(StringBuilder sb, TrialMetrics participantMetrics, TrialMetrics avgHumanMetrics) {
		if(sb == null) {
			sb = new StringBuilder();
		}
		if(participantMetrics != null && participantMetrics.getRR() != null && participantMetrics.getRR().exhibited != null) {			
			if(avgHumanMetrics != null && avgHumanMetrics.getRR() != null && avgHumanMetrics.getRR().exhibited != null) {
				if(participantMetrics.getRR().exhibited) {
					if(avgHumanMetrics.getRR().exhibited) {
						sb.append(" Both the " + participantLabel + " and humans exhibited Representativeness (<b>RR</b>)");
					} else {
						sb.append(" The " + participantLabel + " exhibited Representativeness (<b>RR</b>) but humans did not");
					}
				} else {
					if(avgHumanMetrics.getRR().exhibited) {
						sb.append(" The " + participantLabel + " did not exhibit Representativeness (<b>RR</b>) but humans did");
					} else {
						sb.append(" Neither the " + participantLabel + " nor humans exhibited Representativeness (<b>RR</b>)");
					}
				}
			} else {
				String exhibitedString = participantMetrics.getRR().exhibited ? " exhibited " : " did not exhibit ";
				sb.append(" The " + participantLabel + exhibitedString + "Representativeness (<b>RR</b>)");
			}
			
			if(participantMetrics.getRR().score != null) {
				String lessMoreString = participantMetrics.getRR().score > 0 ? "more" : "less"; 
				sb.append(" (the " + participantLabel + " probabilities were " + lessMoreString + " conservative than the normative probabilities)");
			}
			
			if(participantMetrics.getRR().pass != null) {
				sb.append(", thus the " + participantLabel + createPassFailText(participantMetrics.getRR().pass) + "<b>RR</b>.");
			} else {
				sb.append(".");
			}
			
			if(participantMetrics.getRR().assessed != null && !participantMetrics.getRR().assessed) {
				sb.append(" However, RR was not assessed for this trial.");
			} 
		}
		return sb;
	}
	
	/**
	 * @param sb
	 * @param participantMetrics
	 * @param avgHumanMetrics
	 * @return
	 */
	protected StringBuilder create_AI_AssessmentString(StringBuilder sb, TrialMetrics participantMetrics, TrialMetrics avgHumanMetrics,
			int trialPhase) {
		if(sb == null) {
			sb = new StringBuilder();
		}
		CFAMetric participantAI = participantMetrics != null && participantMetrics.getAI() != null 
				&& trialPhase < participantMetrics.getAI().size() ? participantMetrics.getAI().get(trialPhase) : null;
		CFAMetric avgHumanAI = avgHumanMetrics != null && avgHumanMetrics.getAI() != null 
				&& trialPhase < avgHumanMetrics.getAI().size() ? avgHumanMetrics.getAI().get(trialPhase) : null;		
		if(participantAI != null && participantAI.exhibited != null) {			
			if(avgHumanAI != null && avgHumanAI.exhibited != null) {
				if(participantAI.exhibited) {
					if(avgHumanAI.exhibited) {
						sb.append(" Both the " + participantLabel + " and humans exhibited Anchoring & Adjustment (<b>AA</b>)");
					} else {
						sb.append(" The " + participantLabel + " exhibited Anchoring & Adjustment (<b>AA</b>) but humans did not");
					}
				} else {
					if(avgHumanAI.exhibited) {
						sb.append(" The " + participantLabel + " did not exhibit Anchoring & Adjustment (<b>AA</b>) but humans did");
					} else {
						sb.append(" Neither the " + participantLabel + " nor humans exhibited Anchoring & Adjustment (<b>AA</b>)");
					}
				}
			} else {
				String exhibitedString = participantAI.exhibited ? " exhibited " : " did not exhibit ";
				sb.append(" The " + participantLabel + exhibitedString + "Anchoring & Adjustment (<b>AA</b>)");
			}
			
			if(participantAI.score != null) {				
				String lessMoreString = participantAI.score > 0 ? "more" : "less"; 
				sb.append(" (the " + participantLabel + " probability adjustments were " + lessMoreString + " conservative than the normative probability adjustments");
				if(participantMetrics != null && participantMetrics.getNp_delta() != null && participantMetrics.getNq_delta() != null &&
						trialPhase < participantMetrics.getNp_delta().size() && trialPhase < participantMetrics.getNq_delta().size()) {
					List<Double> Np_delta = participantMetrics.getNp_delta();
					List<Double> Nq_delta = participantMetrics.getNq_delta();		
					if(Math.signum(Np_delta.get(trialPhase)) != Math.signum(Nq_delta.get(trialPhase))) {
						sb.append(", but the adjustment was in the wrong direction)");
					} else {
						sb.append(")");
					}
				} else {
					sb.append(")");
				}				
			}
			
			if(participantAI.pass != null) {
				sb.append(", thus the " + participantLabel + createPassFailText(participantAI.pass) + "<b>AA</b>.");
			} else {
				sb.append(".");
			}
			
			if(participantAI.assessed != null && !participantAI.assessed) {
				sb.append(" However, AA was not assessed for this trial.");
			} 
		}
		return sb;
	}
	
	/**
	 * @param sb
	 * @param participantMetrics
	 * @param avgHumanMetrics
	 * @return
	 */
	protected StringBuilder create_PM_RMS_AssessmentString(StringBuilder sb, TrialMetrics participantMetrics, TrialMetrics avgHumanMetrics) {
		if(sb == null) {
			sb = new StringBuilder();
		}
		if(participantMetrics != null && participantMetrics.getPM() != null && participantMetrics.getPM().exhibited != null) {			
			if(avgHumanMetrics != null && avgHumanMetrics.getPM() != null && avgHumanMetrics.getPM().exhibited != null) {
				if(participantMetrics.getPM().exhibited) {
					if(avgHumanMetrics.getPM().exhibited) {
						sb.append(" Both the " + participantLabel + " and humans exhibited Probability Matching (<b>PM</b>)");
					} else {
						sb.append(" The " + participantLabel + " exhibited Probability Matching (<b>PM</b>) but humans did not");
					}
				} else {
					if(avgHumanMetrics.getPM().exhibited) {
						sb.append(" The " + participantLabel + " did not exhibit Probability Matching (<b>PM</b>) but humans did");
					} else {
						sb.append(" Neither the " + participantLabel + " nor humans exhibited Probability Matching (<b>PM</b>)");
					}
				}
			} else {
				String exhibitedString = participantMetrics.getPM().exhibited ? " exhibited " : " did not exhibit ";
				sb.append(" The " + participantLabel + exhibitedString + "Probability Matching (<b>PM</b>)");
			}
			
			if(participantMetrics.getPM().score != null) {
				if(participantMetrics.getPM().score > 0) {
					sb.append(" (the " + participantLabel + " troop allocations were similar to the " + participantLabel + " probabilities)");	
				} else {
					sb.append(" (the " + participantLabel + " troop allocations were similar to an \"all in\" strategy)");
				}				
			}
			
			if(participantMetrics.getPM().pass != null) {
				sb.append(", thus the " + participantLabel + createPassFailText(participantMetrics.getPM().pass) + "<b>PM</b>.");
			} else {
				sb.append(".");
			}
			
			if(participantMetrics.getPM().assessed != null && !participantMetrics.getPM().assessed) {
				sb.append(" However, PM was not assessed for this trial.");
			} 
		}
		return sb;
	}
	
	private String createPassFailText(Boolean pass) {
		return pass ? "<font color=\"green\"><b> passed </b></font>" : "<font color=\"red\"><b> failed </b></font>";
	}
}
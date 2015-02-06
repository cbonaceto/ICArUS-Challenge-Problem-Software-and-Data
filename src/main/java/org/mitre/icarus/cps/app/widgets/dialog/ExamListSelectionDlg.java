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
package org.mitre.icarus.cps.app.widgets.dialog;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.mitre.icarus.cps.web.model.ExamList;
import org.mitre.icarus.cps.web.model.ExamList.ExamListEntry;

/**
 * A dialog for selecting a Phase 1 or Phase 2 exam from a list of exams.
 *
 * @author CBONACETO
 */
public class ExamListSelectionDlg {

    public static final int OK = 1;

    public static final int CANCEL = 0;

    protected JDialog dlg;

    protected JComboBox<ExamListEntry> examComboBox;

    protected int buttonPressed = CANCEL;

    /**
     * Contains the exams to choose from
     */
    protected Vector<ExamListEntry> exams;

    protected ExamListSelectionDlg(String title, String phaseId, 
            Vector<ExamListEntry> exams) {
        this.exams = exams;
        createDlg(title, phaseId);
    }

    public static ExamListEntry showDialog(Component parent, String title, 
            String phaseId, ExamList examList) {
        //Get the exams for the given phase and sort them by name
        Vector<ExamListEntry> exams = new Vector<ExamListEntry>();
        if (examList != null && !examList.isEmpty()) {
            for (ExamListEntry exam : examList) {
                if (phaseId.equalsIgnoreCase(exam.phaseId)) {
                    exams.add(exam);
                }
            }
        }
        /*Collections.sort(exams, new Comparator<ExamListEntry>() {
            @Override
            public int compare(ExamListEntry o1, ExamListEntry o2) {
                if (o1.examName == null) {
                    return -1;
                } else if (o2.examName == null) {
                    return 1;
                } else {
                    return o1.examName.compareTo(o2.examName);
                }
            }
        });*/

        ExamListSelectionDlg dlg = new ExamListSelectionDlg(title, phaseId, exams);
        dlg.dlg.setLocationRelativeTo(parent);
        dlg.dlg.setVisible(true);
        if (dlg.buttonPressed == OK) {
            return (ExamListEntry) dlg.examComboBox.getSelectedItem();
        }
        return null;
    }

    protected final void createDlg(String title, String phaseId) {
        dlg = new JDialog();
        dlg.setTitle(title);
        dlg.setModal(true);

        //Create the dialolg panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel examSelectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        examSelectionPanel.add(new JLabel("Please select a Phase " + phaseId + " Exam:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;         
        examComboBox = new JComboBox<>(exams);       
        examSelectionPanel.add(examComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(examSelectionPanel, gbc);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                buttonPressed = OK;
                //phaseIndex = phaseComboBox.getSelectedIndex();
                dlg.dispose();
            }
        });
        buttonPanel.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                buttonPressed = CANCEL;
                dlg.dispose();
            }
        });
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        
        dlg.getContentPane().add(panel);	
        dlg.pack();
        dlg.setResizable(false);        
    }

    public static void main(String[] args) {
        ExamListSelectionDlg.showDialog(null, "Select a Phase 2 Exam", "2",
				ExamList.createDefaultExamList());
    }
}

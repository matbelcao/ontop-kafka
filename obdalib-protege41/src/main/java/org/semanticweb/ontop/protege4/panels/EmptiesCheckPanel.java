package org.semanticweb.ontop.protege4.panels;

/*
 * #%L
 * ontop-protege4
 * %%
 * Copyright (C) 2009 - 2013 KRDB Research Centre. Free University of Bozen Bolzano.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.semanticweb.ontop.model.Predicate;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.semanticweb.ontop.owlrefplatform.owlapi3.QuestOWLEmptyEntitiesChecker;

public class EmptiesCheckPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 2317777246039649415L;

	public EmptiesCheckPanel(QuestOWLEmptyEntitiesChecker check) {
		initComponents();
		initContent(check);
	}

	private void initContent(QuestOWLEmptyEntitiesChecker check) {

		/* Fill the label summary value */
		String message;
		try {
			message = check.toString();
		} catch (Exception e) {
			message = String.format("%s. Please try again!", e.getMessage());
		}
		lblSummaryValue.setText(message);

		/* Create table list for empty concepts */
		List<Predicate> emptyC = check.getEmptyConcepts();

		final int rowConcepts = emptyC.size();
		final int col = 1;
		final String[] columnConcept = { "Concepts" };

		Object[][] rowDataConcept = new Object[rowConcepts][col];

		int index = 0;

		for (Predicate concept : emptyC) {

			rowDataConcept[index][0] = concept.getName();
			index++;
		}
		JTable tblConceptCount = createTable(rowDataConcept, columnConcept);
		jScrollConcepts.setViewportView(tblConceptCount);

		/* Create table list for empty roles */
		List<Predicate> emptyR = check.getEmptyRoles();

		final int rowRoles = emptyR.size();

		final String[] columnsRole = { "Roles" };

		Object[][] rowDataRole = new Object[rowRoles][col];

		index = 0;

		for (Predicate role : emptyR) {

			rowDataRole[index][0] = role.getName();
			index++;
		}
		JTable tblRoleCount = createTable(rowDataRole, columnsRole);

		jScrollRoles.setViewportView(tblRoleCount);
	}

	private JTable createTable(Object[][] rowData, String[] columnNames) {

		DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

		@SuppressWarnings("serial")
		JTable table = new JTable(model) {
			// Create a model in which the cells can't be edited
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		return table;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		pnlSummary = new javax.swing.JPanel();
		lblSummary = new javax.swing.JLabel();
		lblSummaryValue = new javax.swing.JLabel();
		pnlEmptiesSummary = new javax.swing.JPanel();
		jScrollConcepts = new javax.swing.JScrollPane();
		jScrollRoles = new javax.swing.JScrollPane();

		setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
		setMinimumSize(new java.awt.Dimension(520, 400));
		setPreferredSize(new java.awt.Dimension(520, 400));
		setLayout(new java.awt.BorderLayout());

		pnlSummary.setMinimumSize(new java.awt.Dimension(156, 23));
		pnlSummary.setPreferredSize(new java.awt.Dimension(156, 23));
		pnlSummary.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		lblSummary.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		lblSummary.setText("Empty concepts and roles");
		pnlSummary.add(lblSummary);

		lblSummaryValue.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
		pnlSummary.add(lblSummaryValue);

		add(pnlSummary, java.awt.BorderLayout.NORTH);

		pnlEmptiesSummary.setLayout(new javax.swing.BoxLayout(pnlEmptiesSummary, javax.swing.BoxLayout.PAGE_AXIS));
		pnlEmptiesSummary.add(jScrollConcepts);

		jScrollRoles.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		pnlEmptiesSummary.add(jScrollRoles);

		add(pnlEmptiesSummary, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane jScrollConcepts;
	private javax.swing.JScrollPane jScrollRoles;
	private javax.swing.JLabel lblSummary;
	private javax.swing.JLabel lblSummaryValue;
	private javax.swing.JPanel pnlEmptiesSummary;
	private javax.swing.JPanel pnlSummary;
	// End of variables declaration//GEN-END:variables
}

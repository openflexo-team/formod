/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.ta.b.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.selection.SelectionListener;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.rm.BResource;
import org.openflexo.view.SelectionSynchronizedModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This class represent the module view for a {@link BComponent}<br>
 * 
 * @author sylvain
 * 
 */
@SuppressWarnings("serial")
public class BComponentModuleView extends JPanel implements SelectionSynchronizedModuleView<BComponent>, PropertyChangeListener {

	private final BComponent component;
	private final FlexoPerspective declaredPerspective;

	private final FlexoController controller;
	private RSyntaxTextArea sourceTextArea;
	private RSyntaxTextArea xmlTextArea;

	static {
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/b", "org.openflexo.ta.b.view.BTokenMaker");
	}

	public BComponentModuleView(BComponent component, FlexoController controller, FlexoPerspective perspective) {
		super(new BorderLayout());
		this.controller = controller;
		declaredPerspective = perspective;
		this.component = component;

		JTabbedPane tabbedPane = new JTabbedPane();

		sourceTextArea = new RSyntaxTextArea(component.getResource().getRawSource());
		sourceTextArea.setSyntaxEditingStyle("text/b");
		sourceTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					component.getResource().updateWithRawSource(sourceTextArea.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					component.getResource().updateWithRawSource(sourceTextArea.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
					component.getResource().updateWithRawSource(sourceTextArea.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		component.getResource().getPropertyChangeSupport().addPropertyChangeListener(this);

		tabbedPane.add("Source", new JScrollPane(sourceTextArea));

		String XML = component.getResource().getFactory().stringRepresentation(component);
		xmlTextArea = new RSyntaxTextArea(XML);
		xmlTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		tabbedPane.add("XML", new JScrollPane(xmlTextArea));

		add(tabbedPane, BorderLayout.CENTER);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(BResource.RAW_SOURCE_KEY)) {
			System.out.println("Je detecte que le source a change !");
			sourceTextArea.setText(component.getResource().getRawSource());
		}
	}

	public FlexoController getFlexoController() {
		return controller;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return declaredPerspective;
	}

	@Override
	public void deleteModuleView() {
		if (getFlexoController() != null) {
			controller.removeModuleView(this);
		}
	}

	@Override
	public List<SelectionListener> getSelectionListeners() {
		Vector<SelectionListener> reply = new Vector<>();
		reply.add(this);
		return reply;
	}

	@Override
	public void willHide() {
	}

	@Override
	public void willShow() {
	}

	@Override
	public void show(FlexoController controller, FlexoPerspective perspective) {
	}

	@Override
	public BComponent getRepresentedObject() {
		return component;
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void fireObjectSelected(FlexoObject object) {

	}

	@Override
	public void fireObjectDeselected(FlexoObject object) {

	}

	@Override
	public void fireResetSelection() {

	}

	@Override
	public void fireBeginMultipleSelection() {

	}

	@Override
	public void fireEndMultipleSelection() {

	}

}

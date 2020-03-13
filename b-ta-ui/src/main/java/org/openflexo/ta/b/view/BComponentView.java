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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.rm.BResource;
import org.openflexo.view.controller.FlexoController;

/**
 * This class represent a view for a {@link BComponent}<br>
 * 
 * @author sylvain
 * 
 */
@SuppressWarnings("serial")
public class BComponentView extends JPanel implements PropertyChangeListener {

	private final BComponent component;

	private final FlexoController controller;
	private RSyntaxTextArea sourceTextArea;

	static {
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/b", "org.openflexo.ta.b.view.BTokenMaker");
	}

	public BComponentView(BComponent component, FlexoController controller) {
		super(new BorderLayout());
		this.controller = controller;
		this.component = component;

		sourceTextArea = new RSyntaxTextArea(component.getResource().getRawSource());
		sourceTextArea.setSyntaxEditingStyle("text/b");
		sourceTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!isProgrammaticallyUpdating) {
					isBeeingParsing = true;
					try {
						component.getResource().updateWithRawSource(sourceTextArea.getText());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					isBeeingParsing = false;
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!isProgrammaticallyUpdating) {
					isBeeingParsing = true;
					try {
						component.getResource().updateWithRawSource(sourceTextArea.getText());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					isBeeingParsing = false;
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!isProgrammaticallyUpdating) {
					isBeeingParsing = true;
					try {
						component.getResource().updateWithRawSource(sourceTextArea.getText());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					isBeeingParsing = false;
				}
			}
		});

		component.getResource().getPropertyChangeSupport().addPropertyChangeListener(this);
		component.getPropertyChangeSupport().addPropertyChangeListener(this);

		add(new JScrollPane(sourceTextArea), BorderLayout.CENTER);
	}

	private boolean isProgrammaticallyUpdating = false;
	private boolean isBeeingParsing = false;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (isBeeingParsing) {
			return;
		}
		if (evt.getPropertyName().equals(BResource.RAW_SOURCE_KEY)) {
			System.out.println("Je detecte que le source a change !");
			isProgrammaticallyUpdating = true;
			sourceTextArea.setText(component.getResource().getRawSource());
			isProgrammaticallyUpdating = false;
		}
		else {
			System.out.println("Je detecte " + evt + " from " + evt.getSource());
			isProgrammaticallyUpdating = true;
			sourceTextArea.setText(component.getResource().getRawSource());
			isProgrammaticallyUpdating = false;
		}
	}

	public FlexoController getFlexoController() {
		return controller;
	}

}

/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.module.formose.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.openflexo.components.doc.editorkit.FlexoDocumentEditorWidget;
import org.openflexo.components.doc.editorkit.widget.FIBFlexoDocumentBrowser;
import org.openflexo.foundation.doc.FlexoDocObject;
import org.openflexo.foundation.doc.nature.FMLControlledDocumentVirtualModelInstanceNature;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.ModuleLoadingException;
import org.openflexo.module.formose.FMSModule;
import org.openflexo.module.formose.controller.DocumentAnnotationPerspective;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.model.action.IdentifyElement;
import org.openflexo.module.formose.model.action.IdentifyRequirement;
import org.openflexo.module.formose.model.action.IdentifyTextFragment;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.controller.DocXAdapterController;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXObject;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * A {@link ModuleView} for the requirement virtual model instance It is stated that the related {@link FMLRTVirtualModelInstance} has the
 * {@link FMLControlledDocumentVirtualModelInstanceNature}
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class DocXRequirementModuleView extends JPanel implements ModuleView<FMLRTVirtualModelInstance>, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DocXRequirementModuleView.class.getPackage().getName());

	private final FMLRTVirtualModelInstance redIdVMI;
	private final DocumentAnnotationPerspective perspective;

	private final FlexoDocumentEditorWidget<DocXDocument, DocXTechnologyAdapter> docxEditor;

	private final FIBFlexoDocumentBrowser browser;
	private final JPanel topPanel;

	private final JButton identifyFragmentButton;
	private final JButton identifyRequirementButton;
	private final JButton identifyElementButton;

	public DocXRequirementModuleView(FMLRTVirtualModelInstance redIdVMI, DocumentAnnotationPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		this.redIdVMI = redIdVMI;
		this.perspective = perspective;

		if (!redIdVMI.hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE)) {
			logger.severe("Supplied FMLRTVirtualModelInstance does not have the FMLControlledDocXVirtualModelInstanceNature");
		}

		docxEditor = new FlexoDocumentEditorWidget<>(getDocument());
		docxEditor.setShowToolbar(false);
		docxEditor.getEditor().getPropertyChangeSupport().addPropertyChangeListener(this);

		// Because we automatically select some text from browsers, we should also
		// reset that selection
		/*docxEditor.getEditorView().addCaretListener(new CaretListener() {
		
			@Override
			public void caretUpdate(CaretEvent e) {
				selectElementInDocumentEditor(null);
			}
		});*/

		add(docxEditor, BorderLayout.CENTER);

		perspective.getReferencesBrowser().getFIBController().getPropertyChangeSupport().addPropertyChangeListener(this);

		browser = new FIBFlexoDocumentBrowser(getDocument(), perspective.getController()) {
			@Override
			public void setSelectedDocumentElement(FlexoDocObject<?, ?> selected) {
				super.setSelectedDocumentElement(selected);
				if (selected instanceof DocXObject) {
					selectElementInDocumentEditor((DocXObject<?>) selected);
				}
			}
		};
		// add(browser, BorderLayout.EAST);

		topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		LocalizedDelegate locales = FlexoLocalization.getMainLocalizer();

		try {
			locales = getPerspective().getController().getApplicationContext().getModuleLoader().getModuleInstance(FMSModule.class)
					.getLocales();
		} catch (ModuleLoadingException e) {
			e.printStackTrace();
		}

		identifyFragmentButton = new JButton(locales.localizedForKey(IdentifyTextFragment.ACTION_TYPE.getActionName()));
		identifyFragmentButton.setIcon(getPerspective().getController().getEditor().getEnabledIconFor(IdentifyTextFragment.ACTION_TYPE));
		identifyFragmentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (docxEditor.getTextSelection() != null) {
					IdentifyTextFragment action = IdentifyTextFragment.ACTION_TYPE.makeNewAction(getFormoseProjectNature(), null,
							getPerspective().getController().getEditor());
					action.setTextSelection(docxEditor.getTextSelection());
					action.doAction();
				}
			}
		});
		topPanel.add(identifyFragmentButton);

		identifyRequirementButton = new JButton(locales.localizedForKey(IdentifyRequirement.ACTION_TYPE.getActionName()));
		identifyRequirementButton.setIcon(getPerspective().getController().getEditor().getEnabledIconFor(IdentifyRequirement.ACTION_TYPE));
		identifyRequirementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IdentifyRequirement action = IdentifyRequirement.ACTION_TYPE.makeNewAction(getFormoseProjectNature(), null,
						getPerspective().getController().getEditor());
				action.doAction();
			}
		});
		topPanel.add(identifyRequirementButton);

		identifyElementButton = new JButton(locales.localizedForKey(IdentifyElement.ACTION_TYPE.getActionName()));
		identifyElementButton.setIcon(getPerspective().getController().getEditor().getEnabledIconFor(IdentifyElement.ACTION_TYPE));
		identifyElementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IdentifyElement action = IdentifyElement.ACTION_TYPE.makeNewAction(getFormoseProjectNature(), null,
						getPerspective().getController().getEditor());
				action.doAction();
			}
		});
		topPanel.add(identifyElementButton);

		updateButtonStates();

		add(topPanel, BorderLayout.NORTH);

		validate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	private void updateButtonStates() {
		if (getDocXEditor().getEditor().getTextSelection() == null) {
			identifyFragmentButton.setEnabled(false);
			identifyRequirementButton.setEnabled(false);
			identifyElementButton.setEnabled(false);
		}
		else {
			identifyRequirementButton.setEnabled(perspective.getReferencesBrowser().getFIBController().getFocusedElement() != null);
			identifyElementButton.setEnabled(perspective.getReferencesBrowser().getFIBController().getFocusedElement() != null);
			identifyFragmentButton.setEnabled(true);
		}
	}

	public FormoseProjectNature getFormoseProjectNature() {
		return getPerspective().getController().getFormoseNature();
	}

	public FMLRTVirtualModelInstance getVirtualModelInstance() {
		return redIdVMI;
	}

	public DocXDocument getDocument() {
		return FMLControlledDocXVirtualModelInstanceNature.getDocument(getVirtualModelInstance());
	}

	@Override
	public DocumentAnnotationPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		perspective.getController().removeModuleView(this);
		// TODO: delete docx editor
	}

	@Override
	public FMLRTVirtualModelInstance getRepresentedObject() {
		return getVirtualModelInstance();
	}

	public FlexoDocumentEditorWidget<DocXDocument, DocXTechnologyAdapter> getDocXEditor() {
		return docxEditor;
	}

	public FIBFlexoDocumentBrowser getBrowser() {
		return browser;
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {
		getPerspective().setBottomLeftView(null);
		perspective.hideReferencesBrowser();
	}

	@Override
	public void willShow() {
		perspective.setBottomLeftView(browser);
		getPerspective().getController().getControllerModel().setLeftViewVisible(true);

		perspective.showReferencesBrowser();

		getPerspective().focusOnObject(getRepresentedObject());
	}

	public DocXAdapterController getDocXAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DocXAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		/*perspective.setTopRightView(null);
		perspective.setBottomRightView(null);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(false);
			}
		});
		
		controller.getControllerModel().setRightViewVisible(false);*/

		/*perspective.setBottomLeftView(browser);
		controller.getControllerModel().setLeftViewVisible(true);*/

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
		if (evt.getSource() == getPerspective().getReferencesBrowser().getFIBController()) {
			updateButtonStates();
		}
		if (evt.getSource() == docxEditor.getEditor()) {
			updateButtonStates();
		}
	}

	private void scrollTo(DocXObject<?> object) {
		if (!docxEditor.getEditor().scrollTo(object, false)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					scrollTo(object);
				}
			});
		}
	}

	protected void selectReferenceInDocumentEditor(FlexoConceptInstance reference) {

		System.out.println("Selection de la reference " + reference);

		/*DocXFragment fragment;
		
		System.out.println("****************** selectFragmentInDocumentEditor with " + fragment.getStringRepresentationPreview());
		
		try {
		
			if (fragment != null) {
				docxEditor.getEditor().highlightObjects(fragment.getElements());
				scrollTo((DocXObject<?>) fragment.getStartElement());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		docxEditor.getJEditorPane().revalidate();
		docxEditor.getJEditorPane().repaint();*/
	}

	protected void selectElementInDocumentEditor(DocXObject<?> element) {

		System.out.println("****************** selectElementInDocumentEditor with " + element);

		docxEditor.getEditor().highlight(element);
		scrollTo(element);

		docxEditor.getJEditorPane().revalidate();
		docxEditor.getJEditorPane().repaint();

	}
}

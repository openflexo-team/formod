/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Formose prototype, a component of the software infrastructure 
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

package org.openflexo.module.formose.controller;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.FlexoModule.WelcomePanel;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FMSIconLibrary;
import org.openflexo.module.formose.FMSModule;
import org.openflexo.module.formose.controller.action.FMSControllerActionInitializer;
import org.openflexo.module.formose.menu.FMSMenuBar;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.view.ConvertToFormoseProjectView;
import org.openflexo.module.formose.view.FormoseWelcomePanelModuleView;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.ta.b.gui.BIconLibrary;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.controller.DocXAdapterController;
import org.openflexo.technologyadapter.docx.gui.DocXIconLibrary;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.technologyadapter.owl.gui.OWLIconLibrary;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Formose-module specific FlexoController.
 * 
 * @author sylvain
 */
public class FMSController extends FlexoController {

	private FormosePerspective formosePerspective;
	private DocumentAnnotationPerspective documentAnnotationPerspective;
	private DomainModelPerspective domainModelPerspective;
	private SysMLKaosPerspective sysMLKaosPerspective;
	private BPerspective bPerspective;

	private DiagramTechnologyAdapterController diagramTAC = null;
	private DocXAdapterController docXTAC = null;

	public FMSController(FMSModule module) {
		super(module);
	}

	public FormoseProjectNature getFormoseNature() {
		if (getProject() != null) {
			return getProject().getNature(FormoseProjectNature.class);
		}
		return null;
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject<?> project) {
		if (project != null && project.hasNature(FormoseProjectNature.class)) {
			return project.getNature(FormoseProjectNature.class);
		}
		return project;
	}

	@Override
	protected void initializePerspectives() {
		this.addToPerspectives(formosePerspective = new MainPerspective(this));
		this.addToPerspectives(documentAnnotationPerspective = new DocumentAnnotationPerspective(this));
		this.addToPerspectives(sysMLKaosPerspective = new SysMLKaosPerspective(this));
		this.addToPerspectives(domainModelPerspective = new DomainModelPerspective(this));
		this.addToPerspectives(bPerspective = new BPerspective(this));
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new FMSSelectionManager(this);
	}

	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new FMSMenuBar(this);
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new FlexoMainPane(this);
	}

	@Override
	protected void updateEditor(final FlexoEditor from, final FlexoEditor to) {
		super.updateEditor(from, to);
		FlexoProject<?> project = (to != null ? to.getProject() : null);
		if (formosePerspective != null) {
			formosePerspective.setProject(project);
		}
		if (documentAnnotationPerspective != null) {
			documentAnnotationPerspective.setProject(project);
		}
		if (domainModelPerspective != null) {
			domainModelPerspective.setProject(project);
		}
		if (sysMLKaosPerspective != null) {
			sysMLKaosPerspective.setProject(project);
		}
	}

	@Override
	public ImageIcon iconForObject(final Object object) {
		if (object instanceof FormoseProjectNature) {
			return FMSIconLibrary.FMS_SMALL_ICON;
		}

		if (getFormoseNature() != null) {
			if (object == getFormoseNature().getDocumentLibrary()) {
				return FMSIconLibrary.DOC_LIBRARY_ICON;
			}
			if (object == getFormoseNature().getFormoseInstance().getAccessedVirtualModelInstance()) {
				return FMSIconLibrary.FMS_SMALL_ICON;
			}
		}

		if (object instanceof FMLRTVirtualModelInstance) {
			VirtualModel type = ((FMLRTVirtualModelInstance) object).getVirtualModel();
			if (type != null) {
				if (type.getName().equals(FMSConstants.DOC_ANNOTATION_METHODOLOGY_VM_NAME)) {
					return FMSIconLibrary.DOC_LIBRARY_ICON;
				}
				else if (type.getName().equals(FMSConstants.FORMOSE_VM_NAME)) {
					return FMSIconLibrary.FMS_SMALL_ICON;
				}
				else if (type.getName().equals(FMSConstants.DOMAIN_MODEL_METHODOLOGY_VM_NAME)) {
					return OWLIconLibrary.ONTOLOGY_ICON;
				}
				else if (type.getName().equals(FMSConstants.DOMAIN_MODEL_VM_NAME)) {
					return OWLIconLibrary.ONTOLOGY_ICON;
				}
				else if (type.getName().equals(FMSConstants.SYSML_KAOS_METHODOLOGY_VM_NAME)) {
					return FMSIconLibrary.SYSML_ICON;
				}
				// FML-controlled document
				else if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE)) {
					return DocXIconLibrary.DOCX_FILE_ICON;
				}
			}

			return super.iconForObject(object);
		}

		if (object instanceof FlexoConceptInstance) {
			FlexoConcept type = ((FlexoConceptInstance) object).getFlexoConcept();
			if (type != null) {
				if (type.getName().equals(FMSConstants.ELEMENT_CONCEPT_NAME)) {
					return FMSIconLibrary.ELEMENT_ICON;
				}
				else if (type.getName().equals(FMSConstants.REQUIREMENT_CONCEPT_NAME)) {
					return FMSIconLibrary.REQUIREMENT_ICON;
				}
				else if (type.getName().equals(FMSConstants.REFERENCE_CONCEPT_NAME)) {
					return DocXIconLibrary.FRAGMENT_ICON;
				}
				else if (type.getName().equals("DocXReference")) {
					return DocXIconLibrary.FRAGMENT_ICON;
				}
				else if (type.getName().equals(FMSConstants.UNCLASSIFIED_CONCEPT_NAME)) {
					return IconLibrary.FOLDER_ICON;
				}
				else if (type.getName().equals(FMSConstants.SYSML_KAOS_ELEMENT_MAPPING_CONCEPT_NAME)) {
					return FMSIconLibrary.ELEMENT_ICON;
				}
				else if (type.getName().equals(FMSConstants.DIAGRAM_MAPPING_CONCEPT_NAME)) {
					return DiagramIconLibrary.DIAGRAM_ICON;
				}
				else if (type.getName().equals(FMSConstants.DOMAIN_MODELLING_ELEMENT_MAPPING_CONCEPT_NAME)) {
					return FMSIconLibrary.ELEMENT_ICON;
				}
				else if (type.getName().equals(FMSConstants.DOMAIN_MODEL_MAPPING_CONCEPT_NAME)) {
					return OWLIconLibrary.ONTOLOGY_ICON;
				}
				else if (type.getName().equals(FMSConstants.B_ELEMENT_MAPPING_CONCEPT_NAME)) {
					return FMSIconLibrary.ELEMENT_ICON;
				}
				else if (type.getName().equals(FMSConstants.B_MAPPING_CONCEPT_NAME)) {
					return BIconLibrary.B_TA_ICON;
				}
			}
		}
		return super.iconForObject(object);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new FMSControllerActionInitializer(this);
	}

	/**
	 * Helper functio to ease access to DiagramTAController
	 * 
	 * @return
	 */
	public DiagramTechnologyAdapterController getDiagramTAC() {
		if (diagramTAC == null) {
			DiagramTechnologyAdapter diagramTA = this.getTechnologyAdapter(DiagramTechnologyAdapter.class);
			diagramTAC = (DiagramTechnologyAdapterController) getTechnologyAdapterController(diagramTA);
		}
		return this.diagramTAC;
	}

	/**
	 * Helper functio to ease access to DiagramTAController
	 * 
	 * @return
	 */
	public DocXAdapterController getDocXTAC() {
		if (docXTAC == null) {
			DocXTechnologyAdapter docXTA = this.getTechnologyAdapter(DocXTechnologyAdapter.class);
			docXTAC = (DocXAdapterController) getTechnologyAdapterController(docXTA);
		}
		return docXTAC;
	}

	@Override
	public void willExecute(FlexoBehaviourAction<?, ?, ?> action) {
		super.willExecute(action);
	}

	@Override
	public void hasExecuted(FlexoBehaviourAction<?, ?, ?> action) {
		super.hasExecuted(action);

		// System.out.println("On vient d'executer " + action);
		if (action.getFlexoBehaviour() instanceof ActionScheme) {
			if (action.getFlexoBehaviour().getName().equals("refine")) {
				action.getFlexoConceptInstance().getPropertyChangeSupport().firePropertyChange("childrenElements", false, true);
				formosePerspective.getProjectBrowser().getFIBController().getPropertyChangeSupport()
						.firePropertyChange("getChildren(FlexoConceptInstance)", false, true);
			}
			else if (action.getFlexoBehaviour().getName().equals("createRequirement")) {
				action.getFlexoConceptInstance().getPropertyChangeSupport().firePropertyChange("goals", false, true);
				formosePerspective.getProjectBrowser().getFIBController().getPropertyChangeSupport()
						.firePropertyChange("getChildren(FlexoConceptInstance)", false, true);
			}
			else if (action.getFlexoBehaviour().getName().equals("createChildrenElement")) {
				action.getFlexoConceptInstance().getPropertyChangeSupport().firePropertyChange("childrenElements", false, true);
				formosePerspective.getProjectBrowser().getFIBController().getPropertyChangeSupport()
						.firePropertyChange("getChildren(FlexoConceptInstance)", false, true);
			}
		}
	}

	/*@Override
	public void objectWasDoubleClicked(Object object) {
		if (object instanceof FlexoConceptInstance) {
			FlexoConceptInstance fci = (FlexoConceptInstance) object;
			if (fci.getFlexoConcept() == getFormoseProject().getElementConcept()) {
				System.out.println("Double-clicking on element");
				NavigationScheme navigationScheme = getFormoseProject().getElementConcept().getNavigationSchemes().get(0);
				NavigationSchemeActionFactory actionType = new NavigationSchemeActionFactory(navigationScheme, fci);
				NavigationSchemeAction navigationSchemeAction = actionType.makeNewAction(fci.getVirtualModelInstance(), null, getEditor());
				navigationSchemeAction.doAction();
			}
		}
		super.objectWasDoubleClicked(object);
	}*/

	public FormosePerspective getFormosePerspective() {
		return formosePerspective;
	}

	public DocumentAnnotationPerspective getDocumentAnnotationPerspective() {
		return documentAnnotationPerspective;
	}

	public SysMLKaosPerspective getSysMLKaosPerspective() {
		return sysMLKaosPerspective;
	}

	public DomainModelPerspective getDomainModelPerspective() {
		return domainModelPerspective;
	}

	public BPerspective getBPerspective() {
		return bPerspective;
	}

	/**
	 * Return ElementReference instance matching supplied Element instance
	 * 
	 * @param element
	 *            an instance of of Element concept
	 * @return an instance of ElementReference concept
	 */
	public FlexoConceptInstance getElementReference(FlexoConceptInstance element) {

		VirtualModel docAnnotationVM = getFormoseNature().getDocAnnotationMethodologyVirtualModel();

		ActionScheme getElementReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("getElementReference",
				getFormoseNature().getElementConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(getElementReference,
				getFormoseNature().getDocAnnotationMethodologyVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewAction(getFormoseNature().getDocAnnotationMethodologyVirtualModelInstance(), null,
				getEditor());
		action.setParameterValue(getElementReference.getParameter("element"), element);
		action.doAction();

		return (FlexoConceptInstance) action.getReturnedValue();

	}

	/**
	 * Return RequirementReference instance matching supplied Requirement instance
	 * 
	 * @param element
	 *            an instance of of Requirement concept
	 * @return an instance of RequirementReference concept
	 */
	public FlexoConceptInstance getRequirementReference(FlexoConceptInstance requirement) {

		VirtualModel docAnnotationVM = getFormoseNature().getDocAnnotationMethodologyVirtualModel();

		ActionScheme getRequirementReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("getRequirementReference",
				getFormoseNature().getRequirementConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(getRequirementReference,
				getFormoseNature().getDocAnnotationMethodologyVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewAction(getFormoseNature().getDocAnnotationMethodologyVirtualModelInstance(), null,
				getEditor());
		action.setParameterValue(getRequirementReference.getParameter("requirement"), requirement);
		action.doAction();

		return (FlexoConceptInstance) action.getReturnedValue();

	}

	@Override
	public ModuleView<?> makeWelcomePanel(WelcomePanel<?> welcomePanel, FlexoPerspective perspective) {
		return new FormoseWelcomePanelModuleView((WelcomePanel<FMSModule>) welcomePanel, this, perspective);
	}

	@Override
	public ModuleView<?> makeDefaultProjectView(FlexoProject<?> project, FlexoPerspective perspective) {
		return new ConvertToFormoseProjectView(project, this, perspective);
	}

}

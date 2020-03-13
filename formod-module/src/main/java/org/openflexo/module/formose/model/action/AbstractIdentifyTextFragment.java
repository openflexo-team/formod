/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.module.formose.model.action;

import java.util.Vector;

import org.openflexo.ApplicationContext;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.foundation.fml.rt.action.CreationSchemeAction;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.formose.FormoseEditor;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

/**
 * @author sylvain
 */
public abstract class AbstractIdentifyTextFragment<A extends FlexoAction<A, FormoseProjectNature, FlexoObject>>
		extends FMSAction<A, FormoseProjectNature, FlexoObject> {

	private TextSelection<DocXDocument, DocXTechnologyAdapter> textSelection;

	public AbstractIdentifyTextFragment(FlexoActionFactory<A, FormoseProjectNature, FlexoObject> actionType,
			final FormoseProjectNature focusedObject, final Vector<FlexoObject> globalSelection, final FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(FormoseEditor.class).getLoadedModuleInstance()
					.getLocales();
		}
		return super.getLocales();
	}

	private FMLRTVirtualModelInstance fmlControlledDocumentVMI;

	public FMLRTVirtualModelInstance getFMLControlledDocumentVMI() {
		return fmlControlledDocumentVMI;
	}

	public void setFMLControlledDocumentVMI(FMLRTVirtualModelInstance fMLControlledDocumentVMI) {
		if ((fMLControlledDocumentVMI == null && this.fmlControlledDocumentVMI != null)
				|| (fMLControlledDocumentVMI != null && !fMLControlledDocumentVMI.equals(this.fmlControlledDocumentVMI))) {
			FMLRTVirtualModelInstance oldValue = this.fmlControlledDocumentVMI;
			this.fmlControlledDocumentVMI = fMLControlledDocumentVMI;
			getPropertyChangeSupport().firePropertyChange("fmlControlledDocumentVMI", oldValue, fMLControlledDocumentVMI);
		}
	}

	public TextSelection<DocXDocument, DocXTechnologyAdapter> getTextSelection() {
		return textSelection;
	}

	public void setTextSelection(TextSelection<DocXDocument, DocXTechnologyAdapter> textSelection) {
		if ((textSelection == null && this.textSelection != null) || (textSelection != null && !textSelection.equals(this.textSelection))) {
			TextSelection<DocXDocument, DocXTechnologyAdapter> oldValue = this.textSelection;
			this.textSelection = textSelection;
			getPropertyChangeSupport().firePropertyChange("textSelection", oldValue, textSelection);
		}
	}

	protected FlexoConceptInstance makeDocXReference() {
		// System.out.println("On identifie un fragment avec la text selection: " + getTextSelection());

		FlexoConcept docXReferenceConcept = getFocusedObject().getDocumentLibraryVirtualModel().getVirtualModelNamed("DocXDocument")
				.getFlexoConcept("DocXReference");

		// System.out.println("docXReferenceConcept=" + docXReferenceConcept);

		CreationScheme creationScheme = docXReferenceConcept.getFlexoBehaviours(CreationScheme.class).get(0);
		CreationSchemeAction creationSchemeAction = new CreationSchemeAction(creationScheme, getFMLControlledDocumentVMI(), null, this);
		creationSchemeAction.setParameterValue(creationScheme.getParameter("textSelection"), getTextSelection());
		creationSchemeAction.doAction();

		FlexoConceptInstance docXReference = creationSchemeAction.getFlexoConceptInstance();
		// System.out.println("docXReference=" + docXReference);

		return docXReference;
	}

	protected FlexoConceptInstance getElementReference(FlexoConceptInstance element) {

		VirtualModel docAnnotationVM = getFocusedObject().getDocAnnotationMethodologyVirtualModel();

		ActionScheme getElementReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("getElementReference",
				getFocusedObject().getElementConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(getElementReference,
				getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance(),
				null, this);
		action.setParameterValue(getElementReference.getParameter("element"), element);
		action.doAction();

		return (FlexoConceptInstance) action.getReturnedValue();

	}

	protected FlexoConceptInstance makeElementReference(FlexoConceptInstance element) {

		VirtualModel docAnnotationVM = getFocusedObject().getDocAnnotationMethodologyVirtualModel();

		FlexoConcept elementReferenceConcept = docAnnotationVM.getFlexoConcept("ElementReference");

		// System.out.println("elementReferenceConcept=" + elementReferenceConcept);

		CreationScheme creationScheme = (CreationScheme) elementReferenceConcept.getDeclaredFlexoBehaviour("createNewEmptyElementReference",
				getFocusedObject().getElementConcept().getInstanceType());
		CreationSchemeAction creationSchemeAction = new CreationSchemeAction(creationScheme,
				getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance(), null, this);
		creationSchemeAction.setParameterValue(creationScheme.getParameter("anElement"), element);
		creationSchemeAction.doAction();

		return creationSchemeAction.getFlexoConceptInstance();

	}

	protected FlexoConceptInstance getRequirementReference(FlexoConceptInstance requirement) {

		VirtualModel docAnnotationVM = getFocusedObject().getDocAnnotationMethodologyVirtualModel();

		ActionScheme getRequirementReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("getRequirementReference",
				getFocusedObject().getRequirementConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(getRequirementReference,
				getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance(),
				null, this);
		action.setParameterValue(getRequirementReference.getParameter("requirement"), requirement);
		action.doAction();

		return (FlexoConceptInstance) action.getReturnedValue();

	}

	protected FlexoConceptInstance makeRequirementReference(FlexoConceptInstance requirement) {

		VirtualModel docAnnotationVM = getFocusedObject().getDocAnnotationMethodologyVirtualModel();

		ActionScheme getRequirementReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("makeRequirementReference",
				getFocusedObject().getRequirementConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(getRequirementReference,
				getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance(),
				null, this);
		action.setParameterValue(getRequirementReference.getParameter("requirement"), requirement);
		action.doAction();

		return (FlexoConceptInstance) action.getReturnedValue();

	}

}

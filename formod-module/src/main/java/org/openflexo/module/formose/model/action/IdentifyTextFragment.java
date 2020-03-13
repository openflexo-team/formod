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

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.module.formose.model.FormoseProjectNature;

/**
 * @author sylvain
 */
public class IdentifyTextFragment extends AbstractIdentifyTextFragment<IdentifyTextFragment> {

	public static final FlexoActionFactory<IdentifyTextFragment, FormoseProjectNature, FlexoObject> ACTION_TYPE = new FlexoActionFactory<IdentifyTextFragment, FormoseProjectNature, FlexoObject>(
			"identify_text_fragment", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		@Override
		public IdentifyTextFragment makeNewAction(final FormoseProjectNature focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new IdentifyTextFragment(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FormoseProjectNature fragment, final Vector<FlexoObject> globalSelection) {
			return fragment != null;
		}

		@Override
		public boolean isEnabledForSelection(final FormoseProjectNature project, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(project, globalSelection);
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FormoseProjectNature.class);
	}

	private FlexoConceptInstance focusedRequirement;
	private FlexoConceptInstance focusedElement;

	private FlexoConceptInstance newReference;

	public IdentifyTextFragment(final FormoseProjectNature focusedObject, final Vector<FlexoObject> globalSelection,
			final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(final Object context) throws FlexoException {

		FlexoConceptInstance docXReference = makeDocXReference();

		FlexoConceptInstance focusedNamedReference = null;

		if (getFocusedRequirement() != null) {
			System.out.println("OK, j'ai comme requirement focus: " + getFocusedRequirement());
			focusedNamedReference = getRequirementReference(getFocusedRequirement());
			System.out.println("La reference: " + focusedNamedReference);
			if (focusedNamedReference == null) {
				System.out.println("C'est pas reference, je le fais maintenant");
				focusedNamedReference = makeRequirementReference(getFocusedRequirement());
				System.out.println("Du coup je travaille avec: " + focusedNamedReference);
			}
		}
		else if (getFocusedElement() != null) {
			System.out.println("OK, j'ai comme element focus: " + getFocusedElement());
			focusedNamedReference = getElementReference(getFocusedElement());
			System.out.println("La reference: " + focusedNamedReference);
			if (focusedNamedReference == null) {
				System.out.println("C'est pas reference, je le fais maintenant");
				focusedNamedReference = makeElementReference(getFocusedElement());
				System.out.println("Du coup je travaille avec: " + focusedNamedReference);
			}
		}

		if (focusedNamedReference != null) {

			VirtualModel docAnnotationVM = getFocusedObject().getDocAnnotationMethodologyVirtualModel();
			FlexoConcept namedReference = docAnnotationVM.getFlexoConcept("NamedReference");

			ActionScheme addReference = (ActionScheme) namedReference.getDeclaredFlexoBehaviour("addReference",
					getFocusedObject().getReferenceConcept().getInstanceType());

			ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(addReference, focusedNamedReference);
			ActionSchemeAction action = actionType.makeNewEmbeddedAction(focusedNamedReference, null, this);
			action.setParameterValue(addReference.getParameter("aReference"), docXReference);
			System.out.println("On execute sur " + focusedNamedReference + ".addReference(aReference) avec " + docXReference);
			action.doAction();

			newReference = (FlexoConceptInstance) action.getReturnedValue();

		}

		else {
			// Unclassified reference

			VirtualModel docAnnotationVM = getFocusedObject().getDocAnnotationMethodologyVirtualModel();

			ActionScheme addUnclassifiedReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("addUnclassifiedReference",
					getFocusedObject().getReferenceConcept().getInstanceType());

			ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(addUnclassifiedReference,
					getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance());
			ActionSchemeAction action = actionType
					.makeNewEmbeddedAction(getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance(), null, this);
			action.setParameterValue(addUnclassifiedReference.getParameter("reference"), docXReference);
			action.doAction();

			newReference = (FlexoConceptInstance) action.getReturnedValue();

		}

		System.out.println("newReference=" + newReference);

		// getParentElement().getPropertyChangeSupport().firePropertyChange("childrenElements", null, newIdentifiedElement);

		// getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance().getPropertyChangeSupport()
		// .firePropertyChange("getElementReference(Element)", false, true);

		/*FlexoConcept identifiedFragmentConcept = getFormoseProject().getIdentifiedFragmentConcept();
		
		CreationScheme creationScheme = identifiedFragmentConcept.getFlexoBehaviours(CreationScheme.class).get(0);
		CreationSchemeAction creationSchemeAction = CreationSchemeAction.actionType
				.makeNewEmbeddedAction(getFormoseProject().getRequirementDocumentVirtualModelInstance(), null, this);
		creationSchemeAction.setCreationScheme(creationScheme);
		creationSchemeAction.setParameterValue(creationScheme.getParameter("fragment"), getFocusedObject());
		creationSchemeAction.doAction();
		
		newReference = creationSchemeAction.getFlexoConceptInstance();
		
		if (focusedRequirement != null) {
			focusedRequirement.addToFlexoActors(newReference, (FlexoConceptInstanceRole) getFormoseProject()
					.getIdentifiedRequirementConcept().getAccessibleRole(FMSConstants.IDENTIFIED_FRAGMENTS_ROLE_NAME));
			newReference.setFlexoActor(focusedRequirement, (FlexoConceptInstanceRole) getFormoseProject()
					.getIdentifiedFragmentConcept().getAccessibleRole(FMSConstants.PARENT_IDENTIFIED_REQUIREMENT_NAME));
		
		}*/
	}

	public FlexoConceptInstance getFocusedRequirement() {
		return focusedRequirement;
	}

	public void setFocusedRequirement(FlexoConceptInstance focusedRequirement) {
		this.focusedRequirement = focusedRequirement;
	}

	public FlexoConceptInstance getFocusedElement() {
		return focusedElement;
	}

	public void setFocusedElement(FlexoConceptInstance focusedElement) {
		this.focusedElement = focusedElement;
	}

	public FlexoConceptInstance getNewReference() {
		return newReference;
	}

}

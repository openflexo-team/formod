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
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FormoseEditor;

/**
 * @author sylvain
 */
public class CreateNewGoalDiagram extends FMSAction<CreateNewGoalDiagram, FlexoConceptInstance, FlexoObject> {

	public static final FlexoActionFactory<CreateNewGoalDiagram, FlexoConceptInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<CreateNewGoalDiagram, FlexoConceptInstance, FlexoObject>(
			"create_new_goal_diagram", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		@Override
		public CreateNewGoalDiagram makeNewAction(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new CreateNewGoalDiagram(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FlexoConceptInstance elementMapping, final Vector<FlexoObject> globalSelection) {
			return elementMapping != null && elementMapping.getFlexoConcept() != null
					&& elementMapping.getFlexoConcept().getName().equals(FMSConstants.SYSML_KAOS_ELEMENT_MAPPING_CONCEPT_NAME);
		}

		@Override
		public boolean isEnabledForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(element, globalSelection);
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FlexoConceptInstance.class);
	}

	private String diagramTitle;
	private String diagramDescription;

	private FlexoConceptInstance elementMapping;

	public CreateNewGoalDiagram(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
			final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(FormoseEditor.class).getLoadedModuleInstance()
					.getLocales();
		}
		return super.getLocales();
	}

	private FlexoConceptInstance newDiagramMapping;

	@Override
	protected void doAction(final Object context) throws FlexoException {

		System.out.println("Create new goal diagram...");

		FlexoConceptInstance elementMapping = getElementMapping();
		System.out.println("elementMapping=" + elementMapping);

		try {
			newDiagramMapping = elementMapping.execute("this.createNewGoalDiagram({$title},{$description})", getDiagramTitle(),
					getDiagramDescription());
		} catch (Exception e) {
			throw new FlexoException(e);
		}

		/*FMLRTVirtualModelInstance methodology = element.getFlexoPropertyValue("applicableSysMLKaosMethodology");
		System.out.println("OK on doit afficher la methodology: " + methodology);
		FMLRTVirtualModelInstance skbgMethodology = methodology;
		System.out.println("skbgMethodology=" + skbgMethodology);
		
		ActionScheme as = (ActionScheme) skbgMethodology.getVirtualModel().getFlexoBehaviour("retrieveDiagramElementMapping",
				FlexoConceptInstanceType.getFlexoConceptInstanceType(element.getFlexoConcept()));
		System.out.println("execute " + as);
		ActionSchemeActionFactory at = new ActionSchemeActionFactory(as, skbgMethodology);
		ActionSchemeAction action = at.makeNewEmbeddedAction(skbgMethodology, null, this);
		FlexoBehaviourParameter elementParam = as.getParameters().get(0);
		action.setParameterValue(elementParam, element);
		action.doAction();
		
		System.out.println("Ca se passe comment: " + action.hasActionExecutionSucceeded());
		
		FlexoConceptInstance diagramElementMapping = (FlexoConceptInstance) action.getReturnedValue();
		
		System.out.println("diagramElementMapping=" + diagramElementMapping);
		
		if (diagramElementMapping != null) {
		
			FlexoConcept diagramElementMappingConcept = diagramElementMapping.getFlexoConcept();
		
			ActionScheme actionScheme = (ActionScheme) diagramElementMappingConcept.getFlexoBehaviour("refineUsingAgentAllocation");
			ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(actionScheme, diagramElementMapping);
		
			ActionSchemeAction action2 = actionType.makeNewEmbeddedAction(diagramElementMapping, null, this);
			action2.doAction();
		
			// getFocusedObject().getPropertyChangeSupport().firePropertyChange(FMSConstants.METHODOLOGY_ROLE_NAME, null,
			// getNewMethodology());
		
		}*/

	}

	public FlexoConceptInstance getElementMapping() {
		if (elementMapping == null) {
			return getFocusedObject();
		}
		return elementMapping;
	}

	public void setElementMapping(FlexoConceptInstance elementMapping) {
		if ((elementMapping == null && this.elementMapping != null)
				|| (elementMapping != null && !elementMapping.equals(this.elementMapping))) {
			FlexoConceptInstance oldValue = this.elementMapping;
			this.elementMapping = elementMapping;
			getPropertyChangeSupport().firePropertyChange("elementMapping", oldValue, elementMapping);
		}
	}

	public String getDiagramTitle() {
		return diagramTitle;
	}

	public void setDiagramTitle(String diagramTitle) {
		if ((diagramTitle == null && this.diagramTitle != null) || (diagramTitle != null && !diagramTitle.equals(this.diagramTitle))) {
			String oldValue = this.diagramTitle;
			this.diagramTitle = diagramTitle;
			getPropertyChangeSupport().firePropertyChange("diagramTitle", oldValue, diagramTitle);
		}
	}

	public String getDiagramDescription() {
		return diagramDescription;
	}

	public void setDiagramDescription(String diagramDescription) {
		if ((diagramDescription == null && this.diagramDescription != null)
				|| (diagramDescription != null && !diagramDescription.equals(this.diagramDescription))) {
			String oldValue = this.diagramDescription;
			this.diagramDescription = diagramDescription;
			getPropertyChangeSupport().firePropertyChange("diagramDescription", oldValue, diagramDescription);
		}
	}

	public FlexoConceptInstance getNewDiagramMapping() {
		return newDiagramMapping;
	}

}

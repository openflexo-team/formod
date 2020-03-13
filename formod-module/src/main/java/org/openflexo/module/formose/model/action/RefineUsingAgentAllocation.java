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

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.openflexo.ApplicationContext;
import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
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
public class RefineUsingAgentAllocation extends FMSAction<RefineUsingAgentAllocation, FlexoConceptInstance, FlexoObject> {

	public static final FlexoActionFactory<RefineUsingAgentAllocation, FlexoConceptInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<RefineUsingAgentAllocation, FlexoConceptInstance, FlexoObject>(
			"refine_using_agent_allocation", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		@Override
		public RefineUsingAgentAllocation makeNewAction(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new RefineUsingAgentAllocation(focusedObject, globalSelection, editor);
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

	private String description;

	private FlexoConceptInstance elementMapping;

	public RefineUsingAgentAllocation(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
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

	@Override
	protected void doAction(final Object context) throws FlexoException {

		System.out.println("Tiens, faudrait raffiner...");

		FlexoConceptInstance elementMapping = getElementMapping();
		System.out.println("elementMapping=" + elementMapping);

		try {
			elementMapping.execute("this.refineUsingAgentAllocation()");
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

	public FlexoConceptInstance getElement() {
		if (getElementMapping() != null) {
			try {
				return getElementMapping().execute("element");
			} catch (TypeMismatchException | NullReferenceException | InvocationTargetException | InvalidBindingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public void setElement(FlexoConceptInstance element) {
		System.out.println("Not implemented: setElement");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if ((description == null && this.description != null) || (description != null && !description.equals(this.description))) {
			String oldValue = this.description;
			this.description = description;
			getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
		}
	}

	/*public FMLRTVirtualModelInstance getFormoseVMI() {
		return (FMLRTVirtualModelInstance) getElementMapping().getOwningVirtualModelInstance();
	}*/

}

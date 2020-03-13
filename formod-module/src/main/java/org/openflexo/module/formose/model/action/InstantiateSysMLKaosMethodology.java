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
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FormoseEditor;

/**
 * @author sylvain
 */
public class InstantiateSysMLKaosMethodology extends FMSAction<InstantiateSysMLKaosMethodology, FlexoConceptInstance, FlexoObject> {

	public static final FlexoActionFactory<InstantiateSysMLKaosMethodology, FlexoConceptInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<InstantiateSysMLKaosMethodology, FlexoConceptInstance, FlexoObject>(
			"instantiate_sysml_kaos_methodology", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		@Override
		public InstantiateSysMLKaosMethodology makeNewAction(final FlexoConceptInstance focusedObject,
				final Vector<FlexoObject> globalSelection, final FlexoEditor editor) {
			return new InstantiateSysMLKaosMethodology(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return element != null && element.getFlexoConcept() != null
					&& element.getFlexoConcept().getName().equals(FMSConstants.ELEMENT_CONCEPT_NAME);
		}

		@Override
		public boolean isEnabledForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(element, globalSelection)
					&& element.getFlexoPropertyValue("applicableSysMLKaosMethodology") == null;
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FlexoConceptInstance.class);
	}

	private String description;

	private FlexoConceptInstance element;

	public InstantiateSysMLKaosMethodology(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
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

	// createSysMLKaosMethodology

	@Override
	protected void doAction(final Object context) throws FlexoException {

		FlexoConcept elementConcept = getFocusedObject().getFlexoConcept();

		ActionScheme actionScheme = (ActionScheme) elementConcept.getFlexoBehaviour("createSysMLKaosMethodology");
		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(actionScheme, getElement());

		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getElement(), null, this);
		/*action.setParameterValue(actionScheme.getParameter("name"), getName());
		if (getDescription() != null) {
			action.setParameterValue(actionScheme.getParameter("description"), getDescription());
		}*/
		action.doAction();

		newMethodology = (FMLRTVirtualModelInstance) action.getReturnedValue();

		getFocusedObject().getPropertyChangeSupport().firePropertyChange(FMSConstants.METHODOLOGY_ROLE_NAME, null, getNewMethodology());
	}

	private FMLRTVirtualModelInstance newMethodology;

	public FMLRTVirtualModelInstance getNewMethodology() {
		return newMethodology;
	}

	public FlexoConceptInstance getElement() {
		if (element == null) {
			return getFocusedObject();
		}
		return element;
	}

	public void setElement(FlexoConceptInstance element) {
		if ((element == null && this.element != null) || (element != null && !element.equals(this.element))) {
			FlexoConceptInstance oldValue = this.element;
			this.element = element;
			getPropertyChangeSupport().firePropertyChange("element", oldValue, element);
		}
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

	public FMLRTVirtualModelInstance getFormoseVMI() {
		return (FMLRTVirtualModelInstance) getElement().getOwningVirtualModelInstance();
	}

}

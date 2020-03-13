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
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FormoseEditor;

/**
 * @author sylvain
 */
public class CreateNewElement extends FMSAction<CreateNewElement, FlexoConceptInstance, FlexoObject> {

	public static final FlexoActionFactory<CreateNewElement, FlexoConceptInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<CreateNewElement, FlexoConceptInstance, FlexoObject>(
			"create_element", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		@Override
		public CreateNewElement makeNewAction(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new CreateNewElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return element != null && element.getFlexoConcept() != null
					&& element.getFlexoConcept().getName().equals(FMSConstants.ELEMENT_CONCEPT_NAME);
		}

		@Override
		public boolean isEnabledForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(element, globalSelection);
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FlexoConceptInstance.class);
	}

	private String name;
	private String description;

	private FlexoConceptInstance newElement;

	private boolean selectAfterCreation;

	public CreateNewElement(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection, final FlexoEditor editor) {
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

		FlexoConcept elementConcept = getFocusedObject().getFlexoConcept();

		ActionScheme createChildrenElementScheme = (ActionScheme) elementConcept.getFlexoBehaviour("createChildrenElement", String.class,
				String.class);
		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(createChildrenElementScheme, getFocusedObject());

		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject(), null, this);
		action.setParameterValue(createChildrenElementScheme.getParameter("elementIdentifier"), getName());
		if (getDescription() != null) {
			action.setParameterValue(createChildrenElementScheme.getParameter("elementDescription"), getDescription());
		}
		action.doAction();

		newElement = (FlexoConceptInstance) action.getReturnedValue();

		// getFocusedObject().getPropertyChangeSupport().firePropertyChange("childrenElements", false, true);
	}

	public FlexoConceptInstance getNewElement() {
		return newElement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if ((name == null && this.name != null) || (name != null && !name.equals(this.name))) {
			String oldValue = this.name;
			this.name = name;
			getPropertyChangeSupport().firePropertyChange("name", oldValue, name);
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

	public boolean getSelectAfterCreation() {
		return selectAfterCreation;
	}

	public void setSelectAfterCreation(boolean selectAfterCreation) {
		if (selectAfterCreation != this.selectAfterCreation) {
			this.selectAfterCreation = selectAfterCreation;
			getPropertyChangeSupport().firePropertyChange("selectAfterCreation", !selectAfterCreation, selectAfterCreation);
		}
	}
}

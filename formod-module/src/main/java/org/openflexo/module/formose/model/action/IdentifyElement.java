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
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.module.formose.model.FormoseProjectNature;

/**
 * @author sylvain
 */
public class IdentifyElement extends AbstractIdentifyTextFragment<IdentifyElement> {

	public static final FlexoActionFactory<IdentifyElement, FormoseProjectNature, FlexoObject> ACTION_TYPE = new FlexoActionFactory<IdentifyElement, FormoseProjectNature, FlexoObject>(
			"identify_element", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		@Override
		public IdentifyElement makeNewAction(final FormoseProjectNature focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new IdentifyElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FormoseProjectNature project, final Vector<FlexoObject> globalSelection) {
			return project != null;
		}

		@Override
		public boolean isEnabledForSelection(final FormoseProjectNature project, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(project, globalSelection);
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FormoseProjectNature.class);
	}

	private String name;
	private String description;
	private FlexoConceptInstance parentElement;

	private FlexoConceptInstance newIdentifiedElement;

	public IdentifyElement(final FormoseProjectNature focusedObject, final Vector<FlexoObject> globalSelection, final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(final Object context) throws FlexoException {

		FlexoConceptInstance docXReference = makeDocXReference();

		VirtualModel docAnnotationVM = getFocusedObject().getDocAnnotationMethodologyVirtualModel();
		ActionScheme identifyElement = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("identifyElement", String.class,
				String.class, getFocusedObject().getElementConcept().getInstanceType(),
				getFocusedObject().getReferenceConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(identifyElement,
				getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject().getDocAnnotationMethodologyVirtualModelInstance(),
				null, this);
		action.setParameterValue(identifyElement.getParameter("elementName"), getName());
		if (getDescription() != null) {
			action.setParameterValue(identifyElement.getParameter("elementDescription"), getDescription());
		}
		action.setParameterValue(identifyElement.getParameter("parentElement"), getParentElement());
		action.setParameterValue(identifyElement.getParameter("reference"), docXReference);
		action.doAction();

		newIdentifiedElement = (FlexoConceptInstance) action.getReturnedValue();

		System.out.println("newIdentifiedElement=" + newIdentifiedElement);

		getParentElement().getPropertyChangeSupport().firePropertyChange("childrenElements", null, newIdentifiedElement);

	}

	public FlexoConceptInstance getNewIdentifiedRequirement() {
		return null;
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

	public FlexoConceptInstance getParentElement() {
		return parentElement;
	}

	public void setParentElement(FlexoConceptInstance element) {
		if ((element == null && this.parentElement != null) || (element != null && !element.equals(this.parentElement))) {
			FlexoConceptInstance oldValue = this.parentElement;
			this.parentElement = element;
			getPropertyChangeSupport().firePropertyChange("parentElement", oldValue, element);
		}
	}

}

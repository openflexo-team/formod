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

import java.io.File;
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
public class CreateBusinessDomainModel extends FMSAction<CreateBusinessDomainModel, FlexoConceptInstance, FlexoObject> {

	public static final FlexoActionFactory<CreateBusinessDomainModel, FlexoConceptInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<CreateBusinessDomainModel, FlexoConceptInstance, FlexoObject>(
			"create_business_domain_model", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		@Override
		public CreateBusinessDomainModel makeNewAction(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new CreateBusinessDomainModel(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return (element != null && element.getFlexoConcept() != null
					&& element.getFlexoConcept().getName().equals(FMSConstants.ELEMENT_CONCEPT_NAME)
					&& !(Boolean) element.getFlexoPropertyValue("hasDomainModel"));
		}

		@Override
		public boolean isEnabledForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(element, globalSelection);
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FlexoConceptInstance.class);
	}

	private FlexoConceptInstance element;
	private String description;
	private File owlFile;
	private String choice;

	public CreateBusinessDomainModel(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
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

		System.out.println("On cree un modele de domaine");

		FlexoConcept elementConcept = getFocusedObject().getFlexoConcept();

		ActionScheme actionScheme = (ActionScheme) elementConcept.getFlexoBehaviour("createDomainModel");
		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(actionScheme, getFocusedObject());

		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject(), null, this);
		action.doAction();

		newDomainModelDiagram = (FMLRTVirtualModelInstance) action.getReturnedValue();

	}

	private FMLRTVirtualModelInstance newDomainModelDiagram;

	public FMLRTVirtualModelInstance getNewDomainModelDiagram() {
		return newDomainModelDiagram;
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

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		if ((choice == null && this.choice != null) || (choice != null && !choice.equals(this.choice))) {
			String oldValue = this.choice;
			this.choice = choice;
			getPropertyChangeSupport().firePropertyChange("choice", oldValue, choice);
		}
	}

	public File getOWLFile() {
		return owlFile;
	}

	public void setOWLFile(File owlFile) {
		if (owlFile != this.owlFile) {
			File oldValue = this.owlFile;
			this.owlFile = owlFile;
			getPropertyChangeSupport().firePropertyChange("owlFile", oldValue, owlFile);
		}
	}

	public FMLRTVirtualModelInstance getFormoseVMI() {
		return (FMLRTVirtualModelInstance) getElement().getOwningVirtualModelInstance();
	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		if (getElement() == null) {
			return false;
		}
		System.out.println("choice=" + getChoice());

		/*if (getDocXFile() == null || !(getDocXFile().exists())) {
			return false;
		}*/
		return true;
	}

}

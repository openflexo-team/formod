/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Fml-rt-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.module.formose.widget;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.fml.rt.controller.widget.FIBFlexoConceptInstanceSelector;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.module.formose.controller.FMSController;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.controller.FlexoController;

/**
 * Widget allowing to select an Element
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class ElementSelector extends FIBFlexoConceptInstanceSelector {

	static final Logger logger = Logger.getLogger(ElementSelector.class.getPackage().getName());

	public static Resource FIB_FILE = ResourceLocator.locateResource("Fib/ElementSelector.fib");

	public ElementSelector(FlexoConceptInstance editedObject) {
		super(editedObject);
	}

	@Override
	public Resource getFIBResource() {
		return FIB_FILE;
	}

	public FMLRTVirtualModelInstance getFormoseVMI() {
		return (FMLRTVirtualModelInstance) getVirtualModelInstance();
	}

	public void setFormoseVMI(FMLRTVirtualModelInstance formoseVMI) {
		if (formoseVMI != getFormoseVMI()) {
			FMLRTVirtualModelInstance oldValue = getFormoseVMI();
			setVirtualModelInstance(formoseVMI);
			getPropertyChangeSupport().firePropertyChange("formoseVMI", oldValue, formoseVMI);
		}
	}

	@Override
	public FMSController getFlexoController() {
		return (FMSController) super.getFlexoController();
	}

	@Override
	protected void initFIBComponent(FIBComponent component) {
		super.initFIBComponent(component);
		if (getFlexoController() != null) {
			component.setBindingFactory(new FMLFIBBindingFactory(getFlexoController().getFormoseNature().getFormoseViewPoint()));
		}
	}

	@Override
	protected SelectorDetailsPanel makeCustomPanel(FlexoConceptInstance editedObject) {
		SelectorDetailsPanel returned = super.makeCustomPanel(editedObject);
		if (getFlexoController() != null) {
			returned.getFIBComponent()
					.setBindingFactory(new FMLFIBBindingFactory(getFlexoController().getFormoseNature().getFormoseViewPoint()));
		}
		return returned;

	}

	@Override
	public void setFlexoController(FlexoController flexoController) {
		super.setFlexoController(flexoController);
		if (flexoController != null && getFIBComponent() != null) {
			getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(getFlexoController().getFormoseNature().getFormoseViewPoint()));
		}
	}

	@Override
	public Type getExpectedType() {
		if (getFlexoController() != null && getFlexoController().getFormoseNature() != null) {
			System.out.println("Element concept = " + getFlexoController().getFormoseNature().getElementConcept());
			return getFlexoController().getFormoseNature().getElementConcept().getInstanceType();
		}
		return super.getExpectedType();
	}
}

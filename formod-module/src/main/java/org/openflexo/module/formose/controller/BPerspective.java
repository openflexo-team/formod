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

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FMSIconLibrary;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.view.BMappingModuleView;
import org.openflexo.module.formose.view.BModuleView;
import org.openflexo.module.formose.widget.AbstractFormoseProjectBrowser;
import org.openflexo.module.formose.widget.BBrowser;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBFlexoConceptInstanceModuleView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;

/**
 * Perspective dedicated to B perspective
 * 
 * @author sylvain
 */
public class BPerspective extends FormosePerspective {

	public BPerspective(final FlexoController controller) {
		super("B", controller);
	}

	@Override
	public FMSController getController() {
		return (FMSController) super.getController();
	}

	@Override
	public ImageIcon getActiveIcon() {
		return FMSIconLibrary.B_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object) {

		if (object instanceof FormoseProjectNature) {
			return new BModuleView((FormoseProjectNature) object, getController(), this);
		}

		if (object instanceof FlexoConceptInstance) {

			FlexoConceptInstance fci = (FlexoConceptInstance) object;
			if (fci.isOf(FMSConstants.B_MAPPING_CONCEPT_NAME)) {
				return new BMappingModuleView(fci, this);
			}

			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {

				return new FMLControlledFIBFlexoConceptInstanceModuleView((FlexoConceptInstance) object, getController(), this,
						getController().getModuleLocales());
			}

		}

		// In all other cases...
		return super.createModuleViewForObject(object);

	}

	@Override
	public boolean hasModuleViewForObject(FlexoObject object) {

		if (object instanceof FormoseProjectNature) {
			return true;
		}

		/*else if (object instanceof FMLRTVirtualModelInstance) {
			// FML-controlled diagram
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				return true;
			}
			if (((FMLRTVirtualModelInstance) object).getVirtualModel().getName().equals(FMSConstants.DOMAIN_MODEL_VM_NAME)) {
				return true;
			}
			return false;
		}*/
		if (object instanceof FlexoConceptInstance) {

			if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals(FMSConstants.B_MAPPING_CONCEPT_NAME)) {
				return true;
			}
			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return true;
			}
		}
		return super.hasModuleViewForObject(object);
	}

	@Override
	protected AbstractFormoseProjectBrowser makeFormoseProjectBrowser() {
		return new BBrowser(getController());
	}
}

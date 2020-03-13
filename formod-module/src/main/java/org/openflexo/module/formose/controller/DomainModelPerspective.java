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

import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;

import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.view.DomainModelModuleView;
import org.openflexo.module.formose.view.DomainModellingModuleView;
import org.openflexo.module.formose.widget.DomainModelBrowser;
import org.openflexo.module.formose.widget.DomainModellingBrowser;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBFlexoConceptInstanceModuleView;
import org.openflexo.technologyadapter.owl.gui.OWLIconLibrary;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;

/**
 * Domain model perspective for Formose module
 * 
 * @author sylvain
 */
public class DomainModelPerspective extends FormosePerspective {

	private final DomainModelBrowser domainModelBrowser;

	public DomainModelPerspective(final FlexoController controller) {
		super("domain_model", controller);
		domainModelBrowser = new DomainModelBrowser(controller);
	}

	@Override
	public ImageIcon getActiveIcon() {
		return OWLIconLibrary.ONTOLOGY_ICON;
	}

	public void showDomainModelBrowser(FMLRTVirtualModelInstance domainModel) {
		setBottomLeftView(getDomainModelBrowser());
		getDomainModelBrowser().setDataObject(domainModel);
	}

	public void hideDomainModelBrowser() {
		setBottomLeftView(null);
	}

	public DomainModelBrowser getDomainModelBrowser() {
		return domainModelBrowser;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object) {

		if (object instanceof FormoseProjectNature) {
			return new DomainModellingModuleView((FormoseProjectNature) object, getController(), this);
		}

		if (object instanceof FlexoConceptInstance) {

			FlexoConceptInstance fci = (FlexoConceptInstance) object;
			if (fci.isOf(FMSConstants.DOMAIN_MODEL_MAPPING_CONCEPT_NAME)) {
				try {
					FMLRTVirtualModelInstance diagramVMI = fci.execute("defaultDiagram");
					if (diagramVMI != null && diagramVMI.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
						DiagramTechnologyAdapterController diagramTAC = ((FMSController) getController()).getDiagramTAC();
						FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(diagramVMI, false, getController(),
								diagramTAC.getToolFactory());
						return new DomainModelModuleView(editor, this);
					}
				} catch (TypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullReferenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidBindingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

		else if (object instanceof FMLRTVirtualModelInstance) {
			// FML-controlled diagram
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				return true;
			}
			if (((FMLRTVirtualModelInstance) object).getVirtualModel().getName().equals(FMSConstants.DOMAIN_MODEL_VM_NAME)) {
				return true;
			}
			return false;
		}
		if (object instanceof FlexoConceptInstance) {

			if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals(FMSConstants.DOMAIN_MODEL_MAPPING_CONCEPT_NAME)) {
				return true;
			}
			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return true;
			}
		}
		return super.hasModuleViewForObject(object);
	}

	@Override
	protected DomainModellingBrowser makeFormoseProjectBrowser() {
		return new DomainModellingBrowser(getController());
	}

}

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

import javax.swing.JPanel;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.module.FlexoModule.WelcomePanel;
import org.openflexo.module.formose.FMSModule;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.view.ConvertToFormoseProjectView;
import org.openflexo.module.formose.view.FormoseProjectNatureModuleView;
import org.openflexo.module.formose.view.FormoseWelcomePanelModuleView;
import org.openflexo.module.formose.widget.AbstractFormoseProjectBrowser;
import org.openflexo.module.formose.widget.GenericProjectBrowser;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBFlexoConceptInstanceModuleView;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBVirtualModelInstanceModuleView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.NaturePerspective;

/**
 * Base implementation for a perspective for Formose module
 * 
 * @author sylvain
 */
public abstract class FormosePerspective extends NaturePerspective<FormoseProjectNature> {

	private AbstractFormoseProjectBrowser projectBrowser;
	private GenericProjectBrowser genericBrowser;

	public FormosePerspective(String name, final FlexoController controller) {
		super(name, controller);
		if (controller.getProject() != null) {
			setProject(controller.getProject());
		}
	}

	@Override
	public Class<FormoseProjectNature> getNatureClass() {
		return FormoseProjectNature.class;
	}

	@Override
	public void willShow() {
		super.willShow();
		updateBrowser(getController().getProject(), false);
	}

	protected abstract AbstractFormoseProjectBrowser makeFormoseProjectBrowser();

	public void setProject(final FlexoProject<?> project) {

		updateBrowser(project, true);
	}

	public void updateBrowser(final FlexoProject<?> project, boolean rebuildBrowser) {

		System.out.println("updating browser " + projectBrowser + " with " + project);

		if (project != null) {
			if (project.hasNature(FormoseProjectNature.class)) {
				if (projectBrowser == null || rebuildBrowser || projectBrowser.getDataObject().getProject() != project) {
					projectBrowser = makeFormoseProjectBrowser();
				}
				System.out.println("Hop in " + this + " projectBrowser=" + projectBrowser);
				if (projectBrowser != null) {
					projectBrowser.setRootObject(project.getNature(FormoseProjectNature.class));
				}
				setTopLeftView(projectBrowser);
			}
			else {
				if (genericBrowser == null || rebuildBrowser || genericBrowser.getDataObject() != project) {
					genericBrowser = new GenericProjectBrowser(getController());
				}
				if (genericBrowser != null) {
					genericBrowser.setRootObject(project);
				}
				setTopLeftView(genericBrowser);
			}
		}
		else {
			setTopLeftView(new JPanel());
		}
	}

	public AbstractFormoseProjectBrowser getProjectBrowser() {
		return projectBrowser;
	}

	public GenericProjectBrowser getGenericBrowser() {
		return genericBrowser;
	}

	@Override
	public String getWindowTitleforObject(final FlexoObject object, final FlexoController controller) {
		if (object instanceof WelcomePanel) {
			return "Welcome";
		}
		if (object instanceof FlexoProject) {
			return ((FlexoProject<?>) object).getProjectName();
		}
		if (object instanceof FormoseProjectNature) {
			if (((FormoseProjectNature) object).getOwner() != null) {
				return ((FormoseProjectNature) object).getOwner().getProjectName();
			}
			return null;
		}
		else if (object instanceof FMLRTVirtualModelInstance) {
			return ((FMLRTVirtualModelInstance) object).getTitle();
		}
		else if (object instanceof FlexoConceptInstance) {
			return ((FlexoConceptInstance) object).getStringRepresentation();
		}
		else {
			return "Object has no title";
		}
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object) {

		if (object instanceof WelcomePanel) {
			return new FormoseWelcomePanelModuleView((WelcomePanel<FMSModule>) object, getController(), this);
		}
		if (object instanceof FlexoProject) {
			return new ConvertToFormoseProjectView((FlexoProject<?>) object, getController(), this);
		}
		if (object instanceof FormoseProjectNature) {
			/*return new FMLControlledFIBVirtualModelInstanceModuleView(((FormoseProjectNature) object).getFormoseView(), getController(), this,
					getController().getModuleLocales());*/
			return new FormoseProjectNatureModuleView((FormoseProjectNature) object, getController(), this);
		}

		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
				return new FMLControlledFIBVirtualModelInstanceModuleView((FMLRTVirtualModelInstance) object, getController(), this,
						getController().getModuleLocales());
			}
		}
		if (object instanceof FlexoConceptInstance) {
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
		if (object instanceof WelcomePanel) {
			return true;
		}
		if (object instanceof FlexoProject) {
			return true;
		}
		if (object instanceof FormoseProjectNature) {
			return true;
		}
		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
				return true;
			}
			return false;
		}
		if (object instanceof FlexoConceptInstance) {

			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return true;
			}
		}
		return super.hasModuleViewForObject(object);
	}

	@Override
	public FlexoObject getDefaultObject(FlexoObject proposedObject) {
		if (proposedObject instanceof FlexoProject && ((FlexoProject<?>) proposedObject).hasNature(FormoseProjectNature.class)) {
			return ((FlexoProject<?>) proposedObject).getNature(FormoseProjectNature.class);
		}

		if (hasModuleViewForObject(proposedObject)) {
			return proposedObject;
		}

		if (getController().getProject() != null && getController().getProject().hasNature(FormoseProjectNature.class)) {
			return getController().getProject().getNature(FormoseProjectNature.class);
		}

		return super.getDefaultObject(proposedObject);
	}

}

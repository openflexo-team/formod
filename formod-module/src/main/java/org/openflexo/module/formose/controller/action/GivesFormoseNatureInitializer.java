/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
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

package org.openflexo.module.formose.controller.action;

import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.task.Progress;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.module.formose.FMSIconLibrary;
import org.openflexo.module.formose.controller.FormosePerspective;
import org.openflexo.module.formose.model.action.GivesFormoseNature;
import org.openflexo.module.formose.view.ConvertToFormoseProjectView;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class GivesFormoseNatureInitializer extends ActionInitializer<GivesFormoseNature, FlexoProject<?>, FlexoObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	GivesFormoseNatureInitializer(FMSControllerActionInitializer actionInitializer) {
		super(GivesFormoseNature.actionType, actionInitializer);
	}

	@Override
	protected FMSControllerActionInitializer getControllerActionInitializer() {
		return (FMSControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionRunnable<GivesFormoseNature, FlexoProject<?>, FlexoObject> getDefaultInitializer() {
		return (e, action) -> {
			Progress.forceHideTaskBar();
			Wizard wizard = new GivesFormoseNatureWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			Progress.stopForceHideTaskBar();
			if (dialog.getStatus() != Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;
		};
	}

	@Override
	protected FlexoActionRunnable<GivesFormoseNature, FlexoProject<?>, FlexoObject> getDefaultFinalizer() {
		return (e, action) -> {

			// Fixed FOR-44: prevent left browser disappearing
			if (getController().getCurrentPerspective() instanceof FormosePerspective) {
				((FormosePerspective) getController().getCurrentPerspective()).updateBrowser(getProject(), true);
			}

			// We store the eventual ModuleView to remove, but we must remove it AFTER selection of new object
			// Otherwise, focus on FlexoProject will be lost
			ConvertToFormoseProjectView viewToRemove = null;
			if (getController().getCurrentModuleView() instanceof ConvertToFormoseProjectView) {
				viewToRemove = (ConvertToFormoseProjectView) getController().getCurrentModuleView();
			}
			getController().selectAndFocusObject(action.getNewNature());
			if (viewToRemove != null) {
				viewToRemove.deleteModuleView();
			}
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory actionType) {
		return FMSIconLibrary.FMS_SMALL_ICON;
	}

}

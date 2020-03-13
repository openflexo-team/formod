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

package org.openflexo.module.formose.controller.action;

import javax.swing.Icon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.model.action.IdentifyTextFragment;
import org.openflexo.module.formose.view.DocXRequirementModuleView;
import org.openflexo.module.formose.widget.ReferencesBrowser;
import org.openflexo.technologyadapter.docx.gui.DocXIconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

/**
 * @author sylvain
 */
public class IdentifyTextFragmentInitializer extends ActionInitializer<IdentifyTextFragment, FormoseProjectNature, FlexoObject> {

	public IdentifyTextFragmentInitializer(final ControllerActionInitializer controllerActionInitializer) {
		super(IdentifyTextFragment.ACTION_TYPE, controllerActionInitializer);
	}

	@Override
	protected FlexoActionRunnable<IdentifyTextFragment, FormoseProjectNature, FlexoObject> getDefaultInitializer() {
		return (e, action) -> {
			if (getController().getCurrentModuleView() instanceof DocXRequirementModuleView) {
				DocXRequirementModuleView moduleView = (DocXRequirementModuleView) getController().getCurrentModuleView();
				if (moduleView.getDocXEditor().getTextSelection() != null) {
					action.setTextSelection(moduleView.getDocXEditor().getTextSelection());
					action.setFMLControlledDocumentVMI(moduleView.getVirtualModelInstance());
				}
				action.setFocusedElement(moduleView.getPerspective().getReferencesBrowser().getFIBController().getFocusedElement());
				action.setFocusedRequirement(moduleView.getPerspective().getReferencesBrowser().getFIBController().getFocusedRequirement());
			}

			return true;
		};
	}

	@Override
	protected FlexoActionRunnable<IdentifyTextFragment, FormoseProjectNature, FlexoObject> getDefaultFinalizer() {
		return (e, action) -> {
			if (getController().getCurrentModuleView() instanceof DocXRequirementModuleView) {
				DocXRequirementModuleView moduleView = (DocXRequirementModuleView) getController().getCurrentModuleView();
				ReferencesBrowser referencesBowser = moduleView.getPerspective().getReferencesBrowser();
				// action.getFormoseProject().getUnclassifiedReferences().updateUnclassifiedReferences();
				referencesBowser.getFIBBrowserWidget().getBrowserModel().fireTreeRestructured();
			}
			getController().getSelectionManager().setSelectedObject(action.getNewReference());
			// getController().selectAndFocusObject(action.getNewIdentifiedFragment());
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory actionType) {
		return IconFactory.getImageIcon(DocXIconLibrary.FRAGMENT_ICON, IconLibrary.NEW_MARKER);
	}

}
